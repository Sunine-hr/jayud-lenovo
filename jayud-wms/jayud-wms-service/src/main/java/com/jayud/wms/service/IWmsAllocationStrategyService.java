package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.dto.allocationStrategy.AllocationStrategyDTO;
import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.model.po.WmsAllocationStrategy;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分配策略 服务类
 *
 * @author jyd
 * @since 2022-01-17
 */
public interface IWmsAllocationStrategyService extends IService<WmsAllocationStrategy> {

    /**
    *  分页查询
    * @param wmsAllocationStrategy
    * @param req
    * @return
    */
    IPage<WmsAllocationStrategy> selectPage(WmsAllocationStrategy wmsAllocationStrategy,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsAllocationStrategy
    * @return
    */
    List<WmsAllocationStrategy> selectList(WmsAllocationStrategy wmsAllocationStrategy);



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
    List<LinkedHashMap<String, Object>> queryWmsAllocationStrategyForExcel(Map<String, Object> paramMap);

    /**
     * @description 保存策略
     * @author  ciro
     * @date   2022/1/17 13:45
     * @param: wmsAllocationStrategy
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult saveStrategy(WmsAllocationStrategy wmsAllocationStrategy);




    /**
     * @description 初始化分配策略数据
     * @author  ciro
     * @date   2022/1/18 13:51
     * @param: materialCode     物料编号
     * @param: owerCode         货主编号
     * @param: warehouseCode    仓库编号
     * @return: java.util.List<com.jayud.model.dto..allocationStrategy.AllocationStrategyDTO>
     **/
    BaseResult<List<AllocationStrategyDTO>> initStrategy(String materialCode,String owerCode,String warehouseCode);

    /**
     * @description
     * @author  ciro
     * @date   2022/1/18 14:26
     * @param: strategyList
 * @param: inventoryDetail
     * @return: java.util.List<com.jayud.model.po.InventoryDetail>
     **/
    List<InventoryDetail> getStrategyInventory(List<AllocationStrategyDTO> strategyList,InventoryDetail inventoryDetail);

    /**
     * @description 逻辑删除
     * @author  ciro
     * @date   2022/2/21 16:52
     * @param: id
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult logicDel(Long id);

    /**
     * @description 新增数据
     * @author  ciro
     * @date   2022/2/21 17:01
     * @param: wmsAllocationStrategy
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult saveMsg(WmsAllocationStrategy wmsAllocationStrategy);
}
