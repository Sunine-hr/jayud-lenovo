package com.jayud.trailer.service;

import com.jayud.trailer.po.TrailerDispatch;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
public interface ITrailerDispatchService extends IService<TrailerDispatch> {

    /**
     * 根据拖车id获取派车信息
     * @param id
     * @return
     */
    TrailerDispatch getEnableByTrailerOrderId(Long id);

    /**
     * 保存或修改派车信息
     * @param trailerDispatch
     */
    void saveOrUpdateTrailerDispatch(TrailerDispatch trailerDispatch);
}
