package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.*;
import com.jayud.mall.utils.SnowflakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    BaseService baseService;

    @Override
    public IPage<OrderInfoVO> findOrderInfoByPage(QueryOrderInfoForm form) {
        //定义分页参数
        Page<OrderInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<OrderInfoVO> pageInfo = orderInfoMapper.findOrderInfoByPage(page, form);
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
        List<OrderCaseVO> orderCaseVOList = orderCaseMapper.findOrderShopByOrderId(orderId);
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
    public CommonResult updateOrderCaseConf(List<OrderCaseForm> list) {
        return null;
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
        List<OrderCaseVO> orderCaseVOList = orderCaseMapper.findOrderShopByOrderId(orderId);
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

        //保存-订单对应商品：order_shop
        List<OrderShopVO> orderShopVOList = form.getOrderShopVOList();
        List<OrderShop> orderShopList = ConvertUtil.convertList(orderShopVOList, OrderShop.class);
        orderShopList.forEach(orderShop -> {
            orderShop.setOrderId(orderInfo.getId().intValue());
        });
        QueryWrapper<OrderShop> orderShopQueryWrapper = new QueryWrapper<>();
        orderShopQueryWrapper.eq("order_id", orderInfo.getId());
        orderShopService.remove(orderShopQueryWrapper);
        orderShopService.saveOrUpdateBatch(orderShopList);

        //保存-订单对应提货信息表：order_pick
        List<OrderPickVO> orderPickVOList = form.getOrderPickVOList();
        if(orderPickVOList.size() > 0){
            List<OrderPick> orderPickList = ConvertUtil.convertList(orderPickVOList, OrderPick.class);
            orderPickList.forEach(orderPick -> {
                orderPick.setOrderId(orderInfo.getId());
            });
            QueryWrapper<OrderPick> orderPickQueryWrapper = new QueryWrapper<>();
            orderPickQueryWrapper.eq("order_id", orderInfo.getId());
            orderPickService.remove(orderPickQueryWrapper);
            orderPickService.saveOrUpdateBatch(orderPickList);
        }

        Integer offerInfoId = orderInfo.getOfferInfoId();
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
        //保存-产品订单表：order_info
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);

        //判断订单号是否存在，不存在则新增
        String orderNo = form.getOrderNo();
        if(orderNo == null || orderNo == ""){
            orderNo = String.valueOf(SnowflakeUtils.getOrderNo());//雪花算法生成订单id
            orderInfo.setOrderNo(orderNo);
        }
        //PLACED_AN_ORDER(10, "已下单：编辑、查看订单详情 "),
        orderInfo.setStatus(OrderEnum.PLACED_AN_ORDER.getCode());//订单状态
        orderInfo.setStatusName(OrderEnum.PLACED_AN_ORDER.getName());//订单名称
        this.saveOrUpdate(orderInfo);

        //提交订单，创建任务
        List<WaybillTaskRelevanceVO> waybillTaskRelevanceVOS =
                waybillTaskRelevanceService.saveWaybillTaskRelevance(orderInfo);

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

        //保存-订单对应商品：order_shop
        List<OrderShopVO> orderShopVOList = form.getOrderShopVOList();
        List<OrderShop> orderShopList = ConvertUtil.convertList(orderShopVOList, OrderShop.class);
        orderShopList.forEach(orderShop -> {
            orderShop.setOrderId(orderInfo.getId().intValue());
        });
        QueryWrapper<OrderShop> orderShopQueryWrapper = new QueryWrapper<>();
        orderShopQueryWrapper.eq("order_id", orderInfo.getId());
        orderShopService.remove(orderShopQueryWrapper);
        orderShopService.saveOrUpdateBatch(orderShopList);

        //保存-订单对应提货信息表：order_pick
        List<OrderPickVO> orderPickVOList = form.getOrderPickVOList();
        List<OrderPick> orderPickList = ConvertUtil.convertList(orderPickVOList, OrderPick.class);
        orderPickList.forEach(orderPick -> {
            orderPick.setOrderId(orderInfo.getId());
        });
        QueryWrapper<OrderPick> orderPickQueryWrapper = new QueryWrapper<>();
        orderPickQueryWrapper.eq("order_id", orderInfo.getId());
        orderPickService.remove(orderPickQueryWrapper);
        orderPickService.saveOrUpdateBatch(orderPickList);

        return CommonResult.success(ConvertUtil.convert(orderInfo, OrderInfoVO.class));

    }

    @Override
    public CommonResult<OrderInfoVO> draftSubmitOrderInfo(OrderInfoForm form) {
        Long id = form.getId();
        OrderInfo orderInfo = this.getById(id);
        //PLACED_AN_ORDER(10, "已下单：编辑、查看订单详情 "),
        orderInfo.setStatus(OrderEnum.PLACED_AN_ORDER.getCode());
        orderInfo.setStatusName(OrderEnum.PLACED_AN_ORDER.getName());
        this.saveOrUpdate(orderInfo);

        //提交订单，创建任务
        List<WaybillTaskRelevanceVO> waybillTaskRelevanceVOS =
                waybillTaskRelevanceService.saveWaybillTaskRelevance(orderInfo);

        OrderInfoVO orderInfoVO = ConvertUtil.convert(orderInfo, OrderInfoVO.class);
        return CommonResult.success(orderInfoVO);
    }

    @Override
    public CommonResult<OrderInfoVO> draftCancelOrderInfo(OrderInfoForm form) {
        Long id = form.getId();
        OrderInfo orderInfo = this.getById(id);
        orderInfo.setStatus(OrderEnum.CANCELED.getCode());
        orderInfo.setStatusName(OrderEnum.CANCELED.getName());
        this.saveOrUpdate(orderInfo);
        OrderInfoVO orderInfoVO = ConvertUtil.convert(orderInfo, OrderInfoVO.class);
        return CommonResult.success(orderInfoVO);
    }

    @Override
    public CommonResult<OrderInfoVO> lookOrderInfo(OrderInfoForm form) {
        Long id = form.getId();
//        OrderInfo orderInfo = this.getById(id);
//        OrderInfoVO orderInfoVO = ConvertUtil.convert(orderInfo, OrderInfoVO.class);
//        return CommonResult.success(orderInfoVO);





        return null;
    }


}
