package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddSystemActionForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.SystemAction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.SystemActionOutVO;
import com.jayud.scm.model.vo.SystemActionVO;

import java.util.List;

/**
 * <p>
 * 按钮权限设置表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
public interface ISystemActionService extends IService<SystemAction> {

    IPage<SystemActionVO> findByPage(QueryForm form);

    SystemAction getSystemActionByActionCode(String actionCode);

    boolean saveOrUpdateSystemAction(AddSystemActionForm form);

    boolean delete(DeleteForm deleteForm);

    List<SystemAction> getSystemActionByIsAudit(Long id);

    List<SystemAction> getSystemActionList(Long id);

    SystemActionOutVO getSystemActionById(Integer id);
}
