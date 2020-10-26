package com.jayud.customs.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSONObject;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.customs.annotations.APILog;
import com.jayud.customs.feign.MsgClient;
import com.jayud.customs.model.bo.*;
import com.jayud.customs.model.vo.*;
import com.jayud.customs.service.ICustomsApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author william
 * @description
 * @Date: 2020-09-07 15:14
 */
@RequestMapping("/customs")
@Api(tags = "云报关接口")
@RestController
@Slf4j
public class ApiController {
    @Autowired
    ICustomsApiService service;
    @Autowired
    MsgClient client;

    @ApiOperation(value = "云报关登录（强制从新登录）")
    @PostMapping("/login")
    public CommonResult login(@Valid @RequestBody LoginForm form) {
        service.login(form);
        return CommonResult.success();
    }


    //委托单上传部分
    @ApiOperation(value = "委托单上传")
    @PostMapping("/order/push")
    public CommonResult pushOrder(@Valid @RequestBody PushOrderForm form) {
        return CommonResult.success(service.pushOrder(form));
    }

    @ApiOperation(value = "上传委托单的附件")
    @PostMapping("/order/appendix/push")
    public CommonResult pushAppendix(@RequestBody @Valid PushAppendixForm form, MultipartFile file) {
        try {
            String encode = Base64.encode(file.getInputStream());
            if (StringUtils.isBlank(encode)) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "文件转码异常，上传失败");
            }
            form.setData(encode);
        } catch (IOException e) {
            e.printStackTrace();
            Asserts.fail(ResultEnum.PARAM_ERROR, "文件转码异常，上传失败");
        }
        service.pushAppendix(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "查询委托单")
    @PostMapping("/order/find")
    public CommonResult<FindOrderInfoVO> findOrderInfo(@Valid @RequestBody FindOrderInfoWrapperForm form) {
        return CommonResult.success(service.findOrderInfo(form));
    }

    @ApiOperation(value = "委托单明细查询")
    @PostMapping("/order/detail/find")
    public CommonResult<FindOrderDetailVO> findOrderDetail(@RequestBody Map<String, Object> param) {
        String uid = MapUtil.getStr(param, "uid");
        if (StringUtils.isBlank(uid)) {
            return CommonResult.success(null);
        }
        return CommonResult.success(service.findOrderDetail(uid));
    }

    //todo swagger的map类型处理注解修改
    @ApiOperation(value = "报关单下载")
    @PostMapping("/declaration/download")
    public CommonResult<DownloadCustomsDeclarationVO> DownloadCustomsDeclaration(@RequestBody Map<String, String> param) {
        if (CollectionUtil.isEmpty(param)) {
            return CommonResult.success();
        }
        String id = MapUtil.getStr(param, "id");
        String idType = MapUtil.getStr(param, "idType");
        if (StringUtils.isBlank(id) || StringUtils.isBlank(idType)) {
            return CommonResult.success();
        }
        return CommonResult.success(service.DownloadCustomsDeclaration(id, idType));
    }

    @ApiOperation(value = "报关进程查询")
    @PostMapping("/declaration/process/info")
    public CommonResult<DclarationProcessStepVO> getDeclarationProcessStep(@RequestBody Map<String, String> param) {
        if (CollectionUtil.isEmpty(param)) {
            return CommonResult.success();
        }
        String id = MapUtil.getStr(param, "id");
        if (StringUtils.isBlank(id)) {
            return CommonResult.success();
        }
        return CommonResult.success(service.getDeclarationProcessStep(id));
    }

    @ApiOperation(value = "委托单操作进程查询")
    @PostMapping("/order/process/info")
    public CommonResult<OrderProcessStepVO> getOrderProcessStep(@RequestBody Map<String, String> param) {
        if (CollectionUtil.isEmpty(param)) {
            return CommonResult.success();
        }
        String id = MapUtil.getStr(param, "id");
        if (StringUtils.isBlank(id)) {
            return CommonResult.success();
        }
        return CommonResult.success(service.getOrderProcessStep(id));
    }

    @ApiOperation(value = "查询应收单并推到金蝶")
    @PostMapping("/order/finance")
    @APILog
    public CommonResult getFinanceInfoAndPush2Kingdee(@RequestBody GetFinanceInfoForm form) {
        if (StringUtils.isEmpty(form.getApplyNo()) &&
                StringUtils.isEmpty(form.getTrustId()) &&
                StringUtils.isEmpty(form.getUnifyNo()) &&
                StringUtils.isEmpty(form.getId())
        ) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "至少填写一个单号");
        }
        service.getFinanceInfoAndPush2Kingdee(form);
//        Map<String, String> map = new HashMap<>();
//        map.put("test", "test");
//       client.consume(map);
        return CommonResult.success("发送完成");
    }

    @ApiOperation(value = "接收云报关审核成功的信息")
    @PostMapping("/feedback/finance/approved")
    @APILog
    public CommonResult receiveFinanceFeed(@RequestBody Map<String, String> param) {
        String applyNo = MapUtil.getStr(param, "apply_no");
        String uid = MapUtil.getStr(param, "uid");
        log.info(String.format("收到数据：apply_no=%s,uid=%s", applyNo == null ? "" : applyNo, uid == null ? "" : uid));
        if (StringUtils.isNotBlank(applyNo) && (18 == applyNo.length())) {
            GetFinanceInfoForm getFinanceInfoForm = new GetFinanceInfoForm();
            getFinanceInfoForm.setApplyNo(applyNo);
            log.info(String.format("开始查找云报关数据..."));
            service.getFinanceInfoAndPush2Kingdee(getFinanceInfoForm);
            return CommonResult.success(String.format("已经收到回执信息：18位报关单号为：%s", applyNo));
        } else {
            Asserts.fail("apply_no需要输入18位报关单号");
        }
        return CommonResult.error(ResultEnum.PARAM_ERROR, "apply_no需要输入18位报关单号");
    }


    @PostMapping("/push/finance")
    public CommonResult repushFinance() {

        try {

            InputStream inputStream = (new ClassPathResource("xml/Data.xml")).getInputStream();
//            String path = classPathResource.getPath();

//            XmlUtil.readXML(inputStream);
            Document document = XmlUtil.readXML(inputStream);
            Element documentElement = document.getDocumentElement();
            NodeList info = documentElement.getElementsByTagName("info");
            int length = info.getLength();
            List<String> infoList = new ArrayList<>();
            Integer count = 0;
            for (int i = 0; i < info.getLength(); i++) {
                Element item = (Element) info.item(i);
                String cuScode = item.getAttribute("CUScode");
                if (StringUtils.isEmpty(cuScode)) {
                    continue;
                }
                String cusCode9 = cuScode.substring(9);
                String passtime = item.getAttribute("passtime");
                if (DateUtil.parseDateTime(passtime).isAfter(DateUtil.parseDateTime("2020-10-01 00:00:00"))) {
                    String thisRow = "i= " + i + "    passtime=" + passtime + "    CusCode18=" + cuScode + "    CusCode9=" + cusCode9;
                    infoList.add(thisRow);
                    System.out.println(thisRow);
                    GetFinanceInfoForm getFinanceInfoForm = new GetFinanceInfoForm();
                    getFinanceInfoForm.setApplyNo(cuScode);
                    service.getFinanceInfoAndPush2Kingdee(getFinanceInfoForm);
//                    count++;
//                    if (count == 15) {
//                        break;
//                    }
                    Thread.sleep(4000);
                }

            }
            for (String s : infoList) {
                System.out.println(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return CommonResult.success();
    }


    @PostMapping("/push/finance/bylist")
    public CommonResult pushDeterminedApplyNos(@RequestBody String param) {
        JSONObject jsonObject = JSONObject.parseObject(param);
        Map<String, Object> innerMap = jsonObject.getInnerMap();

        List<String> list = (List<String>) MapUtil.get(innerMap, "list", List.class);
        List<String> targetList = null;
        if (CollectionUtil.isNotEmpty(list)) {
            targetList = list;

        } else {
            return CommonResult.error(ResultEnum.PARAM_ERROR, "入参不能为空");
        }

        HashSet<String> existSet = new HashSet<>();
        try {
            InputStream inputStream = (new ClassPathResource("xml/Data.xml")).getInputStream();
//            String path = classPathResource.getPath();

//            XmlUtil.readXML(inputStream);
            Document document = XmlUtil.readXML(inputStream);
            Element documentElement = document.getDocumentElement();
            NodeList info = documentElement.getElementsByTagName("info");
            int length = info.getLength();
            List<String> infoList = new ArrayList<>();
            Integer count = 0;
            for (int i = 0; i < info.getLength(); i++) {
                Element item = (Element) info.item(i);
                String cuScode = item.getAttribute("CUScode");
                if (StringUtils.isEmpty(cuScode)) {
                    continue;
                }
                String cusCode9 = cuScode.substring(9);

                if (!targetList.contains(cusCode9)) {
                    continue;
                }
                existSet.add(cusCode9);
                String passtime = item.getAttribute("passtime");
                if (DateUtil.parseDateTime(passtime).isAfter(DateUtil.parseDateTime("2020-10-01 00:00:00"))) {

                    String thisRow = "i= " + i + "    passtime=" + passtime + "    CusCode18=" + cuScode + "    CusCode9=" + cusCode9;
                    infoList.add(thisRow);
                    System.out.println(thisRow);
                    GetFinanceInfoForm getFinanceInfoForm = new GetFinanceInfoForm();
                    getFinanceInfoForm.setApplyNo(cuScode);
                    service.getFinanceInfoAndPush2Kingdee(getFinanceInfoForm);
//                    count++;
//                    if (count == 15) {
//                        break;
//                    }
                    Thread.sleep(4000);
                }

            }
            for (String s : infoList) {
                System.out.println(s);
            }
            if (existSet.size() != targetList.size()) {
                targetList.stream().filter(e -> !existSet.contains(e))
                        .collect(Collectors.toList()).forEach(e -> {
                    System.out.println(e);
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return CommonResult.success();

    }

}


