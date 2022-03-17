package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.PutawayBackLibraryForm;
import com.jayud.wms.model.bo.QueryWarehouseShelfForm;
import com.jayud.wms.model.po.Workbench;
import com.jayud.wms.model.vo.WarehouseShelfSketchMapVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作台 服务类
 *
 * @author jyd
 * @since 2021-12-17
 */
public interface IWorkbenchService extends IService<Workbench> {

    /**
    *  分页查询
    * @param workbench
    * @param req
    * @return
    */
    IPage<Workbench> selectPage(Workbench workbench,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param workbench
    * @return
    */
    List<Workbench> selectList(Workbench workbench);

    /**
     * 保存(新增+编辑)
     * @param workbench
     */
    Workbench saveOrUpdateWorkbench(Workbench workbench);

    /**
     * 逻辑删除
     * @param id
     */
    void delWorkbench(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWorkbenchForExcel(Map<String, Object> paramMap);


    boolean exitSeedingPositionNumber(String seedingPositionNum, int type);

    /**
     * 工作台收货上架:货架号 查询 货架示意图
     * @param form
     * @return
     */
    List<WarehouseShelfSketchMapVO> selectSketchMapByShelf(QueryWarehouseShelfForm form);

    /**
     * 工作台收货上架:工作台货架回库
     * @param form
     */
    void putawayBackLibrary(PutawayBackLibraryForm form);
}
