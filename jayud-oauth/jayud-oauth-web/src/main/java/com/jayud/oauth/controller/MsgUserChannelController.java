package com.jayud.oauth.controller;


import com.jayud.common.CommonResult;
import com.jayud.common.enums.MsgChannelTypeEnum;
import com.jayud.oauth.model.po.MsgUserChannel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户消息渠道 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-08-03
 */
@RestController
@RequestMapping("/msgUserChannel")
public class MsgUserChannelController {

    @ApiOperation(value = "初始化消息渠道")
    @PostMapping(value = "/initMsgChannel")
    public CommonResult<List<MsgUserChannel>> initMsgChannel() {
        List<MsgUserChannel> msgUserChannels = new ArrayList<>();
        for (MsgChannelTypeEnum value : MsgChannelTypeEnum.values()) {
            MsgUserChannel msgUserChannel = new MsgUserChannel().setIsSelect(false)
                    .setName(value.getDesc()).setType(value.getCode());
            msgUserChannels.add(msgUserChannel);
        }
        return CommonResult.success(msgUserChannels);
    }
}

