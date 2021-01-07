package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OceanCounterMapper;
import com.jayud.mall.mapper.OrderConfMapper;
import com.jayud.mall.model.bo.OrderConfForm;
import com.jayud.mall.model.bo.QueryOrderConfForm;
import com.jayud.mall.model.po.OceanConfDetail;
import com.jayud.mall.model.po.OrderConf;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.model.vo.OceanCounterVO;
import com.jayud.mall.model.vo.OfferInfoVO;
import com.jayud.mall.model.vo.OrderConfVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IOceanConfDetailService;
import com.jayud.mall.service.IOrderConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 配载单 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class OrderConfServiceImpl extends ServiceImpl<OrderConfMapper, OrderConf> implements IOrderConfService {

    @Autowired
    OrderConfMapper orderConfMapper;

    @Autowired
    IOceanConfDetailService oceanConfDetailService;

    @Autowired
    BaseService baseService;

    @Autowired
    OceanCounterMapper oceanCounterMapper;

    @Override
    public IPage<OrderConfVO> findOrderConfByPage(QueryOrderConfForm form) {
        //定义分页参数
        Page<OrderConfVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.create_time"));
        IPage<OrderConfVO> orderConfByPage = orderConfMapper.findOrderConfByPage(page, form);
        IPage<OrderConfVO> pageInfo = orderConfByPage;
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderConf(OrderConfForm form) {
        OrderConf orderConf = ConvertUtil.convert(form, OrderConf.class);
        Long id = orderConf.getId();
        if(id == null){
            AuthUser user = baseService.getUser();
            orderConf.setStatus("1");//状态(0无效 1有效)
            orderConf.setUserId(user.getId().intValue());
            orderConf.setUserName(user.getName());
            orderConf.setCreateTime(LocalDateTime.now());
        }
        //1.保存配载单
        this.saveOrUpdate(orderConf);
        //配载单id
        Long orderId = orderConf.getId();

        //2.先删除配载的报价和提单
        QueryWrapper<OceanConfDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        oceanConfDetailService.remove(queryWrapper);
        //配载的报价
        List<OceanConfDetail> offerInfoDetailList = form.getOfferInfoDetailList();
        offerInfoDetailList.forEach(oceanConfDetail -> {
            oceanConfDetail.setOrderId(orderId);
            oceanConfDetail.setTypes(1);//1报价 2提单
            oceanConfDetail.setStatus("1");//状态(0无效 1有效)
        });
        //配载的提单
        List<OceanConfDetail> oceanBillDetailList = form.getOceanBillDetailList();
        oceanBillDetailList.forEach(oceanConfDetail -> {
            oceanConfDetail.setOrderId(orderId);
            oceanConfDetail.setTypes(2);//1报价 2提单
            oceanConfDetail.setStatus("1");//状态(0无效 1有效)
        });
        List<OceanConfDetail> oceanConfDetails = new ArrayList<>();
        oceanConfDetails.addAll(offerInfoDetailList);
        oceanConfDetails.addAll(oceanBillDetailList);
        //3.保存配载的报价和提单
        oceanConfDetailService.saveOrUpdateBatch(oceanConfDetails);
    }

    @Override
    public CommonResult<OrderConfVO> lookOrderConf(Long id) {
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(id);

        Long orderId = orderConfVO.getId();
        //报价信息list
        List<OfferInfoVO> offerInfoVOList = orderConfMapper.findOfferInfoVOByOrderId(orderId);
        orderConfVO.setOfferInfoVOList(offerInfoVOList);
        //提单信息list
        List<OceanBillVO> oceanBillVOList = orderConfMapper.findOceanBillVOByOrderId(orderId);

        oceanBillVOList.forEach(oceanBillVO -> {
            Long obId = oceanBillVO.getId();
            List<OceanCounterVO> oceanCounterVOList = oceanCounterMapper.findOceanCounterVOByObId(obId);
            oceanBillVO.setOceanCounterVOList(oceanCounterVOList);
        });
        orderConfVO.setOceanBillVOList(oceanBillVOList);

        return CommonResult.success(orderConfVO);
    }


}
