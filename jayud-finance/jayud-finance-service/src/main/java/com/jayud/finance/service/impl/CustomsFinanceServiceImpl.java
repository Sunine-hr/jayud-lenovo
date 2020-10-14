package com.jayud.finance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.annotations.HeadProperty;
import com.jayud.finance.annotations.IsFee;
import com.jayud.finance.bo.APARDetailForm;
import com.jayud.finance.bo.PayableHeaderForm;
import com.jayud.finance.bo.ReceivableHeaderForm;
import com.jayud.finance.enums.FormIDEnum;
import com.jayud.finance.enums.InvoiceFormNeedRelationEnum;
import com.jayud.finance.enums.InvoiceTypeEnum;
import com.jayud.finance.kingdeesettings.K3CloudConfig;
import com.jayud.finance.po.*;
import com.jayud.finance.service.*;
import com.jayud.finance.util.FileUtil;
import com.jayud.finance.util.KingdeeHttpUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;


@Service
@Slf4j
@RefreshScope
public class CustomsFinanceServiceImpl implements CustomsFinanceService {
    @Autowired
    RedisUtils redisUtils;
    @Value("${relation-setting.redis-key.yunbaoguan.rec-company}")
    String yunbaoguanCompKeyRec;
    @Value("${relation-setting.redis-key.yunbaoguan.rec-fee}")
    String yunbaoguanFeeKeyRec;
    @Value("${relation-setting.redis-key.yunbaoguan.pay-company}")
    String yunbaoguanCompKeyPay;
    @Value("${relation-setting.redis-key.yunbaoguan.pay-fee}")
    String yunbaoguanFeeKeyPay;
    @Autowired
    KingdeeService kingdeeService;
    @Autowired
    BaseService baseService;

    @Autowired
    ICustomsFinanceCoRelationService coRelationService;
    @Autowired
    ICustomsFinanceFeeRelationService feeRelationService;
    @Autowired
    CookieService cookieService;
    @Autowired
    K3CloudConfig k3CloudConfig;

    @Override
    public Boolean pushReceivable(List<CustomsReceivable> customsReceivable, Map<String, List<String>> unremovables) {

        Map<String, CustomsFinanceCoRelation> coRelationMap = new HashMap<>();
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = new HashMap<>();
        String compJson = redisUtils.get(yunbaoguanCompKeyRec);
        if (StringUtils.isNotEmpty(compJson)) {
            coRelationMap = (Map<String, CustomsFinanceCoRelation>) JSONObject.parseObject(compJson, Map.class);
        } else {
            List<CustomsFinanceCoRelation> list = coRelationService.list();
            for (CustomsFinanceCoRelation item : list) {
                if (!coRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                    coRelationMap.put(item.getYunbaoguanName(), item);
                }
            }
            redisUtils.set(yunbaoguanCompKeyRec, JSONUtil.toJsonStr(coRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        }
        String feeJson = redisUtils.get(yunbaoguanFeeKeyRec);
        if (StringUtils.isNotEmpty(feeJson)) {
            feeRelationMap = (Map<String, CustomsFinanceFeeRelation>) JSONObject.parseObject(feeJson, Map.class);
        } else {
            List<CustomsFinanceFeeRelation> list = feeRelationService.list();
            for (CustomsFinanceFeeRelation item : list) {
                if (!feeRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                    feeRelationMap.put(item.getYunbaoguanName(), item);
                }
            }
            redisUtils.set(yunbaoguanFeeKeyRec, JSONUtil.toJsonStr(feeRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        }

        if (CollectionUtil.isEmpty(feeRelationMap) || CollectionUtil.isEmpty(coRelationMap)) {
            log.error("基础数据加载失败：费用或公司对应关系表加载失败");
            return false;
        }

        //完成预加载数据后判断是否有需要剔除无法重送的数据
        //todo 应收 与 其他应收 在循环中判断当前客户是否可写入
//        if (CollectionUtil.isNotEmpty(unremovables)) {
//            customsReceivable = Lists.newArrayList(customsReceivable);
//            Iterator<CustomsReceivable> iterator = customsReceivable.iterator();
//            while (iterator.hasNext()) {
//                CustomsReceivable next = iterator.next();
//                if (coRelationMap.containsKey(next.getCustomerName())) {
//                    iterator.remove();
//                }
//            }
//        }
        List<String> unremovableReceivableOther = null;
        List<String> unremovableReceivable = null;
        if (CollectionUtil.isNotEmpty(unremovables)) {
            unremovableReceivableOther = unremovables.get("receivableOther");
            unremovableReceivable = unremovables.get("receivable");
        }

        //初始化可能的其他应收单线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<FutureTask> futureTasks = new ArrayList<>();

        //对应收单列表依次处理
        StringBuilder errorString = new StringBuilder();
        for (CustomsReceivable item : customsReceivable) {
            //要写入金蝶的实体
            ReceivableHeaderForm dataForm = new ReceivableHeaderForm();

            //本行数据是否具有特殊应付数据
            Boolean hasOtherReceivable = false;

            //拼装信息

            //写入费用项
            log.debug("1.开始拼装应收明细...");
            List<APARDetailForm> list = new ArrayList<>();
            Class<? extends CustomsReceivable> clz = item.getClass();
            Field[] fields = clz.getDeclaredFields();

            for (Field field : fields) {
                String name = field.getName();

                //找到实体类中带isFee注解的数据
                if (Objects.nonNull(field.getDeclaredAnnotation(IsFee.class))) {
                    //读出注解@ApiModelProperty中的中文释义进行匹配
                    try {
                        Method getMethod = clz.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                        Object invoke = getMethod.invoke(item);
                        //费用项为零不记录
                        if (Objects.isNull(invoke) || Objects.equals("0.00", invoke.toString())) {
                            continue;
                        }
                        ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);

                        String jsonStr = JSONUtil.toJsonStr(feeRelationMap.get(annotation.value()));
                        CustomsFinanceFeeRelation customsFinanceFeeRelation = JSONObject.parseObject(jsonStr, CustomsFinanceFeeRelation.class);

                        if (Objects.isNull(customsFinanceFeeRelation)) {
                            //费用无法对应金蝶,记录
                            errorString.append(String.format("云报关费用项%s无法匹配金蝶费用项;", annotation.value()));
                            log.error(String.format("云报关费用项%s无法匹配金蝶费用项;", annotation.value()));
                        }
                        APARDetailForm feeItem = new APARDetailForm();
                        feeItem.setExpenseName(customsFinanceFeeRelation.getKingdeeName());
                        feeItem.setExpenseTypeName(customsFinanceFeeRelation.getType());
                        feeItem.setExpenseCategoryName(customsFinanceFeeRelation.getCategory());
                        feeItem.setPriceQty(BigDecimal.ONE);
                        //处理费用类别为代垫税金的数据
                        if ((Objects.equals(customsFinanceFeeRelation.getCategory(), "代垫税金"))) {
                            feeItem.setTaxRate(BigDecimal.ZERO);
                            //如果代垫税金项目的应收款不为零，那么将要对本行进行特殊应收款项的处理
                            if (!Objects.equals("0.00", invoke.toString())) {
                                hasOtherReceivable = true;
                            }
                            continue;
                        } else {
                            feeItem.setTaxRate(new BigDecimal(customsFinanceFeeRelation.getTaxRate()));
                        }
                        feeItem.setTaxPrice(new BigDecimal(invoke.toString()));
                        list.add(feeItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //处理可能的其他应收单
            //执行条件：
            //本行存在需要存入其他应收单的数据，并且不存在禁止推送的客户名，或本行的客户名称不在禁止推送的列表中
                        if (hasOtherReceivable &&
                    ((CollectionUtil.isEmpty(unremovableReceivableOther)) ||
                            (CollectionUtil.isNotEmpty(unremovableReceivableOther) && !unremovableReceivableOther.contains(item.getCustomerName())))) {
                log.info("处理其他应收款项");
                PushOtherReceivable pushOtherReceivable = new PushOtherReceivable();
                pushOtherReceivable.customsReceivable = item;
                pushOtherReceivable.coRelationMap = coRelationMap;
                pushOtherReceivable.feeRelationMap = feeRelationMap;
                pushOtherReceivable.cookieService = cookieService;
                pushOtherReceivable.k3CloudConfig = k3CloudConfig;
                FutureTask<String> futureTask = new FutureTask<>(pushOtherReceivable);
                futureTasks.add(futureTask);
                executorService.execute(futureTask);
            }

            //如果本行应收没有数据，将推出本次循环进行下一次循环
            if (CollectionUtil.isEmpty(list)) {
                String feeErrorStr = String.format("当前应收费用项没有数据，退出本行处理过程");
                log.info(feeErrorStr);
                errorString.append(feeErrorStr);
                continue;
            }

            //如果本行应收有数据，但禁止推送列表中有本行对应的客户名，推出本次循环进行下一行处理
            if (CollectionUtil.isNotEmpty(unremovableReceivable)) {
                if (unremovableReceivable.contains(item.getCustomerName())) {
                    String removableErrorStr = String.format("当前应收费用项目已经存在并且不可覆盖，越过。");
                    log.info(removableErrorStr);
                    errorString.append(removableErrorStr);
                    continue;
                }
            }

            dataForm.setEntityDetail(list);

            //抬头信息
            //尝试查询客戶名并写入
            log.debug("2.开始拼装表单头部...");
            //从财务给的数据中对应到金蝶的客户名称
            CustomsFinanceCoRelation customsFinanceCoRelation = null;
            try {
                customsFinanceCoRelation = JSONObject.parseObject(JSONUtil.toJsonStr(coRelationMap.get(item.getCustomerName())), CustomsFinanceCoRelation.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String kingdeeCompName = customsFinanceCoRelation == null ? "" : customsFinanceCoRelation.getKingdeeName();
            Optional<Customer> customer = baseService.get(kingdeeCompName, Customer.class);
            if (!customer.isPresent()) {
                log.error(String.format("没有找到对应的客户name='%s'", item.getCustomerName()));
                errorString.append(String.format("没有找到对应的客户name='%s'", item.getCustomerName()));
                continue;
            }
            dataForm.setCustomerName(kingdeeCompName);
            dataForm.setCurrency("CNY");
            dataForm.setSettleOrgName("深圳市佳裕达报关有限公司");
            dataForm.setSaleDeptName("报关部");
            dataForm.setBusinessNo(item.getCustomApplyNo());
            dataForm.setRemark(

                    getRemark());
            dataForm.setBillNo("");
            dataForm.setBaseCurrency("CNY");
            dataForm.setBusinessDate(item.getApplyDt());


            log.debug("3.拼装完毕，正在汇总拼装时是否发生的异常...");
            if (StringUtils.isNotBlank(errorString.toString())) {
                executorService.shutdown();
                log.error(errorString.toString());
                return false;
            }

            try {
                log.debug("4.拼装校验无异常，开始向金蝶发送数据...");
                CommonResult commonResult = kingdeeService.saveReceivableBill(FormIDEnum.RECEIVABLE.getFormid(), dataForm);
                if (Objects.nonNull(commonResult) && commonResult.getCode() == ResultEnum.SUCCESS.getCode()) {
                    log.info("金蝶应收单推送完毕：（{}）", kingdeeCompName);
                }
            } catch (
                    Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        try {
            executorService.shutdown();
            if (CollectionUtil.isNotEmpty(futureTasks) && !executorService.isTerminated())
                executorService.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (FutureTask futureTask : futureTasks) {
            try {
                errorString.append(futureTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.isNotEmpty(errorString.toString())) {
            log.error(String.format("写入应收单的时候出现异常：{}"), errorString.toString());
            executorService.shutdown();
            return false;
        }


        log.info(String.format("所有的应收数据推送完毕"));
        return true;
    }

    @Override
    public Boolean pushPayable(List<CustomsPayable> customsPayableForms, Map<String, List<String>> unremovables) {
        Map<String, CustomsFinanceCoRelation> coRelationMap = new HashMap<>();
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = new HashMap<>();
        String compJson = redisUtils.get(yunbaoguanCompKeyPay);
        if (StringUtils.isNotEmpty(compJson)) {
            coRelationMap = (Map<String, CustomsFinanceCoRelation>) JSONObject.parseObject(compJson, Map.class);
        } else {
            List<CustomsFinanceCoRelation> list = coRelationService.list();
            for (CustomsFinanceCoRelation item : list) {
                if (!coRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                    coRelationMap.put(item.getYunbaoguanName(), item);
                }
            }
            redisUtils.set(yunbaoguanCompKeyPay, JSONUtil.toJsonStr(coRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        }
        String feeJson = redisUtils.get(yunbaoguanFeeKeyPay);
        if (StringUtils.isNotEmpty(feeJson)) {
            feeRelationMap = (Map<String, CustomsFinanceFeeRelation>) JSONObject.parseObject(feeJson, Map.class);
        } else {
            List<CustomsFinanceFeeRelation> list = feeRelationService.list();
            for (CustomsFinanceFeeRelation item : list) {
                if (!feeRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                    feeRelationMap.put(item.getYunbaoguanName(), item);
                }
            }
            redisUtils.set(yunbaoguanFeeKeyPay, JSONUtil.toJsonStr(feeRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        }

        //判断预加载数据是否成功
        if (CollectionUtil.isEmpty(feeRelationMap) || CollectionUtil.isEmpty(coRelationMap)) {
            log.error("费用或公司名基础数据加载异常");
            return false;
        }

        //如果有删除失败的已存在订单，将不推送此条数据
        //todo 应付 与 其他应付要分开处理（应付中哪些不能传，其他应付中哪些不能传）
        List<String> unremovablePayable = null;
        List<String> unremovablePayableOther = null;
        if (CollectionUtil.isNotEmpty(unremovables)) {
            unremovablePayable = unremovables.get("payable");
            unremovablePayableOther = unremovables.get("payableOther");
        }


        //整理出不同的应付供应商，每一个应付供应商会生成一个单独的应付单
        log.debug("开始根据供应商分组...");
        StringBuilder errorString = new StringBuilder();

        Map<String, List> diffSupplierMaps = new HashMap<>();
        Map<String, List> otherPayableDiffSupplierMaps = new HashMap<>();

        for (CustomsPayable details : customsPayableForms) {
            String supplierName = details.getTargetName();
            CustomsFinanceFeeRelation feeRelation = null;
            try {
                feeRelation = JSONObject.parseObject(JSONUtil.toJsonStr(feeRelationMap.get(details.getFeeName())), CustomsFinanceFeeRelation.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //代垫税金项目单独列给海关
            if (Objects.nonNull(feeRelation)) {
                if (feeRelation.getCategory().equals("代垫税金")) {
                    if (otherPayableDiffSupplierMaps.containsKey(supplierName)) {
                        otherPayableDiffSupplierMaps.get(supplierName).add(details);
                    } else {
                        otherPayableDiffSupplierMaps.put(supplierName, Lists.newArrayList(details));
                    }
                } else {
                    if (diffSupplierMaps.containsKey(supplierName)) {
                        diffSupplierMaps.get(supplierName).add(details);
                    } else {
                        diffSupplierMaps.put(supplierName, Lists.newArrayList(details));
                    }
                }
            }
        }


        log.debug("开始写入应付单数据...");

        ExecutorService executorService = Executors.newCachedThreadPool();
        List<FutureTask> futureTasks = new ArrayList<>();
        //遍历可能的其他应收应付
        if (CollectionUtil.isNotEmpty(otherPayableDiffSupplierMaps)) {
            for (Map.Entry<String, List> stringListEntry : otherPayableDiffSupplierMaps.entrySet()) {
                if (CollectionUtil.isNotEmpty(unremovablePayableOther) && unremovablePayableOther.contains(stringListEntry.getKey())) {
                    String removableErrorStr = String.format("当前其他应付费用项目已经存在并且不可覆盖，越过。");
                    log.info(removableErrorStr);
                    errorString.append(removableErrorStr);
                    continue;
                }
                log.debug("需要写入其他应付单数据");
                //写入其他应付单数据
                PushOtherPayable pushOtherPayable = new PushOtherPayable();
                pushOtherPayable.customsPayable = stringListEntry.getValue();
                pushOtherPayable.coRelationMap = coRelationMap;
                pushOtherPayable.feeRelationMap = feeRelationMap;
                pushOtherPayable.k3CloudConfig = k3CloudConfig;
                pushOtherPayable.cookieService = cookieService;

                FutureTask<String> futureTask = new FutureTask<>(pushOtherPayable);
                futureTasks.add(futureTask);
                executorService.execute(futureTask);
            }
        }
        //遍历供应商，将数据依次写入金蝶中
        for (Map.Entry<String, List> stringListEntry : diffSupplierMaps.entrySet()) {
            if (null != unremovablePayable && unremovablePayable.contains(stringListEntry.getKey())) {
                String removableErrorStr = String.format("当前应付费用项目已经存在并且不可覆盖，越过。");
                log.info(removableErrorStr);
                errorString.append(removableErrorStr);
                continue;
            }
            List<CustomsPayable> customsPayableEntity = (List<CustomsPayable>) stringListEntry.getValue();
            //取出第一行获取相关的应付抬头信息
            CustomsPayable customsPayable = (CustomsPayable) customsPayableEntity.get(0);

            //要写入金蝶的数据
            PayableHeaderForm dataForm = new PayableHeaderForm();

            dataForm.setBusinessNo(customsPayable.getCustomApplyNo());
            //尝试查询供应商名并写入
            CustomsFinanceCoRelation companyProfile = null;
            try {
                companyProfile = JSONObject.parseObject(JSONUtil.toJsonStr(coRelationMap.get(customsPayable.getTargetName())), CustomsFinanceCoRelation.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String kingdeeCompName = companyProfile == null ? customsPayable.getTargetName() : companyProfile.getKingdeeName();
            Optional<Supplier> suppliers = baseService.get(kingdeeCompName, Supplier.class);
            if (!suppliers.isPresent()) {
                log.error("传入的供应商名称 [" + customsPayable.getTargetName() + "] 无法找到相应的金蝶系统代码");
                errorString.append("传入的供应商名称 [" + customsPayable.getTargetName() + "] 无法找到相应的金蝶系统代码");
            }

            //抬头信息
            dataForm.setSupplierName(kingdeeCompName);
            dataForm.setCurrency("CNY");
            dataForm.setSettleOrgName("深圳市佳裕达报关有限公司");
            dataForm.setRemark(getRemark());
            dataForm.setPurchaseDeptName("报关部");
            dataForm.setBillNo("");
            dataForm.setBaseCurrency("CNY");
            dataForm.setExchangeRate(BigDecimal.ONE);
            dataForm.setBusinessDate(customsPayable.getApplyDt());
            //费用明细
            List<APARDetailForm> list = new ArrayList<>();
            for (CustomsPayable payableForm : customsPayableEntity) {

                CustomsFinanceFeeRelation customsFeeItem = null;
                try {
                    customsFeeItem = JSONObject.parseObject(JSONUtil.toJsonStr(feeRelationMap.get(payableForm.getFeeName())), CustomsFinanceFeeRelation.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Objects.isNull(customsFeeItem)) {
                    errorString.append(String.format("在金蝶的数据库中没有找到与%s相关的费用项", payableForm.getFeeName()));
                    continue;
                }
                APARDetailForm item = new APARDetailForm();
                item.setExpenseName(customsFeeItem.getKingdeeName());
                item.setExpenseCategoryName(customsFeeItem.getCategory());
                item.setExpenseTypeName(customsFeeItem.getType());
                item.setPriceQty(BigDecimal.ONE);
                item.setTaxPrice(new BigDecimal(customsPayable.getCost()));
                item.setTaxRate(BigDecimal.ZERO);
                list.add(item);
            }
            dataForm.setEntityDetail(list);

            //调用保存应付单接口
            try {
                CommonResult commonResult = kingdeeService.savePayableBill(FormIDEnum.PAYABLE.getFormid(), dataForm);
                if (Objects.nonNull(commonResult) && commonResult.getCode() == ResultEnum.SUCCESS.getCode()) {
                    log.info("金蝶应付单推送完毕：({})", kingdeeCompName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }


        //收集futuretasks的回执
        try {
            executorService.shutdown();
            if (CollectionUtil.isNotEmpty(futureTasks) && !executorService.isTerminated())
                executorService.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (FutureTask futureTask : futureTasks) {
            try {
                errorString.append(futureTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.isNotEmpty(errorString.toString())) {
            log.error("应付数据推送出现异常：{}", errorString.toString());
            return false;
        } else {
            log.info("所有的应付数据推送完毕");
            return true;
        }

    }


    /**
     * 删除指定的报关单号的财务数据
     *
     * @param applyNo
     * @return
     */
    @Override
    public Map<String, List<String>> removeSpecifiedInvoice(String applyNo, Map<String, List<String>> ordersMap, InvoiceTypeEnum invoiceTypeEnum) {
        //校验订单是否存在
        Boolean removedAP = true;
        Boolean removedAR = true;
        Boolean removedAPOther = true;
        Boolean removedAROther = true;

        HashMap<String, List<String>> unremovables = new HashMap<>();
        if (Objects.equals(invoiceTypeEnum, InvoiceTypeEnum.RECEIVABLE) ||
                Objects.equals(invoiceTypeEnum, InvoiceTypeEnum.ALL)) {
            //处理应收
            List<String> receivable = MapUtil.get(ordersMap, "receivable", List.class);
            List<String> receivableOther = MapUtil.get(ordersMap, "receivableOther", List.class);
            if (CollectionUtil.isNotEmpty(receivable)) {
                List<String> unRemovableReceivable = doRemove(receivable, FormIDEnum.RECEIVABLE.getFormid());
                if (CollectionUtil.isNotEmpty(unRemovableReceivable)) {
                    unremovables.put("receivable", unRemovableReceivable);
                    removedAR = false;
                }
            }
            if (CollectionUtil.isNotEmpty(receivableOther)) {
                List<String> unremovableReceivableOther = doRemove(receivableOther, FormIDEnum.RECEIVABLE_OTHER.getFormid());
                if (CollectionUtil.isNotEmpty(unremovableReceivableOther)) {
                    unremovables.put("receivableOther", unremovableReceivableOther);
                    removedAROther = false;
                }
            }
        }
        if (Objects.equals(invoiceTypeEnum, InvoiceTypeEnum.PAYABLE) ||
                Objects.equals(invoiceTypeEnum, InvoiceTypeEnum.ALL)) {
            //处理应付
            List<String> payable = MapUtil.get(ordersMap, "payable", List.class);
            List<String> payableOther = MapUtil.get(ordersMap, "payableOther", List.class);
            if (CollectionUtil.isNotEmpty(payable)) {
                List<String> unremovablePayable = doRemove(payable, FormIDEnum.PAYABLE.getFormid());
                if (CollectionUtil.isNotEmpty(unremovablePayable)) {
                    unremovables.put("payable", unremovablePayable);
                    removedAR = false;
                }
            }
            if (CollectionUtil.isNotEmpty(payableOther)) {
                List<String> unremovablePayableOther = doRemove(payableOther, FormIDEnum.PAYABLE_OTHER.getFormid());
                if (CollectionUtil.isNotEmpty(unremovablePayableOther)) {
                    unremovables.put("payableOther", unremovablePayableOther);
                    removedAROther = false;
                }
            }
        }
        if (Objects.equals(invoiceTypeEnum, InvoiceTypeEnum.ALL) &&
                (!removedAP || !removedAR || !removedAPOther || !removedAROther)) {
            log.error(String.format("报关单号%s删除失败", applyNo));
            return null;
        }
        if (CollectionUtil.isNotEmpty(unremovables)) {
            return unremovables;
        } else {
            return null;
        }
    }


    /**
     * 实际处理删除应收单数据
     * <br>如果返回为空列表说明全部删除成功
     * <br>如果返回为非空列表，那么说明该客户或供应商的财务数据不能被删除，再推送时要进行忽略
     *
     * @param orderList
     * @return
     */
    private List<String> doRemove(List<String> orderList, String formId) {
        Map<String, CustomsFinanceCoRelation> coRelationMap = new HashMap<>();
        String compJson = redisUtils.get(yunbaoguanCompKeyRec);
        if (StringUtils.isNotEmpty(compJson)) {
            coRelationMap = (Map<String, CustomsFinanceCoRelation>) JSONObject.parseObject(compJson, Map.class);
        } else {
            List<CustomsFinanceCoRelation> list = coRelationService.list();
            for (CustomsFinanceCoRelation item : list) {
                if (!coRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                    coRelationMap.put(item.getYunbaoguanName(), item);
                }
            }
            redisUtils.set(yunbaoguanCompKeyRec, JSONUtil.toJsonStr(coRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        }

        if (CollectionUtil.isEmpty(coRelationMap)) {
            log.error("基础数据加载失败：费用或公司对应关系表加载失败");
            return new ArrayList<>();
        }

        Map<String, Object> header = new HashMap<>();
        header.put("Cookie", cookieService.getCookie(k3CloudConfig));

        Map<String, Object> param = new HashMap<>();
        param.put("Numbers", orderList);
        //拼装数据
        String jsonString = JSONUtil.toJsonStr(param);
        JSONObject data = JSONObject.parseObject(jsonString);
        String content = buildParam(formId, data);

        //向金蝶发送请求
        String result = KingdeeHttpUtil.httpPost(k3CloudConfig.getDelete(), header, content);
        List<String> failedOrders = KingdeeHttpUtil.ifSucceed(result, orderList);


        if (CollectionUtil.isNotEmpty(failedOrders)) {
            List<String> failedCompanies = new ArrayList<>();
            Map<String, Object> queryHeader = new HashMap<>();
            queryHeader.put("Cookie", cookieService.getCookie(k3CloudConfig));
            Map<String, Object> queryParam = new HashMap<>();
            for (String failedOrder : failedOrders) {
                queryParam.put("Number", failedOrder);
                String queryContent = buildParam(formId, JSONObject.parseObject(JSONObject.toJSONString(queryParam)));
                String queryResult = KingdeeHttpUtil.httpPost(k3CloudConfig.getView(), header, queryContent);
                if (null != queryResult) {
                    JSONObject resultObject = JSONObject.parseObject(queryResult);
                    Map<String, Object> innerMap = resultObject.getInnerMap();
                    Object rootResult = innerMap.get("Result");
                    if (null != rootResult) {
                        Object headResult = ((Map) rootResult).get("Result");
                        if (null != headResult) {
                            Map comp = null;
                            if (formId.equals(FormIDEnum.PAYABLE.getFormid())) {
                                comp = (Map) ((Map) headResult).get("SUPPLIERID");
                            } else if (formId.equals(FormIDEnum.RECEIVABLE.getFormid())) {
                                comp = (Map) ((Map) headResult).get("CUSTOMERID");
                            } else if (formId.equals(FormIDEnum.RECEIVABLE_OTHER) || formId.equals(FormIDEnum.PAYABLE_OTHER.getFormid())) {
                                comp = (Map) ((Map) headResult).get("CONTACTUNIT");
                            }
                            if (null != comp) {
                                List<Map> name = (List<Map>) ((Map) comp).get("Name");
                                if (CollectionUtil.isNotEmpty(name)) {

                                    if (null != name.get(0).get("Value")) {
                                        failedCompanies.add(name.get(0).get("Value").toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //如果有无法推送的数据，推送失败的公司名称转为云报关的名称，并返回
            if (CollectionUtil.isNotEmpty(failedCompanies)) {
                List<String> failedCompaniesYunbaoguan = new ArrayList<>();
                for (Map.Entry<String, CustomsFinanceCoRelation> coRelationEntry : coRelationMap.entrySet()) {
                    CustomsFinanceCoRelation coRelation = JSONObject.parseObject(JSONObject.toJSONString(coRelationEntry.getValue()), CustomsFinanceCoRelation.class);
                    String kingdeeName = coRelation.getKingdeeName();
                    if (failedCompanies.contains(kingdeeName)) {
                        failedCompaniesYunbaoguan.add(coRelationEntry.getKey());
                    }
                }
                return failedCompaniesYunbaoguan;
            }
        }
        return new ArrayList<>();
    }

    /**
     * 拼装实际请求的json数据
     *
     * @param formId
     * @param data
     * @return
     */
    private String buildParam(String formId, JSONObject data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formid", formId);
        jsonObject.put("data", data);
        String result = JSONUtil.toJsonStr(jsonObject);
        log.debug("请求内容：{}", result);
        return result;
    }


    /**
     * 拼装备注信息
     *
     * @return
     */
    private String getRemark() {
        return "";
//        return String.format("报关应收单测试%s", String.valueOf(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
    }


}

/**
 * 推送特殊应收单
 */
@Slf4j
class PushOtherReceivable implements Callable<String> {
    public CustomsReceivable customsReceivable;
    public Map<String, CustomsFinanceCoRelation> coRelationMap;
    public Map<String, CustomsFinanceFeeRelation> feeRelationMap;
    public CookieService cookieService;
    public K3CloudConfig k3CloudConfig;

    /**
     * 方法实现
     *
     * @param customsReceivable
     * @param coRelationMap
     * @param feeRelationMap
     * @return
     */
    public String doPush(CustomsReceivable customsReceivable,
                         Map<String, CustomsFinanceCoRelation> coRelationMap,
                         Map<String, CustomsFinanceFeeRelation> feeRelationMap) {
        log.debug("开始处理其他应收单据");
        JSONObject mainEntity = null;
        JSONObject detailEntity = null;

        StringBuilder errorString = new StringBuilder();
        try {
            mainEntity = FileUtil.getMapFromJsonFile("json/OtherReceivableModel.json");
            detailEntity = FileUtil.getMapFromJsonFile("json/OtherReceivableDetailModel.json");
        } catch (Exception e) {
            e.printStackTrace();
            errorString.append("读取模板文件失败;");
            log.error("读取模板文件失败");
        }

        String applyNo = customsReceivable.getCustomApplyNo();
        String customerName = customsReceivable.getCustomerName();

        if (Objects.nonNull(mainEntity) && Objects.nonNull(detailEntity)) {


            //开始塞入数据
            JSONObject root = (JSONObject) mainEntity.get("Model");
            if (Objects.nonNull(root)) {
                Class<? extends CustomsReceivable> clz = customsReceivable.getClass();
                Field[] declaredFields = clz.getDeclaredFields();
                //明细数据
                List<JSONObject> details = new ArrayList<>();

                BigDecimal totalValue = BigDecimal.ZERO;

                for (Field field : declaredFields) {
                    HeadProperty headProperty = field.getAnnotation(HeadProperty.class);
                    IsFee feeProperty = field.getAnnotation(IsFee.class);
                    if (Objects.isNull(headProperty) && Objects.isNull(feeProperty)) {
                        continue;
                    }
                    //装载数据头
                    if (Objects.nonNull(headProperty)) {
                        //获取传参进入的数据
                        Method method = null;
                        try {
                            method = clz.getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        Object invoke = null;
                        if (null != method) {
                            try {
                                invoke = method.invoke(customsReceivable);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }

                        if (null == invoke) {
                            errorString.append("没有获取到数据" + field.getName());
                            continue;
                        }
                        //获取金蝶json的属性名
                        String propertyName = StringUtils
                                .isNotEmpty(headProperty.otherName())
                                ? headProperty.otherName()
                                : headProperty.name();
                        //判定是否需要对属性进行转码为金蝶的专有数据
                        String kingdeePropertyName = "";
                        String kingdeePropertyCode = "";
                        if (headProperty.needRelation() != InvoiceFormNeedRelationEnum.NONE) {
                            switch (headProperty.needRelation()) {
                                case COMPANY:
                                    if (coRelationMap.containsKey(invoke.toString())) {
                                        kingdeePropertyName = getCoRelation(invoke.toString()).getKingdeeName();
                                        kingdeePropertyCode = getCoRelation(invoke.toString()).getKingdeeCode();
                                    } else {
                                        errorString.append(String.format("没有找到 %s 对应的金蝶公司名称", invoke.toString()));
                                        log.debug(String.format("没有找到 %s 对应的金蝶公司名称", invoke.toString()));
                                    }
                                    break;
                                case FEE:
                                    if (coRelationMap.containsKey(invoke.toString())) {
                                        kingdeePropertyName = getFeeRelation(invoke.toString()).getKingdeeName();
                                        kingdeePropertyCode = getFeeRelation(invoke.toString()).getKingdeeCode();
                                    } else {
                                        errorString.append(String.format("没有找到 %s 对应的金蝶费用名称", invoke.toString()));
                                        log.debug(String.format("没有找到 %s 对应的金蝶费用名称", invoke.toString()));
                                    }
                                    break;
                                case NONE:
                                    kingdeePropertyCode = invoke.toString();
                                    kingdeePropertyName = invoke.toString();
                                    break;
                                default:
                                    errorString.append("类型异常");
                                    break;
                            }
                            //转换完成后，判断是否需要打包写入FNumber
                            if (headProperty.wrap()) {
                                if (headProperty.toCode()) {
                                    root.put(propertyName, setWrapper(root.get(propertyName), kingdeePropertyCode));
                                } else {
                                    root.put(propertyName, setWrapper(root.get(propertyName), kingdeePropertyName));
                                }

                            } else {
                                root.put(propertyName, kingdeePropertyCode);
                            }
                        }
                    }
                    //装载明细
                    if (Objects.nonNull(feeProperty)) {
                        JSONObject entity = null;
                        try {
                            entity = FileUtil.getMapFromJsonFile("json/OtherReceivableDetailModel.json");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (Objects.isNull(entity)) {
                            log.error("读取其他应收模板文件失败");
                            errorString.append("读取其他应收模板文件失败;");
                        }
                        //获取数据
                        ApiModelProperty feeName = field.getAnnotation(ApiModelProperty.class);
                        if (Objects.nonNull(feeName)) {
                            if (feeRelationMap.containsKey(feeName.value()) && Objects.equals(getFeeRelation(feeName.value()).getCategory(), "代垫税金")) {
                                CustomsFinanceFeeRelation customsFinanceFeeRelation = getFeeRelation(feeName.value());
                                entity.put("FCOSTID", setWrapper(entity.get("FCOSTID"), customsFinanceFeeRelation.getKingdeeCode()));
                                entity.put("F_JYD_Assistant", setWrapper(entity.get("F_JYD_Assistant"), customsFinanceFeeRelation.getCategoryCode()));
                                entity.put("F_JYD_Assistant1", setWrapper(entity.get("F_JYD_Assistant1"), customsFinanceFeeRelation.getTypeCode()));
                                entity.put("FCOMMENT", applyNo);
                                String fieldName = field.getName();
                                Method method = null;
                                try {
                                    method = clz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                }
                                Object invoke = null;
                                if (null != method) {
                                    try {
                                        invoke = method.invoke(customsReceivable);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (null != invoke && !Objects.equals(invoke.toString(), "0.00")) {
                                    BigDecimal cost = new BigDecimal(invoke.toString());
                                    entity.put("FTAXAMOUNTFOR", cost);
                                    entity.put("FAMOUNTFOR_D", cost);
                                    entity.put("FTAXAMOUNT_D", cost);
                                    totalValue = totalValue.add(cost);
                                } else {
                                    continue;
                                }
                                details.add(entity);
                            }
                        }
                    }
                }

                //将明细数据装入root
                root.put("FEntity", details);
                root.put("FAMOUNTFOR", totalValue);
                root.put("FTAXAMOUNT", totalValue);
                root.put("FAR_OtherRemarks", String.format("%s : %s", customerName, applyNo));
                //root装载完毕，更新main
                mainEntity.put("Model", root);

                //发送数据
                //调用Redis中的cookie
                Map<String, Object> header = new HashMap<>();
                header.put("Cookie", cookieService.getCookie(k3CloudConfig));
                JSONObject msgObject = new JSONObject();
                msgObject.put("formid", "AR_OtherRecAble");
                msgObject.put("data", mainEntity);
                String content = msgObject.toJSONString();
                log.debug("请求内容：{}", content);

                String result = KingdeeHttpUtil.httpPost(k3CloudConfig.getSave(), header, content);

                log.debug("保存结果：{}", result);
                JSONObject jsonObject = JSON.parseObject(result);

                Boolean succeed = KingdeeHttpUtil.ifSucceed(result);
                if (succeed) {
                    log.info(String.format("报关单号（%s）（%s）其他应收单保存成功", applyNo, customerName));
                } else {
                    log.error(String.format("报关单号（%s）（%s）其他应收单保存失败", applyNo, customerName));
                    errorString.append(String.format("报关单号（%s）（%s）其他应收单保存失败", applyNo, customerName));
                }
            }
        }

        return errorString.toString();
    }

    private JSONObject setWrapper(Object item, String value) {
        JSONObject jsonObject = (JSONObject) item;
        jsonObject.put("FNumber", value);
        return jsonObject;
    }

    private CustomsFinanceFeeRelation getFeeRelation(String key) {
        return JSONObject.parseObject(JSONObject.toJSONString(feeRelationMap.get(key)), CustomsFinanceFeeRelation.class);
    }

    private CustomsFinanceCoRelation getCoRelation(String key) {
        return JSONObject.parseObject(JSONObject.toJSONString(coRelationMap.get(key)), CustomsFinanceCoRelation.class);
    }

    @Override
    public String call() {
        return doPush(this.customsReceivable, this.coRelationMap, this.feeRelationMap);
    }
}

/**
 * 推送特殊应付单
 */
@Slf4j
class PushOtherPayable implements Callable<String> {
    public List<CustomsPayable> customsPayable;
    public Map<String, CustomsFinanceCoRelation> coRelationMap;
    public Map<String, CustomsFinanceFeeRelation> feeRelationMap;
    public CookieService cookieService;
    public K3CloudConfig k3CloudConfig;

    public String doPush(List<CustomsPayable> customsPayable,
                         Map<String, CustomsFinanceCoRelation> coRelationMap,
                         Map<String, CustomsFinanceFeeRelation> feeRelationMap) {
        log.debug("开始处理其他应付单据");
        JSONObject mainEntity = null;
        JSONObject detailEntity = null;

        StringBuilder errorString = new StringBuilder();
        try {
            mainEntity = FileUtil.getMapFromJsonFile("json/OtherPayableModel.json");
            detailEntity = FileUtil.getMapFromJsonFile("json/OtherPayableDetailModel.json");
        } catch (Exception e) {
            e.printStackTrace();
            errorString.append("读取模板文件失败;");
            log.error("读取模板文件失败");
        }
        if (Objects.nonNull(mainEntity) && Objects.nonNull(detailEntity)) {
            //开始塞入数据
            JSONObject root = (JSONObject) mainEntity.get("Model");
            if (Objects.nonNull(root) && CollectionUtil.isNotEmpty(customsPayable)) {
                String applyNo = customsPayable.get(0).getCustomApplyNo();
                String customerName = customsPayable.get(0).getCustomerName();
                //其他应付表头只对合计和备注进行处理
                //组织默认使用佳裕达报关
                //来往单位类型默认为供应商-海关
                BigDecimal totalValue = BigDecimal.ZERO;
                List<JSONObject> details = new ArrayList<>();
                //装入明细
                for (CustomsPayable payable : customsPayable) {
                    JSONObject entity = null;
                    try {
                        entity = FileUtil.getMapFromJsonFile("json/OtherPayableDetailModel.json");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (null != entity) {
                        if (feeRelationMap.containsKey(payable.getFeeName())) {
                            CustomsFinanceFeeRelation feeRelation = getFeeRelation(payable.getFeeName());
                            //明细赋值
                            entity.put("FCOSTID", setWrapper(entity.get("FCOSTID"), feeRelation.getKingdeeCode()));
                            entity.put("F_JYD_Assistant", setWrapper(entity.get("F_JYD_Assistant"), feeRelation.getCategoryCode()));
                            entity.put("F_JYD_Assistant1", setWrapper(entity.get("F_JYD_Assistant1"), feeRelation.getTypeCode()));
                            entity.put("FCOMMENT", applyNo);

                            if (Objects.equals(payable.getCost(), "0.00")) {
                                continue;
                            }
                            BigDecimal price = new BigDecimal(payable.getCost());
                            entity.put("FNOTAXAMOUNTFOR", price);
                            entity.put("FTOTALAMOUNTFOR", price);
                            entity.put("FNOTSETTLEAMOUNTFOR_D", price);
                            entity.put("FNOTAXAMOUNT_D", price);
                            totalValue = totalValue.add(price);
                            details.add(entity);
                        } else {
                            String feeGetError = String.format("尝试获取费用名称%s失败", payable.getFeeName());
                            log.error(feeGetError);
                            errorString.append(feeGetError + ";");
                        }
                    } else {
                        log.error("读取其他应付明细模板失败");
                        errorString.append("读取其他应付明细模板失败;");
                    }
                }
                //将明细数据装入root
                root.put("FEntity", details);
                root.put("FTOTALAMOUNTFOR_H", totalValue);
                root.put("FNOTSETTLEAMOUNTFOR", totalValue);
                root.put("FNOTAXAMOUNT", totalValue);
                root.put("FRemarks", String.format("%s ：%s", customerName, applyNo));
                //root装载完毕，更新main
                mainEntity.put("Model", root);

                //发送数据
                //调用Redis中的cookie
                Map<String, Object> header = new HashMap<>();
                header.put("Cookie", cookieService.getCookie(k3CloudConfig));
                JSONObject msgObject = new JSONObject();
                msgObject.put("formid", "AP_OtherPayable");
                msgObject.put("data", mainEntity);
                String content = msgObject.toJSONString();
                log.debug("请求内容：{}", content);

                String result = KingdeeHttpUtil.httpPost(k3CloudConfig.getSave(), header, content);

                log.debug("保存结果：{}", result);
                JSONObject jsonObject = JSON.parseObject(result);

                Boolean succeed = KingdeeHttpUtil.ifSucceed(result);
                if (succeed) {
                    log.info(String.format("报关单号（%s）其他应收单保存成功", applyNo));
                } else {
                    log.error(String.format("报关单号（%s）其他应收单保存失败", applyNo));
                    errorString.append(String.format("报关单号（%s）其他应收单保存失败", applyNo));
                }
                return errorString.toString();
            }
        } else {
            errorString.append("读取模板文件失败;");
            log.error("读取模板文件失败");
        }
        return "";
    }

    private JSONObject setWrapper(Object item, String value) {
        JSONObject jsonObject = (JSONObject) item;
        jsonObject.put("FNumber", value);
        return jsonObject;
    }


    private CustomsFinanceFeeRelation getFeeRelation(String key) {
        return JSONObject.parseObject(JSONObject.toJSONString(feeRelationMap.get(key)), CustomsFinanceFeeRelation.class);
    }

    private CustomsFinanceCoRelation getCoRelation(String key) {
        return JSONObject.parseObject(JSONObject.toJSONString(coRelationMap.get(key)), CustomsFinanceCoRelation.class);
    }

    @Override
    public String call() {
        return doPush(this.customsPayable, this.coRelationMap, this.feeRelationMap);
    }
}

