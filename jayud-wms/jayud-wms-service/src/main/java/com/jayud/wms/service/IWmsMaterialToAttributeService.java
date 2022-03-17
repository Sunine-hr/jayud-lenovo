package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsMaterialToAttribute;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料管理-批属性配置 服务类
 *
 * @author jyd
 * @since 2022-01-12
 */
public interface IWmsMaterialToAttributeService extends IService<WmsMaterialToAttribute> {

    /**
    *  分页查询
    * @param wmsMaterialToAttribute
    * @param req
    * @return
    */
    IPage<WmsMaterialToAttribute> selectPage(WmsMaterialToAttribute wmsMaterialToAttribute,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsMaterialToAttribute
    * @return
    */
    List<WmsMaterialToAttribute> selectList(WmsMaterialToAttribute wmsMaterialToAttribute);



    /**
     * 物理删除
     * @param id
     */
    void phyDelById(int id);


    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsMaterialToAttributeForExcel(Map<String, Object> paramMap);


    /**
     * @description 根据物料id查询
     * @author  ciro
     * @date   2022/1/12 10:29
     * @param: wmsMaterialBasicInfoVO
     * @return: com.jayud.model.vo.WmsMaterialBasicInfoVO
     **/
    WmsMaterialBasicInfoVO selectByMaterialId(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);

    /**
     * @description 保存数据
     * @author  ciro
     * @date   2022/1/12 10:50
     * @param: wmsMaterialBasicInfoVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult add(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);

    /**
     * @description 根据物料id删除
     * @author  ciro
     * @date   2022/1/18 11:30
     * @param: materialId
     * @return: void
     **/
    void delByMaterialId(long materialId);

    /**
     * @description 初始化批属性
     * @author  ciro
     * @date   2022/1/18 14:56
     * @param:
     * @return: java.util.List<com.jayud.model.po.WmsMaterialToAttribute>
     **/
    List<WmsMaterialToAttribute> initAttribute();

}
