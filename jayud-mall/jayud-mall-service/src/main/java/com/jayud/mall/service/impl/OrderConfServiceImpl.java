package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderConfStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.OceanConfDetail;
import com.jayud.mall.model.po.OrderConf;
import com.jayud.mall.model.vo.*;
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
    OceanCounterMapper oceanCounterMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    CounterCaseInfoMapper counterCaseInfoMapper;
    @Autowired
    OrderCaseMapper orderCaseMapper;


    @Autowired
    BaseService baseService;
    @Autowired
    IOceanConfDetailService oceanConfDetailService;


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
    public OrderConfVO saveOrderConf(OrderConfForm form) {
        OrderConf orderConf = ConvertUtil.convert(form, OrderConf.class);
        Long id = orderConf.getId();
        if(id == null){
            //新增
            AuthUser user = baseService.getUser();
            if(ObjectUtil.isEmpty(user)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
            }
            orderConf.setStatusCode(OrderConfStatusEnum.PREPARE.getCode());
            orderConf.setStatusName(OrderConfStatusEnum.PREPARE.getName());
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
        return null;
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

    @Override
    public OrderConfVO saveOrderConfByOfferInfo(OrderConfForm form) {
        OrderConf orderConf = ConvertUtil.convert(form, OrderConf.class);
        Long id = orderConf.getId();
        if(id == null){
            AuthUser user = baseService.getUser();
            if(ObjectUtil.isEmpty(user)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
            }
            orderConf.setStatusCode(OrderConfStatusEnum.PREPARE.getCode());
            orderConf.setStatusName(OrderConfStatusEnum.PREPARE.getName());
            orderConf.setUserId(user.getId().intValue());
            orderConf.setUserName(user.getName());
            orderConf.setCreateTime(LocalDateTime.now());
        }
        //1.保存配载单
        this.saveOrUpdate(orderConf);
        //配载单id
        Long orderId = orderConf.getId();


        //2.先删除配载的报价
        QueryWrapper<OceanConfDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.eq("types", 1);
        oceanConfDetailService.remove(queryWrapper);
        //配载的报价
        List<OceanConfDetail> offerInfoDetailList = form.getOfferInfoDetailList();
        offerInfoDetailList.forEach(oceanConfDetail -> {
            oceanConfDetail.setOrderId(orderId);
            oceanConfDetail.setTypes(1);//1报价 2提单
            oceanConfDetail.setStatus("1");//状态(0无效 1有效)
        });
        //3.保存配载的报价
        oceanConfDetailService.saveOrUpdateBatch(offerInfoDetailList);

        OrderConfVO orderConfVO = ConvertUtil.convert(orderConf, OrderConfVO.class);
        return orderConfVO;
    }

    @Override
    public OrderConfVO saveOrderConfByOceanBill(OrderConfForm form) {
        OrderConf orderConf = ConvertUtil.convert(form, OrderConf.class);
        Long id = orderConf.getId();
        if(id == null){
            AuthUser user = baseService.getUser();
            if(ObjectUtil.isEmpty(user)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
            }
            orderConf.setStatusCode(OrderConfStatusEnum.PREPARE.getCode());
            orderConf.setStatusName(OrderConfStatusEnum.PREPARE.getName());
            orderConf.setUserId(user.getId().intValue());
            orderConf.setUserName(user.getName());
            orderConf.setCreateTime(LocalDateTime.now());
        }
        //1.保存配载单
        this.saveOrUpdate(orderConf);
        //配载单id
        Long orderId = orderConf.getId();

        //2.先删除配载的提单
        QueryWrapper<OceanConfDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.eq("types", 2);
        oceanConfDetailService.remove(queryWrapper);

        //配载的提单
        List<OceanConfDetail> oceanBillDetailList = form.getOceanBillDetailList();
        oceanBillDetailList.forEach(oceanConfDetail -> {
            oceanConfDetail.setOrderId(orderId);
            oceanConfDetail.setTypes(2);//1报价 2提单
            oceanConfDetail.setStatus("1");//状态(0无效 1有效)
        });
        //3.保存配载的提单
        oceanConfDetailService.saveOrUpdateBatch(oceanBillDetailList);

        OrderConfVO orderConfVO = ConvertUtil.convert(orderConf, OrderConfVO.class);
        return orderConfVO;
    }

    @Override
    public OrderConfVO findOrderConfById(Long id) {
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(id);
        Long confId = orderConfVO.getId();//配载单id
        //报价信息list
        List<OfferInfoVO> offerInfoVOList = orderConfMapper.findOfferInfoVOByOrderId(confId);
        orderConfVO.setOfferInfoVOList(offerInfoVOList);
        //提单信息list
        List<OceanBillVO> oceanBillVOList = orderConfMapper.findOceanBillVOByOrderId(confId);
        oceanBillVOList.forEach(oceanBillVO -> {
            Long obId = oceanBillVO.getId();
            List<OceanCounterVO> oceanCounterVOList = oceanCounterMapper.findOceanCounterVOByObId(obId);
            oceanBillVO.setOceanCounterVOList(oceanCounterVOList);
        });
        orderConfVO.setOceanBillVOList(oceanBillVOList);
        //运单(订单)信息list
        List<OrderInfoVO> orderInfoVOList = orderInfoMapper.findOrderInfoByConfId(confId);
        orderInfoVOList.forEach(ororderInfoVO -> {
            Long orderId = ororderInfoVO.getId();
            List<String> confinfos = orderInfoMapper.findOrderConfInfoByOrderId(orderId);
            if(CollUtil.isNotEmpty(confinfos)){
                String confInfo = "";
                for (int i=0; i<confinfos.size(); i++){
                    if(ObjectUtil.isNotEmpty(confinfos.get(i))){
                        confInfo += confinfos.get(i);
                    }
                }
                ororderInfoVO.setConfInfo(confInfo);
            }
        });
        orderConfVO.setOrderInfoVOList(orderInfoVOList);
        return orderConfVO;
    }

    @Override
    public OrderConfVO addOrderConf(OrderConfForm form) {
        OrderConf orderConf = ConvertUtil.convert(form, OrderConf.class);
        Long id = orderConf.getId();
        if(id == null){
            AuthUser user = baseService.getUser();
            if(ObjectUtil.isEmpty(user)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
            }
            orderConf.setStatusCode(OrderConfStatusEnum.PREPARE.getCode());
            orderConf.setStatusName(OrderConfStatusEnum.PREPARE.getName());
            orderConf.setUserId(user.getId().intValue());
            orderConf.setUserName(user.getName());
            orderConf.setCreateTime(LocalDateTime.now());
        }
        //1.保存配载单
        this.saveOrUpdate(orderConf);
        OrderConfVO orderConfVO = ConvertUtil.convert(orderConf, OrderConfVO.class);
        return orderConfVO;
    }

    @Override
    public List<OceanBillVO> findOceanBillByConfId(Long id) {
        //提单信息list
        List<OceanBillVO> oceanBillVOList = orderConfMapper.findOceanBillVOByOrderId(id);
        return oceanBillVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableStatus(OrderConfIdForm form) {
        Long id = form.getId();
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(id);
        if(ObjectUtil.isEmpty(orderConfVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "沒有找到配载单");
        }
        if(!orderConfVO.getStatusCode().equals(OrderConfStatusEnum.PREPARE.getCode())){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "配载单状态不正确，不能操作");
        }
        OrderConf orderConf = ConvertUtil.convert(orderConfVO, OrderConf.class);
        orderConf.setStatusCode(OrderConfStatusEnum.ENABLE.getCode());
        orderConf.setStatusName(OrderConfStatusEnum.ENABLE.getName());
        this.saveOrUpdate(orderConf);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelStatus(OrderConfIdForm form) {
        Long id = form.getId();
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(id);
        if(ObjectUtil.isEmpty(orderConfVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "沒有找到配载单");
        }
//        if(orderConfVO.getStatusCode().equals(OrderConfStatusEnum.PREPARE.getCode())){
//            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "配载单状态不正确，不能操作");
//        }
        OrderConf orderConf = ConvertUtil.convert(orderConfVO, OrderConf.class);
        orderConf.setStatusCode(OrderConfStatusEnum.CANCEL.getCode());
        orderConf.setStatusName(OrderConfStatusEnum.CANCEL.getName());
        this.saveOrUpdate(orderConf);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startAutostowStatus(OrderConfIdForm form) {
        Long id = form.getId();
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(id);
        if(ObjectUtil.isEmpty(orderConfVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "沒有找到配载单");
        }
        if(!orderConfVO.getStatusCode().equals(OrderConfStatusEnum.ENABLE.getCode())){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "配载单状态不正确，不能操作");
        }
        OrderConf orderConf = ConvertUtil.convert(orderConfVO, OrderConf.class);
        orderConf.setStatusCode(OrderConfStatusEnum.START_AUTOSTOW.getCode());
        orderConf.setStatusName(OrderConfStatusEnum.START_AUTOSTOW.getName());
        this.saveOrUpdate(orderConf);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transitStatus(OrderConfIdForm form) {
        Long id = form.getId();
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(id);
        if(ObjectUtil.isEmpty(orderConfVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "沒有找到配载单");
        }
        if(!orderConfVO.getStatusCode().equals(OrderConfStatusEnum.START_AUTOSTOW.getCode())){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "配载单状态不正确，不能操作");
        }
        OrderConf orderConf = ConvertUtil.convert(orderConfVO, OrderConf.class);
        orderConf.setStatusCode(OrderConfStatusEnum.TRANSIT.getCode());
        orderConf.setStatusName(OrderConfStatusEnum.TRANSIT.getName());
        this.saveOrUpdate(orderConf);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishStatus(OrderConfIdForm form) {
        Long id = form.getId();
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(id);
        if(ObjectUtil.isEmpty(orderConfVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "沒有找到配载单");
        }
        if(!orderConfVO.getStatusCode().equals(OrderConfStatusEnum.TRANSIT.getCode())){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "配载单状态不正确，不能操作");
        }
        OrderConf orderConf = ConvertUtil.convert(orderConfVO, OrderConf.class);
        orderConf.setStatusCode(OrderConfStatusEnum.FINISH.getCode());
        orderConf.setStatusName(OrderConfStatusEnum.FINISH.getName());
        this.saveOrUpdate(orderConf);
    }

    @Override
    public void cancelStatusVerify(OrderConfVerifyForm form) {
        Long id = form.getId();
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(id);
        if(ObjectUtil.isEmpty(orderConfVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "沒有找到配载单");
        }
        String verifyPassword = form.getVerifyPassword();
        if("jyd".equals(verifyPassword)){
            //取消时，输入jdy，确认后才能取消
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "取消时，输入jdy，确认后才能取消");
        }
    }

    /**
     * 配载统计箱数(总箱数、未配载箱数)
     * @param id
     * @return
     */
    @Override
    public OrderConfCasesVO statisticsCases(Long id) {
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(id);
        if(ObjectUtil.isEmpty(orderConfVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "配载单不存在");
        }
        Long confId = orderConfVO.getId();
        //运单(订单)信息list
        List<OrderInfoVO> orderInfoList = orderInfoMapper.findOrderInfoByConfId(confId);
        Integer allboxNumber = 0;//总箱数
        Integer notboxNumber = 0;//未配载箱数
        Integer hasboxNumber = 0;//已配箱数
        if (CollUtil.isNotEmpty(orderInfoList)){
            for (int i=0; i<orderInfoList.size(); i++){
                OrderInfoVO orderInfoVO = orderInfoList.get(i);
                Long orderId = orderInfoVO.getId();
                List<OrderCaseVO> orderCaseList = orderCaseMapper.findOrderCaseByOrderId(orderId);
                int allboxNumber1 = orderCaseList.size();//单条订单的总箱数
                List<CounterCaseInfoVO> counterCaseInfoList = counterCaseInfoMapper.findCounterCaseInfoByOrderId(orderId);
                int hasboxNumber1 = counterCaseInfoList.size();//单条订单的已配载箱数
                int notboxNumber1 =  allboxNumber1 - hasboxNumber1;//单条订单的未配载箱数 = 单条订单的总箱数 - 单条订单的已配载箱数
                allboxNumber += allboxNumber1;
                notboxNumber += notboxNumber1;
                hasboxNumber += hasboxNumber1;
            }
        }
        OrderConfCasesVO orderConfCasesVO = new OrderConfCasesVO();
        orderConfCasesVO.setAllboxNumber(allboxNumber);
        orderConfCasesVO.setNotboxNumber(notboxNumber);
        orderConfCasesVO.setHasboxNumber(hasboxNumber);
        return orderConfCasesVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relevanceOfferInfo(OrderConfOfferInfoForm form) {
        Long confId = form.getConfId();
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(confId);
        if(ObjectUtil.isEmpty(orderConfVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "配载信息不存在");
        }
        List<OfferInfoVO> offerInfoList = form.getOfferInfoList();
        //配载的报价
        List<OceanConfDetail> offerInfoDetailList = new ArrayList<>();
        if(CollUtil.isNotEmpty(offerInfoList)){
            offerInfoList.forEach(offerInfoVO -> {
                Long offerInfoId = offerInfoVO.getId();//报价id
                OceanConfDetail oceanConfDetail = new OceanConfDetail();
                oceanConfDetail.setOrderId(confId);
                oceanConfDetail.setIdCode(offerInfoId.intValue());
                oceanConfDetail.setTypes(1);//1报价 2提单
                oceanConfDetail.setStatus("1");//状态(0无效 1有效)
                offerInfoDetailList.add(oceanConfDetail);
            });
        }
        if(CollUtil.isNotEmpty(offerInfoDetailList)){
            //3.保存配载的报价
            oceanConfDetailService.saveOrUpdateBatch(offerInfoDetailList);
        }
    }

}
