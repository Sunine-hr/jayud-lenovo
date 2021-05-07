package com.jayud.storage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelvesLocationForm;
import com.jayud.storage.model.po.WarehouseAreaShelvesLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.WarehouseAreaShelvesLocationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 仓库区域货架库位表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Mapper
public interface WarehouseAreaShelvesLocationMapper extends BaseMapper<WarehouseAreaShelvesLocation> {

    List<WarehouseAreaShelvesLocation> getUpdateTime();

    IPage<WarehouseAreaShelvesLocationVO> findWarehouseAreaShelvesLocationByPage(@Param("page") Page<WarehouseAreaShelvesLocationVO> page, @Param("form") QueryWarehouseAreaShelvesLocationForm form);

    List<WarehouseAreaShelvesLocationVO> getlistByShelvesId(@Param("form")QueryWarehouseAreaShelvesLocationForm form);
}
