package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.QueryStockForm;
import com.jayud.storage.model.po.Stock;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.StockVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 库存表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-29
 */
@Mapper
public interface IStockService extends IService<Stock> {

    boolean saveStock(Stock stock);

    IPage<StockVO> findByPage(QueryStockForm form);
}
