package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryTransportForm;
import com.jayud.mall.model.bo.TransportForm;
import com.jayud.mall.model.bo.TransportParaForm;
import com.jayud.mall.model.po.Transport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.model.vo.TransportVO;

import java.util.List;

/**
 * <p>
 * 运输管理表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-06
 */
public interface ITransportService extends IService<Transport> {

    /**
     * 分页查询运输单号
     * @param form
     * @return
     */
    IPage<TransportVO> findTransportByPage(QueryTransportForm form);

    /**
     * 拼车提货-创建展示运输单号、提货信息、送货信息
     * @param form
     * @return
     */
    CommonResult<TransportVO> createTransport(List<OrderPickVO> form);

    /**
     * 拼车提货-确认
     * @param form
     * @return
     */
    CommonResult<TransportVO> affirmTransport(TransportForm form);

    /**
     * 运输管理-编辑(查询展示)
     * @param form
     * @return
     */
    CommonResult<TransportVO> findTransport(TransportParaForm form);

    /**
     * 运输管理，编辑确认
     * @param from
     * @return
     */
    CommonResult<TransportVO> editAffirmTransport(TransportForm from);

    /**
     * 运输管理-确认送达
     * @param form
     * @return
     */
    CommonResult confirmDelivery(TransportParaForm form);

    /**
     * 根据运输单id，查询运输单信息
     * @param id
     * @return
     */
    TransportVO findTransportById(Long id);
}
