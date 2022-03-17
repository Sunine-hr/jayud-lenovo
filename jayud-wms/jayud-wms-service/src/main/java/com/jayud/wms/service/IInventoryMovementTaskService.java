package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.InventoryMovementTaskCompletedForm;
import com.jayud.wms.model.bo.InventoryMovementTaskForm;
import com.jayud.wms.model.po.InventoryMovementTask;
import com.jayud.wms.model.vo.InventoryMovementTaskAppVO;
import com.jayud.wms.model.vo.InventoryMovementTaskVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存移库任务 服务类
 *
 * @author jyd
 * @since 2021-12-30
 */
public interface IInventoryMovementTaskService extends IService<InventoryMovementTask> {

    /**
    *  分页查询
    * @param inventoryMovementTask
    * @param req
    * @return
    */
    IPage<InventoryMovementTask> selectPage(InventoryMovementTask inventoryMovementTask,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param inventoryMovementTask
    * @return
    */
    List<InventoryMovementTask> selectList(InventoryMovementTask inventoryMovementTask);

    /**
     * 保存(新增+编辑)
     * @param inventoryMovementTask
     */
    InventoryMovementTask saveOrUpdateInventoryMovementTask(InventoryMovementTask inventoryMovementTask);

    /**
     * 逻辑删除
     * @param id
     */
    void delInventoryMovementTask(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryInventoryMovementTaskForExcel(Map<String, Object> paramMap);

    /**
     * 生成库存移动任务
     * @param form
     * @return
     */
    InventoryMovementTaskVO generateInventoryMovementTasks(InventoryMovementTaskForm form);

    /**
     * 库存移动任务确认
     * @param form
     * @return
     */
    boolean inventoryMovementTaskConfirmation(InventoryMovementTaskForm form);

    /**
     * 库存移动任务完成
     * @param form
     */
    boolean inventoryMovementTaskCompleted(InventoryMovementTaskCompletedForm form);

    /**
     *  分页查询
     * @param inventoryMovementTask
     * @param req
     * @return
     */
    IPage<InventoryMovementTaskAppVO> selectPageByFeign(InventoryMovementTask inventoryMovementTask,
                                                        Integer pageNo,
                                                        Integer pageSize,
                                                        HttpServletRequest req);

    /**
     * 去移库(移库详情)
     * @param mainCode
     * @return
     */
    InventoryMovementTaskAppVO queryInventoryMovementTaskByMainCode(String mainCode);

    /**
     * 移库确认
     * 如果有多条 确认当前任务后，返回下一条的任务，没有下一条任务，返回空
     * @param inventoryMovementTaskAppVO
     * @return
     */
    InventoryMovementTaskAppVO inventoryMovementTaskCompletedByApp(InventoryMovementTaskAppVO inventoryMovementTaskAppVO);
}
