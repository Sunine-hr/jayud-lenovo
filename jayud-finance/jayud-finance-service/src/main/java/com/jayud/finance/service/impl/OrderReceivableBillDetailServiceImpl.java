package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderReceivableBillDetailMapper;
import com.jayud.finance.po.*;
import com.jayud.finance.service.IOrderBillCostTotalService;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
import com.jayud.finance.service.IOrderReceivableBillService;
import com.jayud.finance.util.ReflectUtil;
import com.jayud.finance.vo.*;
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
        return pageInfo;
    }

    @Override
    public Boolean editSBill(EditSBillForm form) {
        List<OrderReceiveBillDetailForm> receiveBillDetailForms = form.getReceiveBillDetailForms();//账单详细信息
        //该账单编号下必须有账单信息才可编辑
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", form.getBillNo());
        List<OrderReceivableBillDetail> receivableBillDetails = baseMapper.selectList(queryWrapper);
        if (receivableBillDetails.size() == 0) {
            return false;
        }
        OrderReceivableBillDetail existObject = receivableBillDetails.get(0);
        //客服保存/提交
        if("save".equals(form.getCmd()) || "submit".equals(form.getCmd())) {
            //可编辑的条件：客服主管审核对账单不通过,客服主管反审核对账单，财务审核对账单不通过，财务反审核
            if (!(BillEnum.B_2_1.getCode().equals(existObject.getAuditStatus()) || BillEnum.B_7.getCode().equals(existObject.getAuditStatus()) ||
                    BillEnum.B_8.getCode().equals(existObject.getAuditStatus()) || BillEnum.B_4_1.getCode().equals(existObject.getAuditStatus())
                    || "edit_del".equals(existObject.getAuditStatus())//流程过度状态-编辑删除
                    || "edit_no_commit".equals(existObject.getAuditStatus())//流程过度状态-编辑提交
            )) {
                return false;
            }
            //处理需要删除的费用
            //获取删除标识的账单详情
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
            }
            //处理要新增的费用
            if (receiveBillDetailForms.size() > 0) {
                Boolean result = true;
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
                        return false;
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
                    return false;
                }
                //开始保存对账单详情数据
                //获取剩余旧数据的状态和结算期和结算币种,账单编号维度
                OrderReceivableBillDetail oldSBillDetail = receivableBillDetails.get(0);
                List<OrderReceivableBillDetail> receiveBillDetails = ConvertUtil.convertList(receiveBillDetailForms, OrderReceivableBillDetail.class);
                for (int i = 0; i < receiveBillDetails.size(); i++) {
                    receiveBillDetails.get(i).setStatus("1");
                    receiveBillDetails.get(i).setBillNo(form.getBillNo());
                    receiveBillDetails.get(i).setBillId(existObject.getBillId());
                    receiveBillDetails.get(i).setBeginAccountTerm(oldSBillDetail.getBeginAccountTerm());
                    receiveBillDetails.get(i).setEndAccountTerm(oldSBillDetail.getEndAccountTerm());
                    receiveBillDetails.get(i).setSettlementCurrency(oldSBillDetail.getSettlementCurrency());
                    receiveBillDetails.get(i).setAuditStatus("edit_no_commit");//编辑保存未提交的，给前台做区分
                    receiveBillDetails.get(i).setCreatedOrderTime(DateUtils.stringToDate(receiveBillDetailForms.get(i).getCreatedTimeStr(), DateUtils.DATE_PATTERN));
                    receiveBillDetails.get(i).setMakeUser(form.getLoginUserName());
                    receiveBillDetails.get(i).setMakeTime(LocalDateTime.now());
                    receiveBillDetails.get(i).setCreatedUser(form.getLoginUserName());
                }
                result = saveOrUpdateBatch(receiveBillDetails);
                if (!result) {
                    return false;
                }
                //删除旧的费用
                QueryWrapper removeWrapper = new QueryWrapper();
                removeWrapper.in("cost_id", costIds);
                costTotalService.remove(removeWrapper);
                //开始保存费用维度的金额信息  以结算币种进行转换后保存
                List<OrderBillCostTotal> orderBillCostTotals = new ArrayList<>();
                //根据费用ID统计费用信息,将原始费用信息根据结算币种进行转换
                String settlementCurrency = oldSBillDetail.getSettlementCurrency();
                List<OrderBillCostTotalVO> orderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, settlementCurrency);
                for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                    orderBillCostTotalVO.setBillNo(form.getBillNo());
                    orderBillCostTotalVO.setCurrencyCode(settlementCurrency);
                    BigDecimal localMoney = orderBillCostTotalVO.getMoney();//本币金额
                    BigDecimal money = localMoney.multiply(orderBillCostTotalVO.getExchangeRate());
                    orderBillCostTotalVO.setMoney(money);
                    OrderBillCostTotal orderBillCostTotal = ConvertUtil.convert(orderBillCostTotalVO, OrderBillCostTotal.class);
                    orderBillCostTotal.setLocalMoney(localMoney);
                    orderBillCostTotal.setMoneyType("2");
                    orderBillCostTotals.add(orderBillCostTotal);
                }
                result = costTotalService.saveBatch(orderBillCostTotals);
                if (!result) {
                    return false;
                }
            }
        }
        if("submit".equals(form.getCmd())){//客服提交
            editSBillSubmit(form.getBillNo(),form.getLoginUserName());
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
                receivableBillDetails.add(receivableBillDetail);

                OrderCostForm orderCostForm = new OrderCostForm();
                orderCostForm.setLoginUserName(form.getLoginUserName());
                orderCostForm.setCostGenreId(receiveBillDetailForm.getCostGenreId());
                orderCostForm.setCostId(receiveBillDetailForm.getCostId());
                orderCostForms.add(orderCostForm);
            }
            Boolean result = updateBatchById(receiveBillDetails);
            if (!result) {
                return false;
            }
            omsClient.oprCostGenreByCw(orderCostForms,"receivable");
        }
        return true;
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
            for(ViewBillToCostClassVO viewBillToCostClass : findCostClass){
                if(viewBillToOrder.getOrderNo().equals(viewBillToCostClass.getOrderNo())){
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
                                addProperties = String.valueOf(f.get(viewBillToCostClass));//待新增得属性
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
        if("kf_f_reject".equals(form.getCmd())){
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
        }else if("cw_f_reject".equals(form.getCmd())){
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
    public Boolean auditSInvoice(BillAuditForm form) {
        OrderReceivableBillDetail billDetail = new OrderReceivableBillDetail();
        String applyStatus = "";
        String status = "";
        if("0".equals(form.getAuditStatus())){
            applyStatus = BillEnum.F_2.getCode();
            status = BillEnum.B_6.getCode();
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
            return true;
        }else{
            return false;
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


}
