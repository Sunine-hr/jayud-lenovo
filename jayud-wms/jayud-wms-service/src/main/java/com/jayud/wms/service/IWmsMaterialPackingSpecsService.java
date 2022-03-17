package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsMaterialPackingSpecs;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 物料-包装规格 服务类
 *
 * @author jyd
 * @since 2021-12-16
 */
public interface IWmsMaterialPackingSpecsService extends IService<WmsMaterialPackingSpecs> {

    /**
     * 分页查询
     *
     * @param wmsMaterialPackingSpecs
     * @param req
     * @return
     */
    IPage<WmsMaterialPackingSpecs> selectPage(WmsMaterialPackingSpecs wmsMaterialPackingSpecs,
                                              HttpServletRequest req);

    /**
     * 查询列表
     *
     * @param wmsMaterialPackingSpecs
     * @return
     */
    List<WmsMaterialPackingSpecs> selectList(WmsMaterialPackingSpecs wmsMaterialPackingSpecs);

    /**
     * @description 根据物料id查询
     * @author ciro
     * @date 2021/12/16 16:40
     * @param: wmsMaterialBasicInfoVO
     * @return: com.jayud.model.vo.WmsMaterialBasicInfoVO
     **/
    WmsMaterialBasicInfoVO selectByMaterialId(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);

    /**
     * @description 新增
     * @author ciro
     * @date 2021/12/17 9:11
     * @param: wmsMaterialBasicInfoVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult add(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);

    /**
     * @description 根据物料id删除
     * @author ciro
     * @date 2021/12/17 9:13
     * @param: materialId
     * @return: void
     **/
    void delByMaterialId(long materialId);

    /**
     * @description 获取初始化数据
     * @author ciro
     * @date 2021/12/17 9:33
     * @param:
     * @return: java.util.List<com.jayud.model.po.WmsMaterialPackingSpecs>
     **/
    List<WmsMaterialPackingSpecs> getInitList();

    List<WmsMaterialPackingSpecs> getByCondition(WmsMaterialPackingSpecs condition);

    /**
     * @description 根据物料id获取规格单位
     * @author  ciro
     * @date   2021/12/22 17:08
     * @param: materialId
     * @return: java.util.List<java.lang.String>
     **/
    List<String> getUnitByMaterialId(long materialId);
}
