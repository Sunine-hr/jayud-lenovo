package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsMaterialToLoactionRelation;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 物料和库位关系 服务类
 *
 * @author jyd
 * @since 2021-12-16
 */
public interface IWmsMaterialToLoactionRelationService extends IService<WmsMaterialToLoactionRelation> {

    /**
     * 分页查询
     *
     * @param wmsMaterialToLoactionRelation
     * @param req
     * @return
     */
    IPage<WmsMaterialToLoactionRelation> selectPage(WmsMaterialToLoactionRelation wmsMaterialToLoactionRelation,
                                                    HttpServletRequest req);

    /**
     * 查询列表
     *
     * @param wmsMaterialToLoactionRelation
     * @return
     */
    List<WmsMaterialToLoactionRelation> selectList(WmsMaterialToLoactionRelation wmsMaterialToLoactionRelation);

    /**
     * @description 根据物料id查询数据
     * @author ciro
     * @date 2021/12/16 13:38
     * @param: materialId
     * @return: com.jayud.model.vo.WmsMaterialBasicInfoVO
     **/
    WmsMaterialBasicInfoVO queryByMaterialId(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);

    /**
     * @description 保存库位数据
     * @author ciro
     * @date 2021/12/16 14:23
     * @param: wmsMaterialBasicInfoVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult add(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);

    /**
     * @description 根据物料id删除
     * @author ciro
     * @date 2021/12/16 14:41
     * @param: materialId
     * @return: void
     **/
    void delByMaterialId(long materialId);

    /**
     * @description 判断仓库、库区、库位是否推荐
     * @author ciro
     * @date 2022/1/12 15:08
     * @param: warehouseCode        仓库编号
     * @param: warehouseAreaCode    仓区编号
     * @param: locationCode         库位编号
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult checkRecommend(String warehouseCode, String warehouseAreaCode, String locationCode);

    List<WmsMaterialToLoactionRelation> getByCondition(WmsMaterialToLoactionRelation condition);
}
