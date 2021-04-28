package com.jayud.storage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryWarehouseAreaForm;
import com.jayud.storage.model.po.WarehouseArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.WarehouseAreaVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 仓库区域表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
public interface WarehouseAreaMapper extends BaseMapper<WarehouseArea> {

    List<WarehouseAreaVO> getList(@Param("form") QueryWarehouseAreaForm form);

    IPage<WarehouseAreaVO> findWarehouseAreaByPage(@Param("page")Page<WarehouseAreaVO> page, @Param("form") QueryWarehouseAreaForm form);
}
