package com.jayud.scm.mapper;

import com.jayud.scm.model.po.AccountBankBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * <p>
 * 水单主表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Mapper
public interface AccountBankBillMapper extends BaseMapper<AccountBankBill> {

    void automaticallyGeneratePayment(Map<String, Object> map);
}
