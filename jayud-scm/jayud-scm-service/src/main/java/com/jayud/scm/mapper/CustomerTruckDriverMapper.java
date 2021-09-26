package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.CustomerTruckDriver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.CustomerTruckDriverVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 运输公司车牌司机信息 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@Mapper
public interface CustomerTruckDriverMapper extends BaseMapper<CustomerTruckDriver> {

    IPage<CustomerTruckDriverVO> findByPage(@Param("page") Page<CustomerTruckDriverVO> page, @Param("form")QueryForm form);
}
