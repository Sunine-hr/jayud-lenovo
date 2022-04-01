package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.BaseResult;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.bo.QualityInspectionForm;
import com.jayud.wms.model.bo.QueryQualityInspectionForm;
import com.jayud.wms.model.bo.QueryQualityInspectionMaterialForm;
import com.jayud.wms.model.po.QualityInspection;
import com.jayud.wms.model.vo.QualityInspectionVO;
import com.jayud.wms.model.vo.ReceiptVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 质检检测 服务类
 *
 * @author jyd
 * @since 2021-12-22
 */
public interface IQualityInspectionService extends IService<QualityInspection> {

    /**
    *  分页查询
    * @param queryQualityInspectionForm
    * @param req
    * @return
    */
    IPage<QualityInspectionVO> selectPage(QueryQualityInspectionForm queryQualityInspectionForm,
                                          Integer pageNo,
                                          Integer pageSize,
                                          HttpServletRequest req);

    /**
    *  查询列表
    * @param qualityInspection
    * @return
    */
    List<QualityInspection> selectList(QualityInspection qualityInspection);

    /**
     * 保存(新增+编辑)
     * @param qualityInspection
     */
    QualityInspection saveOrUpdateQualityInspection(QualityInspection qualityInspection);

    /**
     * 逻辑删除
     * @param id
     */
    void delQualityInspection(Long id);

    /**
     * 查询导出
     * @param queryQualityInspectionForm
     * @return
     */
    List<LinkedHashMap<String, Object>> queryQualityInspectionForExcel(QueryQualityInspectionForm queryQualityInspectionForm,
                                                                       HttpServletRequest req);


    void createOrder(QualityInspectionForm form);

    void generateQualityInspection(ReceiptVO details);

    QualityInspectionVO getDetails(Long id);

    void confirm(Long id);

    void transferPendingShelf(Long id);

    //app修改质检单的物料信息
    void copyQualityInspectionMaterial(QueryQualityInspectionMaterialForm queryQualityInspectionMaterialForm);

    /**
     * @description 修改质检人
     * @author  ciro
     * @date   2022/3/31 10:33
     * @param: qcno
     * @return: void
     **/
    void changeQualityUser(Long qcno);

    /**
     * @description 保存质检数据
     * @author  ciro
     * @date   2022/3/31 15:01
     * @param: qualityInspectionVO
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult saveDetail(QualityInspectionVO qualityInspectionVO);

    /**
     * @description 删除质检
     * @author  ciro
     * @date   2022/4/1 11:19
     * @param: deleteForm
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult deleteIds(DeleteForm deleteForm);
}
