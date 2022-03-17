package com.jayud.wms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.wms.model.po.MaterialTypeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物料类型 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@Mapper
public interface MaterialTypeInfoMapper extends BaseMapper<MaterialTypeInfo> {

    List<LinkedHashMap<String, Object>> findMaterialTypeInfo(String createBy, String menuType, Long parentId);


    int updateMaterialTypeInfo(MaterialTypeInfo form);

    int insertMaterialTypeInfo(MaterialTypeInfo form);

    //查询一级和子集
    List<LinkedHashMap<String, Object>> findMaterialTypeInfoListStair(Long id, Long parentId);

    //子集查询
    List<LinkedHashMap<String, Object>> findMaterialTypeInfoListTwo(Long id, Long parentId);


    List<LinkedHashMap<String, Object>> findMaterialTypeInfoListIdParentId(String materialTypeName);


    MaterialTypeInfo  findMaterialTypeInfoName(@Param("materialTypeInfo") MaterialTypeInfo materialTypeInfo);

    //    List<LinkedHashMap<String, Object>> findByPage(@Param("form") QueryForm form);  findMaterialTypeInfoListTwoList
//
//    void toExamine(Map<String, Object> map);
//
//    void deApproval(Map<String, Object> map);
    List<LinkedHashMap<String, Object>> findMaterialTypeInfoListFrom(Map<String, Object> paramMap);

    //导出
    List<LinkedHashMap<String, Object>> queryMaterialTypeInfoFormForExcel(Map<String, Object> paramMap);
}
