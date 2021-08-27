package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.CustomerTruckPlace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.CustomerTruckPlaceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 运输公司车牌信息 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@Mapper
public interface CustomerTruckPlaceMapper extends BaseMapper<CustomerTruckPlace> {

    IPage<CustomerTruckPlaceVO> findByPage(@Param("page") Page<CustomerTruckPlaceVO> page, @Param("form")QueryForm form);
}
