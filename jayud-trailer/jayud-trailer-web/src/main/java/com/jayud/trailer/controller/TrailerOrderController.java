package com.jayud.trailer.controller;


import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.trailer.bo.QueryTrailerOrderForm;
import com.jayud.trailer.feign.FileClient;
import com.jayud.trailer.feign.OauthClient;
import com.jayud.trailer.feign.OmsClient;
import com.jayud.trailer.service.ITrailerOrderService;
import com.jayud.trailer.vo.GoodsVO;
import com.jayud.trailer.vo.OrderAddressVO;
import com.jayud.trailer.vo.TrailerOrderFormVO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 拖车订单表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@RestController
@Slf4j
@RequestMapping("/trailerOrder")
public class TrailerOrderController {

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private ITrailerOrderService trailerOrderService;

    @ApiOperation("分页查询海运订单列表")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QueryTrailerOrderForm form) {

        form.setStartTime();
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


        List list = new ArrayList();
        //获取表头信息
        Class<TrailerOrderFormVO> seaOrderFormVOClass = TrailerOrderFormVO.class;
        Field[] declaredFields = seaOrderFormVOClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
            if(annotation!=null){
                Map map = new HashMap<>();
                map.put("key",declaredField.getName());
                map.put("name",annotation.value());
                list.add(map);
            }
        }
        Map map1 = new HashMap();
        map1.put("header",list);
        IPage<TrailerOrderFormVO> page = this.trailerOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo",new CommonPageResult(page));
            return CommonResult.success(map1);
        }

        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        List<TrailerOrderFormVO> records = page.getRecords();
        List<Long> trailerOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
        List<Long> supplierIds = new ArrayList<>();
        List<Long> cabinetSizeIds = new ArrayList<>();
        List<String> portCodes = new ArrayList<>();
        List<String> unitCodes = new ArrayList<>();
        for (TrailerOrderFormVO record : records) {
            trailerOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            cabinetSizeIds.add(record.getCabinetSize());
            portCodes.add(record.getPortCode());
            unitCodes.add(record.getUnitCode());
            if(record.getTrailerDispatchVO().getSupplierId()!=null){
                supplierIds.add(record.getTrailerDispatchVO().getSupplierId());
            }
        }

        //查询商品信息
        List<GoodsVO> goods = this.omsClient.getGoodsByBusIds(trailerOrderIds, BusinessTypeEnum.HY.getCode()).getData();

        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }
        //查询供应商信息
        JSONArray supplierInfo = null;
        if (CollectionUtils.isNotEmpty(supplierIds) && supplierIds.size()>0) {
            supplierInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(supplierIds).getData());
        }

        //获取结算单位信息
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
            unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        //获取发货人信息
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusIds(trailerOrderIds, BusinessTypeEnum.TC.getCode() );
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 airOrderId={}", trailerOrderIds);
        }

        //获取港口信息
        JSONArray portCodeInfo = null;
        if (CollectionUtils.isNotEmpty(supplierIds) && supplierIds.size()>0) {
            portCodeInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(cabinetSizeIds).getData());
        }

        //获取车型信息
        InitComboxVO cabinetSizeInfo = (InitComboxVO)this.omsClient.getVehicleSizeInfo().getData();


        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (TrailerOrderFormVO record : records) {
            //组装商品信息
            List<GoodsVO> goodsVOS = new ArrayList<>();
            for (GoodsVO goodsVO : goods) {
                if (record.getId().equals(goodsVO.getBusinessId())
                        && BusinessTypeEnum.TC.getCode().equals(goodsVO.getBusinessType())) {
                    goodsVOS.add(goodsVO);
                }
            }
            record.setGoodsForms(goodsVOS);
            record.assemblyGoodsInfo(goods);
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);
            //拼装供应商
            record.assemblySupplierInfo(supplierInfo);

            //拼装结算单位
            record.assemblyUnitCodeInfo(unitCodeInfo);

            //处理地址信息
            for (OrderAddressVO address : resultOne.getData()) {
                address.getFile(prePath);
            }
        }

        map1.put("pageInfo",new CommonPageResult(page));
        return CommonResult.success(map1);
    }

}

