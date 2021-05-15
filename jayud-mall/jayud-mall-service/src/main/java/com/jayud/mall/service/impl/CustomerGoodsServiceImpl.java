package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.CustomerGoodsEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CustomerGoodsMapper;
import com.jayud.mall.mapper.GoodsServiceCostMapper;
import com.jayud.mall.mapper.ServiceGroupMapper;
import com.jayud.mall.model.bo.CustomerGoodsAuditForm;
import com.jayud.mall.model.bo.CustomerGoodsForm;
import com.jayud.mall.model.bo.QueryCustomerGoodsForm;
import com.jayud.mall.model.bo.ServiceGroupForm;
import com.jayud.mall.model.po.CustomerGoods;
import com.jayud.mall.model.po.GoodsServiceCost;
import com.jayud.mall.model.vo.CustomerGoodsVO;
import com.jayud.mall.model.vo.ServiceGroupVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ICustomerGoodsService;
import com.jayud.mall.service.IGoodsServiceCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    ServiceGroupMapper serviceGroupMapper;
    @Autowired
    GoodsServiceCostMapper goodsServiceCostMapper;
    @Autowired
    IGoodsServiceCostService goodsServiceCostService;
    @Autowired
    BaseService baseService;



    @Override
    public IPage<CustomerGoodsVO> findCustomerGoodsByPage(QueryCustomerGoodsForm form) {
        //定义分页参数
        Page<CustomerGoodsVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("t.id"));

        Long offerInfoId = ObjectUtil.isEmpty(form.getOfferInfoId()) ? 0 : form.getOfferInfoId();
        form.setOfferInfoId(offerInfoId);
        IPage<CustomerGoodsVO> pageInfo = customerGoodsMapper.findCustomerGoodsByPage(page, form);

        List<CustomerGoodsVO> records = pageInfo.getRecords();
        records.forEach(customerGoodsVO -> {
            String isNeedFee = customerGoodsVO.getIsNeedFee();//是否需要附加费(1需要 2不需要)
            String serviceUnitPrice = customerGoodsVO.getServiceUnitPrice();
            if(ObjectUtil.isNotEmpty(isNeedFee)){
                if(isNeedFee.equals("2")){
                    serviceUnitPrice = "无附加费";
                }else{
                    if(ObjectUtil.isEmpty(serviceUnitPrice)){
                        serviceUnitPrice = "附加费待定";
                    }
                }
            }else{
                serviceUnitPrice = "附加费待定";
            }
            customerGoodsVO.setServiceUnitPrice(serviceUnitPrice);
        });

        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult auditCustomerGoods(CustomerGoodsAuditForm form) {
        //查询商品list  list 转 map
        List<CustomerGoodsVO> customerGoodsVOList = customerGoodsMapper.findCustomerGoodsByIds(form.getIds());
        Map<Integer, CustomerGoodsVO> customerGoodsMap = customerGoodsVOList.stream().collect(Collectors.toMap(CustomerGoodsVO::getId, Function.identity()));
        //查询服务list list 转 map
        ServiceGroupForm serviceGroupForm = new ServiceGroupForm();
        List<ServiceGroupVO> serviceGroupList = serviceGroupMapper.findServiceGroup(serviceGroupForm);
        Map<Long, ServiceGroupVO> serviceGroupMap = serviceGroupList.stream().collect(Collectors.toMap(ServiceGroupVO::getId, Function.identity()));

        List<Integer> ids = form.getIds();
        Integer status = form.getStatus();
        String statusName = CustomerGoodsEnum.getName(status);
        String dataCode = form.getDataCode();
        String clearanceCode = form.getClearanceCode();
        List<CustomerGoods> customerGoodsList = new ArrayList<>();//商品list
        List<GoodsServiceCost> goodsServiceCostList = new ArrayList<>();//商品服务费用list
        ids.forEach(id -> {
            CustomerGoodsVO customerGoodsVO = customerGoodsMap.get(id);

            CustomerGoods customerGoods = new CustomerGoods();
            customerGoods.setId(id);
            customerGoods.setStatus(status);
            customerGoods.setStatusName(statusName);
            customerGoods.setDataCode(dataCode);
            customerGoods.setClearanceCode(clearanceCode);
            customerGoodsList.add(customerGoods);

            List<GoodsServiceCost> goodsServiceCostList1 = form.getGoodsServiceCostList();
            if(CollUtil.isNotEmpty(goodsServiceCostList1)){
                List<GoodsServiceCost> goodsServiceCosts = new ArrayList<>();
                goodsServiceCostList1.forEach(goodsServiceCost -> {

                    if(ObjectUtil.isNotEmpty(goodsServiceCost.getServiceId())){
                        Long serviceId = goodsServiceCost.getServiceId();
                        ServiceGroupVO serviceGroupVO = serviceGroupMap.get(serviceId);

                        goodsServiceCost.setGoodId(Long.valueOf(id));
                        goodsServiceCost.setCustomerId(Long.valueOf(customerGoodsVO.getCustomerId()));
                        goodsServiceCost.setNameCn(customerGoodsVO.getNameCn());
                        goodsServiceCost.setServiceName(serviceGroupVO.getCodeName());
                        goodsServiceCost.setCustomerName(customerGoodsVO.getCustomerName());

                        goodsServiceCosts.add(goodsServiceCost);
                    }

                });

                goodsServiceCostList.addAll(goodsServiceCosts);
            }


        });
        //1.批量保存，商品
        this.saveOrUpdateBatch(customerGoodsList);
        //2.批量保存，商品服务费用
        QueryWrapper<GoodsServiceCost> qw = new QueryWrapper<>();
        qw.in("good_id", ids);
        goodsServiceCostService.remove(qw);
        if(CollUtil.isNotEmpty(goodsServiceCostList)){
            goodsServiceCostService.saveOrUpdateBatch(goodsServiceCostList);
        }
        return CommonResult.success("审核成功!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerGoodsVO saveCustomerGoods(CustomerGoodsForm form) {
        Integer customerId = form.getCustomerId();
        CustomerGoods customerGoods = ConvertUtil.convert(form, CustomerGoods.class);
        if(ObjectUtil.isEmpty(customerId)){
            CustomerUser customerUser = baseService.getCustomerUser();
            customerGoods.setCustomerId(customerUser.getId());
        }
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

    @Override
    public CustomerGoodsVO findCustomerGoodsCostById(Integer id) {
        CustomerGoodsVO customerGoodsVO = customerGoodsMapper.findCustomerGoodsById(id);
        Long goodId = Long.valueOf(id);
        List<GoodsServiceCost> goodsServiceCostList = goodsServiceCostMapper.findGoodsServiceCostByGoodId(goodId);
        customerGoodsVO.setGoodsServiceCostList(goodsServiceCostList);
        return customerGoodsVO;
    }
}
