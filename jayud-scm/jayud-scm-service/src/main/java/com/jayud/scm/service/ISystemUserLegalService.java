package com.jayud.scm.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.po.SystemUserLegal;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since
 */
public interface ISystemUserLegalService extends IService<SystemUserLegal> {

    List<Long> getLegalId(Long id);

    List<String> getLegalName(Long id);
}
