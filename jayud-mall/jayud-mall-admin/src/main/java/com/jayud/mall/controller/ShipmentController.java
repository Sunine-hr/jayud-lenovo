package com.jayud.mall.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.mapper.CustomerMapper;
import com.jayud.mall.mapper.FabWarehouseMapper;
import com.jayud.mall.model.bo.QueryShipmentForm;
import com.jayud.mall.model.bo.QueryShipmentParaForm;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.model.vo.FabWarehouseVO;
import com.jayud.mall.model.vo.ShipmentIdVO;
import com.jayud.mall.model.vo.ShipmentVO;
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
@RequestMapping("/shipment")
@Api(tags = "A053-admin-南京新智慧订单装货接口")
@ApiSort(value = 53)
public class ShipmentController {

    @Autowired
    IShipmentService shipmentService;
    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    FabWarehouseMapper fabWarehouseMapper;

    @ApiOperation(value = "分页查询订单装货信息")
    @PostMapping("/findShipmentByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<ShipmentVO>> findShipmentByPage(@RequestBody QueryShipmentForm form) {
        IPage<ShipmentVO> pageList = shipmentService.findShipmentByPage(form);
        CommonPageResult<ShipmentVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查询订单装货信息")
    @PostMapping("/findShipmentById")
    @ApiOperationSupport(order = 2)
    public CommonResult<ShipmentVO> findShipmentById(@Valid @RequestBody QueryShipmentParaForm form){
        String shipment_id = form.getShipment_id();
        return shipmentService.findShipmentById(shipment_id);
    }

    //根据新智慧运单生成南京订单(订单、订单商品、订单箱号)
    @ApiOperation(value = "根据新智慧运单生成南京订单(订单、订单商品、订单箱号)")
    @PostMapping("/createOrderByShipment")
    @ApiOperationSupport(order = 3)
    public CommonResult<ShipmentVO> createOrderByShipment(@Valid @RequestBody QueryShipmentParaForm form){
        String shipment_id = form.getShipment_id();
        return shipmentService.createOrderByShipment(shipment_id);
    }

    @ApiOperation(value = "上传文件-导入新智慧Excel")
    @RequestMapping(value = "/importExcelByNewWisdom", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperationSupport(order = 4)
    public CommonResult<ShipmentIdVO> importExcelByNewWisdom(
            //用@RequestHeader获取请求头
            @RequestHeader(value = "customerId") String customerId,
            @RequestParam("file") MultipartFile file){
        //用HttpServletRequest，获取请求头内容
        if (file.isEmpty()) {
            return CommonResult.error(-1, "文件为空！");
        }
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);//运单 10001923.xls

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
        String newWisdomToken = customerVO.getNewWisdomToken();//新智慧token不能为空,每个客户的token不用，通过tonken区分客户
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
        return CommonResult.success(shipmentIdVO);
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



}
