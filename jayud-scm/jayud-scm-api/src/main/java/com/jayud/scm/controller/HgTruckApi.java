package com.jayud.scm.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.TokenGenerator;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.po.BDataDicEntry;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.po.HgTruck;
import com.jayud.scm.model.vo.*;
import com.jayud.scm.service.*;
import com.jayud.scm.utils.RSAUtils;
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
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequestMapping("/api")
@Api(tags = "港车对外接口")
@RestController
@Slf4j
public class HgTruckApi {

    @Value("${scmApi.url.createTransportationUrl}")
    private String createTransportationUrl;

    @Value("${scmApi.url.updateTransportationUrl}")
    private String updateTransportationUrl;

    @Value("${scmApi.url.carBookingSubmissionWithdrawal}")
    private String carBookingSubmissionWithdrawal;

    @Value("${scmApi.url.loginUrl}")
    private String loginUrl;

    @Value("${scmApi.password}")
    private String password;

    @Value("${scmApi.loginName}")
    private String loginName;

    @Autowired
    private IHgTruckService hgTruckService;

    @Autowired
    ISystemUserService systemUserService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @Autowired
    private IBookingOrderEntryService bookingOrderEntryService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @Autowired
    private IHubShippingService hubShippingService;

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

        if(!hgTruckVO.getStateFlag().equals(0)){
            return CommonResult.error(444,"请选择未装车的港车单进行操作");
        }

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
        DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String date = dtf3.format(hgTruckVO.getTruckDate());
        hgTruckApiVO.setTruckDate(date);
        hgTruckApiVO.setWarehouseInfo(hgTruckVO.getShippingHubName());


//        OutAuthenticationForm outAuthenticationForm = new OutAuthenticationForm();
//
//        String s = redisUtils.get(getRedisKey(outAuthenticationForm));
//        if(StringUtils.isBlank(s)){
//            s = doLogin(outAuthenticationForm);
//        }

//        System.out.println("订车请求参数："+JSONUtil.toJsonStr(hgTruckApiVO));
        //加密参数
//        String s1 = null;
//        try {
//             s1 = RSAUtils.privateEncrypt(JSONUtil.toJsonStr(hgTruckApiVO), RSAUtils.getPrivateKey(RSAUtils.PRIVATE_KEY));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("publicKey",RSAUtils.PUBLIC_KEY);
//        jsonObject.put("appId",RSAUtils.APP_ID);
//        jsonObject.put("data",s1);

        JSONObject jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(hgTruckApiVO));

        System.out.println("加密后的参数："+jsonObject);

        //请求
        String feedback = HttpRequest
                .post(createTransportationUrl)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                .header("token",s)
//                .body(JSONUtil.toJsonStr(hgTruckApiVO))
                .body(JSONUtil.toJsonStr(jsonObject))
                .execute().body();
        log.warn("carBookingSubmissionfeedback:"+feedback);

        //解密数据
        String s = null;
        try {
            s = RSAUtils.publicDecrypt(feedback, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取token
        Map map1 = JSONUtil.toBean(s, Map.class);
//        System.out.println(map1);
        if(map1.get("code").equals(200)){
            HgTruck hgTruck = ConvertUtil.convert(hgTruckVO, HgTruck.class);
            hgTruck.setStateFlag(6);
            hgTruck.setPushTms(1);
            boolean result = this.hgTruckService.saveOrUpdate(hgTruck);
            if(result){
                log.warn("订车提交，订车单状态修改成功");
            }
            return CommonResult.success();
        }
        return CommonResult.error((Integer)map1.get("code"),(String)(map1.get("msg")!=null?map1.get("msg"):""));
    }

    @ApiOperation(value = "订车提交撤回")
    @PostMapping(value = "/carBookingSubmissionWithdrawal")
    public CommonResult carBookingSubmissionWithdrawal(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        HgTruckVO hgTruckVO = hgTruckService.getHgTruckById(id);

        if(!hgTruckVO.getStateFlag().equals(6) && !hgTruckVO.getStateFlag().equals(7) ){
            return CommonResult.error(444,"请选择已订车提交或已明细提交的港车单进行操作");
        }

        Map<String,Object> map2 = new HashMap<>();
        map2.put("orderNo",hgTruckVO.getTruckNo());
        JSONObject jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(map2));

        System.out.println("加密后的参数："+jsonObject);

        //请求
        String feedback = HttpRequest
                .post(carBookingSubmissionWithdrawal)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                .header("token",s)
//                .body(JSONUtil.toJsonStr(hgTruckApiVO))
                .body(JSONUtil.toJsonStr(jsonObject))
                .execute().body();
        log.warn("carBookingSubmissionfeedback:"+feedback);

        //解密数据
        String s = null;
        try {
            s = RSAUtils.publicDecrypt(feedback, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取token
        Map map1 = JSONUtil.toBean(s, Map.class);
//        System.out.println(map1);
        if(map1.get("code").equals(200)){
            HgTruck hgTruck = ConvertUtil.convert(hgTruckVO, HgTruck.class);
            hgTruck.setStateFlag(0);
            hgTruck.setPushTms(0);
            boolean result = this.hgTruckService.saveOrUpdate(hgTruck);
            if(result){
                log.warn("订车提交撤回，订车单状态修改成功");
            }
            return CommonResult.success();
        }
        return CommonResult.error((Integer)map1.get("code"),(String)(map1.get("msg")!=null?map1.get("msg"):""));
    }

    @ApiOperation(value = "明细提交")
    @PostMapping(value = "/detailSubmission")
    public CommonResult detailSubmission(@RequestBody Map<String,Object> map) {

        Integer id = MapUtil.getInt(map, "id");
        HgTruckVO hgTruckVO = hgTruckService.getHgTruckById(id);

        if(!hgTruckVO.getStateFlag().equals(6)){
            return CommonResult.error(444,"请选择已订车提交的港车单进行操作");
        }

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
        DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String date = dtf3.format(hgTruckVO.getTruckDate());
        hgTruckApiVO.setTruckDate(date);
        hgTruckApiVO.setWarehouseInfo(hgTruckVO.getShippingHubName());

        List<BookingOrder> bookingOrderByHgTrackId = bookingOrderService.getBookingOrderByHgTrackId(id);
        if(CollectionUtils.isEmpty(bookingOrderByHgTrackId)){
            return CommonResult.error(444,"该港车单未绑定委托单");
        }

        if(hgTruckVO.getModelType().equals(1)){
            List<AddAddressForm> takeAdrForms1 = new ArrayList<>();
            List<AddAddressForm> takeAdrForms2 = new ArrayList<>();
            BDataDicEntry dicEntryByDicCode = ibDataDicEntryService.getBDataDicEntryByDicCode("1049", "香港仓");
            BDataDicEntry dicEntryByDicCode1 = ibDataDicEntryService.getBDataDicEntryByDicCode("1049", "深圳仓");

//            Integer totalPieceAmount = 0;
//            Double totalWeight = 0.0;
//            Double toatalVolume = 0.0;
//            Integer totalPallets = 0;
            Integer pieceAmount = 0;
            Double weight = 0.0;
            Double volume = 0.0;
            Integer pallets = 0;
            AddAddressForm addAddressForm = new AddAddressForm();
            AddAddressForm addAddressForm1 = new AddAddressForm();

            for (BookingOrder bookingOrder : bookingOrderByHgTrackId) {
                List<BookingOrderEntryVO> bookingOrderEntryByBookingId = bookingOrderEntryService.findBookingOrderEntryByBookingId(bookingOrder.getId());

//                Integer pieceAmount = 0;
//                Double weight = 0.0;
//                Double volume = 0.0;
//                Integer pallets = 0;
                for (BookingOrderEntryVO bookingOrderEntryVO : bookingOrderEntryByBookingId) {
                    pieceAmount = pieceAmount + (bookingOrderEntryVO.getPackages()!=null ?bookingOrderEntryVO.getPackages():new BigDecimal(0)).intValue();
                    weight = weight + (bookingOrderEntryVO.getGw()!=null ?bookingOrderEntryVO.getGw():new BigDecimal(0)).doubleValue();
                    volume = volume + (bookingOrderEntryVO.getCbm()!=null ?bookingOrderEntryVO.getCbm():new BigDecimal(0)).doubleValue();
                    pallets = pallets + (bookingOrderEntryVO.getPallets()!=null ?bookingOrderEntryVO.getPallets():new BigDecimal(0)).intValue();
                }
                addAddressForm.setGoodsDesc(bookingOrderEntryByBookingId.get(0).getItemName());
                addAddressForm1.setGoodsDesc(bookingOrderEntryByBookingId.get(0).getItemName());
            }
            addAddressForm.setPieceAmount(pallets);
            addAddressForm.setBulkCargoAmount(pieceAmount);
            addAddressForm.setWeight(weight);
            addAddressForm.setVolume(volume);
            addAddressForm1.setPieceAmount(pallets);
            addAddressForm1.setBulkCargoAmount(pieceAmount);
            addAddressForm1.setWeight(weight);
            addAddressForm1.setVolume(volume);
            addAddressForm.setCityName(dicEntryByDicCode.getReserved3());
            if(dicEntryByDicCode.getReserved4() != null){
                addAddressForm.setAreaName(dicEntryByDicCode.getReserved4());
            }
            addAddressForm.setProvinceName(dicEntryByDicCode.getReserved2());
            addAddressForm.setAddress(dicEntryByDicCode.getReserved5());
            String[] s = dicEntryByDicCode.getReserved1().split(" ");
            addAddressForm.setContacts(s[0]);
            addAddressForm.setPhone(s[1]);


            addAddressForm1.setCityName(dicEntryByDicCode1.getReserved3());
            if(dicEntryByDicCode1.getReserved4() != null){
                addAddressForm1.setAreaName(dicEntryByDicCode1.getReserved4());
            }
            addAddressForm1.setProvinceName(dicEntryByDicCode1.getReserved2());
            addAddressForm1.setAddress(dicEntryByDicCode1.getReserved5());
            String[] s1 = dicEntryByDicCode.getReserved1().split(" ");
            addAddressForm1.setContacts(s1[0]);
            addAddressForm1.setPhone(s1[1]);

            takeAdrForms1.add(addAddressForm);
            takeAdrForms2.add(addAddressForm1);
            hgTruckApiVO.setTakeAdrForms1(takeAdrForms1);
            hgTruckApiVO.setTakeAdrForms2(takeAdrForms2);
        }else if(hgTruckVO.getModelType().equals(2)) {
            List<AddAddressForm> takeAdrForms1 = new ArrayList<>();
            List<AddAddressForm> takeAdrForms2 = new ArrayList<>();
            List<Integer> integers = new ArrayList<>();
            BDataDicEntry dicEntryByDicCode1 = ibDataDicEntryService.getBDataDicEntryByDicCode("1049", "深圳仓");
            AddAddressForm addAddressForm = new AddAddressForm();
            Integer pieceAmount = 0;
            Double weight = 0.0;
            Double volume = 0.0;
            Integer pallets = 0;
            for (BookingOrder bookingOrder : bookingOrderByHgTrackId) {
                List<BookingOrderEntryVO> bookingOrderEntryByBookingId = bookingOrderEntryService.findBookingOrderEntryByBookingId(bookingOrder.getId());

                for (BookingOrderEntryVO bookingOrderEntryVO : bookingOrderEntryByBookingId) {
                    pieceAmount = pieceAmount + (bookingOrderEntryVO.getPackages()!=null ?bookingOrderEntryVO.getPackages():new BigDecimal(0)).intValue();
                    weight = weight + (bookingOrderEntryVO.getGw()!=null ?bookingOrderEntryVO.getGw():new BigDecimal(0)).doubleValue();
                    volume = volume + (bookingOrderEntryVO.getCbm()!=null ?bookingOrderEntryVO.getCbm():new BigDecimal(0)).doubleValue();
                    pallets = pallets + (bookingOrderEntryVO.getPallets()!=null ?bookingOrderEntryVO.getPallets():new BigDecimal(0)).intValue();
                }
                integers.add(bookingOrder.getId());
                addAddressForm.setGoodsDesc(bookingOrderEntryByBookingId.get(0).getItemName());;
            }
            addAddressForm.setBulkCargoAmount(pieceAmount);
            addAddressForm.setWeight(weight);
            addAddressForm.setVolume(volume);
            addAddressForm.setPieceAmount(pallets);

            //拼接地址  填充地址信息
            addAddressForm.setCityName(dicEntryByDicCode1.getReserved3());
            if(dicEntryByDicCode1.getReserved4() != null){
                addAddressForm.setAreaName(dicEntryByDicCode1.getReserved4());
            }
            addAddressForm.setProvinceName(dicEntryByDicCode1.getReserved2());
            addAddressForm.setAddress(dicEntryByDicCode1.getReserved5());
            String[] s1 = dicEntryByDicCode1.getReserved1().split(" ");
            addAddressForm.setContacts(s1[0]);
            addAddressForm.setPhone(s1[1]);
            takeAdrForms1.add(addAddressForm);


            QueryCommonForm queryCommonForm = new QueryCommonForm();
            queryCommonForm.setIds(integers);
            List<HubShippingVO> hubShippingByBookingId = hubShippingService.getHubShippingByBookingId(queryCommonForm);
            if(CollectionUtils.isEmpty(hubShippingByBookingId)){
                return CommonResult.error(444,"该委托单还没有出库单");
            }
            for (HubShippingVO hubShippingVO : hubShippingByBookingId) {
                if(!hubShippingVO.getCheckStateFlag().equals("Y")){
                    return CommonResult.error(444,"该委托单的出库单未审核");
                }
            }

            HashMap<String,HubShippingVO> map1 = new HashMap<>();
            for (HubShippingVO hubShippingVO : hubShippingByBookingId) {
                if(map1.containsKey(hubShippingVO.getWhAddress())){
                    HubShippingVO hubShipping = map1.get(hubShippingVO.getWhAddress());
                    hubShipping.setTotaPackages((hubShippingVO.getTotaPackages()!= null ?hubShippingVO.getTotaPackages():0) + (hubShipping.getTotaPackages()!= null ?hubShipping.getTotaPackages():0));
                    hubShipping.setTotaPallets((hubShippingVO.getTotaPallets() != null ?hubShippingVO.getTotaPallets():0) + (hubShipping.getTotaPallets()!= null ?hubShipping.getTotaPallets():0));
                    hubShipping.setTotalGw(hubShippingVO.getTotalGw()!= null ?hubShippingVO.getTotalGw():new BigDecimal(0).add(hubShipping.getTotalGw()!= null ?hubShipping.getTotalGw():new BigDecimal(0)));
                    hubShipping.setTotaCbm((hubShippingVO.getTotaCbm()!= null ?hubShippingVO.getTotaCbm():new BigDecimal(0)).add(hubShipping.getTotaCbm()!= null ?hubShipping.getTotaCbm():new BigDecimal(0)));
                }else{
                    map1.put(hubShippingVO.getWhAddress(),hubShippingVO);
                }
            }
            List<HubShippingVO> hubShippings = new ArrayList<>();
            for (String s : map1.keySet()) {
                HubShippingVO hubShippingVO = map1.get(s);
                hubShippings.add(hubShippingVO);
            }

            for (HubShippingVO hubShippingVO : hubShippings) {
                AddAddressForm addAddressFor = new AddAddressForm();
                addAddressFor.setBulkCargoAmount(hubShippingVO.getTotaPackages().intValue());
                addAddressFor.setPieceAmount(hubShippingVO.getTotaPallets());
                addAddressFor.setWeight(hubShippingVO.getTotalGw().doubleValue());
                addAddressFor.setVolume(hubShippingVO.getTotaCbm().doubleValue());
                addAddressFor.setContacts(hubShippingVO.getWhName());
                addAddressFor.setPhone(hubShippingVO.getWhTel());
                addAddressFor.setGoodsDesc(hubShippingByBookingId.get(0).getAddHubShippingEntryFormList().get(0).getItemName());
                addAddressFor.setAddress(hubShippingVO.getWhAddress());
                takeAdrForms2.add(addAddressFor);
            }
            hgTruckApiVO.setTakeAdrForms1(takeAdrForms1);
            hgTruckApiVO.setTakeAdrForms2(takeAdrForms2);
        }

        System.out.println("请求参数："+hgTruckApiVO);
//        OutAuthenticationForm outAuthenticationForm = new OutAuthenticationForm();
//
//        String s = redisUtils.get(getRedisKey(outAuthenticationForm));
//        if(StringUtils.isBlank(s)){
//            s = doLogin(outAuthenticationForm);
//        }

//        System.out.println("明细请求参数："+JSONUtil.toJsonStr(hgTruckApiVO));

        //加密参数
//        String s1 = null;
//        try {
//            s1 = RSAUtils.privateEncrypt(JSONUtil.toJsonStr(hgTruckApiVO), RSAUtils.getPrivateKey(RSAUtils.PRIVATE_KEY));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("publicKey",RSAUtils.PUBLIC_KEY);
//        jsonObject.put("appid",RSAUtils.APP_ID);
//        jsonObject.put("data",s1);

        JSONObject jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(hgTruckApiVO));

        System.out.println("加密后的参数："+jsonObject);


        //请求
        String feedback = HttpRequest
                .post(updateTransportationUrl)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                .header("token",s)
//                .body(JSONUtil.toJsonStr(hgTruckApiVO))
                .body(JSONUtil.toJsonStr(jsonObject))
                .execute().body();
        log.warn("detailSubmissionfeedback:"+feedback);

        //解密数据
        String s = null;
        try {
            s = RSAUtils.publicDecrypt(feedback, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.warn("解密后的数据:"+s);

        //获取token
        Map map1 = JSONUtil.toBean(s, Map.class);
        if(map1.get("code").equals(200)){
            HgTruck hgTruck = ConvertUtil.convert(hgTruckVO, HgTruck.class);
            hgTruck.setStateFlag(7);
            hgTruck.setPushTms(2);
            boolean result = this.hgTruckService.saveOrUpdate(hgTruck);
            if(result){
                log.warn("订车明细提交，订车单状态修改成功");
            }

            return CommonResult.success();
        }
        return CommonResult.error(444,"明细提交失败");
    }

    @ApiOperation(value = "获取车次状态")
    @PostMapping(value = "/getTrainNumberStatus")
    public String getTrainNumberStatus(@RequestBody Map<String,Object> map, HttpServletRequest request) {
//        String token = request.getHeader("token");
//        if(StringUtils.isBlank(token)){
//            return CommonResult.error(444,"未登录，请前往登录");
//        }
        String data = MapUtil.getStr(map, "data");

        String s = null;
        try {
            s = RSAUtils.publicDecrypt(data, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        QueryCommonForm form = JSONUtil.toBean(s, QueryCommonForm.class);

        JSONObject jsonObject = null;
        if(StringUtils.isEmpty(form.getTrainStatus())){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"车次状态为空")));
            return JSONUtil.toJsonStr(jsonObject);

//            return CommonResult.error(444,"车次状态为空");
        }
        if(StringUtils.isEmpty(form.getTruckNo())){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"港车编号为空")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"港车编号为空");
        }

        QueryWrapper<HgTruck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgTruck::getTruckNo,form.getTruckNo());
        queryWrapper.lambda().eq(HgTruck::getVoided,0);
        HgTruck hgTruck = hgTruckService.getOne(queryWrapper);
        if(hgTruck == null){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"港车编号不存在")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"港车编号不存在");
        }
        form.setId(hgTruck.getId());

        boolean result = hgTruckService.updateTrainNumberStatus1(form);
        if(!result){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"车次状态修改失败")));
            return JSONUtil.toJsonStr(jsonObject);
        }
        jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.success()));
        return JSONUtil.toJsonStr(jsonObject);
    }

    @ApiOperation(value = "删除中港单")
    @PostMapping(value = "/updateTrainNumberStatus")
    public String updateTrainNumberStatus(@RequestBody Map<String,Object> map, HttpServletRequest request) {
//        String token = request.getHeader("token");
//        if(StringUtils.isBlank(token)){
//            return CommonResult.error(444,"未登录，请前往登录");
//        }
        String data = MapUtil.getStr(map, "data");

        String s = null;
        try {
            s = RSAUtils.publicDecrypt(data, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map1 = JSONUtil.toBean(s, Map.class);

        String orderNo = MapUtil.getStr(map1, "orderNo");

        JSONObject jsonObject = null;

        if(StringUtils.isEmpty(orderNo)){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"港车编号为空")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"港车编号为空");
        }

        QueryWrapper<HgTruck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgTruck::getTruckNo,orderNo);
        queryWrapper.lambda().eq(HgTruck::getVoided,0);
        HgTruck hgTruck = hgTruckService.getOne(queryWrapper);
        if(hgTruck == null){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"港车编号不存在")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"港车编号不存在");
        }
        hgTruck.setStateFlag(0);
        hgTruck.setPushTms(0);

        boolean result = hgTruckService.saveOrUpdate(hgTruck);
        if(!result){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"车次状态修改失败")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"车次状态修改失败");
        }
        jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.success()));
        return JSONUtil.toJsonStr(jsonObject);
//        return CommonResult.success();
    }

    @ApiOperation(value = "获取载货清单")
    @PostMapping(value = "/getManifest")
    public String getManifest(@RequestBody Map<String,Object> map, HttpServletRequest request) {
//        String token = request.getHeader("token");
//        if(StringUtils.isBlank(token)){
//            return CommonResult.error(444,"未登录，请前往登录");
//        }
        String data = MapUtil.getStr(map, "data");

        String s = null;
        try {
            s = RSAUtils.publicDecrypt(data, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map1 = JSONUtil.toBean(s, Map.class);

        String exHkNo = MapUtil.getStr(map1, "exHkNo");
        String truckNo = MapUtil.getStr(map1, "truckNo");
        String userName = MapUtil.getStr(map1, "userName");

        JSONObject jsonObject = null;

        if(StringUtils.isEmpty(truckNo)){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"港车编号为空")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"港车编号为空");
        }
        if(StringUtils.isEmpty(exHkNo)){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"载货清单为空")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"载货清单为空");
        }

        QueryWrapper<HgTruck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgTruck::getTruckNo,truckNo);
        queryWrapper.lambda().eq(HgTruck::getVoided,0);
        HgTruck hgTruck = hgTruckService.getOne(queryWrapper);
        if(hgTruck == null){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"港车编号不存在")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"港车编号不存在");
        }

        boolean result = hgTruckService.getManifest(exHkNo,truckNo,userName);
        if(!result){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"载货清单修改失败")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"载货清单修改失败");
        }
        jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.success()));
        return JSONUtil.toJsonStr(jsonObject);
//        return CommonResult.success();
    }

    @ApiOperation(value = "接受运输公司等信息")
    @PostMapping(value = "/acceptTransportationInformation")
    public String acceptTransportationInformation(@RequestBody Map<String,Object> map,HttpServletRequest request) {

//        String token = request.getHeader("token");
//        if(StringUtils.isBlank(token)){
//            return CommonResult.error(444,"未登录，请前往登录");
//        }

        String data = MapUtil.getStr(map, "data");

        String s = null;
        try {
            s = RSAUtils.publicDecrypt(data, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        AddHgTruckForm form = JSONUtil.toBean(s, AddHgTruckForm.class);

        JSONObject jsonObject = null;

        if(StringUtils.isEmpty(form.getTruckCompany())){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"运输公司不能为空")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"运输公司不能为空");
        }

        QueryWrapper<HgTruck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgTruck::getTruckNo,form.getTruckNo());
        queryWrapper.lambda().eq(HgTruck::getVoided,0);
        HgTruck hgTruck = hgTruckService.getOne(queryWrapper);
        if(hgTruck == null){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"港车编号不存在")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"港车编号不存在");
        }
        form.setId(hgTruck.getId());

        boolean result = hgTruckService.acceptTransportationInformation(form);
        if(!result){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"接收成功，运输信息修改失败")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"接收成功，运输信息修改失败");
        }
        jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.success()));
        return JSONUtil.toJsonStr(jsonObject);
//        return CommonResult.success();
    }

    //登录
    public String doLogin(OutAuthenticationForm form) {
        form.setLoginname(loginName);
        form.setPassword(password);
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
        System.out.println("loginnamefeedback:"+feedback);
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
        String encodeValue = DigestUtils.md5DigestAsHex(("杨小兰" + jsonStr + "123456").getBytes());
        System.out.println(encodeValue);
        String token = TokenGenerator.generateValue(encodeValue);
        System.out.println(token);
    }


}
