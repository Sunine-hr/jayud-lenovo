package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryCustomerLineForm;
import com.jayud.oms.model.po.CustomerLine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.CustomerLineDetailsVO;
import com.jayud.oms.model.vo.CustomerLineVO;
import com.jayud.oms.model.vo.LineDetailsVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户线路管理 Mapper 接口
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
public interface CustomerLineMapper extends BaseMapper<CustomerLine> {

    IPage<CustomerLineVO> findCustomerLineByPage(Page page, @Param("form") QueryCustomerLineForm form);

    CustomerLineDetailsVO getCustomerLineDetails(@Param("id") Long id);

}
