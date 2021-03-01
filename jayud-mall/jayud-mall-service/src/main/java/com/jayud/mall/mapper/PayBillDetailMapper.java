package com.jayud.mall.mapper;

import com.jayud.mall.model.po.PayBillDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

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

}
