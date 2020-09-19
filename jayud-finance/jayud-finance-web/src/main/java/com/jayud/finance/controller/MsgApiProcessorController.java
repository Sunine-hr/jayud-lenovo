package com.jayud.finance.controller;

import cn.hutool.json.JSONUtil;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.ReceivableHeaderForm;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 接收消息处理中心的feign请求
 *
 * @author william
 * @description
 * @Date: 2020-09-19 11:44
 */
@RestController
@RequestMapping("/api/finance/kingdee")
public class MsgApiProcessorController {
    /**
     * 处理云报关的应收推送到金蝶接口
     * by william
     *
     * @param msg
     * @return
     */
    @PostMapping("/yunbaoguan/receivable/push")
    @ApiOperation(value = "接收云报关的应收单信息推至金蝶")
    public CommonResult saveReceivableBill(@RequestBody Map msg) {
        System.out.println(msg);
        return CommonResult.success();
    }
}
