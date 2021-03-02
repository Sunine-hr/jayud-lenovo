package com.jayud.mall.mapper;

import com.jayud.mall.model.po.ReceivableBillDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.ReceivableBillDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 应收账单明细 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Mapper
@Component
public interface ReceivableBillDetailMapper extends BaseMapper<ReceivableBillDetail> {

    /**
     * 根据应收账单主单id，查询应收账单明细
     * @param billMasterId
     * @return
     */
    List<ReceivableBillDetailVO> findReceivableBillDetailByBillMasterId(@Param("billMasterId") Long billMasterId);
}
