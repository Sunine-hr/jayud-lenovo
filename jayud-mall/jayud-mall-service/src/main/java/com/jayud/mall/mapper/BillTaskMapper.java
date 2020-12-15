package com.jayud.mall.mapper;

import com.jayud.mall.model.po.BillTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.BillTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 提单任务列表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
@Mapper
@Component
public interface BillTaskMapper extends BaseMapper<BillTask> {

    /**
     * 根据提单id，查询提单任务列表
     * @param obId
     * @return
     */
    List<BillTaskVO> findbillTaskByObId(Long obId);
}
