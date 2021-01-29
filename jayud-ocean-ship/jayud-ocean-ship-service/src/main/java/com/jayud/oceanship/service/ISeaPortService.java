package com.jayud.oceanship.service;

import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.oceanship.model.po.SeaPort;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 船港口地址表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-29
 */
public interface ISeaPortService extends IService<SeaPort> {

    /**
     * 获取港口列表
     * @return
     */
    List<InitComboxStrVO> initSeaPort();
}
