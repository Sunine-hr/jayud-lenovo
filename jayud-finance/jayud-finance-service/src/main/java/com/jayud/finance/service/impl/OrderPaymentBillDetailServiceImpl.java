package com.jayud.finance.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.BillTypeEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.BeanUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.enums.BillTemplateEnum;
import com.jayud.finance.enums.OrderBillCostTotalTypeEnum;
import com.jayud.finance.feign.OauthClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderPaymentBillDetailMapper;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.po.OrderPaymentBill;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.service.*;
import com.jayud.finance.util.ReflectUtil;
import com.jayud.finance.vo.*;
import io.netty.util.internal.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Service
public class OrderPaymentBillDetailServiceImpl extends ServiceImpl<OrderPaymentBillDetailMapper, OrderPaymentBillDetail> implements IOrderPaymentBillDetailService {

    @Autowired
    OmsClient omsClient;
    @Autowired
    IOrderBillCostTotalService billCostTotalService;
    @Autowired
    IOrderPaymentBillService orderPaymentBillService;
    @Autowired
    ICancelAfterVerificationService verificationService;
    @Autowired
    IMakeInvoiceService makeInvoiceService;
    @Autowired
    IOrderBillCostTotalService costTotalService;
    @Autowired
    IOrderReceivableBillService receivableBillService;
    @Autowired
    ICurrencyRateService currencyRateService;
    @Autowired
    OauthClient oauthClient;
    @Autowired
    private CommonService commonService;
    @Autowired
    private DataProcessingService dataProcessingService;
    @Autowired
    private FinanceService financeService;

    @Override
    public IPage<OrderPaymentBillDetailVO> findPaymentBillDetailByPage(QueryPaymentBillDetailForm form) {
        //定义分页参数
        Page<OrderPaymentBillDetailVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("opbd.make_time"));

        //获取当前用户法人主体名字
        ApiResult legalNameBySystemName = oauthClient.getLegalNameBySystemName(form.getLoginUserName());
        List<String> data = (List<String>) legalNameBySystemName.getData();

        IPage<OrderPaymentBillDetailVO> pageInfo = baseMapper.findPaymentBillDetailByPage(page, form, data);

        List<OrderPaymentBillDetailVO> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageInfo;
        }
        dataProcessingService.processingBillDetail(records, BillTypeEnum.PAYMENT.getCode());

//        List<String> billNos = records.stream().map(OrderPaymentBillDetailVO::getBillNo).collect(toList());
//        //统计合计币种金额
//        List<Map<String, Object>> currencyAmounts = this.costTotalService.totalCurrencyAmount(billNos);
//        //审核意见
//        Map<String, Object> auditComment = this.omsClient.getByExtUniqueFlag(billNos).getData();
//        //币种
//        List<InitComboxStrVO> currencyInfo = omsClient.initCurrencyInfo().getData();
//        Map<String, String> currencyInfoMap = currencyInfo.stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));
//        //核算
//        this.makeInvoiceService.calculationAccounting(billNos, 2);
//        for (OrderPaymentBillDetailVO record : records) {
//            record.totalCurrencyAmount(currencyAmounts);
//            record.setAuditComment(MapUtil.getStr(auditComment, record.getBillNo()));
//            record.setSettlementCurrency(currencyInfoMap.get(record.getSettlementCurrency()));
//        }

        return pageInfo;
    }

    @Override
    public List<OrderPaymentBillDetailVO> findPaymentBillDetail(QueryPaymentBillDetailForm form) {
        List<OrderPaymentBillDetailVO> list = baseMapper.findPaymentBillDetailByPage(form, null);
        List<String> billNos = list.stream().map(OrderPaymentBillDetailVO::getBillNo).collect(toList());

//        List<InitComboxStrVO> currencyInfo = omsClient.initCurrencyInfo().getData();
//        Map<String, String> currencyInfoMap = currencyInfo.stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));
        //统计合计币种金额
        List<Map<String, Object>> currencyAmounts = this.costTotalService.totalCurrencyAmount(billNos);
        for (OrderPaymentBillDetailVO record : list) {
            record.totalCurrencyAmount(currencyAmounts);
        }
        return list;
    }


    @Override
    public CommonResult submitFCw(ListForm form) {
        //参数校验
        if (form.getBillNos() == null || form.getBillNos().size() == 0) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        //校验数据状态
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no", form.getBillNos());
        List<OrderPaymentBillDetail> billDetails = baseMapper.selectList(queryWrapper);
        for (OrderPaymentBillDetail billDetail : billDetails) {
            if (!BillEnum.B_2.getCode().equals(billDetail.getAuditStatus())) {
                return CommonResult.error(10001, "不符合操作条件");
            }
        }
        for (String billNo : form.getBillNos()) {
            OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
            orderPaymentBillDetail.setAuditStatus(BillEnum.B_3.getCode());
            orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
            orderPaymentBillDetail.setUpdatedUser(form.getLoginUserName());
            QueryWrapper updateWrapper = new QueryWrapper();
            updateWrapper.eq("bill_no", billNo);
            update(orderPaymentBillDetail, updateWrapper);

            //保存操作记录in
            AuditInfoForm auditInfoForm = new AuditInfoForm();
            auditInfoForm.setExtUniqueFlag(billNo);
            auditInfoForm.setAuditTypeDesc("提交财务");
            auditInfoForm.setAuditStatus(BillEnum.B_3.getCode());
            auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
            auditInfoForm.setAuditUser(form.getLoginUserName());
            omsClient.saveAuditInfo(auditInfoForm);
        }
        return CommonResult.success();
    }


    @Override
    public Boolean applyPayment(ApplyPaymentForm form) {
        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        orderPaymentBillDetail.setPaymentAmount(form.getPaymentAmount());
        orderPaymentBillDetail.setApplyStatus(BillEnum.F_1.getCode());
        orderPaymentBillDetail.setAuditStatus(BillEnum.B_5.getCode());
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());

        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtUniqueFlag(form.getBillNo());
        auditInfoForm.setAuditTypeDesc("付款申请确认");
        auditInfoForm.setAuditStatus(BillEnum.B_5.getCode());
        auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
        auditInfoForm.setAuditUser(UserOperator.getToken());
        omsClient.saveAuditInfo(auditInfoForm);

        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no", form.getBillNo());
        return update(orderPaymentBillDetail, updateWrapper);
    }

    @Override
    public Boolean applyPaymentCancel(String billNo) {
        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        orderPaymentBillDetail.setApplyStatus(BillEnum.F_4.getCode());
        orderPaymentBillDetail.setAuditStatus(BillEnum.B_5_1.getCode());
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", billNo);
        return update(orderPaymentBillDetail, queryWrapper);
    }

    @Override
    public IPage<PaymentNotPaidBillVO> findEditBillByPage(QueryEditBillForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("opc.id"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findEditBillByPage(page, form);
        List<PaymentNotPaidBillVO> pageList = pageInfo.getRecords();
        for (PaymentNotPaidBillVO paymentNotPaidBillVO : pageList) {
            //处理目的地:当有两条或两条以上时,则获取中转仓地址
            if (!StringUtil.isNullOrEmpty(paymentNotPaidBillVO.getEndAddress())) {
                String[] strs = paymentNotPaidBillVO.getEndAddress().split(",");
                if (strs.length > 1) {
                    paymentNotPaidBillVO.setEndAddress(receivableBillService.getWarehouseAddress(paymentNotPaidBillVO.getOrderNo()));
                }
            }
        }

        if (CollectionUtils.isEmpty(pageList)) {
            return pageInfo;
        }

        //币种
        Map<String, String> currencyMap = this.omsClient.initCurrencyInfo().getData().stream().collect(Collectors.toMap(InitComboxStrVO::getCode, InitComboxStrVO::getName));
        pageInfo.getRecords().forEach(e -> {
            e.assembleAmountStr(currencyMap.get(e.getCurrencyCode()));
        });
        return pageInfo;
    }

    @Override
    public CommonResult editBill(EditBillForm form) {
        List<OrderPaymentBillDetailForm> paymentBillDetailForms = form.getPaymentBillDetailForms();//账单详细信息
        //该账单编号下必须有账单信息才可编辑
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", form.getBillNo());
        List<OrderPaymentBillDetail> paymentBillDetailList = baseMapper.selectList(queryWrapper);
        if (paymentBillDetailList.size() == 0) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        //取原账单下得信息
        OrderPaymentBillDetail existObject = paymentBillDetailList.get(0);
        //结算币种
        String settlementCurrency = form.getSettlementCurrency();
        //客服保存/提交
        if ("save".equals(form.getCmd()) || "submit".equals(form.getCmd())) {
            //可编辑的条件：客服主管审核对账单不通过,客服主管反审核对账单，财务审核对账单不通过，财务反审核
            if (!(BillEnum.B_2_1.getCode().equals(existObject.getAuditStatus()) || BillEnum.B_7.getCode().equals(existObject.getAuditStatus()) ||
                    BillEnum.B_8.getCode().equals(existObject.getAuditStatus()) || BillEnum.B_4_1.getCode().equals(existObject.getAuditStatus())
                    || "edit_del".equals(existObject.getAuditStatus())//流程过度状态-编辑删除
                    || "edit_no_commit".equals(existObject.getAuditStatus())//流程过度状态-编辑提交
            )) {
                return CommonResult.error(10001, "不符合操作条件");
            }
            List<OrderBillCostTotalVO> orderBillCostTotalVOS = new ArrayList<>();
            //校验本次提交的数据是否配置汇率
            if ("submit".equals(form.getCmd()) && paymentBillDetailForms.size() > 0) {
                List<Long> costIds = new ArrayList<>();
                for (OrderPaymentBillDetailForm tempObject : paymentBillDetailForms) {
                    costIds.add(tempObject.getCostId());
                }
//                StringBuilder sb = new StringBuilder();
//                sb.append("请配置[");
//                Boolean flag = true;
//                orderBillCostTotalVOS = costTotalService.findOrderFBillCostTotal(costIds, settlementCurrency, form.getAccountTermStr());
//                AtomicBoolean isCheck = new AtomicBoolean(true);
//                //是否自定义汇率
//                if (form.getIsCustomExchangeRate()) {
//                    //组装数据
//                    Map<String, BigDecimal> customExchangeRate = new HashMap<>();
//                    form.getCustomExchangeRate().forEach(e -> customExchangeRate.put(e.getCode(), e.getNote() == null ? new BigDecimal(0) : new BigDecimal(e.getNote())));
//                    orderBillCostTotalVOS.forEach(e -> {
//                        BigDecimal rate = customExchangeRate.get(e.getCurrencyCode());
//                        e.setExchangeRate(rate);
//                        //结算币种是CNY
//                        if ("CNY".equals(settlementCurrency)) {
//                            isCheck.set(false);
//                            e.setLocalMoneyRate(rate);
//                            e.setLocalMoney(e.getMoney().multiply(rate));
//                        }
//                    });
//                }
//                Set<String> msg = new HashSet<>();
//                for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
//                    BigDecimal exchangeRate = orderBillCostTotalVO.getExchangeRate();//如果费率为0，则抛异常回滚数据
//                    if ((exchangeRate == null || exchangeRate.compareTo(new BigDecimal(0)) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals(settlementCurrency)) {
//                        //根据币种查询币种描述
//                        String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
//                        String dCurrency = currencyRateService.getNameByCode(existObject.getSettlementCurrency());
////                        sb.append("原始币种:" + oCurrency + ",兑换币种:" + dCurrency + ";");
//                        msg.add("原始币种:" + oCurrency + ",兑换币种:" + dCurrency + ";");
//                        flag = false;
//                    }
//                    if (orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
//                        orderBillCostTotalVO.setLocalMoney(orderBillCostTotalVO.getOldLocalMoney());
//                    }
//                }
//                if (isCheck.get()) {
//                    List<OrderBillCostTotalVO> tempOrderBillCostTotalVOS = costTotalService.findOrderFBillCostTotal(costIds, "CNY", form.getAccountTermStr());
//                    for (OrderBillCostTotalVO orderBillCostTotalVO : tempOrderBillCostTotalVOS) {
//                        BigDecimal localMoney = orderBillCostTotalVO.getLocalMoney();//如果本币金额为0，说明汇率为空没配置
//                        if ((localMoney == null || localMoney.compareTo(new BigDecimal("0")) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
//                            //根据币种查询币种描述
//                            String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
////                            sb.append("原始币种:" + oCurrency + ",兑换币种:人民币;");
//                            msg.add("原始币种:" + oCurrency + ",兑换币种:人民币;");
//                            flag = false;
//                        }
//                    }
//                }
//                if (!flag) {
//                    msg.forEach(sb::append);
//                    sb.append("]的汇率");
//                    return CommonResult.error(10001, sb.toString());
//                }
                orderBillCostTotalVOS = this.orderPaymentBillService.configureExchangeRate(costIds, settlementCurrency, form.getAccountTermStr(),
                        form.getIsCustomExchangeRate(), form.getCustomExchangeRate());
            }

            //处理要新增的费用
            if (paymentBillDetailForms.size() > 0) {
                Boolean result = true;//结果标识
//                OrderPaymentBill orderPaymentBill = orderPaymentBillService.getById(existObject.getBillId());//获取账单信息
                //生成账单需要修改order_payment_cost表的is_bill
                List<Long> costIds = new ArrayList<>();
                List<String> orderNos = new ArrayList<>(); //为了统计已出账订单数
                for (OrderPaymentBillDetailForm paymentBillDetail : paymentBillDetailForms) {
                    costIds.add(paymentBillDetail.getCostId());
                    orderNos.add(paymentBillDetail.getOrderNo());
                }
                if (costIds.size() > 0) {
                    OprCostBillForm oprCostBillForm = new OprCostBillForm();
                    oprCostBillForm.setCmd("create");//这里的保存相当于生成对账单
                    oprCostBillForm.setCostIds(costIds);
                    oprCostBillForm.setOprType("payment");
                    result = omsClient.oprCostBill(oprCostBillForm).getData();
                    if (!result) {
                        return CommonResult.error(ResultEnum.OPR_FAIL);
                    }
                }
                //生成对账单数据
                //对现有账单信息进行修改
//                String subType = orderPaymentBill.getSubType();
//                //1.统计已出账金额alreadyPaidAmount
//                BigDecimal nowBillAmount = paymentBillDetailForms.stream().map(OrderPaymentBillDetailForm::getLocalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//                BigDecimal alreadyPaidAmount = orderPaymentBillService.getAlreadyPaidAmount(orderPaymentBill.getLegalName(), orderPaymentBill.getSupplierChName(), subType);
//                orderPaymentBill.setAlreadyPaidAmount(alreadyPaidAmount.add(nowBillAmount));
//                //2.统计已出账订单数billOrderNum
//                List<String> validOrders = new ArrayList<>();
//                orderNos = orderNos.stream().distinct().collect(Collectors.toList());
//                for (String orderNo : orderNos) {
//                    QueryWrapper queryWrapper1 = new QueryWrapper();
//                    queryWrapper1.eq("order_no", orderNo);
//                    List<OrderPaymentBillDetail> orderNoOjects = list(queryWrapper1);
//                    if (orderNoOjects == null || orderNoOjects.size() == 0) {
//                        validOrders.add(orderNo);
//                    }
//                }
//                Integer nowBillOrderNum = validOrders.size();
//                Integer billOrderNum = orderPaymentBillService.getBillOrderNum(orderPaymentBill.getLegalName(), orderPaymentBill.getSupplierChName(), subType);
//                orderPaymentBill.setBillOrderNum(billOrderNum + nowBillOrderNum);
//                //3.统计账单数billNum
//                orderPaymentBill.setBillNum(orderPaymentBill.getBillNum());
//                orderPaymentBill.setUpdatedTime(LocalDateTime.now());
//                orderPaymentBill.setUpdatedUser(form.getLoginUserName());
//                result = orderPaymentBillService.updateById(orderPaymentBill);
//                if (!result) {
//                    return CommonResult.error(ResultEnum.OPR_FAIL);
//                }

                //开始保存对账单详情数据
                //获取剩余旧数据的状态和结算期和结算币种,账单编号维度
                List<OrderPaymentBillDetail> paymentBillDetails = ConvertUtil.convertList(paymentBillDetailForms, OrderPaymentBillDetail.class);
                //录用费用明细
                Map<Long, OrderBillCostTotalVO> map = orderBillCostTotalVOS.stream().collect(Collectors.toMap(e -> e.getCostId(), e -> e));
                for (int i = 0; i < paymentBillDetails.size(); i++) {
                    OrderPaymentBillDetail paymentBillDetail = paymentBillDetails.get(i);
                    paymentBillDetail.setId(paymentBillDetailForms.get(i).getBillDetailId());
                    if (paymentBillDetail.getId() == null) {
                        paymentBillDetail.setMakeUser(form.getLoginUserName());
                        paymentBillDetail.setMakeTime(LocalDateTime.now());
                        paymentBillDetail.setCreatedUser(form.getLoginUserName());
                        paymentBillDetail.setAuditStatus("edit_no_commit");
                    }
                    paymentBillDetail.setStatus("1");
                    paymentBillDetail.setBillNo(form.getBillNo());
                    paymentBillDetail.setBillId(existObject.getBillId());
                    paymentBillDetail.setAccountTerm(form.getAccountTermStr());
                    paymentBillDetail.setSettlementCurrency(settlementCurrency);
                    paymentBillDetail.setCreatedOrderTime(DateUtils.convert2Date(paymentBillDetailForms.get(i).getCreatedTimeStr(), DateUtils.DATE_PATTERN));
                    OrderBillCostTotalVO tmp = map.get(paymentBillDetail.getCostId());
                    paymentBillDetail.setLocalAmount(tmp == null ? null : tmp.getLocalMoney());
                }
                //解决报错时重复添加数据问题
//                QueryWrapper queryWrapper1 = new QueryWrapper();
//                queryWrapper1.in("cost_id", costIds);
//                remove(queryWrapper1);

                result = this.saveOrUpdateBatch(paymentBillDetails);
                if (!result) {
                    return CommonResult.error(ResultEnum.OPR_FAIL);
                }
                //删除旧的费用
                QueryWrapper<OrderBillCostTotal> removeWrapper = new QueryWrapper<>();
                removeWrapper.lambda().in(OrderBillCostTotal::getCostId, costIds)
                        .eq(OrderBillCostTotal::getBillNo, form.getBillNo());
                costTotalService.remove(removeWrapper);
                //开始保存费用维度的金额信息  以结算币种进行转换后保存
                List<OrderBillCostTotal> orderBillCostTotals = new ArrayList<>();
                //根据费用ID统计费用信息,将原始费用信息根据结算币种进行转换
                orderBillCostTotalVOS = orderBillCostTotalVOS.size() == 0 ? costTotalService.findOrderFBillCostTotal(costIds, settlementCurrency, existObject.getAccountTerm()) : orderBillCostTotalVOS;
                for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                    String currencyCode = orderBillCostTotalVO.getCurrencyCode();
                    orderBillCostTotalVO.setBillNo(form.getBillNo());
                    BigDecimal money = orderBillCostTotalVO.getMoney();//录入费用时的金额
                    BigDecimal exchangeRate = orderBillCostTotalVO.getExchangeRate();
                    if (exchangeRate == null || exchangeRate.compareTo(new BigDecimal("0")) == 0) {
                        exchangeRate = new BigDecimal("1");
                    }
                    money = money.multiply(exchangeRate);
                    orderBillCostTotalVO.setMoney(money);
                    OrderBillCostTotal orderBillCostTotal = ConvertUtil.convert(orderBillCostTotalVO, OrderBillCostTotal.class);
                    orderBillCostTotal.setLocalMoney(orderBillCostTotalVO.getLocalMoney());
                    orderBillCostTotal.setOrderNo(orderBillCostTotal.getOrderNo() == null ? orderBillCostTotalVO.getMainOrderNo() : orderBillCostTotal.getOrderNo());
                    orderBillCostTotal.setMoneyType("1");
                    if ("save".equals(form.getCmd())) {
                        exchangeRate = orderBillCostTotal.getExchangeRate().compareTo(new BigDecimal(0)) == 0 ? null : orderBillCostTotal.getExchangeRate();
                    }
                    orderBillCostTotal.setExchangeRate(exchangeRate);
                    orderBillCostTotal.setCurrentCurrencyCode(currencyCode);
                    orderBillCostTotal.setCurrencyCode(settlementCurrency);
                    orderBillCostTotal.setIsCustomExchangeRate(form.getIsCustomExchangeRate());
                    orderBillCostTotals.add(orderBillCostTotal);
                }
                result = costTotalService.saveBatch(orderBillCostTotals);
                if (!result) {
                    return CommonResult.error(ResultEnum.OPR_FAIL);
                }
            }
            //剔除费用
            this.deleteCost(form);
            //统计账单数据
            this.statisticsBill(form.getBillNo());
        }
        if ("submit".equals(form.getCmd())) {//提交
            Boolean result = editBillSubmit(form.getBillNo(), form.getLoginUserName());
            if (!result) {
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
        }
        if ("cw_save".equals(form.getCmd())) {//财务编辑
            List<OrderPaymentBillDetail> paymentBillDetails = new ArrayList<>();
            List<OrderCostForm> orderCostForms = new ArrayList<>();
            for (OrderPaymentBillDetailForm paymentBillDetailForm : paymentBillDetailForms) {
                OrderPaymentBillDetail paymentBillDetail = new OrderPaymentBillDetail();
                paymentBillDetail.setId(paymentBillDetailForm.getBillDetailId());
                paymentBillDetail.setTaxRate(paymentBillDetailForm.getTaxRate());//税率
                paymentBillDetail.setRemarks(paymentBillDetailForm.getRemarks());//备注
                paymentBillDetail.setUpdatedTime(LocalDateTime.now());
                paymentBillDetail.setUpdatedUser(form.getLoginUserName());
                paymentBillDetails.add(paymentBillDetail);

                OrderCostForm orderCostForm = new OrderCostForm();
                orderCostForm.setLoginUserName(form.getLoginUserName());
                orderCostForm.setCostGenreId(paymentBillDetailForm.getCostGenreId());
                orderCostForm.setCostId(paymentBillDetailForm.getCostId());
                orderCostForms.add(orderCostForm);
            }
            Boolean result = updateBatchById(paymentBillDetails);
            if (!result) {
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
            omsClient.oprCostGenreByCw(orderCostForms, "payment");
        }
        //重新编辑后清除核销内容
        QueryWrapper removeVerification = new QueryWrapper();
        removeVerification.eq("bill_no", existObject.getBillNo());
        verificationService.remove(removeVerification);
        return CommonResult.success();
    }

    @Override
    public Boolean editBillSubmit(String billNo, String loginUserName) {
        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtUniqueFlag(billNo);
        auditInfoForm.setAuditTypeDesc("编辑对账单提交到客服主管审核");
        auditInfoForm.setAuditStatus(BillEnum.B_1.getCode());
        auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
        auditInfoForm.setAuditUser(loginUserName);
        omsClient.saveAuditInfo(auditInfoForm);

        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        orderPaymentBillDetail.setAuditStatus(BillEnum.B_1.getCode());
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(loginUserName);
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no", billNo);
        return update(orderPaymentBillDetail, updateWrapper);
    }

    @Override
    public List<ViewFBilToOrderVO> viewBillDetail(String billNo) {
        List<ViewFBilToOrderVO> orderList = baseMapper.viewBillDetail(billNo);
        List<ViewFBilToOrderVO> newOrderList = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(billNo);
        for (ViewFBilToOrderVO viewBillToOrder : orderList) {
            //处理目的地:当有两条或两条以上时,则获取中转仓地址
            if (!StringUtil.isNullOrEmpty(viewBillToOrder.getEndAddress())) {
                String[] strs = viewBillToOrder.getEndAddress().split(",");
                if (strs.length > 1) {
                    viewBillToOrder.setEndAddress(receivableBillService.getWarehouseAddress(viewBillToOrder.getOrderNo()));
                }
            }
            for (ViewBillToCostClassVO viewBillToCostClass : findCostClass) {
                if ((StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo()) && StringUtil.isNullOrEmpty(viewBillToCostClass.getSubOrderNo())
                        && viewBillToOrder.getOrderNo().equals(viewBillToCostClass.getOrderNo()))
                        || ((!StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo())) && viewBillToOrder.getSubOrderNo().equals(viewBillToCostClass.getSubOrderNo()))) {
                    try {
                        String addProperties = "";
                        String addValue = "";
                        Map<String, Object> propertiesMap = new HashMap<String, Object>();
                        Class cls = viewBillToCostClass.getClass();
                        Field[] fields = cls.getDeclaredFields();
                        for (int i = 0; i < fields.length; i++) {
                            Field f = fields[i];
                            f.setAccessible(true);
                            if ("name".equals(f.getName())) {
                                addProperties = String.valueOf(f.get(viewBillToCostClass)).toLowerCase();//待新增得属性
                            }
                            if ("money".equals(f.getName())) {
                                addValue = String.valueOf(f.get(viewBillToCostClass));//待新增属性得值
                            }
                            propertiesMap.put(addProperties, addValue);
                        }
                        viewBillToOrder = (ViewFBilToOrderVO) ReflectUtil.getObject(viewBillToOrder, propertiesMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            newOrderList.add(viewBillToOrder);
        }
        return newOrderList;
    }

    @Override
    public JSONArray viewBillDetailInfo(String billNo, String cmd, String templateCmd) {
        List<ViewFBilToOrderVO> orderList = baseMapper.viewBillDetail(billNo);

        JSONArray array = new JSONArray(orderList);

        List<ViewFBilToOrderVO> newOrderList = new ArrayList<>();
        List<String> mainOrderNos = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(billNo);

        for (int i = 0; i < orderList.size(); i++) {
            ViewFBilToOrderVO viewFBilToOrderVO = orderList.get(i);
            JSONObject jsonObject = array.getJSONObject(i);
            this.tmsSpecialDataProcessing(cmd, viewFBilToOrderVO);

            for (ViewBillToCostClassVO viewBillToCostClass : findCostClass) {
                if ((StringUtil.isNullOrEmpty(viewFBilToOrderVO.getSubOrderNo()) && StringUtil.isNullOrEmpty(viewBillToCostClass.getSubOrderNo()) &&
                        viewBillToCostClass.getOrderNo().equals(viewFBilToOrderVO.getOrderNo()))
                        || ((!StringUtil.isNullOrEmpty(viewFBilToOrderVO.getSubOrderNo())) && viewFBilToOrderVO.getSubOrderNo().equals(viewBillToCostClass.getSubOrderNo()))) {
                    try {
                        String addProperties = "";
                        String addValue = "";
                        Map<String, Object> propertiesMap = new HashMap<String, Object>();
                        Class cls = viewBillToCostClass.getClass();
                        Field[] fields = cls.getDeclaredFields();
                        for (int j = 0; j < fields.length; j++) {
                            Field f = fields[j];
                            f.setAccessible(true);
                            if ("name".equals(f.getName())) {
                                addProperties = String.valueOf(f.get(viewBillToCostClass)).toLowerCase();//待新增得属性
                            }
                            if ("money".equals(f.getName())) {
                                addValue = String.valueOf(f.get(viewBillToCostClass));//待新增属性得值
                            }
                            propertiesMap.put(addProperties, addValue);
                        }
                        jsonObject.putAll(propertiesMap);
//                        viewBillToOrder = (ViewBilToOrderVO) ReflectUtil.getObject(viewBillToOrder, propertiesMap);
//                        viewBillToOrder.setDynamicMap(propertiesMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            newOrderList.add(viewFBilToOrderVO);
            mainOrderNos.add(viewFBilToOrderVO.getOrderNo());
//            list.add(viewBillToOrder);
        }
        //模板数据处理
//        array = this.inlandTPDataProcessing(form, array, mainOrderNos);
        array = this.commonService.templateDataProcessing(cmd, templateCmd, array, mainOrderNos, 1);
        return array;
    }

//    private JSONArray templateDataProcessing(String cmd, JSONArray array, List<String> mainOrderNos) {
//        Map<String, Object> data = new HashMap<>();
//
//        if (SubOrderSignEnum.KY.getSignOne().equals(cmd)) {
//            List<AirOrderTemplate> airOrderTemplate = this.commonService.getAirOrderTemplate(mainOrderNos);
//            data = airOrderTemplate.stream().collect(Collectors.toMap(AirOrderTemplate::getOrderNo, e -> e));
//        }
//        //TODO 中港地址截取6个字符
//
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < array.size(); i++) {
//            if (data.size() == 0) {
//                break;
//            }
//            JSONObject jsonObject = array.getJSONObject(i);
//            JSONObject object = new JSONObject(data.get(jsonObject.getStr("subOrderNo")));
//            object.put("customerName", jsonObject.getStr("supplierChName"));
//            object.putAll(jsonObject);
//            jsonArray.add(object);
//        }
//        return jsonArray.size() == 0 ? array : jsonArray;
//    }

    @Override
    public List<SheetHeadVO> findSheetHead(String billNo, Map<String, Object> callbackArg) {
        List<SheetHeadVO> allHeadList = new ArrayList<>();
        List<SheetHeadVO> fixHeadList = new ArrayList<>();
        try {
            ViewFBilToOrderHeadVO viewBilToOrderVO = new ViewFBilToOrderHeadVO();
            Class cls = viewBilToOrderVO.getClass();
            Field[] fields = cls.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                f.setAccessible(true);
                SheetHeadVO sheetHeadVO = new SheetHeadVO();
                sheetHeadVO.setName(f.getName());
                sheetHeadVO.setViewName(String.valueOf(f.get(viewBilToOrderVO)));
                fixHeadList.add(sheetHeadVO);
            }
            callbackArg.put("fixHeadIndex", fixHeadList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<SheetHeadVO> dynamicHeadList = baseMapper.findSheetHead(billNo);
        for (SheetHeadVO sheetHead : dynamicHeadList) {
            sheetHead.setName(sheetHead.getName().toLowerCase());
        }
        allHeadList.addAll(fixHeadList);
        allHeadList.addAll(dynamicHeadList);
        return allHeadList;
    }

    @Override
    public List<SheetHeadVO> findSSheetHeadInfo(String billNo, Map<String, Object> callbackArg, String cmd, String templateCmd) {
        List<SheetHeadVO> allHeadList = new ArrayList<>();
        List<SheetHeadVO> fixHeadList = new ArrayList<>();
        try {
            Class template = BillTemplateEnum.getTemplate(templateCmd);
            if (template != null) {
                List<Map<String, Object>> maps = Utilities.assembleEntityHead(template, false);
                fixHeadList = Utilities.obj2List(maps, SheetHeadVO.class);
            } else {//TODO 增强不影响原有系统,除非更替完成
                ViewFBilToOrderHeadVO viewBilToOrderVO = new ViewFBilToOrderHeadVO();
                Class cls = viewBilToOrderVO.getClass();
                Field[] fields = cls.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field f = fields[i];
                    f.setAccessible(true);
                    SheetHeadVO sheetHeadVO = new SheetHeadVO();
                    sheetHeadVO.setName(f.getName());
                    sheetHeadVO.setViewName(String.valueOf(f.get(viewBilToOrderVO)));
                    fixHeadList.add(sheetHeadVO);
                }
            }
            //固定头部数目
            callbackArg.put("fixHeadIndex", fixHeadList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<SheetHeadVO> dynamicHeadList = baseMapper.findSheetHead(billNo);
        for (SheetHeadVO sheetHead : dynamicHeadList) {
            sheetHead.setName(sheetHead.getName().toLowerCase());
        }
        allHeadList.addAll(fixHeadList);
        allHeadList.addAll(dynamicHeadList);
        return allHeadList;
    }


    @Override
    public ViewBillVO getViewBill(String billNo) {
        return baseMapper.getViewBill(billNo);
    }

    @Override
    public CommonResult billAudit(BillAuditForm form) {
        //审核条件
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", form.getBillNo());
        List<OrderPaymentBillDetail> existList = baseMapper.selectList(queryWrapper);
        OrderPaymentBillDetail existObject = existList.get(0);
        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        String auditStatus = "";
        if ("audit".equals(form.getCmd())) {
            if (!BillEnum.B_1.getCode().equals(existObject.getAuditStatus())) {
                return CommonResult.error(100001, "不符合审核条件");
            }
            if ("0".equals(form.getAuditStatus())) {
                auditStatus = BillEnum.B_2.getCode();
            } else if ("1".equals(form.getAuditStatus())) {
                auditStatus = BillEnum.B_2_1.getCode();
            }
            orderPaymentBillDetail.setAuditTime(LocalDateTime.now());
            orderPaymentBillDetail.setAuditUser(form.getLoginUserName());
            auditInfoForm.setAuditTypeDesc("应付对账单审核");
        } else if ("cw_audit".equals(form.getCmd())) {//财务对账单审核
            if (!BillEnum.B_3.getCode().equals(existObject.getAuditStatus())) {
                return CommonResult.error(100001, "不符合审核条件");
            }
            if ("0".equals(form.getAuditStatus())) {
                auditStatus = BillEnum.B_4.getCode();
            } else if ("1".equals(form.getAuditStatus())) {
                auditStatus = BillEnum.B_4_1.getCode();
            }
            auditInfoForm.setAuditTypeDesc("财务对账单审核");
        }
        orderPaymentBillDetail.setAuditStatus(auditStatus);
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no", form.getBillNo());
        Boolean result = update(orderPaymentBillDetail, updateWrapper);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        //记录审核信息
        auditInfoForm.setExtUniqueFlag(form.getBillNo());
        auditInfoForm.setAuditStatus(auditStatus);
        auditInfoForm.setAuditComment(form.getAuditComment());
        auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
        auditInfoForm.setAuditUser(form.getLoginUserName());
        omsClient.saveAuditInfo(auditInfoForm);
        return CommonResult.success();
    }

    /**
     * //1.客服主管-应收反审核 kf_s_reject
     * 2.客服主管-应付反审核 kf_f_reject
     * //3.财务-应收反审核 cw_s_reject
     * 4.财务-应付反审核 cw_f_reject
     * ①未申请开票或付款的或作废的才可进行反审核
     */
    @Override
    public CommonResult contraryAudit(ListForm form) {
        //反审核条件
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no", form.getBillNos());
        List<OrderPaymentBillDetail> existList = baseMapper.selectList(queryWrapper);
        if ("kf_f_reject".equals(form.getCmd())) {
            for (OrderPaymentBillDetail existObject : existList) {
                if (!BillEnum.B_2.getCode().equals(existObject.getAuditStatus())) {
                    return CommonResult.error(10001, "存在不符合操作条件的数据");
                }
            }
            for (String billNo : form.getBillNos()) {
                OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
                orderPaymentBillDetail.setAuditStatus(BillEnum.B_7.getCode());
                orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
                orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no", billNo);
                update(orderPaymentBillDetail, updateWrapper);

                //保存审核信息
                AuditInfoForm auditInfoForm = new AuditInfoForm();
                auditInfoForm.setExtUniqueFlag(billNo);
                auditInfoForm.setAuditTypeDesc("应付客服反审核");
                auditInfoForm.setAuditStatus(BillEnum.B_7.getCode());
                auditInfoForm.setAuditComment(form.getRemark());
                auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
                auditInfoForm.setAuditUser(form.getLoginUserName());
                omsClient.saveAuditInfo(auditInfoForm);
            }
        } else if ("cw_f_reject".equals(form.getCmd())) {
            for (OrderPaymentBillDetail existObject : existList) {
                if (!this.financeService.checkAntiAudite(existObject.getAuditStatus())) {
                    return CommonResult.error(10001, "存在不符合操作条件的数据");
                }
            }
            for (String billNo : form.getBillNos()) {
                OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
                orderPaymentBillDetail.setAuditStatus(BillEnum.B_8.getCode());
                orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
                orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no", billNo);
                update(orderPaymentBillDetail, updateWrapper);

                //保存审核信息
                AuditInfoForm auditInfoForm = new AuditInfoForm();
                auditInfoForm.setExtUniqueFlag(billNo);
                auditInfoForm.setAuditTypeDesc("应付财务反审核");
                auditInfoForm.setAuditStatus(BillEnum.B_8.getCode());
                auditInfoForm.setAuditComment(form.getRemark());
                auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
                auditInfoForm.setAuditUser(form.getLoginUserName());
                omsClient.saveAuditInfo(auditInfoForm);
            }
        }
        return CommonResult.success();
    }

    @Override
    public IPage<FinanceAccountVO> findFinanceAccountByPage(QueryFinanceAccountForm form) {
        //定义分页参数
        Page<FinanceAccountVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("temp.createTimeStr"));
        IPage<FinanceAccountVO> pageInfo = baseMapper.findFinanceAccountByPage(page, form);
        this.dataProcessingService.processingFinanceAccount(pageInfo.getRecords(), null);
        return pageInfo;
    }

    @Override
    public List<FinanceAccountVO> findFinanceAccount(QueryFinanceAccountForm form) {
        return baseMapper.findFinanceAccountByPage(form);
    }


    @Override
    public IPage<PaymentNotPaidBillVO> findFBillAuditByPage(QueryFBillAuditForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("temp.createTimeStr"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findFBillAuditByPage(page, form);
        if (pageInfo.getRecords() == null) {
            return pageInfo;
        }
        //获取所有币种
        Map<String, String> currencyMap = this.omsClient.initCurrencyInfo().getData()
                .stream().collect(Collectors.toMap(InitComboxStrVO::getCode, InitComboxStrVO::getName));
        //获取所有费用id
        List<Long> costIds = pageInfo.getRecords().stream().map(PaymentNotPaidBillVO::getCostId).collect(toList());
        //查询所有费用本币金额
        Object costInfo = this.omsClient.getCostInfo(costIds, 1).getData();


        //所有的费用类型
        List<InitComboxVO> initComboxVOS = omsClient.findEnableCostGenre().getData();
        List<PaymentNotPaidBillVO> pageList = pageInfo.getRecords();
        for (PaymentNotPaidBillVO paymentNotPaidBill : pageList) {
            List<InitComboxVO> haveCostGenre = new ArrayList<>();
            if (!StringUtil.isNullOrEmpty(paymentNotPaidBill.getCostGenreStr())) {
                String[] ids = paymentNotPaidBill.getCostGenreStr().split(",");//费用类型逗号分隔形式的
                for (String id : ids) {
                    for (InitComboxVO initComboxVO : initComboxVOS) {
                        if (initComboxVO.getId() == Long.parseLong(id)) {
                            haveCostGenre.add(initComboxVO);
                        }
                    }
                }
                paymentNotPaidBill.setCostGenreList(haveCostGenre);
            }
            //处理目的地:当有两条或两条以上时,则获取中转仓地址
            if (!StringUtil.isNullOrEmpty(paymentNotPaidBill.getEndAddress())) {
                String[] strs = paymentNotPaidBill.getEndAddress().split(",");
                if (strs.length > 1) {
                    paymentNotPaidBill.setEndAddress(receivableBillService.getWarehouseAddress(paymentNotPaidBill.getOrderNo()));
                }
            }

            paymentNotPaidBill.assemblyCostInfo(costInfo, currencyMap);
        }


        return pageInfo;
    }

    @Override
    public List<PaymentNotPaidBillVO> findFBillAudit(QueryFBillAuditForm form) {
        return baseMapper.findFBillAuditByPage(form);
    }

    @Override
    public List<FCostVO> findFCostList(String billNo) {
        return baseMapper.findFCostList(billNo);
    }

    @Override
    public CommonResult auditFInvoice(BillAuditForm form) {
        //是否可以操作付款审核
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", form.getBillNo());
        List<OrderPaymentBillDetail> billDetails = baseMapper.selectList(queryWrapper);

        OrderPaymentBillDetail paymentBillDetail = billDetails.get(0);
        if (!BillEnum.B_5.getCode().equals(paymentBillDetail.getAuditStatus())) {
            return CommonResult.error(10001, "不符合操作条件");
        }
        OrderPaymentBillDetail billDetail = new OrderPaymentBillDetail();
        String applyStatus = "";
        String status = "";
        if ("0".equals(form.getAuditStatus())) {
            applyStatus = BillEnum.F_2.getCode();
            status = BillEnum.B_6.getCode();

            List<OrderBillCostTotal> orderBillCostTotals = this.costTotalService.getByBillNo(Arrays.asList(form.getBillNo()),
                    OrderBillCostTotalTypeEnum.PAYMENT.getCode());
            Map<Long, OrderBillCostTotal> costTotalMap = orderBillCostTotals.stream().collect(Collectors.toMap(e -> e.getCostId(), e -> e));

            //开票审核通过之后，需要反推汇率和本币金额到费用录入表
            List<OrderCostForm> orderCostForms = new ArrayList<>();
            for (OrderPaymentBillDetail tempObject : billDetails) {
                OrderCostForm orderCostForm = new OrderCostForm();
                orderCostForm.setCostId(tempObject.getCostId());
                orderCostForm.setLoginUserName(form.getLoginUserName());
                OrderBillCostTotal orderBillCostTotal = costTotalMap.get(tempObject.getCostId());
                orderCostForm.setLocalMoney(orderBillCostTotal.getLocalMoney());
                orderCostForm.setLocalMoneyRate(orderBillCostTotal.getLocalMoneyRate());
                orderCostForms.add(orderCostForm);
            }
            ApiResult result = omsClient.writeBackCostData(orderCostForms, "payment");
            if (result.getCode() != 200) {
                return CommonResult.error(result.getCode(), result.getMsg());
            }
        } else {
            applyStatus = BillEnum.F_3.getCode();
            status = BillEnum.B_6_1.getCode();
        }
        billDetail.setApplyStatus(applyStatus);
        billDetail.setAuditStatus(status);
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no", form.getBillNo());
        Integer num = baseMapper.update(billDetail, updateWrapper);
        if (num > 0) {
            //保存审核信息
            AuditInfoForm auditInfoForm = new AuditInfoForm();
            auditInfoForm.setExtUniqueFlag(form.getBillNo());
            auditInfoForm.setAuditTypeDesc("付款审核");
            auditInfoForm.setAuditStatus(applyStatus);
            auditInfoForm.setAuditComment(form.getAuditComment());
            auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
            auditInfoForm.setAuditUser(UserOperator.getToken());
            omsClient.saveAuditInfo(auditInfoForm);
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @Override
    public Boolean editFSaveConfirm(List<Long> costIds, EditBillForm form) {
        Map<String, Object> param = new HashMap<>();
        param.put(BeanUtils.convertToFieldName(EditBillForm::getBillNo), form.getBillNo());
        //在录入费用表的is_bill做标识为save_confirm
        return omsClient.editSaveConfirm(costIds, "payment", "save_confirm", param).getData();
    }

    @Override
    public Boolean editFDel(List<Long> costIds) {
        //从删除的costIds里面挑出那种保存确定的数据,回滚到未出账状态
        List<Long> saveConfirmIds = orderPaymentBillService.findSaveConfirmData(costIds);
        Boolean result = true;
        if (saveConfirmIds != null && saveConfirmIds.size() > 0) {
            result = omsClient.editSaveConfirm(saveConfirmIds, "payment", "edit_del", new HashMap<>()).getData();
            if (!result) {
                return false;
            }
        }
        //已存在的数据删除,只在账单详情表做记录
        List<Long> editDelIds = costIds.stream().filter(item -> !saveConfirmIds.contains(item)).collect(toList());
        if (editDelIds != null && editDelIds.size() > 0) {
            OrderPaymentBillDetail paymentBillDetail = new OrderPaymentBillDetail();
            paymentBillDetail.setAuditStatus("edit_del");//持续操作中的过度状态
            QueryWrapper updateWrapper = new QueryWrapper();
            updateWrapper.in("cost_id", editDelIds);
            result = update(paymentBillDetail, updateWrapper);
            if (!result) {
                return false;
            }
            //修改录用费用状态
//            this.omsClient.batchUpdateCostStatus(editDelIds, "0", 1, 1);
        }
        return true;
    }

    @Override
    public List<PayableHeaderForm> getPayableHeaderForm(String billNo) {
        return baseMapper.getPayableHeaderForm(billNo);
    }

    @Override
    public List<APARDetailForm> findPayableHeaderDetail(String billNo, String orderNo) {
        List<APARDetailForm> detailForms = baseMapper.findPayableHeaderDetail(billNo, orderNo);
        for (APARDetailForm detailForm : detailForms) {
            String expenseCategoryName = detailForm.getExpenseCategoryName();
            if (expenseCategoryName.contains("-")) {
                expenseCategoryName = expenseCategoryName.substring(0, expenseCategoryName.indexOf("-"));
                detailForm.setExpenseCategoryName(expenseCategoryName);
            }
        }
        return detailForms;
    }

    @Override
    public CostAmountVO getFCostAmountView(String billNo) {
        return baseMapper.getFCostAmountView(billNo);
    }

    @Override
    public List<OrderPaymentBillDetail> getNowFOrderExist(String legalName, String supplierChName, String subType, String orderNo) {
        return baseMapper.getNowFOrderExist(legalName, supplierChName, subType, orderNo);
    }

    @Override
    public List<OrderPaymentBillDetail> getNowFOrderExistByLegalId(Long legalEntityId, String supplierCode, String subType, String orderNo) {
        return baseMapper.getNowFOrderExistByLegalId(legalEntityId, supplierCode, subType, orderNo);
    }

    /**
     * 剔除费用
     */
    private void deleteCost(EditBillForm form) {
        QueryWrapper queryWrapper = new QueryWrapper();
        //处理需要删除的费用,获取删除标识的账单详情 TODO 直接获取前端传入删除对象集合
        queryWrapper.eq("audit_status", "edit_del");
        List<OrderPaymentBillDetail> delCosts = baseMapper.selectList(queryWrapper);
        List<Long> delCostIds = new ArrayList<>();
        for (OrderPaymentBillDetail billDetail : delCosts) {
            delCostIds.add(billDetail.getCostId());
        }
        if (delCostIds.size() > 0) {
            QueryWrapper removeWrapper = new QueryWrapper();
            removeWrapper.eq("bill_no", form.getBillNo());
            removeWrapper.in("cost_id", delCostIds);
            remove(removeWrapper);

            //相应的费用录入也需要做记录
            OprCostBillForm oprCostBillForm = new OprCostBillForm();
            oprCostBillForm.setCmd("del");
            oprCostBillForm.setOprType("payment");
            oprCostBillForm.setCostIds(delCostIds);
            omsClient.oprCostBill(oprCostBillForm);

            //相应的费用出账金额记录要做删除
//            QueryWrapper<OrderBillCostTotal> removeDelWrapper = new QueryWrapper<>();
//            removeDelWrapper.lambda().in(OrderBillCostTotal::getCostId, delCostIds)
//                    .eq(OrderBillCostTotal::getBillNo, form.getBillNo());
            costTotalService.remove(removeWrapper);

            //4.修改录用费用状态
            this.omsClient.batchUpdateCostStatus(delCostIds, "0", 1, 1);
        }
    }

    /**
     * 获取编辑账单数
     *
     * @param billNo
     */
    @Override
    public int getEditBillNum(String billNo) {
        QueryWrapper<OrderPaymentBillDetail> condition = new QueryWrapper<>();
        condition.lambda().notIn(OrderPaymentBillDetail::getAuditStatus,
                BillEnum.EDIT_DEL.getCode(), BillEnum.EDIT_NO_COMMIT.getCode())
                .eq(OrderPaymentBillDetail::getBillNo, billNo);
        return this.count(condition);
    }

    @Override
    public List<OrderPaymentBillDetail> getByCondition(OrderPaymentBillDetail orderPaymentBillDetail) {
        QueryWrapper<OrderPaymentBillDetail> condition = new QueryWrapper<>(orderPaymentBillDetail);
        return this.baseMapper.selectList(condition);
    }


    /**
     * 统计账单数据
     *
     * @param billNo
     */
    @Override
    public void statisticsBill(String billNo) {
        List<OrderPaymentBillDetail> receivableBillDetails = this.getByCondition(new OrderPaymentBillDetail().setBillNo(billNo));
        //查询账单主键
        OrderPaymentBillDetail billDetail = receivableBillDetails.get(0);
        List<OrderPaymentBillDetail> list = this.getByCondition(new OrderPaymentBillDetail().setBillId(billDetail.getBillId()));
        BigDecimal alreadyPaidAmount = new BigDecimal(0);
        Set<String> orderNos = new HashSet<>();
        Set<String> billNos = new HashSet<>();
        for (OrderPaymentBillDetail orderPaymentBillDetail : list) {
            //统计已出账金额
            if (orderPaymentBillDetail.getLocalAmount() != null) {
                alreadyPaidAmount = alreadyPaidAmount.add(orderPaymentBillDetail.getLocalAmount());
            }
            //统计已出账订单数
            orderNos.add(orderPaymentBillDetail.getOrderNo());
            //统计账单数
            billNos.add(orderPaymentBillDetail.getBillNo());
        }
        OrderPaymentBill paymentBill = new OrderPaymentBill()
                .setId(billDetail.getBillId()).setAlreadyPaidAmount(alreadyPaidAmount)
                .setBillOrderNum(orderNos.size())
                .setBillNum(billNos.size());

        this.orderPaymentBillService.updateById(paymentBill);
    }


    private void tmsSpecialDataProcessing(String cmd, ViewFBilToOrderVO viewFBilToOrderVO) {
        //中港运输 处理目的地:当有两条或两条以上时,则获取中转仓地址
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(cmd)) {
            if (!StringUtil.isNullOrEmpty(viewFBilToOrderVO.getEndAddress())) {
                String[] strs = viewFBilToOrderVO.getEndAddress().split(",");
                if (strs.length > 1) {
                    viewFBilToOrderVO.setEndAddress(receivableBillService.getWarehouseAddress(viewFBilToOrderVO.getOrderNo()));
                }
            }
        }
    }
}
