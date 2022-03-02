package com.jayud.crm.mapper;

import com.jayud.crm.model.po.CrmFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_文件(crm_file) Mapper 接口
 *
 * @author jayud
 * @since 2022-03-02
 */
@Mapper
public interface CrmFileMapper extends BaseMapper<CrmFile> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: page
     * @param: crmFile
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmFile>
     **/
    IPage<CrmFile> pageList(@Param("page") Page<CrmFile> page, @Param("crmFile") CrmFile crmFile);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-02
     * @param: crmFile
     * @return: java.util.List<com.jayud.crm.model.po.CrmFile>
     **/
    List<CrmFile> list(@Param("crmFile") CrmFile crmFile);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-02
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmFileForExcel(Map<String, Object> paramMap);
}
