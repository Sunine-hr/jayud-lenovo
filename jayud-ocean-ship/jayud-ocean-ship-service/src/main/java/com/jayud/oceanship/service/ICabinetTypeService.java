package com.jayud.oceanship.service;

import com.jayud.common.entity.InitComboxVO;
import com.jayud.oceanship.vo.CabinetType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 柜型表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-02-05
 */
public interface ICabinetTypeService extends IService<CabinetType> {

    List<InitComboxVO> initCabinetType();
}
