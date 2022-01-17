package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AcctReceiptFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.AcctReceiptFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 付款跟踪记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Mapper
public interface AcctReceiptFollowMapper extends BaseMapper<AcctReceiptFollow> {

    IPage<AcctReceiptFollowVO> findListByAccountBankBillId(@Param("form") QueryCommonForm form,@Param("page")  Page<AcctReceiptFollowVO> page);
}
