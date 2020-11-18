package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryDriverInfoForm;
import com.jayud.oms.model.po.DriverInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.DriverInfoLinkVO;
import com.jayud.oms.model.vo.DriverInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 供应商对应司机信息 Mapper 接口
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Mapper
public interface DriverInfoMapper extends BaseMapper<DriverInfo> {

    IPage<DriverInfoVO> findDriverInfoByPage(Page page, @Param("form") QueryDriverInfoForm form);

    DriverInfoLinkVO getDriverInfoLink(@Param("driverId") Long driverId);
}
