package com.jayud.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.QuerySystemSqlConfigForm;
import com.jayud.auth.model.po.SystemSqlConfig;
import com.jayud.auth.model.vo.SystemSqlConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

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

    /**
     * 根据SQL代码和通用条件分页查询,返回泛型map
     * @param page 分页
     * @param paraMap 条件参数
     *                 paraMap.put("sqlStr", sqlStr);   SQL语句
     *                 paraMap.put("wehre", wehre);     WHERE语句
     *                 paraMap.put("countSql", countSql);   汇总SQL
     * @return
     */
    IPage<Map<String, Object>> findCommonByPage(@Param("page") Page<Map<String, Object>> page, @Param("paraMap") Map<String, Object> paraMap);

    /**
     * 根据SQL代码和通用条件查询汇总数据,返回泛型map
     * @param paraMap 条件参数
     *                 paraMap.put("sqlStr", sqlStr);   SQL语句
     *                 paraMap.put("wehre", wehre);     WHERE语句
     *                 paraMap.put("countSql", countSql);   汇总SQL
     * @return
     */
    Map<String, Object> findCommonCount(@Param("paraMap") Map<String, Object> paraMap);
}
