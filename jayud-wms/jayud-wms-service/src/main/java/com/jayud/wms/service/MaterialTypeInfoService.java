package com.jayud.wms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.MaterialTypeInfoForm;
import com.jayud.wms.model.po.MaterialTypeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 物料类型信息
 * </p>
 *
 */
public interface MaterialTypeInfoService extends IService<MaterialTypeInfo> {

    List<LinkedHashMap<String, Object>> findMaterialTypeInfo(String createBy, Long parentId);

//    List<LinkedHashMap<String, Object>> findMaterialTypeInfoStair(String createBy,String parentId);

    boolean addMaterialTypeInfo(MaterialTypeInfoForm form);

    boolean deleteMaterialTypeInfo(MaterialTypeInfoForm form) ;

    List<LinkedHashMap<String, Object>> findMaterialTypeInfoList(MaterialTypeInfoForm form);

    MaterialTypeInfo findMaterialTypeInfoMaterialTypeName(@Param("materialTypeName") String materialTypeName, @Param("parentId") Long parentId);


    List<LinkedHashMap<String, Object>> findMaterialTypeInfoMaterialTypeNameList(String materialTypeName);


    // 导出
    List<LinkedHashMap<String, Object>> queryMaterialTypeInfoFormForExcel(Map<String, Object> paramMap);

    List<LinkedHashMap<String, Object>> findMaterialTypeInfoMaterialTypeIdOne(Map<String, Object> paramMap);
}
