package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.*;
import com.jayud.mall.utils.NumberGeneratedUtils;
import com.jayud.mall.utils.SnowflakeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 产品订单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Slf4j
@RefreshScope
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {


    //南京新智慧api
    @Value("${nanjing.newwisdom.access_token:}")
    private String access_token;
    //1、 创建运单
    @Value("${nanjing.newwisdom.urls.create:}")
    private String create;
    //2、 标签获取
    @Value("${nanjing.newwisdom.urls.get_labels:}")
    private String get_labels;
    //3、 服务类型获取
    @Value("${nanjing.newwisdom.urls.get_services:}")
    private String get_services;
    //4、 取消订单
    @Value("${nanjing.newwisdom.urls.shipment_void:}")
    private String shipment_void;
    //5、 查询路由信息
    @Value("${nanjing.newwisdom.urls.tracking:}")
    private String tracking;
    //6、 获取运单信息
    @Value("${nanjing.newwisdom.urls.info:}")
    private String info;
    //7、 修改客户重量尺寸
    @Value("${nanjing.newwisdom.urls.update_weight:}")
    private String update_weight;
    //8、 查看账户余额
    @Value("${nanjing.newwisdom.urls.account:}")
    private String account;

    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    OrderCustomsFileMapper orderCustomsFileMapper;
    @Autowired
    OrderClearanceFileMapper orderClearanceFileMapper;
    @Autowired
    OrderShopMapper orderShopMapper;
    @Autowired
    OrderCaseMapper orderCaseMapper;
    @Autowired
    OrderCopeReceivableMapper orderCopeReceivableMapper;
    @Autowired
    OrderCopeWithMapper orderCopeWithMapper;
    @Autowired
    OrderPickMapper orderPickMapper;
    @Autowired
    TemplateCopeReceivableMapper templateCopeReceivableMapper;
    @Autowired
    TemplateCopeWithMapper templateCopeWithMapper;
    @Autowired
    FabWarehouseMapper fabWarehouseMapper;
    @Autowired
    OfferInfoMapper offerInfoMapper;
    @Autowired
    OrderConfMapper orderConfMapper;
    @Autowired
    QuotationTypeMapper quotationTypeMapper;
    @Autowired
    LogisticsTrackMapper logisticsTrackMapper;
    @Autowired
    ShippingAreaMapper shippingAreaMapper;
    @Autowired
    WaybillTaskRelevanceMapper waybillTaskRelevanceMapper;
    @Autowired
    WaybillTaskMapper waybillTaskMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    DeliveryAddressMapper deliveryAddressMapper;
    @Autowired
    InlandFeeCostMapper inlandFeeCostMapper;
    @Autowired
    CurrencyInfoMapper currencyInfoMapper;
    @Autowired
    OrderInteriorStatusMapper orderInteriorStatusMapper;
    @Autowired
    OrderCaseWmsMapper orderCaseWmsMapper;

    @Autowired
    BaseService baseService;
    @Autowired
    IOrderCustomsFileService orderCustomsFileService;
    @Autowired
    IOrderClearanceFileService orderClearanceFileService;
    @Autowired
    IOrderCaseService orderCaseService;
    @Autowired
    IOrderShopService orderShopService;
    @Autowired
    IOrderPickService orderPickService;
    @Autowired
    IOrderCopeReceivableService orderCopeReceivableService;
    @Autowired
    IOrderCopeWithService orderCopeWithService;
    @Autowired
    IWaybillTaskRelevanceService waybillTaskRelevanceService;
    @Autowired
    ITemplateFileService templateFileService;
    @Autowired
    ICounterCaseService counterCaseService;
    @Autowired
    IWaybillTaskService waybillTaskService;
    @Autowired
    IShipmentService shipmentService;
    @Autowired
    IOrderInteriorStatusService orderInteriorStatusService;
    @Autowired
    ILogisticsTrackService logisticsTrackService;
    @Autowired
    IOrderCaseWmsService orderCaseWmsService;



    @Override
    public IPage<OrderInfoVO> findOrderInfoByPage(QueryOrderInfoForm form) {
        //定义分页参数
        Page<OrderInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<OrderInfoVO> pageInfo = orderInfoMapper.findOrderInfoByPage(page, form);

        List<OrderInfoVO> records = pageInfo.getRecords();
        if(records.size() > 0){
            records.forEach(ororderInfoVO -> {
                Long orderId = ororderInfoVO.getId();
                List<String> confinfos = orderInfoMapper.findOrderConfInfoByOrderId(orderId);

                if(CollUtil.isNotEmpty(confinfos)){
                    String confInfo = "";
                    for (int i=0; i<confinfos.size(); i++){
                        if(ObjectUtil.isNotEmpty(confinfos.get(i))){
                            confInfo += confinfos.get(i);
                        }
                    }
                    ororderInfoVO.setConfInfo(confInfo);
                }
            });
        }
        return pageInfo;
    }

    @Override
    public CommonResult<OrderInfoVO> lookOrderInfoFile(Long id) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(id);
        Long orderId = orderInfoVO.getId();//订单Id

        //订单对应报关文件list
        QueryWrapper<OrderCustomsFile> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("order_id", orderId);
        List<OrderCustomsFile> orderCustomsFiles = orderCustomsFileMapper.selectList(queryWrapper1);
        List<OrderCustomsFileVO> orderCustomsFileVOList =
                ConvertUtil.convertList(orderCustomsFiles, OrderCustomsFileVO.class);
        if (orderCustomsFileVOList.size() > 0) {
            orderCustomsFileVOList.forEach(orderCustomsFileVO -> {
                String templateUrl = orderCustomsFileVO.getTemplateUrl();
                if(templateUrl != null && templateUrl.length() > 0){
                    String json = templateUrl;
                    try {
                        List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                        });
                        orderCustomsFileVO.setTemplateUrlVOS(templateUrlVOS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("json格式错误");
                        orderCustomsFileVO.setTemplateUrlVOS(new ArrayList<>());
                    }
                }else{
                    orderCustomsFileVO.setTemplateUrlVOS(new ArrayList<>());
                }
            });
        }
        orderInfoVO.setOrderCustomsFileVOList(orderCustomsFileVOList);

        //订单对应清关文件list
        QueryWrapper<OrderClearanceFile> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("order_id", orderId);
        List<OrderClearanceFile> orderClearanceFiles = orderClearanceFileMapper.selectList(queryWrapper2);
        List<OrderClearanceFileVO> orderClearanceFileVOList =
                ConvertUtil.convertList(orderClearanceFiles, OrderClearanceFileVO.class);
        if(orderClearanceFileVOList.size() > 0){
            orderClearanceFileVOList.forEach(orderClearanceFileVO -> {
                String templateUrl = orderClearanceFileVO.getTemplateUrl();
                if(templateUrl != null && templateUrl.length() > 0){
                    String json = templateUrl;
                    try {
                        List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                        });
                        orderClearanceFileVO.setTemplateUrlVOS(templateUrlVOS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("json格式错误");
                        orderClearanceFileVO.setTemplateUrlVOS(new ArrayList<>());
                    }
                }else{
                    orderClearanceFileVO.setTemplateUrlVOS(new ArrayList<>());
                }
            });
        }
        orderInfoVO.setOrderClearanceFileVOList(orderClearanceFileVOList);
        return CommonResult.success(orderInfoVO);
    }

    @Override
    public CommonResult<OrderCustomsFileVO> passOrderCustomsFile(Long id) {
        OrderCustomsFile orderCustomsFile = orderCustomsFileMapper.selectById(id);
        orderCustomsFile.setAuditStatus(1);//审核状态(0审核不通过  1审核通过)
        orderCustomsFileService.saveOrUpdate(orderCustomsFile);
        OrderCustomsFileVO orderCustomsFileVO = ConvertUtil.convert(orderCustomsFile, OrderCustomsFileVO.class);
        return CommonResult.success(orderCustomsFileVO);
    }

    @Override
    public CommonResult<OrderClearanceFileVO> passOrderClearanceFile(Long id) {
        OrderClearanceFile orderClearanceFile = orderClearanceFileService.getById(id);
        orderClearanceFile.setAuditStatus(1);//审核状态(0审核不通过  1审核通过)
        orderClearanceFileService.saveOrUpdate(orderClearanceFile);
        OrderClearanceFileVO orderClearanceFileVO = ConvertUtil.convert(orderClearanceFile, OrderClearanceFileVO.class);
        return CommonResult.success(orderClearanceFileVO);
    }

    @Override
    public CommonResult<OrderCustomsFileVO> onPassCustomsFile(Long id) {
        OrderCustomsFile orderCustomsFile = orderCustomsFileMapper.selectById(id);
        orderCustomsFile.setAuditStatus(0);//审核状态(0审核不通过  1审核通过)
        orderCustomsFileService.saveOrUpdate(orderCustomsFile);
        OrderCustomsFileVO orderCustomsFileVO = ConvertUtil.convert(orderCustomsFile, OrderCustomsFileVO.class);
        return CommonResult.success(orderCustomsFileVO);
    }

    @Override
    public CommonResult<OrderClearanceFileVO> onPassOrderClearanceFile(Long id) {
        OrderClearanceFile orderClearanceFile = orderClearanceFileService.getById(id);
        orderClearanceFile.setAuditStatus(0);//审核状态(0审核不通过  1审核通过)
        orderClearanceFileService.saveOrUpdate(orderClearanceFile);
        OrderClearanceFileVO orderClearanceFileVO = ConvertUtil.convert(orderClearanceFile, OrderClearanceFileVO.class);
        return CommonResult.success(orderClearanceFileVO);
    }

    @Override
    public CommonResult<OrderInfoVO> lookOrderInfoGoods(Long id) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(id);
        Long orderId = orderInfoVO.getId();//订单Id

        /*订单对应商品：order_shop*/
        List<OrderShopVO> orderShopVOList = orderShopMapper.findOrderShopByOrderId(orderId);
        orderInfoVO.setOrderShopVOList(orderShopVOList);

        /*订单对应箱号信息:order_case*/
        List<OrderCaseVO> orderCaseVOList = orderCaseMapper.findOrderCaseByOrderId(orderId);
        orderInfoVO.setOrderCaseVOList(orderCaseVOList);

        return CommonResult.success(orderInfoVO);
    }

    @Override
    public void updateOrderCase(List<OrderCaseForm> list) {
        List<OrderCase> orderCaseList = ConvertUtil.convertList(list, OrderCase.class);
        orderCaseService.saveOrUpdateBatch(orderCaseList);
    }

    @Override
    public CommonResult<OrderInfoVO> lookOrderInfoConf(Long id) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(id);
        Long orderId = orderInfoVO.getId();//订单Id

        /*订单对应箱号配载信息:order_case、order_conf*/
        List<OrderCaseConfVO> orderCaseConfVOList = orderCaseMapper.findOrderCaseConfByOrderId(orderId);
        orderInfoVO.setOrderCaseConfVOList(orderCaseConfVOList);

        return CommonResult.success(orderInfoVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateOrderCaseConf(SaveCounterCaseForm form) {
        return counterCaseService.saveCounterCase(form);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateOrderCaseConf2(SaveCounterCase2Form form) {
        //提单柜号id(ocean_counter id)
        Long oceanCounterId = form.getOceanCounterId();
        //运单箱号id(order_case id) list
        List<OrderCaseConfVO> orderCaseConfVOList = form.getOrderCaseConfVOList();

        if(CollUtil.isEmpty(orderCaseConfVOList)){
            return CommonResult.error(-1, "运单箱号不存在，无法修改");
        }
        //先查询，在做批量修改
        List<Long> orderCaseIds = new ArrayList<>();
        orderCaseConfVOList.forEach(orderCaseConfVO->{
            Long orderCaseId = orderCaseConfVO.getId();
            orderCaseIds.add(orderCaseId);
        });

        AuthUser user = baseService.getUser();

        //查询关联表的运单箱号，
        QueryWrapper<CounterCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("order_case_id", orderCaseIds);
        List<CounterCase> list = counterCaseService.list(queryWrapper);
        list.forEach(counterCase->{
            counterCase.setOceanCounterId(oceanCounterId);//运单箱号关联新的提单柜号id
            counterCase.setUserId(user.getId().intValue());
            counterCase.setUserName(user.getName());
        });

        //批量修改运单箱号的对应的提单柜号
        counterCaseService.saveOrUpdateBatch(list);

        return CommonResult.success("订单修改配载信息成功");
    }


    @Override
    public CommonResult<OrderInfoVO> lookOrderInfoCost(Long id) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(id);
        Long orderId = orderInfoVO.getId();//订单Id

        /*订单对应应收费用明细:order_cope_receivable*/
        List<OrderCopeReceivableVO> orderCopeReceivableVOList =
                orderCopeReceivableMapper.findOrderCopeReceivableByOrderId(orderId);
        orderInfoVO.setOrderCopeReceivableVOList(orderCopeReceivableVOList);

        /*订单对应应付费用明细:order_cope_with*/
        List<OrderCopeWithVO> orderCopeWithVOList =
                orderCopeWithMapper.findOrderCopeWithByOrderId(orderId);
        orderInfoVO.setOrderCopeWithVOList(orderCopeWithVOList);

        return CommonResult.success(orderInfoVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult updateOrderInfoCost(OrderInfoCostForm form) {
        /*订单对应应收费用明细*/
        List<OrderCopeReceivableForm> orderCopeReceivableVOList = form.getOrderCopeReceivableVOList();
        if(orderCopeReceivableVOList != null && orderCopeReceivableVOList.size() > 0){
            List<OrderCopeReceivable> orderCopeReceivables =
                    ConvertUtil.convertList(orderCopeReceivableVOList, OrderCopeReceivable.class);
            orderCopeReceivableService.saveOrUpdateBatch(orderCopeReceivables);
        }
        /*订单对应应付费用明细*/
        List<OrderCopeWithForm> orderCopeWithVOList = form.getOrderCopeWithVOList();
        if(orderCopeWithVOList != null && orderCopeWithVOList.size() > 0){
            List<OrderCopeWith> orderCopeWiths =
                    ConvertUtil.convertList(orderCopeWithVOList, OrderCopeWith.class);
            orderCopeWithService.saveOrUpdateBatch(orderCopeWiths);
        }
        return CommonResult.success("修改订单费用信息成功！");
    }

    @Override
    public CommonResult<OrderInfoVO> lookOrderInfoDetails(Long id) {
        /**订单信息**/
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(id);
        Long orderId = orderInfoVO.getId();//订单Id

        /**订单物流轨迹**/
        List<LogisticsTrackVO> logisticsTrackVOS = logisticsTrackMapper.findLogisticsTrackByOrderId(orderId.toString());
        orderInfoVO.setLogisticsTrackVOS(logisticsTrackVOS);

        /**货物信息**/
        /*订单对应商品：order_shop*/
        List<OrderShopVO> orderShopVOList = orderShopMapper.findOrderShopByOrderId(orderId);
        orderInfoVO.setOrderShopVOList(orderShopVOList);

        /**订单对应箱号信息:order_case**/
        List<OrderCaseVO> orderCaseVOList = orderCaseMapper.findOrderCaseByOrderId(orderId);
        orderInfoVO.setOrderCaseVOList(orderCaseVOList);

        /**提货信息**/
        List<OrderPickVO> orderPickVOList = orderPickMapper.findOrderPickByOrderId(orderId);
        orderInfoVO.setOrderPickVOList(orderPickVOList);

        /**配载信息**/
        /*订单对应箱号配载信息:order_case、order_conf*/
        List<OrderCaseConfVO> orderCaseConfVOList = orderCaseMapper.findOrderCaseConfByOrderId(orderId);
        orderInfoVO.setOrderCaseConfVOList(orderCaseConfVOList);

        /**费用信息**/
        //将币种信息转换为map，cid为键，币种信息为值
        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));

        /*订单对应应收费用明细:order_cope_receivable*/
        List<OrderCopeReceivableVO> orderCopeReceivableVOList =
                orderCopeReceivableMapper.findOrderCopeReceivableByOrderId(orderId);
        orderInfoVO.setOrderCopeReceivableVOList(orderCopeReceivableVOList);
        /*订单对应应收费用汇总ist*/
        List<AggregateAmountVO> orderCopeReceivableAggregate = new ArrayList<>();
        Map<Integer, List<OrderCopeReceivableVO>> stringListMap1 = groupListByCid1(orderCopeReceivableVOList);
        for (Map.Entry<Integer, List<OrderCopeReceivableVO>> entry : stringListMap1.entrySet()) {
            Integer cid = entry.getKey();
            List<OrderCopeReceivableVO> orderCopeReceivableVOS = entry.getValue();

            CurrencyInfoVO currencyInfoVO = cidMap.get(Long.valueOf(cid));

            BigDecimal amountSum = new BigDecimal("0");
            for (int i=0; i<orderCopeReceivableVOS.size(); i++){
                OrderCopeReceivableVO orderCopeReceivableVO = orderCopeReceivableVOS.get(i);
                BigDecimal amount = orderCopeReceivableVO.getAmount();
                amountSum = amountSum.add(amount);
            }
            AggregateAmountVO aggregateAmountVO = new AggregateAmountVO();
            aggregateAmountVO.setAmount(amountSum);//金额
            aggregateAmountVO.setCid(cid);
            aggregateAmountVO.setCurrencyCode(currencyInfoVO.getCurrencyCode());
            aggregateAmountVO.setCurrencyName(currencyInfoVO.getCurrencyName());
            orderCopeReceivableAggregate.add(aggregateAmountVO);
        }
        orderInfoVO.setOrderCopeReceivableAggregate(orderCopeReceivableAggregate);

        /*订单对应应付费用明细:order_cope_with*/
        List<OrderCopeWithVO> orderCopeWithVOList =
                orderCopeWithMapper.findOrderCopeWithByOrderId(orderId);
        orderInfoVO.setOrderCopeWithVOList(orderCopeWithVOList);
        /*订单对应应付费用汇总list*/
        List<AggregateAmountVO> orderCopeWithAggregate = new ArrayList<>();
        Map<Integer, List<OrderCopeWithVO>> stringListMap2 = groupListByCid2(orderCopeWithVOList);
        for (Map.Entry<Integer, List<OrderCopeWithVO>> entry : stringListMap2.entrySet()) {
            Integer cid = entry.getKey();
            List<OrderCopeWithVO> orderCopeWithVOS = entry.getValue();
            CurrencyInfoVO currencyInfoVO = cidMap.get(Long.valueOf(cid));
            BigDecimal amountSum = new BigDecimal("0");
            for (int i=0; i<orderCopeWithVOS.size(); i++){
                OrderCopeWithVO orderCopeWithVO = orderCopeWithVOS.get(i);
                BigDecimal amount = orderCopeWithVO.getAmount();
                amountSum = amountSum.add(amount);
            }
            AggregateAmountVO aggregateAmountVO = new AggregateAmountVO();
            aggregateAmountVO.setAmount(amountSum);//金额
            aggregateAmountVO.setCid(cid);
            aggregateAmountVO.setCurrencyCode(currencyInfoVO.getCurrencyCode());
            aggregateAmountVO.setCurrencyName(currencyInfoVO.getCurrencyName());
            orderCopeWithAggregate.add(aggregateAmountVO);
        }
        orderInfoVO.setOrderCopeWithAggregate(orderCopeWithAggregate);

        /**文件信息**/
        //订单对应报关文件list
        QueryWrapper<OrderCustomsFile> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("order_id", orderId);
        List<OrderCustomsFile> orderCustomsFiles = orderCustomsFileMapper.selectList(queryWrapper1);
        List<OrderCustomsFileVO> orderCustomsFileVOList =
                ConvertUtil.convertList(orderCustomsFiles, OrderCustomsFileVO.class);
        orderInfoVO.setOrderCustomsFileVOList(orderCustomsFileVOList);

        //订单对应清关文件list
        QueryWrapper<OrderClearanceFile> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("order_id", orderId);
        List<OrderClearanceFile> orderClearanceFiles = orderClearanceFileMapper.selectList(queryWrapper2);
        List<OrderClearanceFileVO> orderClearanceFileVOList =
                ConvertUtil.convertList(orderClearanceFiles, OrderClearanceFileVO.class);
        orderInfoVO.setOrderClearanceFileVOList(orderClearanceFileVOList);

        return CommonResult.success(orderInfoVO);
    }

    /**
     * 根据币种id，订单应收费用明细 进行分组
     * @param orderCopeReceivableVOList 订单应收费用明细
     * @return
     */
    public Map<Integer, List<OrderCopeReceivableVO>> groupListByCid1(List<OrderCopeReceivableVO> orderCopeReceivableVOList) {
        Map<Integer, List<OrderCopeReceivableVO>> map = new HashMap<>();
        for (OrderCopeReceivableVO orderCopeReceivableVO : orderCopeReceivableVOList) {
            Integer key = orderCopeReceivableVO.getCid();
            List<OrderCopeReceivableVO> tmpList = map.get(key);
            if (CollUtil.isEmpty(tmpList)) {
                tmpList = new ArrayList<>();
                tmpList.add(orderCopeReceivableVO);
                map.put(key, tmpList);
            } else {
                tmpList.add(orderCopeReceivableVO);
            }
        }
        return map;
    }

    /**
     * 根据币种id，订单应付费用明细 进行分组
     * @param orderCopeWithVOList 订单应付费用明细
     * @return
     */
    public Map<Integer, List<OrderCopeWithVO>> groupListByCid2(List<OrderCopeWithVO> orderCopeWithVOList) {
        Map<Integer, List<OrderCopeWithVO>> map = new HashMap<>();
        for (OrderCopeWithVO orderCopeWithVO : orderCopeWithVOList) {
            Integer key = orderCopeWithVO.getCid();
            List<OrderCopeWithVO> tmpList = map.get(key);
            if (CollUtil.isEmpty(tmpList)) {
                tmpList = new ArrayList<>();
                tmpList.add(orderCopeWithVO);
                map.put(key, tmpList);
            } else {
                tmpList.add(orderCopeWithVO);
            }
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<OrderInfoVO> temporaryStorageOrderInfo(OrderInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        if(ObjectUtil.isEmpty(customerUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录。");
        }
        //保存-产品订单表：order_info
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);
        Integer offerInfoId = orderInfo.getOfferInfoId();//报价id，运价id
        OfferInfoVO offerInfoVO = offerInfoMapper.lookOfferInfoFare(Long.valueOf(offerInfoId));

        Integer clearingWay = offerInfoVO.getClearingWay();//结算方式(1票结 2按客户的结算方式(客户表customer clearing_way))
        if(ObjectUtil.isNotEmpty(clearingWay)){
            if(clearingWay.equals("2")){
                Integer customerId = customerUser.getId();
                CustomerVO customerVO = customerMapper.findCustomerById(customerId);
                Integer clearingWay1 = customerVO.getClearingWay();//结算方式(1票结 2月结)
                clearingWay = clearingWay1;
            }
        }else{
            clearingWay = 1;
        }
        orderInfo.setClearingWay(clearingWay);//订单的结算方式  结算方式(1票结 2月结)

        //集货仓库代码 -> 集货仓库名称
        String storeGoodsWarehouseCode1 = orderInfo.getStoreGoodsWarehouseCode();
        ShippingAreaVO shippingAreaVO = shippingAreaMapper.findShippingAreaByWarehouseCode(storeGoodsWarehouseCode1);
        String storeGoodsWarehouseName = shippingAreaVO.getWarehouseName();
        orderInfo.setStoreGoodsWarehouseName(storeGoodsWarehouseName);
        //目的仓库代码 -> 目的仓库名称
        String destinationWarehouseCode = orderInfo.getDestinationWarehouseCode();
        FabWarehouseVO fabWarehouseVO = fabWarehouseMapper.findFabWarehouseByWarehouseCode(destinationWarehouseCode);
        String destinationWarehouseName = fabWarehouseVO.getWarehouseName();
        orderInfo.setDestinationWarehouseName(destinationWarehouseName);

        //判断订单号是否存在，不存在则新增
        String orderNo = form.getOrderNo();
        if(orderNo == null || orderNo == ""){
            orderNo = String.valueOf(SnowflakeUtils.getOrderNo());//雪花算法生成订单id
            orderInfo.setOrderNo(orderNo);
            orderInfo.setCustomerId(customerUser.getId());
            orderInfo.setCreateTime(LocalDateTime.now());
            orderInfo.setCreateUserId(customerUser.getId());
            orderInfo.setCreateUserName(customerUser.getUserName());
        }

        //状态码
        orderInfo.setFrontStatusCode(OrderEnum.FRONT_DRAFT.getCode());
        orderInfo.setFrontStatusName(OrderEnum.FRONT_DRAFT.getName());
        orderInfo.setAfterStatusCode(OrderEnum.AFTER_DRAFT.getCode());
        orderInfo.setAfterStatusName(OrderEnum.AFTER_DRAFT.getName());

        //订单对应箱号信息:order_case
        List<OrderCaseVO> orderCaseVOList = form.getOrderCaseVOList();
        CaseVO caseVO = structuralOrderCase(orderCaseVOList, offerInfoId);
        BigDecimal totalChargeWeight = caseVO.getTotalChargeWeight();//客户预报的总收费重 收费重
        BigDecimal totalVolumeWeight = caseVO.getTotalVolumeWeight();//材积重
        BigDecimal totalAsnWeight = caseVO.getTotalAsnWeight();//实际重
        BigDecimal totalAsnVolume = caseVO.getTotalAsnVolume();//实际体积
        Integer totalCase = caseVO.getTotalCase();//总箱数

        orderInfo.setChargeWeight(totalChargeWeight);
        orderInfo.setVolumeWeight(totalVolumeWeight);
        orderInfo.setActualWeight(totalAsnWeight);
        orderInfo.setActualVolume(totalAsnVolume);
        orderInfo.setTotalCartons(totalCase);

        this.saveOrUpdate(orderInfo);

        //保存-订单对应箱号信息:order_case
//        List<OrderCaseVO> orderCaseVOList = form.getOrderCaseVOList();
        List<OrderCase> orderCaseList = ConvertUtil.convertList(orderCaseVOList, OrderCase.class);
        orderCaseList.forEach(orderCase -> {
            orderCase.setOrderId(orderInfo.getId().intValue());
        });
        QueryWrapper<OrderCase> orderCaseQueryWrapper = new QueryWrapper<>();
        orderCaseQueryWrapper.eq("order_id", orderInfo.getId());
        orderCaseService.remove(orderCaseQueryWrapper);
        orderCaseService.saveOrUpdateBatch(orderCaseList);

        //保存-订单对应商品：order_shop
        List<OrderShopVO> orderShopVOList = form.getOrderShopVOList();
        List<OrderShop> orderShopList = ConvertUtil.convertList(orderShopVOList, OrderShop.class);
        orderShopList.forEach(orderShop -> {
            orderShop.setOrderId(orderInfo.getId().intValue());
            orderShop.setCreateTime(LocalDateTime.now());
        });
        QueryWrapper<OrderShop> orderShopQueryWrapper = new QueryWrapper<>();
        orderShopQueryWrapper.eq("order_id", orderInfo.getId());
        orderShopService.remove(orderShopQueryWrapper);
        orderShopService.saveOrUpdateBatch(orderShopList);

        //保存-订单对应提货信息表：order_pick
        List<OrderPickVO> orderPickVOList = form.getOrderPickVOList();
        if(CollUtil.isNotEmpty(orderPickVOList)){
            //to 集货仓库
            String to_country = shippingAreaVO.getCountryName();
            String to_province = shippingAreaVO.getStateName();
            String to_city = shippingAreaVO.getCityName();
            String to_region = shippingAreaVO.getRegionName();

            List<OrderPick> orderPickList = ConvertUtil.convertList(orderPickVOList, OrderPick.class);
            orderPickList.forEach(orderPick -> {


                //form 提货地址
                //  `address_id` int(11) DEFAULT NULL COMMENT '提货地址id(delivery_address id)',
                Integer addressId = orderPick.getAddressId();

                DeliveryAddressVO deliveryAddressVO = deliveryAddressMapper.findDeliveryAddressById(addressId);
                String from_country = deliveryAddressVO.getCountryName();
                String from_province = deliveryAddressVO.getStateName();
                String from_city = deliveryAddressVO.getCityName();
                String from_region = deliveryAddressVO.getRegionName();
                Integer unit = orderPick.getUnit();//单位(1公斤 2方 3票 4柜)    前端填写选择参数

                BigDecimal weight = orderPick.getWeight();//重量  重量直接作为 数量，进行计费

                orderPick.setOrderId(orderInfo.getId());
                orderPick.setFromCountry(from_country);
                orderPick.setFromProvince(from_province);
                orderPick.setFromCity(from_city);
                orderPick.setFromRegion(from_region);
                orderPick.setToCountry(to_country);
                orderPick.setToProvince(to_province);
                orderPick.setToCity(to_city);
                orderPick.setToRegion(to_region);
                orderPick.setCount(weight);

                Map<String, Object> paraMap = new HashMap<>();
                paraMap.put("from_country", from_country);
                paraMap.put("from_province", from_province);
                paraMap.put("from_city", from_city);
                paraMap.put("from_region", from_region);
                paraMap.put("to_country", to_country);
                paraMap.put("to_province", to_province);
                paraMap.put("to_city", to_city);
                paraMap.put("to_region", to_region);
                paraMap.put("unit", unit);
                InlandFeeCostVO inlandFeeCostVO = inlandFeeCostMapper.findInlandFeeCostByPara(paraMap);

                if(ObjectUtil.isNotEmpty(inlandFeeCostVO)){
                    BigDecimal unitPrice = inlandFeeCostVO.getUnitPrice();
                    Integer cid = inlandFeeCostVO.getCid();
                    orderPick.setUnitPrice(unitPrice);
                    orderPick.setCid(cid);
                    orderPick.setStatus("1");//1有效 费用有效
                    orderPick.setFeeRemark("内陆费，路线匹配，正常计算");
                }else{
                    orderPick.setUnitPrice(new BigDecimal("0"));
                    orderPick.setCid(1);
                    orderPick.setStatus("0");//0无效 费用待定
                    orderPick.setFeeRemark("内陆费，路线不存在，费用待定");
                }
                //金额 = 单价 * 数量
                BigDecimal amount = orderPick.getUnitPrice().multiply(orderPick.getCount());
                orderPick.setAmount(amount);
            });
            QueryWrapper<OrderPick> orderPickQueryWrapper = new QueryWrapper<>();
            orderPickQueryWrapper.eq("order_id", orderInfo.getId());
            orderPickService.remove(orderPickQueryWrapper);
            orderPickService.saveOrUpdateBatch(orderPickList);
        }


        //订单对应报关文件 order_customs_file
        //`need_declare` int(11) DEFAULT NULL COMMENT '是否需要报关0-否，1-是 (订单对应报关文件:order_customs_file)',
        List<OrderCustomsFile> orderCustomsFileList = getOrderCustomsFiles(orderInfo, offerInfoId);
        orderCustomsFileList.forEach(orderCustomsFile -> {
            orderCustomsFile.setOrderId(orderInfo.getId().intValue());
        });
        QueryWrapper<OrderCustomsFile> orderCustomsFileQueryWrapper = new QueryWrapper<>();
        orderCustomsFileQueryWrapper.eq("order_id", orderInfo.getId());
        orderCustomsFileService.remove(orderCustomsFileQueryWrapper);
        orderCustomsFileService.saveOrUpdateBatch(orderCustomsFileList);

        //订单对应清关文件 order_clearance_file
        //`need_clearance` int(11) DEFAULT NULL COMMENT '是否需要清关0-否，1-是 (订单对应清关文件:order_clearance_file)',
        List<OrderClearanceFile> orderClearanceFileList = getOrderClearanceFiles(orderInfo, offerInfoId);
        orderClearanceFileList.forEach(orderClearanceFile -> {
            orderClearanceFile.setOrderId(orderInfo.getId().intValue());
        });
        QueryWrapper<OrderClearanceFile> orderClearanceFileQueryWrapper = new QueryWrapper<>();
        orderClearanceFileQueryWrapper.eq("order_id", orderInfo.getId());
        orderClearanceFileService.remove(orderClearanceFileQueryWrapper);
        orderClearanceFileService.saveOrUpdateBatch(orderClearanceFileList);

        //根据运价(报价)，找报价模板，报价模板对应应收应付费用信息
        OfferInfo offerInfo = offerInfoMapper.selectById(offerInfoId);
        Integer qie = offerInfo.getQie();//报价模板id(quotation_template id)
        Long orderId = orderInfo.getId();//订单id

        //TODO 订单 对应的 费用
        //订单 应收 费用
        List<OrderCopeReceivable> orderCopeReceivables = new ArrayList<>();
        //(1)订柜尺寸 对应 应收`海运费`
        String reserveSize = form.getReserveSize();
        /*订柜尺寸：海运费规格*/
        List<TemplateCopeReceivableVO> oceanFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableOceanFeeByQie(qie);
        for (int i = 0; i<oceanFeeList.size(); i++){
            TemplateCopeReceivableVO templateCopeReceivableVO = oceanFeeList.get(i);
            String specificationCode = templateCopeReceivableVO.getSpecificationCode();
            //查看订舱区间，判断费用代码是否相等，相等则列入海运费 费用明细
            if(specificationCode.equals(reserveSize)){

                BigDecimal min = templateCopeReceivableVO.getMin() == null ? new BigDecimal("0") : templateCopeReceivableVO.getMin();
                BigDecimal max = templateCopeReceivableVO.getMax() == null ? new BigDecimal("0") : templateCopeReceivableVO.getMax();
                totalChargeWeight = caseVO.getTotalChargeWeight();//客户预报的总收费重 收费重
//                if(totalChargeWeight.compareTo(min) == -1 || totalChargeWeight.compareTo(max) == 1){
//                    Asserts.fail(ResultEnum.UNKNOWN_ERROR, "收费重超出或小于，订舱区间的所对应的数量范围");
//                }

                OrderCopeReceivable orderCopeReceivable = new OrderCopeReceivable();
                orderCopeReceivable.setOrderId(orderId);//订单ID(order_info id)
                orderCopeReceivable.setCostCode(templateCopeReceivableVO.getCostCode());//费用代码(cost_item cost_code)
                orderCopeReceivable.setCostName(templateCopeReceivableVO.getCostName());//费用名称(cost_item cost_name)

                orderCopeReceivable.setCalculateWay(templateCopeReceivableVO.getCalculateWay());//计算方式(1自动 2手动)
                orderCopeReceivable.setCount(totalChargeWeight);//数量 --> 海运费数量，取订单箱号的 收费重
                orderCopeReceivable.setUnitPrice(templateCopeReceivableVO.getUnitPrice());//单价
                orderCopeReceivable.setAmount(totalChargeWeight.multiply(templateCopeReceivableVO.getUnitPrice()).setScale(2,BigDecimal.ROUND_HALF_UP));//金额 = 数量 * 单价 (保留两位小数)

                orderCopeReceivable.setCid(templateCopeReceivableVO.getCid());//币种(currency_info id)
                orderCopeReceivable.setRemarks(templateCopeReceivableVO.getRemarks());//描述
                //应收 海运费
                orderCopeReceivables.add(orderCopeReceivable);
                break;
            }
        }
        //(2)集货仓库 对应 应收`内陆费`
        String storeGoodsWarehouseCode = form.getStoreGoodsWarehouseCode();
        /*集货仓库：陆运费规格*/
        List<TemplateCopeReceivableVO> inlandFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableInlandFeeListByQie(qie);

        //是否上门提货 (0否 1是,order_pick) 为是，才计算内陆费，否则不计算内陆费
        Integer isPick = orderInfo.getIsPick();
        if(isPick == 1){
            //汇总，提货地址，的内陆费   汇总的金额，为单价；数量固定为1   (金额=单价*1)
            List<OrderPickVO> orderPickList = orderPickMapper.findOrderPickByOrderId(orderInfo.getId());
            String remarks = "内陆费确认";
            BigDecimal amount = new BigDecimal("0");
            for(int i=0; i<orderPickList.size(); i++){
                OrderPickVO orderPickVO = orderPickList.get(i);
                String status = orderPickVO.getStatus();
                if(status.equals("0")){
                    //内陆费为待定
                    remarks = "内陆费待定";
                    break;
                }
                amount = amount.add(orderPickVO.getAmount());
            }

            for (int i=0; i<inlandFeeList.size(); i++){
                TemplateCopeReceivableVO templateCopeReceivableVO = inlandFeeList.get(i);
                String specificationCode = templateCopeReceivableVO.getSpecificationCode();
                //查看集货仓库，判断费用代码是否相等，相等则列入陆运费 费用明细
                if(specificationCode.equals(storeGoodsWarehouseCode)){
                    OrderCopeReceivable orderCopeReceivable = new OrderCopeReceivable();
                    orderCopeReceivable.setOrderId(orderId);//订单ID(order_info id)
                    orderCopeReceivable.setCostCode(templateCopeReceivableVO.getCostCode());//费用代码(cost_item cost_code)
                    orderCopeReceivable.setCostName(templateCopeReceivableVO.getCostName());//费用名称(cost_item cost_name)

                    orderCopeReceivable.setCalculateWay(templateCopeReceivableVO.getCalculateWay());//计算方式(1自动 2手动)
                    orderCopeReceivable.setCount(new BigDecimal("1"));//数量  内陆费，数量固定为1，实际上是提货地址的内陆费
                    orderCopeReceivable.setUnitPrice(amount.setScale(2,BigDecimal.ROUND_HALF_UP));//单价               金额，直接作为单价
                    orderCopeReceivable.setAmount(amount.setScale(2,BigDecimal.ROUND_HALF_UP));//金额 = 数量 * 单价 (保留两位小数)

                    orderCopeReceivable.setCid(templateCopeReceivableVO.getCid());//币种(currency_info id)
                    orderCopeReceivable.setRemarks(remarks);//描述
                    //应收 内陆费
                    orderCopeReceivables.add(orderCopeReceivable);
                    break;
                }
            }

        }


        //(3)其他应收费用，过滤掉 海运费、内陆费
        //商品的附加费 查询商品，服务费用单价，同一个商品不同的服务，有不同的费用
        List<OrderShopVO> orderShopVOList1 = orderShopMapper.findOrderShopByOrderId(orderInfo.getId());
        //单价排序，最大的排第一
        Collections.sort(orderShopVOList1, new Comparator<OrderShopVO>() {
            @Override
            public int compare(OrderShopVO o1, OrderShopVO o2) {
                return o2.getUnitPrice().compareTo(o1.getUnitPrice());
            }
        });
        //报价对应应收费用明细list
        QueryWrapper<TemplateCopeReceivable> query1 = new QueryWrapper<>();
        query1.eq("qie", qie);
        query1.notIn("cost_code", Arrays.asList("JYD-REC-COS-00001", "JYD-REC-COS-00002"));
        List<TemplateCopeReceivable> otherTemplateCopeReceivables = templateCopeReceivableMapper.selectList(query1);
        List<TemplateCopeReceivableVO> otherTemplateCopeReceivableVOList =
                ConvertUtil.convertList(otherTemplateCopeReceivables, TemplateCopeReceivableVO.class);
        for(int i=0; i<otherTemplateCopeReceivableVOList.size(); i++){
            TemplateCopeReceivableVO templateCopeReceivableVO = otherTemplateCopeReceivableVOList.get(i);

            OrderCopeReceivable orderCopeReceivable = new OrderCopeReceivable();
            orderCopeReceivable.setOrderId(orderId);//订单ID(order_info id)
            orderCopeReceivable.setCostCode(templateCopeReceivableVO.getCostCode());//费用代码(cost_item cost_code)
            orderCopeReceivable.setCostName(templateCopeReceivableVO.getCostName());//费用名称(cost_item cost_name)

            //JYD-REC-COS-00004     附加费
            String costCode = templateCopeReceivableVO.getCostCode();
            String costName = templateCopeReceivableVO.getCostName();
            if(costCode.equals("JYD-REC-COS-00004") && costName.equals("附加费")){
                //附加费
                BigDecimal unitPrice = orderShopVOList1.get(0).getUnitPrice();//取最大的商品单价
                unitPrice = (unitPrice == null) ? new BigDecimal("0") : unitPrice;

                orderCopeReceivable.setCalculateWay(templateCopeReceivableVO.getCalculateWay());//计算方式(1自动 2手动)
                orderCopeReceivable.setCount(totalChargeWeight);//数量 --> 数量，取订单箱号的 收费重
                orderCopeReceivable.setUnitPrice(unitPrice);//单价
                orderCopeReceivable.setAmount(totalChargeWeight.multiply(unitPrice).setScale(2,BigDecimal.ROUND_HALF_UP));//金额 = 数量 * 单价 (保留两位小数)

            }else{
                //其他费用
                orderCopeReceivable.setCalculateWay(templateCopeReceivableVO.getCalculateWay());//计算方式(1自动 2手动)
                orderCopeReceivable.setCount(totalChargeWeight);//数量 --> 数量，取订单箱号的 收费重
                orderCopeReceivable.setUnitPrice(templateCopeReceivableVO.getUnitPrice());//单价
                orderCopeReceivable.setAmount(totalChargeWeight.multiply(templateCopeReceivableVO.getUnitPrice()).setScale(2,BigDecimal.ROUND_HALF_UP));//金额 = 数量 * 单价 (保留两位小数)
            }

            orderCopeReceivable.setCid(templateCopeReceivableVO.getCid());//币种(currency_info id)
            orderCopeReceivable.setRemarks(templateCopeReceivableVO.getRemarks());//描述
            //应收 其他费用
            orderCopeReceivables.add(orderCopeReceivable);
        }

        if(CollUtil.isNotEmpty(orderCopeReceivables)){
            QueryWrapper<OrderCopeReceivable> OrderCopeReceivableQueryWrapper = new QueryWrapper<>();
            OrderCopeReceivableQueryWrapper.eq("order_id", orderInfo.getId());
            orderCopeReceivableService.remove(OrderCopeReceivableQueryWrapper);//先删除
            orderCopeReceivableService.saveOrUpdateBatch(orderCopeReceivables);//在保存  订单应收费用
        }

        //订单 应付 费用
        List<OrderCopeWith> orderCopeWiths = new ArrayList<>();
        //报价对应应付费用明细list
        QueryWrapper<TemplateCopeWith> query2 = new QueryWrapper<>();
        query2.eq("qie", qie);
        List<TemplateCopeWith> templateCopeWiths = templateCopeWithMapper.selectList(query2);
        List<TemplateCopeWithVO> templateCopeWithVOList =
                ConvertUtil.convertList(templateCopeWiths, TemplateCopeWithVO.class);
        for (int i=0; i<templateCopeWithVOList.size(); i++){
            TemplateCopeWithVO templateCopeWithVO = templateCopeWithVOList.get(i);
            OrderCopeWith orderCopeWith = new OrderCopeWith();

            orderCopeWith.setOrderId(orderId);//订单ID(order_info id)
            orderCopeWith.setCostCode(templateCopeWithVO.getCostCode());//费用代码(cost_item cost_code)
            orderCopeWith.setCostName(templateCopeWithVO.getCostName());//费用名称(cost_item cost_name)
            orderCopeWith.setSupplierId(templateCopeWithVO.getSupplierId());//供应商id(supplier_info id)

            orderCopeWith.setCalculateWay(templateCopeWithVO.getCalculateWay());//计算方式(1自动 2手动)
            orderCopeWith.setCount(totalChargeWeight);//数量 --> 海运费数量，取订单箱号的 收费重
            orderCopeWith.setUnitPrice(templateCopeWithVO.getUnitPrice());//单价
            orderCopeWith.setAmount(totalChargeWeight.multiply(templateCopeWithVO.getUnitPrice()).setScale(2,BigDecimal.ROUND_HALF_UP));//金额 = 数量 * 单价 (保留两位小数)

            orderCopeWith.setCid(templateCopeWithVO.getCid());//币种(currency_info id)
            orderCopeWith.setRemarks(templateCopeWithVO.getRemarks());//描述
            //应付 费用
            orderCopeWiths.add(orderCopeWith);
        }

        if(CollUtil.isNotEmpty(orderCopeWiths)){
            QueryWrapper<OrderCopeWith> orderCopeWithQueryWrapper = new QueryWrapper<>();
            orderCopeWithQueryWrapper.eq("order_id", orderInfo.getId());
            orderCopeWithService.remove(orderCopeWithQueryWrapper);//先删除
            orderCopeWithService.saveOrUpdateBatch(orderCopeWiths);//在保存  订单应付费用
        }

        return CommonResult.success(ConvertUtil.convert(orderInfo, OrderInfoVO.class));
    }

    private CaseVO structuralOrderCase(List<OrderCaseVO> orderCaseVOList, Integer offerInfoId) {
        OfferInfoVO offerInfoVO = offerInfoMapper.lookOfferInfoFare(Long.valueOf(offerInfoId));
        if(ObjectUtil.isEmpty(offerInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "报价不存在");
        }

        //计泡系数(默认6000)
        BigDecimal bubbleCoefficient = (offerInfoVO.getBubbleCoefficient() == null) ? new BigDecimal("6000") : offerInfoVO.getBubbleCoefficient();
        //最小重量(默认12) 最小收费重
        BigDecimal minimumQuantity = (offerInfoVO.getMinimumQuantity() == null) ? new BigDecimal("12") : offerInfoVO.getMinimumQuantity();
        //计费重单位(1柜 2KG 3CBM 4车)
        Integer billingWeightUnit = offerInfoVO.getBillingWeightUnit();
        //计算公式 1材积->重量：长*高*宽/计泡系数(单位KG)  2重量->材积：实重/计泡系数(单位CBM)
        Integer designFormulas = offerInfoVO.getDesignFormulas() == null ? 1 : offerInfoVO.getDesignFormulas();

        for (int i=0; i<orderCaseVOList.size(); i++){
            OrderCaseVO orderCaseVO = orderCaseVOList.get(i);
            BigDecimal weight = orderCaseVO.getAsnWeight();//重  实际重量
            BigDecimal length = orderCaseVO.getAsnLength();//长
            BigDecimal width = orderCaseVO.getAsnWidth();//宽
            BigDecimal height = orderCaseVO.getAsnHeight();//高

            //体积(m3) = (长cm * 宽cm * 高cm) / 1000000
            BigDecimal volume = length.multiply(width).multiply(height).divide(new BigDecimal("1000000"),3, BigDecimal.ROUND_HALF_UP);
            //材积重 = (长cm * 宽cm * 高cm) / 计泡系数
            BigDecimal volumeWeight = length.multiply(width).multiply(height).divide(bubbleCoefficient,3, BigDecimal.ROUND_HALF_UP);
            //收费重 ，比较实际重和材积重的大小，谁大取谁 chargeWeight
            BigDecimal chargeWeight = new BigDecimal("0");
            //计算值
            BigDecimal calcValue = new BigDecimal("0");
            if(designFormulas.equals(1)){
                //1材积->重量：长cm*高cm*宽cm/计泡系数(单位KG)   以重量KG为计费重单位，计算数据
                BigDecimal calcWeight = length.multiply(width).multiply(height).divide(bubbleCoefficient, 2, BigDecimal.ROUND_HALF_UP); // KG 重量，也就是千克
                calcValue = calcWeight;

                //比较重量KG，谁大取谁，作为计费重
                if(weight.compareTo(calcValue) == 1){
                    //weight > calcValue
                    chargeWeight = weight;
                }else{
                    chargeWeight = calcValue;
                }
                if(chargeWeight.compareTo(minimumQuantity) < 0){
                    // chargeWeight  < minimumQuantity   收费重  < 最小收费重
                    chargeWeight = minimumQuantity;
                }
            }else if(designFormulas.equals(2)){
                //2重量->材积：实重/计泡系数(单位CBM)      以材积CBM为计费单位，计算数据
                BigDecimal calcVolume = weight.divide(bubbleCoefficient,3, BigDecimal.ROUND_HALF_UP); // CBM 体积，也就是方
                calcValue = calcVolume;

                //比较体积CBM，谁大取谁，作为计费重
                if(volume.compareTo(calcValue) == 1){
                    //volume > calcValue
                    chargeWeight = volume;
                }else{
                    chargeWeight = calcValue;
                }
                BigDecimal minimumVolume = minimumQuantity.divide(bubbleCoefficient,3, BigDecimal.ROUND_HALF_UP); // CBM 体积，也就是方
                if(chargeWeight.compareTo(minimumVolume) < 0){
                    // chargeWeight  < minimumQuantity   收费重  < 最小收费重
                    chargeWeight = minimumQuantity;
                }
            }
            orderCaseVO.setAsnVolume(volume);//体积
            orderCaseVO.setVolumeWeight(volumeWeight);//材积重
            orderCaseVO.setChargeWeight(chargeWeight);//收费重
        }

        CaseVO caseVO = new CaseVO();
        //(客户预报)总重量 实际重
        BigDecimal totalAsnWeight = new BigDecimal("0");
        //客户预报总的材积重 材积重
        BigDecimal totalVolumeWeight = new BigDecimal("0");
        //客户预报总的收费重 收费重
        BigDecimal totalChargeWeight = new BigDecimal("0");
        //(客户预报)总体积
        BigDecimal totalAsnVolume = new BigDecimal("0");

        for (int i = 0; i<orderCaseVOList.size(); i++){
            BigDecimal asnWeight = orderCaseVOList.get(i).getAsnWeight();
            BigDecimal volumeWeight = orderCaseVOList.get(i).getVolumeWeight();
            BigDecimal chargeWeight = orderCaseVOList.get(i).getChargeWeight();
            BigDecimal asnVolume = orderCaseVOList.get(i).getAsnVolume();
            totalAsnWeight = totalAsnWeight.add(asnWeight);
            totalVolumeWeight = totalVolumeWeight.add(volumeWeight);
            totalChargeWeight = totalChargeWeight.add(chargeWeight);
            totalAsnVolume = totalAsnVolume.add(asnVolume);
        }
        caseVO.setTotalAsnWeight(totalAsnWeight);//实际重
        caseVO.setTotalVolumeWeight(totalVolumeWeight);//材积重
        caseVO.setTotalChargeWeight(totalChargeWeight);//收费重
        caseVO.setTotalAsnVolume(totalAsnVolume);//实际体积
        caseVO.setTotalCase(orderCaseVOList.size());//总箱数
        caseVO.setOrderCaseVOList(orderCaseVOList);

        return caseVO;
    }



    /**
     * 获取-订单对应报关文件-s
     * @param orderInfo
     * @param offerInfoId
     * @return
     */
    @Override
    public List<OrderCustomsFile> getOrderCustomsFiles(OrderInfo orderInfo, Integer offerInfoId) {
        /*
        0 否 对应 买关，
        1 是 对应 独立。

        @ApiModelProperty(value = "文件分组代码" +
        "A,报关服务-买单报关" +
        "B,报关服务-独立报关" +
        "C,清关服务-买单报关" +
        "D,清关服务-独立报关", position = 2)
        */
        Integer needDeclare = orderInfo.getNeedDeclare();//是否需要报关0-否，1-是 (订单对应报关文件:order_customs_file)
        List<OrderCustomsFile> orderCustomsFileList = new ArrayList<>();
        List<TemplateFileVO> templateFileByOrder = new ArrayList<>();
        if(needDeclare == 0){
            TemplateFileOrderForm fileOrderForm = new TemplateFileOrderForm();
            fileOrderForm.setOfferInfoId(offerInfoId);
            fileOrderForm.setTypes("1");//类型(1报关服务 2清关服务)
            fileOrderForm.setGroupCode("1");//文件分组代码(1买单 2独立)

            templateFileByOrder = templateFileService.findTemplateFileByOrder(fileOrderForm);
        }else if(needDeclare == 1){
            TemplateFileOrderForm fileOrderForm = new TemplateFileOrderForm();
            fileOrderForm.setOfferInfoId(offerInfoId);
            fileOrderForm.setTypes("1");//类型(1报关服务 2清关服务)
            fileOrderForm.setGroupCode("2");//文件分组代码(1买单 2独立)
            templateFileByOrder = templateFileService.findTemplateFileByOrder(fileOrderForm);
        }
        if(templateFileByOrder.size() > 0){
            templateFileByOrder.forEach(templateFileVO -> {
                OrderCustomsFile orderCustomsFile = new OrderCustomsFile();
                orderCustomsFile.setIdCode(templateFileVO.getFileCode());
                orderCustomsFile.setFileName(templateFileVO.getFileName());
                orderCustomsFile.setOptions(templateFileVO.getOptions());
                orderCustomsFile.setIsCheck(templateFileVO.getIsCheck());
                orderCustomsFile.setTemplateUrl(templateFileVO.getTemplateUrl());
                orderCustomsFile.setDescribe(templateFileVO.getRemarks());
                orderCustomsFile.setAuditStatus(0);//审核状态(0审核不通过  1审核通过)
                orderCustomsFileList.add(orderCustomsFile);
            });
        }
        return orderCustomsFileList;
    }

    /**
     * 获取-订单对应清关文件-s
     * @param orderInfo
     * @param offerInfoId
     * @return
     */
    @Override
    public List<OrderClearanceFile> getOrderClearanceFiles(OrderInfo orderInfo, Integer offerInfoId) {
        /*
        0 否 对应 买关，
        1 是 对应 独立。

        @ApiModelProperty(value = "文件分组代码" +
        "A,报关服务-买单报关" +
        "B,报关服务-独立报关" +
        "C,清关服务-买单报关" +
        "D,清关服务-独立报关", position = 2)
        */
        Integer needClearance = orderInfo.getNeedClearance();//是否需要清关0-否，1-是 (订单对应清关文件:order_clearance_file)
        List<OrderClearanceFile> orderClearanceFileList = new ArrayList<>();
        List<TemplateFileVO> templateFileByOrder = new ArrayList<>();
        if(needClearance == 0){
            TemplateFileOrderForm fileOrderForm = new TemplateFileOrderForm();
            fileOrderForm.setOfferInfoId(offerInfoId);
            fileOrderForm.setTypes("2");//类型(1报关服务 2清关服务)
            fileOrderForm.setGroupCode("1");//文件分组代码(1买单 2独立)

            templateFileByOrder = templateFileService.findTemplateFileByOrder(fileOrderForm);
        }else if(needClearance == 1){
            TemplateFileOrderForm fileOrderForm = new TemplateFileOrderForm();
            fileOrderForm.setOfferInfoId(offerInfoId);
            fileOrderForm.setTypes("2");//类型(1报关服务 2清关服务)
            fileOrderForm.setGroupCode("2");//文件分组代码(1买单 2独立)
            templateFileByOrder = templateFileService.findTemplateFileByOrder(fileOrderForm);
        }
        if(templateFileByOrder.size() > 0){
            templateFileByOrder.forEach(templateFileVO -> {
                OrderClearanceFile orderClearanceFile = new OrderClearanceFile();
                orderClearanceFile.setIdCode(templateFileVO.getFileCode());
                orderClearanceFile.setFileName(templateFileVO.getFileName());
                orderClearanceFile.setOptions(templateFileVO.getOptions());
                orderClearanceFile.setIsCheck(templateFileVO.getIsCheck());
                orderClearanceFile.setTemplateUrl(templateFileVO.getTemplateUrl());
                orderClearanceFile.setDescribe(templateFileVO.getRemarks());
                orderClearanceFile.setAuditStatus(0);//审核状态(0审核不通过  1审核通过)
                orderClearanceFileList.add(orderClearanceFile);
            });
        }
        return orderClearanceFileList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<OrderInfoVO> submitOrderInfo(OrderInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        if(ObjectUtil.isEmpty(customerUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录。");
        }
        //保存-产品订单表：order_info
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);
        Integer offerInfoId = orderInfo.getOfferInfoId();//报价id，运价id

        OfferInfoVO offerInfoVO = offerInfoMapper.lookOfferInfoFare(Long.valueOf(offerInfoId));
        Integer clearingWay = offerInfoVO.getClearingWay();//结算方式(1票结 2按客户的结算方式(客户表customer clearing_way))
        if(ObjectUtil.isNotEmpty(clearingWay)){
            if(clearingWay.equals("2")){
                Integer customerId = customerUser.getId();
                CustomerVO customerVO = customerMapper.findCustomerById(customerId);
                Integer clearingWay1 = customerVO.getClearingWay();//结算方式(1票结 2月结)
                clearingWay = clearingWay1;
            }
        }else{
            clearingWay = 1;
        }
        orderInfo.setClearingWay(clearingWay);//订单的结算方式  结算方式(1票结 2月结)

        //集货仓库代码 -> 集货仓库名称
        String storeGoodsWarehouseCode1 = orderInfo.getStoreGoodsWarehouseCode();
        ShippingAreaVO shippingAreaVO = shippingAreaMapper.findShippingAreaByWarehouseCode(storeGoodsWarehouseCode1);
        String storeGoodsWarehouseName = shippingAreaVO.getWarehouseName();
        orderInfo.setStoreGoodsWarehouseName(storeGoodsWarehouseName);
        //目的仓库代码 -> 目的仓库名称
        String destinationWarehouseCode = orderInfo.getDestinationWarehouseCode();
        FabWarehouseVO fabWarehouseVO = fabWarehouseMapper.findFabWarehouseByWarehouseCode(destinationWarehouseCode);
        String destinationWarehouseName = fabWarehouseVO.getWarehouseName();
        orderInfo.setDestinationWarehouseName(destinationWarehouseName);

        //判断订单号是否存在，不存在则新增
        String orderNo = form.getOrderNo();
        if(orderNo == null || orderNo == ""){
            orderNo = String.valueOf(SnowflakeUtils.getOrderNo());//雪花算法生成订单id
            orderInfo.setOrderNo(orderNo);
            orderInfo.setCustomerId(customerUser.getId());
            orderInfo.setCreateTime(LocalDateTime.now());
            orderInfo.setCreateUserId(customerUser.getId());
            orderInfo.setCreateUserName(customerUser.getUserName());
        }

        //已下单 状态码
        orderInfo.setFrontStatusCode(OrderEnum.FRONT_PLACED.getCode());
        orderInfo.setFrontStatusName(OrderEnum.FRONT_PLACED.getName());
        orderInfo.setAfterStatusCode(OrderEnum.AFTER_PLACED.getCode());
        orderInfo.setAfterStatusName(OrderEnum.AFTER_PLACED.getName());

        //订单对应箱号信息:order_case
        List<OrderCaseVO> orderCaseVOList = form.getOrderCaseVOList();
        CaseVO caseVO = structuralOrderCase(orderCaseVOList, offerInfoId);

        BigDecimal totalChargeWeight = caseVO.getTotalChargeWeight();//客户预报的总收费重 收费重
        BigDecimal totalVolumeWeight = caseVO.getTotalVolumeWeight();//材积重
        BigDecimal totalAsnWeight = caseVO.getTotalAsnWeight();//实际重
        BigDecimal totalAsnVolume = caseVO.getTotalAsnVolume();//实际体积
        Integer totalCase = caseVO.getTotalCase();//总箱数

        orderInfo.setChargeWeight(totalChargeWeight);
        orderInfo.setVolumeWeight(totalVolumeWeight);
        orderInfo.setActualWeight(totalAsnWeight);
        orderInfo.setActualVolume(totalAsnVolume);
        orderInfo.setTotalCartons(totalCase);

        Integer isPick1 = orderInfo.getIsPick();//是否上门提货(0否 1是,order_pick)
        if(isPick1.equals(0)){
            String warehouseNo1 = orderInfo.getWarehouseNo();
            if(ObjectUtil.isEmpty(warehouseNo1)){
                String warehouseNo = NumberGeneratedUtils.getWarehouseNo();
                orderInfo.setWarehouseNo(warehouseNo);//订单提交时，判断是否生成进仓单号
            }
        }
        this.saveOrUpdate(orderInfo);

        //保存-订单对应箱号信息:order_case
//        List<OrderCaseVO> orderCaseVOList = form.getOrderCaseVOList();
        List<OrderCase> orderCaseList;
        orderCaseList = ConvertUtil.convertList(orderCaseVOList, OrderCase.class);
        orderCaseList.forEach(orderCase -> {
            orderCase.setOrderId(orderInfo.getId().intValue());
        });
        QueryWrapper<OrderCase> orderCaseQueryWrapper = new QueryWrapper<>();
        orderCaseQueryWrapper.eq("order_id", orderInfo.getId());
        orderCaseService.remove(orderCaseQueryWrapper);
        orderCaseService.saveOrUpdateBatch(orderCaseList);

        //保存-订单对应商品：order_shop
        List<OrderShopVO> orderShopVOList = form.getOrderShopVOList();
        List<OrderShop> orderShopList = ConvertUtil.convertList(orderShopVOList, OrderShop.class);
        orderShopList.forEach(orderShop -> {
            orderShop.setOrderId(orderInfo.getId().intValue());
            orderShop.setCreateTime(LocalDateTime.now());
        });
        QueryWrapper<OrderShop> orderShopQueryWrapper = new QueryWrapper<>();
        orderShopQueryWrapper.eq("order_id", orderInfo.getId());
        orderShopService.remove(orderShopQueryWrapper);
        orderShopService.saveOrUpdateBatch(orderShopList);

        //保存-订单对应提货信息表：order_pick
        List<OrderPickVO> orderPickVOList = form.getOrderPickVOList();
        if(CollUtil.isNotEmpty(orderPickVOList)){

            //to 集货仓库
            String to_country = shippingAreaVO.getCountryName();
            String to_province = shippingAreaVO.getStateName();
            String to_city = shippingAreaVO.getCityName();
            String to_region = shippingAreaVO.getRegionName();


            List<OrderPick> orderPickList = ConvertUtil.convertList(orderPickVOList, OrderPick.class);
            orderPickList.forEach(orderPick -> {


                //form 提货地址
                //  `address_id` int(11) DEFAULT NULL COMMENT '提货地址id(delivery_address id)',
                Integer addressId = orderPick.getAddressId();

                DeliveryAddressVO deliveryAddressVO = deliveryAddressMapper.findDeliveryAddressById(addressId);
                String from_country = deliveryAddressVO.getCountryName();
                String from_province = deliveryAddressVO.getStateName();
                String from_city = deliveryAddressVO.getCityName();
                String from_region = deliveryAddressVO.getRegionName();
                Integer unit = orderPick.getUnit();//单位(1公斤 2方 3票 4柜)    前端填写选择参数

                BigDecimal weight = orderPick.getWeight();//重量  重量直接作为 数量，进行计费

                orderPick.setOrderId(orderInfo.getId());
                orderPick.setFromCountry(from_country);
                orderPick.setFromProvince(from_province);
                orderPick.setFromCity(from_city);
                orderPick.setFromRegion(from_region);
                orderPick.setToCountry(to_country);
                orderPick.setToProvince(to_province);
                orderPick.setToCity(to_city);
                orderPick.setToRegion(to_region);
                orderPick.setCount(weight);

                Map<String, Object> paraMap = new HashMap<>();
                paraMap.put("from_country", from_country);
                paraMap.put("from_province", from_province);
                paraMap.put("from_city", from_city);
                paraMap.put("from_region", from_region);
                paraMap.put("to_country", to_country);
                paraMap.put("to_province", to_province);
                paraMap.put("to_city", to_city);
                paraMap.put("to_region", to_region);
                paraMap.put("unit", unit);
                InlandFeeCostVO inlandFeeCostVO = inlandFeeCostMapper.findInlandFeeCostByPara(paraMap);

                if(ObjectUtil.isNotEmpty(inlandFeeCostVO)){
                    BigDecimal unitPrice = inlandFeeCostVO.getUnitPrice();
                    Integer cid = inlandFeeCostVO.getCid();
                    orderPick.setUnitPrice(unitPrice);
                    orderPick.setCid(cid);
                    orderPick.setStatus("1");//1有效 费用有效
                    orderPick.setFeeRemark("内陆费，路线匹配，正常计算");
                }else{
                    orderPick.setUnitPrice(new BigDecimal("0"));
                    orderPick.setCid(1);
                    orderPick.setStatus("0");//0无效 费用待定
                    orderPick.setFeeRemark("内陆费，路线不存在，费用待定");
                }

                //金额 = 单价 * 数量
                BigDecimal amount = orderPick.getUnitPrice().multiply(orderPick.getCount());
                orderPick.setAmount(amount);

            });
            QueryWrapper<OrderPick> orderPickQueryWrapper = new QueryWrapper<>();
            orderPickQueryWrapper.eq("order_id", orderInfo.getId());
            orderPickService.remove(orderPickQueryWrapper);
            orderPickService.saveOrUpdateBatch(orderPickList);
        }

        //订单对应报关文件 order_customs_file
        //`need_declare` int(11) DEFAULT NULL COMMENT '是否需要报关0-否，1-是 (订单对应报关文件:order_customs_file)',
        List<OrderCustomsFile> orderCustomsFileList = getOrderCustomsFiles(orderInfo, offerInfoId);
        orderCustomsFileList.forEach(orderCustomsFile -> {
            orderCustomsFile.setOrderId(orderInfo.getId().intValue());
        });
        QueryWrapper<OrderCustomsFile> orderCustomsFileQueryWrapper = new QueryWrapper<>();
        orderCustomsFileQueryWrapper.eq("order_id", orderInfo.getId());
        orderCustomsFileService.remove(orderCustomsFileQueryWrapper);
        orderCustomsFileService.saveOrUpdateBatch(orderCustomsFileList);

        //订单对应清关文件 order_clearance_file
        //`need_clearance` int(11) DEFAULT NULL COMMENT '是否需要清关0-否，1-是 (订单对应清关文件:order_clearance_file)',
        List<OrderClearanceFile> orderClearanceFileList = getOrderClearanceFiles(orderInfo, offerInfoId);
        orderClearanceFileList.forEach(orderClearanceFile -> {
            orderClearanceFile.setOrderId(orderInfo.getId().intValue());
        });
        QueryWrapper<OrderClearanceFile> orderClearanceFileQueryWrapper = new QueryWrapper<>();
        orderClearanceFileQueryWrapper.eq("order_id", orderInfo.getId());
        orderClearanceFileService.remove(orderClearanceFileQueryWrapper);
        orderClearanceFileService.saveOrUpdateBatch(orderClearanceFileList);

        //根据运价(报价)，找报价模板，报价模板对应应收应付费用信息
        OfferInfo offerInfo = offerInfoMapper.selectById(offerInfoId);
        Integer qie = offerInfo.getQie();//报价模板id(quotation_template id)
        Long orderId = orderInfo.getId();//订单id

        //TODO 订单 对应的 费用
        //订单 应收 费用
        List<OrderCopeReceivable> orderCopeReceivables = new ArrayList<>();
        //(1)订柜尺寸 对应 应收`海运费`
        String reserveSize = form.getReserveSize();
        /*订柜尺寸：海运费规格*/
        List<TemplateCopeReceivableVO> oceanFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableOceanFeeByQie(qie);
        for (int i = 0; i<oceanFeeList.size(); i++){
            TemplateCopeReceivableVO templateCopeReceivableVO = oceanFeeList.get(i);
            String specificationCode = templateCopeReceivableVO.getSpecificationCode();
            //查看订舱区间，判断费用代码是否相等，相等则列入海运费 费用明细
            if(specificationCode.equals(reserveSize)){

                BigDecimal min = templateCopeReceivableVO.getMin() == null ? new BigDecimal("0") : templateCopeReceivableVO.getMin();
                BigDecimal max = templateCopeReceivableVO.getMax() == null ? new BigDecimal("0") : templateCopeReceivableVO.getMax();
                totalChargeWeight = caseVO.getTotalChargeWeight();//客户预报的总收费重 收费重
//                if(totalChargeWeight.compareTo(min) == -1 || totalChargeWeight.compareTo(max) == 1){
//                    Asserts.fail(ResultEnum.UNKNOWN_ERROR, "收费重超出或小于，订舱区间的所对应的数量范围");
//                }

                OrderCopeReceivable orderCopeReceivable = new OrderCopeReceivable();
                orderCopeReceivable.setOrderId(orderId);//订单ID(order_info id)
                orderCopeReceivable.setCostCode(templateCopeReceivableVO.getCostCode());//费用代码(cost_item cost_code)
                orderCopeReceivable.setCostName(templateCopeReceivableVO.getCostName());//费用名称(cost_item cost_name)

                orderCopeReceivable.setCalculateWay(templateCopeReceivableVO.getCalculateWay());//计算方式(1自动 2手动)
                orderCopeReceivable.setCount(totalChargeWeight);//数量 --> 海运费数量，取订单箱号的 收费重
                orderCopeReceivable.setUnitPrice(templateCopeReceivableVO.getUnitPrice());//单价
                orderCopeReceivable.setAmount(totalChargeWeight.multiply(templateCopeReceivableVO.getUnitPrice()).setScale(2,BigDecimal.ROUND_HALF_UP));//金额 = 数量 * 单价 (保留两位小数)

                orderCopeReceivable.setCid(templateCopeReceivableVO.getCid());//币种(currency_info id)
                orderCopeReceivable.setRemarks(templateCopeReceivableVO.getRemarks());//描述
                //应收 海运费
                orderCopeReceivables.add(orderCopeReceivable);
                break;
            }
        }
        //(2)集货仓库 对应 应收`内陆费`
        String storeGoodsWarehouseCode = form.getStoreGoodsWarehouseCode();
        /*集货仓库：陆运费规格*/
        List<TemplateCopeReceivableVO> inlandFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableInlandFeeListByQie(qie);

        //是否上门提货 (0否 1是,order_pick) 为是，才计算内陆费，否则不计算内陆费
        Integer isPick = orderInfo.getIsPick();
        if(isPick == 1){
            //汇总，提货地址，的内陆费   汇总的金额，为单价；数量固定为1   (金额=单价*1)
            List<OrderPickVO> orderPickList = orderPickMapper.findOrderPickByOrderId(orderInfo.getId());
            String remarks = "内陆费确认";
            BigDecimal amount = new BigDecimal("0");
            for(int i=0; i<orderPickList.size(); i++){
                OrderPickVO orderPickVO = orderPickList.get(i);
                String status = orderPickVO.getStatus();
                if(status.equals("0")){
                    //内陆费为待定
                    remarks = "内陆费待定";
                    break;
                }
                amount = amount.add(orderPickVO.getAmount());
            }

            for (int i=0; i<inlandFeeList.size(); i++){
                TemplateCopeReceivableVO templateCopeReceivableVO = inlandFeeList.get(i);
                String specificationCode = templateCopeReceivableVO.getSpecificationCode();
                //查看集货仓库，判断费用代码是否相等，相等则列入陆运费 费用明细
                if(specificationCode.equals(storeGoodsWarehouseCode)){
                    OrderCopeReceivable orderCopeReceivable = new OrderCopeReceivable();
                    orderCopeReceivable.setOrderId(orderId);//订单ID(order_info id)
                    orderCopeReceivable.setCostCode(templateCopeReceivableVO.getCostCode());//费用代码(cost_item cost_code)
                    orderCopeReceivable.setCostName(templateCopeReceivableVO.getCostName());//费用名称(cost_item cost_name)

                    orderCopeReceivable.setCalculateWay(templateCopeReceivableVO.getCalculateWay());//计算方式(1自动 2手动)
                    orderCopeReceivable.setCount(new BigDecimal("1"));//数量  内陆费，数量固定为1，实际上是提货地址的内陆费
                    orderCopeReceivable.setUnitPrice(amount.setScale(2,BigDecimal.ROUND_HALF_UP));//单价               金额，直接作为单价
                    orderCopeReceivable.setAmount(amount.setScale(2,BigDecimal.ROUND_HALF_UP));//金额 = 数量 * 单价 (保留两位小数)

                    orderCopeReceivable.setCid(templateCopeReceivableVO.getCid());//币种(currency_info id)
                    orderCopeReceivable.setRemarks(remarks);//描述
                    //应收 内陆费
                    orderCopeReceivables.add(orderCopeReceivable);
                    break;
                }
            }
        }

        //(3)其他应收费用，过滤掉 海运费、内陆费
        //商品的附加费 查询商品，服务费用单价，同一个商品不同的服务，有不同的费用
        List<OrderShopVO> orderShopVOList1 = orderShopMapper.findOrderShopByOrderId(orderInfo.getId());
        //单价排序，最大的排第一
        Collections.sort(orderShopVOList1, new Comparator<OrderShopVO>() {
            @Override
            public int compare(OrderShopVO o1, OrderShopVO o2) {
                return o2.getUnitPrice().compareTo(o1.getUnitPrice());
            }
        });
        //报价对应应收费用明细list
        QueryWrapper<TemplateCopeReceivable> query1 = new QueryWrapper<>();
        query1.eq("qie", qie);
        query1.notIn("cost_code", Arrays.asList("JYD-REC-COS-00001", "JYD-REC-COS-00002"));
        List<TemplateCopeReceivable> otherTemplateCopeReceivables = templateCopeReceivableMapper.selectList(query1);
        List<TemplateCopeReceivableVO> otherTemplateCopeReceivableVOList =
                ConvertUtil.convertList(otherTemplateCopeReceivables, TemplateCopeReceivableVO.class);
        for(int i=0; i<otherTemplateCopeReceivableVOList.size(); i++){
            TemplateCopeReceivableVO templateCopeReceivableVO = otherTemplateCopeReceivableVOList.get(i);

            OrderCopeReceivable orderCopeReceivable = new OrderCopeReceivable();
            orderCopeReceivable.setOrderId(orderId);//订单ID(order_info id)
            orderCopeReceivable.setCostCode(templateCopeReceivableVO.getCostCode());//费用代码(cost_item cost_code)
            orderCopeReceivable.setCostName(templateCopeReceivableVO.getCostName());//费用名称(cost_item cost_name)

            //JYD-REC-COS-00004     附加费
            String costCode = templateCopeReceivableVO.getCostCode();
            String costName = templateCopeReceivableVO.getCostName();
            if(costCode.equals("JYD-REC-COS-00004") && costName.equals("附加费")){
                //附加费
                BigDecimal unitPrice = orderShopVOList1.get(0).getUnitPrice();//取最大的商品单价
                unitPrice = (unitPrice == null) ? new BigDecimal("0") : unitPrice;

                orderCopeReceivable.setCalculateWay(templateCopeReceivableVO.getCalculateWay());//计算方式(1自动 2手动)
                orderCopeReceivable.setCount(totalChargeWeight);//数量 --> 数量，取订单箱号的 收费重
                orderCopeReceivable.setUnitPrice(unitPrice);//单价
                orderCopeReceivable.setAmount(totalChargeWeight.multiply(unitPrice).setScale(2,BigDecimal.ROUND_HALF_UP));//金额 = 数量 * 单价 (保留两位小数)

            }else{
                //其他费用
                orderCopeReceivable.setCalculateWay(templateCopeReceivableVO.getCalculateWay());//计算方式(1自动 2手动)
                orderCopeReceivable.setCount(totalChargeWeight);//数量 --> 数量，取订单箱号的 收费重
                orderCopeReceivable.setUnitPrice(templateCopeReceivableVO.getUnitPrice());//单价
                orderCopeReceivable.setAmount(totalChargeWeight.multiply(templateCopeReceivableVO.getUnitPrice()).setScale(2,BigDecimal.ROUND_HALF_UP));//金额 = 数量 * 单价 (保留两位小数)
            }

            orderCopeReceivable.setCid(templateCopeReceivableVO.getCid());//币种(currency_info id)
            orderCopeReceivable.setRemarks(templateCopeReceivableVO.getRemarks());//描述
            //应收 其他费用
            orderCopeReceivables.add(orderCopeReceivable);
        }

        if(CollUtil.isNotEmpty(orderCopeReceivables)){
            QueryWrapper<OrderCopeReceivable> OrderCopeReceivableQueryWrapper = new QueryWrapper<>();
            OrderCopeReceivableQueryWrapper.eq("order_id", orderInfo.getId());
            orderCopeReceivableService.remove(OrderCopeReceivableQueryWrapper);//先删除
            orderCopeReceivableService.saveOrUpdateBatch(orderCopeReceivables);//在保存  订单应收费用
        }

        //订单 应付 费用
        List<OrderCopeWith> orderCopeWiths = new ArrayList<>();
        //报价对应应付费用明细list
        QueryWrapper<TemplateCopeWith> query2 = new QueryWrapper<>();
        query2.eq("qie", qie);
        List<TemplateCopeWith> templateCopeWiths = templateCopeWithMapper.selectList(query2);
        List<TemplateCopeWithVO> templateCopeWithVOList =
                ConvertUtil.convertList(templateCopeWiths, TemplateCopeWithVO.class);
        for (int i=0; i<templateCopeWithVOList.size(); i++){
            TemplateCopeWithVO templateCopeWithVO = templateCopeWithVOList.get(i);
            OrderCopeWith orderCopeWith = new OrderCopeWith();

            orderCopeWith.setOrderId(orderId);//订单ID(order_info id)
            orderCopeWith.setCostCode(templateCopeWithVO.getCostCode());//费用代码(cost_item cost_code)
            orderCopeWith.setCostName(templateCopeWithVO.getCostName());//费用名称(cost_item cost_name)
            orderCopeWith.setSupplierId(templateCopeWithVO.getSupplierId());//供应商id(supplier_info id)

            orderCopeWith.setCalculateWay(templateCopeWithVO.getCalculateWay());//计算方式(1自动 2手动)
            orderCopeWith.setCount(totalChargeWeight);//数量 --> 海运费数量，取订单箱号的 收费重
            orderCopeWith.setUnitPrice(templateCopeWithVO.getUnitPrice());//单价
            orderCopeWith.setAmount(totalChargeWeight.multiply(templateCopeWithVO.getUnitPrice()).setScale(2,BigDecimal.ROUND_HALF_UP));//金额 = 数量 * 单价 (保留两位小数)

            orderCopeWith.setCid(templateCopeWithVO.getCid());//币种(currency_info id)
            orderCopeWith.setRemarks(templateCopeWithVO.getRemarks());//描述
            //应付 费用
            orderCopeWiths.add(orderCopeWith);
        }

        if(CollUtil.isNotEmpty(orderCopeWiths)){
            QueryWrapper<OrderCopeWith> orderCopeWithQueryWrapper = new QueryWrapper<>();
            orderCopeWithQueryWrapper.eq("order_id", orderInfo.getId());
            orderCopeWithService.remove(orderCopeWithQueryWrapper);//先删除
            orderCopeWithService.saveOrUpdateBatch(orderCopeWiths);//在保存  订单应付费用
        }

        //TODO 提交订单时，创建订单任务
        List<WaybillTaskRelevanceVO> waybillTaskRelevanceVOS = waybillTaskRelevanceService.saveWaybillTaskRelevance(orderInfo);

        return CommonResult.success(ConvertUtil.convert(orderInfo, OrderInfoVO.class));

    }

    @Override
    public CommonResult<OrderInfoVO> draftCancelOrderInfo(OrderInfoForm form) {
        Long id = form.getId();
        OrderInfo orderInfo = this.getById(id);
        if(orderInfo.getFrontStatusCode().equals(OrderEnum.FRONT_DRAFT.getCode())){
            //已取消
            orderInfo.setFrontStatusCode(OrderEnum.FRONT_CANCEL.getCode());
            orderInfo.setFrontStatusName(OrderEnum.FRONT_CANCEL.getName());
            orderInfo.setAfterStatusCode(OrderEnum.AFTER_CANCEL.getCode());
            orderInfo.setAfterStatusName(OrderEnum.AFTER_CANCEL.getName());

            this.saveOrUpdate(orderInfo);
        }else{
            return CommonResult.error(-1, "订单状态不正确，不能取消订单");
        }
        OrderInfoVO orderInfoVO = ConvertUtil.convert(orderInfo, OrderInfoVO.class);
        return CommonResult.success(orderInfoVO);
    }

    @Override
    public CommonResult<OrderInfoVO> lookEditOrderInfo(OrderInfoForm form) {
        Long orderInfoId = form.getId();
        //订单信息
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfo(orderInfoId);
        if(orderInfoVO == null){
            return CommonResult.error(-1, "订单不存在");
        }
        //报价id
        Integer offerInfoId = orderInfoVO.getOfferInfoId();
        //报价模板id
        Integer qie = orderInfoVO.getQie();
        //是否上门提货 (0否 1是,order_pick)
        Integer isPick = orderInfoVO.getIsPick();

        //订柜尺寸[海运费]
        /*订柜尺寸：海运费规格*/
        List<TemplateCopeReceivableVO> oceanFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableOceanFeeByQie(qie);
        orderInfoVO.setOceanFeeList(oceanFeeList);

        //集货仓库[陆运费]
        /*集货仓库：陆运费规格*/
        List<TemplateCopeReceivableVO> inlandFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableInlandFeeListByQie(qie);
        orderInfoVO.setInlandFeeList(inlandFeeList);

        //目的仓库
        List<FabWarehouseVO> fabWarehouseVOList = fabWarehouseMapper.findFabWarehouseByqie(qie);
        orderInfoVO.setFabWarehouseVOList(fabWarehouseVOList);

        //关联的订单箱号
        /*订单对应箱号信息:order_case*/
        List<OrderCaseVO> orderCaseVOList = orderCaseMapper.findOrderCaseByOrderId(orderInfoId);
        orderInfoVO.setOrderCaseVOList(orderCaseVOList);
        CaseVO caseVO = getCaseVO(orderCaseVOList, orderInfoVO);
        orderInfoVO.setCaseVO(caseVO);

        //关联的订单商品
        /*订单对应商品：order_shop*/
        List<OrderShopVO> orderShopVOList = orderShopMapper.findOrderShopByOrderId(orderInfoId);
        orderInfoVO.setOrderShopVOList(orderShopVOList);

        //是否上门提货[是] -- 有提货地址
        //(0否 1是,order_pick)
        if(isPick == 1){
            //订单关联提货地址
            List<OrderPickVO> orderPickVOList = orderPickMapper.findOrderPickByOrderId(orderInfoId);
            orderInfoVO.setOrderPickVOList(orderPickVOList);
        }

        //TODO 其他费用 不做，删掉，放入`费用明细`
        //TODO 费用明细 仅展示 订单应收费用，订单应付费用 不要展示
        OrderCostDetailVO orderCostDetailVO = getOrderCostDetailVO(orderInfoVO);

        orderInfoVO.setOrderCostDetailVO(orderCostDetailVO);//订单费用明细

        String remarks = orderInfoVO.getRemarks();//操作说明
        if(ObjectUtil.isNotEmpty(remarks)){
            String[] split = remarks.split("\n");
            List<String> list = Arrays.asList(split);
            orderInfoVO.setRemarksList(list);
        }

        return CommonResult.success(orderInfoVO);
    }

    private CaseVO getCaseVO(List<OrderCaseVO> orderCaseVOList, OrderInfoVO orderInfoVO) {
        Integer offerInfoId = orderInfoVO.getOfferInfoId();
        if(ObjectUtil.isEmpty(offerInfoId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "报价id为空");
        }
        OfferInfoVO offerInfoVO = offerInfoMapper.lookOfferInfoFare(Long.valueOf(offerInfoId));
        if(ObjectUtil.isEmpty(offerInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "报价不存在");
        }
        //1.计算

        //计泡系数(默认6000)
        BigDecimal bubbleCoefficient = (offerInfoVO.getBubbleCoefficient() == null) ? new BigDecimal("6000") : offerInfoVO.getBubbleCoefficient();
        //最小重量(默认12) 最小收费重
        BigDecimal minimumQuantity = (offerInfoVO.getMinimumQuantity() == null) ? new BigDecimal("12") : offerInfoVO.getMinimumQuantity();
        //计费重单位(1柜 2KG 3CBM 4车)
        Integer billingWeightUnit = offerInfoVO.getBillingWeightUnit();
        //计算公式 1材积->重量：长*高*宽/计泡系数(单位KG)  2重量->材积：实重/计泡系数(单位CBM)
        Integer designFormulas = offerInfoVO.getDesignFormulas() == null ? 1 : offerInfoVO.getDesignFormulas();

        for (int i=0; i<orderCaseVOList.size(); i++){
            OrderCaseVO orderCaseVO = orderCaseVOList.get(i);
            BigDecimal weight = orderCaseVO.getAsnWeight();//重  实际重量
            BigDecimal length = orderCaseVO.getAsnLength();//长
            BigDecimal width = orderCaseVO.getAsnWidth();//宽
            BigDecimal height = orderCaseVO.getAsnHeight();//高

            //体积(m3) = (长cm * 宽cm * 高cm) / 1000000
            BigDecimal volume = length.multiply(width).multiply(height).divide(new BigDecimal("1000000"),3, BigDecimal.ROUND_HALF_UP);
            //材积重 = (长cm * 宽cm * 高cm) / 计泡系数
            BigDecimal volumeWeight = length.multiply(width).multiply(height).divide(bubbleCoefficient,3, BigDecimal.ROUND_HALF_UP);
            //收费重 ，比较实际重和材积重的大小，谁大取谁 chargeWeight
            BigDecimal chargeWeight = new BigDecimal("0");
            //计算值
            BigDecimal calcValue = new BigDecimal("0");
            if(designFormulas.equals(1)){
                //1材积->重量：长cm*高cm*宽cm/计泡系数(单位KG)   以重量KG为计费重单位，计算数据
                BigDecimal calcWeight = length.multiply(width).multiply(height).divide(bubbleCoefficient, 2, BigDecimal.ROUND_HALF_UP); // KG 重量，也就是千克
                calcValue = calcWeight;

                //比较重量KG，谁大取谁，作为计费重
                if(weight.compareTo(calcValue) == 1){
                    //weight > calcValue
                    chargeWeight = weight;
                }else{
                    chargeWeight = calcValue;
                }
                if(chargeWeight.compareTo(minimumQuantity) < 0){
                    // chargeWeight  < minimumQuantity   收费重  < 最小收费重
                    chargeWeight = minimumQuantity;
                }
            }else if(designFormulas.equals(2)){
                //2重量->材积：实重/计泡系数(单位CBM)      以材积CBM为计费单位，计算数据
                BigDecimal calcVolume = weight.divide(bubbleCoefficient,3, BigDecimal.ROUND_HALF_UP); // CBM 体积，也就是方
                calcValue = calcVolume;

                //比较体积CBM，谁大取谁，作为计费重
                if(volume.compareTo(calcValue) == 1){
                    //volume > calcValue
                    chargeWeight = volume;
                }else{
                    chargeWeight = calcValue;
                }
                BigDecimal minimumVolume = minimumQuantity.divide(bubbleCoefficient,3, BigDecimal.ROUND_HALF_UP); // CBM 体积，也就是方
                if(chargeWeight.compareTo(minimumVolume) < 0){
                    // chargeWeight  < minimumQuantity   收费重  < 最小收费重
                    chargeWeight = minimumQuantity;
                }
            }
            orderCaseVO.setAsnVolume(volume);//体积
            orderCaseVO.setVolumeWeight(volumeWeight);//材积重
            orderCaseVO.setChargeWeight(chargeWeight);//收费重
        }

        //汇总

        //订单箱号，修改展示数据
        CaseVO caseVO = new CaseVO();
        //(客户预报)总重量 实际重
        BigDecimal totalAsnWeight = new BigDecimal("0");
        //客户预报总的材积重 材积重
        BigDecimal totalVolumeWeight = new BigDecimal("0");
        //客户预报总的收费重 收费重
        BigDecimal totalChargeWeight = new BigDecimal("0");
        //(客户预报)总体积
        BigDecimal totalAsnVolume = new BigDecimal("0");

        for (int i = 0; i<orderCaseVOList.size(); i++){
            BigDecimal asnWeight = orderCaseVOList.get(i).getAsnWeight();
            BigDecimal volumeWeight = orderCaseVOList.get(i).getVolumeWeight();
            BigDecimal chargeWeight = orderCaseVOList.get(i).getChargeWeight();
            BigDecimal asnVolume = orderCaseVOList.get(i).getAsnVolume();
            totalAsnWeight = totalAsnWeight.add(asnWeight);
            totalVolumeWeight = totalVolumeWeight.add(volumeWeight);
            totalChargeWeight = totalChargeWeight.add(chargeWeight);
            totalAsnVolume = totalAsnVolume.add(asnVolume);
        }
        caseVO.setTotalAsnWeight(totalAsnWeight);
        caseVO.setTotalVolumeWeight(totalVolumeWeight);
        caseVO.setTotalChargeWeight(totalChargeWeight);
        caseVO.setTotalAsnVolume(totalAsnVolume);
        caseVO.setTotalCase(orderCaseVOList.size());
        caseVO.setOrderCaseVOList(orderCaseVOList);
        return caseVO;
    }

    @Override
    public CommonResult<OrderInfoVO> lookOrderInfo(OrderInfoForm form) {
        Long orderInfoId = form.getId();
        //订单信息
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfo(orderInfoId);
        if(orderInfoVO == null){
            return CommonResult.error(-1, "订单不存在");
        }
        Long orderId = orderInfoVO.getId();//订单id
        //订柜尺寸  对应 `海运费`
        String reserveSize = orderInfoVO.getReserveSize();//订柜尺寸
        QuotationType quotationType = quotationTypeMapper.findQuotationTypeByCode(reserveSize);
        orderInfoVO.setReserveSizeName(quotationType.getName());

        //报价模板id
        Integer qie = orderInfoVO.getQie();
        List<TemplateCopeReceivableVO> oceanFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableOceanFeeByQie(qie);

        List<TemplateCopeReceivableVO> reserveSizeTemplateCopeReceivables = new ArrayList<>();

        for (int i=0; i<oceanFeeList.size(); i++){
            TemplateCopeReceivableVO templateCopeReceivableVO = oceanFeeList.get(i);
            String specificationCode = templateCopeReceivableVO.getSpecificationCode();
            //查看订舱区间，判断费用代码是否相等，相等则列入海运费 费用明细
            if(specificationCode.equals(reserveSize)){
                reserveSizeTemplateCopeReceivables.add(templateCopeReceivableVO);
                break;
            }
        }
        orderInfoVO.setOceanFeeList(reserveSizeTemplateCopeReceivables);

        //物流轨迹
        List<LogisticsTrackVO> logisticsTrackVOS = logisticsTrackMapper.findLogisticsTrackByOrderId(orderInfoVO.getId().toString());
        orderInfoVO.setLogisticsTrackVOS(logisticsTrackVOS);

        //关联的订单箱号
        /*订单对应箱号信息:order_case*/
        List<OrderCaseVO> orderCaseVOList = orderCaseMapper.findOrderCaseByOrderId(orderInfoId);
        orderInfoVO.setOrderCaseVOList(orderCaseVOList);

        //关联的订单商品
        /*订单对应商品：order_shop*/
        List<OrderShopVO> orderShopVOList = orderShopMapper.findOrderShopByOrderId(orderInfoId);
        orderInfoVO.setOrderShopVOList(orderShopVOList);

        //订单报关文件
        //订单对应报关文件list
        QueryWrapper<OrderCustomsFile> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("order_id", orderId);
        List<OrderCustomsFile> orderCustomsFiles = orderCustomsFileMapper.selectList(queryWrapper1);
        List<OrderCustomsFileVO> orderCustomsFileVOList = ConvertUtil.convertList(orderCustomsFiles, OrderCustomsFileVO.class);
        if (orderCustomsFileVOList.size() > 0) {
            orderCustomsFileVOList.forEach(orderCustomsFileVO -> {
                String templateUrl = orderCustomsFileVO.getTemplateUrl();
                if(templateUrl != null && templateUrl.length() > 0){
                    String json = templateUrl;
                    try {
                        List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                        });
                        orderCustomsFileVO.setTemplateUrlVOS(templateUrlVOS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("json格式错误");
                        orderCustomsFileVO.setTemplateUrlVOS(new ArrayList<>());
                    }
                }else{
                    orderCustomsFileVO.setTemplateUrlVOS(new ArrayList<>());
                }
            });
        }
        orderInfoVO.setOrderCustomsFileVOList(orderCustomsFileVOList);

        //订单清关文件
        //订单对应清关文件list
        QueryWrapper<OrderClearanceFile> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("order_id", orderId);
        List<OrderClearanceFile> orderClearanceFiles = orderClearanceFileMapper.selectList(queryWrapper2);
        List<OrderClearanceFileVO> orderClearanceFileVOList = ConvertUtil.convertList(orderClearanceFiles, OrderClearanceFileVO.class);
        if(orderClearanceFileVOList.size() > 0){
            orderClearanceFileVOList.forEach(orderClearanceFileVO -> {
                String templateUrl = orderClearanceFileVO.getTemplateUrl();
                if(templateUrl != null && templateUrl.length() > 0){
                    String json = templateUrl;
                    try {
                        List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                        });
                        orderClearanceFileVO.setTemplateUrlVOS(templateUrlVOS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("json格式错误");
                        orderClearanceFileVO.setTemplateUrlVOS(new ArrayList<>());
                    }
                }else{
                    orderClearanceFileVO.setTemplateUrlVOS(new ArrayList<>());
                }
            });
        }
        orderInfoVO.setOrderClearanceFileVOList(orderClearanceFileVOList);

        //集货仓库
        String storeGoodsWarehouseCode = orderInfoVO.getStoreGoodsWarehouseCode();
        ShippingAreaVO shippingAreaVO = shippingAreaMapper.findShippingAreaByWarehouseCode(storeGoodsWarehouseCode);
        orderInfoVO.setShippingAreaVO(shippingAreaVO);

        //目的仓库
        String destinationWarehouseCode = orderInfoVO.getDestinationWarehouseCode();
        FabWarehouseVO fabWarehouseVO = fabWarehouseMapper.findFabWarehouseByWarehouseCode(destinationWarehouseCode);
        orderInfoVO.setFabWarehouseVO(fabWarehouseVO);

        //提货地址
        List<OrderPickVO> orderPickVOList = orderPickMapper.findOrderPickByOrderId(orderId);
        orderInfoVO.setOrderPickVOList(orderPickVOList);


        //费用明细(订单应收费用)
        OrderCostDetailVO orderCostDetailVO = getOrderCostDetailVO(orderInfoVO);
        orderInfoVO.setOrderCostDetailVO(orderCostDetailVO);//订单费用明细

        String remarks = orderInfoVO.getRemarks();//操作说明
        if(ObjectUtil.isNotEmpty(remarks)){
            String[] split = remarks.split("\n");
            List<String> list = Arrays.asList(split);
            orderInfoVO.setRemarksList(list);
        }
        return CommonResult.success(orderInfoVO);
    }

    @Override
    public IPage<OrderInfoVO> findWebOrderInfoByPage(QueryOrderInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        if(ObjectUtil.isEmpty(customerUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录。");
        }
        form.setCustomerId(customerUser.getId().intValue());
        //定义分页参数
        Page<OrderInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<OrderInfoVO> pageInfo = orderInfoMapper.findWebOrderInfoByPage(page, form);

        //订单显示，提货状态
        List<OrderInfoVO> records = pageInfo.getRecords();
        if(CollUtil.isNotEmpty(records)){
            records.forEach(orderInfoVO -> {
                Long orderId = orderInfoVO.getId();
                Integer isPick = orderInfoVO.getIsPick();//是否上门提货(0否 1是,order_pick)
                if(isPick == 1){
                    List<PickStatusVO> pickStatusVOList = orderPickMapper.findPickStatusByOrderId(orderId);
                    orderInfoVO.setPickStatusVOList(pickStatusVOList);
                }else{
                    orderInfoVO.setPickStatusVOList(new ArrayList<>());
                }

                //IsConfirmBilling  IS_CONFIRM_BILLING("is_confirm_billing", "是否确认计费重(1已确认 2未确认)")
                String isConfirmBilling = "";
                Map<String, Object> mapParm = new HashMap<>();
                mapParm.put("order_id", orderInfoVO.getId());
                mapParm.put("order_no", orderInfoVO.getOrderNo());
                mapParm.put("main_status_type", "front");
                mapParm.put("main_status_code", OrderEnum.FRONT_RECEIVED.getCode());//FRONT_RECEIVED("20", "已收货"),
                mapParm.put("interior_status_code", OrderEnum.IS_CONFIRM_BILLING.getCode());//IS_CONFIRM_BILLING("is_confirm_billing", "是否确认计费重(1已确认 2未确认)")
                OrderInteriorStatusVO orderInteriorStatusVO = orderInteriorStatusMapper.findOrderInteriorStatusByMapParm(mapParm);

                String statusFlag = "";//状态标志-是否确认计费重(1已确认 2未确认)
                if(ObjectUtil.isEmpty(orderInteriorStatusVO)){
                    statusFlag = "2";
                }else{
                    statusFlag = orderInteriorStatusVO.getStatusFlag();
                }

                isConfirmBilling = statusFlag;
                orderInfoVO.setIsConfirmBilling(isConfirmBilling);

            });
        }
        return pageInfo;
    }

    @Override
    public Map<String,Long> findOrderInfoDraftCount(QueryOrderInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        if(ObjectUtil.isNotEmpty(customerUser)){
            form.setCustomerId(customerUser.getId().intValue());
        }

        //FRONT_DRAFT("0", "草稿"),
        form.setFrontStatusCode(OrderEnum.FRONT_DRAFT.getCode());
        Long draftNum = orderInfoMapper.findOrderInfoDraftCount(form);
        //FRONT_UPDATE("9", "补资料"),
        form.setFrontStatusCode(OrderEnum.FRONT_UPDATE.getCode());
        Long updateNum = orderInfoMapper.findOrderInfoDraftCount(form);
        //FRONT_PLACED("10", "已下单"),
        form.setFrontStatusCode(OrderEnum.FRONT_PLACED.getCode());
        Long placedNum = orderInfoMapper.findOrderInfoDraftCount(form);
        //FRONT_RECEIVED("20", "已收货"),
        form.setFrontStatusCode(OrderEnum.FRONT_RECEIVED.getCode());
        Long receivedNum = orderInfoMapper.findOrderInfoDraftCount(form);
        //FRONT_TRANSIT("30", "转运中"),
        form.setFrontStatusCode(OrderEnum.FRONT_TRANSIT.getCode());
        Long transitNum = orderInfoMapper.findOrderInfoDraftCount(form);
        //FRONT_SIGNED("40", "已签收"),
        form.setFrontStatusCode(OrderEnum.FRONT_SIGNED.getCode());
        Long signedNum = orderInfoMapper.findOrderInfoDraftCount(form);
        //FRONT_FINISH("50", "已完成"),
        form.setFrontStatusCode(OrderEnum.FRONT_FINISH.getCode());
        Long finishNum = orderInfoMapper.findOrderInfoDraftCount(form);
        //FRONT_CANCEL("-1", "已取消"),
        form.setFrontStatusCode(OrderEnum.FRONT_CANCEL.getCode());
        Long cancelNum = orderInfoMapper.findOrderInfoDraftCount(form);

        Map<String,Long> totalMap = new HashMap<>();
        totalMap.put("draftNum", draftNum);//草稿
        totalMap.put("updateNum", updateNum);//补资料
        totalMap.put("placedNum", placedNum);//已下单
        totalMap.put("receivedNum", receivedNum);//已收货
        totalMap.put("transitNum", transitNum);//转运中
        totalMap.put("signedNum", signedNum);//已签收
        totalMap.put("finishNum", finishNum);//已完成
        totalMap.put("cancelNum", cancelNum);//已取消

        return totalMap;
    }

    @Override
    public Map<String,Long> findOrderInfoAfterCount(QueryOrderInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        if(ObjectUtil.isNotEmpty(customerUser)){
            form.setCustomerId(customerUser.getId().intValue());
        }

        //订单后端主状态

        //AFTER_DRAFT("0", "草稿"),
        form.setAfterStatusCode(OrderEnum.AFTER_DRAFT.getCode());
        Long draftNum = orderInfoMapper.findOrderInfoAfterCount(form);
        //AFTER_UPDATE("9", "补资料"),
        form.setAfterStatusCode(OrderEnum.AFTER_UPDATE.getCode());
        Long updateNum = orderInfoMapper.findOrderInfoAfterCount(form);
        //AFTER_PLACED("10", "已下单"),
        form.setAfterStatusCode(OrderEnum.AFTER_PLACED.getCode());
        Long placedNum = orderInfoMapper.findOrderInfoAfterCount(form);
        //AFTER_RECEIVED("20", "已收货"),
        form.setAfterStatusCode(OrderEnum.AFTER_RECEIVED.getCode());
        Long receivedNum = orderInfoMapper.findOrderInfoAfterCount(form);
        //AFTER_AFFIRM("30", "订单确认"),
        form.setAfterStatusCode(OrderEnum.AFTER_AFFIRM.getCode());
        Long affirmNum = orderInfoMapper.findOrderInfoAfterCount(form);
        //AFTER_TRANSIT("31", "转运中"),
        form.setAfterStatusCode(OrderEnum.AFTER_TRANSIT.getCode());
        Long transitNum = orderInfoMapper.findOrderInfoAfterCount(form);

        //AFTER_SIGNED("40", "已签收"),
        form.setAfterStatusCode(OrderEnum.AFTER_SIGNED.getCode());
        Long signedNum = orderInfoMapper.findOrderInfoAfterCount(form);
        //AFTER_FINISH("50", "已完成"),
        form.setAfterStatusCode(OrderEnum.AFTER_FINISH.getCode());
        Long finishNum = orderInfoMapper.findOrderInfoAfterCount(form);
        //AFTER_CANCEL("-1", "已取消"),
        form.setAfterStatusCode(OrderEnum.AFTER_CANCEL.getCode());
        Long cancelNum = orderInfoMapper.findOrderInfoAfterCount(form);

        Map<String,Long> totalMap = new HashMap<>();
        totalMap.put("draftNum", draftNum);//草稿
        totalMap.put("updateNum", updateNum);//补资料
        totalMap.put("placedNum", placedNum);//已下单
        totalMap.put("receivedNum", receivedNum);//已收货
        totalMap.put("affirmNum", affirmNum);//订单确认 *后端状态
        totalMap.put("transitNum", transitNum);//转运中
        totalMap.put("signedNum", signedNum);//已签收
        totalMap.put("finishNum", finishNum);//已完成
        totalMap.put("cancelNum", cancelNum);//已取消

        return totalMap;
    }


    @Override
    public CommonResult<List<String>> printOrderMark(Long orderId) {
        //订单信息
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfo(orderId);
        if(orderInfoVO == null){
            return CommonResult.error(-1, "订单不存在");
        }
        Long orderInfoId = orderInfoVO.getId();
        List<OrderCaseVO> orderCaseVOList = orderCaseMapper.findOrderCaseByOrderId(orderInfoId);
        List<String> list = new ArrayList<>();
        if(orderCaseVOList.size() > 0){
            orderCaseVOList.forEach(orderCaseVO -> {
                String cartonNo = orderCaseVO.getCartonNo();
                list.add(cartonNo);
            });
        }
        return CommonResult.success(list);
    }

    /*
     * 订单编辑-保存
     * 1.编辑保存-订单箱号
     * 2.编辑保存-订单商品
     * 3.编辑保存-订单文件（报关文件、清关文件）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<OrderInfoVO> editSaveOrderInfo(OrderInfoForm form) {
        Long id = form.getId();
        OrderInfo orderInfo = orderInfoMapper.selectById(id);
        if(orderInfo == null){
            return CommonResult.error(-1, "订单不存在");
        }
        Long orderInfoId = orderInfo.getId();//订单id
        Integer offerInfoId = orderInfo.getOfferInfoId();//报价id，运价id
        //1.编辑保存-订单箱号
        //保存-订单对应箱号信息:order_case
        List<OrderCaseVO> orderCaseVOList = form.getOrderCaseVOList();
        List<OrderCase> orderCaseList = ConvertUtil.convertList(orderCaseVOList, OrderCase.class);
        orderCaseList.forEach(orderCase -> {
            orderCase.setOrderId(orderInfo.getId().intValue());
        });
        QueryWrapper<OrderCase> orderCaseQueryWrapper = new QueryWrapper<>();
        orderCaseQueryWrapper.eq("order_id", orderInfo.getId());
        orderCaseService.remove(orderCaseQueryWrapper);
        orderCaseService.saveOrUpdateBatch(orderCaseList);

//        //TODO 保存，订单箱号 默认关联的配载柜号信息
//        List<OrderCase> orderCaseList1 = orderCaseService.list(orderCaseQueryWrapper);//查询订单箱号
//        //查询柜号信息-->根据报价查询配载id，配载查询提单id，提单查询柜号id，最终获取柜号信息
//        List<OceanCounter> oceanCounterList = orderConfMapper.findOceanCounterByOfferInfoId(offerInfoId);
//        //默认取第一个柜子
//        if(oceanCounterList.size() > 0){
//            OceanCounter oceanCounter = oceanCounterList.get(0);
//            Long hgId = oceanCounter.getId();//货柜id
//            //货柜对应运单箱号信息
//            List<CounterCase> counterCases = new ArrayList<>();
//            //箱号
//            List<Long> xhIds = new ArrayList<>();
//            orderCaseList1.forEach(orderCase -> {
//                Long xhId = orderCase.getId();
//                xhIds.add(xhId);//箱号
//                CounterCase counterCase = new CounterCase();
//                counterCase.setOceanCounterId(hgId);//提单柜号id(ocean_counter id)
//                counterCase.setOrderCaseId(xhId);//运单箱号id[订单箱号id](order_case id)
//                counterCases.add(counterCase);//货柜对应运单箱号信息
//            });
//
//            QueryWrapper<CounterCase> counterCaseQueryWrapper = new QueryWrapper<>();
//            counterCaseQueryWrapper.in("order_case_id", xhIds);//运单箱号id[订单箱号id](order_case id)
//            counterCaseService.remove(counterCaseQueryWrapper);//先删除
//            counterCaseService.saveOrUpdateBatch(counterCases);//在保存 货柜对应运单箱号信息
//        }


        //2.编辑保存-订单商品
        //保存-订单对应商品：order_shop
        List<OrderShopVO> orderShopVOList = form.getOrderShopVOList();
        List<OrderShop> orderShopList = ConvertUtil.convertList(orderShopVOList, OrderShop.class);
        orderShopList.forEach(orderShop -> {
            orderShop.setOrderId(orderInfo.getId().intValue());
            orderShop.setCreateTime(LocalDateTime.now());
        });
        QueryWrapper<OrderShop> orderShopQueryWrapper = new QueryWrapper<>();
        orderShopQueryWrapper.eq("order_id", orderInfo.getId());
        orderShopService.remove(orderShopQueryWrapper);
        orderShopService.saveOrUpdateBatch(orderShopList);

        //3.编辑保存-订单文件（报关文件、清关文件）
        //报关文件
        List<OrderCustomsFileVO> orderCustomsFileVOList = form.getOrderCustomsFileVOList();
        orderCustomsFileVOList.forEach(orderCustomsFileVO -> {
            List<TemplateUrlVO> templateUrlVOS = orderCustomsFileVO.getTemplateUrlVOS();
            if (templateUrlVOS != null && templateUrlVOS.size() > 0) {
                String s = JSONObject.toJSONString(templateUrlVOS);
                orderCustomsFileVO.setTemplateUrl(s);
            }else{
                orderCustomsFileVO.setTemplateUrl(null);
            }
        });
        List<OrderCustomsFile> orderCustomsFiles = ConvertUtil.convertList(orderCustomsFileVOList, OrderCustomsFile.class);
        if(orderCustomsFiles != null && orderCustomsFiles.size() > 0){
            orderCustomsFileService.saveOrUpdateBatch(orderCustomsFiles);
        }else{
            return CommonResult.error(-1, "报关文件不能为空");
        }

        //清关文件
        List<OrderClearanceFileVO> orderClearanceFileVOList = form.getOrderClearanceFileVOList();
        orderClearanceFileVOList.forEach(orderClearanceFileVO -> {
            List<TemplateUrlVO> templateUrlVOS = orderClearanceFileVO.getTemplateUrlVOS();
            if(templateUrlVOS != null && templateUrlVOS.size() > 0){
                String s = JSONObject.toJSONString(templateUrlVOS);
                orderClearanceFileVO.setTemplateUrl(s);
            }else{
                orderClearanceFileVO.setTemplateUrl(null);
            }
        });
        List<OrderClearanceFile> orderClearanceFiles = ConvertUtil.convertList(orderClearanceFileVOList, OrderClearanceFile.class);
        if(orderClearanceFiles != null && orderClearanceFiles.size() > 0){
            orderClearanceFileService.saveOrUpdateBatch(orderClearanceFiles);
        }else{
            return CommonResult.error(-1, "清关文件不能为空");
        }

        OrderInfoVO orderInfoVO = ConvertUtil.convert(orderInfo, OrderInfoVO.class);
        return CommonResult.success(orderInfoVO);
    }

    @Override
    public CommonResult<List<OceanBillVO>> findOceanBillByOfferInfoId(Integer offerInfoId) {
        List<OceanBillVO> oceanBillVO = orderInfoMapper.findOceanBillByOfferInfoId(offerInfoId);
        return CommonResult.success(oceanBillVO);
    }

    @Override
    public CommonResult<List<OceanCounterVO>> findOceanCounterByTdId(Long tdId) {
        List<OceanCounterVO> oceanCounterVOList = orderInfoMapper.findOceanCounterByTdId(tdId);
        return CommonResult.success(oceanCounterVOList);
    }

    @Override
    public CommonResult<OrderBillVO> findOrderBill(Long orderId) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
        if(orderInfoVO == null){
            return CommonResult.error(-1, "订单不存在");
        }
        OrderBillVO orderBillVO = ConvertUtil.convert(orderInfoVO, OrderBillVO.class);
        //订单配载信息
        List<String> confinfos = orderInfoMapper.findOrderConfInfoByOrderId(orderId);
        if(confinfos.size() > 0){
            String confInfo = "";
            for (int i=0; i<confinfos.size(); i++){
                confInfo += confinfos.get(i);
            }
            orderBillVO.setConfInfo(confInfo);
        }

        //订单应收费用
        List<OrderCopeReceivableVO> orderCopeReceivableVOS = orderCopeReceivableMapper.findOrderCopeReceivableByOrderId(orderId);
        orderBillVO.setOrderCopeReceivableVOS(orderCopeReceivableVOS);
        //订单应付费用
        List<OrderCopeWithVO> orderCopeWithVOS = orderCopeWithMapper.findOrderCopeWithByOrderId(orderId);
        orderBillVO.setOrderCopeWithVOS(orderCopeWithVOS);

        return CommonResult.success(orderBillVO);
    }

    @Override
    public CommonResult<OrderInfoVO> lookOrderInfoTask(Long orderId) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfo(orderId);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            return CommonResult.error(-1, "订单不存在");
        }
        //根据订单id，查询订单关联的任务，查看完成情况
        List<WaybillTaskRelevanceVO> waybillTaskRelevanceVOS = orderInfoMapper.findWaybillTaskRelevanceByOrderInfoId(orderId);
        orderInfoVO.setWaybillTaskRelevanceVOS(waybillTaskRelevanceVOS);
        return CommonResult.success(orderInfoVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<WaybillTaskRelevanceVO> confirmCompleted(Long id) {
        AuthUser user = baseService.getUser();
        WaybillTaskRelevance waybillTaskRelevance = waybillTaskRelevanceMapper.selectById(id);
        if(ObjectUtil.isEmpty(waybillTaskRelevance)){
            return CommonResult.error(-1, "没有找到此任务");
        }
        Integer loginUserId = user.getId().intValue();
        Integer taskUserId = waybillTaskRelevance.getUserId();
        if(!loginUserId.equals(taskUserId)){
            return CommonResult.error(-1, "只有本人才能点击完成");
        }
        waybillTaskRelevance.setStatus("3");//状态(0未激活 1已激活,未完成 2已完成)
        waybillTaskRelevance.setUpTime(LocalDateTime.now());
        waybillTaskRelevanceService.saveOrUpdate(waybillTaskRelevance);
        WaybillTaskRelevanceVO waybillTaskRelevanceVO = ConvertUtil.convert(waybillTaskRelevance, WaybillTaskRelevanceVO.class);
        return CommonResult.success(waybillTaskRelevanceVO);
    }

    @Override
    public CommonResult<List<WaybillTaskRelevanceVO>> lookOperateLog(Long id) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfo(id);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            return CommonResult.error(-1, "订单不存在");
        }
        List<WaybillTaskRelevanceVO> waybillTaskRelevanceVOS = orderInfoMapper.lookOperateLog(id);
        return CommonResult.success(waybillTaskRelevanceVOS);
    }

    @Override
    public List<OrderInfoVO> findOrderInfoByCustomer(OrderInfoCustomerForm form) {
        List<OrderInfoVO> orderInfoVOS = orderInfoMapper.findOrderInfoByCustomer(form);
        return orderInfoVOS;
    }

    @Override
    public CommonResult syncOrder(SyncOrderForm form) {

        Integer customerId = form.getCustomerId();
        CustomerVO customerVO = customerMapper.findCustomerById(customerId);
        String newWisdomToken = customerVO.getNewWisdomToken();//新智慧token不能为空,每个客户的token不用，通过tonken区分客户
        //入参键值对
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> validation = new HashMap<>();
        validation.put("access_token", newWisdomToken);
        requestMap.put("validation", validation);
        Map<String, Object> shipment = new HashMap<>();
        shipment.put("shipment_id", form.getShipmentId());
        shipment.put("client_reference", "");
        requestMap.put("shipment", shipment);

        //6、 获取运单信息
        String url = info;
        String feedback = extracted(url, requestMap);
        try{
            Map map = JSONUtil.toBean(feedback, Map.class);
            Integer status = MapUtil.getInt(map, "status");//状态
            String info = MapUtil.getStr(map, "info");//消息
            Long time = MapUtil.getLong(map, "time");//时间
            Map data = MapUtil.get(map, "data", Map.class);//数据

            ShipmentVO shipmentVO = MapUtil.get(data, "shipment", ShipmentVO.class);
            String shipmentJson = JSONUtil.toJsonStr(shipmentVO);
            log.info("shipmentJson:{}", shipmentJson);
            log.info("状态status:{}, 消息info:{}, 时间time:{}", status, info, time);
            log.info("数据data:{} ", data);

            //请求不成功，未获取到数据
            if(status == 0){
                return CommonResult.error(-1, info);
            }

            shipmentVO.setNew_wisdom_token(newWisdomToken);
            ShipmentVO saveShipment = shipmentService.saveShipment(shipmentVO);

        }catch (cn.hutool.json.JSONException exception){
            log.info("feedback: " + feedback);
            return CommonResult.error(-1, "请求不成功，未获取到数据");
        }
        return CommonResult.success("订单同步成功");
    }

    @Override
    public CommonResult<OrderInfoVO> newEditOrderInfo(OrderInfoNewForm form) {
        String orderNo = form.getOrderNo();
        OrderInfoVO orderInfoByOrderNo = orderInfoMapper.findOrderInfoByOrderNo(orderNo);
        if(ObjectUtil.isEmpty(orderInfoByOrderNo)){
            return CommonResult.error(-1, "订单号不存在,请生成订单");
        }

        Long orderInfoId = orderInfoByOrderNo.getId();
        //订单信息
        OrderInfoVO orderInfoVO = orderInfoMapper.newLookOrderInfo(orderInfoId, form.getOfferInfoId());
        if(orderInfoVO == null){
            return CommonResult.error(-1, "订单不存在");
        }
        //报价id
        Integer offerInfoId = form.getOfferInfoId();
        OfferInfo offerInfo = offerInfoMapper.selectById(offerInfoId);
        //报价模板id
        Integer qie = offerInfo.getQie();
        //是否上门提货 (0否 1是,order_pick)
        Integer isPick = orderInfoVO.getIsPick();

        //订柜尺寸[海运费]
        /*订柜尺寸：海运费规格*/
        List<TemplateCopeReceivableVO> oceanFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableOceanFeeByQie(qie);
        orderInfoVO.setOceanFeeList(oceanFeeList);

        //集货仓库[陆运费]
        /*集货仓库：陆运费规格*/
        List<TemplateCopeReceivableVO> inlandFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableInlandFeeListByQie(qie);
        orderInfoVO.setInlandFeeList(inlandFeeList);

        //目的仓库
        List<FabWarehouseVO> fabWarehouseVOList = fabWarehouseMapper.findFabWarehouseByqie(qie);
        orderInfoVO.setFabWarehouseVOList(fabWarehouseVOList);

        //关联的订单箱号
        /*订单对应箱号信息:order_case*/
        List<OrderCaseVO> orderCaseVOList = orderCaseMapper.findOrderCaseByOrderId(orderInfoId);
        orderInfoVO.setOrderCaseVOList(orderCaseVOList);
        CaseVO caseVO = getCaseVO(orderCaseVOList, orderInfoVO);
        orderInfoVO.setCaseVO(caseVO);

        //关联的订单商品
        /*订单对应商品：order_shop*/
        List<OrderShopVO> orderShopVOList = orderShopMapper.findOrderShopByOrderId(orderInfoId);
        orderInfoVO.setOrderShopVOList(orderShopVOList);

        //是否上门提货[是] -- 有提货地址
        //(0否 1是,order_pick)
        if(isPick == 1){
            //订单关联提货地址
            List<OrderPickVO> orderPickVOList = orderPickMapper.findOrderPickByOrderId(orderInfoId);
            orderInfoVO.setOrderPickVOList(orderPickVOList);
        }

        //TODO 其他费用 不做，删掉，放入`费用明细`
        //TODO 费用明细 仅展示 订单应收费用，订单应付费用 不要展示
        OrderCostDetailVO orderCostDetailVO = getOrderCostDetailVO(orderInfoVO);

        orderInfoVO.setOrderCostDetailVO(orderCostDetailVO);//订单费用明细

        String remarks = orderInfoVO.getRemarks();//操作说明
        if(ObjectUtil.isNotEmpty(remarks)){
            String[] split = remarks.split("\n");
            List<String> list = Arrays.asList(split);
            orderInfoVO.setRemarksList(list);
        }

        return CommonResult.success(orderInfoVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderInfoVO afterAffirm(Long id) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(id);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在");
        }
        OrderInfo orderInfo = ConvertUtil.convert(orderInfoVO, OrderInfo.class);
        String afterStatusCode = orderInfo.getAfterStatusCode();
//        if(!afterStatusCode.equals(OrderEnum.AFTER_RECEIVED.getCode())){
//            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单状态错误，不能确认");
//        }
        orderInfo.setAfterStatusCode(OrderEnum.AFTER_AFFIRM.getCode());
        orderInfo.setAfterStatusName(OrderEnum.AFTER_AFFIRM.getName());
        this.saveOrUpdate(orderInfo);
        OrderInfoVO convert = ConvertUtil.convert(orderInfo, OrderInfoVO.class);
        return convert;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fillMaterial(OrderInfoFillForm form) {
        Long id = form.getId();
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(id);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在");
        }
        OrderInfo orderInfo = ConvertUtil.convert(orderInfoVO, OrderInfo.class);
        String fillMaterialDescription = form.getFillMaterialDescription();
        orderInfo.setFillMaterialDescription(fillMaterialDescription);
        orderInfo.setAfterStatusCode(OrderEnum.AFTER_UPDATE.getCode());
        orderInfo.setAfterStatusName(OrderEnum.AFTER_UPDATE.getName());
        orderInfo.setFrontStatusCode(OrderEnum.FRONT_UPDATE.getCode());
        orderInfo.setFrontStatusName(OrderEnum.FRONT_UPDATE.getName());
        this.saveOrUpdate(orderInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderCaseReceipt(OrderCaseReceiptForm form) {
        String cartonNo = form.getCartonNo();
        //根据订单箱号查询，判断箱号是否存在
        OrderCaseVO orderCaseVO = orderCaseMapper.findOrderCaseByCartonNo(cartonNo);
        if(ObjectUtil.isEmpty(orderCaseVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单箱号不存在");
        }
        AuthUser user = baseService.getUser();
        if(ObjectUtil.isEmpty(user)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        OrderCase orderCase = ConvertUtil.convert(orderCaseVO, OrderCase.class);

        BigDecimal wmsLength = form.getWmsLength();
        BigDecimal wmsWidth = form.getWmsWidth();
        BigDecimal wmsHeight = form.getWmsHeight();
        BigDecimal wmsWeight = form.getWmsWeight();
        orderCase.setWmsLength(wmsLength);
        orderCase.setWmsHeight(wmsHeight);
        orderCase.setWmsWidth(wmsWidth);
        orderCase.setWmsWeight(wmsWeight);

        //体积(m3) = (长cm * 宽cm * 高cm) / 1000000
        BigDecimal wmsVolume = wmsLength.multiply(wmsWidth).multiply(wmsHeight).divide(new BigDecimal("1000000"),3, BigDecimal.ROUND_HALF_UP);
        orderCase.setWmsVolume(wmsVolume);
        orderCase.setWmsWeighDate(LocalDateTime.now());

        //1.修改订单箱号
        orderCaseService.saveOrUpdate(orderCase);

        //2.保存 - 订单装箱信息(仓库测量)
        OrderCaseWmsVO orderCaseWmsVO = orderCaseWmsMapper.findOrderCaseWmsByCartonNo(cartonNo);
        if(ObjectUtil.isEmpty(orderCaseWmsVO)){
            orderCaseWmsVO = ConvertUtil.convert(form, OrderCaseWmsVO.class);
        }else{
            orderCaseWmsVO = ConvertUtil.convert(form, OrderCaseWmsVO.class);
        }
        OrderCaseWms orderCaseWms = ConvertUtil.convert(orderCaseWmsVO, OrderCaseWms.class);
        orderCaseWms.setWmsVolume(wmsVolume);
        orderCaseWms.setUserId(user.getId().intValue());
        orderCaseWms.setUserName(user.getName());
        orderCaseWmsService.saveOrUpdate(orderCaseWms);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTrackNotice(OrderTrackNoticeForm form) {
        AuthUser user = baseService.getUser();
        if(ObjectUtil.isEmpty(user)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        String orderId = form.getOrderId();
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(Long.valueOf(orderId));
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在");
        }
        LogisticsTrack logisticsTrack = new LogisticsTrack();
        logisticsTrack.setOrderId(orderInfoVO.getId().toString());
        logisticsTrack.setDescription(form.getDescription());
        logisticsTrack.setCreateTime(form.getCreateTime());
        logisticsTrack.setOperatorId(user.getId().intValue());
        logisticsTrack.setOperatorName(user.getName());
        logisticsTrack.setRemark(form.getRemark());
        //保存-订单物流轨迹。
        logisticsTrackService.saveOrUpdate(logisticsTrack);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void affirmCounterWeightInfo(IsConfirmBillingForm form) {
        Long orderId = form.getOrderId();
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在");
        }
        Map<String, Object> mapParm = new HashMap<>();
        mapParm.put("order_id", orderInfoVO.getId());
        mapParm.put("order_no", orderInfoVO.getOrderNo());
        mapParm.put("main_status_type", "front");
        mapParm.put("main_status_code", OrderEnum.FRONT_RECEIVED.getCode());//FRONT_RECEIVED("20", "已收货"),
        mapParm.put("interior_status_code", OrderEnum.IS_CONFIRM_BILLING.getCode());//IS_CONFIRM_BILLING("is_confirm_billing", "是否确认计费重(1已确认 2未确认)")
        OrderInteriorStatusVO orderInteriorStatusVO = orderInteriorStatusMapper.findOrderInteriorStatusByMapParm(mapParm);

        OrderInteriorStatus orderInteriorStatus = new OrderInteriorStatus();
        if(ObjectUtil.isEmpty(orderInteriorStatusVO)){
            orderInteriorStatus.setOrderId(orderInfoVO.getId());
            orderInteriorStatus.setOrderNo(orderInfoVO.getOrderNo());
            orderInteriorStatus.setMainStatusType("front");//主状态类型(front前端 after后端)
            orderInteriorStatus.setMainStatusCode(OrderEnum.FRONT_RECEIVED.getCode());
            orderInteriorStatus.setMainStatusName(OrderEnum.FRONT_RECEIVED.getName());
            orderInteriorStatus.setInteriorStatusCode(OrderEnum.IS_CONFIRM_BILLING.getCode());
            orderInteriorStatus.setInteriorStatusName(OrderEnum.IS_CONFIRM_BILLING.getName());
            orderInteriorStatus.setStatusFlag(form.getStatusFlag());//状态标志-是否确认计费重(1已确认 2为确认)
        }else{
            orderInteriorStatus = ConvertUtil.convert(orderInteriorStatusVO, OrderInteriorStatus.class);
            orderInteriorStatus.setStatusFlag(form.getStatusFlag());//状态标志-是否确认计费重(1已确认 2为确认)
        }
        orderInteriorStatusService.saveOrUpdate(orderInteriorStatus);
    }

    @Override
    public IsConfirmBillingVO findOrderIsConfirmBilling(Long orderId) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在");
        }
        Map<String, Object> mapParm = new HashMap<>();
        mapParm.put("order_id", orderInfoVO.getId());
        mapParm.put("order_no", orderInfoVO.getOrderNo());
        mapParm.put("main_status_type", "front");
        mapParm.put("main_status_code", OrderEnum.FRONT_RECEIVED.getCode());//FRONT_RECEIVED("20", "已收货"),
        mapParm.put("interior_status_code", OrderEnum.IS_CONFIRM_BILLING.getCode());//IS_CONFIRM_BILLING("is_confirm_billing", "是否确认计费重(1已确认 2未确认)")
        OrderInteriorStatusVO orderInteriorStatusVO = orderInteriorStatusMapper.findOrderInteriorStatusByMapParm(mapParm);

        String statusFlag = "";//状态标志-是否确认计费重(1已确认 2未确认)
        if(ObjectUtil.isEmpty(orderInteriorStatusVO)){
            statusFlag = "2";
        }else{
            statusFlag = orderInteriorStatusVO.getStatusFlag();
        }
        IsConfirmBillingVO isConfirmBillingVO = new IsConfirmBillingVO();
        isConfirmBillingVO.setOrderId(orderId);
        isConfirmBillingVO.setStatusFlag(statusFlag);
        return isConfirmBillingVO;
    }

    @Override
    public IsAuditOrderVO findOrderIsAuditOrder(Long orderId) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在");
        }
        Map<String, Object> mapParm = new HashMap<>();
        mapParm.put("order_id", orderInfoVO.getId());
        mapParm.put("order_no", orderInfoVO.getOrderNo());
        mapParm.put("main_status_type", "after");
        mapParm.put("main_status_code", OrderEnum.AFTER_PLACED.getCode());//AFTER_PLACED("10", "已下单"),
        mapParm.put("interior_status_code", OrderEnum.IS_AUDIT_ORDER.getCode());//IS_AUDIT_ORDER("is_audit_order", "是否审核单据(1已审单 2未审单)"),
        OrderInteriorStatusVO orderInteriorStatusVO = orderInteriorStatusMapper.findOrderInteriorStatusByMapParm(mapParm);

        String statusFlag = "";//状态标志-是否审核单据(1已审单 2未审单)
        if(ObjectUtil.isEmpty(orderInteriorStatusVO)){
            statusFlag = "2";
        }else{
            statusFlag = orderInteriorStatusVO.getStatusFlag();
        }
        IsAuditOrderVO isAuditOrderVO = new IsAuditOrderVO();
        isAuditOrderVO.setOrderId(orderId);
        isAuditOrderVO.setStatusFlag(statusFlag);
        return isAuditOrderVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditOrderIsAuditOrder(IsAuditOrderForm form) {
        Long orderId = form.getOrderId();
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在");
        }
        Map<String, Object> mapParm = new HashMap<>();
        mapParm.put("order_id", orderInfoVO.getId());
        mapParm.put("order_no", orderInfoVO.getOrderNo());
        mapParm.put("main_status_type", "after");
        mapParm.put("main_status_code", OrderEnum.AFTER_PLACED.getCode());//AFTER_PLACED("10", "已下单"),
        mapParm.put("interior_status_code", OrderEnum.IS_AUDIT_ORDER.getCode());//IS_AUDIT_ORDER("is_audit_order", "是否审核单据(1已审单 2未审单)"),
        OrderInteriorStatusVO orderInteriorStatusVO = orderInteriorStatusMapper.findOrderInteriorStatusByMapParm(mapParm);

        OrderInteriorStatus orderInteriorStatus = new OrderInteriorStatus();
        if(ObjectUtil.isEmpty(orderInteriorStatusVO)){
            orderInteriorStatus.setOrderId(orderInfoVO.getId());
            orderInteriorStatus.setOrderNo(orderInfoVO.getOrderNo());
            orderInteriorStatus.setMainStatusType("after");//主状态类型(front前端 after后端)
            orderInteriorStatus.setMainStatusCode(OrderEnum.AFTER_PLACED.getCode());
            orderInteriorStatus.setMainStatusName(OrderEnum.AFTER_PLACED.getName());
            orderInteriorStatus.setInteriorStatusCode(OrderEnum.IS_AUDIT_ORDER.getCode());
            orderInteriorStatus.setInteriorStatusName(OrderEnum.IS_AUDIT_ORDER.getName());
            orderInteriorStatus.setStatusFlag(form.getStatusFlag());//状态标志-是否审核单据(1已审单 2未审单)
        }else{
            orderInteriorStatus = ConvertUtil.convert(orderInteriorStatusVO, OrderInteriorStatus.class);
            orderInteriorStatus.setStatusFlag(form.getStatusFlag());//状态标志-是否审核单据(1已审单 2未审单)
        }
        orderInteriorStatusService.saveOrUpdate(orderInteriorStatus);
    }

    @Override
    public OrderInfoVO affirmReceived(Long id) {
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(id);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在");
        }
        OrderInfo orderInfo = ConvertUtil.convert(orderInfoVO, OrderInfo.class);
        String afterStatusCode = orderInfo.getAfterStatusCode();
//        if(!afterStatusCode.equals(OrderEnum.AFTER_RECEIVED.getCode())){
//            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单状态错误，不能确认");
//        }
        orderInfo.setAfterStatusCode(OrderEnum.AFTER_RECEIVED.getCode());//AFTER_RECEIVED("20", "已收货"),
        orderInfo.setAfterStatusName(OrderEnum.AFTER_RECEIVED.getName());
        this.saveOrUpdate(orderInfo);
        OrderInfoVO convert = ConvertUtil.convert(orderInfo, OrderInfoVO.class);
        return convert;
    }

    private String extracted(String url, Map<String, Object> requestMap) {
        String feedback = HttpRequest
                .post(url)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(JSONUtil.toJsonStr(requestMap))
                .execute().body();
        return feedback;
    }

    /**
     * 获取订单费用明细
     * @param orderInfoVO
     * @return
     */
    private OrderCostDetailVO getOrderCostDetailVO(OrderInfoVO orderInfoVO) {
        /**费用信息**/
        //将币种信息转换为map，cid为键，币种信息为值
        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));

        Long orderId = orderInfoVO.getId();//订单id
        OrderCostDetailVO orderCostDetailVO = new OrderCostDetailVO();
        List<OrderCopeReceivableVO> orderCopeReceivableVOList = orderCopeReceivableMapper.findOrderCopeReceivableByOrderId(orderId);
        orderCostDetailVO.setOrderCopeReceivableVOS(orderCopeReceivableVOList);//订单应收费用

        /*订单对应应收费用汇总ist*/
        List<AggregateAmountVO> orderCopeReceivableAggregate = new ArrayList<>();
        Map<Integer, List<OrderCopeReceivableVO>> stringListMap1 = groupListByCid1(orderCopeReceivableVOList);
        for (Map.Entry<Integer, List<OrderCopeReceivableVO>> entry : stringListMap1.entrySet()) {
            Integer cid = entry.getKey();
            List<OrderCopeReceivableVO> orderCopeReceivableVOS = entry.getValue();

            CurrencyInfoVO currencyInfoVO = cidMap.get(Long.valueOf(cid));

            BigDecimal amountSum = new BigDecimal("0");
            for (int i=0; i<orderCopeReceivableVOS.size(); i++){
                OrderCopeReceivableVO orderCopeReceivableVO = orderCopeReceivableVOS.get(i);
                BigDecimal amount = orderCopeReceivableVO.getAmount();
                amountSum = amountSum.add(amount);
            }
            AggregateAmountVO aggregateAmountVO = new AggregateAmountVO();
            aggregateAmountVO.setAmount(amountSum);//金额
            aggregateAmountVO.setCid(cid);
            aggregateAmountVO.setCurrencyCode(currencyInfoVO.getCurrencyCode());
            aggregateAmountVO.setCurrencyName(currencyInfoVO.getCurrencyName());
            orderCopeReceivableAggregate.add(aggregateAmountVO);
        }
        orderCostDetailVO.setOrderCopeReceivableAggregate(orderCopeReceivableAggregate);
        return orderCostDetailVO;
    }






}
