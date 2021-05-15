package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.oms.mapper.AuditInfoMapper;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.service.IAuditInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 审核信息记录表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
@Service
public class AuditInfoServiceImpl extends ServiceImpl<AuditInfoMapper, AuditInfo> implements IAuditInfoService {

    /**
     * 保存/修改审核记录表
     */
    @Override
    public boolean saveOrUpdateAuditInfo(AuditInfo info) {
        if (Objects.isNull(info.getId())) {
            info.setCreatedTime(LocalDateTime.now())
                    .setCreatedUser(UserOperator.getToken());
            return this.save(info);
        } else {

            return this.updateById(info);
        }
    }

    /**
     * 根据外键id查询最新的审核信息
     */
    @Override
    public AuditInfo getAuditInfoLatestByExtId(Long id, String tableDesc) {
        return this.baseMapper.getLatestByExtId(id, tableDesc);
    }

    /**
     * 根据驳回状态集合查询最新的驳回信息
     */
    @Override
    public AuditInfo getLatestByRejectionStatus(Long orderId, String tableDesc, String... status) {
        QueryWrapper<AuditInfo> condition = new QueryWrapper<>();
        condition.lambda().in(AuditInfo::getAuditStatus, status);
        condition.lambda().eq(AuditInfo::getExtId, orderId).eq(AuditInfo::getExtDesc, tableDesc);
        condition.lambda().orderByDesc(AuditInfo::getId);
        List<AuditInfo> auditInfos = this.baseMapper.selectList(condition);
        return auditInfos.size() > 0 ? auditInfos.get(0) : new AuditInfo();
    }

    @Override
    public List<Map<String, Object>> getByExtUniqueFlag(List<String> extUniqueFlags) {
        return this.baseMapper.getByExtUniqueFlag(extUniqueFlags);
    }


}
