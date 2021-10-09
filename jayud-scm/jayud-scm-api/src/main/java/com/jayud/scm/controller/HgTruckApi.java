package com.jayud.scm.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.ApiException;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.TokenGenerator;
import com.jayud.scm.model.bo.AddHgTruckForm;
import com.jayud.scm.model.bo.BizData;
import com.jayud.scm.model.bo.OutAuthenticationForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.po.HgTruck;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.HgTruckApiVO;
import com.jayud.scm.model.vo.HgTruckVO;
import com.jayud.scm.model.vo.SystemUserVO;
import com.jayud.scm.model.vo.UserLoginToken;
import com.jayud.scm.service.IBookingOrderService;
import com.jayud.scm.service.IHgTruckService;
import com.jayud.scm.service.ISystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@Api(tags = "港车对外接口")
@RestController
@Slf4j
public class HgTruckApi {

    @Value("${scmApi.url.createTransportationUrl}")
    private String createTransportationUrl;

    @Value("${scmApi.url.updateTransportationUrl}")
    private String updateTransportationUrl;

    @Value("${scmApi.url.loginUrl}")
    private String loginUrl;

    @Autowired
    private IHgTruckService hgTruckService;

    @Autowired
    ISystemUserService systemUserService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @ApiOperation(value = "对外授权", notes = "sign签名方法: md5(账号+时间轴转json+密码)")
    @PostMapping(value = "/login")
    public CommonResult outOauth(@RequestBody OutAuthenticationForm form) {

        UserLoginToken token = new UserLoginToken();
        token.setPassword(form.getPassword().toCharArray());
        token.setUsername(form.getLoginname());

        String sign = form.getSign();//密文
        String loginname = form.getLoginname();
        String oprTime = form.getBizData().getOprTime();
        if (StringUtils.isBlank(sign) || StringUtils.isBlank(loginname) || StringUtils.isBlank(form.getPassword()) ||
                form.getBizData() == null || "".equals(form.getBizData()) ||
                StringUtils.isBlank(oprTime) || oprTime.length() != 14) {
            return CommonResult.error(10001, "授权请求参数不合法");
        }

        BizData bizData = new BizData();
        bizData.setOprTime(oprTime);
        String jsonStr = JSONObject.toJSONString(bizData);
        String encodeValue = DigestUtils.md5DigestAsHex((loginname + jsonStr + form.getPassword()).getBytes());
        if (!encodeValue.equals(sign)) {
            return CommonResult.error(10002, "授权不通过");
        }

//        // 获取Subject实例对象，用户实例
//        Subject subject = SecurityUtils.getSubject();
//
//        SystemUserVO cacheUser = null;
//        // 认证
//        // 传到 MyShiroRealm 类中的方法进行认证
//        try {
//            subject.login(token);
//        } catch (ApiException e) {
//            throw e;
//        } catch (Exception e) {
//            cacheUser = new SystemUserVO();
//            cacheUser.setIsError(true);
//            return CommonResult.error(1, "授权不通过", cacheUser);
//        }
        // 构建缓存用户信息返回给前端
//        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(SystemUser::getUserName,token.getUsername());
//        queryWrapper.lambda().eq(SystemUser::getPassword,token.getPassword());
//        queryWrapper.lambda().eq(SystemUser::getStatus,0);
//        SystemUser user = systemUserService.getOne(queryWrapper);
//        SystemUser user = (SystemUser) subject.getPrincipals().getPrimaryPrincipal();
//
//        if (user.getStatus() == 0) {
//            return CommonResult.error(400, "该用户已被禁用");
//        }
//        //响应前端数据
//        cacheUser = ConvertUtil.convert(user, SystemUserVO.class);
//        //缓存用户ID larry 2020年8月13日11:21:11
//        String uid = redisUtils.get(user.getId().toString());
//        if (uid == null) {
//            redisUtils.set(user.getId().toString(), subject.getSession().getId().toString());
//        }
//        cacheUser.setToken(subject.getSession().getId().toString());
//        cacheUser.setPassword(null);
//        log.warn("CacheUser is " + JSONUtil.toJsonStr(cacheUser));
//        //保存登录记录
//        systemUserService.insertLoginLog(user);
//        redisUtils.set(cacheUser.getToken(), user.getName());

        String token1 = TokenGenerator.generateValue(encodeValue);
        redisUtils.set("token",token1);

        return CommonResult.success(token1);

    }

    @ApiOperation(value = "订车提交")
    @PostMapping(value = "/carBookingSubmission")
    public CommonResult carBookingSubmission(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        HgTruckVO hgTruckVO = hgTruckService.getHgTruckById(id);
        HgTruckApiVO hgTruckApiVO = new HgTruckApiVO();
        if(hgTruckVO.getModelType().equals(1)){
            hgTruckApiVO.setBizType("进口");
        }else if(hgTruckVO.getModelType().equals(2)){
            hgTruckApiVO.setBizType("出口");
        }else {
            return CommonResult.error(444,"只有进出口的单才能进行该操作");
        }
        hgTruckApiVO.setIsVehicleWeigh(false);
        hgTruckApiVO.setPortName(hgTruckVO.getOutPort());
        hgTruckApiVO.setOrderNo(hgTruckVO.getTruckNo());
        hgTruckApiVO.setPreTruckStyle(hgTruckVO.getPreTruckStyle());
        hgTruckApiVO.setTruckDate(hgTruckVO.getTruckDate());
        hgTruckApiVO.setWarehouseInfo(hgTruckVO.getShippingHubName());


        OutAuthenticationForm outAuthenticationForm = new OutAuthenticationForm();

        String s = redisUtils.get(getRedisKey(outAuthenticationForm));
        if(StringUtils.isBlank(s)){
            s = doLogin(outAuthenticationForm);
        }

        //请求
        String feedback = HttpRequest
                .post(createTransportationUrl)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("token",s)
                .body(JSONUtil.toJsonStr(hgTruckApiVO))
                .execute().body();
        //获取token
        Map map1 = JSONUtil.toBean(feedback, Map.class);
        System.out.println(map1);
        if(map1.get("code").equals(0)){
            return CommonResult.success();
        }
        return CommonResult.error((Integer)map1.get("code"),(String)map1.get("msg"));
    }

    @ApiOperation(value = "明细提交")
    @PostMapping(value = "/detailSubmission")
    public CommonResult detailSubmission(@RequestBody Map<String,Object> map) {

        Integer id = MapUtil.getInt(map, "id");
        HgTruckVO hgTruckVO = hgTruckService.getHgTruckById(id);
        HgTruckApiVO hgTruckApiVO = new HgTruckApiVO();
        hgTruckApiVO.setOrderNo(hgTruckVO.getTruckNo());

        List<BookingOrder> bookingOrderByHgTrackId = bookingOrderService.getBookingOrderByHgTrackId(id);
        if(CollectionUtils.isEmpty(bookingOrderByHgTrackId)){
            return CommonResult.error(444,"该港车单未绑定委托单");
        }

        OutAuthenticationForm outAuthenticationForm = new OutAuthenticationForm();

        String s = redisUtils.get(getRedisKey(outAuthenticationForm));
        if(StringUtils.isBlank(s)){
            s = doLogin(outAuthenticationForm);
        }

        //请求
        String feedback = HttpRequest
                .post(updateTransportationUrl)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("token",s)
                .body(JSONUtil.toJsonStr(hgTruckApiVO))
                .execute().body();
        //获取token
        Map map1 = JSONUtil.toBean(feedback, Map.class);
        if(map1.get("code").equals(0)){
            return CommonResult.success();
        }
        return CommonResult.error(444,"明细提交失败");
    }




    @ApiOperation(value = "获取车次状态")
    @PostMapping(value = "/getTrainNumberStatus")
    public CommonResult getTrainNumberStatus(@RequestBody QueryCommonForm form, HttpServletRequest request) {

        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
            return CommonResult.error(444,"未登录，请前往登录");
        }

        if(StringUtils.isEmpty(form.getTrainStatus())){
            return CommonResult.error(444,"车次状态为空");
        }
        if(StringUtils.isEmpty(form.getTruckNo())){
            return CommonResult.error(444,"港车编号为空");
        }

        QueryWrapper<HgTruck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgTruck::getTruckNo,form.getTruckNo());
        queryWrapper.lambda().eq(HgTruck::getVoided,0);
        HgTruck hgTruck = hgTruckService.getOne(queryWrapper);
        if(hgTruck == null){
            return CommonResult.error(444,"港车编号不存在");
        }
        form.setId(hgTruck.getId());

        boolean result = hgTruckService.updateTrainNumberStatus1(form);
        if(!result){
            return CommonResult.error(444,"车次状态修改失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "获取载货清单")
    @PostMapping(value = "/getManifest")
    public CommonResult getManifest(@RequestBody Map<String,Object> map, HttpServletRequest request) {

        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
            return CommonResult.error(444,"未登录，请前往登录");
        }

        String exHkNo = MapUtil.getStr(map, "exHkNo");
        String truckNo = MapUtil.getStr(map, "truckNo");
        String userName = MapUtil.getStr(map, "userName");

        if(StringUtils.isEmpty(truckNo)){
            return CommonResult.error(444,"港车编号为空");
        }
        if(StringUtils.isEmpty(exHkNo)){
            return CommonResult.error(444,"载货清单为空");
        }

        QueryWrapper<HgTruck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgTruck::getTruckNo,truckNo);
        queryWrapper.lambda().eq(HgTruck::getVoided,0);
        HgTruck hgTruck = hgTruckService.getOne(queryWrapper);
        if(hgTruck == null){
            return CommonResult.error(444,"港车编号不存在");
        }

        boolean result = hgTruckService.getManifest(exHkNo,truckNo,userName);
        if(!result){
            return CommonResult.error(444,"载货清单修改失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "接受运输公司等信息")
    @PostMapping(value = "/acceptTransportationInformation")
    public CommonResult acceptTransportationInformation(@RequestBody AddHgTruckForm form,HttpServletRequest request) {

        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
            return CommonResult.error(444,"未登录，请前往登录");
        }

        if(StringUtils.isEmpty(form.getTruckCompany())){
            return CommonResult.error(444,"运输公司不能为空");
        }

        QueryWrapper<HgTruck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgTruck::getTruckNo,form.getTruckNo());
        queryWrapper.lambda().eq(HgTruck::getVoided,0);
        HgTruck hgTruck = hgTruckService.getOne(queryWrapper);
        if(hgTruck == null){
            return CommonResult.error(444,"港车编号不存在");
        }
        form.setId(hgTruck.getId());

        boolean result = hgTruckService.acceptTransportationInformation(form);
        if(!result){
            return CommonResult.error(444,"接收成功，运输信息修改失败");
        }
        return CommonResult.success();
    }

    //登录
    public String doLogin(OutAuthenticationForm form) {
        //入参键值对
        Map<String, Object> requestMap = new HashMap<>(4);
        requestMap.put("loginname", form.getLoginname());
        requestMap.put("password", form.getPassword());
        BizData bizData = new BizData();
        String date = DateUtils.format(new Date(), "yyyyMMddHHmmss");
        bizData.setOprTime(date);
        String jsonStr = JSONObject.toJSONString(bizData);
        String encodeValue = DigestUtils.md5DigestAsHex((form.getLoginname() + jsonStr + form.getPassword()).getBytes());
        requestMap.put("sign",encodeValue);
        requestMap.put("bizData",bizData);

        //请求
        String feedback = HttpRequest
                .post(loginUrl)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(JSONUtil.toJsonStr(requestMap))
                .execute().body();
        //获取token
        Map map = JSONUtil.toBean(feedback, Map.class);
        String data = MapUtil.getStr(map, "data");
        Map map1 = JSONUtil.toBean(data, Map.class);
        String token = MapUtil.getStr(map1, "token");
        if (org.apache.commons.lang.StringUtils.isEmpty(token)) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "登录失败，用户名或密码错误");
        }
        //token不为空，放入redis，过期时间0.5小时
        redisUtils.set(getRedisKey(form), token, RedisUtils.EXPIRE_THIRTY_MIN);
        return token;
    }

    private String getRedisKey(OutAuthenticationForm form) {
        return form.getLoginname() + form.getPassword();
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
