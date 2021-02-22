package com.jayud.oceanship.service;

import com.jayud.oceanship.po.CabinetType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oceanship.vo.CabinetTypeVO;

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

    List<CabinetTypeVO> initCabinetType();
}
