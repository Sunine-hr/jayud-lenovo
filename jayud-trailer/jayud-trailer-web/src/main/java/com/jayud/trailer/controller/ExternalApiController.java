package com.jayud.trailer.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.entity.InitChangeStatusVO;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.SubOrderCloseOpt;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.trailer.bo.AddTrailerOrderFrom;
import com.jayud.trailer.feign.OauthClient;
import com.jayud.trailer.feign.OmsClient;
import com.jayud.trailer.po.TrailerOrder;
import com.jayud.trailer.service.ITrailerOrderService;
import com.jayud.trailer.vo.TrailerOrderInfoVO;
import com.jayud.trailer.vo.TrailerOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 被外部模块调用的处理接口
 *
 * @author
 * @description
 * @Date:
 */
@RestController
@Api(tags = "拖车被外部调用的接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    private ITrailerOrderService trailerOrderService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private OauthClient oauthClient;

    /**
     * 创建拖车单
     *
     * @param addTrailerOrderFrom
     * @return
     */
    @RequestMapping(value = "/api/trailer/createOrder")
    public ApiResult<String> createOrder(@RequestBody AddTrailerOrderFrom addTrailerOrderFrom) {
        String order = trailerOrderService.createOrder(addTrailerOrderFrom);
        return ApiResult.ok(order);
    }

    /**
     * 根据主订单号获取拖车订单信息
     */
    @RequestMapping(value = "/api/trailer/getTrailerOrderDetails")
    ApiResult<List<TrailerOrderVO>> getTrailerOrderDetails(@RequestParam("orderNo") String orderNo) {
        List<TrailerOrder> trailerOrders = trailerOrderService.getByMainOrderNO(orderNo);
        List<TrailerOrderVO> list = new ArrayList<>();
        for (TrailerOrder trailerOrder : trailerOrders) {
            TrailerOrderVO trailerOrderVO = trailerOrderService.getTrailerOrderByOrderNO(trailerOrder.getId());
            list.add(trailerOrderVO);
        }
        return ApiResult.ok(list);
    }

    /**
     * 根据主订单号集合获取拖车订单信息
     *
     * @param mainOrderNoList
     * @return
     */
    @RequestMapping(value = "/api/trailer/getTrailerOrderByMainOrderNos")
    ApiResult getTrailerOrderByMainOrderNos(@RequestBody List<String> mainOrderNoList) {
        List<List<TrailerOrder>> trailerOrderList = new ArrayList<>();
        for (String s : mainOrderNoList) {
            List<TrailerOrder> trailerOrders = this.trailerOrderService.getByMainOrderNO(s);
            trailerOrderList.add(trailerOrders);
        }
        return ApiResult.ok(trailerOrderList);
    }

    @RequestMapping(value = "/api/trailer/deleteOrderByMainOrderNo")
    ApiResult deleteOrderByMainOrderNo(@RequestParam("mainOrderNo") String mainOrderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("main_order_no", mainOrderNo);
        boolean remove = trailerOrderService.remove(queryWrapper);
        if (!remove) {
            return ApiResult.error();
        }
        return ApiResult.ok();
    }

    /**
     * 删除该主订单对应的商品和地址信息
     *
     * @param mainOrderNo
     * @return
     */
    @RequestMapping(value = "/api/trailer/getOrderNosByMainOrderNo")
    ApiResult<List<String>> getOrderNosByMainOrderNo(@RequestParam("mainOrderNo") String mainOrderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("main_order_no", mainOrderNo);
        List<TrailerOrder> byMainOrderNO = trailerOrderService.getByMainOrderNO(mainOrderNo);
        List<String> orderNos = new ArrayList<>();
        for (TrailerOrder trailerOrder : byMainOrderNO) {
            orderNos.add(trailerOrder.getOrderNo());
        }
        return ApiResult.ok(orderNos);
    }


    /**
     * 根据主订单号查询所有详情
     *
     * @param mainOrderNos
     * @return
     */
    @RequestMapping(value = "/api/trailer/getTrailerInfoByMainOrderNos")
    public ApiResult<List<TrailerOrderInfoVO>> getTrailerInfoByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos) {
        List<TrailerOrderInfoVO> trailerOrders = this.trailerOrderService.getInfo(mainOrderNos);
        return ApiResult.ok(trailerOrders);
    }

    @ApiModelProperty(value = "获取菜单待处理数")
    @RequestMapping(value = "/api/trailer/getMenuPendingNum")
    public ApiResult getMenuPendingNum(@RequestBody List<Map<String, Object>> menusList) {
        if (CollectionUtil.isEmpty(menusList)) {
            return ApiResult.ok();
        }
        Map<String, String> tmp = new HashMap<>();
        tmp.put("拖车接单", "TT_0");
        tmp.put("拖车派车", "TT_1");
        tmp.put("派车驳回调度", "TT_3_2");
        tmp.put("派车审核", "TT_2");
        tmp.put("拖车提柜", "TT_3");
        tmp.put("拖车到仓", "TT_4");
        tmp.put("拖车离仓", "TT_5");
        tmp.put("拖车过磅", "TT_6");
        tmp.put("确认还柜", "TT_7");
        tmp.put("费用审核", "CostAudit");
        List<Map<String, Object>> result = new ArrayList<>();

        ApiResult<List<Long>> legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = legalEntityByLegalName.getData();

        for (Map<String, Object> menus : menusList) {

            Map<String, Object> map = new HashMap<>();
            Object title = menus.get("title");
            String status = tmp.get(title);
            Integer num = 0;
            if (status != null) {
                num = this.trailerOrderService.getNumByStatus(status, legalIds);
            }
            map.put("menusName", title);
            map.put("num", num);
            result.add(map);
        }
        return ApiResult.ok(result);
    }

    @ApiOperation(value = "获取拖车订单号")
    @RequestMapping(value = "/api/trailer/getOrderNo")
    public ApiResult<List<InitChangeStatusVO>> getTrailerOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        InitChangeStatusVO initChangeStatusVO = new InitChangeStatusVO();
        List<TrailerOrder> list = this.trailerOrderService.getByCondition(new TrailerOrder().setMainOrderNo(mainOrderNo));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            TrailerOrder tmp = list.get(0);
            initChangeStatusVO.setId(tmp.getId());
            initChangeStatusVO.setOrderNo(tmp.getOrderNo());
            initChangeStatusVO.setOrderType(CommonConstant.TC);
            initChangeStatusVO.setOrderTypeDesc(CommonConstant.TC_DESC);
            initChangeStatusVO.setStatus(tmp.getProcessStatus() + "");
            initChangeStatusVO.setNeedInputCost(tmp.getNeedInputCost());
            return ApiResult.ok(initChangeStatusVO);
        }
        return ApiResult.error();
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/api/closeOrder")
    public ApiResult closeOrder(@RequestBody List<SubOrderCloseOpt> form) {
        List<String> orderNos = form.stream().map(SubOrderCloseOpt::getOrderNo).collect(Collectors.toList());
        List<TrailerOrder> list = this.trailerOrderService.getOrdersByOrderNos(orderNos);
        Map<String, TrailerOrder> map = list.stream().collect(Collectors.toMap(TrailerOrder::getOrderNo, e -> e));

        for (SubOrderCloseOpt subOrderCloseOpt : form) {
            TrailerOrder tmp = map.get(subOrderCloseOpt.getOrderNo());
            TrailerOrder trailerOrder = new TrailerOrder();
            trailerOrder.setId(tmp.getId());
            trailerOrder.setProcessStatus(ProcessStatusEnum.CLOSE.getCode());
            trailerOrder.setNeedInputCost(subOrderCloseOpt.getNeedInputCost());
            trailerOrder.setUpdateUser(subOrderCloseOpt.getLoginUser());
            trailerOrder.setUpdateTime(LocalDateTime.now());

            this.trailerOrderService.updateById(trailerOrder);

        }
        return ApiResult.ok();
    }
}
