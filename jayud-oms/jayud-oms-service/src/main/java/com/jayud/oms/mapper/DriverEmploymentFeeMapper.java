package com.jayud.oms.mapper;

import com.jayud.oms.model.po.DriverEmploymentFee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.DriverEmploymentFeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 司机录入费用表(小程序使用) Mapper 接口
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-13
 */
@Mapper
public interface DriverEmploymentFeeMapper extends BaseMapper<DriverEmploymentFee> {

    List<DriverEmploymentFeeVO> getEmploymentFeeInfo(@Param("orderNo") String orderNo);
}
