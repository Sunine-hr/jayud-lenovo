package com.jayud.oauth.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oauth.model.bo.QueryTrainingManagementFrom;
import com.jayud.oauth.model.po.TrainingManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oauth.model.vo.TrainingManagementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 培训管理 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-04-27
 */
@Mapper
public interface TrainingManagementMapper extends BaseMapper<TrainingManagement> {

    IPage<TrainingManagementVO> findByPage(Page<TrainingManagement> page,@Param("form") QueryTrainingManagementFrom form);
}
