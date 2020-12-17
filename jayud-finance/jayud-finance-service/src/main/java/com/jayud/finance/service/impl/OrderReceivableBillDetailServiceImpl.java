package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderReceivableBillDetailMapper;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.po.OrderReceivableBill;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.*;
import com.jayud.finance.util.ReflectUtil;
import com.jayud.finance.vo.*;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 *  服务实现类
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

    @Override
    public IPage<OrderPaymentBillDetailVO> findReceiveBillDetailByPage(QueryPaymentBillDetailForm form) {
        //定义分页参数
        Page<OrderPaymentBillDetailVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("orbd.make_time"));
        IPage<OrderPaymentBillDetailVO> pageInfo = baseMapper.findReceiveBillDetailByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<OrderPaymentBillDetailVO> findReceiveBillDetail(QueryPaymentBillDetailForm form) {
        return baseMapper.findReceiveBillDetailByPage(form);
    }

    @Override
    public CommonResult submitSCw(ListForm form) {
        //参数校验
        if(form.getBillNos() == null || form.getBillNos().size() == 0){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        //校验数据状态
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no",form.getBillNos());
        List<OrderReceivableBillDetail> billDetails = baseMapper.selectList(queryWrapper);
        for (OrderReceivableBillDetail billDetail : billDetails) {
            if(!BillEnum.B_2.getCode().equals(billDetail.getAuditStatus())){
                return CommonResult.error(10001,"不符合操作条件");
            }
        }
        for (String billNo : form.getBillNos()) {
            OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
            orderReceivableBillDetail.setAuditStatus(BillEnum.B_3.getCode());
            orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
            orderReceivableBillDetail.setUpdatedUser(form.getLoginUserName());
            QueryWrapper updateWrapper = new QueryWrapper();
            updateWrapper.eq("bill_no",billNo);
            update(orderReceivableBillDetail,updateWrapper);

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
        updateWrapper.eq("bill_no",form.getBillNo());
        return update(orderReceivableBillDetail,updateWrapper);
    }

    @Override
    public Boolean applyInvoiceCancel(String billNo) {
        OrderReceivableBillDetail orderReceiveBillDetail = new OrderReceivableBillDetail();
        orderReceiveBillDetail.setApplyStatus(BillEnum.F_4.getCode());
        orderReceiveBillDetail.setAuditStatus(BillEnum.B_5_1.getCode());
        orderReceiveBillDetail.setUpdatedTime(LocalDateTime.now());
        orderReceiveBillDetail.setUpdatedUser(UserOperator.getToken());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",billNo);
        return update(orderReceiveBillDetail,queryWrapper);
    }

    @Override
    public IPage<PaymentNotPaidBillVO> findEditSBillByPage(QueryEditBillForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("orc.id"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findEditSBillByPage(page, form);
        List<PaymentNotPaidBillVO> pageList = pageInfo.getRecords();
        for (PaymentNotPaidBillVO paymentNotPaidBillVO : pageList) {
            //处理目的地:当有两条或两条以上时,则获取中转仓地址
            if(!StringUtil.isNullOrEmpty(paymentNotPaidBillVO.getEndAddress())){
                String[] strs = paymentNotPaidBillVO.getEndAddress().split(",");
                if(strs.length > 1){
                    paymentNotPaidBillVO.setEndAddress(receivableBillService.getWarehouseAddress(paymentNotPaidBillVO.getOrderNo()));
                }
            }
        }
        return pageInfo;
    }

    @Override
    public CommonResult editSBill(EditSBillForm form) {
        List<OrderReceiveBillDetailForm> receiveBillDetailForms = form.getReceiveBillDetailForms();//账单详细信息
        //该账单编号下必须有账单信息才可编辑
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", form.getBillNo());
        List<OrderReceivableBillDetail> receivableBillDetails = baseMapper.selectList(queryWrapper);//获取账单详情信息
        if (receivableBillDetails.size() == 0) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        //取原账单下得信息
        OrderReceivableBillDetail existObject = receivableBillDetails.get(0);
        //客服保存/提交
        if("save".equals(form.getCmd()) || "submit".equals(form.getCmd())) {
            //可编辑的条件：客服主管审核对账单不通过,客服主管反审核对账单，财务审核对账单不通过，财务反审核
            if (!(BillEnum.B_2_1.getCode().equals(existObject.getAuditStatus()) || BillEnum.B_7.getCode().equals(existObject.getAuditStatus()) ||
                    BillEnum.B_8.getCode().equals(existObject.getAuditStatus()) || BillEnum.B_4_1.getCode().equals(existObject.getAuditStatus())
                    || "edit_del".equals(existObject.getAuditStatus())//流程过度状态-编辑删除
                    || "edit_no_commit".equals(existObject.getAuditStatus())//流程过度状态-编辑提交
            )) {
                return CommonResult.error(10001,"不符合操作条件");
            }
            //校验本次提交的数据是否配置汇率
            if("submit".equals(form.getCmd()) && receiveBillDetailForms.size()>0){
                List<Long> costIds = new ArrayList<>();
                for (OrderReceiveBillDetailForm tempObject : receiveBillDetailForms) {
                    costIds.add(tempObject.getCostId());
                }
                StringBuilder sb = new StringBuilder();
                sb.append("请配置[");
                Boolean flag = true;
                List<OrderBillCostTotalVO> orderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, existObject.getSettlementCurrency(), existObject.getAccountTerm());
                for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                    BigDecimal exchangeRate = orderBillCostTotalVO.getExchangeRate();//如果费率为0，则抛异常回滚数据
                    if ((exchangeRate == null || exchangeRate.compareTo(new BigDecimal(0)) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals(existObject.getSettlementCurrency())) {
                        //根据币种查询币种描述
                        String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
                        String dCurrency = currencyRateService.getNameByCode(existObject.getSettlementCurrency());
                        sb.append("原始币种:"+oCurrency+",兑换币种:"+dCurrency+";");
                        flag = false;
                    }
                    if(orderBillCostTotalVO.getCurrencyCode().equals("CNY")){
                        orderBillCostTotalVO.setMoney(orderBillCostTotalVO.getOldMoney());
                    }
                }
                List<OrderBillCostTotalVO> tempOrderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, "CNY", existObject.getAccountTerm());
                for (OrderBillCostTotalVO orderBillCostTotalVO : tempOrderBillCostTotalVOS) {
                    BigDecimal money = orderBillCostTotalVO.getMoney();//如果本币金额为0，说明汇率为空没配置
                    if ((money == null || money.compareTo(new BigDecimal("0")) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
                        //根据币种查询币种描述
                        String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
                        sb.append("原始币种:"+oCurrency+",兑换币种:人民币;");
                        flag = false;
                    }
                }
                if(!flag){
                    sb.append("]的汇率");
                    return CommonResult.error(10001,sb.toString());
                }
            }
            //处理需要删除的费用,获取删除标识的账单详情
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
                QueryWrapper removeDelWrapper = new QueryWrapper();
                removeDelWrapper.in("cost_id", delCostIds);
                costTotalService.remove(removeWrapper);
            }
            //处理要新增的费用
            if (receiveBillDetailForms.size() > 0) {
                Boolean result = true;//结果标识
                OrderReceivableBill orderReceivableBill = receivableBillService.getById(existObject.getBillId());//获取账单信息
                //生成账单需要修改order_receivable_cost表的is_bill
                List<Long> costIds = new ArrayList<>();
                List<String> orderNos = new ArrayList<>(); //为了统计已出账订单数
                for (OrderReceiveBillDetailForm receiveBillDetail : receiveBillDetailForms) {
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
                //生成对账单数据
                //对现有账单信息进行修改
                OrderReceivableBill subTypeObject = receivableBillService.getById(existObject.getBillId());
                String subType = subTypeObject.getSubType();
                //1.统计已出账金额alreadyPaidAmount
                BigDecimal nowBillAmount = receiveBillDetailForms.stream().map(OrderReceiveBillDetailForm::getLocalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal alreadyPaidAmount = receivableBillService.getSAlreadyPaidAmount(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), subType);
                orderReceivableBill.setAlreadyPaidAmount(alreadyPaidAmount.add(nowBillAmount));
                //2.统计已出账订单数billOrderNum
                List<String> validOrders = new ArrayList<>();
                for (String orderNo : orderNos) {
                    QueryWrapper queryWrapper1 = new QueryWrapper();
                    queryWrapper1.eq("order_no", orderNo);
                    List<OrderReceivableBillDetail> orderNoOjects = list(queryWrapper1);
                    if (orderNoOjects == null || orderNoOjects.size() == 0) {
                        validOrders.add(orderNo);
                    }
                }
                Integer nowBillOrderNum = validOrders.size();
                Integer billOrderNum = receivableBillService.getSBillOrderNum(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), subType);
                orderReceivableBill.setBillOrderNum(billOrderNum + nowBillOrderNum);
                //3.统计账单数billNum
                Integer billNum = receivableBillService.getSBillNum(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), subType);
                orderReceivableBill.setBillNum(billNum);
                orderReceivableBill.setUpdatedTime(LocalDateTime.now());
                orderReceivableBill.setUpdatedUser(form.getLoginUserName());
                result = receivableBillService.updateById(orderReceivableBill);
                if (!result) {
                    return CommonResult.error(ResultEnum.OPR_FAIL);
                }
                //开始保存对账单详情数据
                //获取剩余旧数据的状态和结算期和结算币种,账单编号维度
                List<OrderReceivableBillDetail> receiveBillDetails = ConvertUtil.convertList(receiveBillDetailForms, OrderReceivableBillDetail.class);
                String settlementCurrency = existObject.getSettlementCurrency();
                for (int i = 0; i < receiveBillDetails.size(); i++) {
                    receiveBillDetails.get(i).setStatus("1");
                    receiveBillDetails.get(i).setBillNo(form.getBillNo());
                    receiveBillDetails.get(i).setBillId(existObject.getBillId());
                    receiveBillDetails.get(i).setAccountTerm(existObject.getAccountTerm());
                    receiveBillDetails.get(i).setSettlementCurrency(settlementCurrency);
                    receiveBillDetails.get(i).setAuditStatus("edit_no_commit");//编辑保存未提交的，给前台做区分
                    receiveBillDetails.get(i).setCreatedOrderTime(DateUtils.stringToDate(receiveBillDetailForms.get(i).getCreatedTimeStr(), DateUtils.DATE_PATTERN));
                    receiveBillDetails.get(i).setMakeUser(form.getLoginUserName());
                    receiveBillDetails.get(i).setMakeTime(LocalDateTime.now());
                    receiveBillDetails.get(i).setCreatedUser(form.getLoginUserName());
                }

                //解决报错时重复添加数据问题
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.in("cost_id",costIds);
                remove(queryWrapper1);

                result = saveBatch(receiveBillDetails);
                if (!result) {
                    return CommonResult.error(ResultEnum.OPR_FAIL);
                }
                //删除旧的费用
                QueryWrapper removeWrapper = new QueryWrapper();
                removeWrapper.in("cost_id", costIds);
                costTotalService.remove(removeWrapper);
                //开始保存费用维度的金额信息  以结算币种进行转换后保存
                List<OrderBillCostTotal> orderBillCostTotals = new ArrayList<>();
                //根据费用ID统计费用信息,将原始费用信息根据结算币种进行转换
                List<OrderBillCostTotalVO> orderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, settlementCurrency,existObject.getAccountTerm());
                for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                    orderBillCostTotalVO.setBillNo(form.getBillNo());
                    orderBillCostTotalVO.setCurrencyCode(settlementCurrency);
                    BigDecimal localMoney = orderBillCostTotalVO.getMoney();//本币金额
                    BigDecimal exchangeRate = orderBillCostTotalVO.getExchangeRate();
                    if(exchangeRate == null || exchangeRate.compareTo(new BigDecimal("0")) == 0){
                        exchangeRate = new BigDecimal("1");
                    }
                    BigDecimal money = localMoney.multiply(exchangeRate);
                    orderBillCostTotalVO.setMoney(money);
                    OrderBillCostTotal orderBillCostTotal = ConvertUtil.convert(orderBillCostTotalVO, OrderBillCostTotal.class);
                    orderBillCostTotal.setLocalMoney(localMoney);
                    orderBillCostTotal.setMoneyType("2");
                    orderBillCostTotals.add(orderBillCostTotal);
                }
                result = costTotalService.saveBatch(orderBillCostTotals);
                if (!result) {
                    return CommonResult.error(ResultEnum.OPR_FAIL);
                }
            }
        }
        if("submit".equals(form.getCmd())){//客服提交
            Boolean result = editSBillSubmit(form.getBillNo(),form.getLoginUserName());
            if (!result) {
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
        }
        if("cw_save".equals(form.getCmd())){//财务编辑
            List<OrderReceivableBillDetail> receiveBillDetails = new ArrayList<>();
            List<OrderCostForm> orderCostForms = new ArrayList<>();
            for (OrderReceiveBillDetailForm receiveBillDetailForm : receiveBillDetailForms) {
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
            omsClient.oprCostGenreByCw(orderCostForms,"receivable");
        }
        //重新编辑后清除核销内容
        QueryWrapper removeVerification = new QueryWrapper();
        removeVerification.eq("bill_no",existObject.getBillNo());
        verificationService.remove(removeVerification);
        return CommonResult.success();
    }

    @Override
    public Boolean editSBillSubmit(String billNo,String loginUserName) {
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
        updateWrapper.eq("bill_no",billNo);
        return update(orderReceivableBillDetail,updateWrapper);
    }

    @Override
    public List<ViewBilToOrderVO> viewSBillDetail(String billNo) {
        List<ViewBilToOrderVO> orderList = baseMapper.viewSBillDetail(billNo);
        List<ViewBilToOrderVO> newOrderList = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findSCostClass(billNo);
        for (ViewBilToOrderVO viewBillToOrder : orderList) {
            //处理目的地:当有两条或两条以上时,则获取中转仓地址
            if(!StringUtil.isNullOrEmpty(viewBillToOrder.getEndAddress())){
                String[] strs = viewBillToOrder.getEndAddress().split(",");
                if(strs.length > 1){
                    viewBillToOrder.setEndAddress(receivableBillService.getWarehouseAddress(viewBillToOrder.getOrderNo()));
                }
            }
            for(ViewBillToCostClassVO viewBillToCostClass : findCostClass){
                if((StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo()) && StringUtil.isNullOrEmpty(viewBillToCostClass.getSubOrderNo()) &&
                   viewBillToCostClass.getOrderNo().equals(viewBillToOrder.getOrderNo()))
                   || ((!StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo())) && viewBillToOrder.getSubOrderNo().equals(viewBillToCostClass.getSubOrderNo()))){
                    try {
                        String addProperties = "";
                        String addValue = "";
                        Map<String,Object> propertiesMap = new HashMap<String,Object>();
                        Class cls = viewBillToCostClass.getClass();
                        Field[] fields = cls.getDeclaredFields();
                        for (int i = 0; i < fields.length; i++) {
                            Field f = fields[i];
                            f.setAccessible(true);
                            if("name".equals(f.getName())){
                                addProperties = String.valueOf(f.get(viewBillToCostClass)).toLowerCase();//待新增得属性
                            }
                            if("money".equals(f.getName())){
                                addValue = String.valueOf(f.get(viewBillToCostClass));//待新增属性得值
                            }
                            propertiesMap.put(addProperties, addValue);
                        }
                        viewBillToOrder = (ViewBilToOrderVO) ReflectUtil.getObject(viewBillToOrder, propertiesMap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            newOrderList.add(viewBillToOrder);
        }
        return newOrderList;
    }

    @Override
    public List<SheetHeadVO> findSSheetHead(String billNo) {
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
        }catch (Exception e){
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
        queryWrapper.eq("bill_no",form.getBillNo());
        List<OrderReceivableBillDetail> existList = baseMapper.selectList(queryWrapper);
        OrderReceivableBillDetail existObject = existList.get(0);
        OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        String auditStatus = "";
        if("audit".equals(form.getCmd())) {
            if(!BillEnum.B_1.getCode().equals(existObject.getAuditStatus())){
                return CommonResult.error(100001,"不符合审核条件");
            }
            if ("0".equals(form.getAuditStatus())) {
                auditStatus = BillEnum.B_2.getCode();
            } else if ("1".equals(form.getAuditStatus())) {
                auditStatus = BillEnum.B_2_1.getCode();
            }
            orderReceivableBillDetail.setAuditTime(LocalDateTime.now());
            orderReceivableBillDetail.setAuditUser(form.getLoginUserName());
            auditInfoForm.setAuditTypeDesc("应收对账单审核");
        }else if("cw_audit".equals(form.getCmd())){//财务对账单审核
            if(!BillEnum.B_3.getCode().equals(existObject.getAuditStatus())){
                return CommonResult.error(100001,"不符合审核条件");
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
        updateWrapper.eq("bill_no",form.getBillNo());
        Boolean result = update(orderReceivableBillDetail,updateWrapper);
        if(!result){
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

    @Override
    public CommonResult contrarySAudit(ListForm form) {
        //反审核条件
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no",form.getBillNos());
        List<OrderReceivableBillDetail> existList = baseMapper.selectList(queryWrapper);
        if("kf_s_reject".equals(form.getCmd())){
            for (OrderReceivableBillDetail existObject : existList) {
                if(!BillEnum.B_2.getCode().equals(existObject.getAuditStatus())){
                    return CommonResult.error(10001,"存在不符合操作条件的数据");
                }
            }
            for (String billNo : form.getBillNos()) {
                OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
                orderReceivableBillDetail.setAuditStatus(BillEnum.B_7.getCode());
                orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
                orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no",billNo);
                update(orderReceivableBillDetail,updateWrapper);
            }
        }else if("cw_s_reject".equals(form.getCmd())){
            for (OrderReceivableBillDetail existObject : existList) {
                if(!BillEnum.B_5_1.getCode().equals(existObject.getAuditStatus())){
                    return CommonResult.error(10001,"存在不符合操作条件的数据");
                }
            }
            for (String billNo : form.getBillNos()) {
                OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
                orderReceivableBillDetail.setAuditStatus(BillEnum.B_8.getCode());
                orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
                orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no",billNo);
                update(orderReceivableBillDetail,updateWrapper);
            }
        }
        return CommonResult.success();
    }

    @Override
    public IPage<PaymentNotPaidBillVO> findSBillAuditByPage(QueryFBillAuditForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("temp.createdTimeStr"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findSBillAuditByPage(page, form);
        //所有的费用类型
        List<InitComboxVO> initComboxVOS = omsClient.findEnableCostGenre().getData();
        List<PaymentNotPaidBillVO> pageList = pageInfo.getRecords();
        for (PaymentNotPaidBillVO paymentNotPaidBill : pageList) {
            List<InitComboxVO> haveCostGenre = new ArrayList<>();
            if(!StringUtil.isNullOrEmpty(paymentNotPaidBill.getCostGenreStr())) {
                String[] ids = paymentNotPaidBill.getCostGenreStr().split(",");//费用类型逗号分隔形式的
                for (String id : ids) {
                    for (InitComboxVO initComboxVO : initComboxVOS) {
                        if(initComboxVO.getId() == Long.parseLong(id)){
                            haveCostGenre.add(initComboxVO);
                        }
                    }
                }
                paymentNotPaidBill.setCostGenreList(haveCostGenre);
            }
            //处理目的地:当有两条或两条以上时,则获取中转仓地址
            if(!StringUtil.isNullOrEmpty(paymentNotPaidBill.getEndAddress())){
                String[] strs = paymentNotPaidBill.getEndAddress().split(",");
                if(strs.length > 1){
                    paymentNotPaidBill.setEndAddress(receivableBillService.getWarehouseAddress(paymentNotPaidBill.getOrderNo()));
                }
            }
        }
        return pageInfo;
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
        queryWrapper.eq("bill_no",form.getBillNo());
        List<OrderReceivableBillDetail> billDetails = baseMapper.selectList(queryWrapper);
        OrderReceivableBillDetail receivableBillDetail = billDetails.get(0);
        if(!BillEnum.B_5.getCode().equals(receivableBillDetail.getAuditStatus())){
            return CommonResult.error(10001,"不符合操作条件");
        }
        OrderReceivableBillDetail billDetail = new OrderReceivableBillDetail();
        String applyStatus = "";
        String status = "";
        if("0".equals(form.getAuditStatus())){
            applyStatus = BillEnum.F_2.getCode();
            status = BillEnum.B_6.getCode();

            //开票审核通过之后，需要反推汇率和本币金额到费用录入表
            List<OrderCostForm> orderCostForms = new ArrayList<>();
            for (OrderReceivableBillDetail tempObject : billDetails) {
                OrderCostForm orderCostForm = new OrderCostForm();
                orderCostForm.setCostId(tempObject.getCostId());
                orderCostForm.setLoginUserName(form.getLoginUserName());
                orderCostForms.add(orderCostForm);
            }
            ApiResult result = omsClient.writeBackCostData(orderCostForms,"receivable");
            if(result.getCode() != 200){
                return CommonResult.error(result.getCode(),result.getMsg());
            }
        }else {
            applyStatus = BillEnum.F_3.getCode();
            status = BillEnum.B_6_1.getCode();
        }
        billDetail.setApplyStatus(applyStatus);
        billDetail.setAuditStatus(status);
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no",form.getBillNo());
        Integer num = baseMapper.update(billDetail,updateWrapper);
        if(num > 0){
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
        }else{
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @Override
    public Boolean editSSaveConfirm(List<Long> costIds) {
        //在录入费用表的is_bill做标识为save_confirm
        return omsClient.editSaveConfirm(costIds,"receivable","save_confirm").getData();
    }

    @Override
    public Boolean editSDel(List<Long> costIds) {
        //从删除的costIds里面挑出那种保存确定的数据,回滚到未出账状态
        List<Long> saveConfirmIds = receivableBillService.findSaveConfirmData(costIds);
        Boolean result = true;
        if(saveConfirmIds != null && saveConfirmIds.size() > 0) {
            result = omsClient.editSaveConfirm(saveConfirmIds, "receivable", "edit_del").getData();
            if (!result) {
                return false;
            }
        }
        //已存在的数据删除,只在账单详情表做记录
        List<Long> editDelIds = costIds.stream().filter(item -> !saveConfirmIds.contains(item)).collect(toList());
        if(editDelIds != null && editDelIds.size() > 0) {
            OrderReceivableBillDetail receivableBillDetail = new OrderReceivableBillDetail();
            receivableBillDetail.setAuditStatus("edit_del");//持续操作中的过度状态
            QueryWrapper updateWrapper = new QueryWrapper();
            updateWrapper.in("cost_id", editDelIds);
            result = update(receivableBillDetail, updateWrapper);
            if(!result){
                return false;
            }
        }
        return true;
    }

    @Override
    public ReceivableHeaderForm getReceivableHeaderForm(String billNo) {
        return baseMapper.getReceivableHeaderForm(billNo);
    }

    @Override
    public List<APARDetailForm> findReceivableHeaderDetail(String billNo) {
        return baseMapper.findReceivableHeaderDetail(billNo);
    }

    @Override
    public CostAmountVO getSCostAmountView(String billNo) {
        return baseMapper.getSCostAmountView(billNo);
    }


}
