package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.InventoryCheck;
import com.jayud.wms.model.vo.InventoryCheckAppVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存盘点表 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-27
 */
@Mapper
public interface InventoryCheckMapper extends BaseMapper<InventoryCheck> {
    /**
    *   分页查询
    */
    IPage<InventoryCheck> pageList(@Param("page") Page<InventoryCheck> page, @Param("inventoryCheck") InventoryCheck inventoryCheck);

    /**
    *   列表查询
    */
    List<InventoryCheck> list(@Param("inventoryCheck") InventoryCheck inventoryCheck);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryInventoryCheckForExcel(Map<String, Object> paramMap);

    /**
     * 分页查询数据Feign
     * @param page
     * @param inventoryCheck
     * @return
     */
    IPage<InventoryCheckAppVO> selectPageByFeign(@Param("page") Page<InventoryCheckAppVO> page, @Param("inventoryCheck") InventoryCheck inventoryCheck);

    /**
     * 根据盘点单号，查询盘点明细，给app确认
     * @param checkCode
     * @return
     */
    InventoryCheckAppVO queryInventoryCheckByCheckCode(@Param("checkCode") String checkCode);
}
