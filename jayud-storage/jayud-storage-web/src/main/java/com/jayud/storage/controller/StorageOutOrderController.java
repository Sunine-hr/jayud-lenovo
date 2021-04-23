package com.jayud.storage.controller;


import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.FileClient;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.QueryStorageOrderForm;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.service.IStorageOutOrderService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 出库订单表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@RestController
@RequestMapping("/storageOutOrder")
public class StorageOutOrderController {

    @Autowired
    private IStorageOutOrderService storageOutOrderService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private OauthClient oauthClient;

    @ApiOperation("分页查询入库订单列表")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QueryStorageOrderForm form) {

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
//        Field[] declaredFields = new Field[100];

        Class<StorageInputOrderFormVO> storageInputOrderVOClass = StorageInputOrderFormVO.class;
        Field[] declaredFields = storageInputOrderVOClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                Map map = new HashMap<>();
                map.put("key", declaredField.getName());
                map.put("name", annotation.value());
                list.add(map);
            }
        }


        Map map1 = new HashMap();
        map1.put("header", list);
        IPage<StorageInputOrderFormVO> page = this.storageOutOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }
        //获取附件地址
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        List<StorageInputOrderFormVO> records = page.getRecords();
        List<Long> trailerOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();

        List<String> unitCodes = new ArrayList<>();
        for (StorageInputOrderFormVO record : records) {
            trailerOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            unitCodes.add(record.getUnitCode());
        }

        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }

        //获取结算单位信息
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
            unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (StorageInputOrderFormVO record : records) {
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);

            //拼装结算单位
            record.assemblyUnitCodeInfo(unitCodeInfo);

        }

        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }

}

