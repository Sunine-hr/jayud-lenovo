package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.Invoice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.InvoiceDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 结算单（应收款）主表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {

    IPage<InvoiceDetailVO> findByPage(@Param("page") Page<InvoiceDetailVO> page,@Param("form") QueryForm form);
}
