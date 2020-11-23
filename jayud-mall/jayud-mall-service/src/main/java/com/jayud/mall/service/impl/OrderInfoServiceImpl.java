package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.enums.OrderEnum;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.*;
import com.jayud.mall.utils.SnowflakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        //保存-产品订单表：order_info
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);

        //判断订单号是否存在，不存在则新增
        String orderNo = form.getOrderNo();
        if(orderNo == null || orderNo == ""){
            orderNo = String.valueOf(SnowflakeUtils.getOrderNo());//雪花算法生成订单id
            orderInfo.setOrderNo(orderNo);
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
    public CommonResult<OrderInfoVO> submitOrderInfo(OrderInfoForm form) {
        //保存-产品订单表：order_info
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);

        //判断订单号是否存在，不存在则新增
        String orderNo = form.getOrderNo();
        if(orderNo == null || orderNo == ""){
            orderNo = String.valueOf(SnowflakeUtils.getOrderNo());//雪花算法生成订单id
            orderInfo.setOrderNo(orderNo);
        }
        //SUBMITTED(1, "已提交"),
        orderInfo.setStatus(OrderEnum.SUBMITTED.getCode());//订单状态
        orderInfo.setStatusName(OrderEnum.SUBMITTED.getName());//订单名称
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


}
