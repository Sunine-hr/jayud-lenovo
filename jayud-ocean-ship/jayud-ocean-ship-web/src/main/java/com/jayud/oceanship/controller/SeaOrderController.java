package com.jayud.oceanship.controller;


import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oceanship.bo.QuerySeaOrderForm;
import com.jayud.oceanship.feign.OauthClient;
import com.jayud.oceanship.feign.OmsClient;
import com.jayud.oceanship.service.ISeaOrderService;
import com.jayud.oceanship.vo.GoodsVO;
import com.jayud.oceanship.vo.SeaOrderFormVO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 海运订单表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@RestController
@RequestMapping("/seaOrder")
public class SeaOrderController {

    @Autowired
    private ISeaOrderService seaOrderService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private OauthClient oauthClient;

    @ApiOperation("分页查询海运订单列表")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QuerySeaOrderForm form){
        //模糊查询客户信息
        if (!StringUtils.isEmpty(form.getCustomerName())) {
            ApiResult result = omsClient.getByCustomerName(form.getCustomerName());
            Object data = result.getData();
            if (data != null && ((List) data).size() > 0) {
                JSONArray mainOrders = new JSONArray(data);
                form.assemblyMainOrderNo(mainOrders);
            } else {
                form.setMainOrderNos(Collections.singletonList("-1"));
            }
        }

        IPage<SeaOrderFormVO> page = this.seaOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            return CommonResult.success(new CommonPageResult<>(page));
        }

        List<SeaOrderFormVO> records = page.getRecords();
        List<Long> airOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
        List<Long> supplierIds = new ArrayList<>();
        for (SeaOrderFormVO record : records) {
            airOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            supplierIds.add(record.getSupplierId());
        }
        //查询商品信息
        List<GoodsVO> goods = this.omsClient.getGoodsByBusIds(airOrderIds, BusinessTypeEnum.HY.getCode()).getData();
        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }
        //查询供应商信息
        JSONArray supplierInfo = null;
        if (CollectionUtils.isNotEmpty(supplierIds)) {
            supplierInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(supplierIds).getData());
        }


        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (SeaOrderFormVO record : records) {
            //组装商品信息
            record.assemblyGoodsInfo(goods);
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);
            //拼装供应商
            record.assemblySupplierInfo(supplierInfo);
        }
        Map map = new HashMap<>();
        //获取表头信息
        Class<SeaOrderFormVO> seaOrderFormVOClass = SeaOrderFormVO.class;
        Field[] declaredFields = seaOrderFormVOClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
            if(annotation!=null){
                map.put(declaredField.getName(),annotation.value());
            }
        }
        Map map1 = new HashMap();
        map1.put("pageInfo",new CommonPageResult(page));
        map1.put("header",map);
        return CommonResult.success();
    }
}

