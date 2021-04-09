package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.*;

import java.util.List;

/**
 * <p>
 * 产品订单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findOrderInfoByPage(QueryOrderInfoForm form);

    /**
     * 订单管理-查看审核文件
     * @param id
     * @return
     */
    CommonResult<OrderInfoVO> lookOrderInfoFile(Long id);

    /**
     * 审核通过-订单对应报关文件
     * @param id
     * @return
     */
    CommonResult<OrderCustomsFileVO> passOrderCustomsFile(Long id);

    /**
     * 审核通过-订单对应清关文件
     * @param id
     * @return
     */
    CommonResult<OrderClearanceFileVO> passOrderClearanceFile(Long id);

    /**
     * 审核不通过-订单对应报关文件
     * @param id
     * @return
     */
    CommonResult<OrderCustomsFileVO> onPassCustomsFile(Long id);

    /**
     * 审核不通过-订单对应清关文件
     * @param id
     * @return
     */
    CommonResult<OrderClearanceFileVO> onPassOrderClearanceFile(Long id);

    /**
     * 订单管理-查看货物信息
     * @param id
     * @return
     */
    CommonResult<OrderInfoVO> lookOrderInfoGoods(Long id);

    /**
     * 订单管理-修改订单箱号(长宽高等)
     * @param list
     */
    void updateOrderCase(List<OrderCaseForm> list);

    /**
     * 订单管理-查看配载信息
     * @param id
     * @return
     */
    CommonResult<OrderInfoVO> lookOrderInfoConf(Long id);

    /**
     * 订单管理-修改配载信息
     * @param form
     * @return
     */
    CommonResult updateOrderCaseConf(SaveCounterCaseForm form);

    /**
     * 订单管理-修改配载信息2
     * @param form
     * @return
     */
    CommonResult updateOrderCaseConf2(SaveCounterCase2Form form);

    /**
     * 订单管理-查看费用信息
     * @param id
     * @return
     */
    CommonResult<OrderInfoVO> lookOrderInfoCost(Long id);

    /**
     * 订单管理-修改费用信息
     * @param form
     * @return
     */
    CommonResult updateOrderInfoCost(OrderInfoCostForm form);

    /**
     * 订单管理-查看订单详细
     * @param id
     * @return
     */
    CommonResult<OrderInfoVO> lookOrderInfoDetails(Long id);

    /**
     * 订单下单-暂存订单
     * @param form
     * @return
     */
    CommonResult<OrderInfoVO> temporaryStorageOrderInfo(OrderInfoForm form);

    /**
     * 订单下单-提交订单
     * @param form
     * @return
     */
    CommonResult<OrderInfoVO> submitOrderInfo(OrderInfoForm form);

    /**
     * 订单列表-草稿-取消
     * @param form
     * @return
     */
    CommonResult<OrderInfoVO> draftCancelOrderInfo(OrderInfoForm form);

    /**
     * 订单列表-查看订单详情(编辑用的)<br/>
     * 草稿-提交-查看订单<br/>
     * 草稿-查看订单详情<br/>
     * @param form
     * @return
     */
    CommonResult<OrderInfoVO> lookEditOrderInfo(OrderInfoForm form);

    /**
     * 订单列表-查看订单详情(查看)<br/>
     * 草稿-提交-查看订单<br/>
     * 草稿-查看订单详情<br/>
     * @param form
     * @return
     */
    CommonResult<OrderInfoVO> lookOrderInfo(OrderInfoForm form);

    /**
     * web端分页查询订单列表
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findWebOrderInfoByPage(QueryOrderInfoForm form);

    /**
     * web端分页查询订单列表(统计草稿)
     * @param form
     * @return
     */
    Long findOrderInfoDraftCount(QueryOrderInfoForm form);

    /**
     * 订单详情-打印唛头（打印订单箱号）
     * @param orderId
     * @return
     */
    CommonResult<List<String>> printOrderMark(Long orderId);

    /**
     * 订单编辑-保存
     * 1.编辑保存-订单箱号
     * 2.编辑保存-订单商品
     * 3.编辑保存-订单文件（报关文件、清关文件）
     * @param form
     * @return
     */
    CommonResult<OrderInfoVO> editSaveOrderInfo(OrderInfoForm form);

    /**
     * 根据订单的运单id，查找配载，在查找配载单关联的提单
     * @param offerInfoId
     * @return
     */
    CommonResult<List<OceanBillVO>> findOceanBillByOfferInfoId(Integer offerInfoId);

    /**
     * 根据提单id，查找提单关联的柜号id list(其实是1对1的)
     * @param tdId
     * @return
     */
    CommonResult<List<OceanCounterVO>> findOceanCounterByTdId(Long tdId);

    /**
     * 订单待生成账单-生成账单(根据 订单id 查询)
     * @param orderId
     * @return
     */
    CommonResult<OrderBillVO> findOrderBill(Long orderId);

    /**
     * 运单任务-反馈状态(根据 订单id 查询)
     * @param orderId
     * @return
     */
    CommonResult<OrderInfoVO> lookOrderInfoTask(Long orderId);

    /**
     * 运单任务-反馈状态(点击已完成)
     * @param id
     * @return
     */
    CommonResult<WaybillTaskRelevanceVO> confirmCompleted(Long id);

    /**
     * 订单操作日志（根据订单id查看）
     * @param id
     * @return
     */
    CommonResult<List<WaybillTaskRelevanceVO>> lookOperateLog(Long id);


    /**
     * 查询客户订单list
     * @param form
     * @return
     */
    List<OrderInfoVO> findOrderInfoByCustomer(OrderInfoCustomerForm form);

    /**
     * 同步订单(根据南京新智慧api查询运单同步订单)
     * @param form
     * @return
     */
    CommonResult syncOrder(SyncOrderForm form);
}
