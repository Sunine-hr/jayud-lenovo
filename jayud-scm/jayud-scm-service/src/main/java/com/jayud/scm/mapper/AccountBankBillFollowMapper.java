package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AccountBankBillFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.AccountBankBillFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 水单跟踪记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Mapper
public interface AccountBankBillFollowMapper extends BaseMapper<AccountBankBillFollow> {

    IPage<AccountBankBillFollowVO> findListByAccountBankBillId(@Param("form") QueryCommonForm form, @Param("page")Page<AccountBankBillFollowVO> page);
}
