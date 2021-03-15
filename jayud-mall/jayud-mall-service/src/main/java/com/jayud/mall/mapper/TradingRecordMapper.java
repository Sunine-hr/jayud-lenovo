package com.jayud.mall.mapper;

import com.jayud.mall.model.bo.TradingRecordCZForm;
import com.jayud.mall.model.bo.TradingRecordQueryForm;
import com.jayud.mall.model.po.TradingRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.TradingRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 交易记录表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-15
 */
@Mapper
@Component
public interface TradingRecordMapper extends BaseMapper<TradingRecord> {

    /**
     * 查询充值记录(仅查询充值)
     * @param form
     * @return
     */
    List<TradingRecordVO> findTradingRecordByCz(@Param("form") TradingRecordCZForm form);

    /**
     * 查询充值、支付记录
     * @param form
     * @return
     */
    List<TradingRecordVO> findTradingRecord(@Param("form") TradingRecordQueryForm form);
}
