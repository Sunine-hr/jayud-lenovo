package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QuerySystemSqlConfigForm;
import com.jayud.scm.model.po.SystemSqlConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.SystemSqlConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * SQL数据源配置 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Mapper
public interface SystemSqlConfigMapper extends BaseMapper<SystemSqlConfig> {

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<SystemSqlConfigVO> findByPage(@Param("page") Page<SystemSqlConfigVO> page, @Param("form") QuerySystemSqlConfigForm form);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SystemSqlConfigVO getSystemSqlConfigById(@Param("id") Integer id);

    /**
     * 根据SQL代码查询
     * @param sqlCode
     * @return
     */
    SystemSqlConfigVO getSystemSqlConfigBySqlCode(@Param("sqlCode") String sqlCode);
}
