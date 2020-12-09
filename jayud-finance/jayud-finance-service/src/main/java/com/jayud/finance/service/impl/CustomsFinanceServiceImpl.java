package com.jayud.finance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
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
import com.jayud.finance.kingdeesettings.K3CloudConfig;
import com.jayud.finance.po.*;
import com.jayud.finance.service.*;
import com.jayud.finance.util.FileUtil;
import com.jayud.finance.util.KingdeeHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@RefreshScope
public class CustomsFinanceServiceImpl implements CustomsFinanceService {
    @Autowired
    RedisUtils redisUtils;

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
    @Autowired
    PreloadService preloadService;

    @Override
    public Boolean pushReceivable(List<CustomsReceivable> customsReceivable, YunbaoguanPushProperties properties) {

        Map<String, CustomsFinanceCoRelation> coRelationMap = preloadService.getCompanyRelationMap();
        //代码逻辑修改后，此处将原有的name-entity的map转为code-entiry的map进而进行比对
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = preloadService.getFeeRelationMap()
                .values()
                .stream().collect(Collectors.toMap(e -> e.getYunbaoguanCode(), e -> e));

        if (CollectionUtil.isEmpty(feeRelationMap) || CollectionUtil.isEmpty(coRelationMap)) {
            return false;
        }

        //完成预加载数据后判断是否有需要剔除无法重送的数据
        Map<FormIDEnum, List<String>> unremovables = properties.getUnRemovableCompName();
        List<String> unremovableReceivableOther = null;
        List<String> unremovableReceivable = null;
        if (CollectionUtil.isNotEmpty(unremovables)) {
            unremovableReceivableOther = unremovables.get(FormIDEnum.RECEIVABLE_OTHER);
            unremovableReceivable = unremovables.get(FormIDEnum.RECEIVABLE);
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

            for (Map.Entry<String, Object> entry : item.getOriginData().getInnerMap().entrySet()) {

                //直接将entry跟费用项表比对,如果费用关系表没有此code，且该项金额为零，跳过此项可继续
                if (!feeRelationMap.containsKey(entry.getKey()) || Objects.equals("0.00", entry.getValue().toString())) {
                    continue;
                }

                CustomsFinanceFeeRelation customsFinanceFeeRelation = getRelationMapItem(entry.getKey(), feeRelationMap, CustomsFinanceFeeRelation.class);
                if (Objects.isNull(customsFinanceFeeRelation)) {
                    //费用无法对应金蝶,记录
                    errorString.append(String.format("云报关费用项%s无法匹配金蝶费用项;", entry.getKey()));
                    log.error(String.format("云报关费用项%s无法匹配金蝶费用项;", entry.getKey()));
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
                    if (!Objects.equals("0.00", entry.getValue().toString())) {
                        hasOtherReceivable = true;
                    }
                    continue;
                } else {
                    feeItem.setTaxRate(new BigDecimal(customsFinanceFeeRelation.getTaxRate()));
                }
                feeItem.setTaxPrice(new BigDecimal(entry.getValue().toString()));
                list.add(feeItem);

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
                customsFinanceCoRelation = getRelationMapItem(item.getCustomerName(), coRelationMap, CustomsFinanceCoRelation.class);
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
                } else {
                    log.error("金蝶应收单推送失败：（{}）", commonResult.getData());
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
            log.warn(String.format("写入应收单的时候出现异常：{}"), errorString.toString());
            return true;
        }


        log.info(String.format("所有的应收数据推送完毕"));
        return true;
    }

    @Override
    public Boolean pushPayable(List<CustomsPayable> customsPayableForms, YunbaoguanPushProperties properties) {
        Map<String, CustomsFinanceCoRelation> coRelationMap = preloadService.getCompanyRelationMap();
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = preloadService.getFeeRelationMap();


        //判断预加载数据是否成功
        if (CollectionUtil.isEmpty(feeRelationMap) || CollectionUtil.isEmpty(coRelationMap)) {
            return false;
        }

        //如果有删除失败的已存在订单，将不推送此条数据
        //todo 应付 与 其他应付要分开处理（应付中哪些不能传，其他应付中哪些不能传）
        List<String> unremovablePayable = null;
        List<String> unremovablePayableOther = null;
        Map<FormIDEnum, List<String>> unremovables = properties.getUnRemovableCompName();
        if (CollectionUtil.isNotEmpty(unremovables)) {
            unremovablePayable = unremovables.get(FormIDEnum.PAYABLE);
            unremovablePayableOther = unremovables.get(FormIDEnum.PAYABLE_OTHER);
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
                companyProfile = getRelationMapItem(customsPayable.getTargetName(), coRelationMap, CustomsFinanceCoRelation.class);
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
//                    customsFeeItem = JSONObject.parseObject(JSONUtil.toJsonStr(feeRelationMap.get(payableForm.getFeeName())), CustomsFinanceFeeRelation.class);
                    customsFeeItem = getRelationMapItem(payableForm.getFeeName(), feeRelationMap, CustomsFinanceFeeRelation.class);
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
                item.setTaxPrice(new BigDecimal(payableForm.getCost()));
                item.setTaxRate(BigDecimal.ZERO);
                list.add(item);
            }
            dataForm.setEntityDetail(list);

            //调用保存应付单接口
            try {
                log.info("调用保存应付单接口...");
                CommonResult commonResult = kingdeeService.savePayableBill(FormIDEnum.PAYABLE.getFormid(), dataForm);
                if (Objects.nonNull(commonResult) && commonResult.getCode() == ResultEnum.SUCCESS.getCode()) {
                    log.info("金蝶应付单推送完毕：({})", kingdeeCompName);
                } else {
                    log.error("金蝶应付单推送失败：（{}）", commonResult.getData());
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
            log.warn("应付数据推送可能出现异常：{}", errorString.toString());
            return true;
        } else {
            log.info("所有的应付数据推送完毕");
            return true;
        }

    }


    /**
     * 删除指定的报关单号的财务数据
     *
     * @return
     */
    @Override
    public YunbaoguanPushProperties removeSpecifiedInvoice(YunbaoguanPushProperties properties) {
        return doRemove(properties);
    }


    /**
     * 实际处理删除应收单数据
     * <br>如果返回为空列表说明全部删除成功
     * <br>如果返回为非空列表，那么说明该客户或供应商的财务数据不能被删除，再推送时要进行忽略
     *
     * @return
     */
    private YunbaoguanPushProperties doRemove(YunbaoguanPushProperties properties) {
        Map<String, CustomsFinanceCoRelation> coRelationMap = preloadService.getCompanyRelationMap();
        if (CollectionUtil.isEmpty(coRelationMap)) {
            properties.setError(true);
            return properties;
        }

        //基础数据加载完成，开始处理
        Map<FormIDEnum, List<String>> existingOrders = properties.getExistingOrders();
        for (Map.Entry<FormIDEnum, List<String>> existingEntries : existingOrders.entrySet()) {
            List<String> orderList = existingEntries.getValue();
            String formId = existingEntries.getKey().getFormid();
            if (CollectionUtil.isNotEmpty(orderList)) {
                Map<String, Object> header = new HashMap<>();
                header.put("Cookie", cookieService.getCookie(k3CloudConfig));

                Map<String, Object> param = new HashMap<>();
                param.put("Numbers", orderList);
                //拼装数据
                String jsonString = JSONUtil.toJsonStr(param);
                JSONObject data = JSONObject.parseObject(jsonString);
                String content = buildParam(formId, data);

                //向金蝶发送请求，尝试删除订单
                String result = KingdeeHttpUtil.httpPost(k3CloudConfig.getDelete(), header, content);
                log.debug("删除订单尝试：{}", result);
                //如果推送失败，返回推送失败的订单号列表
                List<String> failedOrders = KingdeeHttpUtil.ifSucceed(result, orderList);

                //装载删除失败的订单列表
                if (CollectionUtil.isNotEmpty(properties.getUnRemovableOrders())) {
                    properties.getUnRemovableOrders().put(existingEntries.getKey(), failedOrders);
                } else {
                    Map<FormIDEnum, List<String>> failedOrdersMap = new HashMap<>();
                    failedOrdersMap.put(existingEntries.getKey(), failedOrders);
                    properties.setUnRemovableOrders(failedOrdersMap);
                }


                //获取推送失败的订单对应的商户抬头
                List<String> failedCompanies = new ArrayList<>();

                if (CollectionUtil.isNotEmpty(failedOrders)) {
                    Map<String, Object> queryHeader = new HashMap<>();
                    queryHeader.put("Cookie", cookieService.getCookie(k3CloudConfig));
                    Map<String, Object> queryParam = new HashMap<>();
                    for (String failedOrder : failedOrders) {
                        queryParam.put("Number", failedOrder);
                        String queryContent = buildParam(formId, JSONObject.parseObject(JSONObject.toJSONString(queryParam)));
                        String queryResult = KingdeeHttpUtil.httpPost(k3CloudConfig.getView(), header, queryContent);
                        log.debug("查询订单明细结果：{}", queryResult);
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
                                    } else if (formId.equals(FormIDEnum.RECEIVABLE_OTHER.getFormid()) || formId.equals(FormIDEnum.PAYABLE_OTHER.getFormid())) {
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

                    //如果上面的抬头列表不为空，推送失败的公司名称在此转为对应云报关的名称
                    if (CollectionUtil.isNotEmpty(failedCompanies)) {
                        List<String> failedCompaniesYunbaoguan = new ArrayList<>();
                        for (Map.Entry<String, CustomsFinanceCoRelation> coRelationEntry : coRelationMap.entrySet()) {
                            CustomsFinanceCoRelation coRelation = JSONObject.parseObject(JSONObject.toJSONString(coRelationEntry.getValue()), CustomsFinanceCoRelation.class);
                            String kingdeeName = coRelation.getKingdeeName();
                            if (failedCompanies.contains(kingdeeName)) {
                                failedCompaniesYunbaoguan.add(coRelationEntry.getKey());
                            }
                        }
                        if (CollectionUtil.isNotEmpty(properties.getUnRemovableCompName())) {
                            properties.getUnRemovableCompName().put(existingEntries.getKey(), failedCompaniesYunbaoguan);
                        } else {
                            Map<FormIDEnum, List<String>> unRemovableComps = new HashMap<>();
                            unRemovableComps.put(existingEntries.getKey(), failedCompaniesYunbaoguan);
                            properties.setUnRemovableCompName(unRemovableComps);
                        }
                    }
                }

            }
        }
        return properties;

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

    private <T> T getRelationMapItem(String key, Map<String, T> sourceMap, Class<T> returnType) {
        String jsonStr = JSONUtil.toJsonStr(sourceMap.get(key));
        T result = JSONObject.parseObject(jsonStr, returnType);
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
        String fdate = customsReceivable.getApplyDt();

        if (Objects.nonNull(mainEntity) && Objects.nonNull(detailEntity)) {


            //开始塞入数据
            JSONObject root = (JSONObject) mainEntity.get("Model");
            if (Objects.nonNull(root)) {
                Class<? extends CustomsReceivable> clz = customsReceivable.getClass();
                Field[] declaredFields = clz.getDeclaredFields();
                //明细数据
                List<JSONObject> details = new ArrayList<>();

                BigDecimal totalValue = BigDecimal.ZERO;


                //装载数据头
                for (Field field : declaredFields) {
                    HeadProperty headProperty = field.getAnnotation(HeadProperty.class);
                    IsFee feeProperty = field.getAnnotation(IsFee.class);
                    if (Objects.isNull(headProperty) && Objects.isNull(feeProperty)) {
                        continue;
                    }
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
                                        Optional<String> cust = Arrays.stream(getCoRelation(invoke.toString())
                                                .getKingdeeCode()
                                                .split("\\|")).filter(e -> e.toLowerCase().contains("cust")).findFirst();
                                        kingdeePropertyCode = cust.isPresent() ? cust.get() : "";
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
                }


                //装载明细
                for (Map.Entry<String, Object> entry : customsReceivable.getOriginData().getInnerMap().entrySet()) {
                    String code = entry.getKey();

                    //判定是否跳过此次
                    if (!feeRelationMap.containsKey(code) || entry.getValue().equals("0.00")) {
                        continue;
                    }

                    //获取数据模板
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
                    if (Objects.nonNull(code)) {
                        if (feeRelationMap.containsKey(code) && Objects.equals(getFeeRelation(code).getCategory(), "代垫税金")) {
                            CustomsFinanceFeeRelation customsFinanceFeeRelation = getFeeRelation(code);
                            entity.put("FCOSTID", setWrapper(entity.get("FCOSTID"), customsFinanceFeeRelation.getKingdeeCode()));
                            entity.put("F_JYD_Assistant", setWrapper(entity.get("F_JYD_Assistant"), customsFinanceFeeRelation.getCategoryCode()));
                            entity.put("F_JYD_Assistant1", setWrapper(entity.get("F_JYD_Assistant1"), customsFinanceFeeRelation.getTypeCode()));
                            entity.put("FCOMMENT", applyNo);


                            if (StringUtils.isNotEmpty(entry.getValue().toString()) && !Objects.equals(entry.getValue().toString(), "0.00")) {
                                BigDecimal cost = new BigDecimal(entry.getValue().toString());
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


                //将明细数据装入root
                root.put("FEntity", details);
                root.put("FAMOUNTFOR", totalValue);
                root.put("FTAXAMOUNT", totalValue);
                root.put("FAR_OtherRemarks", customerName);
                root.put("F_PCQE_Text", applyNo);
                root.put("FDATE", fdate);
                root.put("FACCNTTIMEJUDGETIME", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
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
                String fdate = customsPayable.get(0).getApplyDt();
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
                root.put("FRemarks", customerName);
                root.put("F_PCQE_Text", applyNo);
                root.put("FDATE",fdate);
                root.put("FACCNTTIMEJUDGETIME", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
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
                    log.info(String.format("报关单号（%s）其他应付单保存成功", applyNo));
                } else {
                    log.error(String.format("报关单号（%s）其他应付单保存失败", applyNo));
                    errorString.append(String.format("报关单号（%s）其他应付单保存失败", applyNo));
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

