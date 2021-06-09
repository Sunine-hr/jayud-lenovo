package com.jayud.finance.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.*;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.enums.BillTemplateEnum;
import com.jayud.finance.enums.OrderBillCostTotalTypeEnum;
import com.jayud.finance.feign.OauthClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderReceivableBillDetailMapper;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.po.OrderReceivableBill;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.*;
import com.jayud.finance.util.ReflectUtil;
import com.jayud.finance.vo.*;
import com.jayud.finance.vo.template.order.AirOrderTemplate;
import io.netty.util.internal.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
public class OrderReceivableBillDetailServiceImpl extends ServiceImpl<OrderReceivableBillDetailMapper, OrderReceivableBillDetail> implements IOrderReceivableBillDetailService {

    @Autowired
    OmsClient omsClient;
    @Autowired
    IOrderReceivableBillService receivableBillService;
    @Autowired
    IOrderBillCostTotalService costTotalService;
    @Autowired
    ICurrencyRateService currencyRateService;
    @Autowired
    ICancelAfterVerificationService verificationService;
    @Autowired
    OauthClient oauthClient;
    @Autowired
    private CommonService commonService;
    @Autowired
    private IMakeInvoiceService makeInvoiceService;
    @Autowired
    private DataProcessingService dataProcessingService;
    @Autowired
    private FinanceService financeService;

    @Override
    public IPage<OrderPaymentBillDetailVO> findReceiveBillDetailByPage(QueryPaymentBillDetailForm form) {
        //定义分页参数
        Page<OrderPaymentBillDetailVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("orbd.make_time"));

        //获取当前用户法人主体名字
        ApiResult legalNameBySystemName = oauthClient.getLegalNameBySystemName(form.getLoginUserName());
        List<String> data = (List<String>) legalNameBySystemName.getData();

        IPage<OrderPaymentBillDetailVO> pageInfo = baseMapper.findReceiveBillDetailByPage(page, form, data);

        List<OrderPaymentBillDetailVO> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageInfo;
        }
        dataProcessingService.processingPaymentBillDetail(records, 0);
//        List<String> billNos = records.stream().map(OrderPaymentBillDetailVO::getBillNo).collect(toList());
//        //统计合计币种金额
//        List<Map<String, Object>> currencyAmounts = this.costTotalService.totalCurrencyAmount(billNos);
//        //审核意见
//        Map<String, Object> auditComment = this.omsClient.getByExtUniqueFlag(billNos).getData();
//        //币种
//        List<InitComboxStrVO> currencyInfo = omsClient.initCurrencyInfo().getData();
//        Map<String, String> currencyInfoMap = currencyInfo.stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));
//        //核算
//        this.makeInvoiceService.calculationAccounting(billNos,2);
//        for (OrderPaymentBillDetailVO record : records) {
//            record.totalCurrencyAmount(currencyAmounts);
//            record.setAuditComment(MapUtil.getStr(auditComment, record.getBillNo()));
//            record.setSettlementCurrency(currencyInfoMap.get(record.getSettlementCurrency()));
//        }
        return pageInfo;
    }

    @Override
    public List<OrderPaymentBillDetailVO> findReceiveBillDetail(QueryPaymentBillDetailForm form) {
        List<OrderPaymentBillDetailVO> list = baseMapper.findReceiveBillDetailByPage(form, null);
        List<String> billNos = list.stream().map(OrderPaymentBillDetailVO::getBillNo).collect(toList());
        //统计合计币种金额
        List<Map<String, Object>> currencyAmounts = this.costTotalService.totalCurrencyAmount(billNos);
        for (OrderPaymentBillDetailVO record : list) {
            record.totalCurrencyAmount(currencyAmounts);
        }
        return list;
    }

    @Override
    public CommonResult submitSCw(ListForm form) {
        //参数校验
        if (form.getBillNos() == null || form.getBillNos().size() == 0) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        //校验数据状态
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no", form.getBillNos());
        List<OrderReceivableBillDetail> billDetails = baseMapper.selectList(queryWrapper);
        for (OrderReceivableBillDetail billDetail : billDetails) {
            if (!BillEnum.B_2.getCode().equals(billDetail.getAuditStatus())) {
                return CommonResult.error(10001, "不符合操作条件");
            }
        }
        for (String billNo : form.getBillNos()) {
            OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
            orderReceivableBillDetail.setAuditStatus(BillEnum.B_3.getCode());
            orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
            orderReceivableBillDetail.setUpdatedUser(form.getLoginUserName());
            QueryWrapper updateWrapper = new QueryWrapper();
            updateWrapper.eq("bill_no", billNo);
            update(orderReceivableBillDetail, updateWrapper);

            //保存操作记录
            AuditInfoForm auditInfoForm = new AuditInfoForm();
            auditInfoForm.setExtUniqueFlag(billNo);
            auditInfoForm.setAuditTypeDesc("提交财务");
            auditInfoForm.setAuditStatus(BillEnum.B_3.getCode());
            auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
            auditInfoForm.setAuditUser(form.getLoginUserName());
            omsClient.saveAuditInfo(auditInfoForm);
        }
        return CommonResult.success();
    }

    @Override
    public Boolean applyInvoice(ApplyInvoiceForm form) {
        OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
        orderReceivableBillDetail.setInvoiceAmount(form.getInvoiceAmount());
        orderReceivableBillDetail.setApplyStatus(BillEnum.F_1.getCode());
        orderReceivableBillDetail.setAuditStatus(BillEnum.B_5.getCode());
        orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
        orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());

        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtUniqueFlag(form.getBillNo());
        auditInfoForm.setAuditTypeDesc("开票申请确认");
        auditInfoForm.setAuditStatus(BillEnum.B_5.getCode());
        auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
        auditInfoForm.setAuditUser(UserOperator.getToken());
        omsClient.saveAuditInfo(auditInfoForm);

        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no", form.getBillNo());
        return update(orderReceivableBillDetail, updateWrapper);
    }

    @Override
    public Boolean applyInvoiceCancel(String billNo) {
        OrderReceivableBillDetail orderReceiveBillDetail = new OrderReceivableBillDetail();
        orderReceiveBillDetail.setApplyStatus(BillEnum.F_4.getCode());
        orderReceiveBillDetail.setAuditStatus(BillEnum.B_5_1.getCode());
        orderReceiveBillDetail.setUpdatedTime(LocalDateTime.now());
        orderReceiveBillDetail.setUpdatedUser(UserOperator.getToken());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", billNo);
        return update(orderReceiveBillDetail, queryWrapper);
    }

    @Override
    public IPage<PaymentNotPaidBillVO> findEditSBillByPage(QueryEditBillForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("orc.id"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findEditSBillByPage(page, form);
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
        return pageInfo;
    }

    @Override
    @Transactional
    public CommonResult editSBill(EditSBillForm form) {
        List<OrderReceiveBillDetailForm> addReceiveBillDetailForms = form.getReceiveBillDetailForms();//账单详细信息
        //该账单编号下必须有账单信息才可编辑
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", form.getBillNo());
        List<OrderReceivableBillDetail> receivableBillDetails = baseMapper.selectList(queryWrapper);//获取账单详情信息
        if (receivableBillDetails.size() == 0) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        //取原账单下得信息
        OrderReceivableBillDetail existObject = receivableBillDetails.get(0);
        //结算币种
        String settlementCurrency = form.getSettlementCurrency();
        List<OrderBillCostTotalVO> orderBillCostTotalVOS = new ArrayList<>();
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
            //校验本次提交的数据是否配置汇率
            if ("submit".equals(form.getCmd()) && addReceiveBillDetailForms.size() > 0) {
                List<Long> costIds = new ArrayList<>();
                for (OrderReceiveBillDetailForm tempObject : addReceiveBillDetailForms) {
                    costIds.add(tempObject.getCostId());
                }
//                StringBuilder sb = new StringBuilder();
//                sb.append("请配置[");
//                Boolean flag = true;
//                orderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, settlementCurrency, form.getAccountTermStr());
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
//                        String dCurrency = currencyRateService.getNameByCode(settlementCurrency);
////                        sb.append("原始币种:" + oCurrency + ",兑换币种:" + dCurrency + ";");
//                        msg.add("原始币种:" + oCurrency + ",兑换币种:" + dCurrency + ";");
//                        flag = false;
//                    }
//                    if (orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
//                        orderBillCostTotalVO.setLocalMoney(orderBillCostTotalVO.getOldLocalMoney());
//                    }
//                }
//                if (isCheck.get()) {
//                    List<OrderBillCostTotalVO> tempOrderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, "CNY", form.getAccountTermStr());
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
                //配置汇率
                orderBillCostTotalVOS = this.receivableBillService.configureExchangeRate(costIds, settlementCurrency, form.getAccountTermStr(),
                        form.getIsCustomExchangeRate(), form.getCustomExchangeRate());
            }

            //处理要新增的费用
            if (addReceiveBillDetailForms.size() > 0) {
                Boolean result = true;//结果标识

                //生成账单需要修改order_receivable_cost表的is_bill
                List<Long> costIds = new ArrayList<>();
                List<String> orderNos = new ArrayList<>(); //为了统计已出账订单数
                for (OrderReceiveBillDetailForm receiveBillDetail : addReceiveBillDetailForms) {
                    costIds.add(receiveBillDetail.getCostId());
                    orderNos.add(receiveBillDetail.getOrderNo());
                }
                if (costIds.size() > 0) {
                    OprCostBillForm oprCostBillForm = new OprCostBillForm();
                    oprCostBillForm.setCmd("create");//这里的保存相当于生成对账单
                    oprCostBillForm.setCostIds(costIds);
                    oprCostBillForm.setOprType("receivable");
                    result = omsClient.oprCostBill(oprCostBillForm).getData();
                    if (!result) {
                        return CommonResult.error(ResultEnum.OPR_FAIL);
                    }
                }
//                //获取账单信息
//                OrderReceivableBill orderReceivableBill = receivableBillService.getById(existObject.getBillId());
//                //生成对账单数据
//                //对现有账单信息进行修改
//                String subType = orderReceivableBill.getSubType();
//                //1.统计已出账金额alreadyPaidAmount
//                BigDecimal nowBillAmount = receiveBillDetailForms.stream().map(OrderReceiveBillDetailForm::getLocalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//                BigDecimal alreadyPaidAmount = receivableBillService.getSAlreadyPaidAmount(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), subType);
//                orderReceivableBill.setAlreadyPaidAmount(alreadyPaidAmount.add(nowBillAmount));
//                //2.统计已出账订单数billOrderNum
//                List<String> validOrders = new ArrayList<>();
//                orderNos = orderNos.stream().distinct().collect(Collectors.toList());
//                for (String orderNo : orderNos) {
//                    List<OrderReceivableBillDetail> orderNoObjects = getNowSOrderExist(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), subType, orderNo);
//                    if (orderNoObjects == null || orderNoObjects.size() == 0) {
//                        validOrders.add(orderNo);
//                    }
//                }
//                Integer nowBillOrderNum = validOrders.size();
//                Integer billOrderNum = receivableBillService.getSBillOrderNum(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), subType);
//                orderReceivableBill.setBillOrderNum(billOrderNum + nowBillOrderNum);
//                //3.统计账单数billNum
//                Integer billNum = receivableBillService.getSBillNum(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), subType);
//                orderReceivableBill.setBillNum(billNum);
//                orderReceivableBill.setUpdatedTime(LocalDateTime.now());
//                orderReceivableBill.setUpdatedUser(form.getLoginUserName());
//                result = receivableBillService.updateById(orderReceivableBill);
//                if (!result) {
//                    return CommonResult.error(ResultEnum.OPR_FAIL);
//                }

                //开始保存对账单详情数据
                //获取剩余旧数据的状态和结算期和结算币种,账单编号维度
                List<OrderReceivableBillDetail> receiveBillDetails = ConvertUtil.convertList(addReceiveBillDetailForms, OrderReceivableBillDetail.class);
                //录用费用明细
                Map<Long, OrderBillCostTotalVO> map = orderBillCostTotalVOS.stream().collect(Collectors.toMap(e -> e.getCostId(), e -> e));
                for (int i = 0; i < receiveBillDetails.size(); i++) {
                    OrderReceivableBillDetail orderReceivableBillDetail = receiveBillDetails.get(i);
                    orderReceivableBillDetail.setId(addReceiveBillDetailForms.get(i).getBillDetailId());
                    if (orderReceivableBillDetail.getId() == null) {
                        orderReceivableBillDetail.setMakeUser(form.getLoginUserName());
                        orderReceivableBillDetail.setMakeTime(LocalDateTime.now());
                        orderReceivableBillDetail.setCreatedUser(form.getLoginUserName());
                        orderReceivableBillDetail.setAuditStatus("edit_no_commit");//编辑保存未提交的，给前台做区分
                    }

                    orderReceivableBillDetail.setStatus("1");
                    orderReceivableBillDetail.setBillNo(form.getBillNo());
                    orderReceivableBillDetail.setBillId(existObject.getBillId());
                    orderReceivableBillDetail.setAccountTerm(form.getAccountTermStr());
                    orderReceivableBillDetail.setSettlementCurrency(settlementCurrency);
                    orderReceivableBillDetail.setCreatedOrderTime(DateUtils.stringToDate(addReceiveBillDetailForms.get(i).getCreatedTimeStr(), DateUtils.DATE_PATTERN));
                    OrderBillCostTotalVO tmp = map.get(orderReceivableBillDetail.getCostId());
                    orderReceivableBillDetail.setLocalAmount(tmp == null ? null : tmp.getLocalMoney());
                }

                //解决报错时重复添加数据问题
//                QueryWrapper queryWrapper1 = new QueryWrapper();
//                queryWrapper1.in("cost_id", costIds);
//                remove(queryWrapper1);

                result = this.saveOrUpdateBatch(receiveBillDetails);

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

                orderBillCostTotalVOS = orderBillCostTotalVOS.size() == 0 ? costTotalService.findOrderSBillCostTotal(costIds, settlementCurrency, existObject.getAccountTerm()) : orderBillCostTotalVOS;
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
                    orderBillCostTotal.setMoneyType("2");
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
        if ("submit".equals(form.getCmd())) {//客服提交
            Boolean result = editSBillSubmit(form.getBillNo(), form.getLoginUserName());
            if (!result) {
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
        }
        if ("cw_save".equals(form.getCmd())) {//财务编辑
            List<OrderReceivableBillDetail> receiveBillDetails = new ArrayList<>();
            List<OrderCostForm> orderCostForms = new ArrayList<>();
            for (OrderReceiveBillDetailForm receiveBillDetailForm : addReceiveBillDetailForms) {
                OrderReceivableBillDetail receivableBillDetail = new OrderReceivableBillDetail();
                receivableBillDetail.setId(receiveBillDetailForm.getBillDetailId());
                receivableBillDetail.setTaxRate(receiveBillDetailForm.getTaxRate());//税率
                receivableBillDetail.setRemarks(receiveBillDetailForm.getRemarks());//备注
                receivableBillDetail.setUpdatedTime(LocalDateTime.now());
                receivableBillDetail.setUpdatedUser(form.getLoginUserName());
                receiveBillDetails.add(receivableBillDetail);

                OrderCostForm orderCostForm = new OrderCostForm();
                orderCostForm.setLoginUserName(form.getLoginUserName());
                orderCostForm.setCostGenreId(receiveBillDetailForm.getCostGenreId());
                orderCostForm.setCostId(receiveBillDetailForm.getCostId());
                orderCostForms.add(orderCostForm);
            }
            Boolean result = updateBatchById(receiveBillDetails);
            if (!result) {
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
            omsClient.oprCostGenreByCw(orderCostForms, "receivable");
        }
        //重新编辑后清除核销内容
        QueryWrapper removeVerification = new QueryWrapper();
        removeVerification.eq("bill_no", existObject.getBillNo());
        verificationService.remove(removeVerification);
        return CommonResult.success();
    }

    @Override
    public Boolean editSBillSubmit(String billNo, String loginUserName) {
        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtUniqueFlag(billNo);
        auditInfoForm.setAuditTypeDesc("编辑对账单提交到客服主管审核");
        auditInfoForm.setAuditStatus(BillEnum.B_1.getCode());
        auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
        auditInfoForm.setAuditUser(loginUserName);
        omsClient.saveAuditInfo(auditInfoForm);

        OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
        orderReceivableBillDetail.setAuditStatus(BillEnum.B_1.getCode());
        orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
        orderReceivableBillDetail.setUpdatedUser(loginUserName);
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no", billNo);
        return update(orderReceivableBillDetail, updateWrapper);
    }

    @Override
    public List<ViewBilToOrderVO> viewSBillDetail(String billNo) {
        List<ViewBilToOrderVO> orderList = baseMapper.viewSBillDetail(billNo);
        List<ViewBilToOrderVO> newOrderList = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findSCostClass(billNo);
        for (ViewBilToOrderVO viewBillToOrder : orderList) {
            //处理目的地:当有两条或两条以上时,则获取中转仓地址
            if (!StringUtil.isNullOrEmpty(viewBillToOrder.getEndAddress())) {
                String[] strs = viewBillToOrder.getEndAddress().split(",");
                if (strs.length > 1) {
                    viewBillToOrder.setEndAddress(receivableBillService.getWarehouseAddress(viewBillToOrder.getOrderNo()));
                }
            }
            for (ViewBillToCostClassVO viewBillToCostClass : findCostClass) {
                if ((StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo()) && StringUtil.isNullOrEmpty(viewBillToCostClass.getSubOrderNo()) &&
                        viewBillToCostClass.getOrderNo().equals(viewBillToOrder.getOrderNo()))
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
                        viewBillToOrder = (ViewBilToOrderVO) ReflectUtil.getObject(viewBillToOrder, propertiesMap);
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
    public JSONArray viewSBillDetailInfo(String billNo, String cmd, String templateCmd) {
        List<ViewBilToOrderVO> orderList = baseMapper.viewSBillDetail(billNo);

        JSONArray array = new JSONArray(orderList);

        List<ViewBilToOrderVO> newOrderList = new ArrayList<>();
        List<String> mainOrderNos = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findSCostClass(billNo);

        for (int i = 0; i < orderList.size(); i++) {
            ViewBilToOrderVO viewBillToOrder = orderList.get(i);
            JSONObject jsonObject = array.getJSONObject(i);
            this.tmsSpecialDataProcessing(cmd, viewBillToOrder);

            for (ViewBillToCostClassVO viewBillToCostClass : findCostClass) {
                if ((StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo()) && StringUtil.isNullOrEmpty(viewBillToCostClass.getSubOrderNo()) &&
                        viewBillToCostClass.getOrderNo().equals(viewBillToOrder.getOrderNo()))
                        || ((!StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo())) && viewBillToOrder.getSubOrderNo().equals(viewBillToCostClass.getSubOrderNo()))) {
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
            newOrderList.add(viewBillToOrder);
            mainOrderNos.add(viewBillToOrder.getOrderNo());
//            list.add(viewBillToOrder);
        }
        //模板数据处理
//        array = this.inlandTPDataProcessing(form, array, mainOrderNos);
        array = this.commonService.templateDataProcessing(cmd, templateCmd, array, mainOrderNos, 0);
        return array;
    }

//    private JSONArray templateDataProcessing(String cmd, JSONArray array, List<String> mainOrderNos) {
//        Map<String, Object> data = new HashMap<>();
//        if (SubOrderSignEnum.KY.getSignOne().equals(cmd)) {
//            List<AirOrderTemplate> airOrderTemplate = this.commonService.getAirOrderTemplate(mainOrderNos);
//            data = airOrderTemplate.stream().collect(Collectors.toMap(AirOrderTemplate::getOrderNo, e -> e));
//        }
//
//        //TODO 中港地址截取6个字符
//
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < array.size(); i++) {
//            if (data.size() == 0) {
//                break;
//            }
//            JSONObject jsonObject = array.getJSONObject(i);
//            JSONObject object = new JSONObject(data.get(jsonObject.getStr("subOrderNo")));
//            object.put("customerName", jsonObject.getStr("unitAccount"));
//            object.putAll(jsonObject);
//            jsonArray.add(object);
//        }
//        return jsonArray.size() == 0 ? array : jsonArray;
//    }


    @Override
    public List<SheetHeadVO> findSSheetHead(String billNo, Map<String, Object> callbackArg) {
        List<SheetHeadVO> allHeadList = new ArrayList<>();
        List<SheetHeadVO> fixHeadList = new ArrayList<>();
        try {
            ViewBilToOrderHeadVO viewBilToOrderVO = new ViewBilToOrderHeadVO();
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
            //固定头部数目
            callbackArg.put("fixHeadIndex", fixHeadList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<SheetHeadVO> dynamicHeadList = baseMapper.findSSheetHead(billNo);
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
                ViewBilToOrderHeadVO viewBilToOrderVO = new ViewBilToOrderHeadVO();
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
        List<SheetHeadVO> dynamicHeadList = baseMapper.findSSheetHead(billNo);
        for (SheetHeadVO sheetHead : dynamicHeadList) {
            sheetHead.setName(sheetHead.getName().toLowerCase());
        }
        allHeadList.addAll(fixHeadList);
        allHeadList.addAll(dynamicHeadList);
        return allHeadList;
    }

    @Override
    public ViewBillVO getViewSBill(String billNo) {
        return baseMapper.getViewSBill(billNo);
    }

    @Override
    public CommonResult billSAudit(BillAuditForm form) {
        //审核条件
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", form.getBillNo());
        List<OrderReceivableBillDetail> existList = baseMapper.selectList(queryWrapper);
        OrderReceivableBillDetail existObject = existList.get(0);
        OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
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
            orderReceivableBillDetail.setAuditTime(LocalDateTime.now());
            orderReceivableBillDetail.setAuditUser(form.getLoginUserName());
            auditInfoForm.setAuditTypeDesc("应收对账单审核");
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
        orderReceivableBillDetail.setAuditStatus(auditStatus);
        orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
        orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no", form.getBillNo());
        Boolean result = update(orderReceivableBillDetail, updateWrapper);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        //记录审核信息
        auditInfoForm.setExtUniqueFlag(form.getBillNo());
        auditInfoForm.setAuditStatus(auditStatus);
        auditInfoForm.setAuditComment(form.getAuditComment());
        auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
        auditInfoForm.setAuditUser(form.getLoginUserName());
        omsClient.saveAuditInfo(auditInfoForm);
        return CommonResult.success();
    }

    /**
     * 1.客服主管-应收反审核 kf_s_reject
     * 2.财务-应收反审核 cw_s_reject
     * ①未申请开票或付款的或作废的才可进行反审核
     */
    @Override
    public CommonResult contrarySAudit(ListForm form) {
        //反审核条件
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no", form.getBillNos());
        List<OrderReceivableBillDetail> existList = baseMapper.selectList(queryWrapper);
        if ("kf_s_reject".equals(form.getCmd())) {
            for (OrderReceivableBillDetail existObject : existList) {
                if (!BillEnum.B_2.getCode().equals(existObject.getAuditStatus())) {
                    return CommonResult.error(10001, "存在不符合操作条件的数据");
                }
            }
            for (String billNo : form.getBillNos()) {
                OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
                orderReceivableBillDetail.setAuditStatus(BillEnum.B_7.getCode());
                orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
                orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no", billNo);
                update(orderReceivableBillDetail, updateWrapper);

                //保存审核信息
                AuditInfoForm auditInfoForm = new AuditInfoForm();
                auditInfoForm.setExtUniqueFlag(billNo);
                auditInfoForm.setAuditTypeDesc("应收客服反审核");
                auditInfoForm.setAuditStatus(BillEnum.B_7.getCode());
                auditInfoForm.setAuditComment(form.getRemark());
                auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
                auditInfoForm.setAuditUser(form.getLoginUserName());
                omsClient.saveAuditInfo(auditInfoForm);
            }
        } else if ("cw_s_reject".equals(form.getCmd())) {
            for (OrderReceivableBillDetail existObject : existList) {
                if (!this.financeService.checkAntiAudite(existObject.getAuditStatus())) {
                    return CommonResult.error(10001, "存在不符合操作条件的数据");
                }
            }
            for (String billNo : form.getBillNos()) {
                OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
                orderReceivableBillDetail.setAuditStatus(BillEnum.B_8.getCode());
                orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
                orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no", billNo);
                update(orderReceivableBillDetail, updateWrapper);

                //保存审核信息
                AuditInfoForm auditInfoForm = new AuditInfoForm();
                auditInfoForm.setExtUniqueFlag(billNo);
                auditInfoForm.setAuditTypeDesc("应收财务反审核");
                auditInfoForm.setAuditStatus(BillEnum.B_8.getCode());
                auditInfoForm.setAuditComment(form.getRemark());
                auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
                auditInfoForm.setAuditUser(form.getLoginUserName());
                omsClient.saveAuditInfo(auditInfoForm);
            }
        }
        return CommonResult.success();
    }

//    @Override
//    public IPage<PaymentNotPaidBillVO> findSBillAuditByPage(QueryFBillAuditForm form) {
//        //定义分页参数
//        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(), form.getPageSize());
//        //定义排序规则
//        page.addOrder(OrderItem.desc("temp.createdTimeStr"));
//        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findSBillAuditByPage(page, form);
//        if (pageInfo.getSize() == 0) {
//            return pageInfo;
//        }
//
//        IPage<Map> pageMap = new Page<>();
//        pageMap.setCurrent(page.getCurrent());
//        pageMap.setPages(page.getPages());
//        pageMap.setSize(page.getSize());
//        pageMap.setTotal(page.getTotal());
//
//        //所有的费用类型
//        List<InitComboxVO> initComboxVOS = omsClient.findEnableCostGenre().getData();
//        List<PaymentNotPaidBillVO> pageList = pageInfo.getRecords();
//        List<String> mainOrderNos = new ArrayList<>();
//        for (PaymentNotPaidBillVO paymentNotPaidBill : pageList) {
//            List<InitComboxVO> haveCostGenre = new ArrayList<>();
//            if (!StringUtil.isNullOrEmpty(paymentNotPaidBill.getCostGenreStr())) {
//                String[] ids = paymentNotPaidBill.getCostGenreStr().split(",");//费用类型逗号分隔形式的
//                for (String id : ids) {
//                    for (InitComboxVO initComboxVO : initComboxVOS) {
//                        if (initComboxVO.getId() == Long.parseLong(id)) {
//                            haveCostGenre.add(initComboxVO);
//                        }
//                    }
//                }
//                paymentNotPaidBill.setCostGenreList(haveCostGenre);
//            }
//            //处理目的地:当有两条或两条以上时,则获取中转仓地址
//            if (!StringUtil.isNullOrEmpty(paymentNotPaidBill.getEndAddress())) {
//                String[] strs = paymentNotPaidBill.getEndAddress().split(",");
//                if (strs.length > 1) {
//                    paymentNotPaidBill.setEndAddress(receivableBillService.getWarehouseAddress(paymentNotPaidBill.getOrderNo()));
//                }
//            }
//            mainOrderNos.add(paymentNotPaidBill.getOrderNo());
//        }
//        JSONArray array = new JSONArray(pageList);
//        array = this.commonService.templateDataProcessing(pageList.get(0).getSubType(),array , mainOrderNos, 0);
//        List<Map> maps = Utilities.obj2List(array, Map.class);
//        pageMap.setRecords(maps);
//        return pageMap;
//    }

    @Override
    public IPage<LinkedHashMap> findSBillAuditByPage(QueryFBillAuditForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("temp.createdTimeStr"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findSBillAuditByPage(page, form);

        IPage<LinkedHashMap> pageMap = new Page<>();
        pageMap.setCurrent(page.getCurrent());
        pageMap.setPages(page.getPages());
        pageMap.setSize(page.getSize());
        pageMap.setTotal(page.getTotal());

        if (pageInfo.getSize() == 0) {
            return pageMap;
        }


        //获取所有币种
        Map<String, String> currencyMap = this.omsClient.initCurrencyInfo().getData()
                .stream().collect(Collectors.toMap(InitComboxStrVO::getCode, InitComboxStrVO::getName));
        //获取所有费用id
        List<Long> costIds = pageInfo.getRecords().stream().map(PaymentNotPaidBillVO::getCostId).collect(toList());
        //查询所有费用本币金额
        Object costInfo = this.omsClient.getCostInfo(costIds, 0).getData();

        //所有的费用类型
        List<InitComboxVO> initComboxVOS = omsClient.findEnableCostGenre().getData();
        List<PaymentNotPaidBillVO> pageList = pageInfo.getRecords();
        List<String> mainOrderNos = new ArrayList<>();
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
            mainOrderNos.add(paymentNotPaidBill.getOrderNo());
            paymentNotPaidBill.assemblyCostInfo(costInfo, currencyMap);
        }
        JSONArray array = new JSONArray(pageList);
        array = this.commonService.templateDataProcessing(pageList.get(0).getSubType(), pageList.get(0).getSubType(), array, mainOrderNos, 0);
        List<LinkedHashMap> maps = Utilities.obj2List(array, LinkedHashMap.class);
        pageMap.setRecords(maps);
        return pageMap;
    }

    @Override
    public List<PaymentNotPaidBillVO> findSBillAudit(QueryFBillAuditForm form) {
        return baseMapper.findSBillAuditByPage(form);
    }


    @Override
    public List<FCostVO> findSCostList(String billNo) {
        return baseMapper.findSCostList(billNo);
    }

    @Override
    public CommonResult auditSInvoice(BillAuditForm form) {
        //是否可以操作付款审核
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", form.getBillNo());
        List<OrderReceivableBillDetail> billDetails = baseMapper.selectList(queryWrapper);
        OrderReceivableBillDetail receivableBillDetail = billDetails.get(0);
        if (!BillEnum.B_5.getCode().equals(receivableBillDetail.getAuditStatus())) {
            return CommonResult.error(10001, "不符合操作条件");
        }
        OrderReceivableBillDetail billDetail = new OrderReceivableBillDetail();
        String applyStatus = "";
        String status = "";
        if ("0".equals(form.getAuditStatus())) {
            applyStatus = BillEnum.F_2.getCode();
            status = BillEnum.B_6.getCode();

            //开票审核通过之后，需要反推汇率和本币金额到费用录入表
            List<OrderBillCostTotal> orderBillCostTotals = this.costTotalService.getByBillNo(Arrays.asList(form.getBillNo()),
                    OrderBillCostTotalTypeEnum.RECEIVABLE.getCode());
            Map<Long, OrderBillCostTotal> costTotalMap = orderBillCostTotals.stream().collect(Collectors.toMap(e -> e.getCostId(), e -> e));
            List<OrderCostForm> orderCostForms = new ArrayList<>();
            for (OrderReceivableBillDetail tempObject : billDetails) {
                OrderCostForm orderCostForm = new OrderCostForm();
                orderCostForm.setCostId(tempObject.getCostId());
                orderCostForm.setLoginUserName(form.getLoginUserName());

                OrderBillCostTotal orderBillCostTotal = costTotalMap.get(tempObject.getCostId());
                orderCostForm.setLocalMoney(orderBillCostTotal.getLocalMoney());
                orderCostForm.setLocalMoneyRate(orderBillCostTotal.getLocalMoneyRate());
                orderCostForms.add(orderCostForm);
            }
            ApiResult result = omsClient.writeBackCostData(orderCostForms, "receivable");
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
            auditInfoForm.setAuditTypeDesc("收款审核");
            auditInfoForm.setAuditStatus(applyStatus);
            auditInfoForm.setAuditComment(form.getAuditComment());
            auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
            auditInfoForm.setAuditUser(UserOperator.getToken());
            omsClient.saveAuditInfo(auditInfoForm);
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @Override
    public Boolean editSSaveConfirm(List<Long> costIds, EditSBillForm form) {
        Map<String, Object> param = new HashMap<>();
        param.put(BeanUtils.convertToFieldName(EditSBillForm::getBillNo), form.getBillNo());
        //在录入费用表的is_bill做标识为save_confirm
        return omsClient.editSaveConfirm(costIds, "receivable", "save_confirm", param).getData();
    }

    @Override
    @Transactional
    public Boolean editSDel(List<Long> costIds) {
        //从删除的costIds里面挑出那种保存确定的数据,回滚到未出账状态
        List<Long> saveConfirmIds = receivableBillService.findSaveConfirmData(costIds);
        Boolean result = true;
        if (saveConfirmIds != null && saveConfirmIds.size() > 0) {
            result = this.omsClient.editSaveConfirm(saveConfirmIds, "receivable", "edit_del", new HashMap<>()).getData();
            if (!result) {
                return false;
            }
        }
        //已存在的数据删除,只在账单详情表做记录 TODO 不做标记,前端逻辑删除,暂存需要传删除对象
        List<Long> editDelIds = costIds.stream().filter(item -> !saveConfirmIds.contains(item)).collect(toList());
        if (editDelIds.size() > 0) {
            OrderReceivableBillDetail receivableBillDetail = new OrderReceivableBillDetail();
            receivableBillDetail.setAuditStatus("edit_del");//持续操作中的过度状态
            QueryWrapper updateWrapper = new QueryWrapper();
            updateWrapper.in("cost_id", editDelIds);
            result = update(receivableBillDetail, updateWrapper);
            if (!result) {
                return false;
            }
            //修改录用费用状态
//            this.omsClient.batchUpdateCostStatus(editDelIds, "0", 1, 0);
        }
        return true;
    }

    @Override
    public List<ReceivableHeaderForm> getReceivableHeaderForm(String billNo) {
        return baseMapper.getReceivableHeaderForm(billNo);
    }

    @Override
    public List<APARDetailForm> findReceivableHeaderDetail(String billNo, String orderNo) {
        List<APARDetailForm> detailForms = baseMapper.findReceivableHeaderDetail(billNo, orderNo);
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
    public CostAmountVO getSCostAmountView(String billNo) {
        return baseMapper.getSCostAmountView(billNo);
    }

    @Override
    public List<OrderReceivableBillDetail> getNowSOrderExist(String legalName, String unitAccount, String subType, String orderNo) {
        return baseMapper.getNowSOrderExist(legalName, unitAccount, subType, orderNo);
    }

    /**
     * TODO 根据法人id和结算code
     * 当前订单是否已经存在当前法人主体，结算单位，订单类型中,若存在则不做数量统计
     *
     * @return
     */
    @Override
    public List<OrderReceivableBillDetail> getNowSOrderExistByLegalId(Long legalId, String unitCode, String subType, String orderNo) {
        return baseMapper.getNowSOrderExistByLegalId(legalId, unitCode, subType, orderNo);
    }

    /**
     * 获取编辑账单数
     *
     * @param billNo
     */
    @Override
    public int getEditBillNum(String billNo) {
        QueryWrapper<OrderReceivableBillDetail> condition = new QueryWrapper<>();
        condition.lambda().notIn(OrderReceivableBillDetail::getAuditStatus,
                BillEnum.EDIT_DEL.getCode(), BillEnum.EDIT_NO_COMMIT.getCode())
                .eq(OrderReceivableBillDetail::getBillNo, billNo);
        return this.count(condition);
    }


    /**
     * 剔除费用
     */
    private void deleteCost(EditSBillForm form) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", form.getBillNo());
        //处理需要删除的费用,获取删除标识的账单详情 TODO 直接获取前端传入删除对象集合
        queryWrapper.eq("audit_status", "edit_del");
        List<OrderReceivableBillDetail> delCosts = baseMapper.selectList(queryWrapper);
        List<Long> delCostIds = new ArrayList<>();
        for (OrderReceivableBillDetail billDetail : delCosts) {
            delCostIds.add(billDetail.getCostId());
        }
        if (delCostIds.size() > 0) {
            //1.对账单相应的详情信息做删除
            QueryWrapper removeWrapper = new QueryWrapper();
            removeWrapper.eq("bill_no", form.getBillNo());
            removeWrapper.in("cost_id", delCostIds);
            remove(removeWrapper);

            //2.相应的费用录入也需要做记录
            OprCostBillForm oprCostBillForm = new OprCostBillForm();
            oprCostBillForm.setCmd("del");
            oprCostBillForm.setOprType("receivable");
            oprCostBillForm.setCostIds(delCostIds);
            omsClient.oprCostBill(oprCostBillForm);

            //3.相应的费用出账金额记录要做删除
//            QueryWrapper removeDelWrapper = new QueryWrapper();
//            removeDelWrapper.in("cost_id", delCostIds);
            costTotalService.remove(removeWrapper);

            //4.修改录用费用状态
            this.omsClient.batchUpdateCostStatus(delCostIds, "0", 1, 0);
        }
    }

    /**
     * 根据账单编号查询应收账单详情
     */
    @Override
    public List<OrderReceivableBillDetail> getByBillNo(String billNo) {
        QueryWrapper<OrderReceivableBillDetail> condition = new QueryWrapper<>();
        condition.lambda().in(OrderReceivableBillDetail::getBillNo, billNo);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 根据条件查询账单详情信息
     */
    @Override
    public List<OrderReceivableBillDetail> getByCondition(OrderReceivableBillDetail orderReceivableBillDetail) {
        QueryWrapper<OrderReceivableBillDetail> condition = new QueryWrapper<>(orderReceivableBillDetail);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 统计账单数据
     *
     * @param billNo
     */
    private void statisticsBill(String billNo) {
        List<OrderReceivableBillDetail> receivableBillDetails = this.getByBillNo(billNo);
        //查询账单主键
        OrderReceivableBillDetail billDetail = receivableBillDetails.get(0);
        List<OrderReceivableBillDetail> list = this.getByCondition(new OrderReceivableBillDetail().setBillId(billDetail.getBillId()));
        BigDecimal alreadyPaidAmount = new BigDecimal(0);
        Set<String> orderNos = new HashSet<>();
        Set<String> billNos = new HashSet<>();
        for (OrderReceivableBillDetail receivableBillDetail : list) {
            //统计已出账金额
            if (receivableBillDetail.getLocalAmount() != null) {
                alreadyPaidAmount = alreadyPaidAmount.add(receivableBillDetail.getLocalAmount());
            }
            //统计已出账订单数
            orderNos.add(receivableBillDetail.getOrderNo());
            //统计账单数
            billNos.add(receivableBillDetail.getBillNo());
        }
        OrderReceivableBill orderReceivableBill = new OrderReceivableBill()
                .setId(billDetail.getBillId()).setAlreadyPaidAmount(alreadyPaidAmount)
                .setBillOrderNum(orderNos.size())
                .setBillNum(billNos.size());

        this.receivableBillService.updateById(orderReceivableBill);
    }


    private void tmsSpecialDataProcessing(String cmd, ViewBilToOrderVO viewBillToOrder) {
        //中港运输 处理目的地:当有两条或两条以上时,则获取中转仓地址
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(cmd)) {
            if (!StringUtil.isNullOrEmpty(viewBillToOrder.getEndAddress())) {
                String[] strs = viewBillToOrder.getEndAddress().split(",");
                if (strs.length > 1) {
                    viewBillToOrder.setEndAddress(receivableBillService.getWarehouseAddress(viewBillToOrder.getOrderNo()));
                }
            }
        }
    }

}
