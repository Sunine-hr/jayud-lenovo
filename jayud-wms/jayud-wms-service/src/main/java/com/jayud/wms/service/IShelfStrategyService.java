package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.ShelfStrategy;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 上架策略 服务类
 *
 * @author jyd
 * @since 2022-01-13
 */
public interface IShelfStrategyService extends IService<ShelfStrategy> {

    /**
    *  分页查询
    * @param shelfStrategy
    * @param req
    * @return
    */
    IPage<ShelfStrategy> selectPage(ShelfStrategy shelfStrategy,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param shelfStrategy
    * @return
    */
    List<ShelfStrategy> selectList(ShelfStrategy shelfStrategy);

    /**
     * 保存(新增+编辑)
     * @param shelfStrategy
     */
    ShelfStrategy saveOrUpdateShelfStrategy(ShelfStrategy shelfStrategy);

    /**
     * 逻辑删除
     * @param id
     */
    void delShelfStrategy(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryShelfStrategyForExcel(Map<String, Object> paramMap);


    Map<String, String> getPolicyPrioritys(Set<String> materialCodes);

    Map<String, String> getPolicyPriority(String materialCode, Long warehouseId);
}
