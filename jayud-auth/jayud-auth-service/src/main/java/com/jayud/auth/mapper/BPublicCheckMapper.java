package com.jayud.auth.mapper;

import com.jayud.auth.model.po.BPublicCheck;
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
 * 审核记录表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-28
 */
@Mapper
public interface BPublicCheckMapper extends BaseMapper<BPublicCheck> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-28
     * @param: page
     * @param: bPublicCheck
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.BPublicCheck>
     **/
    IPage<BPublicCheck> pageList(@Param("page") Page<BPublicCheck> page, @Param("bPublicCheck") BPublicCheck bPublicCheck);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-28
     * @param: bPublicCheck
     * @return: java.util.List<com.jayud.auth.model.po.BPublicCheck>
     **/
    List<BPublicCheck> list(@Param("bPublicCheck") BPublicCheck bPublicCheck);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-02-28
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-02-28
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);

    Map<String, Object> getData(@Param("recordId") Long recordId, @Param("checkTable")String checkTable);

    Map<String, Object> getCheckData(@Param("recordId")Long recordId, @Param("checkTable")String checkTable, @Param("checkDatabase")String checkDatabase);
}