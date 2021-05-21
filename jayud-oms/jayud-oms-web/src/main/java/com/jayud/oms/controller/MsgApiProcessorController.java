package com.jayud.oms.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayud.oms.model.po.CustomsReceivable;
import com.jayud.oms.service.CustomsFinanceService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 接收消息处理中心的feign请求
 *
 * @author Daniel
 */
@RestController
@RequestMapping("/api/finance/oms")
@Slf4j
@RefreshScope
public class MsgApiProcessorController {

    @Autowired
    CustomsFinanceService customsFinanceService;

    /**
     * 处理云报关的应收
     *
     * @param msg
     * @return
     */
    @RequestMapping(path = "/yunbaoguan/receivable/push", method = RequestMethod.POST)
    @ApiOperation(value = "接收云报关的应收单信息保存到OMS")
    public Boolean saveReceivableBill(@RequestBody String msg) {
        Map param = JSONObject.parseObject(msg, Map.class);
        String reqMsg = MapUtil.getStr(param, "msg");
        log.info("接收到报关应收数据：{}", reqMsg);

        //feign调用之前确保从列表中取出单行数据且非空，因此此处不需再校验
        //解析数据，获取应收的数据实体
        JSONArray receivableArray = JSONObject.parseArray(reqMsg);
        List<CustomsReceivable> customsReceivable = new ArrayList<>();
        for (Object o : receivableArray) {
            JSONObject jsonObject = (JSONObject) o;
            CustomsReceivable data = jsonObject.toJavaObject(CustomsReceivable.class);
            data.setOriginData(jsonObject);
            customsReceivable.add(data);
        }
        return customsFinanceService.saveReceivable(customsReceivable);
    }
}
