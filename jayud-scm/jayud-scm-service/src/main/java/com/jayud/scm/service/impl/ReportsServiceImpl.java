package com.jayud.scm.service.impl;

import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddReportsForm;
import com.jayud.scm.model.po.Reports;
import com.jayud.scm.mapper.ReportsMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.service.IReportsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-17
 */
@Service
public class ReportsServiceImpl extends ServiceImpl<ReportsMapper, Reports> implements IReportsService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public boolean saveOrUpdateReports(AddReportsForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        Reports reports = ConvertUtil.convert(form, Reports.class);
        if(form.getId() != null){
            reports.setCrtBy(systemUser.getId().intValue());
            reports.setCrtByDtm(LocalDateTime.now());
            reports.setCrtByName(systemUser.getUserName());
        }else{
            reports.setMdyBy(systemUser.getId().intValue());
            reports.setMdyByDtm(LocalDateTime.now());
            reports.setMdyByName(systemUser.getUserName());
        }
        boolean result = this.saveOrUpdate(reports);
        if(result){
            log.warn("打印报表增加或修改成功");
        }
        return result;
    }
}
