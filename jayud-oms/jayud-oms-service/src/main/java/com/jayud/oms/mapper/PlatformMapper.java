package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryLineForm;
import com.jayud.oms.model.bo.QueryPlatformForm;
import com.jayud.oms.model.po.Platform;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.PlatformDetailsVO;
import com.jayud.oms.model.vo.PlatformVO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 月台管理 Mapper 接口
 * </p>
 *
 * @author CYC
 * @since 2021-10-23
 */
public interface PlatformMapper extends BaseMapper<Platform> {

    IPage<PlatformVO> findPlatformByPage(Page page, @Param("form") QueryPlatformForm form);

    Map<String, Object> getLastCodeByCreateTime(@Param("curDate") String curDate);

    PlatformDetailsVO getPlatformDetails(@Param("id") Long id);
}
