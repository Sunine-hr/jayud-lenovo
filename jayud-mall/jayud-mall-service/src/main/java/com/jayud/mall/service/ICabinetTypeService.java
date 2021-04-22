package com.jayud.mall.service;

import com.jayud.mall.model.bo.CabinetTypeForm;
import com.jayud.mall.model.po.CabinetType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.CabinetTypeVO;

import java.util.List;

/**
 * <p>
 * 柜型基本信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-01-07
 */
public interface ICabinetTypeService extends IService<CabinetType> {

    /**
     * 柜型基本信息list
     * @param form
     * @return
     */
    List<CabinetTypeVO> findCabinetType(CabinetTypeForm form);
}
