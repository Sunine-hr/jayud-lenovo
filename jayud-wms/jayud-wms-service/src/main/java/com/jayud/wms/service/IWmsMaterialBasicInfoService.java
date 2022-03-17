package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsMaterialBasicInfo;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 物料基本信息 服务类
 *
 * @author jyd
 * @since 2021-12-16
 */
public interface IWmsMaterialBasicInfoService extends IService<WmsMaterialBasicInfo> {

        /**
         *  分页查询
         * @param wmsMaterialBasicInfoVO
         * @param req
         * @return
         */
        IPage<WmsMaterialBasicInfoVO> selectPage(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO,
                                                 Integer currentPage,
                                                 Integer pageSize,
                                                 HttpServletRequest req);

    /**
     * 查询列表
     *
     * @param wmsMaterialBasicInfoVO
     * @return
     */
    List<WmsMaterialBasicInfoVO> selectList(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);
    /**
     * 查询列表
     *
     * @param wmsMaterialBasicInfoVO
     * @return
     */
    /**
     * @description 查询列表
     * @author  ciro
     * @date   2022/1/26 10:52
     * @param: wmsMaterialBasicInfoVO
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.model.vo.WmsMaterialBasicInfoVO>
     **/
    IPage<WmsMaterialBasicInfoVO> selectWmsMaterialBasicInfoVOListList(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO,
                                                                      Integer currentPage,
                                                                      Integer pageSize,
                                                                      HttpServletRequest req);

    /**
     * @description 根据id查询
     * @author ciro
     * @date 2021/12/16 11:19
     * @param: id
     * @return: com.jayud.model.vo.WmsMaterialBasicInfoVO
     **/
    WmsMaterialBasicInfoVO selectById(long id);

    /**
     * @description 根据id删除
     * @author ciro
     * @date 2021/12/16 15:14
     * @param: id
     * @return: void
     **/
    void delByMaterialId(long id);

    /**
     * @description 保存
     * @author ciro
     * @date 2021/12/17 10:14
     * @param: wmsMaterialBasicInfoVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult add(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);

    /**
     * 根据条件查询物料基本信息
     * @param condition
     * @return
     */
    List<WmsMaterialBasicInfo> getByCondition(WmsMaterialBasicInfo condition);

    /**
     * 获取入库物料超收策略
     * @param materialCodes
     * @return
     */
    Map<String, BigDecimal> getOverchargePolicy(List<String> materialCodes);

    /**
     * @description 根据编码查询
     * @author  ciro
     * @date   2021/12/30 18:06
     * @param: materialCode
     * @return: com.jayud.model.vo.WmsMaterialBasicInfoVO
     **/
    WmsMaterialBasicInfoVO selectByCode(String materialCode);

    /**
     * @description 判断仓库、库区是否推荐
     * @author  ciro
     * @date   2022/1/12 14:56
     * @param: warehouseCode    仓库编号
     * @param: warehouseAreaCode    库区编号
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult checkRecommend(String warehouseCode,String warehouseAreaCode);

    List<WmsMaterialBasicInfo> getByMaterialCodes(Set<String> materialCodes);

    WmsMaterialBasicInfo getByMaterialCode(String materialCode);
}
