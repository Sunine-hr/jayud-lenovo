package com.jayud.mall.service;

import com.jayud.mall.model.po.ClearingWay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.ClearingWayVO;

import java.util.List;

/**
 * <p>
 * 基础表-结算方式 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-26
 */
public interface IClearingWayService extends IService<ClearingWay> {

    /**
     * 查询结算方式list
     * @return
     */
    List<ClearingWayVO> findClearingWay();
}
