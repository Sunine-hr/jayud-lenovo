package com.jayud.mall.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageDraftResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CustomerMapper;
import com.jayud.mall.mapper.FabWarehouseMapper;
import com.jayud.mall.model.bo.OrderInfoCustomerForm;
import com.jayud.mall.model.bo.OrderInfoForm;
import com.jayud.mall.model.bo.OrderInfoNewForm;
import com.jayud.mall.model.bo.QueryOrderInfoForm;
import com.jayud.mall.model.po.OrderClearanceFile;
import com.jayud.mall.model.po.OrderCustomsFile;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IOrderInfoService;
import com.jayud.mall.service.IShipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderinfo")
@Api(tags = "C013-client-产品订单表接口")
@ApiSort(value = 13)
public class OrderInfoController {

    @Autowired
    IOrderInfoService orderInfoService;
    @Autowired
    BaseService baseService;
    @Autowired
    IShipmentService shipmentService;

    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    FabWarehouseMapper fabWarehouseMapper;

    //分页查询订单列表
    @ApiOperation(value = "web端分页查询订单列表,废弃")
    @PostMapping("/findWebOrderInfoByPage")
    @ApiOperationSupport(order = 1)
    @Deprecated
    public CommonResult<CommonPageResult<OrderInfoVO>> findWebOrderInfoByPage(@RequestBody QueryOrderInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        form.setCustomerId(customerUser.getId());//当前登录客户
        IPage<OrderInfoVO> pageList = orderInfoService.findWebOrderInfoByPage(form);
        CommonPageResult<OrderInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //分页查询订单列表
    @ApiOperation(value = "web端分页查询订单列表(统计草稿)")
    @PostMapping("/findWebOrderInfoByPageDraft")
    @ApiOperationSupport(order = 2)
    public CommonResult<CommonPageDraftResult<OrderInfoVO>> findWebOrderInfoByPageDraft(@RequestBody QueryOrderInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        form.setCustomerId(customerUser.getId());//当前登录客户
        IPage<OrderInfoVO> pageList = orderInfoService.findWebOrderInfoByPage(form);

        Map<String,Long> totalMap = orderInfoService.findOrderInfoDraftCount(form);
        CommonPageDraftResult<OrderInfoVO> draftResult = new CommonPageDraftResult(pageList, totalMap);
        return CommonResult.success(draftResult);
    }


    //订单下单-暂存订单
    @ApiOperation(value = "订单下单-暂存订单")
    @PostMapping("/temporaryStorageOrderInfo")
    @ApiOperationSupport(order = 3)
    public CommonResult<OrderInfoVO> temporaryStorageOrderInfo(@Valid @RequestBody OrderInfoForm form){
        //订单对应箱号信息:order_case
        List<OrderCaseVO> orderCaseVOList = form.getOrderCaseVOList();
        if(CollUtil.isEmpty(orderCaseVOList)){
            return CommonResult.error(-1, "订单箱号不能为空");
        }
        //订单对应商品：order_shop
        List<OrderShopVO> orderShopVOList = form.getOrderShopVOList();
        if(CollUtil.isEmpty(orderShopVOList)){
            return CommonResult.error(-1, "订单商品不能为空");
        }
        Integer isPick = form.getIsPick();//是否上门提货(0否 1是,order_pick) is_pick=1
        if(isPick == 1){
            //订单对应提货信息表：order_pick
            List<OrderPickVO> orderPickVOList = form.getOrderPickVOList();
            if(CollUtil.isEmpty(orderPickVOList)){
                return CommonResult.error(-1, "订单提货信息不能为空");
            }
            Integer totalCartonSum = 0;
            for (int i=0; i<orderPickVOList.size(); i++){
                OrderPickVO orderPickVO = orderPickVOList.get(i);
                Integer totalCarton = orderPickVO.getTotalCarton();
                if(ObjectUtil.isEmpty(totalCarton) || totalCarton <= 0){
                    return CommonResult.error(-1, "提货地址的箱数，不能为空或者小于等于0");
                }
                totalCartonSum += totalCarton;
            }
            int size = orderCaseVOList.size();
            if(!totalCartonSum.equals(size)){
                return CommonResult.error(-1, "提货箱数不等于订单箱数");
            }
        }
        return orderInfoService.temporaryStorageOrderInfo(form);
    }

    //订单下单-提交订单
    @ApiOperation(value = "订单下单-提交订单")
    @PostMapping("/submitOrderInfo")
    @ApiOperationSupport(order = 4)
    public CommonResult<OrderInfoVO> submitOrderInfo(@Valid @RequestBody OrderInfoForm form){
        //订单对应箱号信息:order_case
        List<OrderCaseVO> orderCaseVOList = form.getOrderCaseVOList();
        if(CollUtil.isEmpty(orderCaseVOList)){
            return CommonResult.error(-1, "订单箱号不能为空");
        }
        //订单对应商品：order_shop
        List<OrderShopVO> orderShopVOList = form.getOrderShopVOList();
        if(CollUtil.isEmpty(orderShopVOList)){
            return CommonResult.error(-1, "订单商品不能为空");
        }
        Integer isPick = form.getIsPick();//是否上门提货(0否 1是,order_pick) is_pick=1
        if(isPick == 1){
            //订单对应提货信息表：order_pick
            List<OrderPickVO> orderPickVOList = form.getOrderPickVOList();
            if(CollUtil.isEmpty(orderPickVOList)){
                return CommonResult.error(-1, "订单提货信息不能为空");
            }
            Integer totalCartonSum = 0;
            for (int i=0; i<orderPickVOList.size(); i++){
                OrderPickVO orderPickVO = orderPickVOList.get(i);
                Integer totalCarton = orderPickVO.getTotalCarton();
                if(ObjectUtil.isEmpty(totalCarton) || totalCarton <= 0){
                    return CommonResult.error(-1, "提货地址的箱数，不能为空或者小于等于0");
                }
                totalCartonSum += totalCarton;
            }
            int size = orderCaseVOList.size();
            if(!totalCartonSum.equals(size)){
                return CommonResult.error(-1, "提货箱数不等于订单箱数");
            }
        }
        return orderInfoService.submitOrderInfo(form);
    }

    //订单列表-草稿-取消
    @ApiOperation(value = "订单列表-草稿-取消")
    @PostMapping("/draftCancelOrderInfo")
    @ApiOperationSupport(order = 5)
    public CommonResult<OrderInfoVO> draftCancelOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.draftCancelOrderInfo(form);
    }

    //订单-草稿-提交-进入编辑订单详情(从草稿进去，可以编辑，暂存和提交)
    @ApiOperation(value = "订单-草稿-提交-进入编辑订单详情(从草稿进去，可以编辑，暂存和提交)")
    @PostMapping("/lookEditOrderInfo")
    @ApiOperationSupport(order = 6)
    public CommonResult<OrderInfoVO> lookEditOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.lookEditOrderInfo(form);
    }

    //订单列表-查看订单详情
    @ApiOperation(value = "订单列表-查看订单详情(查看、编辑)")
    @PostMapping("/lookOrderInfo")
    @ApiOperationSupport(order = 7)
    public CommonResult<OrderInfoVO> lookOrderInfo(@RequestBody OrderInfoForm form){
        return orderInfoService.lookOrderInfo(form);
    }

    //订单详情-计柜重信息-确认 TODO 待具体实现 预留
    @ApiOperation(value = "订单详情-计柜重信息-确认(待具体实现 预留)")
    @PostMapping("/affirmCounterWeightInfo")
    @ApiOperationSupport(order = 8)
    public CommonResult affirmCounterWeightInfo(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return CommonResult.success("订单详情-计柜重信息-确认 TODO 待具体实现 预留");
    }

    //订单详情-打印唛头（打印订单箱号）
    @ApiOperation(value = "订单详情-打印唛头（打印订单箱号）")
    @PostMapping("/printOrderMark")
    @ApiOperationSupport(order = 9)
    public CommonResult<List<String>> printOrderMark(@RequestBody OrderInfoForm form){
        Long orderId = form.getId();
        return orderInfoService.printOrderMark(orderId);
    }


    //订单详情-账单确认(rec应收账单) TODO 待开发，以及业务确认
    @ApiOperation(value = "订单详情-账单确认(rec应收账单) TODO 待开发，以及业务确认")
    @PostMapping("/billConfirmedRec")
    @ApiOperationSupport(order = 10)
    public CommonResult billConfirmedRec(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return CommonResult.success("订单详情-账单确认(rec应收账单) TODO 待开发，以及业务确认");
    }

    //订单详情-账单确认(pay应付账单) TODO 待开发，以及业务确认
    @ApiOperation(value = "订单详情-账单确认(rec应收账单) TODO 待开发，以及业务确认")
    @PostMapping("/billConfirmedPay")
    @ApiOperationSupport(order = 11)
    public CommonResult billConfirmedPay(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return CommonResult.success("订单详情-账单确认(rec应收账单) TODO 待开发，以及业务确认");
    }

    /**
     * 订单编辑-保存
     * 1.编辑保存-订单箱号
     * 2.编辑保存-订单商品
     * 3.编辑保存-订单文件（报关文件、清关文件）
     * @param form
     * @return
     */
    @ApiOperation(value = "订单编辑-保存(箱号、商品、文件)")
    @PostMapping("/editSaveOrderInfo")
    @ApiOperationSupport(order = 12)
    public CommonResult<OrderInfoVO> editSaveOrderInfo(@RequestBody OrderInfoForm form){
        //订单对应箱号信息:order_case
        List<OrderCaseVO> orderCaseVOList = form.getOrderCaseVOList();
        if(orderCaseVOList == null || orderCaseVOList.size() == 0){
            return CommonResult.error(-1, "订单箱号不能为空");
        }
        //订单对应商品：order_shop
        List<OrderShopVO> orderShopVOList = form.getOrderShopVOList();
        if(orderShopVOList == null || orderShopVOList.size() == 0){
            return CommonResult.error(-1, "订单商品不能为空");
        }
        return orderInfoService.editSaveOrderInfo(form);
    }

    //查询客户订单
    @ApiOperation(value = "查询客户订单list")
    @ApiOperationSupport(order = 13)
    @PostMapping("/findOrderInfoByCustomer")
    public CommonResult<List<OrderInfoVO>> findOrderInfoByCustomer(){
        CustomerUser customerUser = baseService.getCustomerUser();
        OrderInfoCustomerForm form = new OrderInfoCustomerForm();
        form.setCustomerId(customerUser.getId());
        List<OrderInfoVO> orderInfoVOS = orderInfoService.findOrderInfoByCustomer(form);
        return CommonResult.success(orderInfoVOS);
    }

    //查询获取-订单报关文件
    @ApiOperation(value = "查询获取-订单报关文件")
    @ApiOperationSupport(order = 14)
    @PostMapping("/getOrderCustomsFiles")
    public CommonResult<List<OrderCustomsFile>> getOrderCustomsFiles(@Valid @RequestBody OrderInfoForm form){
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);
        Integer offerInfoId = orderInfo.getOfferInfoId();//报价id，运价id
        List<OrderCustomsFile> orderCustomsFiles = orderInfoService.getOrderCustomsFiles(orderInfo, offerInfoId);
        return CommonResult.success(orderCustomsFiles);
    }

    //查询获取-订单清关文件
    @ApiOperation(value = "查询获取-订单清关文件")
    @ApiOperationSupport(order = 15)
    @PostMapping("/getOrderClearanceFiles")
    public CommonResult<List<OrderClearanceFile>> getOrderClearanceFiles(@Valid @RequestBody OrderInfoForm form){
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);
        Integer offerInfoId = orderInfo.getOfferInfoId();//报价id，运价id
        List<OrderClearanceFile> orderClearanceFiles = orderInfoService.getOrderClearanceFiles(orderInfo, offerInfoId);
        return CommonResult.success(orderClearanceFiles);
    }


    //订单报价-上传新智慧Excel
    @ApiOperation(value = "订单报价-上传新智慧Excel")
    @RequestMapping(value = "/importExcelByNewWisdom", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperationSupport(order = 16)
    public CommonResult<OrderInfoVO> importExcelByNewWisdom(
            //用@RequestHeader获取请求头
            @RequestHeader(value = "offerInfoId") Integer offerInfoId,
            @RequestParam("file") MultipartFile file){
        //用HttpServletRequest，获取请求头内容
        if (file.isEmpty()) {
            return CommonResult.error(-1, "文件为空！");
        }
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);//运单 10001923.xls

        CustomerUser customerUser = baseService.getCustomerUser();
        if(ObjectUtil.isEmpty(customerUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "客户失效，请重新登录");
        }
        String customerId = customerUser.getId().toString();

        //运单id
        String shipment_id = originalFilename.substring("运单 ".length(), originalFilename.length() - ".xls".length());
        //导入新智慧运单excel
        List<List<Object>> excelList = shipmentService.importExcelByNewWisdom(file);

        //运单
        Map shipment = new HashMap();
        shipment.put("shipment_id", shipment_id);
        //收货人
        Map to_address = new HashMap();
        //货箱编号 list parcels
        List<Map> parcels = new ArrayList<>();
        for (int i=0; i<excelList.size(); i++){
            //服务*   0
            if(i==0){
                List<Object> o = excelList.get(i);
                String service = String.valueOf(o.get(1));
                shipment.put("service", service);
            }
            //收件人姓名*    2
            if(i==2){
                List<Object> o = excelList.get(i);
                String name = String.valueOf(o.get(1));
                to_address.put("name", name);
            }
            //收件人公司 3
            if(i==3){
                List<Object> o = excelList.get(i);
                String company = String.valueOf(o.get(1));
                to_address.put("company", company);
            }
            //收件人地址一*   4
            if(i==4){
                List<Object> o = excelList.get(i);
                String address_1 = String.valueOf(o.get(1));
                to_address.put("address_1", address_1);
            }
            //收件人地址二    5
            if(i==5){
                List<Object> o = excelList.get(i);
                String address_2 = String.valueOf(o.get(1));
                to_address.put("address_2", address_2);
            }
            //收件人地址三    6
            if(i==6){
                List<Object> o = excelList.get(i);
                String address_3 = String.valueOf(o.get(1));
                to_address.put("address_3", address_3);
            }
            //收件人城市*    7
            if(i==7){
                List<Object> o = excelList.get(i);
                String city = String.valueOf(o.get(1));
                to_address.put("city", city);
            }
            //收件人省份/州   8
            if(i==8){
                List<Object> o = excelList.get(i);
                String state = String.valueOf(o.get(1));
                to_address.put("state", state);
            }
            //收件人邮编*    9
            if(i==9){
                List<Object> o = excelList.get(i);
                String postcode = String.valueOf(o.get(1));
                to_address.put("postcode", postcode);
            }
            //收件人国家代码(二字代码)*    10
            if(i==10){
                List<Object> o = excelList.get(i);
                String country = String.valueOf(o.get(1));
                to_address.put("country", country);
            }
            //收件人电话 11
            if(i==11){
                List<Object> o = excelList.get(i);
                String tel = String.valueOf(o.get(1));
                to_address.put("tel", tel);
            }
            //客户订单号 12
            if(i==12){
                List<Object> o = excelList.get(i);
                String client_reference = String.valueOf(o.get(1));
                shipment.put("client_reference", client_reference);
            }

            //货箱编号+产品
            Map parcel = new HashMap();
            if(i>21){
                List<Object> o = excelList.get(i);
                //货箱编号*-number
                String number = StrUtil.isEmpty(String.valueOf(o.get(0))) ? "" : String.valueOf(o.get(0));
                parcel.put("number", number);
                //客户货箱重量(KG)-client_weight
                String client_weight = StrUtil.isEmpty(String.valueOf(o.get(1))) ? "" : String.valueOf(o.get(1));
                parcel.put("client_weight", client_weight);
                //客户货箱长度(CM)-client_length
                String client_length = StrUtil.isEmpty(String.valueOf(o.get(2))) ? "" : String.valueOf(o.get(2));
                parcel.put("client_length", client_length);
                //客户货箱宽度(CM)-client_width
                String client_width = StrUtil.isEmpty(String.valueOf(o.get(3))) ? "" : String.valueOf(o.get(3));
                parcel.put("client_width", client_width);
                //客户货箱高度(CM)-client_height
                String client_height = StrUtil.isEmpty(String.valueOf(o.get(4))) ? "" : String.valueOf(o.get(4));
                parcel.put("client_height", client_height);
                //货箱重量(KG)-actual_weight
                String actual_weight = StrUtil.isEmpty(String.valueOf(o.get(5))) ? "" : String.valueOf(o.get(5));
                parcel.put("actual_weight", actual_weight);
                //货箱长度(CM)-chargeable_length
                String chargeable_length = StrUtil.isEmpty(String.valueOf(o.get(6))) ? "" : String.valueOf(o.get(6));
                parcel.put("chargeable_length", chargeable_length);
                //货箱宽度(CM)-chargeable_width
                String chargeable_width = StrUtil.isEmpty(String.valueOf(o.get(7))) ? "" : String.valueOf(o.get(7));
                parcel.put("chargeable_width", chargeable_width);
                //货箱高度(CM)-chargeable_height
                String chargeable_height = StrUtil.isEmpty(String.valueOf(o.get(8))) ? "" : String.valueOf(o.get(8));
                parcel.put("chargeable_height", chargeable_height);

                //declarations
                //产品SKU-sku
                String sku = StrUtil.isEmpty(String.valueOf(o.get(9))) ? "" : String.valueOf(o.get(9));
                parcel.put("sku", sku);
                //产品中文品名*-name_zh
                String name_zh = StrUtil.isEmpty(String.valueOf(o.get(10))) ? "" : String.valueOf(o.get(10));
                parcel.put("name_zh", name_zh);
                //产品英文品名*-name_en
                String name_en = StrUtil.isEmpty(String.valueOf(o.get(11))) ? "" : String.valueOf(o.get(11));
                parcel.put("name_en", name_en);
                //产品申报单价*-unit_value
                String unit_value = StrUtil.isEmpty(String.valueOf(o.get(12))) ? "" : String.valueOf(o.get(12));
                parcel.put("unit_value", unit_value);
                //产品申报数量*-qty
                String qty = StrUtil.isEmpty(String.valueOf(o.get(13))) ? "" : String.valueOf(o.get(13));
                parcel.put("qty", qty);
                //产品材质*-material
                String material = StrUtil.isEmpty(String.valueOf(o.get(14))) ? "" : String.valueOf(o.get(14));
                parcel.put("material", material);
                //产品海关编码-hscode
                String hscode = StrUtil.isEmpty(String.valueOf(o.get(15))) ? "" : String.valueOf(o.get(15));
                parcel.put("hscode", hscode);
                //产品用途-usage
                String usage = StrUtil.isEmpty(String.valueOf(o.get(16))) ? "" : String.valueOf(o.get(16));
                parcel.put("usage", usage);
                //产品品牌-brand
                String brand = StrUtil.isEmpty(String.valueOf(o.get(17))) ? "" : String.valueOf(o.get(17));
                parcel.put("brand", brand);
                //产品型号-size
                String size = StrUtil.isEmpty(String.valueOf(o.get(18))) ? "" : String.valueOf(o.get(18));
                parcel.put("size", size);
                //产品销售链接-sale_url
                String sale_url = StrUtil.isEmpty(String.valueOf(o.get(19))) ? "" : String.valueOf(o.get(19));
                parcel.put("sale_url", sale_url);
                //产品销售价格-sale_price
                String sale_price = StrUtil.isEmpty(String.valueOf(o.get(20))) ? "" : String.valueOf(o.get(20));
                parcel.put("sale_price", sale_price);
                //产品图片链接-photos
                String photos = StrUtil.isEmpty(String.valueOf(o.get(21))) ? "" : String.valueOf(o.get(21));
                parcel.put("photos", photos);
                //产品重量(kg)-weight
                String weight = StrUtil.isEmpty(String.valueOf(o.get(22))) ? "" : String.valueOf(o.get(22));
                parcel.put("weight", weight);
                //产品ASIN-asin
                String asin = StrUtil.isEmpty(String.valueOf(o.get(23))) ? "" : String.valueOf(o.get(23));
                parcel.put("asin", asin);
                //产品FNSKU-fnsku
                String fnsku = StrUtil.isEmpty(String.valueOf(o.get(24))) ? "" : String.valueOf(o.get(24));
                parcel.put("fnsku", fnsku);
                //承运商 无字段
                //跟踪号 无字段

                parcels.add(parcel);
            }
        }

        //根据 货箱编号*-number 分组，再次组装数据
        Map<String, List<Map>> stringListMap = groupListByNumber(parcels);


        parcels = new ArrayList<>();
        for (Map.Entry<String, List<Map>> entry : stringListMap.entrySet()) {
            //String number = entry.getKey();//货箱编号
            List<Map> parcelsList = entry.getValue();//货箱编号 list
            Map parcelObj = parcelsList.get(0);

            Map parcel = new HashMap();
            String number = MapUtil.getStr(parcelObj, "number");//货箱编号*-number
            parcel.put("number", number);
            String client_weight = MapUtil.getStr(parcelObj, "client_weight");//客户货箱重量(KG)-client_weight
            parcel.put("client_weight", client_weight);
            String client_length = MapUtil.getStr(parcelObj, "client_length");//客户货箱长度(CM)-client_length
            parcel.put("client_length", client_length);
            String client_width = MapUtil.getStr(parcelObj, "client_width");//客户货箱宽度(CM)-client_width
            parcel.put("client_width", client_width);
            String client_height = MapUtil.getStr(parcelObj, "client_height");//客户货箱高度(CM)-client_height
            parcel.put("client_height", client_height);
            String actual_weight = MapUtil.getStr(parcelObj, "actual_weight");//货箱重量(KG)-actual_weight
            parcel.put("actual_weight", actual_weight);
            String chargeable_length = MapUtil.getStr(parcelObj, "chargeable_length");//货箱长度(CM)-chargeable_length
            parcel.put("chargeable_length", chargeable_length);
            String chargeable_width = MapUtil.getStr(parcelObj, "chargeable_width");//货箱宽度(CM)-chargeable_width
            parcel.put("chargeable_width", chargeable_width);
            String chargeable_height = MapUtil.getStr(parcelObj, "chargeable_height");//货箱高度(CM)-chargeable_height
            parcel.put("chargeable_height", chargeable_height);
            //产品SKU list    declarations
            List<Map> declarations = new ArrayList<>();
            for (int i=0; i<parcelsList.size(); i++){
                Map parcelMap = parcelsList.get(i);

                Map declaration = new HashMap();
                String sku = MapUtil.getStr(parcelMap, "sku");//产品SKU-sku
                declaration.put("sku", sku);
                String name_zh = MapUtil.getStr(parcelMap, "name_zh");//产品中文品名*-name_zh
                declaration.put("name_zh", name_zh);
                String name_en = MapUtil.getStr(parcelMap, "name_en");//产品英文品名*-name_en
                declaration.put("name_en", name_en);
                String unit_value = MapUtil.getStr(parcelMap, "unit_value");//产品申报单价*-unit_value
                declaration.put("unit_value", unit_value);
                String qty = MapUtil.getStr(parcelMap, "qty");//产品申报数量*-qty
                declaration.put("qty", qty);
                String material = MapUtil.getStr(parcelMap, "material");//产品材质*-material
                declaration.put("material", material);
                String hscode = MapUtil.getStr(parcelMap, "hscode");//产品海关编码-hscode
                declaration.put("hscode", hscode);
                String usage = MapUtil.getStr(parcelMap, "usage");//产品用途-usage
                declaration.put("usage", usage);
                String brand = MapUtil.getStr(parcelMap, "brand");//产品品牌-brand
                declaration.put("brand", brand);
                String size = MapUtil.getStr(parcelMap, "size");//产品型号-size
                declaration.put("size", size);
                String sale_url = MapUtil.getStr(parcelMap, "sale_url");//产品销售链接-sale_url
                declaration.put("sale_url", sale_url);
                String sale_price = MapUtil.getStr(parcelMap, "sale_price");//产品销售价格-sale_price
                declaration.put("sale_price", sale_price);
                String photos = MapUtil.getStr(parcelMap, "photos");//产品图片链接-photos
                declaration.put("photos", photos);
                String weight = MapUtil.getStr(parcelMap, "weight");//产品重量(kg)-weight
                declaration.put("weight", weight);
                String asin = MapUtil.getStr(parcelMap, "asin");//产品ASIN-asin
                declaration.put("asin", asin);
                String fnsku = MapUtil.getStr(parcelMap, "fnsku");//产品FNSKU-fnsku
                declaration.put("fnsku", fnsku);

                declarations.add(declaration);
            }
            parcel.put("declarations",declarations);
            parcels.add(parcel);
        }


        shipment.put("to_address", to_address);//运单收货地址
        shipment.put("parcels", parcels);//运单箱号，箱号对应的商品明细


        Map data = MapUtil.newHashMap();
        data.put("shipment", shipment);
        ShipmentVO shipmentVO = MapUtil.get(data, "shipment", ShipmentVO.class);
        String shipmentJson = JSONUtil.toJsonStr(shipmentVO);

        CustomerVO customerVO = customerMapper.findCustomerById(Integer.valueOf(customerId));
        String newWisdomToken = customerVO.getNewWisdomToken();//新智慧token不能为空,每个客户的token不同，通过tonken区分客户
        shipmentVO.setNew_wisdom_token(newWisdomToken);//没有确认运单归属的客户token，暂时为空
        ShipmentVO saveShipment = shipmentService.saveShipment(shipmentVO);

        ShipmentIdVO shipmentIdVO = new ShipmentIdVO();
        //订单号(新智慧运单号)
        shipmentIdVO.setShipment_id(shipment_id);

        //目的仓库代码
        String destinationWarehouseCode = MapUtil.getStr(to_address, "name");//目的仓库代码,例如：ONT8
        FabWarehouseVO fabWarehouseVO = fabWarehouseMapper.findFabWarehouseByWarehouseCode(destinationWarehouseCode);
        if(ObjectUtil.isEmpty(fabWarehouseVO)){
            //fabWarehouseVO 不存在 直接设置name
            shipmentIdVO.setWarehouseCode(destinationWarehouseCode);
        }else{
            //fabWarehouseVO 存在 直接设置id
            shipmentIdVO.setWarehouseId(fabWarehouseVO.getId());
        }
        //运单服务
        String service = MapUtil.getStr(shipment, "service");
        shipmentIdVO.setService(service);

        Map to_address_map = MapUtil.get(shipment, "to_address", Map.class);
        String postcode = MapUtil.getStr(to_address_map, "postcode");
        shipmentIdVO.setPostcode(postcode);

        //上传excel，组装数据
        OrderInfoNewForm form = new OrderInfoNewForm();
        form.setOrderNo(shipmentIdVO.getShipment_id());//订单号
        form.setOfferInfoId(offerInfoId);//报价id不能为空

        return orderInfoService.newEditOrderInfo(form);
    }

    /**
     * 根据 货箱编号*-number 分组，再次组装数据
     * @param parcels 货箱编号
     * @return
     */
    private Map<String, List<Map>> groupListByNumber(List<Map> parcels) {
        Map<String, List<Map>> map = new HashMap<>();
        for (Map parcel : parcels) {
            String key = MapUtil.getStr(parcel, "number");//货箱编号
            List<Map> tmpList = map.get(key);
            if (CollUtil.isEmpty(tmpList)) {
                tmpList = new ArrayList<>();
                tmpList.add(parcel);
                map.put(key, tmpList);
            } else {
                tmpList.add(parcel);
            }
        }
        return map;
    }


    @ApiOperation(value = "订单-计算订单费用")
    @ApiOperationSupport(order = 17)
    @PostMapping("/calcOrderCopeReceivable")
    public CommonResult<OrderInfoVO> calcOrderCopeReceivable(@Valid @RequestBody OrderInfoForm form){
        //1.先暂存
        CommonResult<OrderInfoVO> orderInfoVOCommonResult = orderInfoService.temporaryStorageOrderInfo(form);
        OrderInfoVO orderInfoVO = orderInfoVOCommonResult.getData();
        Long orderId = orderInfoVO.getId();
        //2.在查询费用
        return orderInfoService.lookOrderInfoCost(orderId);
    }


}
