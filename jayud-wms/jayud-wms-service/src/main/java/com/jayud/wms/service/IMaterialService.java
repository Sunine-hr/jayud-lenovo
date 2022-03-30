package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.BaseResult;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.bo.MaterialForm;
import com.jayud.wms.model.bo.QualityMaterialForm;
import com.jayud.wms.model.po.Material;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 货单物料信息 服务类
 *
 * @author jyd
 * @since 2021-12-21
 */
public interface IMaterialService extends IService<Material> {

    /**
     * 分页查询
     *
     * @param material
     * @param req
     * @return
     */
    IPage<Material> selectPage(Material material,
                               Integer pageNo,
                               Integer pageSize,
                               HttpServletRequest req);

    /**
     * 查询列表
     *
     * @param material
     * @return
     */
    List<Material> selectList(Material material);

    /**
     * 保存(新增+编辑)
     *
     * @param material
     */
    Material saveOrUpdateMaterial(Material material);

    /**
     * 逻辑删除
     *
     * @param id
     */
    void delMaterial(int id);

    /**
     * 查询导出
     *
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryMaterialForExcel(Map<String, Object> paramMap);


    void updateByCondition(Material material, Material condition);

    List<Material> getByCondition(Material condition);

    MaterialForm copyMaterial(MaterialForm form);

    MaterialForm establishMaterial(MaterialForm form);

    /**
     * 根据条件查询 收货单 绑定 物料信息
     */
    List<Material>  findMaterialOne(Material material);

    /**
     * 根据序列编号和收货单号 查询物料信息
     */
    List<Material> findMaterialSNOne(QualityMaterialForm qualityMaterialForm);

    int  updateAllMaterialList(@Param("material") Material material);

    /**
     * @description 确认收货
     * @author  ciro
     * @date   2022/3/29 13:44
     * @param: deleteForm
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult confirmReceipt(DeleteForm deleteForm);

    /**
     * @description 撤销确认收货
     * @author  ciro
     * @date   2022/3/29 14:12
     * @param: deleteForm
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult cancelConfirmReceipt(DeleteForm deleteForm);


    /**
     * @description 确认上架
     * @author  ciro
     * @date   2022/3/29 17:10
     * @param: deleteForm
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult confirmPullShelf(DeleteForm deleteForm);

}
