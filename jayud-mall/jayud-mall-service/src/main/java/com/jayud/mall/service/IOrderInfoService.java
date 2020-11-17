package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OrderCaseForm;
import com.jayud.mall.model.bo.OrderInfoCostForm;
import com.jayud.mall.model.bo.QueryOrderInfoForm;
import com.jayud.mall.model.po.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OrderClearanceFileVO;
import com.jayud.mall.model.vo.OrderCustomsFileVO;
import com.jayud.mall.model.vo.OrderInfoVO;

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
     * @param list
     * @return
     */
    CommonResult updateOrderCaseConf(List<OrderCaseForm> list);

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
}
