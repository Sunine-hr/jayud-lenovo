package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.bo.BillTaskRelevanceQueryForm;
import com.jayud.mall.model.po.BillTaskRelevance;
import com.jayud.mall.model.vo.BillTaskRelevanceVO;
import com.jayud.mall.model.vo.BillTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提单任务关联 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
@Mapper
@Component
public interface BillTaskRelevanceMapper extends BaseMapper<BillTaskRelevance> {

    /**
     * 根据提单id，找运营组和任务
     * @param obId 提单id
     * @return
     */
    List<BillTaskVO> findBillTaskByObId(@Param("obId") Long obId);

    /**
     * 查询提单任务
     * @param form
     * @return
     */
    List<BillTaskRelevanceVO> findBillTaskRelevance(@Param("form") BillTaskRelevanceQueryForm form);

    /**
     * 根据id，查询任务
     * @param id
     * @return
     */
    BillTaskRelevanceVO findBillTaskRelevanceById(@Param("id") Long id);

    /**
     * 查询，完成任务后，激活 的 其他任务(提单任务)
     * @param paraMap
     * @return
     */
    List<BillTaskRelevance> findBillTaskRelevanceByParaMap(@Param("paraMap") Map<String, Object> paraMap);

    /**
     * 根据obid，查询提单任务
     * @param obId
     * @return
     */
    List<BillTaskRelevanceVO> findBillTaskRelevanceByobId(@Param("obId") Long obId);
}
