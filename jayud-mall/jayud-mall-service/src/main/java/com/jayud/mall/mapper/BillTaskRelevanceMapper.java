package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.BillTaskRelevance;
import com.jayud.mall.model.vo.BillTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
