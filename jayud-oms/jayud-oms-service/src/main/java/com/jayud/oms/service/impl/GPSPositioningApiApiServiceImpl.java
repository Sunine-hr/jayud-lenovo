package com.jayud.oms.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.enums.GPSTypeEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.oms.model.po.GpsPositioning;
import com.jayud.oms.model.vo.gps.GPSYGTResponse;
import com.jayud.oms.service.GPSPositioningApiService;
import org.springframework.stereotype.Service;

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
public class GPSPositioningApiApiServiceImpl implements GPSPositioningApiService {

//    @Value("${address.positionsAddress}")
//    private String positionsAddress;

    @Override
    public Object getPositionsObj(List<String> licensePlates, Integer gpsType, Map<String, Object> params) {
//        String supplierCode = MapUtil.getStr(params, "supplierCode");
        GPSTypeEnum gpsEnum = GPSTypeEnum.getEnum(gpsType);
        switch (gpsEnum) {
            case ONE:
                return this.getYGTPositions(licensePlates, params);
            case TWO:
                break;
            case FOUND_NOT:
                throw new JayudBizException(400, gpsEnum.getDesc());
        }
        return null;
    }

    @Override
    public List<GpsPositioning> convertDatas(Object list) {
        List<Object> tmps = (List<Object>) list;
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
                positionings.add(gpsPositioning);
            }

        }

        return positionings;
    }

    private List<GPSYGTResponse> getYGTPositions(List<String> licensePlates, Map<String, Object> params) {
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
}
