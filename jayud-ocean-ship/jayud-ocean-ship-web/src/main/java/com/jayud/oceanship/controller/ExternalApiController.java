package com.jayud.oceanship.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.oceanship.bo.AddSeaOrderForm;
import com.jayud.oceanship.feign.OauthClient;
import com.jayud.oceanship.po.SeaOrder;
import com.jayud.oceanship.service.ICabinetSizeNumberService;
import com.jayud.oceanship.service.ISeaOrderService;
import com.jayud.oceanship.vo.CabinetSizeNumberVO;
import com.jayud.oceanship.vo.SeaOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 被外部模块调用的处理接口
 *
 * @author
 * @description
 * @Date:
 */
@RestController
@Api(tags = "海运被外部调用的接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    private ISeaOrderService seaOrderService;

    @Autowired
    private ICabinetSizeNumberService cabinetSizeNumberService;

    @Autowired
    private OauthClient oauthClient;

    /**
     * 创建海运单
     * @param addSeaOrderForm
     * @return
     */
    @RequestMapping(value = "/api/oceanship/createOrder")
    public ApiResult<String> createOrder(@RequestBody AddSeaOrderForm addSeaOrderForm) {

        String orderNo = seaOrderService.createOrder(addSeaOrderForm);
        return ApiResult.ok(orderNo);
    }

    /**
     * 根据主订单号获取海运单信息
     */
    @RequestMapping(value = "/api/oceanship/getSeaOrderDetails")
    ApiResult<SeaOrderVO> getSeaOrderDetails(@RequestParam("orderNo")String orderNo){
        SeaOrder seaOrder = seaOrderService.getByMainOrderNO(orderNo);
        SeaOrderVO seaOrderVO = seaOrderService.getSeaOrderByOrderNO(seaOrder.getId());
        //获取柜型数量

        if(CollectionUtils.isEmpty(seaOrderVO.getCabinetSizeNumbers())){
            List<CabinetSizeNumberVO> cabinetSizeNumberVOS = new ArrayList<>();
            cabinetSizeNumberVOS.add(new CabinetSizeNumberVO());
            seaOrderVO.setCabinetSizeNumbers(cabinetSizeNumberVOS);
        }


        return ApiResult.ok(seaOrderVO);
    }

    /**
     * 根据主订单号集合获取海运订单信息
     * @param mainOrderNoList
     * @return
     */
    @RequestMapping(value = "/api/oceanship/getSeaOrderByMainOrderNos")
    ApiResult getSeaOrderByMainOrderNos(@RequestBody List<String> mainOrderNoList){
        List<SeaOrder> seaOrders = this.seaOrderService.getSeaOrderByOrderNOs(mainOrderNoList);
        return ApiResult.ok(seaOrders);
    }


    @ApiModelProperty(value = "获取菜单待处理数")
    @RequestMapping(value = "/api/getMenuPendingNum")
    public ApiResult getMenuPendingNum(@RequestBody List<Map<String, Object>> menusList) {
        if (CollectionUtil.isEmpty(menusList)) {
            return ApiResult.ok();
        }
        Map<String, String> tmp = new HashMap<>();
        tmp.put("海运接单", "S_0");
        tmp.put("海运订船", "S_1");
        tmp.put("订船驳回", "S_3_2");
        tmp.put("确认入仓", "S_2");
        tmp.put("提交补料", "S_3");
        tmp.put("补料审核", "S_4");
        tmp.put("提单草稿确认", "S_5");
        tmp.put("确认装船", "S_6");
        tmp.put("放单确认", "S_7");
        tmp.put("确认到港", "S_8");
        tmp.put("海外代理", "S_9");
        tmp.put("订单签收", "S_10");
        List<Map<String, Object>> result = new ArrayList<>();

        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        for (Map<String, Object> menus : menusList) {

            Map<String, Object> map = new HashMap<>();
            Object title = menus.get("title");
            String status = tmp.get(title);
            Integer num = 0;
            if (status != null) {
                num = this.seaOrderService.getNumByStatus(status, legalIds);
            }
            map.put("menusName", title);
            map.put("num", num);
            result.add(map);
        }
        return ApiResult.ok(result);
    }

}
