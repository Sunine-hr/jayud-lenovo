package com.jayud.storage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryStorageOrderForm;
import com.jayud.storage.model.po.StorageOutOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出库订单表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
public interface StorageOutOrderMapper extends BaseMapper<StorageOutOrder> {

    IPage<StorageInputOrderFormVO> findByPage(@Param("page") Page<StorageInputOrderFormVO> page, @Param("form") QueryStorageOrderForm form, @Param("legalIds") List<Long> legalIds);
}
