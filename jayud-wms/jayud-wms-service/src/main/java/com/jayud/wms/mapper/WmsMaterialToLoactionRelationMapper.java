package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsMaterialToLoactionRelation;
import com.jayud.wms.model.vo.WmsMaterialToLoactionRelationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 物料和库位关系 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-16
 */
@Mapper
public interface WmsMaterialToLoactionRelationMapper extends BaseMapper<WmsMaterialToLoactionRelation> {
    /**
    *   分页查询
    */
    IPage<WmsMaterialToLoactionRelation> pageList(@Param("page") Page<WmsMaterialToLoactionRelation> page, @Param("wmsMaterialToLoactionRelation") WmsMaterialToLoactionRelation wmsMaterialToLoactionRelation);

    /**
    *   列表查询
    */
    List<WmsMaterialToLoactionRelation> list(@Param("wmsMaterialToLoactionRelation") WmsMaterialToLoactionRelation wmsMaterialToLoactionRelation);

    /**
     * @description 根据物料id查询
     * @author  ciro
     * @date   2021/12/16 14:00
     * @param: materialId
     * @return: java.util.List<com.jayud.model.vo.WmsMaterialToLoactionRelationVO>
     **/
    List<WmsMaterialToLoactionRelationVO> queryByMaterialId(@Param("materialId") long materialId);

    /**
     * @description 根据物料id和库位id删除
     * @author  ciro
     * @date   2021/12/17 9:13
     * @param: materialId
     * @param: locactionIdList
     * @param: username
     * @return: int
     **/
    int delByLocationIdAndMaterialId(@Param("materialId") long materialId,@Param("locactionIdList") List<String> locactionIdList,@Param("username") String username);

}
