package com.jayud.oms.service;

import cn.hutool.json.JSONObject;
import com.jayud.common.entity.MapEntity;

public interface MapPositioningService {

    /**
     * 获取腾讯地图经纬度详情
     *
     * @param address
     * @param key
     * @return
     */
    public JSONObject getTencentMapLaAndLoInfo(String address, String key);

    /**
     * 获取腾讯地图经纬度
     *
     * @param address
     * @param key
     * @return
     */
    public MapEntity getTencentMapLaAndLo(String address, String key);
}
