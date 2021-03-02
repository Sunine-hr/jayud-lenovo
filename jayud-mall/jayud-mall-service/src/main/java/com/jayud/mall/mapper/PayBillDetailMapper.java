package com.jayud.mall.mapper;

import com.jayud.mall.model.po.PayBillDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.PayBillDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 应付账单明细 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Mapper
@Component
public interface PayBillDetailMapper extends BaseMapper<PayBillDetail> {

    /**
     * 根据应付账单主单id,查询订单对应应付费用明细list
     * @param billMasterId
     * @return
     */
    List<PayBillDetailVO> findPayBillDetailByBillMasterId(@Param("billMasterId") Long billMasterId);
}
