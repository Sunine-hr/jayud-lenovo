package com.jayud.storage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryStockForm;
import com.jayud.storage.model.po.Stock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.StockVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 库存表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-04-29
 */
public interface StockMapper extends BaseMapper<Stock> {

    IPage<StockVO> findByPage(@Param("page") Page<StockVO> page, @Param("form") QueryStockForm form);
}
