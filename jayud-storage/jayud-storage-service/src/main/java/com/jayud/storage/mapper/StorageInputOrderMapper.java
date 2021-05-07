package com.jayud.storage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryStorageOrderForm;
import com.jayud.storage.model.po.StorageInputOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.StorageInputOrderWarehouseingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 入库订单表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Mapper
public interface StorageInputOrderMapper extends BaseMapper<StorageInputOrder> {

    IPage<StorageInputOrderFormVO> findByPage(@Param("page") Page<StorageInputOrderFormVO> page, @Param("form")QueryStorageOrderForm form, @Param("legalIds")List<Long> legalIds);

    IPage<StorageInputOrderWarehouseingVO> findWarehousingByPage(@Param("page") Page<StorageInputOrderWarehouseingVO> page, @Param("form")QueryStorageOrderForm form, @Param("legalIds")List<Long> legalIds);
}
