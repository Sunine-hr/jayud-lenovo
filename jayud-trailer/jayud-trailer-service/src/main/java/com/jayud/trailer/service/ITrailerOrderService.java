package com.jayud.trailer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.entity.DataControl;
import com.jayud.trailer.bo.*;
import com.jayud.trailer.po.TrailerOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.trailer.vo.TrailerOrderFormVO;
import com.jayud.trailer.vo.TrailerOrderInfoVO;
import com.jayud.trailer.vo.TrailerOrderVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拖车订单表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
public interface ITrailerOrderService extends IService<TrailerOrder> {

    /**
     * 创建拖车单
     *
     * @param addTrailerOrderFrom
     */
    String createOrder(AddTrailerOrderFrom addTrailerOrderFrom);

    boolean isExistOrder(String orderNo);

    /**
     * 根据主订单号获取订单信息
     *
     * @param orderNo
     * @return
     */
    List<TrailerOrder> getByMainOrderNO(String orderNo);

    /**
     * 根据id获取订单信息
     *
     * @param id
     * @return
     */
    TrailerOrderVO getTrailerOrderByOrderNO(Long id);

    /**
     * 根据主订单号集合获取订单信息
     *
     * @param mainOrderNoList
     * @return
     */
    List<TrailerOrder> getTrailerOrderByOrderNOs(List<String> mainOrderNoList);

    /**
     * 分页查询订单信息
     *
     * @param form
     * @return
     */
    IPage<TrailerOrderFormVO> findByPage(QueryTrailerOrderForm form);

    /**
     * 修改流程状态
     *
     * @param trailerOrder1
     * @param form
     */
    void updateProcessStatus(TrailerOrder trailerOrder1, TrailerProcessOptForm form);

    /**
     * 拖车流程操作记录
     */
    void trailerProcessOptRecord(TrailerProcessOptForm form);

    /**
     * 派车操作
     *
     * @param form
     */
    void doTrailerDispatchOpt(TrailerProcessOptForm form);

    /**
     * 订单驳回
     *
     * @param tmp
     * @param auditInfoForm
     * @param trailerCargoRejected
     */
    void orderReceiving(TrailerOrder tmp, AuditInfoForm auditInfoForm, TrailerCargoRejected trailerCargoRejected);

    /**
     * 订单驳回
     *
     * @param tmp
     * @param auditInfoForm
     * @param trailerCargoRejected
     */
    void rejectedOpt(TrailerOrder tmp, AuditInfoForm auditInfoForm, TrailerCargoRejected trailerCargoRejected);

    /**
     * 根据订单号获取订单详情
     *
     * @param orderNo
     * @return
     */
    TrailerOrder getByOrderNO(String orderNo);

    /**
     * 根据主订单号查询所有详情
     *
     * @param mainOrderNos
     * @return
     */
    List<TrailerOrderInfoVO> getTrailerInfoByMainOrderNos(List<String> mainOrderNos);

    List<TrailerOrderInfoVO> getInfo(List<String> mainOrderNos);

    Integer getNumByStatus(String status, DataControl dataControl, Map<String, Object> datas);

    List<TrailerOrder> getByCondition(TrailerOrder setMainOrderNo);

    List<TrailerOrder> getOrdersByOrderNos(List<String> orderNos);

    public List<TrailerOrder> getByLegalEntityId(List<Long> legalIds);
}
