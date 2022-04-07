package com.jayud.wms.mapper;

import com.jayud.wms.model.po.WmsOutboundOrderInfoToService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库单-附加服务 Mapper 接口
 *
 * @author jayud
 * @since 2022-04-06
 */
@Mapper
public interface WmsOutboundOrderInfoToServiceMapper extends BaseMapper<WmsOutboundOrderInfoToService> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-06
     * @param: page
     * @param: wmsOutboundOrderInfoToService
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsOutboundOrderInfoToService>
     **/
    IPage<WmsOutboundOrderInfoToService> pageList(@Param("page") Page<WmsOutboundOrderInfoToService> page, @Param("wmsOutboundOrderInfoToService") WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-06
     * @param: wmsOutboundOrderInfoToService
     * @return: java.util.List<com.jayud.wms.model.po.WmsOutboundOrderInfoToService>
     **/
    List<WmsOutboundOrderInfoToService> list(@Param("wmsOutboundOrderInfoToService") WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-04-06
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-04-06
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-04-06
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoToServiceForExcel(Map<String, Object> paramMap);

    /**
     * @description 根据id逻辑删除
     * @author  ciro
     * @date   2022/4/6 16:17
     * @param: idList
     * @param: username
     * @return: int
     **/
    int logicDelByIds(@RequestParam("idList") List<String> idList,@RequestParam("username") String username);
}
