package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddMaintenanceManagementForm;
import com.jayud.oms.model.bo.QueryMaintenanceManagementForm;
import com.jayud.oms.model.po.MaintenanceManagement;
import com.jayud.oms.mapper.MaintenanceManagementMapper;
import com.jayud.oms.model.po.OilCardManagement;
import com.jayud.oms.model.vo.MaintenanceManagementVO;
import com.jayud.oms.service.IMaintenanceManagementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 维修管理 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-10-13
 */
@Service
public class MaintenanceManagementServiceImpl extends ServiceImpl<MaintenanceManagementMapper, MaintenanceManagement> implements IMaintenanceManagementService {

    @Override
    public void saveOrUpdate(AddMaintenanceManagementForm form) {
        MaintenanceManagement tmp = ConvertUtil.convert(form, MaintenanceManagement.class);
        if (tmp.getId() == null) {
            tmp.setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
        } else {
            tmp.setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(tmp);
    }

    @Override
    public IPage<MaintenanceManagementVO> findByPage(QueryMaintenanceManagementForm form) {
        Page<MaintenanceManagementVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<MaintenanceManagementVO> iPage=this.baseMapper.findByPage(page,form);
        return iPage;
    }
}
