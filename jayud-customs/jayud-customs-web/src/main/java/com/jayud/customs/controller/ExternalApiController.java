package com.jayud.customs.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.customs.feign.FileClient;
import com.jayud.customs.feign.OauthClient;
import com.jayud.customs.model.bo.CustomsChangeStatusForm;
import com.jayud.customs.model.bo.InputOrderCustomsForm;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.InitChangeStatusVO;
import com.jayud.customs.model.vo.InputOrderCustomsVO;
import com.jayud.customs.model.vo.OrderCustomsVO;
import com.jayud.customs.service.IOrderCustomsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@Api(tags = "customs对外接口")
public class ExternalApiController {

    @Autowired
    IOrderCustomsService orderCustomsService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    private FileClient fileClient;
    @Autowired
    private OauthClient oauthClient;


    @ApiOperation(value = "获取子订单信息")
    @RequestMapping(value = "/api/getCustomsOrderNum")
    ApiResult getCustomsOrderNum(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        int customsNum = 0;
        Map<String, Object> param = new HashMap<>();
        param.put("main_order_no", mainOrderNo);
        List<OrderCustomsVO> orderCustomsVOS = orderCustomsService.findOrderCustomsByCondition(param);
        if (orderCustomsVOS != null) {
            customsNum = orderCustomsVOS.size();
        }
        return ApiResult.ok(customsNum);
    }

    @ApiOperation(value = "获取子订单详情")
    @RequestMapping(value = "/api/getCustomsDetail")
    ApiResult getCustomsDetail(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        InputOrderCustomsVO inputOrderCustomsVO = orderCustomsService.getOrderCustomsDetail(mainOrderNo);
        return ApiResult.ok(inputOrderCustomsVO);
    }


    /**
     * 创建报关单
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/createOrderCustoms")
    ApiResult createOrderCustoms(@RequestBody InputOrderCustomsForm form) {
        boolean result = orderCustomsService.oprOrderCustoms(form);
        return ApiResult.ok(result);
    }

    @ApiOperation(value = "获取报关订单单号")
    @RequestMapping(value = "/api/findCustomsOrderNo")
    ApiResult findCustomsOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.MAIN_ORDER_NO, mainOrderNo);
        List<OrderCustoms> orderCustoms = orderCustomsService.list(queryWrapper);
        List<InitChangeStatusVO> changeStatusVOS = new ArrayList<>();
        for (OrderCustoms orderCustom : orderCustoms) {
            InitChangeStatusVO initChangeStatusVO = new InitChangeStatusVO();
            initChangeStatusVO.setId(orderCustom.getId());
            initChangeStatusVO.setOrderNo(orderCustom.getOrderNo());
            initChangeStatusVO.setOrderType(CommonConstant.BG);
            initChangeStatusVO.setNeedInputCost(orderCustom.getNeedInputCost());
            initChangeStatusVO.setStatus(orderCustom.getStatus());
            changeStatusVOS.add(initChangeStatusVO);
        }
        return ApiResult.ok(changeStatusVOS);
    }

    @ApiOperation(value = "修改报关状态")
    @RequestMapping(value = "/api/changeCustomsStatus")
    ApiResult changeCustomsStatus(@RequestBody List<CustomsChangeStatusForm> form) {
        for (CustomsChangeStatusForm customs : form) {
            OrderCustoms orderCustoms = new OrderCustoms();
            orderCustoms.setStatus(customs.getStatus());
            orderCustoms.setNeedInputCost(customs.getNeedInputCost());
            orderCustoms.setUpdatedUser(customs.getLoginUser());
            orderCustoms.setUpdatedTime(LocalDateTime.now());
            QueryWrapper<OrderCustoms> updateWrapper = new QueryWrapper<>();
            updateWrapper.eq(SqlConstant.ORDER_NO, customs.getOrderNo());
            orderCustomsService.update(orderCustoms, updateWrapper);
        }
        return ApiResult.ok();
    }


    @ApiOperation(value = "根据主订单集合查询所有报关信息")
    @RequestMapping(value = "/api/getCustomsOrderByMainOrderNos")
    ApiResult getCustomsOrderByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos) {
        return ApiResult.ok(this.orderCustomsService.getCustomsOrderByMainOrderNos(mainOrderNos));
    }

    @ApiOperation(value = "根据主订单查询六联单号附件")
    @RequestMapping(value = "/api/getEncodePicByMainOrderNo")
    public ApiResult getEncodePicByMainOrderNo(@RequestParam("mainOrderNos") String mainOrderNo) {
        List<OrderCustoms> orderCustoms = this.orderCustomsService.getCustomsOrderByMainOrderNos(Collections.singletonList(mainOrderNo));
        List<FileView> fileViews = new ArrayList<>();
        Object url = fileClient.getBaseUrl().getData();
        for (int i = 1; i < orderCustoms.size(); i++) {
            OrderCustoms orderCustom = orderCustoms.get(i);
            if (!StringUtils.isEmpty(orderCustom.getEncodePicName())) {
                List<FileView> files = StringUtils.getFileViews(orderCustom.getEncodePic(),
                        orderCustom.getEncodePicName(), url.toString());
                fileViews.addAll(files);
            }
        }

        return ApiResult.ok(fileViews);
    }


    @ApiModelProperty(value = "获取菜单待处理数")
    @RequestMapping(value = "/api/getMenuPendingNum")
    public ApiResult getMenuPendingNum(@RequestBody List<Map<String, Object>> menusList) {
        if (CollectionUtil.isEmpty(menusList)) {
            return ApiResult.ok();
        }
        Map<String, String> tmp = new HashMap<>();
        tmp.put("报关接单", "C_0");
        tmp.put("报关打单", "C_1");
        tmp.put("报关复核", "C_2");
        tmp.put("报关二复", "C_3");
        tmp.put("申报舱单", "C_9");
        tmp.put("报关申报", "C_11");
        tmp.put("报关放行", "C_4");
        tmp.put("放行审核", "C_10");
        tmp.put("驳回列表", "C_5_1");
        tmp.put("通关确认", "C_5");

        List<Map<String, Object>> result = new ArrayList<>();

        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(UserOperator.getToken());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        for (Map<String, Object> menus : menusList) {

            Map<String, Object> map = new HashMap<>();
            Object title = menus.get("title");
            String status = tmp.get(title);
            Integer num = 0;
            if (status != null) {
                num = this.orderCustomsService.getNumByStatus(status, legalIds);
            }
            map.put("menusName", title);
            map.put("num", num);
            result.add(map);
        }
        return ApiResult.ok(result);
    }
}









    



