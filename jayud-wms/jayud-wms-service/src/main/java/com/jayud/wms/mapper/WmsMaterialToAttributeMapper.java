package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsMaterialToAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料管理-批属性配置 Mapper 接口
 *
 * @author jyd
 * @since 2022-01-12
 */
@Mapper
public interface WmsMaterialToAttributeMapper extends BaseMapper<WmsMaterialToAttribute> {
    /**
    *   分页查询
    */
    IPage<WmsMaterialToAttribute> pageList(@Param("page") Page<WmsMaterialToAttribute> page, @Param("wmsMaterialToAttribute") WmsMaterialToAttribute wmsMaterialToAttribute);

    /**
    *   列表查询
    */
    List<WmsMaterialToAttribute> list(@Param("wmsMaterialToAttribute") WmsMaterialToAttribute wmsMaterialToAttribute);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsMaterialToAttributeForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 根据物料id删除
     * @author  ciro
     * @date   2022/1/18 11:29
     * @param: materialId
     * @param: username
     * @return: int
     **/
    int delByMaterialId(@Param("materialId") long materialId,@Param("username") String username);
}
