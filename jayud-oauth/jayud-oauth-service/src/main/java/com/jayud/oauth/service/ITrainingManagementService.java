package com.jayud.oauth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oauth.model.bo.AddTrainingManagementForm;
import com.jayud.oauth.model.bo.QueryTrainingManagementFrom;
import com.jayud.oauth.model.po.TrainingManagement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oauth.model.vo.TrainingManagementVO;

import java.util.List;

/**
 * <p>
 * 培训管理 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-04-27
 */
public interface ITrainingManagementService extends IService<TrainingManagement> {

    void addOrUpdate(AddTrainingManagementForm form);

    IPage<TrainingManagementVO> findByPage(QueryTrainingManagementFrom form);

    List<TrainingManagementVO> getInfoLastWeek();

}
