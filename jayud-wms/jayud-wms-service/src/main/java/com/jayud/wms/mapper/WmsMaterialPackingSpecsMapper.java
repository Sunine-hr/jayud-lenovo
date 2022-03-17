package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsMaterialPackingSpecs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 物料-包装规格 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-16
 */
@Mapper
public interface WmsMaterialPackingSpecsMapper extends BaseMapper<WmsMaterialPackingSpecs> {
    /**
    *   分页查询
    */
    IPage<WmsMaterialPackingSpecs> pageList(@Param("page") Page<WmsMaterialPackingSpecs> page, @Param("wmsMaterialPackingSpecs") WmsMaterialPackingSpecs wmsMaterialPackingSpecs);

    /**
    *   列表查询
    */
    List<WmsMaterialPackingSpecs> list(@Param("wmsMaterialPackingSpecs") WmsMaterialPackingSpecs wmsMaterialPackingSpecs);

    /**
     * @description 根据物料id删除
     * @author  ciro
     * @date   2021/12/17 9:14
     * @param: materialId
     * @param: username
     * @return: int
     **/
    int delByMaterialId(@Param("materialId") long materialId,@Param("username") String username);

}
