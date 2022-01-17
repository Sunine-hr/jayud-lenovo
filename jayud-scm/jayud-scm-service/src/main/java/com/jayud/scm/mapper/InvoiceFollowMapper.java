package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.InvoiceFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.InvoiceFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 结算单（应收款）跟踪记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Mapper
public interface InvoiceFollowMapper extends BaseMapper<InvoiceFollow> {

    IPage<InvoiceFollowVO> findListByInvoiceId(@Param("form") QueryCommonForm form, @Param("page")Page<InvoiceFollowVO> page);
}
