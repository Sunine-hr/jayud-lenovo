package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.OrderCaseForm;
import com.jayud.mall.model.bo.QueryOrderInfoForm;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IOrderCaseService;
import com.jayud.mall.service.IOrderClearanceFileService;
import com.jayud.mall.service.IOrderCustomsFileService;
import com.jayud.mall.service.IOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    IOrderCustomsFileService orderCustomsFileService;

    @Autowired
    IOrderClearanceFileService orderClearanceFileService;

    @Autowired
    IOrderCaseService orderCaseService;


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
        List<OrderCaseVO> orderCaseVOList = orderCaseMapper.findOrderCaseConfByOrderId(orderId);
        orderInfoVO.setOrderCaseVOList(orderCaseVOList);

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


}
