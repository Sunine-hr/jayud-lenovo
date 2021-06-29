package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.OrderClearanceFile;
import com.jayud.mall.model.po.OrderCustomsFile;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
     * 获取订单报关文件list
     * @param orderInfo
     * @param offerInfoId
     * @return
     */
    List<OrderCustomsFile> getOrderCustomsFiles(OrderInfo orderInfo, Integer offerInfoId);

    /**
     * 获取订单清关文件list
     * @param orderInfo
     * @param offerInfoId
     * @return
     */
    List<OrderClearanceFile> getOrderClearanceFiles(OrderInfo orderInfo, Integer offerInfoId);

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
    CommonResult<OrderInfoVO> draftCancelOrderInfo(OrderInfoParaForm form);

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
     * web端分页查询订单列表 前端状态统计
     * @param form
     * @return
     */
    Map<String,Long> findOrderInfoDraftCount(QueryOrderInfoForm form);

    /**
     * 后端统计状态数据
     * @param form
     * @return
     */
    Map<String,Long> findOrderInfoAfterCount(QueryOrderInfoForm form);

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

    /**
     * 订单-草稿-提交-进入编辑订单详情(从草稿进去，可以编辑，暂存和提交)给新智慧用的
     * @param form
     * @return
     */
    CommonResult<OrderInfoVO> newEditOrderInfo(OrderInfoNewForm form);

    /**
     * 后台-订单确认
     * @param id
     * @return
     */
    OrderInfoVO afterAffirm(Long id);

    /**
     * 补充资料操作
     * @param form
     */
    void fillMaterial(OrderInfoFillForm form);

    /**
     * 订单-仓库收货(订单箱号收货)
     * @param form
     */
    void orderCaseReceipt(OrderCaseReceiptForm form);

    /**
     * 订单 - 保存物流轨迹
     * @param form
     */
    void saveTrackNotice(OrderTrackNoticeForm form);

    /**
     * 确认计费重信息
     * @param form
     */
    void affirmCounterWeightInfo(IsConfirmBillingForm form);

    /**
     * 查询订单计费重状态
     * @param orderId
     * @return
     */
    IsConfirmBillingVO findOrderIsConfirmBilling(Long orderId);

    /**
     * 查询，订单是否审核单据状态
     * @param orderId
     * @return
     */
    IsAuditOrderVO findOrderIsAuditOrder(Long orderId);

    /**
     * 审核，订单内部状态(是否审核单据)
     * @param form
     */
    void auditOrderIsAuditOrder(IsAuditOrderForm form);

    /**
     * 后台-确认收货
     * @param id
     * @return
     */
    OrderInfoVO affirmReceived(Long id);

    /**
     * 订单-取消按钮前验证
     * @param form
     */
    void cancelStatusVerify(OrderInfoCancelForm form);

    /**
     * 使用新智慧Excel，修改订单箱子的数据
     * @param orderId 订单id
     * @param file 新智慧Excel
     * @return
     */
    OrderInfoVO importExcelUpdateCaseByNewWisdom(Long orderId, MultipartFile file);

    /**
     * 后台-订单签收
     * @param id
     * @return
     */
    OrderInfoVO afterSigned(Long id);
}
