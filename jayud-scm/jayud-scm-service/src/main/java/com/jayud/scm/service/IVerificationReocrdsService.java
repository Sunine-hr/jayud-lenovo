package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddVerificationReocrdsForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.VerificationReocrds;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 核销列表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-08
 */
public interface IVerificationReocrdsService extends IService<VerificationReocrds> {

    boolean writeOff(List<AddVerificationReocrdsForm> form);

    boolean cancelWriteOff(QueryCommonForm form);
}
