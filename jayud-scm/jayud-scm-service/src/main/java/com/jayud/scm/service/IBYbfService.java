package com.jayud.scm.service;

import com.jayud.scm.model.po.BYbf;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 * 运保费表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-27
 */
public interface IBYbfService extends IService<BYbf> {

    BYbf getBYbfByDtaTime(LocalDateTime now);
}
