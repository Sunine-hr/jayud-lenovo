package com.jayud.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.jayud.bo.APARDetailForm;
import com.jayud.bo.PayableHeaderForm;
import com.jayud.bo.ReceivableHeaderForm;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.vaildator.ValidList;
import com.jayud.enums.FormIDEnum;
import com.jayud.kingdeesettings.K3CloudConfig;
import com.jayud.kingdeesettings.K3CloudConfigBack;
import com.jayud.po.Currency;
import com.jayud.po.*;
import com.jayud.service.BaseService;
import com.jayud.service.CookieService;
import com.jayud.service.KingdeeService;
import com.jayud.util.FileUtil;
import com.jayud.util.KingdeeHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.jayud.enums.KingdeeResultEnum.LOGIN_FAIL;


/**
 * 金蝶物料接口实现类
 */
@Service
@Slf4j
public class KingdeeServiceImpl implements KingdeeService {

    @Autowired
    private K3CloudConfig k3CloundConfig;
    /**
     * 仅查询时使用，在默认账套查询失败时使用
     */
    @Autowired
    private K3CloudConfigBack k3CloudConfigBack;

    @Autowired
    CookieService cookieService;

    @Autowired
    BaseService baseService;

    @Override
    public CommonResult login(String url, String content) {

        ResponseEntity<String> responseEntity = KingdeeHttpUtil.httpPost(url, content);
        //获取登录cookie
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String loginCookie = "";
            Set<String> keys = responseEntity.getHeaders().keySet();
            for (String key : keys) {
                if ("Set-Cookie".equalsIgnoreCase(key)) {
                    List<String> cookies = responseEntity.getHeaders().get(key);
                    for (String cookie : cookies) {
                        if (cookie.startsWith("kdservice-sessionid")) {
                            loginCookie = cookie;
                            break;
                        }
                    }
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("cookie", loginCookie);
            return CommonResult.success(map);
        }

        Map<String, Object> result = (Map) JSONUtil.toBean(responseEntity.getBody().toString(), Map.class);
        return CommonResult.error(ResultEnum.LOGIN_FAIL, LOGIN_FAIL.getMessage());
    }


    @Override
    public CommonResult view(String formId, Map<String, Object> data) {
        //调用Redis中的cookie
        Map<String, Object> header = new HashMap<>();
        header.put("Cookie", cookieService.getCookie(k3CloundConfig));
        String result = KingdeeHttpUtil.httpPost(k3CloundConfig.getView(), header, buildParam(formId, JSONObject.parseObject(JSONUtil.toJsonStr(data))));
        return CommonResult.success(result);
    }

    @Override
    public CommonResult saveReceivableBill(String formId, ReceivableHeaderForm reqForm) {
        //调用Redis中的cookie
        Map<String, Object> header = new HashMap<>();
        header.put("Cookie", cookieService.getCookie(k3CloundConfig));
        String content = buildParam(formId, constructReceivableBill(reqForm));

        log.info("请求内容：{}", content);

        String result = KingdeeHttpUtil.httpPost(k3CloundConfig.getSave(), header, content);

        log.info("保存结果：{}", result);
        JSONObject jsonObject = JSON.parseObject(result);
        //获取金蝶响应json
        Map<String, Object> map = (Map<String, Object>) jsonObject.get("Result");
        //获取金蝶响应json下的responseStatus字段
        Map<String, Object> responseStatus = (Map<String, Object>) map.get("ResponseStatus");

        Boolean isSuccess = (Boolean) responseStatus.get("IsSuccess");
        if (isSuccess) {
            return CommonResult.success(map.get("Number"));
        } else {
            List<Map<String, Object>> errors = (List<Map<String, Object>>) responseStatus.get("Errors");
            return CommonResult.error(-1, "errors");
        }

    }


    @Override
    public CommonResult savePayableBill(String formId, PayableHeaderForm reqForm) {
        //调用Redis中的cookie
        Map<String, Object> header = new HashMap<>();
        header.put("Cookie", cookieService.getCookie(k3CloundConfig));
        String content = buildParam(formId, constructPayableBill(reqForm));
        log.info("请求内容：{}", content);

        String result = KingdeeHttpUtil.httpPost(k3CloundConfig.getSave(), header, content);

        log.info("保存结果：{}", result);
        JSONObject jsonObject = JSON.parseObject(result);
        Map<String, Object> map = (Map<String, Object>) jsonObject.get("Result");
        Map<String, Object> responseStatus = (Map<String, Object>) map.get("ResponseStatus");
        Boolean isSuccess = (Boolean) responseStatus.get("IsSuccess");
        if (isSuccess) {
            return CommonResult.success(map.get("Number"));
        } else {
            List<Map<String, Object>> errors = (List<Map<String, Object>>) responseStatus.get("Errors");
            return CommonResult.error(-1, "errors");
        }


    }

    @Override
    @Deprecated
    public CommonResult save(String formId, JSONObject data) {
        //调用Redis中的cookie
        Map<String, Object> header = new HashMap<>();
        header.put("Cookie", cookieService.getCookie(k3CloundConfig));

        String content = buildParam(formId, data);
        log.info("请求内容：{}", content);

        String result = KingdeeHttpUtil.httpPost(k3CloundConfig.getSave(), header, content);

        log.info("保存结果：{}", result);
        //String result = KingdeeHttpUtil.httpPost(url, header, content);
        JSONObject jsonObject = JSON.parseObject(result);
        Map<String, Object> map = (Map<String, Object>) jsonObject.get("Result");
        Map<String, Object> responseStatus = (Map<String, Object>) map.get("ResponseStatus");
        Boolean isSuccess = (Boolean) responseStatus.get("IsSuccess");
        if (isSuccess) {
            return CommonResult.success();
        } else {
            List<Map<String, Object>> errors = (List<Map<String, Object>>) responseStatus.get("Errors");
            return CommonResult.error(-1, "errors");
        }
    }


    /**
     * 列表查询数据
     *
     * @param filterString 过滤条件
     * @param clz
     * @param k3Entity
     * @return
     */
    @Override
    public <T> CommonResult<List<T>> query(String filterString, Class<T> clz, K3Entity k3Entity) {

        String[] fieldKeys = k3Entity.value();

        String formId = k3Entity.formId().getFormid();

        //1.data：JSON格式数据（详情参考JSON格式数据）（必录）
        JSONObject data = new JSONObject();
        //1.1.FormId：业务对象表单Id（必录）
        data.put("FormId", formId);
        //1.2.FieldKeys：需查询的字段key集合，字符串类型，格式："key1,key2,..."（必录） 注（查询单据体内码,需加单据体Key和下划线,如：FEntryKey_FEntryId）
        data.put("FieldKeys", String.join(",", fieldKeys));
        //1.3.FilterString：过滤条件，字符串类型（非必录）
        data.put("FilterString", filterString);
//        1.4.OrderString：排序字段，字符串类型（非必录）
//        1.5.TopRowCount：返回总行数，整型（非必录）
//        1.6.StartRow：开始行索引，整型（非必录）
//        1.7.Limit：最大行数，整型，不能超过2000（非必录）
        //调用Redis中的cookie
        Map<String, Object> header = new HashMap<>();
        header.put("Cookie", cookieService.getCookie(k3CloundConfig));
        String content = buildParam(formId, data);
        log.info("请求内容：{}", content);
        String result = KingdeeHttpUtil.httpPost(k3CloundConfig.getQuery(), header, content);
        log.info("默认账套查询结果：{}", result);
        if ("[]".equals(result)) {
            header = new HashMap<>();

            header.put("Cookie", cookieService.getCookie(k3CloudConfigBack));
            content = buildParam(formId, data);
            log.info("请求内容：{}", content);
            result = KingdeeHttpUtil.httpPost(k3CloudConfigBack.getQuery(), header, content);
            log.info("默认账套查询结果：{}", result);
        }

        if (result.contains("IsSuccess")) {
            List<List<Object>> list = (List<List<Object>>) JSONObject.parse(result);
            JSONObject jsonObject = JSON.parseObject(list.get(0).get(0).toString());
            Map<String, Object> map = (Map<String, Object>) jsonObject.get("Result");
            Map<String, Object> responseStatus = (Map<String, Object>) map.get("ResponseStatus");
            Boolean isSuccess = (Boolean) responseStatus.get("IsSuccess");
            if (isSuccess) {
                return CommonResult.success();
            } else {
                List<Map<String, Object>> errors = (List<Map<String, Object>>) responseStatus.get("Errors");
                return CommonResult.error(-1, "errors");
            }
        }
        List<T> list = JSONUtil.toList(format(result, fieldKeys), clz);
        return CommonResult.success(list);
    }

    @Override
    public <T> CommonResult<T> getInvoice(String BusinessCode, Class<T> clz) {
        if (clz.isAssignableFrom(InvoiceBase.class)) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "请求实体类型不正确");
        }
        //1. 获取应收或应付单单号
        List<InvoiceBase> idLists = (List<InvoiceBase>) baseService.query(BusinessCode, clz);
        if (idLists.isEmpty()) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "没有查询到数据");
        }

        //账单ID表
        List<String> orderIds = idLists.stream().map(id -> {
            return id.getFBillNo();
        }).collect(Collectors.toList());

        //每一个ID对应的查询返回数据存放于此
        List<InvoiceOutputBase> infoLists = new ArrayList<>();
        InvoiceOutputBase outputResult = null;
        //遍历ID表，获取所有信息
        for (String orderId : orderIds) {
            //2. 获取表单ID
            K3Entity k3Entity = clz.getAnnotation(K3Entity.class);
            String formId = k3Entity.formId().getFormid();

            //3. 获取请求头
            Map<String, Object> header = new HashMap<>();
            header.put("Cookie", cookieService.getCookie(k3CloundConfig));

            //4. 请求并获取回执信息
            Map<String, Object> param = new HashMap<>(3);
            param.put("CreateOrgId", 0);
            param.put("Number", orderId);
            param.put("Id", "");

            String result = KingdeeHttpUtil.httpPost(k3CloundConfig.getView(), header, buildParam(formId, JSONObject.parseObject(JSONUtil.toJsonStr(param))));

            log.info("回执数据为：" + result);

            //5.解析回执
            JSONObject jsonObject = JSON.parseObject(result);

            Map<String, Object> jsonObjectInnerMap = jsonObject.getInnerMap();

            Map ContainerMap = getObject(jsonObjectInnerMap, Map.class, "Result");

            Map responseStatus = getObject(ContainerMap, Map.class, "ResponseStatus");
            //如果没有获取成功，取用备用配置查询
            if (!Objects.isNull(responseStatus) && Objects.equals(responseStatus.get("IsSuccess"), false)) {
                header = new HashMap<>();
                header.put("Cookie", cookieService.getCookie(k3CloudConfigBack));

                //4. 请求并获取回执信息
                param = new HashMap<>(3);
                param.put("CreateOrgId", 0);
                param.put("Number", orderId);
                param.put("Id", "");
                result = KingdeeHttpUtil.httpPost(k3CloundConfig.getView(), header, buildParam(formId, JSONObject.parseObject(JSONUtil.toJsonStr(param))));
                log.info("回执数据为：" + result);
                //5.解析回执
                jsonObject = JSON.parseObject(result);
                jsonObjectInnerMap = jsonObject.getInnerMap();
                ContainerMap = getObject(jsonObjectInnerMap, Map.class, "Result");
                responseStatus = getObject(ContainerMap, Map.class, "ResponseStatus");
                if (!Objects.isNull(responseStatus)) {
                    Asserts.fail(ResultEnum.PARAM_ERROR, "备用资源也没有找到相关的数据，请检查金蝶数据库中是否有此数据");
                }
            }
            //根节点
            Map rootMap = getObject(ContainerMap, Map.class, "Result");


            //6. 数据拼装

            if (FormIDEnum.RECEIVABLE.getFormid().equals(k3Entity.formId().getFormid())) {

                //应收单
                if ((Objects.isNull(outputResult))) {
                    outputResult = new ReceivableOutputDTO();
                }
                generateReceivableInvoice(rootMap, outputResult);

            } else if (FormIDEnum.PAYABLE.getFormid().equals(k3Entity.formId().getFormid())) {
                //应付单
                if ((Objects.isNull(outputResult))) {
                    outputResult = new PayableOutputDTO();
                }
                generatePayableInvoice(rootMap, outputResult);
            }
            infoLists.add(outputResult);
        }


        //7. 返回数据
        return (CommonResult<T>) CommonResult.success(outputResult);
    }

    /**
     * 组装应付单输出数据
     *
     * @param rootMap
     * @return
     */
    private InvoiceOutputBase generatePayableInvoice(Map rootMap, InvoiceOutputBase outputResult) {
        //组织 SETTLEORGID
        outputResult.setSettleOrgName(getInfoFromMap(rootMap, "SETTLEORGID"));
        //采购部门 PURCHASEDEPTID
        outputResult.setDeptName(getInfoFromMap(rootMap, "PURCHASEDEPTID"));
        //业务单号 REMARK
        outputResult.setBusinessNo(getObject(rootMap, String.class, "F_JYD_Text"));
        //明细
        if (Objects.isNull(outputResult.getDetail())) {
            outputResult.setDetail(new ArrayList());
        }
        getInvoiceDetail(rootMap, outputResult);

        return outputResult;
    }

    /**
     * 组装应收单输出数据
     *
     * @param rootMap
     * @return
     */
    private InvoiceOutputBase generateReceivableInvoice(Map rootMap, InvoiceOutputBase outputResult) {
        //组织 SETTLEORGID
        outputResult.setSettleOrgName(getInfoFromMap(rootMap, "SETTLEORGID"));
        //销售部门 SALEDEPTID
        outputResult.setDeptName(getInfoFromMap(rootMap, "SALEDEPTID"));
        //业务单号 REMARK
        outputResult.setBusinessNo(getObject(rootMap, String.class, "REMARK"));
        //明细
        if (Objects.isNull(outputResult.getDetail())) {
            outputResult.setDetail(new ArrayList());
        }
        getInvoiceDetail(rootMap, outputResult);

        return outputResult;
    }

    private void getInvoiceDetail(Map<String, Object> rootMap, InvoiceOutputBase invoiceOutputBase) {
        List<APARDetailDTO> details = invoiceOutputBase.getDetail();
        //获得列表信息
        List ap_payableentry = getObject(rootMap, List.class, "AP_PAYABLEENTRY");
        for (Object o : ap_payableentry) {
            APARDetailDTO detail = new APARDetailDTO();

            //获取根Map对象
            Map<String, Object> listMap = JSON.parseObject(o.toString());
            //公司名称（客户/供应商）
            String checkCompany = getInfoFromMap(rootMap, "CUSTOMERID");
            detail.setCompanyName(Objects.isNull(checkCompany) ? getInfoFromMap(rootMap, "SUPPLIERID") : checkCompany);
            //账单号
            detail.setBillNo(getObject(rootMap, String.class, "F_PCQE_Text"));
            //备注
            String checkRemark = getObject(rootMap, String.class, "F_JYD_Remarks");
            detail.setRemark(Objects.isNull(checkRemark) ? getObject(rootMap, String.class, "REMARK") : checkRemark);
            //费用项名称
            detail.setExpenseName(getInfoFromMap(listMap, "MATERIALID"));
            //币别
            detail.setCurrency(getInfoFromMap(rootMap, "CURRENCYID"));
            //费用类别
            detail.setExpenseCategoryName(getInfoFromMapByKeys(listMap, String.class, new String[]{"F_JYD_Assistant", "FDataValue", "Value"}));
            //费用类型
            detail.setExpenseTypeName(getInfoFromMapByKeys(listMap, String.class, new String[]{"F_JYD_Assistant1", "FDataValue", "Value"}));
            //计价数量
            detail.setPriceQty((BigDecimal) listMap.get("PriceQty"));
            //含税单价
            detail.setTaxPrice((BigDecimal) listMap.get("TaxPrice"));
            //税率
            detail.setTaxRate((BigDecimal) listMap.get("EntryTaxRate"));
            //不含税单价
            detail.setNoTaxPrice((BigDecimal) listMap.get("FPrice"));
            //总税额
            detail.setTaxTotal((BigDecimal) listMap.get("FTAXAMOUNTFOR_D"));
            //含税总价
            detail.setTaxAmount((BigDecimal) listMap.get("FALLAMOUNTFOR_D"));

            details.add(detail);
        }
        invoiceOutputBase.setDetail(details);
    }

    /**
     * 从金蝶查询账单获取的map中拿到需要的表头信息
     *
     * @param rootMap 根目录
     * @param keyWord 所需信息所在一级节点的Key
     * @return
     */
    private String getInfoFromMap(Map rootMap, String keyWord) {
        try {
            Map orgRoot = getObject(rootMap, Map.class, keyWord);
            List orgName = getObject(orgRoot, List.class, "Name");
            return getObject((Map<String, Object>) orgName.get(0), String.class, "Value");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从Map中获取指定任意节点的Key值
     *
     * @param rootMap 根目录
     * @param clz     目标的数据类型
     * @param Keys    依次传入从Map第一级至目标所在数据的各级Key值
     * @return
     */
    private <T> T getInfoFromMapByKeys(Map rootMap, Class<T> clz, String[] Keys) {
        if (Keys.length == 0) {
            return null;
        }
        Object gotObject = rootMap.get(Keys[0]);
        if (Keys.length >= 2) {
            List<String> list = new ValidList<>();
            for (int i = 1; i < Keys.length; i++) {
                list.add(Keys[i]);
            }
            String[] strs = new String[Keys.length - 1];
            list.toArray(strs);
            Map<String, Object> map;
            if (gotObject instanceof List) {
                if (((List) gotObject).isEmpty()) {
                    return null;
                }
                map = (Map<String, Object>) ((List) gotObject).get(0);
            } else {
                map = (Map<String, Object>) gotObject;
            }
            return getInfoFromMapByKeys(map, clz, strs);
        } else {
            return (T) gotObject;
        }

    }

    /**
     * 通过业务单号获取到金蝶的应收应付单数据后
     * <br/>从map中获取指定Key值对应的指定类型的数据
     *
     * @param targetMap
     * @return
     */
    private <T> T getObject(Map<String, Object> targetMap, Class<T> clz, String key) {
        Object o = targetMap.get(key);
        if (Objects.isNull(o)) {
            log.error("不存在" + key + "这个节点，请检查");
            return null;
        }
        if (clz.isInstance(o)) {
            return clz.cast(o);
        }
        log.error("无法转换的类型 from {} to {}", o.toString(), clz.toString());
        return null;
    }

    /**
     * <b>单据返回数据转化</b>
     * <li/>单据响应数据格式：[[1, "801", "深圳市佳裕达物流科技有限公司"]]
     * <li/>由于该结果不能直接格式化，所以需要进行转化
     *
     * @param result
     * @param fieldKeys
     * @return
     */
    private cn.hutool.json.JSONArray format(String result, String[] fieldKeys) {
        List<List<Object>> data = (List<List<Object>>) JSONObject.parse(result);
        JSONArray array = new JSONArray();
        JSONObject element = null;
        String fieldKey = null;
        for (List<Object> items : data) {
            element = new JSONObject();
            for (int i = 0, len = items.size(); i < len; i++) {
                //将用 . 连接的 key 转换为 _ 连接的key
                fieldKey = String.join("_", fieldKeys[i].split("\\."));
                element.put(fieldKey, items.get(i));
            }
            array.add(element);
        }
        return array;
    }


    @Override
    @Deprecated
    public CommonResult save(String formId, String jsonContent) {
        log.info("请求参数：{}", jsonContent);
        //调用Redis中的cookie
        Map<String, Object> header = new HashMap<>();

        header.put("Cookie", "kdservice-sessionid=20750c4e-10fa-4c11-bf05-7103a812f9b0; path=/");
        String result = KingdeeHttpUtil.httpPost(k3CloundConfig.getSave(), header, jsonContent);

        log.info("保存结果：{}", result);

        JSONObject jsonObject = JSON.parseObject(result);
        Map<String, Object> map = (Map<String, Object>) jsonObject.get("Result");
        Map<String, Object> responseStatus = (Map<String, Object>) map.get("ResponseStatus");
        Boolean isSuccess = (Boolean) responseStatus.get("IsSuccess");
        if (isSuccess) {
            return CommonResult.success();
        } else {
            List<Map<String, Object>> errors = (List<Map<String, Object>>) responseStatus.get("Errors");
            return CommonResult.error(-1, "errors");
        }

    }

    /**
     * 根据请求数据组装应付单数据
     *
     * @param reqForm 接口请求
     * @return
     */
    private JSONObject constructPayableBill(PayableHeaderForm reqForm) {
        //TODO 在查询出现异常时能够正确的抛出异常并结束操作
        //将请求参数中的字段中文名称转成对应代码
        Name2Code(reqForm);

        String filePathReceivableMain = "json/PayableMainModel.json";
        String filePathReceivableDetail = "json/PayableDetailModel.json";
        JSONObject basic = null;
        try {
            basic = getMapFromJsonFile(filePathReceivableMain);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("服务异常：{}", e, e.getMessage());
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "读取基础模板数据时出现异常");
        }

        //获取表头信息MAP以及建立明细数据的列表
        Map<String, Object> model = (Map<String, Object>) basic.get("Model");
        List<APARDetailForm> detailForms = reqForm.getEntityDetail();
        //人民币默认代码
        String keyRMB = reqForm.getCurrency();
        //汇率类型默认代码
        String exchangeType = "HLTX01_SYS";
        //汇率默认1.0
        BigDecimal exchangeRate = reqForm.getExchangeRate();

        //业务日期默认为当前时间（必填项）
        if (StringUtils.isEmpty(reqForm.getBusinessDate())) {
            model.put("FDATE", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        } else {
            model.put("FDATE", reqForm.getBusinessDate());
        }

        //到期时间 当前时间往后三个月（必填项）
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 3);
        model.put("FENDDATE_H", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));

        //FDOCUMENTSTATUS 单据状态（必填项）默认用模板自带的"Z"

        //供应商代码（必填项） TODO 可能要想办法根据名称获取代码
        putPackedProperty(model, "FSUPPLIERID", reqForm.getSupplierName());
        //币别（必填） 由前端传入
        //FBillTypeID 单据类型沿用模板，不需要修改
        putPackedProperty(model, "FCURRENCYID", keyRMB);
        //结算组织（必填项） TODO 可能要想办法根据名称获取代码
        putPackedProperty(model, "FSETTLEORGID", reqForm.getSettleOrgName());
        //采购组织 TODO 可能要想办法根据名称获取代码,可能要区分销售组织，虽然默认情况下，结算，销售，收款为一样的数据
        putPackedProperty(model, "FPURCHASEORGID", reqForm.getSettleOrgName());
        //采购部门 TODO 可能要想办法根据名称获取代码
        putPackedProperty(model, "FPURCHASEDEPTID", reqForm.getPurchaseDeptName());
        //付款组织（必填项）TODO 可能要想办法根据名称获取代码
        putPackedProperty(model, "FPAYORGID", reqForm.getSettleOrgName());
        //业务单号（报关单9位编号）
        model.put("F_JYD_Text", reqForm.getBusinessNo());
        //备注
        model.put("FAP_Remark", reqForm.getRemark());
        //账单编号
        model.put("F_PCQE_Text", reqForm.getBillNo());

        /**
         * FsubHeadSuppiler 表头客户,此处要修改三个字段 TODO 可能要想办法根据名称获取代码
         */
        Map<String, Object> subHeadSuppiler = (Map<String, Object>) model.get("FsubHeadSuppiler");
        putPackedProperty(subHeadSuppiler, "FORDERID", reqForm.getSupplierName());
        putPackedProperty(subHeadSuppiler, "FTRANSFERID", reqForm.getSupplierName());
        putPackedProperty(subHeadSuppiler, "FChargeId", reqForm.getSupplierName());
        model.put("FsubHeadSuppiler", subHeadSuppiler);


        /**
         * FEntityDetail 明细拼装
         */
        //统计不含税金额总额
        BigDecimal noTaxTotalPrice = new BigDecimal(0);
        //统计税金总额
        BigDecimal taxTotal = new BigDecimal(0);

        //定义用于存放明细列表的list
        List<Map<String, Object>> detailLists = new ArrayList<>();
        //遍历请求参数中的明细列表，对明细map进行重新拼装
        for (APARDetailForm detailForm : detailForms) {
            JSONObject detailBasic = null;
            try {
                detailBasic = getMapFromJsonFile(filePathReceivableDetail);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("服务异常：{}", e, e.getMessage());
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "读取基础模板数据时出现异常");
            }
            Map<String, Object> detail = putPayableEntityDetail(detailBasic, detailForm, noTaxTotalPrice, taxTotal);
            detailLists.add(detail);
        }
        model.put("FEntityDetail", detailLists);
        //TODO 报关过来的数据只有代垫费用和代理报关服务费两种费用类型，代垫费用不开票，不计税，除此以外都记税开票
        /**
         * FsubHeadFinc 表头财务 TODO 可能要想办法根据名称获取代码
         */
        //TODO 要对应增删和修改相应的重复字段
        Map<String, Object> subHeadFinc = (Map<String, Object>) model.get("FsubHeadFinc");
        //到期日期计算日期（必填项）
        subHeadFinc.put("FACCNTTIMEJUDGETIME", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        //本位币，默认人民币PRE001(必填项)
        putPackedProperty(subHeadFinc, "FMAINBOOKSTDCURRID", reqForm.getBaseCurrency());
        //汇率类型（必填项）
        putPackedProperty(subHeadFinc, "FEXCHANGETYPE", exchangeType);
        //汇率
        subHeadFinc.put("FExchangeRate", exchangeRate);
        //税额
        subHeadFinc.put("FTaxAmountFor", taxTotal);
        //不含税总额
        subHeadFinc.put("FNoTaxAmountFor", noTaxTotalPrice);
        model.put("FsubHeadFinc", subHeadFinc);

        /**
         * FEntityPlan 收款计划拼装(默认取第一个元素)
         */
        //TODO 收款计划暂且丢空数据进金蝶
        //List<Map<String, Object>> entityPlanList = (List<Map<String, Object>>) model.get("FEntityPlan");
        //if (entityPlanList.isEmpty()) {
        //    Asserts.fail(ResultEnum.UNKNOWN_ERROR);
        //}
        //entityPlanList.get(0).put("FENDDATE", reqForm.getEndDate());
        //TODO 修改此处价格
        //entityPlanList.get(0).put("FPAYAMOUNTFOR",detailForms.get(0).getAllAmountForD());
        List<Map<String, Object>> entityPlanList = new ArrayList<>();
        model.put("FEntityPlan", entityPlanList);

        //设置完更新Model
        basic.put("Model", model);

        //全部处理完毕返回
        return basic;
    }


    /**
     * 根据请求数据组装应收单数据
     *
     * @param reqForm 接口请求
     * @return
     */
    private JSONObject constructReceivableBill(ReceivableHeaderForm reqForm) {
        //将请求参数中的字段中文名称转成对应代码
        Name2Code(reqForm);

        String filePathReceivableMain = "json/ReceivableMainModel.json";
        String filePathReceivableDetail = "json/ReceivableDetailModel.json";
        JSONObject basic = null;
        try {
            basic = getMapFromJsonFile(filePathReceivableMain);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("服务异常：{}", e, e.getMessage());
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "读取基础模板数据时出现异常");
        }
        Map<String, Object> model = (Map<String, Object>) basic.get("Model");
        List<APARDetailForm> detailForms = reqForm.getEntityDetail();
        //人民币默认代码
        String keyRMB = reqForm.getCurrency();
        //汇率类型默认代码
        String exchangeType = "HLTX01_SYS";
        //汇率默认1.0
        BigDecimal exchangeRate = reqForm.getExchangeRate();

        //业务日期默认为当前时间（必填项）
        if (StringUtils.isEmpty(reqForm.getBusinessDate())) {
            model.put("FDATE", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        } else {
            model.put("FDATE", reqForm.getBusinessDate());
        }
        //到期时间 当前时间往后三个月（必填项）
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 3);
        model.put("FENDDATE_H", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));


        //客户代码（必填项） TODO 可能要想办法根据名称获取代码
        putPackedProperty(model, "FCUSTOMERID", reqForm.getCustomerName());
        //币别（必填） 默认为人民币PRE001
        //FBillTypeID 单据类型沿用模板，不需要修改
        putPackedProperty(model, "FCURRENCYID", keyRMB);
        //结算组织（必填项） TODO 可能要想办法根据名称获取代码
        putPackedProperty(model, "FSETTLEORGID", reqForm.getSettleOrgName());
        //销售组织 TODO 可能要想办法根据名称获取代码,可能要区分销售组织，虽然默认情况下，结算，销售，收款为一样的数据
        putPackedProperty(model, "FSALEORGID", reqForm.getSettleOrgName());
        //销售部门（必填项） TODO 可能要想办法根据名称获取代码
        putPackedProperty(model, "FSALEDEPTID", reqForm.getSaleDeptName());
        //收款组织（必填项）TODO 可能要想办法根据名称获取代码
        putPackedProperty(model, "FPAYORGID", reqForm.getSettleOrgName());
        //业务单号（报关单9位编号）
        model.put("FAR_Remark", reqForm.getBusinessNo());
        //备注
        model.put("F_JYD_Remarks", reqForm.getRemark());
        //账单编号
        model.put("F_PCQE_Text", reqForm.getBillNo());

        /**
         * FsubHeadSuppiler 表头客户,此处要修改三个字段 TODO 可能要想办法根据名称获取代码
         */
        Map<String, Object> subHeadSuppiler = (Map<String, Object>) model.get("FsubHeadSuppiler");
        putPackedProperty(subHeadSuppiler, "FORDERID", reqForm.getCustomerName());
        putPackedProperty(subHeadSuppiler, "FTRANSFERID", reqForm.getCustomerName());
        putPackedProperty(subHeadSuppiler, "FChargeId", reqForm.getCustomerName());
        model.put("FsubHeadSuppiler", subHeadSuppiler);


        /**
         * FEntityDetail 明细拼装
         */
        //统计不含税金额总额
        BigDecimal noTaxTotalPrice = new BigDecimal(0);
        //统计税金总额
        BigDecimal taxTotal = new BigDecimal(0);

        //定义用于存放明细列表的list
        List<Map<String, Object>> detailLists = new ArrayList<>();
        //遍历请求参数中的明细列表，对明细map进行重新拼装
        for (APARDetailForm detailForm : detailForms) {
            JSONObject detailBasic = null;
            try {
                detailBasic = getMapFromJsonFile(filePathReceivableDetail);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("服务异常：{}{}", e, e.getMessage());
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "读取基础模板数据时出现异常");
            }
            Map<String, Object> detail = putReceivableEntityDetail(detailBasic, detailForm, noTaxTotalPrice, taxTotal);
            detailLists.add(detail);
        }
        model.put("FEntityDetail", detailLists);
        //TODO 报关过来的数据只有代垫费用和代理报关服务费两种费用类型，代垫费用不开票，不计税，除此以外都记税开票
        /**
         * FsubHeadFinc 表头财务 TODO 可能要想办法根据名称获取代码
         */
        //TODO 要对应增删和修改相应的重复字段
        Map<String, Object> subHeadFinc = (Map<String, Object>) model.get("FsubHeadFinc");
        //到期日期计算日期（必填项）
        subHeadFinc.put("FACCNTTIMEJUDGETIME", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        //本位币，默认人民币PRE001(必填项)
        putPackedProperty(subHeadFinc, "FMAINBOOKSTDCURRID", reqForm.getBaseCurrency());
        //汇率类型（必填项）
        putPackedProperty(subHeadFinc, "FEXCHANGETYPE", exchangeType);
        //汇率
        subHeadFinc.put("FExchangeRate", exchangeRate);
        //税额
        subHeadFinc.put("FTaxAmountFor", taxTotal);
        //不含税总额
        subHeadFinc.put("FNoTaxAmountFor", noTaxTotalPrice);
        model.put("FsubHeadFinc", subHeadFinc);

        /**
         * FEntityPlan 收款计划拼装(默认取第一个元素)
         */
        //TODO 收款计划暂且丢空数据进金蝶
        //List<Map<String, Object>> entityPlanList = (List<Map<String, Object>>) model.get("FEntityPlan");
        //if (entityPlanList.isEmpty()) {
        //    Asserts.fail(ResultEnum.UNKNOWN_ERROR);
        //}
        //entityPlanList.get(0).put("FENDDATE", reqForm.getEndDate());
        //TODO 修改此处价格
        //entityPlanList.get(0).put("FPAYAMOUNTFOR",detailForms.get(0).getAllAmountForD());
        List<Map<String, Object>> entityPlanList = new ArrayList<>();
        model.put("FEntityPlan", entityPlanList);

        //设置完更新Model
        basic.put("Model", model);

        //全部处理完毕返回
        return basic;
    }

    private String buildParam(String formId, JSONObject data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formid", formId);
        jsonObject.put("data", JSON.toJSONString(data));
        return JSON.toJSONString(jsonObject);
    }

    /**
     * 将请求参数中的数据填写到应付单模板中并放入列表
     *
     * @param detailBasic     明细模板
     * @param detailForm      请求参数中的某一条
     * @param noTaxTotalPrice
     * @param taxTotal
     */
    //TODO 获取相关代码
    private Map<String, Object> putPayableEntityDetail(JSONObject detailBasic, APARDetailForm detailForm, BigDecimal noTaxTotalPrice, BigDecimal taxTotal) {
        //计价单位默认Pcs
        String priceUnitIdPacked = "Pcs";

        Map<String, Object> detail = detailBasic.getInnerMap();
        //物料编码
        putPackedProperty(detail, "FMATERIALID", detailForm.getMaterialIdPacked());
        //物料名称
        detail.put("FMaterialDesc", detailForm.getExpenseName());
        //费用类别代码
        putPackedProperty(detail, "F_JYD_Assistant1", detailForm.getExpenseCategoryName());
        //费用类型代码
        putPackedProperty(detail, "F_JYD_Assistant", detailForm.getExpenseTypeName());

        //计价单位
        putPackedProperty(detail, "FPRICEUNITID", priceUnitIdPacked);
        //定义计价个数
        BigDecimal num = detailForm.getPriceQty();

        BigDecimal taxPrice = detailForm.getTaxPrice();

        //计价数量()
        detail.put("FPriceQty", num);
        //含税单价
        detail.put("FTaxPrice", taxPrice.setScale(2, RoundingMode.HALF_UP));
        //单条税率
        BigDecimal taxRate = detailForm.getTaxRate();

        //根据前端传入的数据获取

        //定义单价
        BigDecimal price = taxPrice.divide(BigDecimal.ONE.add(taxRate.divide(new BigDecimal("100"))), 8, RoundingMode.HALF_UP);

        //单价(如果计税，单价=含税单价/（1+税率）并保留6位小数，四舍五入)
        detail.put("FPrice", price.setScale(2, RoundingMode.HALF_UP));
        //含税净价
        detail.put("FPriceWithTax", price.setScale(2, RoundingMode.HALF_UP));
        //税率F
        detail.put("FEntryTaxRate", taxRate);

        BigDecimal taxPriceTotal = taxPrice.multiply(num);
        BigDecimal afterTaxPriceTotal = price.multiply(num);

        //不含税金额（含税时单价保留两位小数并四舍五入，不含税时等于含税单价）
        detail.put("FNoTaxAmountFor_D", afterTaxPriceTotal.setScale(2, RoundingMode.HALF_UP));
        //税额(含税单价-不含税金额)
        detail.put("FTAXAMOUNTFOR_D", taxPriceTotal.subtract(afterTaxPriceTotal).setScale(2, RoundingMode.HALF_UP));
        //价税合计（含税单价）
        detail.put("FALLAMOUNTFOR_D", taxPriceTotal.setScale(2, RoundingMode.HALF_UP));
        //统计不含税金额
        noTaxTotalPrice.add((BigDecimal) detail.get("FNoTaxAmountFor_D"));
        //统计税金
        taxTotal.add((BigDecimal) detail.get("FTAXAMOUNTFOR_D"));
        /**
         * 默认但是需要动态修改部分
         */
        //库存单位，默认等于计价单位
        putPackedProperty(detail, "FStockUnitId", priceUnitIdPacked);
        //库存数量，默认等于计价数量
        detail.put("FStockQty", num);
        //库存基本数量，默认等于计价数量
        detail.put("FStockBaseQty", num);


        //TODO 计价基本分母,销售基本分子,库存基本分子默认不修改

        return detail;
    }

    /**
     * 将请求参数中的数据填写到应收单模板中并放入列表
     *
     * @param detailBasic     明细模板
     * @param detailForm      请求参数中的某一条
     * @param noTaxTotalPrice
     * @param taxTotal
     */
    private Map<String, Object> putReceivableEntityDetail(JSONObject detailBasic, APARDetailForm detailForm, BigDecimal noTaxTotalPrice, BigDecimal taxTotal) {
        //计价单位默认Pcs
        String priceUnitIdPacked = "Pcs";

        Map<String, Object> detail = detailBasic.getInnerMap();
        //物料编码
        putPackedProperty(detail, "FMATERIALID", detailForm.getMaterialIdPacked());
        //物料名称
        detail.put("FMaterialDesc", detailForm.getExpenseName());
        //费用类别代码
        putPackedProperty(detail, "F_JYD_Assistant", detailForm.getExpenseCategoryName());
        //费用类型代码(08为报关服务费，需要收税；11为代垫，不收税)
        putPackedProperty(detail, "F_JYD_Assistant1", detailForm.getExpenseTypeName());
        //计价单位
        putPackedProperty(detail, "FPRICEUNITID", priceUnitIdPacked);
        //定义计价个数
        BigDecimal num = detailForm.getPriceQty();
        //含税单价
        BigDecimal taxPrice = detailForm.getTaxPrice();
        //税率
        BigDecimal taxRate = detailForm.getTaxRate();
        //定义单价
        BigDecimal afterTaxPrice = taxPrice.divide(
                BigDecimal.ONE.add(taxRate.divide(new BigDecimal("100"))), 8, RoundingMode.HALF_UP
        );
        //计价数量()
        detail.put("FPriceQty", num);
        //含税单价
        detail.put("FTaxPrice", taxPrice.setScale(2, RoundingMode.HALF_UP));
        //税后单价(如果计税，单价=含税单价/（1+税率）并保留6位小数，四舍五入)
        detail.put("FPrice", afterTaxPrice.setScale(2, RoundingMode.HALF_UP));
        //税率
        detail.put("FEntryTaxRate", detailForm.getTaxRate());
        //税后总计
        BigDecimal afterTaxPriceTotal = afterTaxPrice.multiply(num);
        //税前总计
        BigDecimal taxPriceTotal = taxPrice.multiply(num);
        //不含税金额（含税时单价保留两位小数并四舍五入，不含税时等于含税单价）
        detail.put("FNoTaxAmountFor_D", afterTaxPriceTotal.setScale(2, RoundingMode.HALF_UP));
        //税额(含税单价-不含税金额)
        detail.put("FTAXAMOUNTFOR_D", taxPriceTotal.subtract(afterTaxPriceTotal).setScale(2, RoundingMode.HALF_UP));
        //价税合计（含税单价）
        detail.put("FALLAMOUNTFOR_D", taxPriceTotal.setScale(2, RoundingMode.HALF_UP));
        //统计不含税金额
        noTaxTotalPrice.add((BigDecimal) detail.get("FNoTaxAmountFor_D"));
        //统计税金
        taxTotal.add((BigDecimal) detail.get("FTAXAMOUNTFOR_D"));
        /**
         * 默认但是需要动态修改部分
         */
        //库存单位，默认等于计价单位
        putPackedProperty(detail, "FStockUnitId", priceUnitIdPacked);
        //库存数量，默认等于计价数量
        detail.put("FStockQty", num);
        //库存基本数量，默认等于计价数量
        detail.put("FStockBaseQty", num);
        //销售单位，默认等于计价单位
        putPackedProperty(detail, "FSalUnitId", priceUnitIdPacked);
        //销售数量，默认等于计价数量
        detail.put("FSalQty", num);
        //销售基本数量，默认等于计价数量
        detail.put("FSalBaseQty", num);
        //TODO 计价基本分母,销售基本分子,库存基本分子默认不修改

        return detail;
    }

    /**
     * 使用packed标记时数据要填写至节点下的FNumber节点中
     * <br>例如这样的字段 : "字段名": {"FNumber": "字段值"}
     * <br>字段内部还要封装一层FNumber才是对应的值
     *
     * @param model   表头数据
     * @param keyName 表头数据中需要再封装一层数据的字段名
     * @param Value   需要封装的内容
     */
    private void putPackedProperty(Map<String, Object> model, String keyName, Object Value) {
        Map<String, Object> fNumber = (Map<String, Object>) model.get(keyName);
        fNumber.put("FNumber", Value);
        model.put(keyName, fNumber);
    }

    /**
     * 从文件路径模板获取json对象以便后续处理
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    private JSONObject getMapFromJsonFile(String filePath) throws Exception {
        //获取文件
        //获取物料数据模板
        String template = FileUtil.toString(new ClassPathResource(filePath));
        log.info("template = {}", template);
        //带顺序的json，model顺序会相互影响，不能改变顺序
        //将template读到的模板数据尝试解析并放入LinkedHashMap中
        LinkedHashMap<String, Object> json = JSON.parseObject(template, LinkedHashMap.class, Feature.OrderedField);
        JSONObject basic = new JSONObject(true);
        //将LinkedHashMap数据装进JSONObject
        basic.putAll(json);
        return basic;
    }

    /**
     * 将请求表单中的中文数据转换为代码
     *
     * @param reqForm 传入的表单数据
     * @return
     */
    private Object Name2Code(Object reqForm) {
        /**
         * 传入表单是一个应收单
         */
        if (reqForm instanceof ReceivableHeaderForm) {
            //传入的请求参数
            ReceivableHeaderForm result = (ReceivableHeaderForm) reqForm;
            //币别
            Optional<com.jayud.po.Currency> currency = baseService.get(result.getCurrency(), com.jayud.po.Currency.class);
            if (!currency.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入的币别代码 [" + result.getCurrency() + "] 无法找到相应的金蝶系统代码");
            }
            result.setCurrency(currency.get().getFNumber());
            //本位币别
            currency = baseService.get("CNY", com.jayud.po.Currency.class);
            if (!currency.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入本位币别代码 [CNY] 无法找到相应的金蝶系统代码");
            }
            result.setBaseCurrency(currency.get().getFNumber());
            //客户
            Optional<Customer> customers = baseService.get(result.getCustomerName(), Customer.class);
            if (!customers.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入的客户名称 [" + result.getCustomerName() + "] 无法找到相应的金蝶系统代码");
            }
            result.setCustomerName(customers.get().getFNumber());
            //组织
            Optional<Organization> organizations = baseService.get(result.getSettleOrgName(), Organization.class);
            if (!organizations.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入的组织名称 [" + result.getSettleOrgName() + "] 无法找到相应的金蝶系统代码");
            }
            result.setSettleOrgName(organizations.get().getFNumber());
            //部门
            Optional<Department> departments = baseService.get(result.getSaleDeptName(), Department.class);
            if (!departments.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入的部门名称 [" + result.getSaleDeptName() + "] 无法找到相应的金蝶系统代码");
            }
            result.setSaleDeptName(departments.get().getFNumber());
            result.setEntityDetail(putEntityDetail(result.getEntityDetail()));

        } else if (reqForm instanceof PayableHeaderForm) {
            //传入的请求参数
            PayableHeaderForm result = (PayableHeaderForm) reqForm;
            //币别
            Optional<com.jayud.po.Currency> currency = baseService.get(result.getCurrency(), com.jayud.po.Currency.class);
            if (!currency.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入的币别代码 [" + result.getCurrency() + "] 无法找到相应的金蝶系统代码");
            }
            result.setCurrency(currency.get().getFNumber());
            //本位币别
            currency = baseService.get("CNY", Currency.class);
            if (!currency.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入本位币别代码 [CNY] 无法找到相应的金蝶系统代码");
            }
            result.setBaseCurrency(currency.get().getFNumber());
            //供应商
            Optional<Supplier> suppliers = baseService.get(result.getSupplierName(), Supplier.class);
            if (!suppliers.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入的供应商名称 [" + result.getSupplierName() + "] 无法找到相应的金蝶系统代码");
            }
            result.setSupplierName(suppliers.get().getFNumber());
            //组织
            Optional<Organization> organizations = baseService.get(result.getSettleOrgName(), Organization.class);
            if (!organizations.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入的组织名称 [" + result.getSettleOrgName() + "] 无法找到相应的金蝶系统代码");
            }
            result.setSettleOrgName(organizations.get().getFNumber());
            //部门
            Optional<Department> departments = baseService.get(result.getPurchaseDeptName(), Department.class);
            if (!departments.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入的部门名称 [" + result.getPurchaseDeptName() + "] 无法找到相应的金蝶系统代码");
            }
            result.setPurchaseDeptName(departments.get().getFNumber());
            result.setEntityDetail(putEntityDetail(result.getEntityDetail()));
        }
        return null;
    }

    /**
     * 将传入参数的明细中的各项名称转为金蝶的代码后，更新入参的明细数据
     *
     * @param detailForms
     * @return
     */
    private List<APARDetailForm> putEntityDetail(List<APARDetailForm> detailForms) {
        //明细
        for (APARDetailForm detailForm : detailForms) {
            //物料名
            Optional<Material> materialList = baseService.get(detailForm.getExpenseName(), Material.class);
            if (!materialList.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入的费用名称 [" + detailForm.getExpenseName() + "] 无法找到相应的金蝶系统代码");
            }
            detailForm.setMaterialIdPacked(materialList.get().getFNumber());
            //費用类型，费用类别
            Optional<AssistantProperty> assistantProperties = baseService.get(detailForm.getExpenseCategoryName(), AssistantProperty.class);
            if (!assistantProperties.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入的费用类别 " + detailForm.getExpenseCategoryName() + "] 无法找到相应的金蝶系统代码");
            }
            Optional<AssistantProperty> assistantProperties1 = baseService.get(detailForm.getExpenseTypeName(), AssistantProperty.class);
            if (!assistantProperties1.isPresent()) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "传入的费用类型 [" + detailForm.getExpenseTypeName() + "] 无法找到相应的金蝶系统代码");
            }
            detailForm.setExpenseCategoryName(assistantProperties.get().getFNumber());
            detailForm.setExpenseTypeName(assistantProperties1.get().getFNumber());
        }
        return detailForms;
    }

}
