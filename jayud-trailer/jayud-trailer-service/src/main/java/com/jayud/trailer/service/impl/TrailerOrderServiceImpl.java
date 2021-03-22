package com.jayud.trailer.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.DelOprStatusForm;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.trailer.bo.*;
import com.jayud.trailer.enums.TrailerOrderStatusEnum;
import com.jayud.trailer.feign.FileClient;
import com.jayud.trailer.feign.OauthClient;
import com.jayud.trailer.feign.OmsClient;
import com.jayud.trailer.po.OrderFlowSheet;
import com.jayud.trailer.po.OrderStatus;
import com.jayud.trailer.po.TrailerDispatch;
import com.jayud.trailer.po.TrailerOrder;
import com.jayud.trailer.mapper.TrailerOrderMapper;
import com.jayud.trailer.service.ITrailerDispatchService;
import com.jayud.trailer.service.ITrailerOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.trailer.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 拖车订单表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Service
@Slf4j
public class TrailerOrderServiceImpl extends ServiceImpl<TrailerOrderMapper, TrailerOrder> implements ITrailerOrderService {

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private FileClient fileClient;


    @Autowired
    private ITrailerDispatchService trailerDispatchService;

    @Override
    public String createOrder(AddTrailerOrderFrom addTrailerOrderFrom) {
        LocalDateTime now = LocalDateTime.now();
        addTrailerOrderFrom.getPathAndName();
        TrailerOrder trailerOrder = ConvertUtil.convert(addTrailerOrderFrom, TrailerOrder.class);
//        System.out.println("trailerOrder===================================="+trailerOrder);
        //创建拖车单
        if (addTrailerOrderFrom.getId() == null) {
            //生成订单号
//            String orderNo = generationOrderNo(addTrailerOrderFrom.getLegalEntityId(),addTrailerOrderFrom.getImpAndExpType());
//            trailerOrder.setOrderNo(orderNo);
            trailerOrder.setCreateTime(now);
            trailerOrder.setCreateUser(UserOperator.getToken());
            trailerOrder.setStatus(OrderStatusEnum.TT_0.getCode());
            this.save(trailerOrder);
//            QueryWrapper queryWrapper = new QueryWrapper();
//            queryWrapper.eq("class_code","TC");
//            queryWrapper.isNotNull("sub_sorts");
//            queryWrapper.orderByAsc("sub_sorts");
//            if(addTrailerOrderFrom.getImpAndExpType().equals(1)){
//                queryWrapper.ne("contain_state","TT7");
//            }else{
//                queryWrapper.ne("contain_state","TT4");
//                if(!addTrailerOrderFrom.getIsWeighed()){
//                    queryWrapper.ne("contain_state","TT7");
//                }
//            }
//            List<OrderStatus> statuses = orderStatusService.list(queryWrapper);
//            List<OrderFlowSheet> list = new ArrayList<>();
//            for (int i = 0; i < statuses.size(); i++) {
//                OrderFlowSheet orderFlowSheet = new OrderFlowSheet();
//                orderFlowSheet.setMainOrderNo(trailerOrder.getMainOrderNo());
//                orderFlowSheet.setOrderNo(trailerOrder.getOrderNo());
//                orderFlowSheet.setProductClassifyId(statuses.get(i).getClassCode());
//                orderFlowSheet.setProductClassifyName(statuses.get(i).getClassName());
//                orderFlowSheet.setStatus(statuses.get(i).getContainState());
//                orderFlowSheet.setStatusName(statuses.get(i).getName());
//                if(i==0){
//                    orderFlowSheet.setComplete("1");
//                    orderFlowSheet.setFStatus(null);
//                }else{
//                    orderFlowSheet.setComplete("0");
//                    orderFlowSheet.setFStatus(statuses.get(i-1).getContainState());
//                }
//                orderFlowSheet.setIsPass("1");
//                orderFlowSheet.setCreateTime(now);
//                orderFlowSheet.setCreateUser(trailerOrder.getCreateUser());
//                list.add(orderFlowSheet);
//            }
//            ApiResult apiResult = omsClient.batchAddOrUpdateProcess(list);
//            if (apiResult.getCode() != HttpStatus.SC_OK) {
//                log.warn("批量保存订单流程报错={}", new JSONArray(list));
//            }

        } else {
            //修改拖车单
            trailerOrder.setId(addTrailerOrderFrom.getId());
            trailerOrder.setStatus(addTrailerOrderFrom.getStatus());
            trailerOrder.setUpdateTime(now);
            trailerOrder.setUpdateUser(UserOperator.getToken());
            this.updateById(trailerOrder);
        }
        if(addTrailerOrderFrom.getOrderAddressForms()!=null&&addTrailerOrderFrom.getOrderAddressForms().size()>0){
            //获取用户地址
            List<AddTrailerOrderAddressForm> orderAddressForms = addTrailerOrderFrom.getOrderAddressForms();
            List<AddOrderAddressForm> orderAddressForms1 = new ArrayList<>();
            for (AddTrailerOrderAddressForm addTrailerOrderAddressForm : orderAddressForms) {

                AddGoodsForm goodsForm = new AddGoodsForm();
                goodsForm.setOrderNo(trailerOrder.getOrderNo());
                goodsForm.setBusinessId(trailerOrder.getId());
                goodsForm.setBusinessType(BusinessTypeEnum.TC.getCode());
                goodsForm.setName(addTrailerOrderAddressForm.getName());
                goodsForm.setSize(addTrailerOrderAddressForm.getSize());
                goodsForm.setBulkCargoAmount(addTrailerOrderAddressForm.getBulkCargoAmount());
                goodsForm.setBulkCargoUnit(addTrailerOrderAddressForm.getBulkCargoUnit());
                goodsForm.setTotalWeight(addTrailerOrderAddressForm.getTotalWeight());
                goodsForm.setVolume(addTrailerOrderAddressForm.getVolume());
                //批量保存货物信息
                ApiResult result = this.omsClient.saveOrUpdateGood(goodsForm);
                if (result.getCode() != HttpStatus.SC_OK) {
                    log.warn("批量保存/修改商品信息失败,商品信息={}", new JSONArray(goodsForm));
                }

                AddOrderAddressForm orderAddressForm = ConvertUtil.convert(addTrailerOrderAddressForm, AddOrderAddressForm.class);
                orderAddressForm.setOrderNo(trailerOrder.getOrderNo());
                orderAddressForm.setBusinessType(BusinessTypeEnum.TC.getCode());
                orderAddressForm.setBusinessId(trailerOrder.getId());
                orderAddressForm.setCreateTime(LocalDateTime.now());
                orderAddressForm.setFileName(StringUtils.getFileNameStr(orderAddressForm.getTakeFiles()));
                orderAddressForm.setFilePath(StringUtils.getFileStr(orderAddressForm.getTakeFiles()));

                orderAddressForm.setBindGoodsId(Long.parseLong(result.getData().toString()));

                orderAddressForms1.add(orderAddressForm);
            }
            //批量保存用户地址
            ApiResult result = this.omsClient.saveOrUpdateOrderAddressBatch(orderAddressForms1);
            if (result.getCode() != HttpStatus.SC_OK) {
                log.warn("批量保存/修改订单地址信息失败,订单地址信息={}", new JSONArray(orderAddressForms1));
            }

        }
        return trailerOrder.getOrderNo();
    }

    /**
     * 是否存在订单
     */
    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper<TrailerOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(TrailerOrder::getOrderNo, orderNo);
        return this.count(condition) > 0;
    }

    /**
     * 根据主订单号获取海运订单详情
     * @param orderNo
     * @return
     */
    @Override
    public TrailerOrder getByMainOrderNO(String orderNo) {
        QueryWrapper<TrailerOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(TrailerOrder::getMainOrderNo, orderNo);
        return this.getOne(condition);
    }

    @Override
    public TrailerOrderVO getTrailerOrderByOrderNO(Long id) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        Integer businessType = BusinessTypeEnum.TC.getCode();
        //拖车订单信息
        TrailerOrderVO trailerOrderVO = this.baseMapper.getTrailerOrder(id);
        trailerOrderVO.getFile(prePath);

        //获取港口信息
        List<InitComboxStrVO> portCodeInfo = (List<InitComboxStrVO>)this.omsClient.initDictByDictTypeCode("Port").getData();
        for (InitComboxStrVO initComboxStrVO : portCodeInfo) {
            if(initComboxStrVO.getCode().equals(trailerOrderVO.getPortCode())){
                trailerOrderVO.setPortCodeName(initComboxStrVO.getName());
            }
        }

        //获取车型信息
        ApiResult cabinetSizeInfo = this.omsClient.getVehicleSizeInfo();
        trailerOrderVO.assemblyCabinetSize(cabinetSizeInfo);
        //查询地址信息
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusIds(Collections.singletonList(id), businessType);
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 airOrderId={}");
        }
        //处理地址信息
        List<TrailerOrderAddressVO> trailerOrderAddressVOS = new ArrayList<>();
        List<GoodsVO> goodsVOS = new ArrayList<>();
        for (OrderAddressVO address : resultOne.getData()) {
            address.getFile(prePath);
            TrailerOrderAddressVO convert = ConvertUtil.convert(address, TrailerOrderAddressVO.class);
            ApiResult goodResult = omsClient.getGoodById(address.getBindGoodsId());
            JSONObject goodById = new JSONObject(goodResult.getData());
            GoodsVO convert1 = ConvertUtil.convert(goodById, GoodsVO.class);
            goodsVOS.add(convert1);
            convert.setName(convert1.getName());
            convert.setBulkCargoAmount(convert1.getBulkCargoAmount());
            convert.setBulkCargoUnit(convert1.getBulkCargoUnit());
            convert.setSize(convert1.getSize());
            convert.setTotalWeight(convert1.getTotalWeight());
            convert.setVolume(convert1.getVolume());
            trailerOrderAddressVOS.add(convert);
        }
        if(goodsVOS.size()>0){
            trailerOrderVO.assemblyGoodsInfo(goodsVOS);
        }
        trailerOrderVO.setOrderAddressForms(trailerOrderAddressVOS);
        //查询派车信息
        TrailerDispatch trailerDispatch = this.trailerDispatchService.getEnableByTrailerOrderId(id);
        TrailerDispatchVO trailerDispatchVO = ConvertUtil.convert(trailerDispatch,TrailerDispatchVO.class);
        if(trailerDispatchVO.getPlateNumber()!=null){
            VehicleInfoLinkVO data = omsClient.initVehicleInfo(trailerDispatchVO.getPlateNumber()).getData();
            trailerDispatchVO.setPlateNumberName(data.getPlateNumber());
            for (DriverInfoVO driverInfo : data.getDriverInfos()) {
                if(trailerDispatchVO.getName().equals(driverInfo.getId())){
                    trailerDispatchVO.setDriverName(driverInfo.getName());
                }
            }
        }
        if(trailerOrderVO.getImpAndExpType().equals(2)&&trailerOrderVO.getStatus().equals(OrderStatusEnum.TT_4.getCode())){
            if(trailerOrderVO.getProcessDescription()!=null){
                trailerOrderVO.setStatus(trailerOrderVO.getProcessStatusDesc());
            }
        }
        if(trailerOrderVO.getImpAndExpType().equals(1)&&trailerOrderVO.getStatus().equals(OrderStatusEnum.TT_7.getCode())){
            if(trailerOrderVO.getProcessDescription()!=null){
                trailerOrderVO.setStatus(trailerOrderVO.getProcessStatusDesc());
            }
        }
        if(trailerOrderVO.getImpAndExpType().equals(2)&&trailerOrderVO.getStatus().equals(OrderStatusEnum.TT_7.getCode())&&trailerOrderVO.getIsWeighed()){
            if(trailerOrderVO.getProcessDescription()!=null){
                trailerOrderVO.setStatus(trailerOrderVO.getProcessStatusDesc());
            }
        }
        trailerOrderVO.setTrailerDispatchVO(trailerDispatchVO);
        return trailerOrderVO;
    }

    @Override
    public List<TrailerOrder> getTrailerOrderByOrderNOs(List<String> mainOrderNoList) {
        QueryWrapper<TrailerOrder> condition = new QueryWrapper<>();
        condition.lambda().in(TrailerOrder::getMainOrderNo, mainOrderNoList);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public IPage<TrailerOrderFormVO> findByPage(QueryTrailerOrderForm form) {
        if (StringUtils.isEmpty(form.getStatus())) { //订单列表
            form.setProcessStatusList(Arrays.asList(ProcessStatusEnum.PROCESSING.getCode()
                    , ProcessStatusEnum.COMPLETE.getCode(), ProcessStatusEnum.CLOSE.getCode()));
        } else {
            form.setProcessStatusList(Collections.singletonList(ProcessStatusEnum.PROCESSING.getCode()));
        }

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Page<TrailerOrderFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form,legalIds);
    }

    /**
     * 更新流程状态
     */
    @Transactional
    @Override
    public void updateProcessStatus(TrailerOrder trailerOrder, TrailerProcessOptForm form) {
        TrailerOrder trailerOrder1 = baseMapper.selectById(form.getId());
        trailerOrder.setId(form.getId());
        trailerOrder.setUpdateTime(LocalDateTime.now());
        trailerOrder.setUpdateUser(UserOperator.getToken());
        if(trailerOrder1.getImpAndExpType().equals(2)&&form.getStatus().equals(OrderStatusEnum.TT_3.getCode())){
            trailerOrder.setStatus(OrderStatusEnum.TT_4.getCode());
            trailerOrder.setProcessDescription(OrderStatusEnum.TT_3.getCode());
        }else if(trailerOrder1.getImpAndExpType().equals(1)&&form.getStatus().equals(OrderStatusEnum.TT_6.getCode())) {
            trailerOrder.setStatus(OrderStatusEnum.TT_7.getCode());
            trailerOrder.setProcessDescription(OrderStatusEnum.TT_6.getCode());
        }else if(trailerOrder1.getImpAndExpType().equals(2)&&form.getStatus().equals(OrderStatusEnum.TT_6.getCode())&&!trailerOrder1.getIsWeighed()){
            trailerOrder.setStatus(OrderStatusEnum.TT_7.getCode());
            trailerOrder.setProcessDescription(OrderStatusEnum.TT_6.getCode());
        }else{
            trailerOrder.setStatus(form.getStatus());
        }

        //更新状态节点状态
        this.baseMapper.updateById(trailerOrder);
        //节点操作记录
        this.trailerProcessOptRecord(form);

        //完成订单状态
        finishTrailerOrderOpt(trailerOrder);
    }


    /**
     * 拖车流程操作记录
     */
    @Override
    public void trailerProcessOptRecord(TrailerProcessOptForm form) {
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getId());
        auditInfoForm.setExtDesc(SqlConstant.SEA_ORDER);
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditUser(UserOperator.getToken());
        auditInfoForm.setFileViews(form.getFileViewList());
        auditInfoForm.setAuditStatus(form.getStatus());
        auditInfoForm.setAuditTypeDesc(form.getStatusName());

        //文件拼接
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setBusinessType(BusinessTypeEnum.TC.getCode());

        if (omsClient.saveOprStatus(form).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用物流轨迹失败");
        }
        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
        }

    }

    //判断订单完成状态
    private void finishTrailerOrderOpt(TrailerOrder trailerOrder) {
        if (OrderStatusEnum.TT_8.getCode().equals(trailerOrder.getStatus())) {
            //查询海运订单信息
            TrailerOrder trailerOrder1 = new TrailerOrder();
            trailerOrder1.setId(trailerOrder.getId());
            trailerOrder1.setProcessStatus(1);
            this.updateById(trailerOrder1);
        }

    }

    /**
     * 派车操作
     */
    @Override
    @Transactional
    public void doTrailerDispatchOpt(TrailerProcessOptForm form) {
        AddTrailerDispatchFrom addTrailerDispatchFrom = form.getTrailerDispatchVO();
        //查询派车是否存在,存在做更新操作
        TrailerDispatch oldTrailerDispatch = this.trailerDispatchService.getEnableByTrailerOrderId(addTrailerDispatchFrom.getId());
        addTrailerDispatchFrom.setId(oldTrailerDispatch != null ? oldTrailerDispatch.getId() : null);

        TrailerDispatch trailerDispatch = ConvertUtil.convert(addTrailerDispatchFrom, TrailerDispatch.class);
        trailerDispatch.setOrderId(form.getId());
        trailerDispatch.setTrailerOrderNo(form.getOrderNo());
        //设置派车状态
        trailerDispatch.setStatus(0);

        trailerDispatchService.saveOrUpdateTrailerDispatch(trailerDispatch);
        //更改流程节点完成状态
        TrailerOrder trailerOrder = new TrailerOrder();
        if(form.getPaperStripSeal()!=null){
            trailerOrder.setPaperStripSeal(form.getPaperStripSeal());
            trailerOrder.setCabinetNumber(form.getCabinetNumber());
        }
        updateProcessStatus(trailerOrder, form);
    }

    @Override
    public void orderReceiving(TrailerOrder trailerOrder, AuditInfoForm auditInfoForm, TrailerCargoRejected trailerCargoRejected) {
        TrailerOrder tmp = new TrailerOrder();
        tmp.setId(trailerOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());

        omsClient.saveAuditInfo(auditInfoForm);
        omsClient.doMainOrderRejectionSignOpt(trailerOrder.getMainOrderNo(),
                trailerOrder.getOrderNo() + "-" + auditInfoForm.getAuditComment() + ",");

        omsClient.saveAuditInfo(auditInfoForm);
        this.updateById(tmp);
    }

    @Override
    public void rejectedOpt(TrailerOrder trailerOrder, AuditInfoForm auditInfoForm, TrailerCargoRejected trailerCargoRejected) {

        TrailerOrder tmp = new TrailerOrder();
        tmp.setId(trailerOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());
        //根据选择是否派车驳回
        ApiResult result = new ApiResult();
        //删除物流轨迹表订舱数据
        switch (trailerCargoRejected.getRejectOptions()) {
            case 1://订单驳回
                result = omsClient.deleteLogisticsTrackByType(trailerOrder.getId(), BusinessTypeEnum.TC.getCode());
                //删除派车数据
                TrailerDispatch trailerDispatch = new TrailerDispatch();
                trailerDispatch.setStatus(TrailerOrderStatusEnum.DELETE.getCode());
                this.trailerDispatchService.updateByTrailerOrderId(trailerOrder.getId(), trailerDispatch);

                //执行主订单驳回标识
                omsClient.doMainOrderRejectionSignOpt(trailerOrder.getMainOrderNo(),
                        trailerOrder.getOrderNo() + "-" + auditInfoForm.getAuditComment() + ",");
                break;
            case 2://派车驳回
                DelOprStatusForm form = new DelOprStatusForm();
                form.setOrderId(trailerOrder.getId());
                form.setStatus(Collections.singletonList(OrderStatusEnum.TT_3.getCode()));
                result = this.omsClient.delSpecOprStatus(form);
                tmp.setStatus(OrderStatusEnum.TT_3_2.getCode());
        }

        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("远程调用删除订舱轨迹失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }

        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }

        //更改为驳回状态
        this.updateById(tmp);
    }
}
