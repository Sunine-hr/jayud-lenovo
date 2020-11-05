package com.jayud.oms.service;

import com.jayud.oms.model.po.AuditInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审核信息记录表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
public interface IAuditInfoService extends IService<AuditInfo> {

    /**
     * 保存/修改审核记录表
     */
    boolean saveOrUpdateAuditInfo(AuditInfo info);

    /**
     * 根据外键id查询最新的审核信息
     */
    AuditInfo getAuditInfoLatestByExtId(Long id,String tableDesc);

}
