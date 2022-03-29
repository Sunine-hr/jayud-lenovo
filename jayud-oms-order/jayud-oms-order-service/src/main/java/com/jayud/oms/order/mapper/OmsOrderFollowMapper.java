package com.jayud.oms.order.mapper;

import com.jayud.oms.order.model.po.OmsOrderFollow;
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
 * 订单状态跟进表 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-23
 */
@Mapper
public interface OmsOrderFollowMapper extends BaseMapper<OmsOrderFollow> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-23
     * @param: page
     * @param: omsOrderFollow
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.oms.order.model.po.OmsOrderFollow>
     **/
    IPage<OmsOrderFollow> pageList(@Param("page") Page<OmsOrderFollow> page, @Param("omsOrderFollow") OmsOrderFollow omsOrderFollow);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrderFollow
     * @return: java.util.List<com.jayud.oms.order.model.po.OmsOrderFollow>
     **/
    List<OmsOrderFollow> list(@Param("omsOrderFollow") OmsOrderFollow omsOrderFollow);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-23
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryOmsOrderFollowForExcel(Map<String, Object> paramMap);
}
