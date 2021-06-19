package com.jayud.storage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryOrderStorageForm;
import com.jayud.storage.model.po.StorageOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.StockVO;
import com.jayud.storage.model.vo.StorageOrderVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-05-27
 */
public interface StorageOrderMapper extends BaseMapper<StorageOrder> {

    IPage<StorageOrderVO> findByPage(@Param("page") Page<StockVO> page, @Param("form") QueryOrderStorageForm form);

    List<StorageOrderVO> getList(@Param("orderNo")String orderNo, @Param("outOrderNo")String outOrderNo, @Param("customerName")String customerName, @Param("month")String month);
}
