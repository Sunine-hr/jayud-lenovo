package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryAccountPayableForm;
import com.jayud.mall.model.po.AccountPayable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.AccountPayableVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 应付对账单表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
@Mapper
@Component
public interface AccountPayableMapper extends BaseMapper<AccountPayable> {

    /**
     * 应付对账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<AccountPayableVO> findAccountPayableByPage(Page<AccountPayableVO> page, @Param("form") QueryAccountPayableForm form);

    /**
     * 根据应付对账单id，查询应付对账单
     * @param id
     * @return
     */
    AccountPayableVO findAccountPayableById(@Param("id") Long id);
}
