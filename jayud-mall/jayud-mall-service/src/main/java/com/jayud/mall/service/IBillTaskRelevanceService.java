package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.BillTaskRelevanceIdForm;
import com.jayud.mall.model.bo.BillTaskRelevanceQueryForm;
import com.jayud.mall.model.po.BillTaskRelevance;
import com.jayud.mall.model.po.OceanBill;
import com.jayud.mall.model.vo.BillTaskRelevanceVO;

import java.util.List;

/**
 * <p>
 * 提单任务关联 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
public interface IBillTaskRelevanceService extends IService<BillTaskRelevance> {

    /**
     * <p>提单任务关联</p>
     * <p>根据提单，找到任务任务组，运营小组，最后找到提单任务列表</p>
     * <p>保存对应的提单任务关联</p>
     */
    List<BillTaskRelevanceVO> savebillTaskRelevance(OceanBill oceanBill);

    /**
     * 1.查询提单任务list
     * @param form
     * @return
     */
    List<BillTaskRelevanceVO> findBillTaskRelevance(BillTaskRelevanceQueryForm form);

    /**
     * 2.根据id查询提单任务
     * @param id
     * @return
     */
    BillTaskRelevanceVO findBillTaskRelevanceById(Long id);

    /**
     * 3.完成提单任务
     * @param form
     */
    void finishTask(BillTaskRelevanceIdForm form);

    /**
     * 4.延期提单任务
     * @param form
     */
    void postponeTask(BillTaskRelevanceIdForm form);
}
