package com.jayud.mall.mapper;

import com.jayud.mall.model.bo.AccountBalanceForm;
import com.jayud.mall.model.po.AccountBalance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.AccountBalanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 账户余额表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-15
 */
@Mapper
@Component
public interface AccountBalanceMapper extends BaseMapper<AccountBalance> {

    /**
     * 查询当前登录客户的账户余额
     * @param form
     * @return
     */
    List<AccountBalanceVO> findCurrAccountBalance(@Param("form") AccountBalanceForm form);
}
