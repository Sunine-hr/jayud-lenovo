package com.jayud.oms.order.mapper;

import com.jayud.oms.order.model.po.OmsOrderEntry;
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
 * 订单管理-订单明细表 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-23
 */
@Mapper
public interface OmsOrderEntryMapper extends BaseMapper<OmsOrderEntry> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-23
     * @param: page
     * @param: omsOrderEntry
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.oms.order.model.po.OmsOrderEntry>
     **/
    IPage<OmsOrderEntry> pageList(@Param("page") Page<OmsOrderEntry> page, @Param("omsOrderEntry") OmsOrderEntry omsOrderEntry);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrderEntry
     * @return: java.util.List<com.jayud.oms.order.model.po.OmsOrderEntry>
     **/
    List<OmsOrderEntry> list(@Param("omsOrderEntry") OmsOrderEntry omsOrderEntry);


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
    List<LinkedHashMap<String, Object>> queryOmsOrderEntryForExcel(Map<String, Object> paramMap);
}
