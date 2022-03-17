package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.CreateShelfMoveTaskForm;
import com.jayud.wms.model.po.ShelfMoveTask;
import com.jayud.common.dto.HikAGVFrom;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 货架移动任务 服务类
 *
 * @author jyd
 * @since 2022-03-05
 */
public interface IShelfMoveTaskService extends IService<ShelfMoveTask> {

    /**
    *  分页查询
    * @param shelfMoveTask
    * @param req
    * @return
    */
    IPage<ShelfMoveTask> selectPage(ShelfMoveTask shelfMoveTask,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param shelfMoveTask
    * @return
    */
    List<ShelfMoveTask> selectList(ShelfMoveTask shelfMoveTask);

    /**
     * 保存(新增+编辑)
     * @param shelfMoveTask
     */
    ShelfMoveTask saveOrUpdateShelfMoveTask(ShelfMoveTask shelfMoveTask);

    /**
     * 逻辑删除
     * @param id
     */
    void delShelfMoveTask(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryShelfMoveTaskForExcel(Map<String, Object> paramMap);

    /**
     * 创建货架移库任务
     * @param form
     */
    void createShelfMoveTask(CreateShelfMoveTaskForm form);

    /**
     * 根据货架移动任务明细号，更新状态
     * @param from
     * @return
     */
    BaseResult updateShelfMoveTaskByMxCode(HikAGVFrom from);
}
