package com.jayud.trailer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.trailer.bo.AddTrailerOrderFrom;
import com.jayud.trailer.bo.QueryTrailerOrderForm;
import com.jayud.trailer.bo.TrailerProcessOptForm;
import com.jayud.trailer.po.TrailerOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.trailer.vo.TrailerOrderFormVO;
import com.jayud.trailer.vo.TrailerOrderVO;

import java.util.List;

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
     * @param addTrailerOrderFrom
     */
    void createOrder(AddTrailerOrderFrom addTrailerOrderFrom);

    /**
     * 获取拖车订单号
     * @param legalId
     * @param integer
     * @return
     */
    String generationOrderNo(Long legalId, Integer integer);

    boolean isExistOrder(String orderNo);

    /**
     * 根据主订单号获取订单信息
     * @param orderNo
     * @return
     */
    TrailerOrder getByMainOrderNO(String orderNo);

    /**
     * 根据id获取订单信息
     * @param id
     * @return
     */
    TrailerOrderVO getTrailerOrderByOrderNO(Long id);

    /**
     * 根据主订单号集合获取订单信息
     * @param mainOrderNoList
     * @return
     */
    List<TrailerOrder> getTrailerOrderByOrderNOs(List<String> mainOrderNoList);

    /**
     * 分页查询订单信息
     * @param form
     * @return
     */
    IPage<TrailerOrderFormVO> findByPage(QueryTrailerOrderForm form);

    /**
     * 修改流程状态
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
     * @param form
     */
    void doTrailerDispatchOpt(TrailerProcessOptForm form);
}
