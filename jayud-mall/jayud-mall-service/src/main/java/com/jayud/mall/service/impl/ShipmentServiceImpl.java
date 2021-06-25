package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.sax.Excel03SaxReader;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.QueryShipmentForm;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * <p>
 * 南京新智慧-运单装货信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-09
 */
@Slf4j
@Service
public class ShipmentServiceImpl extends ServiceImpl<ShipmentMapper, Shipment> implements IShipmentService {

    @Autowired
    ShipmentMapper shipmentMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    CustomerGoodsMapper customerGoodsMapper;
    @Autowired
    OrderCaseMapper orderCaseMapper;
    @Autowired
    FabWarehouseMapper fabWarehouseMapper;
    @Autowired
    NumberGeneratedMapper numberGeneratedMapper;

    @Autowired
    IOrderInfoService orderInfoService;
    @Autowired
    IOrderCaseService orderCaseService;
    @Autowired
    IOrderShopService orderShopService;
    @Autowired
    ICustomerGoodsService customerGoodsService;
    @Autowired
    IFabWarehouseService fabWarehouseService;
    @Autowired
    IShipmentService shipmentService;

    /**
     * excelList
     */
    private static List<List<Object>> excelList = Collections.synchronizedList(new ArrayList<List<Object>>());

    @Override
    public ShipmentVO saveShipment(ShipmentVO shipmentVO) {
        String shipment_id = shipmentVO.getShipment_id();
        ShipmentVO shipment = shipmentMapper.findShipmentById(shipment_id);
        if(ObjectUtil.isEmpty(shipment)){
            //新增插入
            log.info("新增插入:{}", shipmentVO);
        }else{
            //更新修改
            log.info("更新修改:{}", shipmentVO);
        }
        Shipment s = ConvertUtil.convert(shipmentVO, Shipment.class);
        LocalDateTime now = LocalDateTime.now();
        if(ObjectUtil.isNotEmpty(shipmentVO.getPicking_time())){
            long l = Long.valueOf(shipmentVO.getPicking_time())*1000L;//秒转为毫秒
            LocalDateTime ldt = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
            s.setPicking_time(ldt);
        }else{
            s.setPicking_time(now);
        }
        if(ObjectUtil.isNotEmpty(shipmentVO.getRates_time())){
            long l = Long.valueOf(shipmentVO.getRates_time())*1000L;//秒转为毫秒
            LocalDateTime ldt = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
            s.setRates_time(ldt);
        }else{
            s.setRates_time(now);
        }
        if(ObjectUtil.isNotEmpty(shipmentVO.getCreat_time())){
            long l = Long.valueOf(shipmentVO.getCreat_time())*1000L;//秒转为毫秒
            LocalDateTime ldt = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
            s.setCreat_time(ldt);
        }else{
            s.setRates_time(now);
        }
        s.setShipmentJson(JSONUtil.toJsonStr(shipmentVO));
        this.saveOrUpdate(s);

        String shipment_id1 = s.getShipment_id();
//        OrderInfoVO orderInfoByOrderNo = orderInfoMapper.findOrderInfoByOrderNo(shipment_id1);
//        if(ObjectUtil.isEmpty(orderInfoByOrderNo)){
//            shipmentService.createOrderByShipment(shipment_id1);
//        }
        //根据新智慧运单shipment_id，创建订单
        shipmentService.createOrderByShipment(shipment_id1);
        return shipmentVO;
    }

    @Override
    public IPage<ShipmentVO> findShipmentByPage(QueryShipmentForm form) {
        //定义分页参数
        Page<ShipmentVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.shipment_id"));
        IPage<ShipmentVO> pageInfo = shipmentMapper.findShipmentByPage(page, form);
        List<ShipmentVO> records = pageInfo.getRecords();
        if(CollUtil.isNotEmpty(records)){
            records.forEach(shipmentVO -> {
                fit(shipmentVO);
            });
        }
        return pageInfo;
    }

    @Override
    public CommonResult<ShipmentVO> findShipmentById(String shipment_id) {
        ShipmentVO shipmentVO = shipmentMapper.findShipmentById(shipment_id);
        if(ObjectUtil.isEmpty(shipmentVO)){
            return CommonResult.error(-1, "没有做找到新智慧对应的运单信息");
        }
        fit(shipmentVO);
        return CommonResult.success(shipmentVO);
    }

    @Override
    public CommonResult<ShipmentVO> createOrderByShipment(String shipment_id) {
        ShipmentVO shipmentVO = shipmentMapper.findShipmentById(shipment_id);
        if(ObjectUtil.isEmpty(shipmentVO)){
            return CommonResult.error(-1, "没有做找到新智慧对应的运单信息");
        }
        fit(shipmentVO);

        //订单商品
        List<OrderShop> orderShopList = new ArrayList<>();
        //订单箱号
        List<OrderCase> orderCaseList = new ArrayList<>();

        //组装数据-构造订单箱号，构造订单商品
        extracted(shipmentVO, orderShopList, orderCaseList);

        //目的仓库代码
        JSONObject shipmentJSONObject = JSONUtil.parseObj(shipmentVO);
        Object to_address = shipmentJSONObject.get("to_address");
        JSONObject to_addressJsonObject = JSONUtil.parseObj(to_address);
        String destinationWarehouseCode = to_addressJsonObject.get("name", String.class);//目的仓库代码,例如：ONT8
        FabWarehouseVO fabWarehouseVO = fabWarehouseMapper.findFabWarehouseByWarehouseCode(destinationWarehouseCode);

        //1.保存订单
        String orderNo = shipmentVO.getShipment_id();
        OrderInfoVO orderInfoVO = orderInfoMapper.findOrderInfoByOrderNo(orderNo);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            //为空，新增订单
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderNo(shipmentVO.getShipment_id());//订单号
            orderInfo.setCustomerId(shipmentVO.getCustomerId());//客户ID(customer id)

            orderInfo.setOfferInfoId(null);//报价id(offer_info id),没有关联的报价
            orderInfo.setReserveSize(null);//订柜尺寸,根据报价选择
            orderInfo.setStoreGoodsWarehouseCode(null);//集货仓库代码,根据报价选择
            orderInfo.setStoreGoodsWarehouseName(null);//集货仓库名称,根据报价选择
            if(ObjectUtil.isNotEmpty(fabWarehouseVO)){
                orderInfo.setDestinationWarehouseCode(fabWarehouseVO.getWarehouseCode());//目的仓库代码,根据报价选择
                orderInfo.setDestinationWarehouseName(fabWarehouseVO.getWarehouseName());//目的仓库名称,根据报价选择
            }
            orderInfo.setIsPick(0);//是否上门提货(0否 1是),默认为否

            //状态码,默认为 旧系统导入 状态
            orderInfo.setFrontStatusCode(OrderEnum.FRONT_IMPORT.getCode());
            orderInfo.setFrontStatusName(OrderEnum.FRONT_IMPORT.getName());
            orderInfo.setAfterStatusCode(OrderEnum.AFTER_IMPORT.getCode());
            orderInfo.setAfterStatusName(OrderEnum.AFTER_IMPORT.getName());

            orderInfo.setNeedDeclare(0);//是否需要报关0-否，1-是 (订单对应报关文件:order_customs_file)
            orderInfo.setNeedClearance(0);//是否需要清关0-否，1-是 (订单对应清关文件:order_clearance_file)

            orderInfo.setCreateTime(shipmentVO.getCreatTime());//创建日期,新智慧的下单日期
            orderInfo.setCreateUserId(shipmentVO.getCustomerId());//创建人ID(customer id)
            orderInfo.setCreateUserName(shipmentVO.getCustomerUserName());//创建人名称(customer user_name)
            orderInfo.setOrderOrigin("2");//订单来源(1web端 2新智慧同步)

            orderInfo.setRemark("旧系统同步订单");
            if(ObjectUtil.isNotEmpty(shipmentVO.getChargeable_weight())){
                orderInfo.setChargeWeight(new BigDecimal(shipmentVO.getChargeable_weight()));//收费重(KG)
                orderInfo.setVolumeWeight(new BigDecimal(shipmentVO.getChargeable_weight()));//材积重(KG),默认等于 收费重(KG)
            }
            orderInfo.setActualVolume(null);//实际体积(m3),默认为空
            orderInfo.setTotalCartons(null);//总箱数,默认为空

            //订单
            orderInfoService.saveOrUpdate(orderInfo);
            Long orderId = orderInfo.getId();

            //订单箱号
            orderCaseList.forEach(orderCase -> {
                orderCase.setOrderId(orderId.intValue());
            });
            QueryWrapper<OrderCase> orderCaseQueryWrapper = new QueryWrapper<>();
            orderCaseQueryWrapper.eq("order_id", orderInfo.getId());
            orderCaseService.remove(orderCaseQueryWrapper);
            orderCaseService.saveOrUpdateBatch(orderCaseList);
            //订单对应商品
            orderShopList.forEach(orderShop -> {
                orderShop.setOrderId(orderId.intValue());
            });
            QueryWrapper<OrderShop> orderShopQueryWrapper = new QueryWrapper<>();
            orderShopQueryWrapper.eq("order_id", orderInfo.getId());
            orderShopService.remove(orderShopQueryWrapper);
            orderShopService.saveOrUpdateBatch(orderShopList);
        }else{
            OrderInfo orderInfo = ConvertUtil.convert(orderInfoVO, OrderInfo.class);
            orderInfo.setCustomerId(shipmentVO.getCustomerId());//客户ID(customer id)

            orderInfo.setOfferInfoId(null);//报价id(offer_info id),没有关联的报价
            orderInfo.setReserveSize(null);//订柜尺寸,根据报价选择
            orderInfo.setStoreGoodsWarehouseCode(null);//集货仓库代码,根据报价选择
            orderInfo.setStoreGoodsWarehouseName(null);//集货仓库名称,根据报价选择

            if(ObjectUtil.isNotEmpty(fabWarehouseVO)){
                orderInfo.setDestinationWarehouseCode(fabWarehouseVO.getWarehouseCode());//目的仓库代码,根据报价选择
                orderInfo.setDestinationWarehouseName(fabWarehouseVO.getWarehouseName());//目的仓库名称,根据报价选择
            }

            orderInfo.setIsPick(0);//是否上门提货(0否 1是),默认为否

//            //状态码,默认为草稿状态  不改状态
//            orderInfo.setFrontStatusCode(OrderEnum.FRONT_DRAFT.getCode());
//            orderInfo.setFrontStatusName(OrderEnum.FRONT_DRAFT.getName());
//            orderInfo.setAfterStatusCode(OrderEnum.AFTER_DRAFT.getCode());
//            orderInfo.setAfterStatusName(OrderEnum.AFTER_DRAFT.getName());

            orderInfo.setCreateTime(shipmentVO.getCreatTime());//创建日期,新智慧的下单日期
            orderInfo.setCreateUserId(shipmentVO.getCustomerId());//创建人ID(customer id)
            orderInfo.setCreateUserName(shipmentVO.getCustomerUserName());//创建人名称(customer user_name)
            orderInfo.setOrderOrigin("2");//订单来源(1web端 2新智慧同步)

            orderInfo.setRemark("旧系统同步订单");
            if(ObjectUtil.isNotEmpty(shipmentVO.getChargeable_weight())){
                orderInfo.setChargeWeight(new BigDecimal(shipmentVO.getChargeable_weight()));//收费重(KG)
                orderInfo.setVolumeWeight(new BigDecimal(shipmentVO.getChargeable_weight()));//材积重(KG),默认等于 收费重(KG)
            }

            orderInfo.setActualVolume(null);//实际体积(m3),默认为空
            orderInfo.setTotalCartons(null);//总箱数,默认为空

            //订单
            orderInfoService.saveOrUpdate(orderInfo);
            Long orderId = orderInfo.getId();

            //2.保存订单箱号
            orderCaseList.forEach(orderCase -> {
                orderCase.setOrderId(orderId.intValue());
            });
            QueryWrapper<OrderCase> orderCaseQueryWrapper = new QueryWrapper<>();
            orderCaseQueryWrapper.eq("order_id", orderInfo.getId());
            orderCaseService.remove(orderCaseQueryWrapper);
            orderCaseService.saveOrUpdateBatch(orderCaseList);
            //3.保存订单对应商品
            orderShopList.forEach(orderShop -> {
                orderShop.setOrderId(orderId.intValue());
            });
            QueryWrapper<OrderShop> orderShopQueryWrapper = new QueryWrapper<>();
            orderShopQueryWrapper.eq("order_id", orderInfo.getId());
            orderShopService.remove(orderShopQueryWrapper);
            orderShopService.saveOrUpdateBatch(orderShopList);
        }
        return CommonResult.success(shipmentVO);
    }

    @Override
    public List<List<Object>> importExcelByNewWisdom(MultipartFile file) {
        //每次进入createRowHandler之前先清空excelList
        excelList.clear();
        // 获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStream is = FileMagic.prepareToCheckMagic(inputStream);
        try {
            FileMagic fm = FileMagic.valueOf(is);
            switch(fm) {
                case OLE2:
                    //excel是03版本
                    Excel03SaxReader reader03 = new Excel03SaxReader(createRowHandler());
                    reader03.read(is, 0);
                    break;
                case OOXML:
                    //excel是07版本
                    Excel07SaxReader reader07 = new Excel07SaxReader(createRowHandler());
                    reader07.read(is, 0);
                    break;
                default:
                    throw new IOException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return excelList;
    }

    /**
     * 读取大数据Excel
     * @return
     */
    private RowHandler createRowHandler() {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, long rowIndex, List<Object> rowlist) {
                List<Object> row = new ArrayList<>(26);
                row.add(0, rowlist.get(0));
                try {
                    row.add(1, rowlist.get(1));
                } catch (Exception e) {
                    row.add(1, "");
                }
                try {
                    row.add(2, rowlist.get(2));
                } catch (Exception e) {
                    row.add(2, "");
                }
                try {
                    row.add(3, rowlist.get(3));
                } catch (Exception e) {
                    row.add(3, "");
                }
                try {
                    row.add(4, rowlist.get(4));
                } catch (Exception e) {
                    row.add(4, "");
                }
                try {
                    row.add(5, rowlist.get(5));
                } catch (Exception e) {
                    row.add(5, "");
                }
                try {
                    row.add(6, rowlist.get(6));
                } catch (Exception e) {
                    row.add(6, "");
                }
                try {
                    row.add(7, rowlist.get(7));
                } catch (Exception e) {
                    row.add(7, "");
                }
                try {
                    row.add(8, rowlist.get(8));
                } catch (Exception e) {
                    row.add(8, "");
                }
                try {
                    row.add(9, rowlist.get(9));
                } catch (Exception e) {
                    row.add(9, "");
                }
                try {
                    row.add(10, rowlist.get(10));
                } catch (Exception e) {
                    row.add(10, "");
                }
                try {
                    row.add(11, rowlist.get(11));
                } catch (Exception e) {
                    row.add(11, "");
                }
                try {
                    row.add(12, rowlist.get(12));
                } catch (Exception e) {
                    row.add(12, "");
                }
                try {
                    row.add(13, rowlist.get(13));
                } catch (Exception e) {
                    row.add(13, "");
                }
                try {
                    row.add(14, rowlist.get(14));
                } catch (Exception e) {
                    row.add(14, "");
                }
                try {
                    row.add(15, rowlist.get(15));
                } catch (Exception e) {
                    row.add(15, "");
                }
                try {
                    row.add(16, rowlist.get(16));
                } catch (Exception e) {
                    row.add(16, "");
                }
                try {
                    row.add(17, rowlist.get(17));
                } catch (Exception e) {
                    row.add(17, "");
                }
                try {
                    row.add(18, rowlist.get(18));
                } catch (Exception e) {
                    row.add(18, "");
                }
                try {
                    row.add(19, rowlist.get(19));
                } catch (Exception e) {
                    row.add(19, "");
                }
                try {
                    row.add(20, rowlist.get(20));
                } catch (Exception e) {
                    row.add(20, "");
                }
                try {
                    row.add(21, rowlist.get(21));
                } catch (Exception e) {
                    row.add(21, "");
                }
                try {
                    row.add(22, rowlist.get(22));
                } catch (Exception e) {
                    row.add(22, "");
                }
                try {
                    row.add(23, rowlist.get(23));
                } catch (Exception e) {
                    row.add(23, "");
                }
                try {
                    row.add(24, rowlist.get(24));
                } catch (Exception e) {
                    row.add(24, "");
                }
                try {
                    row.add(25, rowlist.get(25));
                } catch (Exception e) {
                    row.add(25, "");
                }
                try {
                    row.add(26, rowlist.get(26));
                } catch (Exception e) {
                    row.add(26, "");
                }
                excelList.add(row);
            }
        };
    }

    /**
     * 组装数据-构造订单箱号，构造订单商品
     * @param shipmentVO 新智慧运单(订单)
     * @param orderShopList 订单商品
     * @param orderCaseList 订单箱号
     */
    private void extracted(ShipmentVO shipmentVO, List<OrderShop> orderShopList, List<OrderCase> orderCaseList) {
        JSONObject shipmentJSONObject = JSONUtil.parseObj(shipmentVO);
        Object parcels = shipmentJSONObject.get("parcels");
        JSONArray objects = JSONUtil.parseArray(parcels);
        for (int i = 0; i < objects.size(); i++) {
            //订单箱号
            JSONObject parcelsJsonObject = objects.getJSONObject(i);
            //String cartonNo = parcelsJsonObject.get("number",String.class);
            //OrderCaseVO orderCaseVO = orderCaseMapper.findOrderCaseByCartonNo(cartonNo);
            String fabNo = parcelsJsonObject.get("number",String.class);
            OrderCaseVO orderCaseVO = orderCaseMapper.findOrderCaseByfabNo(fabNo);
            if(ObjectUtil.isEmpty(orderCaseVO)){
                //orderCaseVO 为null
                OrderCase orderCase = new OrderCase();
                //String cartonNOa = NumberGeneratedUtils.getOrderNoByCode2("case_number");
                String cartonNO = numberGeneratedMapper.getOrderNoByCode("case_number");
                orderCase.setCartonNo(cartonNO);//箱号
                orderCase.setFabNo(fabNo);//FBA
                orderCase.setAsnLength(parcelsJsonObject.get("client_length", BigDecimal.class));//客户测量的长度，单位cm
                orderCase.setAsnWidth(parcelsJsonObject.get("client_width", BigDecimal.class));//客户测量的宽度，单位cm
                orderCase.setAsnHeight(parcelsJsonObject.get("client_height", BigDecimal.class));//客户测量的高度，单位cm
                orderCase.setAsnWeight(parcelsJsonObject.get("client_weight", BigDecimal.class));//客户测量的重量，单位kg
                //计算体积
                //体积(m3) = (长cm * 宽cm * 高cm) / 1000000
                BigDecimal asnVolume = orderCase.getAsnLength().multiply(orderCase.getAsnWidth()).multiply(orderCase.getAsnHeight()).divide(new BigDecimal("1000000"),3, BigDecimal.ROUND_HALF_UP);
                orderCase.setAsnVolume(asnVolume);

                orderCase.setConfirmLength(parcelsJsonObject.get("chargeable_length", BigDecimal.class));//最终确认长度，单位cm
                orderCase.setConfirmWidth(parcelsJsonObject.get("chargeable_width", BigDecimal.class));//最终确认宽度，单位cm
                orderCase.setConfirmHeight(parcelsJsonObject.get("chargeable_height", BigDecimal.class));//最终确认高度，单位cm
                orderCase.setConfirmWeight(parcelsJsonObject.get("chargeable_weight", BigDecimal.class));//最终确认重量，单位kg
                //计算体积
                //体积(m3) = (长cm * 宽cm * 高cm) / 1000000
                BigDecimal confirmVolume = orderCase.getConfirmLength().multiply(orderCase.getConfirmWidth()).multiply(orderCase.getConfirmHeight()).divide(new BigDecimal("1000000"),3, BigDecimal.ROUND_HALF_UP);
                orderCase.setConfirmVolume(confirmVolume);

                Long picking_time = parcelsJsonObject.get("picking_time", Long.class);
                if(ObjectUtil.isNotEmpty(picking_time)){
                    long l = picking_time*1000L;//秒转为毫秒
                    LocalDateTime confirmWeighDate = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    log.info("confirmWeighDate:{}", confirmWeighDate);
                    orderCase.setConfirmWeighDate(confirmWeighDate);
                }
                orderCase.setStatus(0);//是否已确认（0-未确认,1-已确认）
                orderCase.setRemark("新智慧同步箱号");//备注
                orderCaseList.add(orderCase);
            }else{
                OrderCase orderCase = ConvertUtil.convert(orderCaseVO, OrderCase.class);

                // 客户预报 长、宽、高、重、体积
                orderCase.setAsnLength(parcelsJsonObject.get("client_length", BigDecimal.class));//客户测量的长度，单位cm
                orderCase.setAsnWidth(parcelsJsonObject.get("client_width", BigDecimal.class));//客户测量的宽度，单位cm
                orderCase.setAsnHeight(parcelsJsonObject.get("client_height", BigDecimal.class));//客户测量的高度，单位cm
                orderCase.setAsnWeight(parcelsJsonObject.get("client_weight", BigDecimal.class));//客户测量的重量，单位kg
                //计算体积
                //体积(m3) = (长cm * 宽cm * 高cm) / 1000000
                BigDecimal asnVolume = orderCase.getAsnLength().multiply(orderCase.getAsnWidth()).multiply(orderCase.getAsnHeight()).divide(new BigDecimal("1000000"),3, BigDecimal.ROUND_HALF_UP);
                orderCase.setAsnVolume(asnVolume);

                // 仓库测量 长、宽、高、重、体积
                orderCase.setWmsLength(parcelsJsonObject.get("chargeable_length", BigDecimal.class));//仓库测量的长度，单位cm
                orderCase.setWmsWidth(parcelsJsonObject.get("chargeable_width", BigDecimal.class));//仓库测量的宽度，单位cm
                orderCase.setWmsHeight(parcelsJsonObject.get("chargeable_height", BigDecimal.class));//仓库测量的高度，单位cm
                orderCase.setWmsVolume(parcelsJsonObject.get("chargeable_weight", BigDecimal.class));//仓库测量的重量，单位kg
                //计算体积
                //体积(m3) = (长cm * 宽cm * 高cm) / 1000000
                BigDecimal wmsVolume = orderCase.getWmsLength().multiply(orderCase.getWmsWidth()).multiply(orderCase.getWmsHeight()).divide(new BigDecimal("1000000"),3, BigDecimal.ROUND_HALF_UP);
                orderCase.setWmsVolume(wmsVolume);

                // 最终确认 长、宽、高、重、体积
                orderCase.setConfirmLength(parcelsJsonObject.get("chargeable_length", BigDecimal.class));//最终确认长度，单位cm
                orderCase.setConfirmWidth(parcelsJsonObject.get("chargeable_width", BigDecimal.class));//最终确认宽度，单位cm
                orderCase.setConfirmHeight(parcelsJsonObject.get("chargeable_height", BigDecimal.class));//最终确认高度，单位cm
                orderCase.setConfirmWeight(parcelsJsonObject.get("chargeable_weight", BigDecimal.class));//最终确认重量，单位kg
                //计算体积
                //体积(m3) = (长cm * 宽cm * 高cm) / 1000000
                BigDecimal confirmVolume = orderCase.getConfirmLength().multiply(orderCase.getConfirmWidth()).multiply(orderCase.getConfirmHeight()).divide(new BigDecimal("1000000"),3, BigDecimal.ROUND_HALF_UP);
                orderCase.setConfirmVolume(confirmVolume);

                Long picking_time = parcelsJsonObject.get("picking_time", Long.class);
                if(ObjectUtil.isNotEmpty(picking_time)){
                    long l = picking_time*1000L;//秒转为毫秒
                    LocalDateTime confirmWeighDate = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    log.info("confirmWeighDate:{}", confirmWeighDate);
                    orderCase.setConfirmWeighDate(confirmWeighDate);
                }
                orderCase.setStatus(0);//是否已确认（0-未确认,1-已确认）
                orderCase.setRemark("新智慧同步箱号");//备注
                orderCaseList.add(orderCase);
            }

            //遍历，新智慧运单，箱号下的商品
            Object declarations = parcelsJsonObject.get("declarations");
            JSONArray declarationsJSONArray = JSONUtil.parseArray(declarations);
            for (int j=0; j < declarationsJSONArray.size(); j++){
                //订单商品
                JSONObject declarationsJsonObject = declarationsJSONArray.getJSONObject(j);
                String sku = declarationsJsonObject.get("sku", String.class);
                CustomerGoodsVO customerGoodsVO = null;
                if(ObjectUtil.isNotEmpty(sku)){
                    //新智慧商品 sku 不为空
                    customerGoodsVO = customerGoodsMapper.findCustomerGoodsByCustomerIdAndsku(shipmentVO.getCustomerId(), sku);
                }else{
                    //新智慧商品 sku 为空
                    /**
                     * By查询：
                     * 产品中文品名*-name_zh
                     * 产品英文品名*-name_en
                     * 产品材质*-material
                     * 产品海关编码-hscode
                     * 产品用途-usage
                     */
                    Integer customerId = shipmentVO.getCustomerId();//客户id
                    String nameCn = declarationsJsonObject.get("name_zh", String.class);//中文名
                    String nameEn = declarationsJsonObject.get("name_en", String.class);//英文名
                    String materialQuality = declarationsJsonObject.get("material", String.class);//材质
                    String hscode = declarationsJsonObject.get("hscode", String.class);//海关编码
                    String purpose = declarationsJsonObject.get("usage", String.class);//用途

                    Map<String, Object> newWisdomParam = new HashMap<>();
                    newWisdomParam.put("customerId", customerId);
                    newWisdomParam.put("nameCn", nameCn);
                    newWisdomParam.put("nameEn", nameEn);
                    newWisdomParam.put("materialQuality", materialQuality);
                    newWisdomParam.put("hscode", hscode);
                    newWisdomParam.put("purpose", purpose);
                    customerGoodsVO = customerGoodsMapper.findCustomerGoodsByNewWisdomParam(newWisdomParam);
                }

                if(ObjectUtil.isEmpty(customerGoodsVO)){
                    //customerGoodsVO 为null
                    CustomerGoods customerGoods = new CustomerGoods();
                    customerGoods.setCustomerId(shipmentVO.getCustomerId());//客户ID(customer id)
                    customerGoods.setSku(IdUtil.simpleUUID());//SKU商品编码
                    customerGoods.setNameCn(declarationsJsonObject.get("name_zh", String.class));//中文名
                    customerGoods.setNameEn(declarationsJsonObject.get("name_en", String.class));//英文名
                    customerGoods.setMaterialQuality(declarationsJsonObject.get("material", String.class));//材质
                    customerGoods.setHsCode(declarationsJsonObject.get("hscode", String.class));//海关编码
                    customerGoods.setPurpose(declarationsJsonObject.get("usage", String.class));//用途
                    customerGoods.setImageUrl(declarationsJsonObject.get("photos", String.class));//图片地址
                    customerGoods.setIsSensitive("0");//是否敏感货物，1是0否，默认为0
                    customerGoods.setTypes(1);//商品类型(1普货 2特货)
                    customerGoods.setStatus(0);//审核状态代码：1-审核通过，0-等待审核，-1-审核不通过
                    customerGoods.setRemark("原系统同步商品");//备注
                    customerGoodsService.saveOrUpdate(customerGoods);
                    Integer goodId = customerGoods.getId();
                    OrderShop orderShop = new OrderShop();
                    orderShop.setGoodId(goodId);//商品编号(customer_goods id)
                    orderShop.setQuantity(declarationsJsonObject.get("qty", Integer.class));//数量
                    orderShopList.add(orderShop);
                }else{
                    //customerGoodsVO 不为null
                    Integer goodId = customerGoodsVO.getId();
                    OrderShop orderShop = new OrderShop();
                    orderShop.setGoodId(goodId);//商品编号(customer_goods id)
                    orderShop.setQuantity(declarationsJsonObject.get("qty", Integer.class));//数量
                    orderShopList.add(orderShop);
                }
            }
        }

        //对商品进行合并,统计数量
        Map<Integer, List<OrderShop>> integerListMap = groupListByGoodId(orderShopList);
        orderShopList.clear();
        // entrySet遍历，在键和值都需要时使用（最常用）
        for (Map.Entry<Integer, List<OrderShop>> entry : integerListMap.entrySet()) {
            Integer goodId = entry.getKey();//商品id
            List<OrderShop> orderShops = entry.getValue();
            Integer quantity = 0;
            for (int i=0; i<orderShops.size(); i++){
                OrderShop orderShop = orderShops.get(i);
                quantity += orderShop.getQuantity();//数量
            }
            OrderShop shop = new OrderShop();
            shop.setGoodId(goodId);//商品编号(customer_goods id)
            shop.setQuantity(quantity);//数量
            orderShopList.add(shop);
        }
        System.out.println(orderShopList);

    }


    /**
     * 根据币种id，分组
     * @param orderShopList 订单商品
     * @return
     */
    private Map<Integer, List<OrderShop>> groupListByGoodId(List<OrderShop> orderShopList) {
        Map<Integer, List<OrderShop>> map = new HashMap<>();
        for (OrderShop orderShop : orderShopList) {
            Integer key = orderShop.getGoodId();//商品id
            List<OrderShop> tmpList = map.get(key);
            if (CollUtil.isEmpty(tmpList)) {
                tmpList = new ArrayList<>();
                tmpList.add(orderShop);
                map.put(key, tmpList);
            } else {
                tmpList.add(orderShop);
            }
        }
        return map;
    }

    /**
     * 组装数据
     * @param shipmentVO
     */
    private void fit(ShipmentVO shipmentVO) {
        String shipmentJson = shipmentVO.getShipmentJson();
        if (StrUtil.isNotEmpty(shipmentJson)) {
            ShipmentVO shipment = null;
            try {
                shipment = JSONUtil.toBean(shipmentJson, ShipmentVO.class);
                shipmentVO.setAttrs(shipment.getAttrs());
                shipmentVO.setTo_address(shipment.getTo_address());
                shipmentVO.setFrom_address(shipment.getFrom_address());
                shipmentVO.setCharge_list(shipment.getCharge_list());
                shipmentVO.setParcels(shipment.getParcels());
                shipmentVO.setPicking_time(shipment.getPicking_time());
                shipmentVO.setRates_time(shipment.getRates_time());
                shipmentVO.setCreat_time(shipment.getCreat_time());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
