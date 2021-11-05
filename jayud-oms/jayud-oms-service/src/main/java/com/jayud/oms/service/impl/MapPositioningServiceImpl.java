package com.jayud.oms.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.jayud.common.entity.MapEntity;
import com.jayud.common.exception.JayudBizException;
import com.jayud.oms.service.MapPositioningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MapPositioningServiceImpl implements MapPositioningService {

    /**
     * 获取腾讯地图经纬度
     *
     * @param address
     * @param key
     * @return
     */
    @Override
    public JSONObject getTencentMapLaAndLoInfo(String address, String key) {
        HttpResponse response = HttpRequest.get("https://apis.map.qq.com/ws/geocoder/v1/?address=" + address + "&key=" + key)
                .execute();
        String feedback = response.body();
        JSONObject jsonObject = new JSONObject(feedback);
        if (jsonObject.getInt("status") != 0) {
            log.warn(jsonObject.getStr("message"));
            return jsonObject;
        }
        return jsonObject;
    }

    /**
     * 获取腾讯地图经纬度
     *
     * @param address
     * @param key
     * @return
     */
    @Override
    public MapEntity getTencentMapLaAndLo(String address, String key) {
        JSONObject jsonObject = this.getTencentMapLaAndLoInfo(address, key);
        if (jsonObject.getInt("status") != 0) {
            log.warn("地址:" + address + " " + jsonObject.getStr("message"));
            throw new JayudBizException(jsonObject.getInt("status"), jsonObject.getStr("message"));
        }
        MapEntity map = new MapEntity();
        JSONObject result = jsonObject.getJSONObject("result");
        map.setAddrName(result.getStr("title"));
        JSONObject location = result.getJSONObject("location");
        map.setLongitude(location.getDouble("lng"));
        map.setLatitude(location.getDouble("lat"));
        return map;
    }

}
