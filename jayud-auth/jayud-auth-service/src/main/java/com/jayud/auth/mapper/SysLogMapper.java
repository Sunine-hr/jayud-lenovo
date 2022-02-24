package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysLog;
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
 * 系统日志表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-24
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-24
     * @param: page
     * @param: sysLog
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysLog>
     **/
    IPage<SysLog> pageList(@Param("page") Page<SysLog> page, @Param("sysLog") SysLog sysLog);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-24
     * @param: sysLog
     * @return: java.util.List<com.jayud.auth.model.po.SysLog>
     **/
    List<SysLog> list(@Param("sysLog") SysLog sysLog);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-02-24
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-02-24
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);
}
