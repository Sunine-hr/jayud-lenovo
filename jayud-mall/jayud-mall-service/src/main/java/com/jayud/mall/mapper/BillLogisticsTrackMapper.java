package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.BillLogisticsTrack;
import com.jayud.mall.model.vo.BillLogisticsTrackVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 提单物流轨迹跟踪表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-04
 */
@Mapper
@Component
public interface BillLogisticsTrackMapper extends BaseMapper<BillLogisticsTrack> {

    /**
     * 跟据提单id，查询提单物流轨迹
     * @param billId 提单id
     * @return
     */
    List<BillLogisticsTrackVO> findBillLogisticsTrackByBillId(@Param("billId") Long billId);
}
