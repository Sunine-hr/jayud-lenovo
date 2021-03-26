package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
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
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.*;
import com.jayud.mall.utils.SnowflakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 产品订单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

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
    BaseService baseService;



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
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(id);
        Long orderId = orderInfoVO.getId();//订单Id
        /**货物信息**/
        /*订单对应商品：order_shop*/
        List<OrderShopVO> orderShopVOList = orderShopMapper.findOrderShopByOrderId(orderId);
        orderInfoVO.setOrderShopVOList(orderShopVOList);

        /*订单对应箱号信息:order_case*/
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
        /*订单对应应收费用明细:order_cope_receivable*/
        List<OrderCopeReceivableVO> orderCopeReceivableVOList =
                orderCopeReceivableMapper.findOrderCopeReceivableByOrderId(orderId);
        orderInfoVO.setOrderCopeReceivableVOList(orderCopeReceivableVOList);

        /*订单对应应付费用明细:order_cope_with*/
        List<OrderCopeWithVO> orderCopeWithVOList =
                orderCopeWithMapper.findOrderCopeWithByOrderId(orderId);
        orderInfoVO.setOrderCopeWithVOList(orderCopeWithVOList);

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<OrderInfoVO> temporaryStorageOrderInfo(OrderInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();

        //保存-产品订单表：order_info
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);
        Integer offerInfoId = orderInfo.getOfferInfoId();//报价id，运价id

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
        orderInfo.setStatus(OrderEnum.DRAFT.getCode());//订单状态
        orderInfo.setStatusName(OrderEnum.DRAFT.getName());//订单名称
        this.saveOrUpdate(orderInfo);

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

        //TODO 保存，订单箱号 默认关联的配载柜号信息
        List<OrderCase> orderCaseList1 = orderCaseService.list(orderCaseQueryWrapper);//查询订单箱号
        //查询柜号信息-->根据报价查询配载id，配载查询提单id，提单查询柜号id，最终获取柜号信息
        List<OceanCounter> oceanCounterList = orderConfMapper.findOceanCounterByOfferInfoId(offerInfoId);
        //默认取第一个柜子

        if(CollUtil.isNotEmpty(oceanCounterList)){
            OceanCounter oceanCounter = oceanCounterList.get(0);
            Long hgId = oceanCounter.getId();//货柜id
            //货柜对应运单箱号信息
            List<CounterCase> counterCases = new ArrayList<>();
            //箱号
            List<Long> xhIds = new ArrayList<>();
            orderCaseList1.forEach(orderCase -> {
                Long xhId = orderCase.getId();
                xhIds.add(xhId);//箱号
                CounterCase counterCase = new CounterCase();
                counterCase.setOceanCounterId(hgId);//提单柜号id(ocean_counter id)
                counterCase.setOrderCaseId(xhId);//运单箱号id[订单箱号id](order_case id)
                counterCases.add(counterCase);//货柜对应运单箱号信息
            });

            QueryWrapper<CounterCase> counterCaseQueryWrapper = new QueryWrapper<>();
            counterCaseQueryWrapper.in("order_case_id", xhIds);//运单箱号id[订单箱号id](order_case id)
            counterCaseService.remove(counterCaseQueryWrapper);//先删除
            counterCaseService.saveOrUpdateBatch(counterCases);//在保存 货柜对应运单箱号信息
        }

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
            List<OrderPick> orderPickList = ConvertUtil.convertList(orderPickVOList, OrderPick.class);
            orderPickList.forEach(orderPick -> {
                orderPick.setOrderId(orderInfo.getId());
            });
            QueryWrapper<OrderPick> orderPickQueryWrapper = new QueryWrapper<>();
            orderPickQueryWrapper.eq("order_id", orderInfo.getId());
            orderPickService.remove(orderPickQueryWrapper);
            orderPickService.saveOrUpdateBatch(orderPickList);
        }


        //订单对应报关文件 order_customs_file
        //`need_declare` int(11) DEFAULT NULL COMMENT '是否需要报关0-否，1-是 (订单对应报关文件:order_customs_file)',
        List<OrderCustomsFile> orderCustomsFileList = getOrderCustomsFiles(orderInfo, offerInfoId);
        QueryWrapper<OrderCustomsFile> orderCustomsFileQueryWrapper = new QueryWrapper<>();
        orderCustomsFileQueryWrapper.eq("order_id", orderInfo.getId());
        orderCustomsFileService.remove(orderCustomsFileQueryWrapper);
        orderCustomsFileService.saveOrUpdateBatch(orderCustomsFileList);

        //订单对应清关文件 order_clearance_file
        //`need_clearance` int(11) DEFAULT NULL COMMENT '是否需要清关0-否，1-是 (订单对应清关文件:order_clearance_file)',
        List<OrderClearanceFile> orderClearanceFileList = getOrderClearanceFiles(orderInfo, offerInfoId);
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
            if(specificationCode.equals(reserveSize)){
                OrderCopeReceivable orderCopeReceivable = new OrderCopeReceivable();
                orderCopeReceivable.setOrderId(orderId);//订单ID(order_info id)
                orderCopeReceivable.setCostCode(templateCopeReceivableVO.getCostCode());//费用代码(cost_item cost_code)
                orderCopeReceivable.setCostName(templateCopeReceivableVO.getCostName());//费用名称(cost_item cost_name)
                orderCopeReceivable.setAmount(templateCopeReceivableVO.getAmount());//金额
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
        for (int i=0; i<inlandFeeList.size(); i++){
            TemplateCopeReceivableVO templateCopeReceivableVO = inlandFeeList.get(i);
            String specificationCode = templateCopeReceivableVO.getSpecificationCode();
            if(specificationCode.equals(storeGoodsWarehouseCode)){
                OrderCopeReceivable orderCopeReceivable = new OrderCopeReceivable();
                orderCopeReceivable.setOrderId(orderId);//订单ID(order_info id)
                orderCopeReceivable.setCostCode(templateCopeReceivableVO.getCostCode());//费用代码(cost_item cost_code)
                orderCopeReceivable.setCostName(templateCopeReceivableVO.getCostName());//费用名称(cost_item cost_name)
                orderCopeReceivable.setAmount(templateCopeReceivableVO.getAmount());//金额
                orderCopeReceivable.setCid(templateCopeReceivableVO.getCid());//币种(currency_info id)
                orderCopeReceivable.setRemarks(templateCopeReceivableVO.getRemarks());//描述
                //应收 内陆费
                orderCopeReceivables.add(orderCopeReceivable);
                break;
            }
        }
        //(3)其他应收费用，过滤掉 海运费、内陆费
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
            orderCopeReceivable.setAmount(templateCopeReceivableVO.getAmount());//金额
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
            orderCopeWith.setAmount(templateCopeWithVO.getAmount());//金额
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

    /**
     * 获取-订单对应清关文件-s
     * @param orderInfo
     * @param offerInfoId
     * @return
     */
    private List<OrderClearanceFile> getOrderClearanceFiles(OrderInfo orderInfo, Integer offerInfoId) {
        Integer needClearance = orderInfo.getNeedClearance();
        List<OrderClearanceFile> orderClearanceFileList = new ArrayList<>();
        List<TemplateFileVO> templateFileByOrder = new ArrayList<>();
        if(needClearance == 0){
            TemplateFileOrderForm fileOrderForm = new TemplateFileOrderForm();
            fileOrderForm.setOfferInfoId(offerInfoId);
            fileOrderForm.setGroupCode("C");
            templateFileByOrder = templateFileService.findTemplateFileByOrder(fileOrderForm);
        }else if(needClearance == 1){
            TemplateFileOrderForm fileOrderForm = new TemplateFileOrderForm();
            fileOrderForm.setOfferInfoId(offerInfoId);
            fileOrderForm.setGroupCode("D");
            templateFileByOrder = templateFileService.findTemplateFileByOrder(fileOrderForm);
        }
        if(templateFileByOrder.size() > 0){
            templateFileByOrder.forEach(templateFileVO -> {
                OrderClearanceFile orderClearanceFile = new OrderClearanceFile();
                orderClearanceFile.setOrderId(orderInfo.getId().intValue());
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

    /**
     * 获取-订单对应报关文件-s
     * @param orderInfo
     * @param offerInfoId
     * @return
     */
    private List<OrderCustomsFile> getOrderCustomsFiles(OrderInfo orderInfo, Integer offerInfoId) {
        /*
        0 否 对应 买关，
        1 是 对应 独立。

        @ApiModelProperty(value = "文件分组代码" +
        "A,报关服务-买单报关" +
        "B,报关服务-独立报关" +
        "C,清关服务-买单报关" +
        "D,清关服务-独立报关", position = 2)
        */
        Integer needDeclare = orderInfo.getNeedDeclare();
        List<OrderCustomsFile> orderCustomsFileList = new ArrayList<>();
        List<TemplateFileVO> templateFileByOrder = new ArrayList<>();
        if(needDeclare == 0){
            TemplateFileOrderForm fileOrderForm = new TemplateFileOrderForm();
            fileOrderForm.setOfferInfoId(offerInfoId);
            fileOrderForm.setGroupCode("A");
            templateFileByOrder = templateFileService.findTemplateFileByOrder(fileOrderForm);
        }else if(needDeclare == 1){
            TemplateFileOrderForm fileOrderForm = new TemplateFileOrderForm();
            fileOrderForm.setOfferInfoId(offerInfoId);
            fileOrderForm.setGroupCode("B");
            templateFileByOrder = templateFileService.findTemplateFileByOrder(fileOrderForm);
        }
        if(templateFileByOrder.size() > 0){
            templateFileByOrder.forEach(templateFileVO -> {
                OrderCustomsFile orderCustomsFile = new OrderCustomsFile();
                orderCustomsFile.setOrderId(orderInfo.getId().intValue());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<OrderInfoVO> submitOrderInfo(OrderInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();

        //保存-产品订单表：order_info
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);
        Integer offerInfoId = orderInfo.getOfferInfoId();//报价id，运价id

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
        //PLACED_AN_ORDER(10, "已下单：编辑、查看订单详情 "),
        orderInfo.setStatus(OrderEnum.PLACED_AN_ORDER.getCode());//订单状态
        orderInfo.setStatusName(OrderEnum.PLACED_AN_ORDER.getName());//订单名称
        this.saveOrUpdate(orderInfo);

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

        //TODO 保存，订单箱号 默认关联的配载柜号信息
        List<OrderCase> orderCaseList1 = orderCaseService.list(orderCaseQueryWrapper);//查询订单箱号
        //查询柜号信息-->根据报价查询配载id，配载查询提单id，提单查询柜号id，最终获取柜号信息
        List<OceanCounter> oceanCounterList = orderConfMapper.findOceanCounterByOfferInfoId(offerInfoId);
        //默认取第一个柜子
        if(CollUtil.isNotEmpty(oceanCounterList)){
            OceanCounter oceanCounter = oceanCounterList.get(0);
            Long hgId = oceanCounter.getId();//货柜id
            //货柜对应运单箱号信息
            List<CounterCase> counterCases = new ArrayList<>();
            //箱号
            List<Long> xhIds = new ArrayList<>();
            orderCaseList1.forEach(orderCase -> {
                Long xhId = orderCase.getId();
                xhIds.add(xhId);//箱号
                CounterCase counterCase = new CounterCase();
                counterCase.setOceanCounterId(hgId);//提单柜号id(ocean_counter id)
                counterCase.setOrderCaseId(xhId);//运单箱号id[订单箱号id](order_case id)
                counterCases.add(counterCase);//货柜对应运单箱号信息
            });

            QueryWrapper<CounterCase> counterCaseQueryWrapper = new QueryWrapper<>();
            counterCaseQueryWrapper.in("order_case_id", xhIds);//运单箱号id[订单箱号id](order_case id)
            counterCaseService.remove(counterCaseQueryWrapper);//先删除
            counterCaseService.saveOrUpdateBatch(counterCases);//在保存 货柜对应运单箱号信息
        }


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
            List<OrderPick> orderPickList = ConvertUtil.convertList(orderPickVOList, OrderPick.class);
            orderPickList.forEach(orderPick -> {
                orderPick.setOrderId(orderInfo.getId());
            });
            QueryWrapper<OrderPick> orderPickQueryWrapper = new QueryWrapper<>();
            orderPickQueryWrapper.eq("order_id", orderInfo.getId());
            orderPickService.remove(orderPickQueryWrapper);
            orderPickService.saveOrUpdateBatch(orderPickList);
        }

        //订单对应报关文件 order_customs_file
        //`need_declare` int(11) DEFAULT NULL COMMENT '是否需要报关0-否，1-是 (订单对应报关文件:order_customs_file)',
        List<OrderCustomsFile> orderCustomsFileList = getOrderCustomsFiles(orderInfo, offerInfoId);
        QueryWrapper<OrderCustomsFile> orderCustomsFileQueryWrapper = new QueryWrapper<>();
        orderCustomsFileQueryWrapper.eq("order_id", orderInfo.getId());
        orderCustomsFileService.remove(orderCustomsFileQueryWrapper);
        orderCustomsFileService.saveOrUpdateBatch(orderCustomsFileList);

        //订单对应清关文件 order_clearance_file
        //`need_clearance` int(11) DEFAULT NULL COMMENT '是否需要清关0-否，1-是 (订单对应清关文件:order_clearance_file)',
        List<OrderClearanceFile> orderClearanceFileList = getOrderClearanceFiles(orderInfo, offerInfoId);
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
            if(specificationCode.equals(reserveSize)){
                OrderCopeReceivable orderCopeReceivable = new OrderCopeReceivable();
                orderCopeReceivable.setOrderId(orderId);//订单ID(order_info id)
                orderCopeReceivable.setCostCode(templateCopeReceivableVO.getCostCode());//费用代码(cost_item cost_code)
                orderCopeReceivable.setCostName(templateCopeReceivableVO.getCostName());//费用名称(cost_item cost_name)
                orderCopeReceivable.setAmount(templateCopeReceivableVO.getAmount());//金额
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
        for (int i=0; i<inlandFeeList.size(); i++){
            TemplateCopeReceivableVO templateCopeReceivableVO = inlandFeeList.get(i);
            String specificationCode = templateCopeReceivableVO.getSpecificationCode();
            if(specificationCode.equals(storeGoodsWarehouseCode)){
                OrderCopeReceivable orderCopeReceivable = new OrderCopeReceivable();
                orderCopeReceivable.setOrderId(orderId);//订单ID(order_info id)
                orderCopeReceivable.setCostCode(templateCopeReceivableVO.getCostCode());//费用代码(cost_item cost_code)
                orderCopeReceivable.setCostName(templateCopeReceivableVO.getCostName());//费用名称(cost_item cost_name)
                orderCopeReceivable.setAmount(templateCopeReceivableVO.getAmount());//金额
                orderCopeReceivable.setCid(templateCopeReceivableVO.getCid());//币种(currency_info id)
                orderCopeReceivable.setRemarks(templateCopeReceivableVO.getRemarks());//描述
                //应收 内陆费
                orderCopeReceivables.add(orderCopeReceivable);
                break;
            }
        }
        //(3)其他应收费用，过滤掉 海运费、内陆费
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
            orderCopeReceivable.setAmount(templateCopeReceivableVO.getAmount());//金额
            orderCopeReceivable.setCid(templateCopeReceivableVO.getCid());//币种(currency_info id)
            orderCopeReceivable.setRemarks(templateCopeReceivableVO.getRemarks());//描述
            //应收 其他费用
            orderCopeReceivables.add(orderCopeReceivable);
        }

        QueryWrapper<OrderCopeReceivable> OrderCopeReceivableQueryWrapper = new QueryWrapper<>();
        OrderCopeReceivableQueryWrapper.eq("order_id", orderInfo.getId());
        orderCopeReceivableService.remove(OrderCopeReceivableQueryWrapper);//先删除
        orderCopeReceivableService.saveOrUpdateBatch(orderCopeReceivables);//在保存  订单应收费用

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
            orderCopeWith.setAmount(templateCopeWithVO.getAmount());//金额
            orderCopeWith.setCid(templateCopeWithVO.getCid());//币种(currency_info id)
            orderCopeWith.setRemarks(templateCopeWithVO.getRemarks());//描述
            //应付 费用
            orderCopeWiths.add(orderCopeWith);
        }

        QueryWrapper<OrderCopeWith> orderCopeWithQueryWrapper = new QueryWrapper<>();
        orderCopeWithQueryWrapper.eq("order_id", orderInfo.getId());
        orderCopeWithService.remove(orderCopeWithQueryWrapper);//先删除
        orderCopeWithService.saveOrUpdateBatch(orderCopeWiths);//在保存  订单应付费用

        //TODO 提交订单时，创建订单任务
        List<WaybillTaskRelevanceVO> waybillTaskRelevanceVOS = waybillTaskRelevanceService.saveWaybillTaskRelevance(orderInfo);

        return CommonResult.success(ConvertUtil.convert(orderInfo, OrderInfoVO.class));

    }

    @Override
    public CommonResult<OrderInfoVO> draftCancelOrderInfo(OrderInfoForm form) {
        Long id = form.getId();
        OrderInfo orderInfo = this.getById(id);
        if(orderInfo.getStatus().equals(OrderEnum.DRAFT.getCode())){
            orderInfo.setStatus(OrderEnum.CANCELED.getCode());
            orderInfo.setStatusName(OrderEnum.CANCELED.getName());
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
    public CommonResult<OrderInfoVO> lookOrderInfo(OrderInfoForm form) {
        Long orderInfoId = form.getId();
        //订单信息
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfo(orderInfoId);
        if(orderInfoVO == null){
            return CommonResult.error(-1, "订单不存在");
        }
        Long orderId = orderInfoVO.getId();//订单id
        //订柜尺寸
        String reserveSize = orderInfoVO.getReserveSize();//订柜尺寸
        QuotationType quotationType = quotationTypeMapper.findQuotationTypeByCode(reserveSize);
        orderInfoVO.setReserveSizeName(quotationType.getName());

        //物流轨迹 TODO 待实现，先给测试数据
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
        form.setCustomerId(customerUser.getId().intValue());
        //定义分页参数
        Page<OrderInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<OrderInfoVO> pageInfo = orderInfoMapper.findWebOrderInfoByPage(page, form);
        return pageInfo;
    }

    @Override
    public Long findOrderInfoDraftCount(QueryOrderInfoForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        form.setCustomerId(customerUser.getId().intValue());
        Long draftNum = orderInfoMapper.findOrderInfoDraftCount(form);
        return draftNum;
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

        //TODO 保存，订单箱号 默认关联的配载柜号信息
        List<OrderCase> orderCaseList1 = orderCaseService.list(orderCaseQueryWrapper);//查询订单箱号
        //查询柜号信息-->根据报价查询配载id，配载查询提单id，提单查询柜号id，最终获取柜号信息
        List<OceanCounter> oceanCounterList = orderConfMapper.findOceanCounterByOfferInfoId(offerInfoId);
        //默认取第一个柜子
        if(oceanCounterList.size() > 0){
            OceanCounter oceanCounter = oceanCounterList.get(0);
            Long hgId = oceanCounter.getId();//货柜id
            //货柜对应运单箱号信息
            List<CounterCase> counterCases = new ArrayList<>();
            //箱号
            List<Long> xhIds = new ArrayList<>();
            orderCaseList1.forEach(orderCase -> {
                Long xhId = orderCase.getId();
                xhIds.add(xhId);//箱号
                CounterCase counterCase = new CounterCase();
                counterCase.setOceanCounterId(hgId);//提单柜号id(ocean_counter id)
                counterCase.setOrderCaseId(xhId);//运单箱号id[订单箱号id](order_case id)
                counterCases.add(counterCase);//货柜对应运单箱号信息
            });

            QueryWrapper<CounterCase> counterCaseQueryWrapper = new QueryWrapper<>();
            counterCaseQueryWrapper.in("order_case_id", xhIds);//运单箱号id[订单箱号id](order_case id)
            counterCaseService.remove(counterCaseQueryWrapper);//先删除
            counterCaseService.saveOrUpdateBatch(counterCases);//在保存 货柜对应运单箱号信息
        }


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
        waybillTaskRelevance.setStatus("3");//状态(0未激活 1已激活 2异常 3已完成)
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

    /**
     * 获取订单费用明细
     * @param orderInfoVO
     * @return
     */
    private OrderCostDetailVO getOrderCostDetailVO(OrderInfoVO orderInfoVO) {
        Long orderId = orderInfoVO.getId();//订单id
        OrderCostDetailVO orderCostDetailVO = new OrderCostDetailVO();
        List<OrderCopeReceivableVO> orderCopeReceivableVOS = orderCopeReceivableMapper.findOrderCopeReceivableByOrderId(orderId);
        orderCostDetailVO.setOrderCopeReceivableVOS(orderCopeReceivableVOS);//订单应收费用
        BigDecimal orderCopeReceivableAmountTotal = new BigDecimal("0");//汇总金额
        if(orderCopeReceivableVOS != null && orderCopeReceivableVOS.size() > 0){
            for (int i = 0; i<orderCopeReceivableVOS.size(); i++){
                OrderCopeReceivableVO orderCopeReceivableVO = orderCopeReceivableVOS.get(i);
                BigDecimal amount = orderCopeReceivableVO.getAmount() != null ? orderCopeReceivableVO.getAmount() : new BigDecimal("0");
                orderCopeReceivableAmountTotal = orderCopeReceivableAmountTotal.add(amount);
            }
            String currencyCode = orderCopeReceivableVOS.get(0).getCurrencyCode();//金额的币种，默认取第一个
            String orderCopeReceivableAmountTotalFormat = orderCopeReceivableAmountTotal.toString() + " " + currencyCode;
            orderCostDetailVO.setOrderCopeReceivableAmountTotal(orderCopeReceivableAmountTotalFormat);
        }
        return orderCostDetailVO;
    }






}
