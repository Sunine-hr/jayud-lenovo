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
import com.jayud.storage.model.vo.StockVO;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.service.IStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public boolean saveStock(Stock stock) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sku",stock.getSku());
        queryWrapper.eq("good_name",stock.getGoodName());
        queryWrapper.eq("specification_model",stock.getSpecificationModel());
        Good good = goodService.getOne(queryWrapper);
        Stock stock1 = this.getOne(queryWrapper);
        if(stock1!=null){
            stock1.setAvailableStock(stock1.getAvailableStock()+stock.getAvailableStock());
            boolean b = this.saveOrUpdate(stock1);
            if(!b){
                return false;
            }
        }
        stock.setCustomerId(good.getCustomerId());
        stock.setCreateUser(UserOperator.getToken());
        stock.setCreateTime(LocalDateTime.now());
        boolean b = this.saveOrUpdate(stock);
        if(!b){
            return false;
        }
        return true;
    }

    @Override
    public IPage<StockVO> findByPage(QueryStockForm form) {
        Page<StockVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }
}
