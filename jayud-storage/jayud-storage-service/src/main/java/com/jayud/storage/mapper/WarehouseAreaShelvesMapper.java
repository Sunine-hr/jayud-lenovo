package com.jayud.storage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelves2Form;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelvesForm;
import com.jayud.storage.model.po.WarehouseAreaShelves;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.WarehouseAreaShelvesFormVO;
import com.jayud.storage.model.vo.WarehouseAreaShelvesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品区域货架表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Mapper
public interface WarehouseAreaShelvesMapper extends BaseMapper<WarehouseAreaShelves> {

    IPage<WarehouseAreaShelvesVO> findWarehouseAreaShelvesByPage(@Param("page") Page<WarehouseAreaShelvesVO> page, @Param("form")QueryWarehouseAreaShelvesForm form);

    IPage<WarehouseAreaShelvesFormVO> findWarehouseAreaShelvesLocationByPage(@Param("page")Page<WarehouseAreaShelvesFormVO> page, @Param("form")QueryWarehouseAreaShelves2Form form);

    WarehouseAreaShelvesFormVO getWarehouseAreaShelvesByShelvesId(@Param("shelvesId")Long shelvesId);
}
