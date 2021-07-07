package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.OrderConf;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.model.vo.OrderConfCasesVO;
import com.jayud.mall.model.vo.OrderConfVO;

import java.util.List;

/**
 * <p>
 * 配载单 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
public interface IOrderConfService extends IService<OrderConf> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<OrderConfVO> findOrderConfByPage(QueryOrderConfForm form);

    /**
     * 保存配载单
     * @param form
     * @return
     */
    OrderConfVO saveOrderConf(OrderConfForm form);

    /**
     * 查看配载详情
     * @param id
     * @return
     */
    CommonResult<OrderConfVO> lookOrderConf(Long id);

    /**
     * 配载，关联报价
     * @param form
     * @return
     */
    OrderConfVO saveOrderConfByOfferInfo(OrderConfForm form);

    /**
     * 配载，关联提单
     * @param form
     * @return
     */
    OrderConfVO saveOrderConfByOceanBill(OrderConfForm form);

    /**
     * 新增编辑查询配载：提单、报价、运单(订单)
     * @param id
     * @return
     */
    OrderConfVO findOrderConfById(Long id);

    /**
     * 新增配载
     * @param form
     * @return
     */
    OrderConfVO addOrderConf(OrderConfForm form);

    /**
     * 根据配载id，查询提单list
     * @param id
     * @return
     */
    List<OceanBillVO> findOceanBillByConfId(Long id);

    /**
     * 配载单-启用按钮
     * @param form
     */
    void enableStatus(OrderConfIdForm form);

    /**
     * 配载单-取消按钮
     * @param form
     */
    void cancelStatus(OrderConfIdForm form);

    /**
     * 配载单-开始配载按钮
     * @param form
     */
    void startAutostowStatus(OrderConfIdForm form);

    /**
     * 配载单-转运中按钮
     * @param form
     */
    void transitStatus(OrderConfIdForm form);

    /**
     * 配载单-完成按钮
     * @param form
     */
    void finishStatus(OrderConfIdForm form);

    /**
     * 配载单-取消按钮前验证
     * @param form
     */
    void cancelStatusVerify(OrderConfVerifyForm form);

    /**
     * 配载统计箱数(总箱数、未配载箱数)
     * @param id
     * @return
     */
    OrderConfCasesVO statisticsCases(Long id);

    void relevanceOfferInfo(OrderConfOfferInfoForm form);

    /**
     * 查询配载订单下的箱子(配载 留仓)
     * @param orderId
     * @return
     */
    List<OrderCaseVO> findOrderCaseByOrderId(Long orderId);
}
