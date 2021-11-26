package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryContractQuotationForm;
import com.jayud.oms.model.po.ContractQuotation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.ContractQuotationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 合同报价 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-10-26
 */
@Mapper
public interface ContractQuotationMapper extends BaseMapper<ContractQuotation> {

    IPage<ContractQuotationVO> findByPage(@Param("page") Page<ContractQuotation> page, @Param("form") QueryContractQuotationForm form);

    int countByTime(@Param("now") LocalDateTime now);
}
