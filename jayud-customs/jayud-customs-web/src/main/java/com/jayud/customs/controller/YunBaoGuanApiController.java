package com.jayud.customs.controller;

import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.customs.feign.FileClient;
import com.jayud.customs.model.bo.InputOrderCustomsForm;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.service.IOrderCustomsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "customs对接云报关接口")
public class YunBaoGuanApiController {

    @Autowired
    IOrderCustomsService orderCustomsService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    private FileClient fileClient;


    /**
     * 根据云报关状态，更新oms系统报关单状态
     */
    public void UpdateBGProcess(){
        //获取报关打单及其之后状态的所有订单
        List<String> statuses = new ArrayList<>();
        statuses.add(OrderStatusEnum.CUSTOMS_C_3.getCode());
        statuses.add(OrderStatusEnum.CUSTOMS_C_4.getCode());
        statuses.add(OrderStatusEnum.CUSTOMS_C_5.getCode());
        statuses.add(OrderStatusEnum.CUSTOMS_C_6.getCode());
        List<OrderCustoms> orderCustoms = orderCustomsService.getOrderCustomsByStatus(statuses);

    }

    /**
     * 获取云报关的数据，根据委托号
     */


}









    



