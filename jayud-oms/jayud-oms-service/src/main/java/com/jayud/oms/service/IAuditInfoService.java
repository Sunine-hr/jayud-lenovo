package com.jayud.oms.service;

import com.jayud.oms.model.po.AuditInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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
    AuditInfo getAuditInfoLatestByExtId(Long id, String tableDesc);

    /**
     * 根据驳回状态集合查询最新的驳回信息
     */
    public AuditInfo getLatestByRejectionStatus(Long orderId, String tableDesc, String... status);

    List<Map<String, Object>> getByExtUniqueFlag(List<String> extUniqueFlags);
}
