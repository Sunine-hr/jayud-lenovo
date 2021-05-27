package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.DateUtils;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.model.bo.QueryStockForm;
import com.jayud.storage.model.po.Good;
import com.jayud.storage.model.po.Stock;
import com.jayud.storage.mapper.StockMapper;
import com.jayud.storage.model.po.WarehouseGoods;
import com.jayud.storage.model.vo.StockLocationNumberVO;
import com.jayud.storage.model.vo.StockVO;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.WarehouseGoodsVO;
import com.jayud.storage.service.IGoodsLocationRecordService;
import com.jayud.storage.service.IStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.storage.service.IWarehouseGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 库存表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-29
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

    @Autowired
    private GoodServiceImpl goodService;

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @Override
    public boolean saveStock(Stock stock) {
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("sku",stock.getSku());
        queryWrapper1.eq("name",stock.getGoodName());
        queryWrapper1.eq("specification_model",stock.getSpecificationModel());
        Good good = goodService.getOne(queryWrapper1);
        if(good == null){
            return false;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sku",stock.getSku());
        queryWrapper.eq("good_name",stock.getGoodName());
        queryWrapper.eq("specification_model",stock.getSpecificationModel());
        Stock stock1 = this.getOne(queryWrapper);
        if(stock1!=null){
            stock1.setAvailableStock(stock1.getAvailableStock()+stock.getAvailableStock());
            boolean b = this.saveOrUpdate(stock1);
            if(!b){
                return false;
            }
            return true;
        }else{
//            stock.setCustomerId(good.getCustomerId());
            stock.setCustomerId(good.getCustomerId());
            stock.setCreateUser(UserOperator.getToken());
            stock.setCreateTime(LocalDateTime.now());
            boolean b = this.saveOrUpdate(stock);
            if(!b){
                return false;
            }
            return true;
        }
    }

    @Override
    public IPage<StockVO> findByPage(QueryStockForm form) {
        Page<StockVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<StockVO> byPage = this.baseMapper.findByPage(page, form);
        return byPage;
    }

    @Override
    public String getIsStockNumber(String sku, Integer number) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sku",sku);
        Stock stock = this.baseMapper.selectOne(queryWrapper);
        if(stock==null){
            return "该商品不存在";
        }
        if(stock.getAvailableStock()<number){
            return "该商品数量超出库存";
        }
        return "pass";
    }

    @Override
    public boolean lockInInventory(WarehouseGoods convert) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sku",convert.getSku());
        Stock stock = this.baseMapper.selectOne(queryWrapper);
        stock.setAvailableStock(stock.getAvailableStock()-convert.getNumber());
        stock.setLockStock(convert.getNumber());
        boolean b = this.saveOrUpdate(stock);
        if(!b){
            return false;
        }
        return true;
    }

    @Override
    public boolean releaseInventory(Long orderId, String orderNo) {
        List<WarehouseGoodsVO> list1 = warehouseGoodsService.getList1(orderId, orderNo);
        List<Stock> stocks = new ArrayList<>();
        for (WarehouseGoodsVO warehouseGoodsVO : list1) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("sku",warehouseGoodsVO.getSku());
            Stock stock = this.baseMapper.selectOne(queryWrapper);
            stock.setLockStock(stock.getLockStock()-warehouseGoodsVO.getNumber());
            stocks.add(stock);
        }
        boolean b = this.saveOrUpdateBatch(stocks);
        if(!b){
            return false;
        }
        return true;
    }

    @Override
    public boolean changeInventory(String orderNo, Long id) {
        List<WarehouseGoodsVO> list1 = warehouseGoodsService.getList1(id, orderNo);
        List<Stock> stocks = new ArrayList<>();
        for (WarehouseGoodsVO warehouseGoodsVO : list1) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("sku",warehouseGoodsVO.getSku());
            Stock stock = this.baseMapper.selectOne(queryWrapper);
            stock.setLockStock(stock.getLockStock()-warehouseGoodsVO.getNumber());
            stock.setAvailableStock(stock.getAvailableStock()+warehouseGoodsVO.getNumber());
            stocks.add(stock);
        }
        boolean b = this.saveOrUpdateBatch(stocks);
        if(!b){
            return false;
        }
        return true;
    }

    @Override
    public StockLocationNumberVO getListBySkuAndLocationCode(String sku, String locationCode,Long customerId) {
        return goodsLocationRecordService.getListBySkuAndLocationCode(sku, locationCode,customerId);
    }
}
