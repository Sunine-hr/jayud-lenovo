package com.jayud.scm.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.HttpRequester;
import com.jayud.common.utils.TokenGenerator;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.*;
import com.jayud.scm.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequestMapping("/api")
@Api(tags = "报关对外接口")
@RestController
@Slf4j
public class HgBillApi {

    @Autowired
    private IHgBillService hgBillService;

    @Autowired
    private IHgBillFollowService hgBillFollowService;

    @Autowired
    ISystemUserService systemUserService;

    @Autowired
    RedisUtils redisUtils;

    @Value("${yunbaoguan.urls.login}")
    String loginUrl;

    @Value("${yunbaoguan.urls.trusts}")
    String trustsUrl;

    @Value("${yunbaoguan.username}")
    String defaultUserName;

    @Value("${yunbaoguan.password}")
    String defaultPassword;

    @Value("${yunbaoguan.urls.declaration}")
    String declarationUrl;

    @Value("${yunbaoguan.urls.trust-trace}")
    String trustTraceUrl;


    @ApiOperation(value = "提交云报关")
    @PostMapping(value = "/submitYunBaoGuan")
    public CommonResult submitYunBaoGuan(@RequestBody QueryCommonForm form) {

        List<YunBaoGuanData> yunBaoGuanData = hgBillService.getYunBaoGuanData(form.getId());
        HgBill hgBill1 = hgBillService.getById(form.getId());
        PushOrderForm pushOrderForm = new PushOrderForm();

        //拼接数据传到云报关
        if(CollectionUtil.isNotEmpty(yunBaoGuanData)){
            CustomsHeadForm customsHeadForm = ConvertUtil.convert(yunBaoGuanData.get(0), CustomsHeadForm.class);
            YunBaoGuanData yunBaoGuanData1 = yunBaoGuanData.get(0);
            if(yunBaoGuanData.get(0).getModelType().equals(1)){
                customsHeadForm.setDeclareId(2);
            }else{
                customsHeadForm.setDeclareId(1);
            }
            customsHeadForm.setUid(hgBill1.getUid());
            customsHeadForm.setIndateDt(yunBaoGuanData1.getBillDate());
            customsHeadForm.setNote("供应链系统推送");
            customsHeadForm.setDeclareGroupId(1);

            List<CustomsGoodsForm> goodsForms = new ArrayList<>();
            for (YunBaoGuanData yunBaoGuanDatum : yunBaoGuanData) {
                CustomsGoodsForm customsGoodsForm = new CustomsGoodsForm();
                customsGoodsForm.setGoodsNo(yunBaoGuanDatum.getItemNo());
                customsGoodsForm.setGoodsName(yunBaoGuanDatum.getItemName());
                customsGoodsForm.setGoodsSpec(yunBaoGuanDatum.getItemModel());
//                customsGoodsForm.setGoodsNo("111111");
//                customsGoodsForm.setGoodsName("测试商品");
//                customsGoodsForm.setGoodsSpec("测试型号");
                customsGoodsForm.setAmount(yunBaoGuanDatum.getQty());
                customsGoodsForm.setAmount02(yunBaoGuanDatum.getQty2());
                customsGoodsForm.setAmount03(yunBaoGuanDatum.getQty3());
                customsGoodsForm.setUnitNo(yunBaoGuanDatum.getUnitNo());
                customsGoodsForm.setUnit02No(yunBaoGuanDatum.getUnitNo3());
                customsGoodsForm.setUnit03No(yunBaoGuanDatum.getUnitNo3());
                customsGoodsForm.setCountryNo(yunBaoGuanDatum.getCountryNo());
                customsGoodsForm.setOtherCountryNo(yunBaoGuanDatum.getOtherCountryNo());
                customsGoodsForm.setSum(yunBaoGuanDatum.getTotalCipPrice());
                customsGoodsForm.setPrice(yunBaoGuanDatum.getCipPrice());
                customsGoodsForm.setCurrencyNo(yunBaoGuanDatum.getCurrencyNo());
                customsGoodsForm.setGrossWeight(yunBaoGuanDatum.getGw());
                customsGoodsForm.setNetWeight(yunBaoGuanDatum.getNw());
                customsGoodsForm.setPiece(yunBaoGuanDatum.getPackages());
                customsGoodsForm.setSjgoodsNo(yunBaoGuanDatum.getSjgoodsNo());
                customsGoodsForm.setGjf(yunBaoGuanDatum.getGif());
                customsGoodsForm.setCiqName(yunBaoGuanDatum.getCiqName());
                customsGoodsForm.setDistrictCode(yunBaoGuanDatum.getDistrictcode());
                customsGoodsForm.setOriginCode(yunBaoGuanDatum.getOrigincode());
                goodsForms.add(customsGoodsForm);
            }
            pushOrderForm.setGdtl(goodsForms);
            pushOrderForm.setHead(customsHeadForm);
        }


        //调用云报关的委托单上传的方法
        PushOrderVO pushOrderVO = this.pushOrder(pushOrderForm);
        if(pushOrderVO == null){
            return CommonResult.error(444,"推送数据到云报关失败");
        }

        HgBill hgBill = new HgBill();
        hgBill.setUid(pushOrderVO.getHead().getUid());
        hgBill.setId(form.getId());
        hgBill.setCustomsState(1);
        boolean update = hgBillService.submitYunBaoGuan(hgBill);
        if(!update){
            return CommonResult.error(444,"提交云报关操作失败");
        }
        return CommonResult.success();
    }


    @ApiOperation(value = "请求云报关更新状态")
    @PostMapping(value = "/updateHgBill")
    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateHgBill() {
        //查询所有状态为已提交的报关单
        List<HgBill> hgBills = hgBillService.getHgBillDataByCustomsState();

        List<HgBillFollow> hgBillFollows = new ArrayList<>();

        //根据推送云报关拿到的uid查询当前单的进度
        for (HgBill hgBill : hgBills) {
            OrderProcessStepVO orderProcessStep = this.getOrderProcessStep(hgBill.getUid());
            List<DeclarationTraceVO> bgTrace = orderProcessStep.getBgTrace();

            for (DeclarationTraceVO declarationTraceVO : bgTrace) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(declarationTraceVO.getCname()).append(" : ").append(declarationTraceVO.getProcess_dt());

                //判断进程信息是否已保存
                List<HgBillFollow> hgBillFollows1 = hgBillFollowService.getHgBillFollowByBillIdAndContent(hgBill.getId(),stringBuffer.toString());
                if(CollectionUtil.isEmpty(hgBillFollows1)){
                    HgBillFollow hgBillFollow = new HgBillFollow();
                    hgBillFollow.setBillId(hgBill.getId());
                    hgBillFollow.setSType("系统");
                    hgBillFollow.setFollowContext(stringBuffer.toString());
                    hgBillFollow.setCrtByDtm(LocalDateTime.now());
                    hgBillFollow.setCrtByName("系统");
                    hgBillFollows.add(hgBillFollow);
                }
                if(declarationTraceVO.getCname().equals("海关已放行")){
                    //海关已放行，拿取改单的报关号和报关日期   并修改状态为提交完成
                    DownloadCustomsDeclarationVO downloadCustomsDeclarationVO = this.DownloadCustomsDeclaration(hgBill.getUid(), "2");
                    if(downloadCustomsDeclarationVO != null){
                        hgBill.setCustomsNo(downloadCustomsDeclarationVO.getHead().getApplyNo());
                        hgBill.setCustomsDate(downloadCustomsDeclarationVO.getHead().getApplyDt());
                        hgBill.setCustomsState(2);
                        boolean result = hgBillService.updateHgBillByYunBaoGuan(hgBill);
                        if(result){
                            log.warn("拿取报关号和报关日期，并添加成功");
                        }
                    }
                }
            }
        }

        //将跟踪信息保存起来
        boolean result = this.hgBillFollowService.saveBatch(hgBillFollows);
        if(result){
            log.warn("更新云报关状态");
        }
    }




    //登录
    private String doLogin(LoginForm form) {
        //入参键值对
        Map<String, String> requestMap = new HashMap<>(2);
        requestMap.put("name", form.getName());
        requestMap.put("password", form.getPassword());

        //请求
        String feedback = HttpRequest
                .post(loginUrl)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(JSONUtil.toJsonStr(requestMap))
                .execute().body();
        //获取token
        Map map = JSONUtil.toBean(feedback, Map.class);
        String ticket = MapUtil.getStr(map, "ticket");
        if (StringUtils.isEmpty(ticket)) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "登录失败，用户名或密码错误");
        }
        //token不为空，放入redis，过期时间12小时
        redisUtils.set(getRedisKey(form), ticket, RedisUtils.EXPIRE_YUNBAOGUAN);
        return ticket;
    }


    //委托单上传
    public PushOrderVO pushOrder(PushOrderForm form) {
        Gson gson = new Gson();
        String requestStr = gson.toJson(form);
        System.out.println("requestStr==================================" + requestStr);
        //请求
        String feedback = doPost(requestStr, trustsUrl);
        System.out.println("feedback=====================================" + feedback);

        PushOrderVO result = null;
        try {
            result = JSONUtil.toBean(feedback, PushOrderVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            Asserts.fail(ResultEnum.SAVE_ERROR, "出现异常，写入数据失败");
        }

        if (Objects.isNull(result)) {
            Asserts.fail(ResultEnum.SAVE_ERROR, "返回值为null,写入数据失败");
        }
        return result;
    }

    //报关单下载
    public DownloadCustomsDeclarationVO DownloadCustomsDeclaration(String id, String idType) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("id", id);
        param.put("idType", idType);
        cn.hutool.json.JSONObject feedback = doGet(declarationUrl, param);
        try {
            return JSONUtil.toBean(feedback, DownloadCustomsDeclarationVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取委托单进程
    public OrderProcessStepVO getOrderProcessStep(String id) {
        Map<String, Object> param = new HashMap<>(1);
        param.put("id", id);
        cn.hutool.json.JSONObject feedback = doGet(trustTraceUrl, param);
        try {
            return JSONUtil.toBean(feedback, OrderProcessStepVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //get请求方法
    private cn.hutool.json.JSONObject doGet(String url, Map<String, Object> params) {
        StringBuffer actualUrl = new StringBuffer().append(url);
        String requestUrl = url;
        if (!CollectionUtil.isEmpty(params)) {
            actualUrl.append("?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                actualUrl.append(String.format("%s=%s&", entry.getKey(), entry.getValue().toString()));
            }
            requestUrl = actualUrl.toString();
            requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
        }
        HttpRequester httpRequester = new HttpRequester();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)));
        try {
            cn.hutool.json.JSONObject jsonObject = httpRequester.sendGet(requestUrl, null, headerMap);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
//todo 为什么返回的数据不是json
//        return HttpRequest.get(requestUrl)
//                .header("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)))
//                .execute()
//                .body();
    }

    //post请求方法
    private String doPost(String requestStr, String url) {
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        String feedback = HttpRequest
                .post(url)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("X-Ticket", checkoutUserToken(new LoginForm(defaultUserName, defaultPassword, null)))
                .body(JSONUtil.toJsonStr(requestStr))
                .execute().body();

        return feedback;

    }


    public String checkoutUserToken(LoginForm form) {
        String token = redisUtils.get(getRedisKey(form));
        if (StringUtils.isEmpty(token)) {
            token = doLogin(form);
        }
        return token;
    }

    private String getRedisKey(LoginForm form) {
        return form.getName() + form.getPassword();
    }


    public static void main(String[] args) {

        BizData bizData = new BizData();
        String date = DateUtils.format(new Date(), "yyyyMMddHHmmss");
        bizData.setOprTime(date);
        System.out.println(date);
        String jsonStr = JSONObject.toJSONString(bizData);
        String encodeValue = DigestUtils.md5DigestAsHex(("admin" + jsonStr + "123456").getBytes());
        System.out.println(encodeValue);
        String token = TokenGenerator.generateValue(encodeValue);
        System.out.println(token);
    }


}
