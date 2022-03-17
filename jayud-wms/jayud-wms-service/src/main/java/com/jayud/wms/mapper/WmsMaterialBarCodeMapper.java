package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsMaterialBarCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 物料-条码 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-16
 */
@Mapper
public interface WmsMaterialBarCodeMapper extends BaseMapper<WmsMaterialBarCode> {
    /**
    *   分页查询
    */
    IPage<WmsMaterialBarCode> pageList(@Param("page") Page<WmsMaterialBarCode> page, @Param("wmsMaterialBarCode") WmsMaterialBarCode wmsMaterialBarCode);

    /**
    *   列表查询
    */
    List<WmsMaterialBarCode> list(@Param("wmsMaterialBarCode") WmsMaterialBarCode wmsMaterialBarCode);

    /**
     * @description 根据物料id和编码删除
     * @author  ciro
     * @date   2021/12/17 9:00
     * @param: materialId
     * @param: codeList
     * @param: username
     * @return: int
     **/
    int delByMaterialIdAndCode(@Param("materialId") long materialId,@Param("codeList") List<String> codeList,@Param("username") String username);
}
