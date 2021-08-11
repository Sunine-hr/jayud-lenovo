package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonPageResult;
import com.jayud.scm.model.bo.QueryCommonConfigForm;
import com.jayud.scm.model.bo.QuerySystemSqlConfigForm;
import com.jayud.scm.model.bo.SystemSqlConfigForm;
import com.jayud.scm.model.po.SystemSqlConfig;
import com.jayud.scm.model.vo.SystemSqlConfigVO;
import com.jayud.scm.model.vo.TableColumnVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SQL数据源配置 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
public interface ISystemSqlConfigService extends IService<SystemSqlConfig> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<SystemSqlConfigVO> findByPage(QuerySystemSqlConfigForm form);

    /**
     * 根据id，查询
     * @param id
     * @return
     */
    SystemSqlConfigVO getSystemSqlConfigById(Integer id);

    /**
     * 保存
     * @param form
     */
    void saveSystemSqlConfig(SystemSqlConfigForm form);

    /**
     * 根据id删除
     * @param id
     */
    void delSystemSqlConfig(Integer id);

    /**
     * 根据SQL代码，获取表格列
     * @param sqlCode
     * @return
     */
    List<TableColumnVO> getTableColumn(String sqlCode);

    /**
     * 根据SQL代码和通用条件分页查询,返回泛型map
     * @param form
     * @return
     */
    CommonPageResult<Map<String, Object>> findCommonByPage(QueryCommonConfigForm form);
}
