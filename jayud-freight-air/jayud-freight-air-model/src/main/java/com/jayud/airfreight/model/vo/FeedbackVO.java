package com.jayud.airfreight.model.vo;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * 与vivo交互的统一消息回执VO
 *
 * @author william
 * @description
 * @Date: 2020-09-15 11:24
 */
@Data
@AllArgsConstructor
public class FeedbackVO {
//1.接收成功，返回status=1，message=ok;
// 2.接收失败，返回status=0,message=error message;
// 3.若无返回值，可以重抛1次，若依然无返回值，需要排查接口是否畅通，人工介入
    public FeedbackVO(String feedbackJson) {
        try {
            Map<String, Object> feedbackMap = JSONUtil.toBean(feedbackJson, Map.class);
            this.status = MapUtil.getInt(feedbackMap, "status");
            this.message = MapUtil.getStr(feedbackMap, "message");
        } catch (Exception e) {
            e.printStackTrace();
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "返回数据读取失败");
        }
    }
    private Integer status;
    private String message;
}
