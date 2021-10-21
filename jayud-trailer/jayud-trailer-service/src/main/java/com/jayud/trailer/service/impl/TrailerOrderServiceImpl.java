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
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.trailer.bo.*;
import com.jayud.trailer.feign.FileClient;
import com.jayud.trailer.feign.OauthClient;
import com.jayud.trailer.feign.OmsClient;
import com.jayud.trailer.po.TrailerDispatch;
import com.jayud.trailer.po.TrailerOrder;
import com.jayud.trailer.mapper.TrailerOrderMapper;
import com.jayud.trailer.service.ITrailerDispatchService;
import com.jayud.trailer.service.ITrailerOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.trailer.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
            boolean save = this.save(trailerOrder);
            if (save) {
                log.warn(trailerOrder.getMainOrderNo() + "拖车单添加成功");
            } else {
                log.error(trailerOrder.getMainOrderNo() + "拖车单添加失败");
            }
        } else {
            //修改拖车单
            trailerOrder.setId(addTrailerOrderFrom.getId());
            trailerOrder.setStatus(OrderStatusEnum.TT_0.getCode());
            trailerOrder.setUpdateTime(now);
            trailerOrder.setUpdateUser(UserOperator.getToken());
            boolean update = this.saveOrUpdate(trailerOrder);
            if (update) {
                log.warn(trailerOrder.getMainOrderNo() + "拖车单修改成功");
            } else {
                log.error(trailerOrder.getMainOrderNo() + "拖车单修改失败");
            }
        }
//        omsClient.deleteGoodsByBusOrders(Collections.singletonList(trailerOrder.getOrderNo()), BusinessTypeEnum.TC.getCode());
//        omsClient.deleteOrderAddressByBusOrders(Collections.singletonList(trailerOrder.getOrderNo()), BusinessTypeEnum.TC.getCode());
        if (addTrailerOrderFrom.getOrderAddressForms() != null && addTrailerOrderFrom.getOrderAddressForms().size() > 0) {
            //获取用户地址
            List<AddTrailerOrderAddressForm> orderAddressForms = addTrailerOrderFrom.getOrderAddressForms();
            for (AddTrailerOrderAddressForm addTrailerOrderAddressForm : orderAddressForms) {

                addTrailerOrderAddressForm.setOrderNo(trailerOrder.getOrderNo());
                addTrailerOrderAddressForm.setBusinessId(trailerOrder.getId());
                addTrailerOrderAddressForm.setBusinessType(BusinessTypeEnum.TC.getCode());
                addTrailerOrderAddressForm.setType(3);

            }

            //批量保存用户地址
            ApiResult result = this.omsClient.saveOrUpdateOrderAddressAndGoodsBatch(orderAddressForms);
            if (result.getCode() != HttpStatus.SC_OK) {
                log.warn("批量保存/修改订单地址信息失败,订单地址信息={}", new JSONArray(orderAddressForms));
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
     * 根据主订单号获取拖车订单详情
     *
     * @param orderNo
     * @return
     */
    @Override
    public List<TrailerOrder> getByMainOrderNO(String orderNo) {
        QueryWrapper<TrailerOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(TrailerOrder::getMainOrderNo, orderNo);
        return this.list(condition);
    }

    @Override
    public TrailerOrderVO getTrailerOrderByOrderNO(Long id) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        Integer businessType = BusinessTypeEnum.TC.getCode();
        //拖车订单信息
        TrailerOrderVO trailerOrderVO = this.baseMapper.getTrailerOrder(id);
        trailerOrderVO.getFile(prePath);
        //主订单信息
        Object mainOrderInfo = this.omsClient.getMainOrderByOrderNos(Arrays.asList(trailerOrderVO.getMainOrderNo())).getData();
        trailerOrderVO.assemblyMainOrderInfo(mainOrderInfo);

        //获取港口信息
        List<InitComboxStrVO> portCodeInfo = (List<InitComboxStrVO>) this.omsClient.initDictByDictTypeCode("Port").getData();
        for (InitComboxStrVO initComboxStrVO : portCodeInfo) {
            if (initComboxStrVO.getCode().equals(trailerOrderVO.getPortCode())) {
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

//        List<FileView> attachments = (List<FileView>)this.omsClient.getTrailerAttachments(trailerOrderVO.getId()).getData();
//        trailerOrderVO.setAllPics(attachments);

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
        if (goodsVOS.size() > 0) {
            trailerOrderVO.assemblyGoodsInfo(goodsVOS);
        }
        trailerOrderVO.setOrderAddressForms(trailerOrderAddressVOS);
        //查询派车信息
        TrailerDispatch trailerDispatch = this.trailerDispatchService.getEnableByTrailerOrderId(trailerOrderVO.getOrderNo());
        TrailerDispatchVO trailerDispatchVO = ConvertUtil.convert(trailerDispatch, TrailerDispatchVO.class);
        if (trailerDispatchVO.getPlateNumber() != null) {
            VehicleInfoLinkVO data = omsClient.initVehicleInfo(trailerDispatchVO.getPlateNumber()).getData();
            trailerDispatchVO.setPlateNumberName(data.getPlateNumber());
            for (DriverInfoVO driverInfo : data.getDriverInfos()) {
                if (trailerDispatchVO.getName().equals(driverInfo.getId())) {
                    trailerDispatchVO.setDriverName(driverInfo.getName());
                }
            }
        }
        if (trailerOrderVO.getImpAndExpType().equals(2) && trailerOrderVO.getStatus().equals(OrderStatusEnum.TT_4.getCode())) {
            if (trailerOrderVO.getProcessDescription() != null) {
                trailerOrderVO.setStatus(trailerOrderVO.getProcessStatusDesc());
            }
        }
        if (trailerOrderVO.getImpAndExpType().equals(1) && trailerOrderVO.getStatus().equals(OrderStatusEnum.TT_7.getCode())) {
            if (trailerOrderVO.getProcessDescription() != null) {
                trailerOrderVO.setStatus(trailerOrderVO.getProcessStatusDesc());
            }
        }
        if (trailerOrderVO.getImpAndExpType().equals(2) && trailerOrderVO.getStatus().equals(OrderStatusEnum.TT_7.getCode()) && trailerOrderVO.getIsWeighed()) {
            if (trailerOrderVO.getProcessDescription() != null) {
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
        if (form.getProcessStatus() != null) {
            form.setProcessStatusList(Collections.singletonList(form.getProcessStatus()));
        } else {
            if (StringUtils.isEmpty(form.getStatus())) { //订单列表
                form.setProcessStatusList(Arrays.asList(ProcessStatusEnum.PROCESSING.getCode()
                        , ProcessStatusEnum.COMPLETE.getCode(), ProcessStatusEnum.CLOSE.getCode()));
            } else {
                form.setProcessStatusList(Collections.singletonList(ProcessStatusEnum.PROCESSING.getCode()));
            }

        }

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Page<TrailerOrderFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<TrailerOrderFormVO> iPage = this.baseMapper.findByPage(page, form, legalIds);
        return iPage;
    }

    /**
     * 更新流程状态
     */
    @Transactional
    @Override
    public void updateProcessStatus(TrailerOrder trailerOrder, TrailerProcessOptForm form) {
        TrailerOrder trailerOrder1 = baseMapper.selectById(form.getOrderId());
        trailerOrder.setId(form.getOrderId());
        trailerOrder.setUpdateTime(LocalDateTime.now());
        trailerOrder.setUpdateUser(UserOperator.getToken());
        if (trailerOrder1.getImpAndExpType().equals(2) && form.getStatus().equals(OrderStatusEnum.TT_3.getCode())) {
            trailerOrder.setStatus(OrderStatusEnum.TT_4.getCode());
            trailerOrder.setProcessDescription(OrderStatusEnum.TT_3.getCode());
        } else if (trailerOrder1.getImpAndExpType().equals(1) && form.getStatus().equals(OrderStatusEnum.TT_6.getCode())) {
            trailerOrder.setStatus(OrderStatusEnum.TT_7.getCode());
            trailerOrder.setProcessDescription(OrderStatusEnum.TT_6.getCode());
        } else if (trailerOrder1.getImpAndExpType().equals(2) && form.getStatus().equals(OrderStatusEnum.TT_6.getCode()) && !trailerOrder1.getIsWeighed()) {
            trailerOrder.setStatus(OrderStatusEnum.TT_7.getCode());
            trailerOrder.setProcessDescription(OrderStatusEnum.TT_6.getCode());
        } else {
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
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.TRAILER_ORDER);
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
        TrailerDispatch oldTrailerDispatch = this.trailerDispatchService.getEnableByTrailerOrderId(form.getOrderNo());
        addTrailerDispatchFrom.setId(oldTrailerDispatch != null ? oldTrailerDispatch.getId() : null);

        TrailerDispatch trailerDispatch = ConvertUtil.convert(addTrailerDispatchFrom, TrailerDispatch.class);
        trailerDispatch.setOrderId(form.getOrderId());
        trailerDispatch.setTrailerOrderNo(form.getOrderNo());
        //设置派车状态
        trailerDispatch.setStatus(0);

        trailerDispatchService.saveOrUpdateTrailerDispatch(trailerDispatch);
        //更改流程节点完成状态
        TrailerOrder trailerOrder = new TrailerOrder();
        if (form.getPaperStripSeal() != null) {
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
//                TrailerDispatch trailerDispatch = new TrailerDispatch();
//                trailerDispatch.setStatus(TrailerOrderStatusEnum.DELETE.getCode());
//                this.trailerDispatchService.updateByTrailerOrderId(trailerOrder.getId(), trailerDispatch);
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("order_id", trailerOrder.getId());
                this.trailerDispatchService.remove(queryWrapper);
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

    @Override
    public TrailerOrder getByOrderNO(String orderNo) {
        QueryWrapper<TrailerOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(TrailerOrder::getOrderNo, orderNo);
        return this.getOne(condition);
    }

    /**
     * 根据主订单号查询所有详情
     *
     * @param mainOrderNos
     * @return
     */
    @Override
    public List<TrailerOrderInfoVO> getTrailerInfoByMainOrderNos(List<String> mainOrderNos) {
        return this.baseMapper.getTrailerInfoByMainOrderNos(mainOrderNos);
    }

    @Override
    public List<TrailerOrderInfoVO> getInfo(List<String> mainOrderNos) {
        List<TrailerOrderInfoVO> trailerOrders = this.getTrailerInfoByMainOrderNos(mainOrderNos);

        List<Long> orderIds = new ArrayList<>();
        List<Long> vehicleIds = new ArrayList<>();

        for (TrailerOrderInfoVO trailerOrder : trailerOrders) {
            orderIds.add(trailerOrder.getId());
            TrailerDispatchInfoVO dispatchInfoVO = trailerOrder.getTrailerDispatchInfoVO();
            if (dispatchInfoVO != null && !StringUtils.isEmpty(dispatchInfoVO.getPlateNumber())) {
                vehicleIds.add(Long.valueOf(dispatchInfoVO.getPlateNumber()));
            }
        }

        //查询起运港/目的港
        List<InitComboxStrVO> port = omsClient.initDictByDictTypeCode("Port").getData();
        Map<String, String> portMap = port.stream().collect(Collectors.toMap(InitComboxStrVO::getCode, InitComboxStrVO::getName));

        //查询车型尺寸
        Object vehicleSize = this.omsClient.getVehicleSizeInfo().getData();
        Map<Long, String> vehicleSizeMap = new HashMap<>();
        if (vehicleSize != null) {
            List<InitComboxVO> initComboxVOS = Utilities.obj2List(vehicleSize, InitComboxVO.class);
            vehicleSizeMap = initComboxVOS.stream().collect(Collectors.toMap(InitComboxVO::getId, InitComboxVO::getName));
        }

        //查询订单地址
        List<OrderDeliveryAddress> deliveryAddresses = omsClient.getDeliveryAddress(orderIds, BusinessTypeEnum.TC.getCode()).getData();
        Map<Long, List<OrderDeliveryAddress>> deliveryAddressMap = deliveryAddresses.stream().collect(Collectors.groupingBy(OrderDeliveryAddress::getBusinessId));

        //查询车辆信息
        Object vehicleInfos = this.omsClient.getVehicleInfoByIds(vehicleIds).getData();

        for (TrailerOrderInfoVO trailerOrder : trailerOrders) {
            trailerOrder.setPortName(portMap.get(trailerOrder.getPortCode()))
                    .setDeliveryAddresses(deliveryAddressMap.get(trailerOrder.getId()))
                    .setCabinetSize(vehicleSizeMap.get(Long.valueOf(trailerOrder.getCabinetSize())))
                    .assemblyVehicleInfos(vehicleInfos);

        }

        return trailerOrders;
    }

    @Override
    public Integer getNumByStatus(String status, List<Long> legalIds, Map<String, Object> datas) {
        Integer num = 0;
        switch (status) {
            case "CostAudit":
//                List<Long> legalIds = dataControl.getCompanyIds();
                List<TrailerOrder> list = this.getByLegalEntityId(legalIds);
                if (CollectionUtils.isEmpty(list)) return num;
                List<String> orderNos = list.stream().map(TrailerOrder::getOrderNo).collect(Collectors.toList());
                num = this.omsClient.auditPendingExpenses(SubOrderSignEnum.TC.getSignOne(), legalIds, orderNos).getData();
                break;
            case "trailerReceiverCheck":
                Map<String, Integer> costNum = (Map<String, Integer>) datas.get(status);
                num = costNum.get("B_1");
                break;
            case "trailerPayCheck":
                costNum = (Map<String, Integer>) datas.get(status);
                num = costNum.get("B_1");
                break;
            default:
                num = this.baseMapper.getNumByStatus(status, legalIds);
        }

        return num == null ? 0 : num;
    }

    @Override
    public List<TrailerOrder> getByCondition(TrailerOrder trailerOrder) {
        QueryWrapper<TrailerOrder> condition = new QueryWrapper<>(trailerOrder);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 根据子订单号集合查询子订单
     *
     * @param orderNos
     * @return
     */
    @Override
    public List<TrailerOrder> getOrdersByOrderNos(List<String> orderNos) {
        QueryWrapper<TrailerOrder> condition = new QueryWrapper<>();
        condition.lambda().in(TrailerOrder::getOrderNo, orderNos);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<TrailerOrder> getByLegalEntityId(List<Long> legalIds) {
        QueryWrapper<TrailerOrder> condition = new QueryWrapper<>();
        condition.lambda().in(TrailerOrder::getLegalEntityId, legalIds);
        return this.baseMapper.selectList(condition);
    }

}
