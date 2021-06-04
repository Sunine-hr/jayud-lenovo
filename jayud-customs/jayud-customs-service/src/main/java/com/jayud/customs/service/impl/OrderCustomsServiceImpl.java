package com.jayud.customs.service.impl;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.*;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.customs.feign.*;
import com.jayud.customs.mapper.OrderCustomsMapper;
import com.jayud.customs.model.bo.*;
import com.jayud.customs.model.enums.BGBizModelEnum;
import com.jayud.customs.model.enums.BGOrderStatusEnum;
import com.jayud.customs.model.po.Dict;
import com.jayud.customs.model.po.Email;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.*;
import com.jayud.customs.service.IOrderCustomsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 报关业务订单表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Slf4j
@Service
public class OrderCustomsServiceImpl extends ServiceImpl<OrderCustomsMapper, OrderCustoms> implements IOrderCustomsService {

    @Autowired
    OmsClient omsClient;

    @Autowired
    FileClient fileClient;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    OauthClient oauthClient;

    @Autowired
    MsgClient msgClient;

    @Autowired
    TmsClient tmsClient;

    @Autowired
    InlandTpClient inlandTpClient;

    @Autowired
    OceanShipClient oceanShipClient;

    @Autowired
    FreightAirClient freightAirClient;

    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no", orderNo);
        List<OrderCustoms> orderCustomsList = baseMapper.selectList(queryWrapper);
        if (orderCustomsList == null || orderCustomsList.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean oprOrderCustoms(InputOrderCustomsForm form) {
        try {
            //暂存或提交只是主订单的状态不一样，子订单的操作每次先根据主订单号清空子订单
//            QueryWrapper<OrderCustoms> queryWrapper = new QueryWrapper<OrderCustoms>();
//            queryWrapper.eq("main_order_no", form.getMainOrderNo());
//            remove(queryWrapper);
            for (InputSubOrderCustomsForm subOrder : form.getSubOrders()) {
                if (subOrder.getSubOrderId() != null) {
                    removeById(subOrder.getSubOrderId());
                }
            }
            //子订单数据初始化处理
            //设置子订单号/报关抬头/结算单位/附件
            List<OrderCustoms> orderCustomsList = new ArrayList<>();
            List<InputSubOrderCustomsForm> subOrderCustomsForms = form.getSubOrders();
            for (InputSubOrderCustomsForm subOrder : subOrderCustomsForms) {
                OrderCustoms customs = ConvertUtil.convert(form, OrderCustoms.class);
                customs.setDescription(subOrder.getDescription());
                customs.setDescName(subOrder.getDescName());
                customs.setOrderNo(subOrder.getOrderNo());
                customs.setTitle(subOrder.getTitle());
                customs.setIsTitle(subOrder.getIsTitle());
                customs.setUnitCode(subOrder.getUnitCode());
                customs.setMainOrderNo(form.getMainOrderNo());
                customs.setStatus(OrderStatusEnum.CUSTOMS_C_0.getCode());
                customs.setCreatedUser(form.getLoginUser());
                customs.setCntrPic(StringUtils.getFileStr(form.getCntrPics()));
                customs.setCntrPicName(StringUtils.getFileNameStr(form.getCntrPics()));
                customs.setDescription(StringUtils.getFileStr(subOrder.getFileViews()));
                customs.setDescName(StringUtils.getFileNameStr(subOrder.getFileViews()));
                customs.setEncodePic(StringUtils.getFileStr(form.getEncodePics()));
                customs.setEncodePicName(StringUtils.getFileNameStr(form.getEncodePics()));
                customs.setAirTransportPic(StringUtils.getFileStr(form.getAirTransportPics()));
                customs.setAirTransPicName(StringUtils.getFileNameStr(form.getAirTransportPics()));
                customs.setSeaTransportPic(StringUtils.getFileStr(form.getSeaTransportPics()));
                customs.setSeaTransPicName(StringUtils.getFileNameStr(form.getSeaTransportPics()));
                customs.setOrderRemarks(form.getOrderRemarks());
                orderCustomsList.add(customs);
            }
            if (!orderCustomsList.isEmpty()) {
                saveOrUpdateBatch(orderCustomsList);
            }

            // 发送报关邮件
            sendEmail(form, orderCustomsList);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void sendEmail(InputOrderCustomsForm form, List<OrderCustoms> orderCustomsList) {
        if (!OrderStatusEnum.MAIN_1.getCode().equals(form.getMainOrderStatus())
                && !OrderStatusEnum.MAIN_4.getCode().equals(form.getMainOrderStatus())) {
            return;
        }

        // 获取收件人信息
        Map<String, Dict> transportToEmail = omsClient.findDictType("BGEmail").getData()
                .stream().collect(Collectors.toMap(Dict::getCode, e -> e));

        Email email = new Email();
        String bizModelDesc = BGBizModelEnum.getDesc(form.getBizModel());
        String goodType = CommonConstant.VALUE_1.equals(form.getGoodsType().toString()) ? CommonConstant.GOODS_TYPE_DESC_1 : CommonConstant.GOODS_TYPE_DESC_2;
        BGBizModelEnum bizModelEnum = BGBizModelEnum.getEnum(form.getBizModel());
        String emailTo = transportToEmail.get(bizModelEnum.getDictCode()).getValue();

        // 陆路运输
        if (BGBizModelEnum.LAND_TRANSPORT.getCode().equals(form.getBizModel())) {
            // 中港
            InputOrderTransportVO transportVO = tmsClient.getOrderTransport(form.getMainOrderNo()).getData();

            // 提货日期(MM-dd)
            String takeTimeStr = "";
            // 件数
            String totalNum = "";
            if (Objects.nonNull(transportVO) && Objects.nonNull(transportVO.getOrderNo())) {
                takeTimeStr = transportVO.getOrderTakeAdrForms1().stream()
                        .min(Comparator.comparing(InputOrderTakeAdrVO::getTakeTimeStr)).get().getTakeTimeStr();
                takeTimeStr = DateUtils.format(takeTimeStr, "MM-dd");
                totalNum = transportVO.getTotalAmount().toString();
            }

            email.setTo(emailTo);
            // 业务模式名称+日期+口岸+进出口+单量+件数+主订单号
            email.setSubject(String.format("%s %s %s %s %s单 %s件 作业号:%s"
                    , bizModelDesc, takeTimeStr, form.getPortName(), goodType, orderCustomsList.size(), totalNum, form.getMainOrderNo()));
            getAssembleEmailTextAndFiles(form, email);
        }

        // 内陆
        if (BGBizModelEnum.INLAND.getCode().equals(form.getBizModel())) {
            InputOrderInlandTPVO inlandTPVO = inlandTpClient.getOrderDetails(form.getMainOrderNo()).getData();

            // 提货日期
            String takeTimeStr = "";
            // 件数
            String totalNum = "";
            if (Objects.nonNull(inlandTPVO) && Objects.nonNull(inlandTPVO.getOrderNo())) {
//                takeTimeStr = inlandTPVO.getPickUpAddressList().stream()
//                        .min(Comparator.comparing(OrderDeliveryAddress::getDeliveryDate)).get().getDeliveryDate();
                List<OrderDeliveryAddress> pickUpAddressList = inlandTPVO.getPickUpAddressList();
                if (CollectionUtils.isEmpty(pickUpAddressList)){
                    takeTimeStr = DateUtils.format(pickUpAddressList.get(0).getDeliveryDate(), "MM-dd");
                }
                totalNum = inlandTPVO.getTotalNum();
            }

            email.setTo(emailTo);
            // 业务模式名称+日期+口岸+进出口+单量+件数+主订单号
            email.setSubject(String.format("%s %s %s %s %s单 %s 作业号:%s"
                    , bizModelDesc, takeTimeStr, form.getPortName(), goodType, orderCustomsList.size(), totalNum, form.getMainOrderNo()));
            getAssembleEmailTextAndFiles(form, email);
        }

        // 海运
        if (BGBizModelEnum.SEA_TRANSPORT.getCode().equals(form.getBizModel())) {
            // 邮件主题:业务模式+日期+口岸+进出口+柜型+单量+主订单号
            InputSeaOrderVO seaOrderVO = this.oceanShipClient.getSeaOrderDetails(form.getMainOrderNo()).getData();
            // 货好时间
            String goodTime = "";
            // 柜型
            String cabinetType = "";
            if (Objects.nonNull(seaOrderVO) && Objects.nonNull(seaOrderVO.getOrderNo())) {
                goodTime = DateUtils.format(seaOrderVO.getGoodTime(), "MM-dd");
                cabinetType = seaOrderVO.getCabinetTypeName();
                if (Objects.equals(cabinetType, "FCL")) {
                    cabinetType = seaOrderVO.getCabinetSizeNumbers().stream()
                            .map(e -> String.format("%s * %S", e.getCabinetTypeSize(), e.getNumber()))
                            .collect(Collectors.joining(","));
                }
            }

            email.setTo(emailTo);
            email.setSubject(String.format("%s %s %s %s %s %s单 作业号:%s",
                    bizModelDesc, goodTime, form.getPortName(), goodType, cabinetType, orderCustomsList.size(), form.getMainOrderNo()));
            getAssembleEmailTextAndFiles(form, email);
        }

        // 空运
        if (BGBizModelEnum.AIR_TRANSPORT.getCode().equals(form.getBizModel())
                || BGBizModelEnum.EXPRESS.getCode().equals(form.getBizModel())) {
            // 邮件主题:业务模式+日期+单量+通关口岸
            InputAirOrderVO airOrderVO = this.freightAirClient.getAirOrderDetails(form.getMainOrderNo()).getData();
            // 货好时间
            String goodTime = "";
            if (Objects.nonNull(airOrderVO) && Objects.nonNull(airOrderVO.getOrderNo())) {
                goodTime = DateUtils.format(airOrderVO.getGoodTime(), "MM-dd");
            }
            email.setTo(emailTo);
            email.setSubject(String.format("%s %s %s单 %s", bizModelDesc, goodTime, orderCustomsList.size(), form.getPortName()));
            getAssembleEmailTextAndFiles(form, email);
        }

        HashMap<String, Object> msg = new HashMap<>();
        msg.put("mainOrderNo", form.getMainOrderNo());
        msg.put("email", email);
        generateKafkaMsg(KafkaMsgEnums.CUSTOM_SEND_EMAIL.getTopic(), KafkaMsgEnums.CUSTOM_SEND_EMAIL.getKey(), JSONObject.toJSONString(msg));
    }

    /**
     * 组装邮件内容及附件
     *
     * @param form
     * @param email
     */
    private void getAssembleEmailTextAndFiles(InputOrderCustomsForm form, Email email) {
        String customerName = omsClient.getCustomerNameByOrderNo(form.getMainOrderNo()).getData();

        InputOrderCustomsVO orderCustomsDetail = getOrderCustomsDetail(form.getMainOrderNo());
        List<InputSubOrderCustomsVO> subOrders = orderCustomsDetail.getSubOrders();
        String orderNoStrs = subOrders.stream().map(InputSubOrderCustomsVO::getOrderNo).collect(Collectors.joining(","));
        // 客户名称 + 报关订单号 + 备注内容
        email.setText(String.format("客户名称:%s\n报关订单号:%s\n备注内容:%s", customerName, orderNoStrs, form.getOrderRemarks()));

        List<FileView> fileViews = subOrders.stream().map(InputSubOrderCustomsVO::getFileViews)
                .flatMap(Collection::stream).collect(Collectors.toList());
        fileViews.addAll(orderCustomsDetail.getEncodePics());
        fileViews.addAll(orderCustomsDetail.getCntrPics());
        fileViews.addAll(orderCustomsDetail.getAirTransportPics());
        fileViews.addAll(orderCustomsDetail.getSeaTransportPics());
        email.setFileViews(fileViews);
    }

    private Boolean generateKafkaMsg(String topic, String key, String msg) {
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("topic", topic);
        msgMap.put("key", key);
        msgMap.put("msg", msg);
        log.info("开始发送邮件信息给kafka...");
        Map<String, String> consume = msgClient.consume(msgMap);

        if (Objects.nonNull(consume)) {
            log.info(consume.toString());
            String code = MapUtil.getStr(consume, "code");
            if (!StringUtils.isEmpty(code) && Integer.parseInt(code) == ResultEnum.SUCCESS.getCode()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }


    @Override
    public List<OrderCustomsVO> findOrderCustomsByCondition(Map<String, Object> param) {
        return baseMapper.findOrderCustomsByCondition(param);
    }

    @Override
    public IPage<CustomsOrderInfoVO> findCustomsOrderByPage(QueryCustomsOrderInfoForm form) {

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        //定义分页参数
        Page<CustomsOrderInfoVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("oc.id"));
        IPage<CustomsOrderInfoVO> pageInfo = baseMapper.findCustomsOrderByPage(page, form, legalIds);
        //处理附件
        List<CustomsOrderInfoVO> customsOrderInfoVOS = pageInfo.getRecords();
        String prePath = fileClient.getBaseUrl().getData().toString();
        for (CustomsOrderInfoVO customsOrder : customsOrderInfoVOS) {
            customsOrder.setGoodsTypeDesc(customsOrder.getGoodsType());
            //处理子订单附件信息
            String fileStr = customsOrder.getFileStr();
            String fileNameStr = customsOrder.getFileNameStr();
            customsOrder.setFileViews(StringUtils.getFileViews(fileStr, fileNameStr, prePath));
        }
        return pageInfo;
    }


    @Override
    public InputOrderCustomsVO getOrderCustomsDetail(String mainOrderNo) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        InputOrderCustomsVO inputOrderCustomsVO = new InputOrderCustomsVO();
        Map<String, Object> param = new HashMap<>();
        param.put("main_order_no", mainOrderNo);
        List<OrderCustomsVO> orderCustomsVOS = findOrderCustomsByCondition(param);
        if (orderCustomsVOS != null && orderCustomsVOS.size() > 0) {
            OrderCustomsVO orderCustomsVO = orderCustomsVOS.get(0);
            //设置纯报关头部分
            inputOrderCustomsVO.setId(orderCustomsVO.getSubOrderId());
            inputOrderCustomsVO.setPortCode(orderCustomsVO.getPortCode());
            inputOrderCustomsVO.setPortName(orderCustomsVO.getPortName());
            inputOrderCustomsVO.setGoodsType(orderCustomsVO.getGoodsType());
            inputOrderCustomsVO.setCntrNo(orderCustomsVO.getCntrNo());
            inputOrderCustomsVO.setCntrPics(StringUtils.getFileViews(orderCustomsVO.getCntrPic(), orderCustomsVO.getCntrPicName(), prePath));
            inputOrderCustomsVO.setEncode(orderCustomsVO.getEncode());
            inputOrderCustomsVO.setEncodePics(StringUtils.getFileViews(orderCustomsVO.getEncodePic(), orderCustomsVO.getEncodePicName(), prePath));
            inputOrderCustomsVO.setIsAgencyTax(orderCustomsVO.getIsAgencyTax());
            inputOrderCustomsVO.setSeaTransportNo(orderCustomsVO.getSeaTransportNo());
            inputOrderCustomsVO.setSeaTransportPics(StringUtils.getFileViews(orderCustomsVO.getSeaTransportPic(), orderCustomsVO.getSeaTransPicName(), prePath));
            inputOrderCustomsVO.setAirTransportNo(orderCustomsVO.getAirTransportNo());
            inputOrderCustomsVO.setAirTransportPics(StringUtils.getFileViews(orderCustomsVO.getAirTransportPic(), orderCustomsVO.getAirTransPicName(), prePath));
            inputOrderCustomsVO.setLegalName(orderCustomsVO.getLegalName());
            inputOrderCustomsVO.setLegalEntityId(orderCustomsVO.getLegalEntityId());
            inputOrderCustomsVO.setBizModel(orderCustomsVO.getBizModel());
            inputOrderCustomsVO.setSupervisionMode(orderCustomsVO.getSupervisionMode());
            //为了控制驳回编辑子订单之间互不影响,报关中驳回时所有子订单都应驳回
            inputOrderCustomsVO.setSubCustomsStatus(orderCustomsVO.getStatus());
            inputOrderCustomsVO.setOrderRemarks(orderCustomsVO.getOrderRemarks());
            //处理子订单部分
            List<InputSubOrderCustomsVO> subOrderCustomsVOS = new ArrayList<>();
            for (OrderCustomsVO orderCustoms : orderCustomsVOS) {
                InputSubOrderCustomsVO subOrderCustomsVO = new InputSubOrderCustomsVO();
                subOrderCustomsVO.setSubOrderId(orderCustoms.getSubOrderId());
                subOrderCustomsVO.setOrderNo(orderCustoms.getOrderNo());
                subOrderCustomsVO.setTitle(orderCustoms.getTitle());
                subOrderCustomsVO.setIsTitle(orderCustoms.getIsTitle());
                subOrderCustomsVO.setUnitCode(orderCustoms.getUnitCode());
                orderCustoms.setStatusDesc(orderCustoms.getStatus());
                subOrderCustomsVO.setStatusDesc(orderCustoms.getStatusDesc());
                subOrderCustomsVO.setEntrustNo(orderCustoms.getEntrustNo());
                subOrderCustomsVO.setYunCustomsNo(orderCustoms.getYunCustomsNo());
                subOrderCustomsVO.setSupervisionMode(orderCustoms.getSupervisionMode());
                subOrderCustomsVO.setOrderRemarks(orderCustoms.getOrderRemarks());
                //处理子订单附件信息
                String fileStr = orderCustoms.getFileStr();
                String fileNameStr = orderCustoms.getFileNameStr();
                subOrderCustomsVO.setFileViews(StringUtils.getFileViews(fileStr, fileNameStr, prePath));
                subOrderCustomsVOS.add(subOrderCustomsVO);
            }
            inputOrderCustomsVO.setSubOrders(subOrderCustomsVOS);
            inputOrderCustomsVO.setNumber(String.valueOf(subOrderCustomsVOS.size()));

        }
        return inputOrderCustomsVO;
    }

    @Override
    public StatisticsDataNumberVO statisticsDataNumber() {
        return baseMapper.statisticsDataNumber();
    }

    /**
     * 根据主订单集合查询所有报关信息
     */
    @Override
    public List<OrderCustoms> getCustomsOrderByMainOrderNos(List<String> mainOrderNos) {
        QueryWrapper<OrderCustoms> condition = new QueryWrapper<>();
        condition.lambda().in(OrderCustoms::getMainOrderNo, mainOrderNos);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 获取特定状态下的报关订单
     *
     * @param statuses
     * @return
     */
    @Override
    public List<OrderCustoms> getOrderCustomsByStatus(List<String> statuses) {
        return this.baseMapper.getOrderCustomsByStatus(statuses);
    }

    /**
     * 查询菜单待处理订单数
     *
     * @param status
     * @param legalIds
     * @return
     */
    @Override
    public Integer getNumByStatus(String status, List<Long> legalIds) {
        Integer num = 0;
        switch (status) {
            case "portFeeCheck":
                List<OrderCustoms> list = this.getByLegalEntityId(legalIds);
                if (org.apache.commons.collections4.CollectionUtils.isEmpty(list)) return num;
                List<String> orderNos = list.stream().map(OrderCustoms::getOrderNo).collect(Collectors.toList());
                num = this.omsClient.auditPendingExpenses(SubOrderSignEnum.BG.getSignOne(), legalIds, orderNos).getData();
                break;
            default:
                List<String> mainOrderNos = this.baseMapper.getMainOrderNoByStatus(status, legalIds);
                if (CollectionUtils.isEmpty(mainOrderNos)) {
                    return 0;
                }
                num = this.omsClient.getFilterOrderStatus(mainOrderNos, 1).getData();
        }

        return num == null ? 0 : num;
    }

    @Override
    public OrderCustoms getOrderCustomsByOrderNo(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no", orderNo);

        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean updateProcessStatus(OrderCustoms orderCustoms) {
        OprStatusForm form = new OprStatusForm();
        //获取主订单id
        Long mainOrderId = omsClient.getIdByOrderNo(orderCustoms.getMainOrderNo()).getData();
        //保存操作节点
        form.setMainOrderId(mainOrderId);
        form.setOrderId(orderCustoms.getId());
        form.setStatus(orderCustoms.getStatus());
        form.setStatusName(OrderStatusEnum.getEnums(orderCustoms.getStatus()).getDesc());
        form.setBusinessType(BusinessTypeEnum.BG.getCode());
        form.setOperatorTime(LocalDateTime.now().toString());
        omsClient.saveOprStatus(form);

        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditStatus(orderCustoms.getStatus());
        auditInfoForm.setAuditTypeDesc(BGOrderStatusEnum.getDesc1(orderCustoms.getStatus()));
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.ORDER_CUSTOMS);
        auditInfoForm.setAuditUser(form.getOperatorUser());
        auditInfoForm.setFileViews(form.getFileViewList());
        omsClient.saveAuditInfo(auditInfoForm);

        boolean b = this.saveOrUpdate(orderCustoms);
        return b;
    }

    @Override
    public List<OrderCustoms> getByLegalEntityId(List<Long> legalIds) {
        QueryWrapper<OrderCustoms> condition = new QueryWrapper<>();
        condition.lambda().in(OrderCustoms::getLegalEntityId, legalIds);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<OrderCustoms> getOrderCustomsTaskData() {
        return this.baseMapper.getOrderCustomsTaskData();
    }

    @Override
    public Boolean updateProcessStatus(OrderCustoms orderCustoms, String auditUser, String auditTime) {
        OprStatusForm form = new OprStatusForm();
        //获取主订单id
        Long mainOrderId = omsClient.getIdByOrderNo(orderCustoms.getMainOrderNo()).getData();
        //保存操作节点
        form.setMainOrderId(mainOrderId);
        form.setOrderId(orderCustoms.getId());
        form.setStatus(orderCustoms.getStatus());
        form.setStatusName(OrderStatusEnum.getEnums(orderCustoms.getStatus()).getDesc());
        form.setBusinessType(BusinessTypeEnum.BG.getCode());
        form.setOperatorTime(auditTime);
        form.setOperatorUser(auditUser);
        omsClient.saveOprStatus(form);

        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditStatus(orderCustoms.getStatus());
        auditInfoForm.setAuditTypeDesc(BGOrderStatusEnum.getDesc(orderCustoms.getStatus()));
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.ORDER_CUSTOMS);
        auditInfoForm.setAuditUser(auditUser);
        LocalDateTime processTime = DateUtils.str2LocalDateTime(auditTime, DateUtils.DATE_TIME_PATTERN);
        auditInfoForm.setAuditTime(processTime);
        auditInfoForm.setFileViews(form.getFileViewList());
        omsClient.saveAuditInfo(auditInfoForm);

        return this.saveOrUpdate(orderCustoms);
    }

    /**
     * 查询所有通关报关订单
     *
     * @param mainOrders
     * @return
     */
    @Override
    public List<String> getAllPassByMainOrderNos(List<String> mainOrders) {
        //获取订单信息
        List<OrderCustoms> list = this.getCustomsOrderByMainOrderNos(mainOrders);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        //查询批量
        Map<String, Long> gourp = list.stream().collect(Collectors.groupingBy(OrderCustoms::getMainOrderNo, Collectors.counting()));
        List<String> passOrders = new ArrayList<>();
        for (OrderCustoms orderCustoms : list) {
            if (OrderStatusEnum.CUSTOMS_C_5.getCode().equals(orderCustoms.getStatus())) {
                Long count = gourp.get(orderCustoms.getMainOrderNo());
                gourp.put(orderCustoms.getMainOrderNo(), --count);
                if (count == 0) {
                    passOrders.add(orderCustoms.getMainOrderNo());
                    continue;
                }

            }

        }
        return passOrders;
    }

    @Override
    public InputSubOrderCustomsVO getOrderCustomsByYunCustomsNo(String yunCustomsNo) {
        OrderCustoms orderCustoms = getOne(Wrappers.<OrderCustoms>lambdaQuery().eq(OrderCustoms::getYunCustomsNo, yunCustomsNo));
        if (Objects.isNull(orderCustoms)) {
            return null;
        }
        InputSubOrderCustomsVO inputSubOrderCustomsVO = new InputSubOrderCustomsVO();
        BeanUtils.copyProperties(orderCustoms, inputSubOrderCustomsVO);
        return inputSubOrderCustomsVO;
    }
}
