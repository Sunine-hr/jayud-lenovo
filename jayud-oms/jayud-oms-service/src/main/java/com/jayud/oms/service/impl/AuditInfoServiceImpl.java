package com.jayud.oms.service.impl;

import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.mapper.AuditInfoMapper;
import com.jayud.oms.service.IAuditInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
