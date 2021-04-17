package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.CustomerGoodsEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CustomerGoodsMapper;
import com.jayud.mall.model.bo.CustomerGoodsAuditForm;
import com.jayud.mall.model.bo.CustomerGoodsForm;
import com.jayud.mall.model.bo.QueryCustomerGoodsForm;
import com.jayud.mall.model.po.CustomerGoods;
import com.jayud.mall.model.vo.CustomerGoodsVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ICustomerGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 客户商品表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class CustomerGoodsServiceImpl extends ServiceImpl<CustomerGoodsMapper, CustomerGoods> implements ICustomerGoodsService {

    @Autowired
    CustomerGoodsMapper customerGoodsMapper;
    @Autowired
    BaseService baseService;


    @Override
    public IPage<CustomerGoodsVO> findCustomerGoodsByPage(QueryCustomerGoodsForm form) {
        //定义分页参数
        Page<CustomerGoodsVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("t.id"));
        IPage<CustomerGoodsVO> pageInfo = customerGoodsMapper.findCustomerGoodsByPage(page, form);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult auditCustomerGoods(CustomerGoodsAuditForm form) {
        List<Integer> ids = form.getIds();
        Integer status = form.getStatus();
        String statusName = CustomerGoodsEnum.getName(status);
        String dataCode = form.getDataCode();
        String clearanceCode = form.getClearanceCode();
        List<CustomerGoods> customerGoodsList = new ArrayList<>();
        ids.forEach(id -> {
            CustomerGoods customerGoods = new CustomerGoods();
            customerGoods.setId(id);
            customerGoods.setStatusName(statusName);
            customerGoods.setDataCode(dataCode);
            customerGoods.setClearanceCode(clearanceCode);
            customerGoodsList.add(customerGoods);
        });
        this.saveOrUpdateBatch(customerGoodsList);
        return CommonResult.success("审核成功!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerGoodsVO saveCustomerGoods(CustomerGoodsForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        CustomerGoods customerGoods = ConvertUtil.convert(form, CustomerGoods.class);
        customerGoods.setCustomerId(customerUser.getId());
        this.saveOrUpdate(customerGoods);
        CustomerGoodsVO customerGoodsVO = ConvertUtil.convert(customerGoods, CustomerGoodsVO.class);
        return customerGoodsVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveCustomerGoods(List<CustomerGoodsVO> list) {
        CustomerUser customerUser = baseService.getCustomerUser();
        list.forEach(customerGoodsVO -> {
            String typesName = customerGoodsVO.getTypesName();
            customerGoodsVO.setCustomerId(customerUser.getId());//客户ID(customer id)
            customerGoodsVO.setStatus(0);//审核状态代码：1-审核通过，0-等待审核，-1-审核不通过
            if(typesName != null && typesName.equalsIgnoreCase("普货")){
                customerGoodsVO.setTypes(1);
            }else if(typesName != null && typesName.equalsIgnoreCase("特货")){
                customerGoodsVO.setTypes(2);
            }
        });
        List<CustomerGoods> customerGoods = ConvertUtil.convertList(list, CustomerGoods.class);
        this.saveOrUpdateBatch(customerGoods);
    }

    @Override
    public CommonResult<CustomerGoodsVO> findCustomerGoodsById(Integer id) {
        CustomerGoodsVO customerGoodsVO = customerGoodsMapper.findCustomerGoodsById(id);
        if(ObjectUtil.isEmpty(customerGoodsVO)){
            return CommonResult.error(-1, "没有找到商品");
        }
        return CommonResult.success(customerGoodsVO);
    }
}
