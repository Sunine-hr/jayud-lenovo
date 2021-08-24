package com.jayud.scm.service;

import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.po.CheckOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 提验货主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
public interface ICheckOrderService extends IService<CheckOrder> {

    boolean delete(DeleteForm deleteForm);
}
