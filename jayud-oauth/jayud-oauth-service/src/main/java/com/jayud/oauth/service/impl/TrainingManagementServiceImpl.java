package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oauth.model.bo.AddTrainingManagementForm;
import com.jayud.oauth.model.bo.QueryTrainingManagementFrom;
import com.jayud.oauth.model.po.TrainingManagement;
import com.jayud.oauth.mapper.TrainingManagementMapper;
import com.jayud.oauth.model.vo.TrainingManagementVO;
import com.jayud.oauth.service.ITrainingManagementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 培训管理 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-04-27
 */
@Service
public class TrainingManagementServiceImpl extends ServiceImpl<TrainingManagementMapper, TrainingManagement> implements ITrainingManagementService {

    @Override
    public void addOrUpdate(AddTrainingManagementForm form) {
        TrainingManagement tmp = ConvertUtil.convert(form, TrainingManagement.class);
        tmp.setFileName(StringUtils.getFileNameStr(form.getFileViewList()))
                .setFilePath(StringUtils.getFileStr(form.getFileViewList()));
        if (tmp.getId() == null) {
            tmp.setCreateUser(UserOperator.getToken())
                    .setCreateTime(LocalDateTime.now());
        } else {
            tmp.setUpdateTime(LocalDateTime.now());
            tmp.setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(tmp);
    }


    @Override
    public IPage<TrainingManagementVO> findByPage(QueryTrainingManagementFrom form) {
        Page<TrainingManagement> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }
}
