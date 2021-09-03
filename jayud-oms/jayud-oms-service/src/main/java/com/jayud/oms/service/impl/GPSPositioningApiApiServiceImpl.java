package com.jayud.oms.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.nacos.common.util.Md5Utils;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.GPSTypeEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.MD5;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.po.GpsPositioning;
import com.jayud.oms.model.vo.gps.GPSBeiDouResponse;
import com.jayud.oms.model.vo.gps.GPSYGTResponse;
import com.jayud.oms.service.GPSPositioningApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * GPS定位
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
@Service
@Slf4j
public class GPSPositioningApiApiServiceImpl implements GPSPositioningApiService {

    //    @Value("${address.positionsAddress}")
//    private String positionsAddress;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Object getPositionsObj(List<String> licensePlates, Integer gpsType, Map<String, Object> params) {
//        String supplierCode = MapUtil.getStr(params, "supplierCode");
        GPSTypeEnum gpsEnum = GPSTypeEnum.getEnum(gpsType);
        switch (gpsEnum) {
            case ONE:
                return this.getYGTPositions(licensePlates, params);
            case TWO:
                return this.getBeiDouPositions(licensePlates, params);
            case FOUND_NOT:
                throw new JayudBizException(400, gpsEnum.getDesc());
        }
        return null;
    }

    /**
     * 获取历史轨迹
     *
     * @param plateNumber 车牌
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param gpsType
     * @param params
     * @return
     */
    @Override
    public Object getHistory(String plateNumber, LocalDateTime startTime, LocalDateTime endTime,
                             Integer gpsType, Map<String, Object> params) {
        GPSTypeEnum gpsEnum = GPSTypeEnum.getEnum(gpsType);
        switch (gpsEnum) {
            case ONE:
                return this.getYGTHistory(plateNumber, startTime, endTime, params);
            case TWO:
                return this.getBeiDouHistory(plateNumber, startTime, endTime, params);
            case FOUND_NOT:
                throw new JayudBizException(400, gpsEnum.getDesc());
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.LocalDateTime2Str(LocalDateTime.now(), DateUtils.TIMESTAMP_PATTERN));
    }

    @Override
    public List<GPSYGTResponse> getYGTHistory(String plateNumber, LocalDateTime startTime, LocalDateTime endTime, Map<String, Object> params) {
        String appKey = MapUtil.getStr(params, "appKey");
        String gpsAddress = MapUtil.getStr(params, "gpsAddress");
        JSONObject requestParam = new JSONObject();
        requestParam.put("AccessToken", appKey);
        requestParam.put("LicenceNumber", plateNumber);
        requestParam.put("Begin", DateUtils.LocalDateTime2Str(startTime, DateUtils.DATE_PATTERN));
        requestParam.put("End", DateUtils.LocalDateTime2Str(endTime, DateUtils.DATE_PATTERN));
        //组装请求的参数
        HttpResponse response = HttpRequest.post(gpsAddress + "/GetHistory")
                .body(requestParam.toString())
                .execute();
        String feedback = response.body();
        JSONObject responseJson = new JSONObject(feedback);
        if (!responseJson.getBool("Success")) {
            throw new JayudBizException(400, responseJson.getStr("Message"));
        }
        JSONArray positions = responseJson.getJSONArray("Data");
        List<GPSYGTResponse> tmp = positions.toList(GPSYGTResponse.class);
        tmp.forEach(e -> e.setLicenceNumber(responseJson.getStr("LicenceNumber")));
        return tmp;
    }

    @Override
    public List<GPSBeiDouResponse.historicalPos> getBeiDouHistory(String plateNumber, LocalDateTime startTime, LocalDateTime endTime,
                                                                  Map<String, Object> params) {

        String userId = MapUtil.getStr(params, "userId", "");
        String loginType = MapUtil.getStr(params, "loginType", "");
        String loginWay = MapUtil.getStr(params, "loginWay", "");
        String gpsAddress = MapUtil.getStr(params, "gpsAddress");
        String sessionId = this.getBeiDouSessionId(params);
        String startTimeStr = DateUtils.LocalDateTime2Str(startTime, DateUtils.TIMESTAMP_PATTERN);
        String endTimeStr = DateUtils.LocalDateTime2Str(endTime, DateUtils.TIMESTAMP_PATTERN);
        //组装请求的参数
        HttpResponse response = HttpRequest.get(gpsAddress + "/gps-web/api/get_gps_h_plate.jsp?" +
                "carPlate=" + plateNumber + "&startTime=" + startTimeStr + "&endTime=" + endTimeStr + " &userId=" + userId + "&sessionId=" + sessionId + "&loginType=" + loginType + "&loginWay=" + loginWay)
                .execute();
        String feedback = response.body();
        JSONObject responseJson = new JSONObject(feedback);
        if (!responseJson.getBool("rspCode")) {
            log.warn("实时定位失败,车牌号={},错误信息={}", plateNumber, responseJson.getStr("rspDesc"));
        }
        List<GPSBeiDouResponse.historicalPos> list = responseJson.getJSONArray("list").toList(GPSBeiDouResponse.historicalPos.class);
        return list;
    }

    @Override
    public List<GpsPositioning> convertDatas(Object list) {
        List<Object> tmps = (List<Object>) list;
        if (CollectionUtils.isEmpty(tmps)) return null;
        Object obj = tmps.get(0);
        List<GpsPositioning> positionings = new ArrayList<>();
        if (obj instanceof GPSYGTResponse) {
            for (Object o : tmps) {
                GPSYGTResponse tmp = (GPSYGTResponse) o;
                GpsPositioning gpsPositioning = new GpsPositioning();
                gpsPositioning.setVehicleStatus(tmp.getAccState() == 1 ? "点火" : "熄火");
                gpsPositioning.setDirection(tmp.getDirection().toString());
                gpsPositioning.setLatitude(tmp.getLatitude().toString());
                gpsPositioning.setLongitude(tmp.getLongitude().toString());
                gpsPositioning.setSpeed(tmp.getSpeed().toString());
                gpsPositioning.setPlateNumber(tmp.getLicenceNumber());
                gpsPositioning.setType(GPSTypeEnum.ONE.getCode());
                gpsPositioning.setGpsTime(tmp.getReportTime());
                positionings.add(gpsPositioning);
            }
        } else if (obj instanceof GPSBeiDouResponse.realTimePos) {
            for (Object o : tmps) {
                GPSBeiDouResponse.realTimePos tmp = (GPSBeiDouResponse.realTimePos) o;
                GpsPositioning gpsPositioning = new GpsPositioning();
                gpsPositioning.setVehicleStatus(tmp.getStateCn());
                gpsPositioning.setDirection(tmp.getDrct());
                gpsPositioning.setLatitude(tmp.getLat());
                gpsPositioning.setLongitude(tmp.getLng());
                gpsPositioning.setSpeed(tmp.getSpeed());
                gpsPositioning.setPlateNumber(tmp.getCarPlate());
                gpsPositioning.setType(GPSTypeEnum.TWO.getCode());
                gpsPositioning.setGpsTime(tmp.getTime());
                positionings.add(gpsPositioning);
            }
        } else if (obj instanceof GPSBeiDouResponse.historicalPos) {
            for (Object o : tmps) {
                GPSBeiDouResponse.historicalPos tmp = (GPSBeiDouResponse.historicalPos) o;
                GpsPositioning gpsPositioning = new GpsPositioning();
                gpsPositioning.setDirection(tmp.getDrct());
                gpsPositioning.setLatitude(tmp.getLat());
                gpsPositioning.setLongitude(tmp.getLng());
                gpsPositioning.setSpeed(tmp.getSpeed());
                gpsPositioning.setPlateNumber(tmp.getCarPlate());
                gpsPositioning.setType(GPSTypeEnum.TWO.getCode());
                gpsPositioning.setGpsTime(tmp.getTime());
                positionings.add(gpsPositioning);
            }
        }

        return positionings;
    }

    /**
     * 云港通批量获取实时定位
     *
     * @param licensePlates
     * @param params
     * @return
     */
    @Override
    public List<GPSYGTResponse> getYGTPositions(List<String> licensePlates, Map<String, Object> params) {
        String appKey = MapUtil.getStr(params, "appKey");
        String gpsAddress = MapUtil.getStr(params, "gpsAddress");
        JSONObject requestParam = new JSONObject();
        requestParam.put("AccessToken", appKey);
        requestParam.put("LicenceNumbers", licensePlates);
        //组装请求的参数
        HttpResponse response = HttpRequest.post(gpsAddress + "/GetPositions")
                .body(requestParam.toString())
                .execute();
        String feedback = response.body();
        JSONObject responseJson = new JSONObject(feedback);
        if (!responseJson.getBool("Success")) {
            throw new JayudBizException(400, responseJson.getStr("Message"));
        }
        JSONArray positions = responseJson.getJSONArray("Positions");
        List<GPSYGTResponse> tmp = positions.toList(GPSYGTResponse.class);
        return tmp;
    }

    @Override
    public List<GPSBeiDouResponse.realTimePos> getBeiDouPositions(List<String> licensePlates,
                                                                  Map<String, Object> params) {
        String userId = MapUtil.getStr(params, "userId", "");
        String loginType = MapUtil.getStr(params, "loginType", "");
        String loginWay = MapUtil.getStr(params, "loginWay", "");
        String gpsAddress = MapUtil.getStr(params, "gpsAddress");
        String sessionId = this.getBeiDouSessionId(params);
        List<GPSBeiDouResponse.realTimePos> list = new ArrayList<>();
        //组装请求的参数
        licensePlates.forEach(e -> {
            HttpResponse response = HttpRequest.get(gpsAddress + "/gps-web/api/get_gps_r_plate.jsp?" +
                    "carPlate=" + e + "&userId=" + userId + "&sessionId=" + sessionId + "&loginType=" + loginType + "&loginWay=" + loginWay)
                    .execute();
            String feedback = response.body();
            JSONObject responseJson = new JSONObject(feedback);
            if (!responseJson.getBool("rspCode")) {
                log.warn("实时定位失败,车牌号={},错误信息={}", e, responseJson.getStr("rspDesc"));
            }
            GPSBeiDouResponse.realTimePos realTimePos = responseJson.getJSONArray("list").getJSONObject(0).toBean(GPSBeiDouResponse.realTimePos.class);
            list.add(realTimePos);
        });
        return list;
    }

//    /**
//     * 登录北斗
//     *
//     * @param params
//     * @return
//     */
//    private Map<String, Object> loginBeiDou(Map<String, Object> params) {
//        String password = MapUtil.getStr(params, "password", "");
//        String userId = MapUtil.getStr(params, "userId", "");
//        String loginType = MapUtil.getStr(params, "loginType", "");
//        String loginWay = MapUtil.getStr(params, "loginWay", "");
//        String gpsAddress = MapUtil.getStr(params, "gpsAddress");
//        password = MD5.getmd5(password);
//        //组装请求的参数
//        HttpResponse response = HttpRequest.get(gpsAddress + "/gps-web/api/login.jsp?" +
//                "userId=" + userId + "&password=" + password + "&loginType=" + loginType + "&loginWay=" + loginWay)
//                .execute();
//        String feedback = response.body();
//        JSONObject responseJson = new JSONObject(feedback);
//        if (!responseJson.getBool("Success")) {
//            throw new JayudBizException(400, responseJson.getStr("Message"));
//        }
//        //登录会话为空,请核实
//        JSONArray positions = responseJson.getJSONArray("Positions");
//        return positions;
//    }

    /**
     * 获取北斗会话id
     *
     * @param params
     * @return
     */
    @Override
    public String getBeiDouSessionId(Map<String, Object> params) {
        String password = MapUtil.getStr(params, "password", "");
        String userId = MapUtil.getStr(params, "userId", "");
        String loginType = MapUtil.getStr(params, "loginType", "");
        String loginWay = MapUtil.getStr(params, "loginWay", "");
        String gpsAddress = MapUtil.getStr(params, "gpsAddress");
        String sessionId = redisUtils.get("BEIDOU-" + userId + "-SESSIONID");
        if (!StringUtils.isEmpty(sessionId)) {
            return sessionId;
        }

        password = MD5.getmd5(password);
        //组装请求的参数
        HttpResponse response = HttpRequest.get(gpsAddress + "/gps-web/api/login.jsp?" +
                "userId=" + userId + "&password=" + password + "&loginType=" + loginType + "&loginWay=" + loginWay)
                .execute();
        String feedback = response.body();
        JSONObject responseJson = new JSONObject(feedback);
        if (!responseJson.getBool("rspCode")) {
            throw new JayudBizException(400, responseJson.getStr("rspDesc"));
        }
        sessionId = responseJson.getStr("sessionId");
        redisUtils.set("BEIDOU-" + userId + "-SESSIONID", sessionId, 1500);
        return sessionId;
    }


}
