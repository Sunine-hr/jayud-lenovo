package com.jayud.tms.schedule;

import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.RedisUtils;
import com.jayud.common.entity.MapEntity;
import com.jayud.common.utils.DateUtils;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.model.po.DeliveryAddress;
import com.jayud.tms.service.IDeliveryAddressService;
import com.jayud.tms.service.IOrderSendCarsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定时任务
 */
@Component
@Slf4j
@EnableScheduling
public class ScheduledTask {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private OmsClient omsClient;
    @Value("${tencentMap.key}")
    private String tencentMapKey;
    @Autowired
    private IDeliveryAddressService deliveryAddressService;

    /**
     * 定时同步中港地址经纬度
     * corn表达式格式：秒 分 时 日 月 星期 年（可选）
     * 0/7 * * * * ?        代表每7秒执行一次
     * 0 0 4 1 * ?          每月1号凌晨4点触发
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void syncMainOrderData() {
        log.info("*********   定时同步中港地址经纬度 :" + DateUtils.LocalDateTime2Str(LocalDateTime.now(), DateUtils.DATE_TIME_PATTERN) + "  **************");
        StopWatch stopWatch = new StopWatch();
        // 开始时间
        stopWatch.start();
        //获取缓存地址
        String tmsDispatchAddress = redisUtils.get("tmsDispatchAddress");
        JSONArray jsonArray = new JSONArray(tmsDispatchAddress);
        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MapEntity mapEntity = omsClient.getTencentMapLaAndLo(jsonObject.getStr("address"), tencentMapKey).getData();
                if (mapEntity != null) {
                    DeliveryAddress tmp = new DeliveryAddress().setId(jsonObject.getLong("id")).setLoAndLa(mapEntity.getLongitude() + "," + mapEntity.getLatitude());
                    this.deliveryAddressService.updateById(tmp);
                    jsonArray.remove(jsonObject);
                }
            }
            redisUtils.set("tmsDispatchAddress", jsonArray.toString());
        }


        // 结束时间
        stopWatch.stop();
        log.info("********* 定时同步中港地址经纬度 (单位:秒): " + stopWatch.getTotalTimeSeconds() + " 秒. **************");
    }


}
