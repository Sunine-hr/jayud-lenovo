package com.jayud.wms.mapper;

import com.jayud.wms.model.bo.WmsInventoryMovementForm;
import com.jayud.wms.model.po.WmsInventoryMovement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.jayud.wms.model.vo.WmsInventoryMovementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存移动订单表 Mapper 接口
 *
 * @author jayud
 * @since 2022-04-11
 */
@Mapper
public interface WmsInventoryMovementMapper extends BaseMapper<WmsInventoryMovement> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: page
     * @param: wmsInventoryMovement
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsInventoryMovement>
     **/
    IPage<WmsInventoryMovementVO> pageList(@Param("page") Page<WmsInventoryMovementForm> page, @Param("wmsInventoryMovement") WmsInventoryMovementForm wmsInventoryMovement);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-11
     * @param: wmsInventoryMovement
     * @return: java.util.List<com.jayud.wms.model.po.WmsInventoryMovement>
     **/
    List<WmsInventoryMovement> list(@Param("wmsInventoryMovement") WmsInventoryMovement wmsInventoryMovement);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-04-11
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryWmsInventoryMovementForExcel(Map<String, Object> paramMap);
}
