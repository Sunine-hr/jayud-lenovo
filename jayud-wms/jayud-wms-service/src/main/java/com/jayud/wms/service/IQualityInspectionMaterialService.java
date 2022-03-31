package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.BaseResult;
import com.jayud.wms.model.bo.QueryQualityInspectionMaterialForm;
import com.jayud.wms.model.po.QualityInspectionMaterial;
import com.jayud.wms.model.vo.QualityInspectionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 质检物料信息 服务类
 *
 * @author jyd
 * @since 2021-12-22
 */
public interface IQualityInspectionMaterialService extends IService<QualityInspectionMaterial> {

    /**
    *  分页查询
    * @param qualityInspectionMaterial
    * @param req
    * @return
    */
    IPage<QualityInspectionMaterial> selectPage(QualityInspectionMaterial qualityInspectionMaterial,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param qualityInspectionMaterial
    * @return
    */
    List<QualityInspectionMaterial> selectList(QualityInspectionMaterial qualityInspectionMaterial);

    /**
     * 保存(新增+编辑)
     * @param qualityInspectionMaterial
     */
    QualityInspectionMaterial saveOrUpdateQualityInspectionMaterial(QualityInspectionMaterial qualityInspectionMaterial);

    /**
     * 逻辑删除
     * @param id
     */
    void delQualityInspectionMaterial(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryQualityInspectionMaterialForExcel(Map<String, Object> paramMap);


    List<QualityInspectionMaterial> getByCondition(QualityInspectionMaterial condition);


    /**
     *   校验查询此订单有没有该物料
     */
    QualityInspectionMaterial findQualityInspectionMaterialOne(QueryQualityInspectionMaterialForm qualityInspectionMaterial);



}
