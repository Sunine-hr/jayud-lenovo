package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryTransportForm;
import com.jayud.mall.model.po.Transport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.TransportVO;

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
}
