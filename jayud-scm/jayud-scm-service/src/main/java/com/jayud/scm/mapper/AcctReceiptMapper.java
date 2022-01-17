package com.jayud.scm.mapper;

import com.jayud.scm.model.po.AcctReceipt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 收款单表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Mapper
public interface AcctReceiptMapper extends BaseMapper<AcctReceipt> {

    public String fnGetCurrency(@Param("now") LocalDateTime now, @Param("currencyName")String currencyName, @Param("i")int i);

}
