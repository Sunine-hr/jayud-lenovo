package com.jayud.oceanship.service;

import com.jayud.common.entity.InitComboxVO;
import com.jayud.oceanship.po.CabinetSize;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 柜型大小表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-02-19
 */
public interface ICabinetSizeService extends IService<CabinetSize> {

    List<InitComboxVO> initCabinetSize();
}
