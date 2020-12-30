package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CustomerGoodsMapper;
import com.jayud.mall.model.bo.CustomerGoodsForm;
import com.jayud.mall.model.bo.QueryCustomerGoodsForm;
import com.jayud.mall.model.po.CustomerGoods;
import com.jayud.mall.model.vo.CustomerGoodsVO;
import com.jayud.mall.service.ICustomerGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CommonResult auditCustomerGoods(CustomerGoodsForm form) {
        CustomerGoods customerGoods = ConvertUtil.convert(form, CustomerGoods.class);
        this.saveOrUpdate(customerGoods);
        return CommonResult.success("审核成功!");
    }

    @Override
    public CustomerGoodsVO saveCustomerGoods(CustomerGoodsForm form) {
        CustomerGoods customerGoods = ConvertUtil.convert(form, CustomerGoods.class);
        this.saveOrUpdate(customerGoods);
        CustomerGoodsVO customerGoodsVO = ConvertUtil.convert(customerGoods, CustomerGoodsVO.class);
        return customerGoodsVO;
    }

    @Override
    public void batchSaveCustomerGoods(List<CustomerGoodsVO> list) {
        list.forEach(customerGoodsVO -> {
            String typesName = customerGoodsVO.getTypesName();
            customerGoodsVO.setCustomerId(1);//客户ID(customer id)
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
}
