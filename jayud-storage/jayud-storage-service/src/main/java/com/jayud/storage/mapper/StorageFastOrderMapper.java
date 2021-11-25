package com.jayud.storage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryStorageFastOrderForm;
import com.jayud.storage.model.po.StorageFastOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.StorageFastOrderFormVO;
import com.jayud.storage.model.vo.StorageFastOrderVO;
import com.jayud.storage.model.vo.StorageOutOrderFormVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 快进快出订单表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-06-10
 */
public interface StorageFastOrderMapper extends BaseMapper<StorageFastOrder> {

    IPage<StorageFastOrderFormVO> findByPage(@Param("page") Page<StorageOutOrderFormVO> page, @Param("form")QueryStorageFastOrderForm form, @Param("legalIds")List<Long> legalIds);

    List<StorageFastOrderVO> getFastOrderByMainOrder(@Param("mainOrderNos") List<String> mainOrderNos);
}
