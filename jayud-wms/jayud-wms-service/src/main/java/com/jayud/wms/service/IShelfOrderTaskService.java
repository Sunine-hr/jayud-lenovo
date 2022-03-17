package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.po.ShelfOrderTask;
import com.jayud.wms.model.vo.ReceiptVO;
import com.jayud.wms.model.vo.ShelfOrderTaskVO;
import com.jayud.wms.model.vo.WarehouseLocationVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 上架任务单 服务类
 *
 * @author jyd
 * @since 2021-12-23
 */
public interface IShelfOrderTaskService extends IService<ShelfOrderTask> {

    /**
    *  分页查询
    * @param queryShelfOrderTaskForm
    * @param req
    * @return
    */
    IPage<ShelfOrderTaskVO> selectPage(QueryShelfOrderTaskForm queryShelfOrderTaskForm,
                                       Integer pageNo,
                                       Integer pageSize,
                                       HttpServletRequest req);

    /**
    *  查询列表
    * @param queryShelfOrderTaskForm
    * @return
    */
    List<ShelfOrderTaskVO> selectList(QueryShelfOrderTaskForm queryShelfOrderTaskForm);


    /**
     *  查询列表 过滤掉强制上架
     * @param queryShelfOrderTaskForm
     * @return
     */
    List<ShelfOrderTaskVO> selectListFeign(QueryShelfOrderTaskForm queryShelfOrderTaskForm);

    /**
     * 保存(新增+编辑)
     * @param shelfOrderTask
     */
    ShelfOrderTask saveOrUpdateShelfOrderTask(ShelfOrderTask shelfOrderTask);

    /**
     * 逻辑删除
     * @param id
     */
    void delShelfOrderTask(Long id);

    /**
     * 查询导出
     * @param queryShelfOrderTaskForm
     * @return
     */
    List<LinkedHashMap<String, Object>> queryShelfOrderTaskForExcel(QueryShelfOrderTaskForm queryShelfOrderTaskForm,
                                                                    HttpServletRequest req);

    /**
     * 强制上架 入库
     * @param shelfOrderTasks
     */
    void forcedShelf(List<ShelfOrderTask> shelfOrderTasks);

    void cancel(List<ShelfOrderTask> shelfOrderTasks);

    /**
     * 直接生成上架任务
     * @param details
     */
    void directGenerationShelfTask(ReceiptVO details);

    String doRecommendedLocation(String materialCode, Long warehouseId, List<WarehouseLocationVO> warehouseLocations);

}
