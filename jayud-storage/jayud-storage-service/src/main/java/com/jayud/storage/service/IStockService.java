package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.QueryStockForm;
import com.jayud.storage.model.po.Stock;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.po.WarehouseGoods;
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

    boolean getIsStockNumber(String sku, Integer number);

    /**
     * 锁定库存
     * @param convert
     * @return
     */
    boolean lockInInventory(WarehouseGoods convert);

    /**
     * 释放锁定库存
     * @return
     */
    boolean releaseInventory(Long orderId, String orderNo);

    /**
     * 订单驳回，库存变更
     * @param orderNo
     * @param id
     * @return
     */
    boolean changeInventory(String orderNo, Long id);
}
