package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysDictItem;
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
 * 字典项 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-23
 */
@Mapper
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-23
     * @param: page
     * @param: sysDictItem
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysDictItem>
     **/
    IPage<SysDictItem> pageList(@Param("page") Page<SysDictItem> page, @Param("sysDictItem") SysDictItem sysDictItem);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-23
     * @param: sysDictItem
     * @return: java.util.List<com.jayud.auth.model.po.SysDictItem>
     **/
    List<SysDictItem> list(@Param("sysDictItem") SysDictItem sysDictItem);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-02-23
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-02-23
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);

    /**
     * @description 根据字典编码查询子项
     * @author  ciro
     * @date   2022/2/23 9:30
     * @param: dictCode
     * @return: java.util.List<com.jayud.auth.model.po.SysDictItem>
     **/
    List<SysDictItem> selectItemByDictCode(@Param("dictCode") String dictCode);
}
