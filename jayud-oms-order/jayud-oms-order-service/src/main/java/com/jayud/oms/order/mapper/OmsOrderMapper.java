package com.jayud.oms.order.mapper;

import com.jayud.oms.order.model.po.OmsOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单管理——订单主表 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-23
 */
@Mapper
public interface OmsOrderMapper extends BaseMapper<OmsOrder> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-23
     * @param: page
     * @param: omsOrder
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.oms.order.model.po.OmsOrder>
     **/
    IPage<OmsOrder> pageList(@Param("page") Page<OmsOrder> page, @Param("omsOrder") OmsOrder omsOrder);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrder
     * @return: java.util.List<com.jayud.oms.order.model.po.OmsOrder>
     **/
    List<OmsOrder> list(@Param("omsOrder") OmsOrder omsOrder);


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
    int logicDel(@Param("id") List<Long> id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-23
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryOmsOrderForExcel(Map<String, Object> paramMap);
}
