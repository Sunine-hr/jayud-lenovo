package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsAllocationStrategyDetail;
import com.jayud.wms.model.vo.WmsAllocationStrategyDetailVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分配策略详情 服务类
 *
 * @author jyd
 * @since 2022-01-17
 */
public interface IWmsAllocationStrategyDetailService extends IService<WmsAllocationStrategyDetail> {

    /**
    *  分页查询
    * @param wmsAllocationStrategyDetailVO
    * @param req
    * @return
    */
    IPage<WmsAllocationStrategyDetailVO> selectPage(WmsAllocationStrategyDetailVO wmsAllocationStrategyDetailVO,
                                                    Integer currentPage,
                                                    Integer pageSize,
                                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsAllocationStrategyDetailVO
    * @return
    */
    List<WmsAllocationStrategyDetailVO> selectList(WmsAllocationStrategyDetailVO wmsAllocationStrategyDetailVO);



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
    List<LinkedHashMap<String, Object>> queryWmsAllocationStrategyDetailForExcel(Map<String, Object> paramMap);


    /**
     * @description 保存分配策略详情
     * @author  ciro
     * @date   2022/1/17 14:07
     * @param: wmsAllocationStrategyDetail
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult saveStrategyDetail(WmsAllocationStrategyDetail wmsAllocationStrategyDetail);

    /**
     * @description 根据策略信息查询
     * @author  ciro
     * @date   2022/1/17 15:50
     * @param: strategyId       分配策略id
     * @param: strategyCode     分配策略编号
     * @return: java.util.List<com.jayud.model.po.WmsAllocationStrategyDetail>
     **/
    List<WmsAllocationStrategyDetail> selectByStrategyMsg(Long strategyId,String strategyCode);

    /**
     * @description 根据策略id删除
     * @author  ciro
     * @date   2022/1/20 11:17
     * @param: strategyId
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult delByStrategyId(long strategyId);

    /**
     * @description 根据策略id查询详情
     * @author  ciro
     * @date   2022/2/21 16:56
     * @param: strategyId
     * @return: java.util.List<com.jayud.model.vo.WmsAllocationStrategyDetailVO>
     **/
    List<WmsAllocationStrategyDetailVO> selectByStrategyId(Long strategyId);
}
