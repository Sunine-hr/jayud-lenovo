package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.TransportWayForm;
import com.jayud.mall.model.po.TransportWay;
import com.jayud.mall.model.vo.TransportWayVO;

import java.util.List;

/**
 * <p>
 * 运输方式 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface ITransportWayService extends IService<TransportWay> {

    /**
     * 查询运输方式list
     * @param form
     * @return
     */
    List<TransportWayVO> findTransportWay(TransportWayForm form);
}
