package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryAccountReceivableForm;
import com.jayud.mall.model.po.AccountReceivable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.AccountReceivableVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 应收对账单表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
@Mapper
@Component
public interface AccountReceivableMapper extends BaseMapper<AccountReceivable> {

    /**
     * 分页查询应收对账单
     * @param page
     * @param form
     * @return
     */
    IPage<AccountReceivableVO> findAccountReceivableByPage(Page<AccountReceivableVO> page, @Param("form") QueryAccountReceivableForm form);

    /**
     * 根据对账单id，查询对账单
     * @param id
     * @return
     */
    AccountReceivableVO findAccountReceivableById(@Param("id") Long id);
}
