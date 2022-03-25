package com.jayud.auth.mapper;

import com.jayud.auth.model.po.AirPort;
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
 * 空运港口表 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-24
 */
@Mapper
public interface AirPortMapper extends BaseMapper<AirPort> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-24
     * @param: page
     * @param: airPort
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.AirPort>
     **/
    IPage<AirPort> pageList(@Param("page") Page<AirPort> page, @Param("airPort") AirPort airPort);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-24
     * @param: airPort
     * @return: java.util.List<com.jayud.auth.model.po.AirPort>
     **/
    List<AirPort> list(@Param("airPort") AirPort airPort);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-24
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-24
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-24
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryAirPortForExcel(Map<String, Object> paramMap);
}
