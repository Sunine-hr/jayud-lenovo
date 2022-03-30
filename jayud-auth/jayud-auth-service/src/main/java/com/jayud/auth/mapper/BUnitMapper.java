package com.jayud.auth.mapper;

import com.jayud.auth.model.po.BUnit;
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
 * 计量单位代码表 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-25
 */
@Mapper
public interface BUnitMapper extends BaseMapper<BUnit> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-25
     * @param: page
     * @param: bUnit
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.BUnit>
     **/
    IPage<BUnit> pageList(@Param("page") Page<BUnit> page, @Param("bUnit") BUnit bUnit);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-25
     * @param: bUnit
     * @return: java.util.List<com.jayud.auth.model.po.BUnit>
     **/
    List<BUnit> list(@Param("bUnit") BUnit bUnit);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-25
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-25
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-25
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryBUnitForExcel(Map<String, Object> paramMap);

    /**
     * 获取单位下拉
     * @return
     */
    List<String> getUnits();
}
