package com.jayud.oms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.entity.DataControl;
import com.jayud.common.entity.SubOrderCloseOpt;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.*;
import com.jayud.oms.feign.*;
import com.jayud.oms.mapper.OrderInfoMapper;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.*;
import io.netty.util.internal.StringUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static com.jayud.common.enums.CreateUserTypeEnum.*;

/**
 * <p>
 * 主订单基础数据表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    @Autowired
    RedisUtils redisUtils;
    @Autowired
    IProductBizService productBizService;
    @Autowired
    IOrderPaymentCostService paymentCostService;
    @Autowired
    IOrderReceivableCostService receivableCostService;
    @Autowired
    IOrderStatusService orderStatusService;
    @Autowired
    ILogisticsTrackService logisticsTrackService;
    @Autowired
    ICurrencyInfoService currencyInfoService;
    @Autowired
    ICostInfoService costInfoService;
    @Autowired
    CustomsClient customsClient;
    @Autowired
    TmsClient tmsClient;
    @Autowired
    ICostTypeService costTypeService;
    @Autowired
    FileClient fileClient;
    @Autowired
    ICustomerInfoService customerInfoService;
    @Autowired
    private FreightAirClient freightAirClient;
    @Autowired
    private MsgClient msgClient;
    @Autowired
    private ISupplierInfoService supplierInfoService;
    @Autowired
    private IAuditInfoService auditInfoService;
    @Autowired
    private InlandTpClient inlandTpClient;
    @Autowired
    private IOrderFlowSheetService orderFlowSheetService;
    @Autowired
    private IOrderReceivableCostService orderReceivableCostService;
    @Autowired
    private IOrderPaymentCostService orderPaymentCostService;
    @Autowired
    private IProductClassifyService productClassifyService;
    @Autowired
    private StorageClient storageClient;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IOrderAddressService orderAddressService;
    @Autowired
    private FinanceClient financeClient;
    @Autowired
    private ICostGenreService costGenreService;


    private final String[] KEY_SUBORDER = {SubOrderSignEnum.ZGYS.getSignOne(),
            SubOrderSignEnum.KY.getSignOne(), SubOrderSignEnum.HY.getSignOne(),
            SubOrderSignEnum.BG.getSignOne(), SubOrderSignEnum.NL.getSignOne(),
            SubOrderSignEnum.TC.getSignOne()};

    @Autowired
    private IServiceOrderService serviceOrderService;

    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private IOrderAttachmentService orderAttachmentService;

    @Autowired
    private OceanShipClient oceanShipClient;

    @Autowired
    private TrailerClient trailerClient;

    @Autowired
    private IOrderTypeNumberService orderTypeNumberService;

    @Value("${scm.urls.base:}")
    private String urlBase;
    // 设置运输公司信息
    @Value("${scm.urls.accept-order-info-numbers:}")
    private String urlAcceptOrderInfoNumbers;

    @Autowired
    private IClientSecretKeyService clientSecretKeyService;

    public String generationOrderNo(Long legalId, Integer integer, String classStatus) {
        //生成订单号
        String legalCode = (String) oauthClient.getLegalEntityCodeByLegalId(legalId).getData();
        String preOrder = null;
        String classCode = null;
        if (classStatus.equals("main")) {
            preOrder = OrderTypeEnum.JYD.getCode() + legalCode;
            classCode = OrderTypeEnum.JYD.getCode();
        }
        if (classStatus.equals(OrderStatusEnum.FWD.getCode())) {
            preOrder = OrderTypeEnum.FW.getCode() + legalCode;
            classCode = OrderTypeEnum.FW.getCode();
        }
        if (classStatus.equals(OrderStatusEnum.HY.getCode())) {
            if (integer.equals(1)) {
                preOrder = OrderTypeEnum.SI.getCode() + legalCode;
                classCode = OrderTypeEnum.SI.getCode();
            } else {
                preOrder = OrderTypeEnum.SE.getCode() + legalCode;
                classCode = OrderTypeEnum.SE.getCode();
            }
        }
        if (classStatus.equals(OrderStatusEnum.ZGYS.getCode())) {
            if (integer.equals(1)) {
                preOrder = OrderTypeEnum.TI.getCode() + legalCode;
                classCode = OrderTypeEnum.TI.getCode();
            } else {
                preOrder = OrderTypeEnum.TE.getCode() + legalCode;
                classCode = OrderTypeEnum.TE.getCode();
            }
        }
        if (classStatus.equals(OrderStatusEnum.TC.getCode())) {
            if (integer.equals(1)) {
                preOrder = OrderTypeEnum.TTI.getCode() + legalCode;
                classCode = OrderTypeEnum.TTI.getCode();
            } else {
                preOrder = OrderTypeEnum.TTE.getCode() + legalCode;
                classCode = OrderTypeEnum.TTE.getCode();
            }
        }
        if (classStatus.equals(OrderStatusEnum.NLYS.getCode())) {
            preOrder = OrderTypeEnum.TL.getCode() + legalCode;
            classCode = OrderTypeEnum.TL.getCode();
        }
        if (classStatus.equals(OrderStatusEnum.CBG.getCode())) {
            if (integer.equals(1)) {
                preOrder = OrderTypeEnum.BI.getCode() + legalCode;
                classCode = OrderTypeEnum.BI.getCode();
            } else {
                preOrder = OrderTypeEnum.BE.getCode() + legalCode;
                classCode = OrderTypeEnum.BE.getCode();
            }

        }
        if (classStatus.equals(OrderStatusEnum.KY.getCode())) {
            if (integer.equals(1)) {
                preOrder = OrderTypeEnum.AI.getCode() + legalCode;
                classCode = OrderTypeEnum.AI.getCode();
            } else {
                preOrder = OrderTypeEnum.AE.getCode() + legalCode;
                classCode = OrderTypeEnum.AE.getCode();
            }
        }
        if (classStatus.equals(OrderStatusEnum.CC.getCode())) {
            if (integer.equals(1)) {
                preOrder = OrderTypeEnum.RK.getCode() + legalCode;
                classCode = OrderTypeEnum.RK.getCode();
            } else if (integer.equals(2)) {
                preOrder = OrderTypeEnum.CK.getCode() + legalCode;
                classCode = OrderTypeEnum.CK.getCode();
            } else {
                preOrder = OrderTypeEnum.IO.getCode() + legalCode;
                classCode = OrderTypeEnum.IO.getCode();
            }
        }
        String orderNo = orderTypeNumberService.getOrderNo(preOrder, classCode);
        return orderNo;
    }

    @Override
    public String oprMainOrder(InputMainOrderForm form, String loginUserName) {
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);

        //生成主订单号
        if (form.getCmd().equals("submit")) {
            if (form.getOrderId() == null) {
                String orderNo = generationOrderNo(form.getLegalEntityId(), 0, "main");
                orderInfo.setOrderNo(orderNo);
            }
            if (form.getStatus() != null && form.getStatus().equals(2)) {
                String orderNo = generationOrderNo(form.getLegalEntityId(), 0, "main");
                orderInfo.setOrderNo(orderNo);
            }

        }
        if (form.getCmd().equals("preSubmit") && form.getOrderId() == null) {
            //生成主订单号
            String orderNo = StringUtils.loadNum(CommonConstant.M, 12);
            while (true) {
                if (!isExistOrder(orderNo)) {//重复
                    orderNo = StringUtils.loadNum(CommonConstant.M, 12);
                } else {
                    break;
                }
            }
            orderInfo.setOrderNo(orderNo);
        }

        if (form != null && form.getOrderId() != null) {//修改
            //修改时也要返回主订单号
            OrderInfo oldOrder = baseMapper.selectById(form.getOrderId());
            orderInfo.setId(form.getOrderId());
            orderInfo.setUpTime(LocalDateTime.now());
//            orderInfo.setIsRejected(false);
//            orderInfo.setRejectComment(" ");
            orderInfo.setUpUser(UserOperator.getToken() == null ? loginUserName : UserOperator.getToken());
        } else {//新增

            orderInfo.setCreateTime(LocalDateTime.now());
            orderInfo.setCreatedUser(UserOperator.getToken() == null ? loginUserName : UserOperator.getToken());
        }
        if (CommonConstant.PRE_SUBMIT.equals(form.getCmd())) {
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_2.getCode()));
        } else if (CommonConstant.SUBMIT.equals(form.getCmd()) && CommonConstant.VALUE_1.equals(form.getIsDataAll())) {
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_1.getCode()));
        } else if (CommonConstant.SUBMIT.equals(form.getCmd()) && CommonConstant.VALUE_0.equals(form.getIsDataAll())) {
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_4.getCode()));
        } else {
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_1.getCode()));
        }
        saveOrUpdate(orderInfo);
        form.setNextStatus(orderInfo.getStatus());
        return orderInfo.getOrderNo();
    }


    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no", orderNo);
        List<OrderInfo> orderInfoList = baseMapper.selectList(queryWrapper);
        if (orderInfoList == null || orderInfoList.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public IPage<OrderInfoVO> findOrderInfoByPage(QueryOrderInfoForm form) {
        //定义分页参数
        Page<OrderInfoVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<OrderInfoVO> pageInfo = null;

        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        DataControl dataControl = this.oauthClient.getDataPermission(form.getLoginUserName(), UserTypeEnum.EMPLOYEE_TYPE.getCode()).getData();

        if (CommonConstant.GO_CUSTOMS_AUDIT.equals(form.getCmd())) {
            //定义排序规则
            page.addOrder(OrderItem.desc("oi.id"));
            Map<String, Object> callbackParam = new HashMap<>();
            Set<Long> mainOrderIds = this.filterGoCustomsAudit(callbackParam, dataControl, null);
            pageInfo = baseMapper.findGoCustomsAuditByPage(page,
                    form.setMainOrderIds(new ArrayList<>(mainOrderIds)),
                    dataControl);
            //补充数据
            this.supplementaryGoCustomsAuditData(pageInfo, callbackParam);
        } else {
            //定义排序规则
            page.addOrder(OrderItem.desc("id"));
            pageInfo = baseMapper.findOrderInfoByPage(page, form, dataControl);
            //根据主订单查询子订单数据
            List<OrderInfoVO> orderInfoVOs = pageInfo.getRecords();
            if (CollectionUtil.isEmpty(orderInfoVOs)) {
                return pageInfo;
            }
            List<String> mainOrderNos = orderInfoVOs.stream().map(OrderInfoVO::getOrderNo).collect(Collectors.toList());
            //费用状态
//            Map<String, Object> receivableCostStatus = this.orderReceivableCostService.getOrderCostStatus(mainOrderNos, null);
//            Map<String, Object> paymentCostStatus = this.orderPaymentCostService.getOrderCostStatus(mainOrderNos, null);
            Map<String, Object> costStatus = this.getCostStatus(mainOrderNos, null);
            for (OrderInfoVO orderInfoVO : orderInfoVOs) {
                //重组费用状态
                orderInfoVO.assembleCostStatus(costStatus);
            }

//            List<String> mainOrderNoList = orderInfoVOs.stream().map(OrderInfoVO::getOrderNo).collect(Collectors.toList());
//            Map<String, Map<String, Object>> subOrderMap = this.getSubOrderByMainOrderNos(mainOrderNoList);
//            //查询子订单驳回原因
//            this.getSubOrderRejectionMsg(orderInfoVOs, subOrderMap);
//            //组装主订单数据
//            assemblyMasterOrderData(orderInfoVOs, subOrderMap);
        }
        return pageInfo;
    }


    private void supplementaryGoCustomsAuditData(IPage<OrderInfoVO> pageInfo, Map<String, Object> callbackParam) {
        List<OrderInfoVO> records = pageInfo.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return;
        }
        //获取中港信息
        Object tmsOrders = callbackParam.get("tmsOrders");

        Set<String> classCodes = records.stream().map(OrderInfoVO::getClassCode).collect(Collectors.toSet());
        List<ProductClassify> productClassifies = this.productClassifyService.getIdCodes(new ArrayList<>(classCodes));
        Map<String, String> map = productClassifies.stream().collect(Collectors.toMap(ProductClassify::getIdCode, ProductClassify::getName));
        pageInfo.getRecords().forEach(e -> {
            e.setClassCodeDesc(map.get(e.getClassCode()));
        });
    }


    @Override
    public InputMainOrderVO getMainOrderById(Long idValue) {
        return baseMapper.getMainOrderById(idValue);
    }

    @Override
    public Long getIdByOrderNo(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no", orderNo);
        OrderInfo orderInfo = baseMapper.selectOne(queryWrapper);
        if (orderInfo == null) {
            return 0L;
        }
        return orderInfo.getId();
    }


    @Override
    public boolean saveOrUpdateCost(InputCostForm form) {
        try {
            InputMainOrderVO inputOrderVO = getMainOrderById(form.getMainOrderId());
            if (inputOrderVO == null) {
                return false;
            }
            List<InputPaymentCostForm> paymentCostForms = form.getPaymentCostList();
            List<InputReceivableCostForm> receivableCostForms = form.getReceivableCostList();
            List<InputPaymentCostForm> newPaymentCostForms = new ArrayList<>();
            List<InputReceivableCostForm> newReceivableCostForms = new ArrayList<>();
            //1.如果是暂存情况下,已提交的费用不再次处理
            //2.空数据不进行保存处理
            //3.已审核通过的不再次处理
            for (InputPaymentCostForm inputPaymentCost : paymentCostForms) {
                if ((!OrderStatusEnum.COST_2.getCode().equals(inputPaymentCost.getStatus())) &&
                        !StringUtil.isNullOrEmpty(inputPaymentCost.getCostCode()) &&
                        (!OrderStatusEnum.COST_3.getCode().equals(inputPaymentCost.getStatus()))) {
                    newPaymentCostForms.add(inputPaymentCost);
                }
            }
            for (InputReceivableCostForm inputReceivableCost : receivableCostForms) {
                if ((!OrderStatusEnum.COST_2.getCode().equals(inputReceivableCost.getStatus())) &&
                        !StringUtil.isNullOrEmpty(inputReceivableCost.getCostCode()) &&
                        (!OrderStatusEnum.COST_3.getCode().equals(inputReceivableCost.getStatus()))) {
                    newReceivableCostForms.add(inputReceivableCost);
                }
            }
            List<OrderPaymentCost> orderPaymentCosts = ConvertUtil.convertList(newPaymentCostForms, OrderPaymentCost.class);
            List<OrderReceivableCost> orderReceivableCosts = ConvertUtil.convertList(newReceivableCostForms, OrderReceivableCost.class);
            //业务场景:暂存时提交所有未提交审核的信息,为了避免用户删除一条然后又添加的情况，每次暂存前先把原来未提交审核的清空
            //TODO 根据传入应收/应付订单,对比费用,增删改查

            if ("preSubmit_main".equals(form.getCmd()) || "preSubmit_sub".equals(form.getCmd())) {
                this.costOperationComparison(form, inputOrderVO);
            }
            //当录入的是子订单费用,且主/子订单的法人主体和结算单位不相等时,不可汇总到主订单
            Boolean isSumToMain = true;//1
            if ("preSubmit_main".equals(form.getCmd()) || "submit_main".equals(form.getCmd())) {//入主订单费用
                form.setOrderNo(null);//表中是通过有没有子订单来判断这条数据是主订单的费用还是子订单的费用
            } else if ("preSubmit_sub".equals(form.getCmd()) || "submit_sub".equals(form.getCmd())) {//入子订单费用
                if (!(inputOrderVO.getLegalName().equals(form.getSubLegalName()) && inputOrderVO.getUnitCode().equals(form.getSubUnitCode()))) {
                    isSumToMain = false;//0-主，子订单的法人主体和结算单位不一致则不能汇总到主订单
                }
            }

            Map<String, String> customerNameMap = this.customerInfoService.getCustomerName();

            Map<String, String> supplierInfoMap = new HashMap<>();
            for (OrderPaymentCost orderPaymentCost : orderPaymentCosts) {//应付费用
                orderPaymentCost.setMainOrderNo(inputOrderVO.getOrderNo());
                orderPaymentCost.setOrderNo(form.getOrderNo());
                orderPaymentCost.setIsBill("0");//未出账
                orderPaymentCost.setSubType(form.getSubType());
                //客户名称
                String supplierName = supplierInfoMap.get(orderPaymentCost.getCustomerCode());
                if (StringUtils.isEmpty(supplierName)) {
                    List<SupplierInfo> supplierInfos = this.supplierInfoService.getByCondition(new SupplierInfo().setSupplierCode(orderPaymentCost.getCustomerCode()));
                    SupplierInfo supplierInfo = supplierInfos.get(0);
                    orderPaymentCost.setCustomerName(supplierInfo.getSupplierChName());
                    supplierInfoMap.put(orderPaymentCost.getCustomerCode(), supplierInfo.getSupplierChName());
                } else {
                    orderPaymentCost.setCustomerName(supplierName);
                }

                Long departmentId;
                if (isSumToMain) {
                    orderPaymentCost.setLegalName(inputOrderVO.getLegalName());
                    orderPaymentCost.setLegalId(Long.parseLong(oauthClient.getLegalEntityByLegalName(inputOrderVO.getLegalName()).getData().toString()));
                    orderPaymentCost.setUnitCode(inputOrderVO.getUnitCode()).setUnitName(customerNameMap.get(inputOrderVO.getUnitCode()));
                    departmentId = inputOrderVO.getBizBelongDepart();
                } else {
                    orderPaymentCost.setLegalName(form.getSubLegalName());
                    orderPaymentCost.setLegalId(Long.parseLong(oauthClient.getLegalEntityByLegalName(form.getSubLegalName()).getData().toString()));
                    orderPaymentCost.setUnitCode(form.getSubUnitCode()).setUnitName(customerNameMap.get(form.getSubUnitCode()));
                    departmentId = form.getDepartmentId();
                }


                if ("preSubmit_main".equals(form.getCmd()) || "preSubmit_sub".equals(form.getCmd())) {
                    orderPaymentCost.setIsSumToMain(isSumToMain);
                    orderPaymentCost.setCreatedTime(LocalDateTime.now());
                    orderPaymentCost.setCreatedUser(UserOperator.getToken());
                    orderPaymentCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_1.getCode()));
                } else if ("submit_main".equals(form.getCmd()) || "submit_sub".equals(form.getCmd())) {
                    orderPaymentCost.setOptName(UserOperator.getToken());
                    orderPaymentCost.setOptTime(LocalDateTime.now());
                    orderPaymentCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_2.getCode()));
                    //操作部门
                    orderPaymentCost.setDepartmentId(departmentId);
                }
            }

            Map<String, String> customerInfoMap = new HashMap<>();
            for (OrderReceivableCost orderReceivableCost : orderReceivableCosts) {//应收费用
                orderReceivableCost.setMainOrderNo(inputOrderVO.getOrderNo());
                orderReceivableCost.setOrderNo(form.getOrderNo());
                orderReceivableCost.setIsBill("0");//未出账
                orderReceivableCost.setSubType(form.getSubType());

                //客户名称
                String customerName = customerInfoMap.get(orderReceivableCost.getCustomerCode());
                if (StringUtils.isEmpty(customerName)) {
                    CustomerInfo customerInfo = this.customerInfoService.getByCode(orderReceivableCost.getCustomerCode());
                    orderReceivableCost.setCustomerName(customerInfo.getName());
                    customerInfoMap.put(orderReceivableCost.getCustomerCode(), customerInfo.getName());
                } else {
                    orderReceivableCost.setCustomerName(customerName);
                }

                Long departmentId;
                //新增
                if (isSumToMain) {
                    orderReceivableCost.setLegalName(inputOrderVO.getLegalName());
                    orderReceivableCost.setLegalId(Long.parseLong(oauthClient.getLegalEntityByLegalName(inputOrderVO.getLegalName()).getData().toString()));
                    orderReceivableCost.setUnitCode(inputOrderVO.getUnitCode()).setUnitName(customerNameMap.get(inputOrderVO.getUnitCode()));
                    departmentId = inputOrderVO.getBizBelongDepart();
                } else {
                    orderReceivableCost.setLegalName(form.getSubLegalName());
                    orderReceivableCost.setLegalId(Long.parseLong(oauthClient.getLegalEntityByLegalName(form.getSubLegalName()).getData().toString()));
                    orderReceivableCost.setUnitCode(form.getSubUnitCode()).setUnitName(customerNameMap.get(form.getSubUnitCode()));
                    departmentId = form.getDepartmentId();
                }

                if ("preSubmit_main".equals(form.getCmd()) || "preSubmit_sub".equals(form.getCmd())) {
                    orderReceivableCost.setIsSumToMain(isSumToMain);
                    orderReceivableCost.setCreatedTime(LocalDateTime.now());
                    orderReceivableCost.setCreatedUser(UserOperator.getToken());
                    orderReceivableCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_1.getCode()));
                } else if ("submit_main".equals(form.getCmd()) || "submit_sub".equals(form.getCmd())) {
                    orderReceivableCost.setOptName(UserOperator.getToken());
                    orderReceivableCost.setOptTime(LocalDateTime.now());
                    orderReceivableCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_2.getCode()));
                    //操作部门
                    orderReceivableCost.setDepartmentId(departmentId);
                }
            }
            if (orderPaymentCosts.size() > 0) {
                paymentCostService.saveOrUpdateBatch(orderPaymentCosts);
                //批量更新
                if (form.getDepartmentId() != null && ("submit_main".equals(form.getCmd()) || "submit_sub".equals(form.getCmd()))) {
                    paymentCostService.updateByOrderNo(inputOrderVO.getOrderNo(), form.getOrderNo(), new OrderPaymentCost().setDepartmentId(orderPaymentCosts.get(0).getDepartmentId()));
                }


            }
            if (orderReceivableCosts.size() > 0) {
                receivableCostService.saveOrUpdateBatch(orderReceivableCosts);
                //批量更新
                if (form.getDepartmentId() != null && ("submit_main".equals(form.getCmd()) || "submit_sub".equals(form.getCmd()))) {
                    receivableCostService.updateByOrderNo(inputOrderVO.getOrderNo(), form.getOrderNo(), new OrderReceivableCost().setDepartmentId(orderReceivableCosts.get(0).getDepartmentId()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除多余费用
     *
     * @param form
     * @param inputOrderVO
     */
    private void costOperationComparison(InputCostForm form, InputMainOrderVO inputOrderVO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if ("preSubmit_main".equals(form.getCmd())) {
            queryWrapper.eq("main_order_no", inputOrderVO.getOrderNo());
            queryWrapper.isNull("order_no");
        }

        if ("preSubmit_sub".equals(form.getCmd())) {
            queryWrapper.eq("order_no", form.getOrderNo());
        }
        queryWrapper.eq("status", OrderStatusEnum.COST_1.getCode());

        //查询存在费用
        List<OrderPaymentCost> oldPayList = this.paymentCostService.list(queryWrapper);
        Map<Long, Long> oldPayMap = oldPayList.stream().collect(Collectors.toMap(OrderPaymentCost::getId, OrderPaymentCost::getId));
        for (InputPaymentCostForm newPay : form.getPaymentCostList()) {
            oldPayMap.remove(newPay.getId());
        }
        List<OrderReceivableCost> oldReceivableList = this.receivableCostService.list(queryWrapper);
        Map<Long, Long> oldReceivableMap = oldReceivableList.stream().collect(Collectors.toMap(OrderReceivableCost::getId, OrderReceivableCost::getId));
        for (InputReceivableCostForm newReceivable : form.getReceivableCostList()) {
            oldReceivableMap.remove(newReceivable.getId());
        }
        paymentCostService.removeByIds(oldPayMap.values());
        receivableCostService.removeByIds(oldReceivableMap.values());
    }

    /**
     * 供应商费用详情
     *
     * @param form
     */
    @Override
    public InputCostVO getPayCostDetail(GetCostDetailForm form) {
        List<InputPaymentCostVO> paymentCost = this.paymentCostService.findPaymentCost(form);
        InputCostVO inputCostVO = new InputCostVO();
        inputCostVO.setPaymentCostList(paymentCost);
        //计算费用,利润/合计币种
        this.calculateCost(inputCostVO);
        return inputCostVO;
    }

    /**
     * 查询供应商异常费用
     *
     * @param form
     * @return
     */
    @Override
    public InputCostVO getSupplierAbnormalCostDetail(GetCostDetailForm form) {
        List<InputPaymentCostVO> paymentCost = this.paymentCostService.getSupplierAbnormalCostDetail(form);
        InputCostVO inputCostVO = new InputCostVO();
        inputCostVO.setPaymentCostList(paymentCost);
        //计算费用,利润/合计币种
        this.calculateCost(inputCostVO);
        return inputCostVO;
    }

    @Override
    public List<ProductClassifyVO> getOrderModuleNode(Long mainOrderId) {
        //获取
        OrderInfo orderInfo = this.getById(mainOrderId);
        if (orderInfo == null) {
            throw new JayudBizException("当前订单不存在");
        }
        String selectedServer = orderInfo.getSelectedServer();
        Map<String, Boolean> map = new HashMap<>();
        for (String code : selectedServer.split(",")) {
            map.put(code, true);
        }
        //根据classCode获取信息
        List<ProductClassify> classCode = this.productClassifyService.getIdCodes(Collections.singletonList(orderInfo.getClassCode()));
        //查询子集模块
        List<ProductClassify> subModule = this.productClassifyService.getByCondition(new ProductClassify().setFId(classCode.get(0).getId()).setStatus(StatusEnum.ENABLE.getCode().toString()));
        List<ProductClassifyVO> list = new ArrayList<>();
        subModule.forEach(e -> {
            ProductClassifyVO productClassifyVO = new ProductClassifyVO();
            productClassifyVO.setId(e.getId());
            productClassifyVO.setIdCode(e.getIdCode());
            productClassifyVO.setName(e.getName());
            productClassifyVO.setDescs(e.getDescription().split(";"));
            productClassifyVO.setFId(e.getFId());
            productClassifyVO.setModuleSelected(map.get(e.getIdCode()) != null);
            list.add(productClassifyVO);
        });
        return list;
    }

    /**
     * 追加订单模块节点
     *
     * @param form
     */
    @Override
    @Transactional
    public void addOrderModule(InputOrderForm form) {
        InputMainOrderForm inputMainOrderForm = form.getOrderForm();

        String oldMainOrderNo = inputMainOrderForm.getOrderNo() != null ? inputMainOrderForm.getOrderNo() : null;

        inputMainOrderForm.setCmd(form.getCmd());
        String mainOrderNo = inputMainOrderForm.getOrderNo();

        String classCode = inputMainOrderForm.getClassCode();//订单类型
        String selectedServer = inputMainOrderForm.getSelectedServer();//所选服务

        //中港运输并且并且订单状态为驳回或为空或为待接单
        if (OrderStatusEnum.ZGYS.getCode().equals(classCode) ||
                selectedServer.contains(OrderStatusEnum.ZGYSDD.getCode())) {
            //创建中港订单信息
            InputOrderTransportForm orderTransportForm = form.getOrderTransportForm();

            //生成中港订单号
            if (orderTransportForm.getId() == null) {
                String orderNo = generationOrderNo(orderTransportForm.getLegalEntityId(), orderTransportForm.getGoodsType(), OrderStatusEnum.ZGYS.getCode());
                orderTransportForm.setOrderNo(orderNo);
            } else {
                //不更改状态情况下更改数据
                orderTransportForm.setIsGoodsEdit(true);
            }

            if (!selectedServer.contains(OrderStatusEnum.XGQG.getCode())) {
                //若没有选择香港清关,则情况香港清关信息，避免信息有误
                orderTransportForm.setHkLegalName(null);
                orderTransportForm.setHkUnitCode(null);
                orderTransportForm.setIsHkClear(null);
            }
            orderTransportForm.setMainOrderNo(mainOrderNo);
            orderTransportForm.setLoginUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());

            //根据主订单获取提货地址送货地址得客户ID
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id_code", inputMainOrderForm.getCustomerCode());
            CustomerInfo customerInfo = customerInfoService.getOne(queryWrapper);
            List<InputOrderTakeAdrForm> takeAdrForms1 = orderTransportForm.getTakeAdrForms1();
            List<InputOrderTakeAdrForm> takeAdrForms2 = orderTransportForm.getTakeAdrForms2();
            for (InputOrderTakeAdrForm takeAdrForm1 : takeAdrForms1) {
                takeAdrForm1.setCustomerId(customerInfo.getId());
            }
            for (InputOrderTakeAdrForm takeAdrForm2 : takeAdrForms2) {
                takeAdrForm2.setCustomerId(customerInfo.getId());
            }

            //
            tmsClient.createOrderTransport(orderTransportForm).getData();

        }
        //内陆运输
        if (selectedServer.contains(OrderStatusEnum.NLDD.getCode())) {
            InputOrderInlandTransportForm orderInlandTransportForm = form.getOrderInlandTransportForm();

            //生成内陆订单号
            if (orderInlandTransportForm.getId() == null) {
                String orderNo = generationOrderNo(orderInlandTransportForm.getLegalEntityId(), null, OrderStatusEnum.NLYS.getCode());
                orderInlandTransportForm.setOrderNo(orderNo);
            } else {
                return;
            }

            Integer processStatus = orderInlandTransportForm.getProcessStatus() == null ? ProcessStatusEnum.PROCESSING.getCode()
                    : orderInlandTransportForm.getProcessStatus();
            //查询结算单位
            String orderNo = orderInlandTransportForm.getOrderNo();
            CustomerInfo unitName = this.customerInfoService.getByCode(orderInlandTransportForm.getUnitCode());
            orderInlandTransportForm.setMainOrderNo(mainOrderNo);
            orderInlandTransportForm.setOrderNo(orderNo);
            orderInlandTransportForm.setCreateUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());
            orderInlandTransportForm.setProcessStatus(processStatus);
            orderInlandTransportForm.setUnitName(unitName.getName());
            String subOrderNo = this.inlandTpClient.createOrder(orderInlandTransportForm).getData();
            orderInlandTransportForm.setOrderNo(subOrderNo);

            this.initProcessNode(mainOrderNo, orderNo, OrderStatusEnum.NLYS,
                    form, orderInlandTransportForm.getId(), OrderStatusEnum.getInlandTPProcess());

        }

        //空运
        if (selectedServer.contains(OrderStatusEnum.KYDD.getCode())) {
            InputAirOrderForm airOrderForm = form.getAirOrderForm();
            //生成空运订单号
            if (airOrderForm.getId() == null) {
                String orderNo = generationOrderNo(airOrderForm.getLegalEntityId(), airOrderForm.getImpAndExpType(), OrderStatusEnum.KY.getCode());
                airOrderForm.setOrderNo(orderNo);
            } else {
                return;
            }

            //拼装地址信息
            airOrderForm.assemblyAddress();
            airOrderForm.setMainOrderNo(mainOrderNo);
            airOrderForm.setCreateUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());
            Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                    : ProcessStatusEnum.DRAFT.getCode();
            airOrderForm.setProcessStatus(processStatus);
            this.freightAirClient.createOrder(airOrderForm);
        }
        //服务单
//        if (OrderStatusEnum.FWD.getCode().equals(classCode)) {
//            //创建服务单订单信息
//            InputOrderServiceForm orderServiceForm = form.getOrderServiceForm();
//            //生成服务订单号
//            orderServiceForm.setMainOrderNo(mainOrderNo);
//            orderServiceForm.setLoginUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());
//            serviceOrderService.createOrder(orderServiceForm);
//
//        }
        //海运
        //System.out.println(OrderStatusEnum.HY.getCode().equals(classCode));
        if (selectedServer.contains(OrderStatusEnum.HYDD.getCode())) {
            InputSeaOrderForm seaOrderForm = form.getSeaOrderForm();
            //生成海运订单号
            if (seaOrderForm.getOrderId() == null) {
                String orderNo = generationOrderNo(seaOrderForm.getLegalEntityId(), seaOrderForm.getImpAndExpType(), OrderStatusEnum.HY.getCode());
                seaOrderForm.setOrderNo(orderNo);
            } else {
                return;
            }

            //拼装地址信息
            seaOrderForm.assemblyAddress();
            seaOrderForm.setMainOrderNo(mainOrderNo);
            seaOrderForm.setCreateUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());
            Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                    : ProcessStatusEnum.DRAFT.getCode();
            seaOrderForm.setProcessStatus(processStatus);
            String subOrderNo = this.oceanShipClient.createOrder(seaOrderForm).getData();
            seaOrderForm.setOrderNo(subOrderNo);
            this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.HY,
                    form, seaOrderForm.getOrderId(), OrderStatusEnum.getSeaOrderProcess());
        }
        //拖车
        if (selectedServer.contains(OrderStatusEnum.TCEDD.getCode())
                || selectedServer.contains(OrderStatusEnum.TCIDD.getCode())) {
            List<InputTrailerOrderFrom> trailerOrderFroms = form.getTrailerOrderFrom();

            List<String> strings = new ArrayList<>();
            strings.add("TT_1_1");
            strings.add("TT_2_1");
            strings.add("TT_3_1");
            strings.add("TT_4_1");
            boolean flag = false;
            for (String string : strings) {
                for (InputTrailerOrderFrom trailerOrderFrom : trailerOrderFroms) {
                    if (trailerOrderFrom.getStatus() != null && string.equals(trailerOrderFrom.getStatus())) {
                        flag = true;
                    }
                }
            }
            if (flag) {
                for (InputTrailerOrderFrom trailerOrderFrom : trailerOrderFroms) {
                    if (trailerOrderFrom.getId() != null) continue;
                    trailerOrderFrom.setMainOrderNo(mainOrderNo);
                    trailerOrderFrom.setCreateUser(UserOperator.getToken());
                    Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                            : ProcessStatusEnum.DRAFT.getCode();
                    trailerOrderFrom.setProcessStatus(processStatus);
                    String subOrderNo = this.trailerClient.createOrder(trailerOrderFrom).getData();
                    trailerOrderFrom.setOrderNo(subOrderNo);

                    this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.TC,
                            form, trailerOrderFrom.getId(), OrderStatusEnum.getTrailerOrderProcess());
                }
            } else {

                if (trailerOrderFroms.get(0).getMainOrderNo() != null) {
                    List<String> data = trailerClient.getOrderNosByMainOrderNo(trailerOrderFroms.get(0).getMainOrderNo()).getData();
                    if (data != null && data.size() > 0) {
                        goodsService.deleteGoodsByBusOrders(data, BusinessTypeEnum.TC.getCode());
                        orderAddressService.deleteOrderAddressByBusOrders(data, BusinessTypeEnum.TC.getCode());
                        log.warn("删除商品和地址信息成功");
                    }
                }

                if (form.getCmd().equals("submit") && trailerOrderFroms.get(0).getStatus() != null && trailerOrderFroms.get(0).getStatus().equals("TT_0")) {
                    ApiResult result = trailerClient.deleteOrderByMainOrderNo(trailerOrderFroms.get(0).getMainOrderNo());
                    if (result.getCode() != HttpStatus.SC_OK) {
                        log.warn("删除订单信息失败");
                    }
                }

                for (InputTrailerOrderFrom trailerOrderFrom : trailerOrderFroms) {

                    //生成拖车订单号
                    if (trailerOrderFrom.getId() == null && trailerOrderFrom.getStatus() == null) {
                        String orderNo = generationOrderNo(trailerOrderFrom.getLegalEntityId(), trailerOrderFrom.getImpAndExpType(), OrderStatusEnum.TC.getCode());
                        trailerOrderFrom.setOrderNo(orderNo);
                    } else {
                        continue;
                    }


                    trailerOrderFrom.setMainOrderNo(mainOrderNo);
                    trailerOrderFrom.setCreateUser(UserOperator.getToken());
                    Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                            : ProcessStatusEnum.DRAFT.getCode();
                    trailerOrderFrom.setProcessStatus(processStatus);
                    String subOrderNo = this.trailerClient.createOrder(trailerOrderFrom).getData();
                    trailerOrderFrom.setOrderNo(subOrderNo);

                    this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.TC,
                            form, trailerOrderFrom.getId(), OrderStatusEnum.getTrailerOrderProcess());
                }


            }


        }

        //仓储
        if (selectedServer.contains(OrderStatusEnum.CCIDD.getCode())
                || selectedServer.contains(OrderStatusEnum.CCEDD.getCode())
                || selectedServer.contains(OrderStatusEnum.CCFDD.getCode())) {
            if (selectedServer.contains(OrderStatusEnum.CCIDD.getCode())) {
                InputStorageInputOrderForm storageInputOrderForm = form.getStorageInputOrderForm();
                if (storageInputOrderForm.getId() == null) {
                    String orderNo = generationOrderNo(storageInputOrderForm.getLegalEntityId(), 1, OrderStatusEnum.CC.getCode());
                    storageInputOrderForm.setOrderNo(orderNo);
                } else {
                    return;
                }

                storageInputOrderForm.setMainOrderNo(mainOrderNo);
                for (AddWarehouseGoodsForm addWarehouseGoodsForm : storageInputOrderForm.getGoodsFormList()) {
                    addWarehouseGoodsForm.setFileName(StringUtils.getFileNameStr(addWarehouseGoodsForm.getTakeFiles()));
                    addWarehouseGoodsForm.setFilePath(StringUtils.getFileStr(addWarehouseGoodsForm.getTakeFiles()));
                }
                Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                        : ProcessStatusEnum.DRAFT.getCode();
                storageInputOrderForm.setProcessStatus(processStatus);
                String subOrderNo = this.storageClient.createInOrder(storageInputOrderForm).getData();
                storageInputOrderForm.setOrderNo(subOrderNo);

                this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.CC,
                        form, storageInputOrderForm.getId(), OrderStatusEnum.getInStorageOrderProcess());

            }
            if (selectedServer.contains(OrderStatusEnum.CCEDD.getCode())) {
                InputStorageOutOrderForm storageOutOrderForm = form.getStorageOutOrderForm();
                if (storageOutOrderForm.getId() == null) {
                    String orderNo = generationOrderNo(storageOutOrderForm.getLegalEntityId(), 2, OrderStatusEnum.CC.getCode());
                    storageOutOrderForm.setOrderNo(orderNo);
                } else {
                    return;
                }

                storageOutOrderForm.setMainOrderNo(mainOrderNo);
                storageOutOrderForm.setCmd(form.getCmd());
                for (AddWarehouseGoodsForm addWarehouseGoodsForm : storageOutOrderForm.getGoodsFormList()) {
                    addWarehouseGoodsForm.setFileName(StringUtils.getFileNameStr(addWarehouseGoodsForm.getTakeFiles()));
                    addWarehouseGoodsForm.setFilePath(StringUtils.getFileStr(addWarehouseGoodsForm.getTakeFiles()));
                }
                Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                        : ProcessStatusEnum.DRAFT.getCode();
                storageOutOrderForm.setProcessStatus(processStatus);
                String subOrderNo = this.storageClient.createOutOrder(storageOutOrderForm).getData();
                storageOutOrderForm.setOrderNo(subOrderNo);

                this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.CC,
                        form, storageOutOrderForm.getId(), OrderStatusEnum.getOutStorageOrderProcess());
            }
            if (selectedServer.contains(OrderStatusEnum.CCFDD.getCode())) {
                InputStorageFastOrderForm storageFastOrderForm = form.getStorageFastOrderForm();
                if (storageFastOrderForm.getId() == null) {
                    String orderNo = generationOrderNo(storageFastOrderForm.getLegalEntityId(), 3, OrderStatusEnum.CC.getCode());
                    storageFastOrderForm.setOrderNo(orderNo);
                } else {
                    return;
                }

                storageFastOrderForm.setMainOrderNo(mainOrderNo);
                if (CollectionUtils.isNotEmpty(storageFastOrderForm.getInGoodsFormList())) {
                    for (AddWarehouseGoodsForm addWarehouseGoodsForm : storageFastOrderForm.getInGoodsFormList()) {
                        addWarehouseGoodsForm.setFileName(StringUtils.getFileNameStr(addWarehouseGoodsForm.getTakeFiles()));
                        addWarehouseGoodsForm.setFilePath(StringUtils.getFileStr(addWarehouseGoodsForm.getTakeFiles()));
                    }
                }
                if (CollectionUtils.isNotEmpty(storageFastOrderForm.getOutGoodsFormList())) {
                    for (AddWarehouseGoodsForm addWarehouseGoodsForm : storageFastOrderForm.getOutGoodsFormList()) {
                        addWarehouseGoodsForm.setFileName(StringUtils.getFileNameStr(addWarehouseGoodsForm.getTakeFiles()));
                        addWarehouseGoodsForm.setFilePath(StringUtils.getFileStr(addWarehouseGoodsForm.getTakeFiles()));
                    }
                }
                if (CollectionUtils.isNotEmpty(storageFastOrderForm.getFastGoodsFormList())) {
                    for (AddWarehouseGoodsForm addWarehouseGoodsForm : storageFastOrderForm.getFastGoodsFormList()) {
                        addWarehouseGoodsForm.setFileName(StringUtils.getFileNameStr(addWarehouseGoodsForm.getTakeFiles()));
                        addWarehouseGoodsForm.setFilePath(StringUtils.getFileStr(addWarehouseGoodsForm.getTakeFiles()));
                    }
                }

                Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                        : ProcessStatusEnum.DRAFT.getCode();
                storageFastOrderForm.setProcessStatus(processStatus);
                String subOrderNo = this.storageClient.createFastOrder(storageFastOrderForm).getData();
                storageFastOrderForm.setOrderNo(subOrderNo);

                if (storageFastOrderForm.getIsWarehouse().equals(1)) {
                    this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.CC,
                            form, storageFastOrderForm.getId(), OrderStatusEnum.getFastStorageOrderProcess());
                }

            }
        }

        //纯报关和出口报关并且订单状态为驳回(C_1_1)或为空或为暂存待补全的待接单
        if (selectedServer.contains(OrderStatusEnum.CKBG.getCode())) {
            InputOrderCustomsForm orderCustomsForm = form.getOrderCustomsForm();
            //生成报关订单号
            if (CollectionUtil.isNotEmpty(orderCustomsForm.getSubOrders())) {
                for (InputSubOrderCustomsForm subOrder : orderCustomsForm.getSubOrders()) {
                    if (subOrder.getSubOrderId() == null) {
                        String orderNo = generationOrderNo(orderCustomsForm.getLegalEntityId(), orderCustomsForm.getGoodsType(), OrderStatusEnum.CBG.getCode());
                        subOrder.setOrderNo(orderNo);
                    } else {
                        continue;
                    }
                }
            }

            //如果没有生成子订单则不调用
            if (orderCustomsForm.getSubOrders() != null) {

                if (oldMainOrderNo != null) {
                    orderCustomsForm.setOldMainOrderNo(oldMainOrderNo);
                }

                orderCustomsForm.setMainOrderNo(mainOrderNo);
                if (OrderStatusEnum.CBG.getCode().equals(classCode)) {
                    orderCustomsForm.setClassCode(OrderStatusEnum.CBG.getCode());
                } else {
                    orderCustomsForm.setClassCode(OrderStatusEnum.CKBG.getCode());
                }
                orderCustomsForm.setLoginUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());
                orderCustomsForm.setMainOrderStatus(inputMainOrderForm.getNextStatus());
                customsClient.createOrderCustoms(orderCustomsForm);

            }
        }
        //查询主单信息
        OrderInfo orderInfo = this.getById(inputMainOrderForm.getOrderId());
        this.updateById(new OrderInfo().setId(orderInfo.getId()).setIsDataAll(inputMainOrderForm.getIsDataAll()).setSelectedServer(orderInfo.getSelectedServer() + "," + inputMainOrderForm.getSelectedServer()));


    }


    @Override
    public InputCostVO getCostDetail(GetCostDetailForm form) {
        List<InputPaymentCostVO> inputPaymentCostVOS = paymentCostService.findPaymentCost(form);
        //供应商过滤
        List<InputPaymentCostVO> payCost = inputPaymentCostVOS.stream()
                .filter(e -> e.getSupplierId() == null
                        || (e.getSupplierId() != null
                        && (OrderStatusEnum.COST_2.getCode().equals(e.getStatus().toString())
                        || OrderStatusEnum.COST_3.getCode().equals(e.getStatus().toString())))).collect(Collectors.toList());

        List<InputReceivableCostVO> inputReceivableCostVOS = receivableCostService.findReceivableCost(form);

        Map<String, List<InitComboxVO>> costType = this.costInfoService.initCostTypeByCostInfoCode();

        inputReceivableCostVOS.stream().forEach(e -> e.setCategorys(costType.get(e.getCostCode())));
        payCost.stream().forEach(e -> e.setCategorys(costType.get(e.getCostCode())));

        InputCostVO inputCostVO = new InputCostVO();
        inputCostVO.setPaymentCostList(payCost);
        inputCostVO.setReceivableCostList(inputReceivableCostVOS);
        //计算费用,利润/合计币种
        this.calculateCost(inputCostVO);

        return inputCostVO;
    }

    /**
     * 根据费用id查询所有费用并且统计
     *
     * @param reCostIds
     * @param payCostIds
     * @return
     */
    @Override
    public InputCostVO getCostDetailByCostIds(List<Long> reCostIds, List<Long> payCostIds) {
        List<OrderReceivableCost> receivableCosts = new ArrayList<>();
        List<OrderPaymentCost> paymentCosts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reCostIds)) {
            receivableCosts = this.orderReceivableCostService.listByIds(reCostIds);
        }
        if (!CollectionUtils.isEmpty(payCostIds)) {
            paymentCosts = this.orderPaymentCostService.listByIds(payCostIds);
        }

        //费用名称
        Map<String, String> costInfoMap = this.costInfoService.list().stream().collect(Collectors.toMap(e -> e.getIdCode(), e -> e.getName()));
        //费用类别(作业环节)
        Map<Long, String> costTypeMap = this.costTypeService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getCodeName()));
        //费用类型
        Map<Long, String> costGenreMap = this.costGenreService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
        //币种
        Map<String, String> currencyMap = this.currencyInfoService.initCurrencyInfo().stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));

        List<InputReceivableCostVO> reCosts = ConvertUtil.convertList(receivableCosts, InputReceivableCostVO.class);
        List<InputPaymentCostVO> payCosts = ConvertUtil.convertList(paymentCosts, InputPaymentCostVO.class);

        reCosts.forEach(e -> {
            e.setCostName(costInfoMap.get(e.getCostCode()));
            e.setCostType(costTypeMap.get(e.getCostTypeId()));
            e.setCostGenre(costGenreMap.get(e.getCostGenreId()));
            e.setCurrencyName(currencyMap.get(e.getCurrencyCode()));
        });

        payCosts.forEach(e -> {
            e.setCostName(costInfoMap.get(e.getCostCode()));
            e.setCostType(costTypeMap.get(e.getCostTypeId()));
            e.setCostGenre(costGenreMap.get(e.getCostGenreId()));
            e.setCurrencyName(currencyMap.get(e.getCurrencyCode()));
        });

        InputCostVO inputCostVO = new InputCostVO();
        inputCostVO.setPaymentCostList(payCosts);
        inputCostVO.setReceivableCostList(reCosts);
        //计算费用,利润/合计币种
        this.calculateCost(inputCostVO);

        return inputCostVO;
    }

    /**
     * 根据法人主体ids查询订单
     *
     * @param legalIds
     * @return
     */
    @Override
    public List<OrderInfo> getByLegalEntityIds(List<Long> legalIds) {
        QueryWrapper<OrderInfo> condition = new QueryWrapper<>();
        condition.lambda().in(OrderInfo::getLegalEntityId, legalIds);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<Map<String, Integer>> getMainOrderSummary(QueryStatisticalReport form, List<Long> legalIds) {
        return this.baseMapper.getMainOrderSummary(form, legalIds);
    }

    /**
     * 获取统计基础数据
     *
     * @param form
     * @param legalIds
     * @param orderInfo
     * @return
     */
    @Override
    public List<OrderInfoVO> getBasicStatistics(QueryStatisticalReport form, List<Long> legalIds, OrderInfo orderInfo) {
        return this.baseMapper.getBasicStatistics(form, orderInfo, legalIds);
    }

    @Override
    public Integer pendingExternalCustomsDeclarationNum(List<Long> legalIds, String userName) {
        return this.baseMapper.pendingCustomsDeclarationNum(legalIds, userName);
    }


    /**
     * 计算费用,利润/合计币种
     *
     * @param inputCostVO
     */
    @Override
    public void calculateCost(InputCostVO inputCostVO) {
        Map<String, BigDecimal> receivableCost = new HashMap<>();//应收币种
        Map<String, BigDecimal> paymentCost = new HashMap<>();//应付币种
        //应付总本币
        BigDecimal receivableCostTotal = new BigDecimal(0);
        //应收总本币
        BigDecimal paymentCostTotal = new BigDecimal(0);
        //计算应收
        for (InputReceivableCostVO receivableCostVO : inputCostVO.getReceivableCostList()) {
            if (receivableCostVO.getAmount() == null || receivableCostVO.getChangeAmount() == null) return;
            receivableCost.merge(receivableCostVO.getCurrencyName(), receivableCostVO.getAmount(), BigDecimal::add);
            //合计应收本币金额
            receivableCostTotal = receivableCostTotal.add(receivableCostVO.getChangeAmount());
        }
        //计算应付
        for (InputPaymentCostVO paymentCostVO : inputCostVO.getPaymentCostList()) {
            if (paymentCostVO.getAmount() == null || paymentCostVO.getChangeAmount() == null) return;
            paymentCost.merge(paymentCostVO.getCurrencyName(), paymentCostVO.getAmount(), BigDecimal::add);
            //合计应付本币金额
            paymentCostTotal = paymentCostTotal.add(paymentCostVO.getChangeAmount());
        }
        //计算利润
        inputCostVO.setProfit(receivableCostTotal.subtract(paymentCostTotal));
        //拼接应收币种数量
        StringBuffer receivableCostStr = new StringBuffer();
        receivableCost.forEach((k, v) -> {
            receivableCostStr.append(v).append(k).append(" ");
        });
        StringBuffer paymentCostStr = new StringBuffer();
        paymentCost.forEach((k, v) -> {
            paymentCostStr.append(v).append(k).append(" ");
        });
        inputCostVO.setCurrencyReceivable(receivableCostStr.toString());
        inputCostVO.setCurrencyPayable(paymentCostStr.toString());
    }

    @Override
    @Transactional
    public boolean auditCost(AuditCostForm form) {
        try {
            List<OrderPaymentCost> orderPaymentCosts = form.getPaymentCosts();
            List<OrderReceivableCost> orderReceivableCosts = form.getReceivableCosts();
            //查询应收审核通过数量
//            this.receivableCostService.getApprovalFeeCount()
            for (OrderPaymentCost paymentCost : orderPaymentCosts) {
                paymentCost.setStatus(Integer.valueOf(form.getStatus()));
                paymentCost.setRemarks(form.getRemarks());
            }
            for (OrderReceivableCost receivableCost : orderReceivableCosts) {
                receivableCost.setStatus(Integer.valueOf(form.getStatus()));
                receivableCost.setRemarks(form.getRemarks());
            }
            //费用合并操作
            this.costMergeOpt(form);

            boolean optOne = false;
            if (orderPaymentCosts.size() > 0) {
                paymentCostService.saveOrUpdateBatch(orderPaymentCosts);
                //实报实销操作
                this.doReimbursementOpt(form);
            }
            if (orderReceivableCosts.size() > 0) {
                Map<String, String> customerInfoMap = this.customerInfoService.list().stream().filter(e -> !StringUtils.isEmpty(e.getIdCode())).collect(Collectors.toMap(e -> e.getIdCode(), e -> e.getName()));
                orderReceivableCosts.stream().filter(e -> StringUtils.isEmpty(e.getCustomerName())).forEach(e -> e.setCustomerName(customerInfoMap.get(e.getCustomerCode())));
                optOne = receivableCostService.saveOrUpdateBatch(orderReceivableCosts);
            }
            //推送应收费用审核消息
            if (optOne && "3".equals(form.getStatus())) { //只能推子订单
                this.receivableAuditMsgPush(orderReceivableCosts);
            }

        } catch (Exception e) {
            if (e instanceof JayudBizException) throw new JayudBizException(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public List<OrderStatusVO> handleProcess(QueryOrderStatusForm form) {
        List<OrderStatusVO> orderStatusVOS = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("class_code", form.getClassCode());
        queryWrapper.eq("status", "1");
        queryWrapper.ne("sorts", 0);
        List<OrderStatus> allProcess = orderStatusService.list(queryWrapper);//所有流程

        allProcess.sort((h1, h2) -> {//排序
            if (h1.getFId().equals(h2.getFId())) {
                return Integer.compare(h1.getSorts(), h2.getSorts());
            }
            return Integer.compare(h1.getFId(), h2.getFId());

        });

        allProcess.forEach(x -> {
            OrderStatusVO orderStatus = new OrderStatusVO();
            orderStatus.setId(x.getId());
            orderStatus.setProcessName(x.getName());
            orderStatus.setProcessCode(x.getIdCode());
            orderStatus.setContainState(x.getContainState());
            orderStatus.setSorts(x.getSorts());
            orderStatus.setStatus();
            if (x.getFId() == 0) {
                orderStatusVOS.add(orderStatus);
            } else {
                orderStatusVOS.forEach(v -> {
                    if (v.getId() == x.getFId()) {
                        v.addChildren(orderStatus);
                    }
                });
            }
        });
        InputMainOrderVO inputMainOrderVO = getMainOrderById(form.getMainOrderId());
        //根据主订单ID获取子订单数量
        int customsNum = 0;//纯报关单数量
        if ((OrderStatusEnum.CBG.getCode().equals(form.getClassCode()) && inputMainOrderVO != null) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CKBG.getCode())
        ) {
            customsNum = customsClient.getCustomsOrderNum(inputMainOrderVO.getOrderNo()).getData();
        }
        //以后有其他的会逐渐增加 TODO
        int finalCustomsNum = customsNum;
        orderStatusVOS.forEach(x -> {
            //循环处理主流程节点,下面有子流程得contain_state字段配置类型区分，不配置状态了
            String containState = x.getContainState();
            if (containState != null && !"".equals(containState)) {
                //循环处理子节点，如纯报关,出口报关,中港等
                if (!x.getChildren().isEmpty()) {
                    for (int i = 0; i < x.getChildren().size(); i++) {
                        OrderStatusVO subOrder = x.getChildren().get(i);
                        String subContainState = subOrder.getContainState();
                        if (subContainState != null && !"".equals(subContainState)) {
                            //目前规定：子订单所包含的操作状态只有一个
                            QueryWrapper subParam = new QueryWrapper();
                            subParam.eq("main_order_id", form.getMainOrderId());
                            subParam.eq("status", subContainState);
                            queryWrapper.isNotNull("order_id");
                            subParam.orderByDesc("created_time");
                            List<LogisticsTrack> subTrack = logisticsTrackService.list(subParam);//已操作的子流程
                            //此处需区分业务场景
                            //纯报关，出口报关
                            if (OrderStatusEnum.CBG.getCode().equals(inputMainOrderVO.getClassCode()) ||
                                    OrderStatusEnum.CKBG.getCode().equals(containState)) {
                                //若子订单流程记录小于子订单数，说明主流程节点状态为进行中
                                Integer sorts = x.getChildren().get(i).getSorts();
                                if (subTrack == null || subTrack.size() == 0) {
                                    if (sorts != 1) {
                                        x.setStatus("2");
                                    } else {//如果子流程的第一个都没有已操作记录，说明主流程未进行，后面的子流程就没必要循环了
                                        break;
                                    }
                                } else if (subTrack != null && subTrack.size() < finalCustomsNum) {
                                    x.setStatus("2");//进行中
                                    subOrder.setStatus("2");
                                } else {
                                    //子节点循环中最后一个流程节点未操作完毕
                                    if (!subOrder.getProcessCode().equals(x.getChildren().get(x.getChildren().size() - 1).getProcessCode())) {
                                        x.setStatus("2");//进行中
                                    } else {
                                        x.setStatus("3");//已完成
                                        x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                    }
                                    subOrder.setStatus("3");
                                    subOrder.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                    subOrder.setOperator(subTrack.get(0).getOperatorUser());
                                }
                            }
                            //中港运输除出口报关其他子流程节点
                            else if ((OrderStatusEnum.ZGYS.getCode().equals(inputMainOrderVO.getClassCode())
                                    || OrderStatusEnum.ZGYSDD.getCode().equals(containState)) && !OrderStatusEnum.CKBG.getCode().equals(containState)) {
                                if (subTrack != null && subTrack.size() > 0) {
                                    subOrder.setStatus("3");//已完成
                                    subOrder.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                    subOrder.setOperator(subTrack.get(0).getOperatorUser());
                                    x.setStatus("2");//进行中
                                    if (subOrder.getProcessCode().equals(x.getChildren().get(x.getChildren().size() - 1).getProcessCode())) {
                                        x.setStatus("3");//已完成
                                        x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                        x.setOperator(subTrack.get(0).getOperatorUser());
                                    }
                                }
                            } else { //没有特殊操作,通用处理
                                if (subTrack != null && subTrack.size() > 0) {
                                    subOrder.setStatus("3");//已完成
                                    subOrder.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                    subOrder.setOperator(subTrack.get(0).getOperatorUser());
                                    x.setStatus("2");//进行中
                                    if (subOrder.getProcessCode().equals(x.getChildren().get(x.getChildren().size() - 1).getProcessCode())) {
                                        if (subOrder.getProcessCode().equals(x.getChildren().get(x.getChildren().size() - 1).getProcessCode())) {
                                            x.setStatus("3");//已完成
                                            x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                            x.setOperator(subTrack.get(0).getOperatorUser());

                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //没有子节点流程的，说明只要有该主流程状态得就是已完成
                    //1.没有子节点流程得contain_state字段值为操作状态一定要配置,可为多个，如:XX1,XX2
                    //并且XX1,XX2操作状态会保存在logistics_track表
                    String[] containStates = containState.split(",");
                    QueryWrapper mainParam = new QueryWrapper();
                    mainParam.eq("main_order_id", form.getMainOrderId());
                    mainParam.in("status", containStates);
                    mainParam.isNotNull("order_id");
                    mainParam.orderByDesc("created_time");
                    List<LogisticsTrack> subTrack = logisticsTrackService.list(mainParam);//XX1,XX2流程是否已经操作
                    if (subTrack != null && subTrack.size() < containStates.length) {
                        x.setStatus("2");//进行中
                    } else if (subTrack.size() == containStates.length) {
                        x.setStatus("3");//已完成
                        x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                        x.setOperator(subTrack.get(0).getOperatorUser());
                    }
                }
            }

        });
        return orderStatusVOS;
    }

    @Override
    public List<OrderStatusVO> handleSubProcess(HandleSubProcessForm form) {
        List<OrderStatusVO> orderStatusVOS = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("class_code", form.getClassCode());
        queryWrapper.eq("status", "1");
        queryWrapper.ne("sorts", 0);
        List<OrderStatus> allProcess = orderStatusService.list(queryWrapper);//所有流程
        allProcess.sort((h1, h2) -> {//排序
            if (h1.getFId().equals(h2.getFId())) {
                return Integer.compare(h1.getSorts(), h2.getSorts());
            }
            return Integer.compare(h1.getFId(), h2.getFId());

        });
        allProcess.forEach(x -> {
            OrderStatusVO orderStatus = new OrderStatusVO();
            orderStatus.setId(x.getId());
            orderStatus.setProcessName(x.getName());
            orderStatus.setProcessCode(x.getIdCode());
            orderStatus.setContainState(x.getContainState());
            orderStatus.setSorts(x.getSorts());
            orderStatus.setStatus();
            if (x.getFId() == 0) {
                orderStatusVOS.add(orderStatus);
            } else {
                orderStatusVOS.forEach(v -> {
                    if (v.getId() == x.getFId()) {
                        v.addChildren(orderStatus);
                    }
                });
            }
        });
        orderStatusVOS.forEach(x -> {
            //循环处理主流程节点
            String containState = x.getContainState();
            if (containState != null && !"".equals(containState)) {
                //循环处理子节点，如纯报关
                if (!x.getChildren().isEmpty()) {
                    for (int i = 0; i < x.getChildren().size(); i++) {
                        OrderStatusVO subOrder = x.getChildren().get(i);
                        String subContainState = subOrder.getContainState();
                        if (subContainState != null && !"".equals(subContainState)) {
                            QueryWrapper subParam = new QueryWrapper();
                            subParam.eq("main_order_id", form.getMainOrderId());
                            subParam.eq("status", subContainState);
                            subParam.orderByDesc("created_time");
                            List<LogisticsTrack> subTrack = logisticsTrackService.list(subParam);//已操作的子流程
                            if (subTrack != null && subTrack.size() > 0) {
                                subOrder.setStatus("3");//已完成
                                subOrder.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                subOrder.setOperator(subTrack.get(0).getOperatorUser());
                                x.setStatus("2");//进行中
                                if (subOrder.getProcessCode().equals(x.getChildren().get(x.getChildren().size() - 1).getProcessCode())) {
                                    x.setStatus("3");//已完成
                                    x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                    x.setOperator(subTrack.get(0).getOperatorUser());
                                }
                            }
                        }
                    }
                } else {//没有子节点流程的
                    String[] containStates = containState.split(",");
                    QueryWrapper mainParam = new QueryWrapper();
                    mainParam.eq("main_order_id", form.getMainOrderId());
                    mainParam.in("status", containStates);
                    mainParam.orderByDesc("created_time");
                    List<LogisticsTrack> subTrack = logisticsTrackService.list(mainParam);//已操作的子流程
                    if (subTrack != null && subTrack.size() < containStates.length) {
                        x.setStatus("2");//进行中
                    } else if (subTrack.size() == containStates.length) {
                        x.setStatus("3");//已完成
                        x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                        x.setOperator(subTrack.get(0).getOperatorUser());
                    }
                }
            }

        });
        return orderStatusVOS;
    }

    @Override
    public InputOrderVO getOrderDetail(GetOrderDetailForm form) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        InputOrderVO inputOrderVO = new InputOrderVO();
        //获取主订单信息
        InputMainOrderVO inputMainOrderVO = getMainOrderById(form.getMainOrderId());
        Object departmentInfo = this.oauthClient.getDepartmentNameById(inputMainOrderVO.getBizBelongDepart()).getData();
        JSONObject department = new JSONObject(departmentInfo);
        inputMainOrderVO.setBizBelongDepartDesc(department.getStr("name"));
        inputMainOrderVO.setCreateUserTypeName(CreateUserTypeEnum.getDesc(inputMainOrderVO.getCreateUserType()));
        inputOrderVO.setOrderForm(inputMainOrderVO);

        Map<Long, String> departmentMap = this.oauthClient.findDepartment().getData().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));

        //获取纯报关和出口报关信息
        if (OrderStatusEnum.CBG.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CKBG.getCode())) {
            InputOrderCustomsVO inputOrderCustomsVO = customsClient.getCustomsDetail(inputMainOrderVO.getOrderNo()).getData();
            if (inputOrderCustomsVO != null) {
                //创建订单页面头附件
                List<FileView> allPics = new ArrayList<>();
                allPics.addAll(inputOrderCustomsVO.getCntrPics());
                allPics.addAll(inputOrderCustomsVO.getEncodePics());
                allPics.addAll(inputOrderCustomsVO.getAirTransportPics());
                allPics.addAll(inputOrderCustomsVO.getSeaTransportPics());
                //其余附件信息
                //获取反馈操作人时上传的附件
                for (InputSubOrderCustomsVO subOrder : inputOrderCustomsVO.getSubOrders()) {
                    List<FileView> attachments = this.logisticsTrackService.getAttachments(subOrder.getSubOrderId()
                            , BusinessTypeEnum.BG.getCode(), prePath);//节点附件
                    allPics.addAll(attachments);
                    if (CollectionUtils.isEmpty(subOrder.getFileViews())) {
                        subOrder.setFileViews(attachments);
                    } else {
                        subOrder.getFileViews().addAll(attachments);
                    }

                    //结算单位名称
                    CustomerInfo customerInfo = customerInfoService.getByCode(subOrder.getUnitCode());
                    if (customerInfo != null) {
                        subOrder.setUnitName(customerInfo.getName());
                    }

                }
                inputOrderCustomsVO.setAllPics(allPics);
                inputOrderCustomsVO.setDepartment(departmentMap.get(inputOrderCustomsVO.getDepartmentId()));

                //循环处理接单人和接单时间
                List<InputSubOrderCustomsVO> inputSubOrderCustomsVOS = inputOrderCustomsVO.getSubOrders();
                for (InputSubOrderCustomsVO inputSubOrderCustomsVO : inputSubOrderCustomsVOS) {
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("order_id", inputSubOrderCustomsVO.getSubOrderId());
                    queryWrapper.eq("status", OrderStatusEnum.CUSTOMS_C_1.getCode());
                    queryWrapper.orderByDesc("created_time");
                    List<LogisticsTrack> logisticsTracks = logisticsTrackService.list(queryWrapper);
                    if (!logisticsTracks.isEmpty()) {
                        inputSubOrderCustomsVO.setJiedanTimeStr(DateUtils.getLocalToStr(logisticsTracks.get(0).getOperatorTime()));
                        inputSubOrderCustomsVO.setJiedanUser(logisticsTracks.get(0).getOperatorUser());
                    }
                }
                inputOrderVO.setOrderCustomsForm(inputOrderCustomsVO);
            }
        }

        //获取中港运输信息
        if (OrderStatusEnum.ZGYS.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.ZGYSDD.getCode())) {
            InputOrderTransportVO inputOrderTransportVO = tmsClient.getOrderTransport(inputMainOrderVO.getOrderNo()).getData();
            if (inputOrderTransportVO != null) {
                //组装车型/柜号
                inputOrderTransportVO.assembleModelAndCntrNo();
                //结算单位名称
                CustomerInfo customerInfo = customerInfoService.getByCode(inputOrderTransportVO.getUnitCode());
                if (customerInfo != null) {
                    inputOrderTransportVO.setUnitName(customerInfo.getName());
                }
                List<FileView> cntrPics = StringUtils.getFileViews(inputOrderTransportVO.getCntrPic(), inputOrderTransportVO.getCntrPicName(), prePath);
                inputOrderTransportVO.setCntrPics(cntrPics);

                //附件信息
                List<FileView> allPics = new ArrayList<>(cntrPics);
                //获取反馈操作人时上传的附件
                List<FileView> attachments = this.logisticsTrackService.getAttachments(inputOrderTransportVO.getId()
                        , BusinessTypeEnum.ZGYS.getCode(), prePath);
                allPics.addAll(attachments);
                inputOrderTransportVO.setAllPics(allPics);
                //设置提货信息的客户
                List<InputOrderTakeAdrVO> orderTakeAdrForms1 = inputOrderTransportVO.getOrderTakeAdrForms1();
                for (InputOrderTakeAdrVO inputOrderTakeAdr1 : orderTakeAdrForms1) {
                    inputOrderTakeAdr1.setCustomerName(inputMainOrderVO.getCustomerName());
                }
                List<InputOrderTakeAdrVO> orderTakeAdrForms2 = inputOrderTransportVO.getOrderTakeAdrForms2();
                for (InputOrderTakeAdrVO inputOrderTakeAdr2 : orderTakeAdrForms2) {
                    inputOrderTakeAdr2.setCustomerName(inputMainOrderVO.getCustomerName());
                }
                inputOrderTransportVO.setDepartment(departmentMap.get(inputOrderTransportVO.getDepartmentId()));
                inputOrderVO.setOrderTransportForm(inputOrderTransportVO);
            }
        }
        //获取内陆运输
        if (OrderStatusEnum.NLYS.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.NLDD.getCode())) {
            InputOrderInlandTPVO inlandTPVO = this.inlandTpClient.getOrderDetails(inputMainOrderVO.getOrderNo()).getData();
            if (inlandTPVO != null) {
                //添加附件
                List<FileView> attachments = this.logisticsTrackService.getAttachments(inlandTPVO.getId()
                        , BusinessTypeEnum.NL.getCode(), prePath);
                inlandTPVO.setAllPics(attachments);
                inlandTPVO.setDepartment(departmentMap.get(inlandTPVO.getDepartmentId()));
                inputOrderVO.setOrderInlandTransportForm(inlandTPVO);
            }
        }

        if (OrderStatusEnum.KY.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.KYDD.getCode())) {
            InputAirOrderVO airOrderVO = this.freightAirClient.getAirOrderDetails(inputMainOrderVO.getOrderNo()).getData();
            if (airOrderVO != null) {
                //查询供应商
                InputAirBookingVO airBookingVO = airOrderVO.getAirBookingVO();
                if (airBookingVO != null && airBookingVO.getAgentSupplierId() != null) {
                    SupplierInfo supplierInfo = this.supplierInfoService.getById(airBookingVO.getAgentSupplierId());
                    airBookingVO.setAgentSupplier(supplierInfo.getSupplierChName());
                }
                //添加附件
                List<FileView> attachments = this.logisticsTrackService.getAttachments(airOrderVO.getId()
                        , BusinessTypeEnum.KY.getCode(), prePath);
                airOrderVO.setAllPics(attachments);
                //结算单位名称
                CustomerInfo customerInfo = customerInfoService.getByCode(airOrderVO.getSettlementUnitCode());
                if (customerInfo != null) {
                    airOrderVO.setUnitName(customerInfo.getName());
                }
                airOrderVO.setDepartment(departmentMap.get(airOrderVO.getDepartmentId()));
                inputOrderVO.setAirOrderForm(airOrderVO);
            }
        }
        //服务单信息
        if (OrderStatusEnum.FWD.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.FWDDD.getCode())) {
            InputOrderServiceVO orderServiceVO = serviceOrderService.getSerOrderDetails(inputMainOrderVO.getOrderNo());
            if (orderServiceVO != null) {
                inputOrderVO.setOrderServiceForm(orderServiceVO);
            }
        }
        //获取海运信息
        if (OrderStatusEnum.HY.getCode().equals(form.getClassCode()) || inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.HYDD.getCode())) {
            InputSeaOrderVO seaOrderVO = this.oceanShipClient.getSeaOrderDetails(inputMainOrderVO.getOrderNo()).getData();
            if (seaOrderVO != null) {
                //查询供应商
                InputSeaBookshipVO seaBookshipVO = seaOrderVO.getSeaBookshipVO();
                if (seaBookshipVO != null && seaBookshipVO.getAgentSupplierId() != null) {
                    SupplierInfo supplierInfo = this.supplierInfoService.getById(seaBookshipVO.getAgentSupplierId());
                    seaBookshipVO.setAgentSupplier(supplierInfo.getSupplierChName());
                }
                //添加附件
                List<FileView> attachments = this.logisticsTrackService.getAttachments(seaOrderVO.getOrderId()
                        , BusinessTypeEnum.HY.getCode(), prePath);
                seaOrderVO.setAllPics(attachments);
                //结算单位名称
                CustomerInfo customerInfo = customerInfoService.getByCode(seaOrderVO.getUnitCode());
                if (customerInfo != null) {
                    seaOrderVO.setUnitName(customerInfo.getName());
                }
                seaOrderVO.setDepartment(departmentMap.get(seaOrderVO.getDepartmentId()));
                inputOrderVO.setSeaOrderForm(seaOrderVO);
            }
        }

        //获取拖车信息
        if (OrderStatusEnum.TC.getCode().equals(form.getClassCode()) || inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.TCEDD.getCode()) || inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.TCIDD.getCode())) {
            List<InputTrailerOrderVO> trailerOrderVOs = this.trailerClient.getTrailerOrderDetails(inputMainOrderVO.getOrderNo()).getData();
            for (InputTrailerOrderVO trailerOrderVO : trailerOrderVOs) {
                if (trailerOrderVO != null) {
                    //查询供应商
                    TrailerDispatchVO trailerDispatchVO = trailerOrderVO.getTrailerDispatchVO();
                    if (trailerDispatchVO != null && trailerDispatchVO.getSupplierId() != null) {
                        SupplierInfo supplierInfo = this.supplierInfoService.getById(trailerDispatchVO.getSupplierId());
                        trailerDispatchVO.setSupplierName(supplierInfo.getSupplierChName());
                    }
                    //添加附件
                    List<FileView> attachments = this.logisticsTrackService.getAttachments(trailerOrderVO.getId()
                            , BusinessTypeEnum.TC.getCode(), prePath);
                    trailerOrderVO.setAllPics(attachments);
                    //结算单位名称
                    CustomerInfo customerInfo = customerInfoService.getByCode(trailerOrderVO.getUnitCode());
                    if (customerInfo != null) {
                        trailerOrderVO.setUnitName(customerInfo.getName());
                    }
                }
                trailerOrderVO.setDepartment(departmentMap.get(trailerOrderVO.getDepartmentId()));
            }
            inputOrderVO.setTrailerOrderForm(trailerOrderVOs);

        }
        //获取仓储订单信息
        if (OrderStatusEnum.CC.getCode().equals(form.getClassCode()) || inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCEDD.getCode())
                || inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCIDD.getCode()) || inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCFDD.getCode())) {
            if (inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCEDD.getCode())) {
                InputStorageOutOrderVO storageOutOrderVO = this.storageClient.getStorageOutOrderDetails(inputMainOrderVO.getOrderNo()).getData();
                if (storageOutOrderVO != null) {
                    //添加附件
                    List<FileView> attachments = this.logisticsTrackService.getAttachments(storageOutOrderVO.getId()
                            , BusinessTypeEnum.CK.getCode(), prePath);
                    storageOutOrderVO.setAllPics(attachments);
                    //结算单位名称
                    CustomerInfo customerInfo = customerInfoService.getByCode(storageOutOrderVO.getUnitCode());
                    if (customerInfo != null) {
                        storageOutOrderVO.setUnitCodeName(customerInfo.getName());
                    }
                    storageOutOrderVO.setDepartment(departmentMap.get(storageOutOrderVO.getDepartmentId()));
                }
                inputOrderVO.setStorageOutOrderForm(storageOutOrderVO);
            }
            if (inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCIDD.getCode())) {
                InputStorageInputOrderVO storageInputOrderVO = this.storageClient.getStorageInOrderDetails(inputMainOrderVO.getOrderNo()).getData();
                if (storageInputOrderVO != null) {
                    //添加附件
                    List<FileView> attachments = this.logisticsTrackService.getAttachments(storageInputOrderVO.getId()
                            , BusinessTypeEnum.RK.getCode(), prePath);
                    storageInputOrderVO.setAllPics(attachments);
                    //结算单位名称
                    CustomerInfo customerInfo = customerInfoService.getByCode(storageInputOrderVO.getUnitCode());
                    if (customerInfo != null) {
                        storageInputOrderVO.setUnitCodeName(customerInfo.getName());
                    }
                    storageInputOrderVO.setDepartment(departmentMap.get(storageInputOrderVO.getDepartmentId()));
                }
                inputOrderVO.setStorageInputOrderForm(storageInputOrderVO);
            }
            if (inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCFDD.getCode())) {
                InputStorageFastOrderVO inputStorageFastOrderVO = this.storageClient.getStorageFastOrderDetails(inputMainOrderVO.getOrderNo()).getData();
                if (inputStorageFastOrderVO != null) {
                    //添加附件
                    List<FileView> attachments = this.logisticsTrackService.getAttachments(inputStorageFastOrderVO.getId()
                            , BusinessTypeEnum.IO.getCode(), prePath);
                    inputStorageFastOrderVO.setAllPics(attachments);
                    //结算单位名称
                    CustomerInfo customerInfo = customerInfoService.getByCode(inputStorageFastOrderVO.getUnitCode());
                    if (customerInfo != null) {
                        inputStorageFastOrderVO.setUnitCodeName(customerInfo.getName());
                    }
                    inputStorageFastOrderVO.setDepartment(departmentMap.get(inputStorageFastOrderVO.getDepartmentId()));
                }
                inputOrderVO.setStorageFastOrderForm(inputStorageFastOrderVO);
            }
        }

        return inputOrderVO;
    }

    //需要修改 的内陆订单部分
    @Override
    @GlobalTransactional
    public boolean createOrder(InputOrderForm form) {
        //保存主订单
        InputMainOrderForm inputMainOrderForm = form.getOrderForm();

        String oldMainOrderNo = inputMainOrderForm.getOrderNo() != null ? inputMainOrderForm.getOrderNo() : null;

        inputMainOrderForm.setCmd(form.getCmd());
        //特殊处理
        this.specialTreatment(form, inputMainOrderForm);

        String mainOrderNo = oprMainOrder(inputMainOrderForm, form.getLoginUserName());

        if (StringUtil.isNullOrEmpty(mainOrderNo)) {
            return false;
        }
        String classCode = inputMainOrderForm.getClassCode();//订单类型
        String selectedServer = inputMainOrderForm.getSelectedServer();//所选服务
        //提交所有子订单号
        Map<String, Object> submitOrderMap = new HashMap<>();

        //中港运输并且并且订单状态为驳回或为空或为待接单
        if (OrderStatusEnum.ZGYS.getCode().equals(classCode) ||
                selectedServer.contains(OrderStatusEnum.ZGYSDD.getCode())) {
            //创建中港订单信息
            InputOrderTransportForm orderTransportForm = form.getOrderTransportForm();

            //生成中港订单号
            if (form.getCmd().equals("submit")) {
                if (orderTransportForm.getId() == null) {
                    String orderNo = generationOrderNo(orderTransportForm.getLegalEntityId(), orderTransportForm.getGoodsType(), OrderStatusEnum.ZGYS.getCode());
                    orderTransportForm.setOrderNo(orderNo);
                }
                if (orderTransportForm.getOrderNo() != null && !orderTransportForm.getOrderNo().substring(0, 2).equals("TI") && !orderTransportForm.getOrderNo().substring(0, 2).equals("TE")) {
                    if (orderTransportForm.getSubTmsStatus() != null && orderTransportForm.getSubTmsStatus().equals("T_0")) {
                        String orderNo = generationOrderNo(orderTransportForm.getLegalEntityId(), orderTransportForm.getGoodsType(), OrderStatusEnum.ZGYS.getCode());
                        orderTransportForm.setOrderNo(orderNo);
                    }
                }
            }
            if (form.getCmd().equals("preSubmit") && orderTransportForm.getId() == null) {
                //生成中港订单号
                String orderNo = StringUtils.loadNum(CommonConstant.T, 12);
                while (true) {
                    if (!isExistOrder(orderNo)) {//重复
                        orderNo = StringUtils.loadNum(CommonConstant.T, 12);
                    } else {
                        break;
                    }
                }
                orderTransportForm.setOrderNo(orderNo);
            }

            if (this.queryEditOrderCondition(orderTransportForm.getSubTmsStatus(),
                    inputMainOrderForm.getStatus(), SubOrderSignEnum.ZGYS.getSignOne(), form)) {
                if (!selectedServer.contains(OrderStatusEnum.XGQG.getCode())) {
                    //若没有选择香港清关,则情况香港清关信息，避免信息有误
                    orderTransportForm.setHkLegalName(null);
                    orderTransportForm.setHkUnitCode(null);
                    orderTransportForm.setIsHkClear(null);
                }
                Map<String, Object> subOrderMap = new HashMap<>();
                submitOrderMap.put(orderTransportForm.getOrderNo(),
                        subOrderMap.put("departmentId", orderTransportForm.getDepartmentId()));

                orderTransportForm.setMainOrderNo(mainOrderNo);
                orderTransportForm.setLoginUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());

                //根据主订单获取提货地址送货地址得客户ID
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("id_code", inputMainOrderForm.getCustomerCode());
                CustomerInfo customerInfo = customerInfoService.getOne(queryWrapper);
                List<InputOrderTakeAdrForm> takeAdrForms1 = orderTransportForm.getTakeAdrForms1();
                List<InputOrderTakeAdrForm> takeAdrForms2 = orderTransportForm.getTakeAdrForms2();
                for (InputOrderTakeAdrForm takeAdrForm1 : takeAdrForms1) {
                    takeAdrForm1.setCustomerId(customerInfo.getId());
                }
                for (InputOrderTakeAdrForm takeAdrForm2 : takeAdrForms2) {
                    takeAdrForm2.setCustomerId(customerInfo.getId());
                }
                orderTransportForm.setCmd(form.getCmd());
                Boolean result = tmsClient.createOrderTransport(orderTransportForm).getData();
                if (!result) {//调用失败
                    return false;
                }
            }
        }
        //内陆运输
        if (OrderStatusEnum.NLYS.getCode().equals(classCode)
                || selectedServer.contains(OrderStatusEnum.NLDD.getCode())) {

            InputOrderInlandTransportForm orderInlandTransportForm = form.getOrderInlandTransportForm();

            //生成内陆订单号
            if (form.getCmd().equals("submit")) {
                if (orderInlandTransportForm.getId() == null) {
                    String orderNo = generationOrderNo(orderInlandTransportForm.getLegalEntityId(), null, OrderStatusEnum.NLYS.getCode());
                    orderInlandTransportForm.setOrderNo(orderNo);
                }
                if (orderInlandTransportForm.getStatus() != null && orderInlandTransportForm.getStatus().equals("NL_0")) {
                    String orderNo = generationOrderNo(orderInlandTransportForm.getLegalEntityId(), null, OrderStatusEnum.NLYS.getCode());
                    orderInlandTransportForm.setOrderNo(orderNo);
                }
            }
            if (form.getCmd().equals("preSubmit") && orderInlandTransportForm.getId() == null) {
                //生成订单号
                String orderNo = StringUtils.loadNum(CommonConstant.NL, 12);
                while (true) {
                    if (!isExistOrder(orderNo)) {//重复
                        orderNo = StringUtils.loadNum(CommonConstant.NL, 12);
                    } else {
                        break;
                    }
                }
                orderInlandTransportForm.setOrderNo(orderNo);
            }


            if (this.queryEditOrderCondition(orderInlandTransportForm.getStatus(),
                    inputMainOrderForm.getStatus(), SubOrderSignEnum.NL.getSignOne(), form)) {
                Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                        : ProcessStatusEnum.DRAFT.getCode();

                Map<String, Object> subOrderMap = new HashMap<>();
                submitOrderMap.put(orderInlandTransportForm.getOrderNo(),
                        subOrderMap.put("departmentId", orderInlandTransportForm.getDepartmentId()));

                //查询结算单位
                String orderNo = orderInlandTransportForm.getOrderNo();
                CustomerInfo unitName = this.customerInfoService.getByCode(orderInlandTransportForm.getUnitCode());
                orderInlandTransportForm.setMainOrderNo(mainOrderNo);
                orderInlandTransportForm.setOrderNo(orderNo);
                orderInlandTransportForm.setCreateUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());
                orderInlandTransportForm.setProcessStatus(processStatus);
                orderInlandTransportForm.setUnitName(unitName.getName());
                ApiResult<String> result = this.inlandTpClient.createOrder(orderInlandTransportForm);
                if (!result.isOk()) {//调用失败
                    throw new JayudBizException("创建内陆订单失败");
                }
                String subOrderNo = result.getData();
                orderInlandTransportForm.setOrderNo(subOrderNo);

                this.initProcessNode(mainOrderNo, orderNo, OrderStatusEnum.NLYS,
                        form, orderInlandTransportForm.getId(), OrderStatusEnum.getInlandTPProcess());


            }
        }

        //空运
        if (OrderStatusEnum.KY.getCode().equals(classCode)) {
            InputAirOrderForm airOrderForm = form.getAirOrderForm();

            //生成空运订单号
            if (form.getCmd().equals("submit")) {
                if (airOrderForm.getId() == null) {
                    String orderNo = generationOrderNo(airOrderForm.getLegalEntityId(), airOrderForm.getImpAndExpType(), OrderStatusEnum.KY.getCode());
                    airOrderForm.setOrderNo(orderNo);
                }
                if (airOrderForm.getStatus() != null && airOrderForm.getStatus().equals("A_0")) {
                    String orderNo = generationOrderNo(airOrderForm.getLegalEntityId(), airOrderForm.getImpAndExpType(), OrderStatusEnum.KY.getCode());
                    airOrderForm.setOrderNo(orderNo);
                }

            }
            if (form.getCmd().equals("preSubmit") && airOrderForm.getId() == null) {
                //生成空运订单号
                String orderNo = StringUtils.loadNum(CommonConstant.A, 12);
                while (true) {
                    if (!isExistOrder(orderNo)) {//重复
                        orderNo = StringUtils.loadNum(CommonConstant.A, 12);
                    } else {
                        break;
                    }
                }
                airOrderForm.setOrderNo(orderNo);
            }

            if (this.queryEditOrderCondition(airOrderForm.getStatus(),
                    inputMainOrderForm.getStatus(), SubOrderSignEnum.KY.getSignOne(), form)) {

                Map<String, Object> subOrderMap = new HashMap<>();
                submitOrderMap.put(airOrderForm.getOrderNo(),
                        subOrderMap.put("departmentId", airOrderForm.getDepartmentId()));

                //拼装地址信息
                airOrderForm.assemblyAddress();
                airOrderForm.setMainOrderNo(mainOrderNo);
                airOrderForm.setCreateUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());
                Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                        : ProcessStatusEnum.DRAFT.getCode();
                airOrderForm.setProcessStatus(processStatus);
                this.freightAirClient.createOrder(airOrderForm);
            }
        }
        //服务单
        if (OrderStatusEnum.FWD.getCode().equals(classCode)) {
            //创建服务单订单信息
            InputOrderServiceForm orderServiceForm = form.getOrderServiceForm();

            //生成服务订单号
//            String orderNo = generationOrderNo(orderServiceForm.getLegalEntityId(),seaOrderForm.getImpAndExpType(),OrderStatusEnum.HY.getCode());
//            seaOrderForm.setOrderNo(orderNo);
            orderServiceForm.setMainOrderNo(mainOrderNo);
            orderServiceForm.setLoginUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());
            boolean result = serviceOrderService.createOrder(orderServiceForm);
            if (!result) {
                return false;
            }
        }
        //海运
        //System.out.println(OrderStatusEnum.HY.getCode().equals(classCode));
        if (OrderStatusEnum.HY.getCode().equals(classCode)) {
            InputSeaOrderForm seaOrderForm = form.getSeaOrderForm();

            //生成海运订单号
            if (form.getCmd().equals("submit")) {
                if (seaOrderForm.getOrderId() == null) {
                    String orderNo = generationOrderNo(seaOrderForm.getLegalEntityId(), seaOrderForm.getImpAndExpType(), OrderStatusEnum.HY.getCode());
                    seaOrderForm.setOrderNo(orderNo);
                }
                if (seaOrderForm.getStatus() != null && seaOrderForm.getStatus().equals("S_0")) {
                    String orderNo = generationOrderNo(seaOrderForm.getLegalEntityId(), seaOrderForm.getImpAndExpType(), OrderStatusEnum.HY.getCode());
                    seaOrderForm.setOrderNo(orderNo);
                }

            }
            if (form.getCmd().equals("preSubmit") && seaOrderForm.getOrderId() == null) {
                //生成海运订单号
                String orderNo = StringUtils.loadNum(CommonConstant.S, 12);
                while (true) {
                    if (!isExistOrder(orderNo)) {//重复
                        orderNo = StringUtils.loadNum(CommonConstant.S, 12);
                    } else {
                        break;
                    }
                }
                seaOrderForm.setOrderNo(orderNo);
            }

            if (this.queryEditOrderCondition(seaOrderForm.getStatus(),
                    inputMainOrderForm.getStatus(), SubOrderSignEnum.HY.getSignOne(), form)) {

                Map<String, Object> subOrderMap = new HashMap<>();
                submitOrderMap.put(seaOrderForm.getOrderNo(),
                        subOrderMap.put("departmentId", seaOrderForm.getDepartmentId()));

                //拼装地址信息
                seaOrderForm.assemblyAddress();
                seaOrderForm.assemblyUP();
                seaOrderForm.setFileName(StringUtils.getFileNameStr(seaOrderForm.getFileViews()));
                seaOrderForm.setFilePath(StringUtils.getFileStr(seaOrderForm.getFileViews()));
                seaOrderForm.setMainOrderNo(mainOrderNo);
                seaOrderForm.setCreateUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());
                Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                        : ProcessStatusEnum.DRAFT.getCode();
                seaOrderForm.setProcessStatus(processStatus);
                String subOrderNo = this.oceanShipClient.createOrder(seaOrderForm).getData();
                seaOrderForm.setOrderNo(subOrderNo);
                this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.HY,
                        form, seaOrderForm.getOrderId(), OrderStatusEnum.getSeaOrderProcess());
            }
        }
        //拖车
        if (OrderStatusEnum.TC.getCode().equals(classCode) || selectedServer.contains(OrderStatusEnum.TCEDD.getCode()) || selectedServer.contains(OrderStatusEnum.TCIDD.getCode())) {
            List<InputTrailerOrderFrom> trailerOrderFroms = form.getTrailerOrderFrom();

            List<String> strings = new ArrayList<>();
            strings.add("TT_1_1");
            strings.add("TT_2_1");
            strings.add("TT_3_1");
            strings.add("TT_4_1");
            boolean flag = false;
            for (String string : strings) {
                for (InputTrailerOrderFrom trailerOrderFrom : trailerOrderFroms) {
                    if (trailerOrderFrom.getStatus() != null && string.equals(trailerOrderFrom.getStatus())) {
                        flag = true;
                    }
                }
            }
            if (flag) {
                for (InputTrailerOrderFrom trailerOrderFrom : trailerOrderFroms) {

                    if (this.queryEditOrderCondition(trailerOrderFrom.getStatus(),
                            inputMainOrderForm.getStatus(), SubOrderSignEnum.TC.getSignOne(), form)) {
                        Map<String, Object> subOrderMap = new HashMap<>();
                        submitOrderMap.put(trailerOrderFrom.getOrderNo(),
                                subOrderMap.put("departmentId", trailerOrderFrom.getDepartmentId()));

//                        trailerOrderFrom.setLegalName(oauthClient.getLegalNameByLegalId(trailerOrderFrom.getLegalEntityId()).getData());

                        trailerOrderFrom.setMainOrderNo(mainOrderNo);
                        trailerOrderFrom.setCreateUser(UserOperator.getToken());
                        Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                                : ProcessStatusEnum.DRAFT.getCode();
                        trailerOrderFrom.setProcessStatus(processStatus);
                        String subOrderNo = this.trailerClient.createOrder(trailerOrderFrom).getData();
                        trailerOrderFrom.setOrderNo(subOrderNo);

                        this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.TC,
                                form, trailerOrderFrom.getId(), OrderStatusEnum.getTrailerOrderProcess());
                    }
                }
            } else {
                if (this.queryEditOrderCondition(trailerOrderFroms.get(0).getStatus(),
                        inputMainOrderForm.getStatus(), SubOrderSignEnum.TC.getSignOne(), form)) {

                    if (trailerOrderFroms.get(0).getMainOrderNo() != null) {
                        List<String> data = trailerClient.getOrderNosByMainOrderNo(trailerOrderFroms.get(0).getMainOrderNo()).getData();
                        if (data != null && data.size() > 0) {
                            goodsService.deleteGoodsByBusOrders(data, BusinessTypeEnum.TC.getCode());
                            orderAddressService.deleteOrderAddressByBusOrders(data, BusinessTypeEnum.TC.getCode());
                            log.warn("删除商品和地址信息成功");
                        }
                    }

                    if (form.getCmd().equals("preSubmit") && trailerOrderFroms.get(0).getMainOrderNo() != null) {
                        ApiResult result = trailerClient.deleteOrderByMainOrderNo(trailerOrderFroms.get(0).getMainOrderNo());
                        if (result.getCode() != HttpStatus.SC_OK) {
                            log.warn("删除订单信息失败");
                            throw new JayudBizException("删除订单信息失败");
                        }
                    }

                    if (form.getCmd().equals("submit") && trailerOrderFroms.get(0).getStatus() != null && trailerOrderFroms.get(0).getStatus().equals("TT_0")) {
                        ApiResult result = trailerClient.deleteOrderByMainOrderNo(trailerOrderFroms.get(0).getMainOrderNo());
                        if (result.getCode() != HttpStatus.SC_OK) {
                            log.warn("删除订单信息失败");
                            throw new JayudBizException("删除订单信息失败");
                        }
                    }

                    for (InputTrailerOrderFrom trailerOrderFrom : trailerOrderFroms) {

                        //生成拖车订单号
                        if (form.getCmd().equals("submit")) {//提交
                            if (trailerOrderFrom.getId() == null && trailerOrderFrom.getStatus() == null) {
                                String orderNo = generationOrderNo(trailerOrderFrom.getLegalEntityId(), trailerOrderFrom.getImpAndExpType(), OrderStatusEnum.TC.getCode());
                                trailerOrderFrom.setOrderNo(orderNo);
                            }
                            //草稿编辑提交
                            if (trailerOrderFrom.getStatus() != null && trailerOrderFrom.getStatus().equals("TT_0")) {
                                String orderNo = generationOrderNo(trailerOrderFrom.getLegalEntityId(), trailerOrderFrom.getImpAndExpType(), OrderStatusEnum.TC.getCode());
                                trailerOrderFrom.setOrderNo(orderNo);
                            }

                        }
                        //暂存，随机生成订单号
                        if (form.getCmd().equals("preSubmit")) {

                            if (trailerOrderFrom.getId() == null) {
                                //生成拖车订单号
                                String orderNo = StringUtils.loadNum(CommonConstant.TC, 12);
                                while (true) {
                                    if (!isExistOrder(orderNo)) {//重复
                                        orderNo = StringUtils.loadNum(CommonConstant.TC, 12);
                                    } else {
                                        break;
                                    }
                                }
                                trailerOrderFrom.setOrderNo(orderNo);
                            }

                        }

                        if (this.queryEditOrderCondition(trailerOrderFrom.getStatus(),
                                inputMainOrderForm.getStatus(), SubOrderSignEnum.TC.getSignOne(), form)) {

                            Map<String, Object> subOrderMap = new HashMap<>();
                            submitOrderMap.put(trailerOrderFrom.getOrderNo(),
                                    subOrderMap.put("departmentId", trailerOrderFrom.getDepartmentId()));

//                            LegalEntityVO legalEntityVO = (LegalEntityVO)oauthClient.getLegalEntityByLegalId(trailerOrderFrom.getLegalEntityId()).getData();
                            trailerOrderFrom.setLegalName(oauthClient.getLegalNameByLegalId(trailerOrderFrom.getLegalEntityId()).getData());

                            trailerOrderFrom.setMainOrderNo(mainOrderNo);
                            trailerOrderFrom.setCreateUser(UserOperator.getToken());
                            Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                                    : ProcessStatusEnum.DRAFT.getCode();
                            trailerOrderFrom.setProcessStatus(processStatus);
                            ApiResult<String> result = this.trailerClient.createOrder(trailerOrderFrom);
                            if (result.getCode() != HttpStatus.SC_OK) {
                                throw new JayudBizException("创建拖车订单失败");
                            }

                            String subOrderNo = result.getData();

                            trailerOrderFrom.setOrderNo(subOrderNo);

                            this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.TC,
                                    form, trailerOrderFrom.getId(), OrderStatusEnum.getTrailerOrderProcess());


                        }
                    }
                }


            }


        }

        //仓储
        if (OrderStatusEnum.CC.getCode().equals(classCode) || selectedServer.contains(OrderStatusEnum.CCIDD.getCode()) || selectedServer.contains(OrderStatusEnum.CCEDD.getCode()) || selectedServer.contains(OrderStatusEnum.CCFDD.getCode())) {
            if (selectedServer.contains(OrderStatusEnum.CCIDD.getCode())) {
                InputStorageInputOrderForm storageInputOrderForm = form.getStorageInputOrderForm();
                if (form.getCmd().equals("submit")) {//提交
                    if (storageInputOrderForm.getId() == null) {
                        String orderNo = generationOrderNo(storageInputOrderForm.getLegalEntityId(), 1, OrderStatusEnum.CC.getCode());
                        storageInputOrderForm.setOrderNo(orderNo);
                    }
                    //草稿编辑提交
                    if (storageInputOrderForm.getStatus() != null && storageInputOrderForm.getStatus().equals("CCI_0")) {
                        String orderNo = generationOrderNo(storageInputOrderForm.getLegalEntityId(), 1, OrderStatusEnum.CC.getCode());
                        storageInputOrderForm.setOrderNo(orderNo);
                    }
                }
                //暂存，随机生成订单号
                if (form.getCmd().equals("preSubmit") && storageInputOrderForm.getId() == null) {
                    //生成仓储订单号
                    String orderNo = StringUtils.loadNum(CommonConstant.CC, 12);
                    while (true) {
                        if (!isExistOrder(orderNo)) {//重复
                            orderNo = StringUtils.loadNum(CommonConstant.CC, 12);
                        } else {
                            break;
                        }
                    }
                    storageInputOrderForm.setOrderNo(orderNo);
                }

                if (this.queryEditOrderCondition(storageInputOrderForm.getStatus(),
                        inputMainOrderForm.getStatus(), SubOrderSignEnum.CCI.getSignOne(), form)) {

                    Map<String, Object> subOrderMap = new HashMap<>();
                    submitOrderMap.put(storageInputOrderForm.getOrderNo(),
                            subOrderMap.put("departmentId", storageInputOrderForm.getDepartmentId()));

                    storageInputOrderForm.setMainOrderNo(mainOrderNo);
                    for (AddWarehouseGoodsForm addWarehouseGoodsForm : storageInputOrderForm.getGoodsFormList()) {
                        addWarehouseGoodsForm.setFileName(StringUtils.getFileNameStr(addWarehouseGoodsForm.getTakeFiles()));
                        addWarehouseGoodsForm.setFilePath(StringUtils.getFileStr(addWarehouseGoodsForm.getTakeFiles()));
                    }
                    Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                            : ProcessStatusEnum.DRAFT.getCode();
                    storageInputOrderForm.setProcessStatus(processStatus);
                    ApiResult<String> result = this.storageClient.createInOrder(storageInputOrderForm);
                    if (result.getCode() != HttpStatus.SC_OK) {
                        throw new JayudBizException("创建仓储订单失败");
                    }
                    String subOrderNo = result.getData();
                    storageInputOrderForm.setOrderNo(subOrderNo);

                    this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.CC,
                            form, storageInputOrderForm.getId(), OrderStatusEnum.getInStorageOrderProcess());
                }

            }
            if (selectedServer.contains(OrderStatusEnum.CCEDD.getCode())) {
                InputStorageOutOrderForm storageOutOrderForm = form.getStorageOutOrderForm();
                if (form.getCmd().equals("submit")) {//提交
                    if (storageOutOrderForm.getId() == null) {
                        String orderNo = generationOrderNo(storageOutOrderForm.getLegalEntityId(), 2, OrderStatusEnum.CC.getCode());
                        storageOutOrderForm.setOrderNo(orderNo);
                    }
                    //草稿编辑提交
                    if (storageOutOrderForm.getStatus() != null && storageOutOrderForm.getStatus().equals("CCE_0")) {
                        String orderNo = generationOrderNo(storageOutOrderForm.getLegalEntityId(), 2, OrderStatusEnum.CC.getCode());
                        storageOutOrderForm.setOrderNo(orderNo);
                    }

                }
                //暂存，随机生成订单号
                if (form.getCmd().equals("preSubmit") && storageOutOrderForm.getId() == null) {
                    //生成仓储订单号
                    String orderNo = StringUtils.loadNum(CommonConstant.CC, 12);
                    while (true) {
                        if (!isExistOrder(orderNo)) {//重复
                            orderNo = StringUtils.loadNum(CommonConstant.CC, 12);
                        } else {
                            break;
                        }
                    }
                    storageOutOrderForm.setOrderNo(orderNo);
                }

                if (this.queryEditOrderCondition(storageOutOrderForm.getStatus(),
                        inputMainOrderForm.getStatus(), SubOrderSignEnum.CCE.getSignOne(), form)) {

                    Map<String, Object> subOrderMap = new HashMap<>();
                    submitOrderMap.put(storageOutOrderForm.getOrderNo(),
                            subOrderMap.put("departmentId", storageOutOrderForm.getDepartmentId()));

                    storageOutOrderForm.setMainOrderNo(mainOrderNo);
                    storageOutOrderForm.setCmd(form.getCmd());
                    for (AddWarehouseGoodsForm addWarehouseGoodsForm : storageOutOrderForm.getGoodsFormList()) {
                        addWarehouseGoodsForm.setFileName(StringUtils.getFileNameStr(addWarehouseGoodsForm.getTakeFiles()));
                        addWarehouseGoodsForm.setFilePath(StringUtils.getFileStr(addWarehouseGoodsForm.getTakeFiles()));
                    }
                    Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                            : ProcessStatusEnum.DRAFT.getCode();
                    storageOutOrderForm.setProcessStatus(processStatus);
                    String subOrderNo = this.storageClient.createOutOrder(storageOutOrderForm).getData();
                    storageOutOrderForm.setOrderNo(subOrderNo);

                    this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.CC,
                            form, storageOutOrderForm.getId(), OrderStatusEnum.getOutStorageOrderProcess());
                }
            }
            if (selectedServer.contains(OrderStatusEnum.CCFDD.getCode())) {
                InputStorageFastOrderForm storageFastOrderForm = form.getStorageFastOrderForm();
                if (form.getCmd().equals("submit")) {//提交
                    if (storageFastOrderForm.getId() == null) {
                        String orderNo = generationOrderNo(storageFastOrderForm.getLegalEntityId(), 3, OrderStatusEnum.CC.getCode());
                        storageFastOrderForm.setOrderNo(orderNo);
                    }
                    //草稿编辑提交
                    if (storageFastOrderForm.getStatus() != null && storageFastOrderForm.getStatus().equals("CCF_0")) {
                        String orderNo = generationOrderNo(storageFastOrderForm.getLegalEntityId(), 3, OrderStatusEnum.CC.getCode());
                        storageFastOrderForm.setOrderNo(orderNo);
                    }

                }
                //暂存，随机生成订单号
                if (form.getCmd().equals("preSubmit") && storageFastOrderForm.getId() == null) {
                    //生成仓储订单号
                    String orderNo = StringUtils.loadNum(CommonConstant.CC, 12);
                    while (true) {
                        if (!isExistOrder(orderNo)) {//重复
                            orderNo = StringUtils.loadNum(CommonConstant.CC, 12);
                        } else {
                            break;
                        }
                    }
                    storageFastOrderForm.setOrderNo(orderNo);
                }

                if (this.queryEditOrderCondition(storageFastOrderForm.getStatus(),
                        inputMainOrderForm.getStatus(), SubOrderSignEnum.CCF.getSignOne(), form)) {

                    Map<String, Object> subOrderMap = new HashMap<>();
                    submitOrderMap.put(storageFastOrderForm.getOrderNo(),
                            subOrderMap.put("departmentId", storageFastOrderForm.getDepartmentId()));

                    storageFastOrderForm.setMainOrderNo(mainOrderNo);
                    if (CollectionUtils.isNotEmpty(storageFastOrderForm.getInGoodsFormList())) {
                        for (AddWarehouseGoodsForm addWarehouseGoodsForm : storageFastOrderForm.getInGoodsFormList()) {
                            addWarehouseGoodsForm.setFileName(StringUtils.getFileNameStr(addWarehouseGoodsForm.getTakeFiles()));
                            addWarehouseGoodsForm.setFilePath(StringUtils.getFileStr(addWarehouseGoodsForm.getTakeFiles()));
                        }
                    }
                    if (CollectionUtils.isNotEmpty(storageFastOrderForm.getOutGoodsFormList())) {
                        for (AddWarehouseGoodsForm addWarehouseGoodsForm : storageFastOrderForm.getOutGoodsFormList()) {
                            addWarehouseGoodsForm.setFileName(StringUtils.getFileNameStr(addWarehouseGoodsForm.getTakeFiles()));
                            addWarehouseGoodsForm.setFilePath(StringUtils.getFileStr(addWarehouseGoodsForm.getTakeFiles()));
                        }
                    }
                    if (CollectionUtils.isNotEmpty(storageFastOrderForm.getFastGoodsFormList())) {
                        for (AddWarehouseGoodsForm addWarehouseGoodsForm : storageFastOrderForm.getFastGoodsFormList()) {
                            addWarehouseGoodsForm.setFileName(StringUtils.getFileNameStr(addWarehouseGoodsForm.getTakeFiles()));
                            addWarehouseGoodsForm.setFilePath(StringUtils.getFileStr(addWarehouseGoodsForm.getTakeFiles()));
                        }
                    }

                    Integer processStatus = CommonConstant.SUBMIT.equals(form.getCmd()) ? ProcessStatusEnum.PROCESSING.getCode()
                            : ProcessStatusEnum.DRAFT.getCode();
                    storageFastOrderForm.setProcessStatus(processStatus);
                    ApiResult<String> result = this.storageClient.createFastOrder(storageFastOrderForm);
                    if (!result.isOk()) {
                        throw new JayudBizException("快进快出创建失败");
                    }
                    String subOrderNo = result.getData();
                    storageFastOrderForm.setOrderNo(subOrderNo);

                    if (storageFastOrderForm.getIsWarehouse().equals(1)) {
                        this.initProcessNode(mainOrderNo, subOrderNo, OrderStatusEnum.CC,
                                form, storageFastOrderForm.getId(), OrderStatusEnum.getFastStorageOrderProcess());
                    }

                }
            }
        }

        //纯报关和出口报关并且订单状态为驳回(C_1_1)或为空或为暂存待补全的待接单
        if (OrderStatusEnum.CBG.getCode().equals(classCode) ||
                selectedServer.contains(OrderStatusEnum.CKBG.getCode())) {
            InputOrderCustomsForm orderCustomsForm = form.getOrderCustomsForm();

            //生成报关订单号
            if (form.getCmd().equals("submit") && CollectionUtil.isNotEmpty(orderCustomsForm.getSubOrders())) {

                for (InputSubOrderCustomsForm subOrder : orderCustomsForm.getSubOrders()) {

                    String orderNo = generationOrderNo(orderCustomsForm.getLegalEntityId(), orderCustomsForm.getGoodsType(), OrderStatusEnum.CBG.getCode());
                    subOrder.setOrderNo(orderNo);

//                    if (orderCustomsForm.getStatus() != null && subOrder.getStatus().equals("NL_0")) {
//                        String orderNo = generationOrderNo(orderCustomsForm.getLegalEntityId(), null, OrderStatusEnum.NLYS.getCode());
//                        subOrder.setOrderNo(orderNo);
//                    }
                }

            }

            //查询编辑条件
            //主订单草稿状态,可以对所有订单进行编辑
            //创建订单如果没有选择资料齐全,提交订单报关状态待是补全状态,可以进行编辑,报关状态待接单或者没有创建状态
            if (this.queryEditOrderCondition(orderCustomsForm.getSubCustomsStatus(),
                    inputMainOrderForm.getStatus(), SubOrderSignEnum.BG.getSignOne(), form)) {
                //如果没有生成子订单则不调用
                if (orderCustomsForm.getSubOrders() != null && orderCustomsForm.getSubOrders().size() >= 0) {

                    orderCustomsForm.getSubOrders().forEach(e -> {
                        Map<String, Object> subOrderMap = new HashMap<>();
                        submitOrderMap.put(e.getOrderNo(),
                                subOrderMap.put("departmentId", e.getDepartmentId()));
                    });

                    if (oldMainOrderNo != null) {
                        orderCustomsForm.setOldMainOrderNo(oldMainOrderNo);
                    }

                    orderCustomsForm.setMainOrderNo(mainOrderNo);
                    if (OrderStatusEnum.CBG.getCode().equals(classCode)) {
                        orderCustomsForm.setClassCode(OrderStatusEnum.CBG.getCode());
                    } else {
                        orderCustomsForm.setClassCode(OrderStatusEnum.CKBG.getCode());
                    }
                    orderCustomsForm.setLoginUser(UserOperator.getToken() == null ? form.getLoginUserName() : UserOperator.getToken());
                    orderCustomsForm.setMainOrderStatus(inputMainOrderForm.getNextStatus());
                    Boolean result = customsClient.createOrderCustoms(orderCustomsForm).getData();
                    if (!result) {//调用失败
                        return false;
                    }
                }
            }
        }
        //批量操作
//        this.orderBatchOperation(form, submitOrderMap);
        return true;
    }


    @Override
    public List<InitChangeStatusVO> findSubOrderNo(GetOrderDetailForm form) {
        List<InitChangeStatusVO> changeStatusVOS = new ArrayList<>();
        //获取主订单信息
        InputMainOrderVO inputMainOrderVO = getMainOrderById(form.getMainOrderId());
        //获取纯报关或出口报关信息
        if (OrderStatusEnum.CBG.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CKBG.getCode())) {
            List<InitChangeStatusVO> cbgList = customsClient.findCustomsOrderNo(inputMainOrderVO.getOrderNo()).getData();
            changeStatusVOS.addAll(cbgList);
        }
        //获取中港运输信息
        if (OrderStatusEnum.ZGYS.getCode().equals(form.getClassCode())
                || inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.ZGYSDD.getCode())) {
            InitChangeStatusVO initChangeStatusVO = tmsClient.getTransportOrderNo(inputMainOrderVO.getOrderNo()).getData();
            changeStatusVOS.add(initChangeStatusVO);
        }
        //获取内陆运输
        if (OrderStatusEnum.NLYS.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.NLDD.getCode())) {
            InitChangeStatusVO initChangeStatusVO = this.inlandTpClient.getOrderNo(inputMainOrderVO.getOrderNo()).getData();
            changeStatusVOS.add(initChangeStatusVO);
        }
        //空运
        if (OrderStatusEnum.KY.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.KYDD.getCode())) {
            InitChangeStatusVO initChangeStatusVO = this.freightAirClient.getAirOrderNo(inputMainOrderVO.getOrderNo()).getData();
            changeStatusVOS.add(initChangeStatusVO);
        }
        //海运
        if (OrderStatusEnum.HY.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.HYDD.getCode())) {
            InitChangeStatusVO initChangeStatusVO = this.oceanShipClient.getSeaOrderNo(inputMainOrderVO.getOrderNo()).getData();
            changeStatusVOS.add(initChangeStatusVO);
        }
        //拖车
        if (OrderStatusEnum.TC.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.TCEDD.getCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.TCIDD.getCode())) {
            List<InitChangeStatusVO> initChangeStatusVO = this.trailerClient.getTrailerOrderNo(inputMainOrderVO.getOrderNo()).getData();
            changeStatusVOS.addAll(initChangeStatusVO);
        }
        //仓储
        if (OrderStatusEnum.CC.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCEDD.getCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCIDD.getCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCFDD.getCode())) {
            if (inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCEDD.getCode())) {
                InitChangeStatusVO initChangeStatusVO = this.storageClient.getStorageOutOrderNo(inputMainOrderVO.getOrderNo()).getData();
                changeStatusVOS.add(initChangeStatusVO);
            }
            if (inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCIDD.getCode())) {
                InitChangeStatusVO initChangeStatusVO = this.storageClient.getStorageInOrderNo(inputMainOrderVO.getOrderNo()).getData();
                changeStatusVOS.add(initChangeStatusVO);
            }
            if (inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CCFDD.getCode())) {
                InitChangeStatusVO initChangeStatusVO = this.storageClient.getStorageFastOrderNo(inputMainOrderVO.getOrderNo()).getData();
                changeStatusVOS.add(initChangeStatusVO);
            }
        }
        return changeStatusVOS;
    }

    @Override
    public boolean changeStatus(ChangeStatusListForm form) {
        List<ConfirmChangeStatusForm> forms = form.getForms();
        List<CustomsChangeStatusForm> bgs = new ArrayList<>();
        List<TmsChangeStatusForm> zgys = new ArrayList<>();
        List<SubOrderCloseOpt> airs = new ArrayList<>();
        List<SubOrderCloseOpt> inlands = new ArrayList<>();
        List<SubOrderCloseOpt> seas = new ArrayList<>();
        List<SubOrderCloseOpt> trailers = new ArrayList<>();
        List<SubOrderCloseOpt> storageIn = new ArrayList<>();
        List<SubOrderCloseOpt> storageOut = new ArrayList<>();
        List<SubOrderCloseOpt> storageFast = new ArrayList<>();
        //全勾修改主订单状态
        if (form.getCheckAll()) {
            //循环处理,判断主订单是否需要录入费用
            Boolean needInputCost = false;
            //如果主订单下的所有子订单都不用录入费用那主订单也不需要录入费用了
            for (ConfirmChangeStatusForm temp : forms) {
                if (temp.getNeedInputCost()) {
                    needInputCost = true;
                    break;
                }
            }
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_3.getCode()));
            orderInfo.setId(form.getMainOrderId());
            orderInfo.setUpTime(LocalDateTime.now());
            orderInfo.setUpUser(UserOperator.getToken());
            orderInfo.setNeedInputCost(needInputCost);
            baseMapper.updateById(orderInfo);
        }
        for (ConfirmChangeStatusForm confirmChangeStatusForm : forms) {
            if (CommonConstant.BG.equals(confirmChangeStatusForm.getOrderType())) {
                CustomsChangeStatusForm bg = new CustomsChangeStatusForm();
                bg.setNeedInputCost(confirmChangeStatusForm.getNeedInputCost());
                bg.setOrderNo(confirmChangeStatusForm.getOrderNo());
                bg.setStatus(form.getStatus());
                bg.setLoginUser(UserOperator.getToken());
                bgs.add(bg);
            } else if (CommonConstant.ZGYS.equals(confirmChangeStatusForm.getOrderType())) {
                TmsChangeStatusForm tm = new TmsChangeStatusForm();
                tm.setNeedInputCost(confirmChangeStatusForm.getNeedInputCost());
                tm.setOrderNo(confirmChangeStatusForm.getOrderNo());
                tm.setStatus(form.getStatus());
                tm.setLoginUser(UserOperator.getToken());
                zgys.add(tm);
            } else if (CommonConstant.KY.equals(confirmChangeStatusForm.getOrderType())) {
                SubOrderCloseOpt air = new SubOrderCloseOpt();
                air.setNeedInputCost(confirmChangeStatusForm.getNeedInputCost());
                air.setOrderNo(confirmChangeStatusForm.getOrderNo());
//                air.setStatus(form.getStatus());
                air.setLoginUser(UserOperator.getToken());
                airs.add(air);
            } else if (CommonConstant.NLYS.equals(confirmChangeStatusForm.getOrderType())) {
                SubOrderCloseOpt closeOpt = new SubOrderCloseOpt();
                closeOpt.setNeedInputCost(confirmChangeStatusForm.getNeedInputCost());
                closeOpt.setOrderNo(confirmChangeStatusForm.getOrderNo());
//                air.setStatus(form.getStatus());
                closeOpt.setLoginUser(UserOperator.getToken());
                inlands.add(closeOpt);
            } else if (CommonConstant.HY.equals(confirmChangeStatusForm.getOrderType())) {
                SubOrderCloseOpt closeOpt = new SubOrderCloseOpt();
                closeOpt.setNeedInputCost(confirmChangeStatusForm.getNeedInputCost());
                closeOpt.setOrderNo(confirmChangeStatusForm.getOrderNo());
//                air.setStatus(form.getStatus());
                closeOpt.setLoginUser(UserOperator.getToken());
                seas.add(closeOpt);
            } else if (CommonConstant.TC.equals(confirmChangeStatusForm.getOrderType())) {
                SubOrderCloseOpt closeOpt = new SubOrderCloseOpt();
                closeOpt.setNeedInputCost(confirmChangeStatusForm.getNeedInputCost());
                closeOpt.setOrderNo(confirmChangeStatusForm.getOrderNo());
//                air.setStatus(form.getStatus());
                closeOpt.setLoginUser(UserOperator.getToken());
                trailers.add(closeOpt);
            } else if (CommonConstant.CCI.equals(confirmChangeStatusForm.getOrderType())) {
                SubOrderCloseOpt closeOpt = new SubOrderCloseOpt();
                closeOpt.setNeedInputCost(confirmChangeStatusForm.getNeedInputCost());
                closeOpt.setOrderNo(confirmChangeStatusForm.getOrderNo());
//                air.setStatus(form.getStatus());
                closeOpt.setLoginUser(UserOperator.getToken());
                storageIn.add(closeOpt);
            } else if (CommonConstant.CCE.equals(confirmChangeStatusForm.getOrderType())) {
                SubOrderCloseOpt closeOpt = new SubOrderCloseOpt();
                closeOpt.setNeedInputCost(confirmChangeStatusForm.getNeedInputCost());
                closeOpt.setOrderNo(confirmChangeStatusForm.getOrderNo());
//                air.setStatus(form.getStatus());
                closeOpt.setLoginUser(UserOperator.getToken());
                storageOut.add(closeOpt);
            } else if (CommonConstant.CCF.equals(confirmChangeStatusForm.getOrderType())) {
                SubOrderCloseOpt closeOpt = new SubOrderCloseOpt();
                closeOpt.setNeedInputCost(confirmChangeStatusForm.getNeedInputCost());
                closeOpt.setOrderNo(confirmChangeStatusForm.getOrderNo());
//                air.setStatus(form.getStatus());
                closeOpt.setLoginUser(UserOperator.getToken());
                storageFast.add(closeOpt);
            }

        }
        //处理报关订单
        if (bgs.size() > 0) {
            customsClient.changeCustomsStatus(bgs).getData();
        }
        if (zgys.size() > 0) {
            tmsClient.changeTransportStatus(zgys).getData();
        }
        if (airs.size() > 0) {
            freightAirClient.closeAirOrder(airs).getData();
        }
        if (inlands.size() > 0) {
            inlandTpClient.closeOrder(inlands).getData();
        }
        if (seas.size() > 0) {
            oceanShipClient.closeOrder(seas).getData();
        }
        if (trailers.size() > 0) {
            trailerClient.closeOrder(trailers).getData();
        }
        if (storageIn.size() > 0) {
            storageClient.closeInOrder(storageIn).getData();
        }
        if (storageOut.size() > 0) {
            storageClient.closeOutOrder(storageOut).getData();
        }
        if (storageFast.size() > 0) {
            storageClient.closeFastOrder(storageFast).getData();
        }
        return true;
    }

    @Override
    public OrderDataCountVO countOrderData(QueryOrderInfoForm form) {

        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();
        DataControl dataControl = this.oauthClient.getDataPermission(form.getLoginUserName(), UserTypeEnum.EMPLOYEE_TYPE.getCode()).getData();

        return baseMapper.countOrderData(form, dataControl);
    }

    @Override
    public InitGoCustomsAuditVO initGoCustomsAudit(InitGoCustomsAuditForm form) {
        InitGoCustomsAuditVO initGoCustomsAuditVO = new InitGoCustomsAuditVO();
        //查询主订单信息
        OrderInfo orderInfo = this.getByOrderNos(Collections.singletonList(form.getOrderNo())).get(0);
        //查询中港信息
        Object tmsOrderInfos = this.tmsClient.getTmsOrderInfoByMainOrderNos(Collections.singletonList(form.getOrderNo())).getData();


        String prePath = fileClient.getBaseUrl().getData().toString();
        if (orderInfo.getSelectedServer().contains(OrderStatusEnum.CKBG.getCode())) {//出口报关
            initGoCustomsAuditVO = baseMapper.initGoCustomsAudit1(form);
            //内部报关附件
            //查询报关六联单号附件
            List<FileView> encodePics = this.customsClient.getEncodePicByMainOrderNo(form.getOrderNo()).getData();

            String[] statusList = {OrderStatusEnum.CUSTOMS_C_11.getCode(), OrderStatusEnum.CUSTOMS_C_10.getCode()};
            for (int i = 0; i < statusList.length; i++) {
                String status = statusList[i];
                List<LogisticsTrack> logisticsTracks = this.logisticsTrackService.getByCondition(new LogisticsTrack()
                        .setMainOrderId(orderInfo.getId()).setStatus(status));
                if (CollectionUtil.isEmpty(logisticsTracks)) {
                    continue;
                }
                LogisticsTrack logisticsTrack = logisticsTracks.get(logisticsTracks.size() - 1);
                List<FileView> fileViews = StringUtils.getFileViews(logisticsTrack.getStatusPic()
                        , logisticsTrack.getStatusPicName(), prePath);
                switch (i) { //设置舱单文件
                    case 0:
                        initGoCustomsAuditVO.setManifestAttachment(fileViews);
                        break;
                    case 1: //设置报关文件
                        initGoCustomsAuditVO.setCustomsOrderAttachment(fileViews);
                        break;
                }
            }
            initGoCustomsAuditVO.setEncodePics(encodePics);

        } else {//外部报关放行
            initGoCustomsAuditVO = baseMapper.initGoCustomsAudit2(form);
            //外部报关附件
            List<OrderAttachment> orderAttachments = orderAttachmentService.getByMainOrderNoAndRemarks(form.getOrderNo()
                    , Arrays.asList(OrderAttachmentTypeEnum.SIX_SHEET_ATTACHMENT.getDesc(),
                            OrderAttachmentTypeEnum.MANIFEST_ATTACHMENT.getDesc(),
                            OrderAttachmentTypeEnum.CUSTOMS_ATTACHMENT.getDesc()));

            initGoCustomsAuditVO.distributeFiles(orderAttachments, prePath);
        }
        initGoCustomsAuditVO.assemblyData(tmsOrderInfos);
//        initGoCustomsAuditVO.setFileViewList(StringUtils.getFileViews(initGoCustomsAuditVO.getFileStr(), initGoCustomsAuditVO.getFileNameStr(), prePath));
        return initGoCustomsAuditVO;
    }

    /**
     * 根据客户名称获取订单信息
     */
    @Override
    public List<OrderInfo> getByCustomerName(String customerName) {
        QueryWrapper<OrderInfo> condition = new QueryWrapper<>();
        if (customerName != null) {
            condition.lambda().like(OrderInfo::getCustomerName, customerName);
        }
        return this.baseMapper.selectList(condition);
    }

    /**
     * 根据主订单集合查询主订单信息
     */
    @Override
    public List<OrderInfo> getByOrderNos(List<String> orderNos) {
        QueryWrapper<OrderInfo> condition = new QueryWrapper<>();
        condition.lambda().in(OrderInfo::getOrderNo, orderNos);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 根据主订单集合查询主订单信息
     */
    @Override
    public boolean updateByMainOrderNo(String mainOrderNo, OrderInfo orderInfo) {
        QueryWrapper<OrderInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderInfo::getOrderNo, mainOrderNo);
        return update(orderInfo, condition);
    }

    /**
     * 根据主订单号查询所有子订单数据
     *
     * @param mainOrderNoList
     * @return
     */
    @Override
    public Map<String, Map<String, Object>> getSubOrderByMainOrderNos(List<String> mainOrderNoList) {
        //报关
        ApiResult result = this.customsClient.getCustomsOrderByMainOrderNos(mainOrderNoList);
        Map<String, List<Map<String, Object>>> customsOrderMap = this.object2Map(result.getData());

        //中港
        result = this.tmsClient.getTmsOrderByMainOrderNos(mainOrderNoList);
        Map<String, List<Map<String, Object>>> tmsOrderMap = this.object2Map(result.getData());

        //空运
//        result = this.freightAirClient.getAirOrderByMainOrderNos(mainOrderNoList);
//        Map<String, List<Map<String, Object>>> airOrderMap = this.object2Map(result.getData());

        //海运
        result = this.oceanShipClient.getSeaOrderByMainOrderNos(mainOrderNoList);
        Map<String, List<Map<String, Object>>> seaOrderMap = this.object2Map(result.getData());

        //拖车
        result = this.trailerClient.getTrailerOrderByMainOrderNos(mainOrderNoList);
        Map<String, List<Map<String, Object>>> trailerOrderMap = this.object2Map(result.getData());


        //内陆运输
        result = this.inlandTpClient.getInlandOrderByMainOrderNos(mainOrderNoList);
        Map<String, List<Map<String, Object>>> inlandOrderMap = this.object2Map(result.getData());

        Map<String, Map<String, Object>> map = new HashMap<>();
        for (String mainOrderNo : mainOrderNoList) {
            Map<String, Object> subOrder = new HashMap<>();
            subOrder.put(KEY_SUBORDER[0], tmsOrderMap.get(mainOrderNo));
//            subOrder.put(KEY_SUBORDER[1], airOrderMap.get(mainOrderNo));
            subOrder.put(KEY_SUBORDER[2], seaOrderMap.get(mainOrderNo));
            subOrder.put(KEY_SUBORDER[3], customsOrderMap.get(mainOrderNo));
            subOrder.put(KEY_SUBORDER[4], inlandOrderMap.get(mainOrderNo));
            subOrder.put(KEY_SUBORDER[5], trailerOrderMap.get(mainOrderNo));
            map.put(mainOrderNo, subOrder);
        }
        return map;
    }

    /**
     * 获取法人主体下的待外部报关数
     *
     * @param dataControl
     * @return
     */
    @Override
    public int pendingExternalCustomsDeclarationNum(DataControl dataControl) {
        return this.baseMapper.pendingExternalCustomsDeclarationNum(dataControl);
    }

    /**
     * 获取法人主体下的待通关前审核
     *
     * @param legalIds
     * @return
     */
    @Override
    public int pendingGoCustomsAuditNum(List<Long> legalIds) {
        return this.baseMapper.pendingGoCustomsAuditNum(legalIds);
    }


    /**
     * 是否录用过费用
     *
     * @param orderNo
     * @param type    0.主订单,1子订单
     * @return
     */
    @Override
    public boolean isCost(String orderNo, Integer type) {
        if (orderReceivableCostService.isCost(orderNo, type)) {
            return true;
        }
        if (orderPaymentCostService.isCost(orderNo, type)) {
            return true;
        }
        return false;
    }

    /**
     * 根据主订单号码集合和状态查询订单
     *
     * @return
     */
    @Override
    public List<OrderInfo> getOrderByStatus(List<String> orderNo, Integer status) {
        QueryWrapper<OrderInfo> condition = new QueryWrapper<>();
        condition.lambda().in(OrderInfo::getOrderNo, orderNo)
                .eq(OrderInfo::getStatus, status);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 获取费用状态
     */
    @Override
    public Map<String, Object> getCostStatus(List<String> mainOrderNo, List<String> subOrderNo) {
        Map<String, Object> receivableCallbackParam = new HashMap<>();
        Map<String, Object> paymentCallbackParam = new HashMap<>();

        Map<String, Object> receivableCostStatus = this.orderReceivableCostService.getOrderCostStatus(mainOrderNo, subOrderNo, receivableCallbackParam);
        Map<String, Object> paymentCostStatus = this.orderPaymentCostService.getOrderCostStatus(mainOrderNo, subOrderNo, paymentCallbackParam);
        Map<String, Object> map = new HashMap<>();
        map.put("receivableCostStatus", receivableCostStatus);
        map.put("paymentCostStatus", paymentCostStatus);
        return map;
    }


    /**
     * 过滤关前审核
     *
     * @param callbackParam
     * @param dataControl
     * @param userName
     * @return
     */
    @Override
    public Set<Long> filterGoCustomsAudit(Map<String, Object> callbackParam, DataControl dataControl, String userName) {
        //1.完成过磅,2.选择没有过磅情况下完成提货,需要到通关前审核
        Object tmsOrders = this.tmsClient.preconditionsGoCustomsAudit().getData();
        if (tmsOrders == null) {
            return new HashSet<>();
        }
        JSONArray tmsJsonArray = new JSONArray(tmsOrders);
        //获取所有主订单号
        List<String> mainOrderNos = new ArrayList<>();
        Map<String, Object> tmsOrderMap = new HashMap<>();
        for (int i = 0; i < tmsJsonArray.size(); i++) {
            JSONObject tmsOrder = tmsJsonArray.getJSONObject(i);
            String mainOrderNo = tmsOrder.getStr("mainOrderNo");
            mainOrderNos.add(mainOrderNo);
            tmsOrderMap.put(mainOrderNo, tmsOrder);
        }
        if (CollectionUtil.isEmpty(mainOrderNos)) {
            return new HashSet<>();
        }

        //批量查询主订单详情
        QueryWrapper<OrderInfo> condition = new QueryWrapper<>();
        condition.lambda().select(OrderInfo::getId, OrderInfo::getOrderNo, OrderInfo::getCustomsRelease)
                .in(OrderInfo::getOrderNo, mainOrderNos);
        if (!StringUtils.isEmpty(userName)) {
            condition.lambda().eq(OrderInfo::getCreatedUser, userName);
        }
        if (CollectionUtil.isNotEmpty(dataControl.getCompanyIds())) {
            condition.lambda().in(OrderInfo::getLegalEntityId, dataControl.getCompanyIds());
        }
        if (CollectionUtils.isNotEmpty(dataControl.getDepartmentId())) {
            condition.lambda().in(OrderInfo::getBizBelongDepart, dataControl.getDepartmentId());
        }

        List<OrderInfo> orderInfos = this.baseMapper.selectList(condition);
        //分发订单号
        Set<Long> orderIds = new HashSet<>();
        Map<String, Long> customsMainOrderMap = new HashMap<>();
        for (OrderInfo orderInfo : orderInfos) {
            if (orderInfo.getCustomsRelease()) {
                //外部报关放行通过
                orderIds.add(orderInfo.getId());
            } else {
                //内部报关过滤条件
                customsMainOrderMap.put(orderInfo.getOrderNo(), orderInfo.getId());
            }
        }

        //查询报关订单,需要全部都放行审核通过
        if (CollectionUtil.isNotEmpty(customsMainOrderMap)) {
            List<String> customsMainOrders = this.customsClient.getAllPassByMainOrderNos(new ArrayList<>(customsMainOrderMap.keySet())).getData();
            if (CollectionUtil.isNotEmpty(customsMainOrders)) {
                customsMainOrders.forEach(e -> orderIds.add(customsMainOrderMap.get(e)));
            }
        }
        if (CollectionUtil.isNotEmpty(callbackParam)) {
            callbackParam.put("tmsOrders", tmsOrderMap);
        }

        return orderIds;
    }

    /**
     * 供应商录入费用
     *
     * @param form
     */
    @Override
    public void doSupplierEntryFee(InputCostForm form) {
        //获取登录用户信息
        DataControl dataControl = this.oauthClient.getDataPermission(UserOperator.getToken(), UserTypeEnum.SUPPLIER_TYPE.getCode()).getData();
        if (CollectionUtils.isEmpty(dataControl.getCompanyIds())) {
            throw new JayudBizException(400, "无法操作");
        }
        //查询供应商信息
        SupplierInfo supplierInfo = this.supplierInfoService.getById(dataControl.getCompanyIds().get(0));
        //查询主订单
        OrderInfo orderInfo = this.getById(form.getMainOrderId());
        //币种汇率
        Map<String, BigDecimal> currencyRates = this.financeClient.getExchangeRates("CNY", DateUtils.LocalDateTime2Str(orderInfo.getCreateTime(), "yyyy-MM")).getData();
        //主订单业务类型(费用类型)
        ProductBiz productBiz = this.productBizService.getProductBizByCode(orderInfo.getBizCode());
        //费用类别
        List<CostType> costTypes = this.costTypeService.getByCondition(new CostType().setCode("FYLB2002"));
        if (CollectionUtils.isEmpty(costTypes)) {
            throw new JayudBizException(400, "不存在该费用类别");
        }
        form.getPaymentCostList().forEach(e -> {
            BigDecimal exchangeRate = currencyRates.get(e.getCurrencyCode());
            e.setUnit(UnitEnum.PCS.getCode());
            e.setCustomerCode(supplierInfo.getSupplierCode());
            e.setCostTypeId(costTypes.get(0).getId());
            e.setCostGenreId(productBiz.getCostGenreDefault());
            e.setExchangeRate(exchangeRate);
            if (e.getAmount() != null && exchangeRate != null) {
                e.setChangeAmount(e.getAmount().multiply(exchangeRate));
            }
            e.setSupplierId(supplierInfo.getId());
        });
        form.setReceivableCostList(new ArrayList<>());
        //1.需求为，提交审核按钮跟在每一条记录后面 2.暂存是保存所有未提交审核的数据  3.提交审核的数据不可编辑和删除
        this.saveOrUpdateCost(form);
    }


    /**
     * 子订单使用
     * JSONArray转Map
     */
    private Map<String, List<Map<String, Object>>> object2Map(Object data) {
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        if (data != null) {
            JSONArray orders = new JSONArray(data);
            for (int i = 0; i < orders.size(); i++) {
                JSONObject order = orders.getJSONObject(i);
                List<Map<String, Object>> subOrder = map.get(order.getStr("mainOrderNo"));
                if (subOrder != null) {
                    subOrder.add(order.toBean(Map.class));
                    map.put(order.getStr("mainOrderNo"), subOrder);
                } else {
                    List<Map<String, Object>> list = new ArrayList<>();
                    list.add(order.toBean(Map.class));
                    map.put(order.getStr("mainOrderNo"), list);
                }
            }

        }
        return map;
    }


    /**
     * 应付消息推送
     *
     * @param orderReceivableCosts
     */
    public void receivableAuditMsgPush(List<OrderReceivableCost> orderReceivableCosts) {
        OrderReceivableCost orderReceivableCost = this.receivableCostService.getById(orderReceivableCosts.get(0).getId());
        if (StringUtils.isEmpty(orderReceivableCost.getOrderNo())) {
            return;
        }
        switch (ReceivableAndPayableOrderTypeEnum.getEnum(orderReceivableCost.getSubType())) {
            case KY:
                this.airFreightFeePush(orderReceivableCost.getMainOrderNo(), orderReceivableCost.getOrderNo()
                        , orderReceivableCosts);
                break;
            case ZGYS:
                this.tmsFeePush(orderReceivableCost.getMainOrderNo(), orderReceivableCost.getOrderNo()
                        , orderReceivableCosts);
                break;

        }
    }

    /**
     * 空运费用推送
     *
     * @param mainOrderNo
     * @param orderNo
     * @param orderReceivableCosts
     */
    public void airFreightFeePush(String mainOrderNo, String orderNo,
                                  List<OrderReceivableCost> orderReceivableCosts) {
        //查询空运订单信息
        ApiResult result = this.freightAirClient.getAirOrderInfoByOrderNo(orderNo);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("查询空运订单信息失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject jsonObject = new JSONObject(result.getData());
        Integer createUserType = jsonObject.getInt("createUserType");
        switch (getEnum(createUserType)) {
            case VIVO:
                List<Long> ids = orderReceivableCosts.stream().map(OrderReceivableCost::getId).collect(Collectors.toList());
                //查询审核通过应收费用
                List<OrderReceivableCost> receivableCosts = this.receivableCostService.getApprovalFee(mainOrderNo, ids);
                //操作类型判断
                Map<String, String> map = new HashMap<>();
                map.put("topic", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_FOUR.getTopic());
                map.put("key", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_FOUR.getKey());
                Map<String, Object> msg = new HashMap<>();
                msg.put("mainOrderNo", mainOrderNo);
                msg.put("airOrderId", jsonObject.getLong("id"));
                msg.put("operationType", receivableCosts.size() > 0 ? "update" : "add");
                msg.put("bookingNo", jsonObject.getStr("thirdPartyOrderNo"));
                //组装商品
                receivableCosts.addAll(orderReceivableCosts);
                List<Map<String, Object>> costItems = new ArrayList<>();
                for (OrderReceivableCost receivableCost : receivableCosts) {
                    Map<String, Object> tmp = new HashMap<>();
                    tmp.put("costItemCode", receivableCost.getCostCode());
                    tmp.put("currencyOfPayment", receivableCost.getCurrencyCode());
                    tmp.put("amountPayable", receivableCost.getAmount());
                    costItems.add(tmp);
                }
                msg.put("line", costItems);
                map.put("msg", JSONUtil.toJsonStr(msg));
                msgClient.consume(map);
                break;
            default:
        }
    }


    /**
     * 中港费用推送
     *
     * @param mainOrderNo
     * @param orderNo
     * @param orderReceivableCosts
     */
    public void tmsFeePush(String mainOrderNo, String orderNo,
                           List<OrderReceivableCost> orderReceivableCosts) {
        //查询中港订单信息
        ApiResult result = this.tmsClient.getTmsOrderByOrderNo(orderNo);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("查询中港订单信息失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject jsonObject = new JSONObject(result.getData());
        Integer createUserType = jsonObject.getInt("createUserType");
        switch (getEnum(createUserType)) {
            case VIVO:
                List<Long> ids = orderReceivableCosts.stream().map(OrderReceivableCost::getId).collect(Collectors.toList());
                //查询审核通过应收费用
                List<OrderReceivableCost> receivableCosts = this.receivableCostService.getApprovalFee(mainOrderNo, ids);
                //操作类型判断
                Map<String, String> map = new HashMap<>();
                map.put("topic", KafkaMsgEnums.VIVO_FREIGHT_TMS_MESSAGE_TWO.getTopic());
                map.put("key", KafkaMsgEnums.VIVO_FREIGHT_TMS_MESSAGE_TWO.getKey());
                Map<String, Object> msg = new HashMap<>();
                msg.put("mainOrderNo", mainOrderNo);
                msg.put("tmsOrderId", jsonObject.getLong("id"));
                msg.put("operationType", receivableCosts.size() > 0 ? "update" : "add");
                msg.put("dispatchNo", jsonObject.getStr("thirdPartyOrderNo"));
                //组装商品
                receivableCosts.addAll(orderReceivableCosts);
                List<Map<String, Object>> costItems = new ArrayList<>();
                for (OrderReceivableCost receivableCost : receivableCosts) {
                    Map<String, Object> tmp = new HashMap<>();
                    tmp.put("costItemCode", receivableCost.getCostCode());
                    tmp.put("currencyOfPayment", receivableCost.getCurrencyCode());
                    tmp.put("amountPayable", receivableCost.getAmount());
                    costItems.add(tmp);
                }
                msg.put("line", costItems);
                map.put("msg", JSONUtil.toJsonStr(msg));
                msgClient.consume(map);
                break;
            default:
        }
    }

    /**
     * 查询是否可编辑
     */
    private boolean queryEditOrderCondition(String orderStatus, Integer mainOrderStatus,
                                            String orderType, InputOrderForm form) {


        //主订单是草稿状态,和初始化时候可以通过校验
        if (mainOrderStatus == null || OrderStatusEnum.MAIN_2.getCode().equals(String.valueOf(mainOrderStatus))) {
            return true;
        }
        String mainOrderStatusStr = String.valueOf(mainOrderStatus);
        if (StringUtils.isEmpty(orderStatus)) {
            return true;
        }

        //报关
        if (SubOrderSignEnum.BG.getSignOne().equals(orderType)) {
            InputOrderTransportForm orderTransportForm = form.getOrderTransportForm() == null
                    ? new InputOrderTransportForm() : form.getOrderTransportForm();

            if (OrderStatusEnum.CUSTOMS_C_1_1.getCode().equals(orderStatus)
                    && (orderTransportForm.getIsGoodsEdit() == null
                    || !orderTransportForm.getIsGoodsEdit())) {
                return true;
            }
            if (OrderStatusEnum.MAIN_4.getCode().equals(mainOrderStatusStr)) {
                return true;
            }
            return false;
        }
        //中港
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(orderType)) {
            InputOrderTransportForm orderTransportForm = form.getOrderTransportForm();
            if (orderTransportForm.getIsGoodsEdit()) { //货物编辑可以进行编辑
                return true;
            }

        }
        //快进快出编辑
        if (SubOrderSignEnum.CCF.getSignOne().equals(orderType)) {
            return true;

        }
        //只有中港的货物编辑,驳回可以编辑
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(orderType)) {
            if (OrderStatusEnum.getRejectionStatus(orderStatus, orderType) != null) {
                return true;
            }
        } else {
            //其他情况下,货物编辑==null和false情况下,子订单驳回才能编辑
            InputOrderTransportForm orderTransportForm = form.getOrderTransportForm();
            orderTransportForm = orderTransportForm == null ? new InputOrderTransportForm() : orderTransportForm;
            if (OrderStatusEnum.getRejectionStatus(orderStatus, orderType) != null
                    && (orderTransportForm.getIsGoodsEdit() == null
                    || !orderTransportForm.getIsGoodsEdit())) {
                return true;
            }
        }

        return false;
    }


    /**
     * 查询子订单驳回原因
     *
     * @return
     */
//    private void getSubOrderRejectionMsg
//    (List<OrderInfoVO> orderInfoVOs, Map<String, Map<String, Object>> subOrderMap) {
//        for (OrderInfoVO orderInfoVO : orderInfoVOs) {
//            Map<String, Object> subOrderInfos = subOrderMap.get(orderInfoVO.getOrderNo());
//            String[] rejectionStatus = OrderStatusEnum.getRejectionStatus(null);
//            StringBuffer sb = StringUtils.isEmpty(orderInfoVO.getRejectComment()) ? new StringBuffer() : new StringBuffer(orderInfoVO.getRejectComment() + ",");
//            subOrderInfos.forEach((key, value) -> {
//                if (value != null) {
//                    String tableDesc = SubOrderSignEnum.getSignOne2SignTwo(key);
//                    if (value instanceof List) {
//                        List<Map<String, Object>> maps = (List<Map<String, Object>>) value;
//                        for (Map<String, Object> map : maps) {
//                            AuditInfo auditInfo = this.auditInfoService.getLatestByRejectionStatus(Long.valueOf(map.get("id").toString()),
//                                    tableDesc + "表", rejectionStatus);
//                            if (!StringUtils.isEmpty(auditInfo.getAuditComment())) {
//                                sb.append(map.get("orderNo")).append("-")
//                                        .append(auditInfo.getAuditComment()).append(",");
//                            }
//                        }
//                    }
//                }
//            });
//            if (!StringUtils.isEmpty(sb.toString())) {
//                orderInfoVO.setRejectComment(sb.substring(0, sb.length() - 1));
//            }
//        }
//    }

    /**
     * 组装数据
     *
     * @param orderInfoVOs
     * @param subOrderMap
     */
    private void assemblyMasterOrderData(List<OrderInfoVO> orderInfoVOs,
                                         Map<String, Map<String, Object>> subOrderMap) {
        for (OrderInfoVO orderInfoVO : orderInfoVOs) {
            //子订单信息
            Map<String, Object> subOrderInfos = subOrderMap.get(orderInfoVO.getOrderNo());
            //订单状态
//            orderInfoVO.setSubOrderStatusDesc(assemblySubOrderStatus(subOrderInfos, orderInfoVO));
            //增加中港信息字段
            //增加空运信息字段
            //商品信息组合
            orderInfoVO.setGoodsInfo(assemblySubOrderGoods(subOrderInfos));
        }

    }

    private String assemblySubOrderGoods(Map<String, Object> subOrderInfos) {
        //中港商品信息
        Object tmsOrder = subOrderInfos.get(KEY_SUBORDER[0]);
        StringBuffer goodsInfos = new StringBuffer();
        if (tmsOrder != null) {
            JSONObject tmsOrderJSON = new JSONArray(tmsOrder).getJSONObject(0);
            JSONArray orderTakeAdrs = tmsOrderJSON.getJSONArray("orderTakeAdrs");
            for (int i = 0; i < orderTakeAdrs.size(); i++) {
                JSONObject orderTakeAdr = orderTakeAdrs.getJSONObject(0);
                goodsInfos.append(orderTakeAdr.getStr("goodsDesc"))
                        .append("/")
                        .append(orderTakeAdr.getInt("plateAmount", 0)).append("板")
                        .append("/")
                        .append(orderTakeAdr.getInt("pieceAmount", 0)).append("件")
                        .append("/")
                        .append(orderTakeAdr.getDouble("weight", 0.0)).append("重量")
                        .append(",");
            }

        }
        //空运商品信息

        return goodsInfos.toString();

    }

    /**
     * 组装订单状态
     *
     * @param subOrderInfos
     * @param orderInfoVO
     * @return
     */
    private String assemblySubOrderStatus(Map<String, Object> subOrderInfos, OrderInfoVO orderInfoVO) {
        StringBuffer subOrderStatus = new StringBuffer();
        for (String subOrderType : KEY_SUBORDER) {
            Object subOrder = subOrderInfos.get(subOrderType);

            JSONArray array = new JSONArray(subOrder);
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                subOrderStatus.append(jsonObject.getStr("orderNo"))
                        .append("-").append(OrderStatusEnum.getDesc(jsonObject.getStr("status"))).append(",");

                if (SubOrderSignEnum.NL.getSignOne().equals(subOrderType)) {
//                    orderInfoVO.setSubInlandStatus(jsonObject.getStr("status"));
                }
            }
        }
        return subOrderStatus.toString();
    }


    private List<OrderFlowSheet> assemblyProcess(String mainOrderNo, String orderNo,
                                                 String classifyId, String classifyName,
                                                 List<OrderStatusEnum> process) {

        String preStatus = null;
        List<OrderFlowSheet> list = new ArrayList<>();
        for (OrderStatusEnum tpProcess : process) {
            OrderFlowSheet orderFlowSheet = new OrderFlowSheet();

            orderFlowSheet.setMainOrderNo(mainOrderNo)
                    .setOrderNo(orderNo)
                    .setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken())
                    .setProductClassifyId(classifyId)
                    .setProductClassifyName(classifyName)
                    .setStatus(tpProcess.getCode())
                    .setStatusName(tpProcess.getDesc())
                    .setFStatus(preStatus)
                    .setIsPass("1");
            preStatus = tpProcess.getCode();
            list.add(orderFlowSheet);
        }
        return list;
    }

    private void initProcessNode(String mainOrderNo, String orderNo,
                                 OrderStatusEnum statusEnum, InputOrderForm form,
                                 Long id, List<OrderStatusEnum> process) {
        if ("submit".equals(form.getCmd())) {
            List<OrderFlowSheet> tmp = null;
            if (id != null) {
                tmp = this.orderFlowSheetService.getByCondition(new OrderFlowSheet().setOrderNo(orderNo));

            }
            if (CollectionUtil.isEmpty(tmp)) {
                //流程节点重组
                List<OrderFlowSheet> orderFlowSheets = this.assemblyProcess(mainOrderNo, orderNo, statusEnum.getCode(), statusEnum.getDesc(), process);
                this.orderFlowSheetService.saveOrUpdateBatch(orderFlowSheets);
            }

        }
    }


    private void specialTreatment(InputOrderForm form, InputMainOrderForm orderInfo) {
        //中港货物编辑入口时候,不能修改主订单驳回状态
        InputOrderTransportForm orderTransportForm = form.getOrderTransportForm();
        if (orderTransportForm != null
                && orderTransportForm.getIsGoodsEdit() != null
                && orderTransportForm.getIsGoodsEdit()) {
            return;
        }
        orderInfo.setIsRejected(false);
        orderInfo.setRejectComment(" ");
    }


    /**
     * 内部往来
     *
     * @param form
     */
    private void costMergeOpt(AuditCostForm form) {
        if (form.getSubUnitCode() == null || form.getReceivableCosts().size() == 0) {
            return;
        }
        //根据结算单位查询客户名称
        CustomerInfo customerInfo = this.customerInfoService.getByCode(form.getSubUnitCode());
        //查询主订单操作主体
        String mainOrderNo;
        if (CollectionUtil.isNotEmpty(form.getPaymentCosts())) {
            mainOrderNo = form.getPaymentCosts().get(0).getMainOrderNo();
        } else {
            mainOrderNo = form.getReceivableCosts().get(0).getMainOrderNo();
        }
        List<OrderInfo> mainOrders = this.getByOrderNos(Collections.singletonList(mainOrderNo));
        OrderInfo orderInfo = mainOrders.get(0);
        //子订单结算单位和主订单操作主体匹配
        if (!orderInfo.getLegalName().equals(customerInfo.getName())) {
            return;
        }


        String tmp = form.getReceivableCosts().get(0).getMainOrderNo();
        List<OrderReceivableCost> oldReceivableCostList = this.orderReceivableCostService.getSubInternalCostByMainOrderNo(tmp, null);

        //应付供应商是子订单操作主体
        SupplierInfo supplierInfo = this.supplierInfoService.getByName(form.getSubLegalName());

        List<OrderReceivableCost> newReceivableCosts = ConvertUtil.convertList(form.getReceivableCosts(), OrderReceivableCost.class);
        //设置内部往来标识
        form.getReceivableCosts().forEach(e -> e.setIsInternal(true));


//        form.getReceivableCosts().forEach(e -> {
//            e.setCustomerName(supplierInfo.getSupplierChName()).setCustomerCode(supplierInfo.getSupplierCode());
//        });
        newReceivableCosts.forEach(e -> {
            e.setCustomerName(supplierInfo.getSupplierChName()).setCustomerCode(supplierInfo.getSupplierCode());
        });

//        orderReceivableCostList.addAll(orderReceivableCosts);
        newReceivableCosts.addAll(oldReceivableCostList);
        //过滤相同数据
        newReceivableCosts = newReceivableCosts.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(OrderReceivableCost::getId))), ArrayList::new));


        //根据主订单号查询主订单应付费用并且是绑定应收id
        List<OrderPaymentCost> paymentCost = this.paymentCostService.getReceivableBinding(new OrderPaymentCost()
                .setMainOrderNo(mainOrderNo).setIsSumToMain(true));
        //合拼操作
        ConcurrentMap<Long, OrderPaymentCost> oldBinds = paymentCost.stream().collect(Collectors.toConcurrentMap(OrderPaymentCost::getReceivableId, e -> e));
        //添加/修改
        List<OrderPaymentCost> addOrUpdate = new ArrayList<>();


        for (OrderReceivableCost receivableCost : newReceivableCosts) {

            OrderPaymentCost bind = oldBinds.remove(receivableCost.getId());
            OrderPaymentCost convert = ConvertUtil.convert(receivableCost, OrderPaymentCost.class);
            //内部往来
            receivableCost.setIsInternal(true);
            convert.setIsInternal(true);
            if (bind != null) {
                if (!bind.getStatus().toString().equals(OrderStatusEnum.COST_1.getCode())) {
                    //只同步草稿状态
                    continue;
                }
                convert.setId(bind.getId()).setReceivableId(receivableCost.getId()).setCustomerName(null)
                        .setCustomerCode(null);
            } else {
                convert.setId(null).setReceivableId(receivableCost.getId())
                        .setCreatedTime(LocalDateTime.now()).setCreatedUser(UserOperator.getToken());
            }
            convert.setOrderNo(null).setLegalName(null).setLegalId(null)
                    .setStatus(Integer.valueOf(OrderStatusEnum.COST_1.getCode()))
                    .setIsSumToMain(true).setIsBill("0").setSubType("main")
                    .setInternalDepartmentId(receivableCost.getDepartmentId())
                    .setDepartmentId(Long.valueOf(orderInfo.getBizBelongDepart()));
//            if (supplierInfo != null) {
//                convert.setCustomerName(supplierInfo.getSupplierChName()).setCustomerCode(supplierInfo.getSupplierCode());
//            }
            addOrUpdate.add(convert);
        }
        //剔除应付费用
        for (OrderPaymentCost deleteBind : oldBinds.values()) {
            if (!deleteBind.getStatus().toString()
                    .equals(OrderStatusEnum.COST_1.getCode())) {
                oldBinds.remove(deleteBind.getReceivableId());
            }
        }
        List<Long> deletePayIds = new ArrayList<>();
        oldBinds.forEach((k, v) -> {
            deletePayIds.add(v.getId());
        });
        this.paymentCostService.removeByIds(deletePayIds);
        this.paymentCostService.saveOrUpdateBatch(addOrUpdate);

    }

    private void doReimbursementOpt(AuditCostForm form) {
        Map<String, CostInfo> costInfoMap = this.costInfoService.list().stream().filter(e -> e.getIsReimbursement() != null && e.getIsReimbursement()).collect(Collectors.toMap(e -> e.getIdCode(), e -> e));
        List<OrderPaymentCost> paymentCosts = form.getPaymentCosts().stream().filter(e ->(e.getIsInternal()==null||!e.getIsInternal())&& costInfoMap.get(e.getCostCode()) != null).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(paymentCosts)) {
            return;
        }
        //查询主订单操作主体
        String mainOrderNo = paymentCosts.get(0).getMainOrderNo();

        OrderInfo mainOrder = this.getByOrderNos(Collections.singletonList(mainOrderNo)).get(0);
        CustomerInfo customerInfo = this.customerInfoService.getByCode(mainOrder.getUnitCode());
        CustomerInfo subCustomerInfo = this.customerInfoService.getByCode(form.getSubUnitCode());
        List<OrderReceivableCost> list = new ArrayList<>();
        List<Long> receivableIds = new ArrayList<>();
        for (OrderPaymentCost paymentCost : paymentCosts) {
            OrderReceivableCost receivableCost = ConvertUtil.convert(paymentCost, OrderReceivableCost.class);
            if (!OrderStatusEnum.COST_0.getCode().equals(form.getStatus())) {
                if (paymentCost.getIsSumToMain()) {
                    receivableCost.setSubType(SubOrderSignEnum.MAIN.getSignOne()).setCustomerCode(mainOrder.getUnitCode()).setCustomerName(customerInfo.getName());
                } else {
                    receivableCost.setOrderNo(form.getSubOrderNo()).setCustomerCode(form.getSubUnitCode()).setCustomerName(subCustomerInfo.getName());
                }
            }
            if (paymentCost.getReceivableId() != null) {
                receivableIds.add(paymentCost.getReceivableId());
            }
            list.add(receivableCost.setId(null).setIsBill("0").setStatus(Integer.valueOf(OrderStatusEnum.COST_2.getCode())).setId(paymentCost.getReceivableId()).setIsReimbursement(true));
        }
        if (!CollectionUtils.isEmpty(receivableIds)) {
            Map<Long, OrderReceivableCost> oldReceivableCostMap = this.orderReceivableCostService.listByIds(receivableIds).stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
            list = list.stream().map(e -> {
                OrderReceivableCost orderReceivableCost = oldReceivableCostMap.get(e.getId());
                if (e.getId() != null) {
                    if (OrderStatusEnum.COST_0.getCode().equals(form.getStatus())
                            && OrderStatusEnum.COST_3.getCode().equals(orderReceivableCost.getStatus() + "")) {
                        throw new JayudBizException(400, "应收费用已审核无法进行反审核");
                    }
                }
                if (OrderStatusEnum.COST_2.getCode().equals(e.getStatus() + "") ||
                        OrderStatusEnum.COST_1.getCode().equals(e.getStatus() + "")) {
                    return e;
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }


        this.receivableCostService.saveOrUpdateBatch(list);


        for (int i = 0; i < paymentCosts.size(); i++) {
            paymentCosts.get(i).setReceivableId(list.get(i).getId()).setIsReimbursement(true);
        }
        this.paymentCostService.updateBatchById(paymentCosts);
    }

    private void orderBatchOperation(InputOrderForm form, Map<String, Object> submitOrderMap) {
        if ("submit".equals(form.getCmd())) {
            //同步更新费用
            this.synchronousUpdateFee(submitOrderMap);
        }
    }

    /**
     * 同步更新费用
     *
     * @param submitOrderMap
     */
    private void synchronousUpdateFee(Map<String, Object> submitOrderMap) {
        submitOrderMap.forEach((k, v) -> {
            if (v != null) {
                Map<String, Object> map = (Map<String, Object>) v;
                Long departmentId = (Long) map.get("departmentId");
                this.receivableCostService.updateByOrderNo(null, k,
                        new OrderReceivableCost().setDepartmentId(departmentId));
            }
        });
    }

    /**
     * 推送六联单号去供应商
     *
     * @param orderId 主订单单号
     * @param encode  六联单号
     */
    @Override
    public void pushMessageNumbers(Long orderId, String encode) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", orderId);

        OrderInfo orderInfo = this.getOne(queryWrapper);

        ApiResult orderTransportOneThirdParty = tmsClient.getOrderTransportOneThirdParty(orderInfo.getOrderNo());
        if (orderTransportOneThirdParty.getCode() != HttpStatus.SC_OK) {
            log.warn("远程调用查询客户信息失败 message=" + orderTransportOneThirdParty.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject jsonObject = JSONUtil.parseObj(orderTransportOneThirdParty.getData());
        String thirdPartyOrderNo = jsonObject.getStr("thirdPartyOrderNo");
        String createUserType = jsonObject.getStr("createUserType");

        if (Integer.parseInt(createUserType) != CreateUserTypeEnum.SCM.getCode()) {
            return;
        }
        Map<String, Object> form = new HashMap<>();
        form.put("truckNo", thirdPartyOrderNo);
        form.put("exHkNo", encode);//六联单号

        //根据主订单 unit_code   操作人去查询 客户信息表 id  customer_code
        CustomerInfo byCode = customerInfoService.getByCode(orderInfo.getCustomerCode());

        //使用id 查询  秘钥
        ClientSecretKeyVO clientSecretKeyOne = this.clientSecretKeyService.findClientSecretOne(byCode.getId().toString());
        String s1 = com.alibaba.fastjson.JSONObject.toJSONString(form);
        String sjm = null;
        try {
            sjm = RSAUtils.privateEncrypt(s1, RSAUtils.getPrivateKey(clientSecretKeyOne.getAppPrivateSecret()));
            String orderIns = com.alibaba.fastjson.JSONObject.toJSONString(sjm);
            System.out.println(orderIns);
            httpClient(sjm, clientSecretKeyOne.getAppSecret());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }


    //给供应商推送消息
    public String httpClient(String data, String publickey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("data", data);
        String s = jsonObject.toString();
        System.out.println(s);
        log.warn("远程调用查询客户信息 message=" + s);
        HttpResponse response = cn.hutool.http.HttpRequest
                .post(urlBase + urlAcceptOrderInfoNumbers)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(s)
                .execute();


        String feedback = response.body();


        System.out.println("" + feedback);
        JSONObject parseObj = JSONUtil.parseObj(feedback);

        String data2 = parseObj.getStr("data");

        String publicKey = parseObj.getStr("publicKey");

        //解密
        String jmm = RSAUtils.publicDecrypt(data2, RSAUtils.getPublicKey(publicKey));
        JSONObject paj = JSONUtil.parseObj(jmm);
        String code = paj.getStr("code");
        log.info("状态码:" + code);
        log.warn("远程调用查询客户信息失败 message=" + code);
        System.out.println("解密后的数据：" + code);
        if (org.apache.commons.lang.StringUtils.isEmpty(code)) {
            throw new JayudBizException("400，不是供应链数据失败！");
        }
        log.info("报文:" + response.toString());
        log.info("供应链返回参数:" + feedback);
        return code;
//        return JSONUtil.parseObj(jmm).get("code").toString();
//        MapUtil.getStr(feedback, "data");
//        if () {
//
//        }

    }
}
