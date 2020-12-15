package com.jayud.mall.service;

import com.jayud.mall.model.po.BillTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.BillTaskVO;

import java.util.List;

/**
 * <p>
 * 提单任务列表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
public interface IBillTaskService extends IService<BillTask> {

    /**
     * 根据提单id，查询提单任务列表
     * @param obId 提单id
     * @return
     */
    List<BillTaskVO> findbillTaskByObId(Long obId);
}
