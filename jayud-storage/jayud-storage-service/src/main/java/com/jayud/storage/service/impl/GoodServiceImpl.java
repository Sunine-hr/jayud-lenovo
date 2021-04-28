package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryGoodForm;
import com.jayud.storage.model.po.Good;
import com.jayud.storage.mapper.GoodMapper;
import com.jayud.storage.model.vo.GoodVO;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.service.IGoodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品信息维护表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-22
 */
@Service
public class GoodServiceImpl extends ServiceImpl<GoodMapper, Good> implements IGoodService {

    /**
     * 分页查询数据
     * @param form
     * @return
     */
    @Override
    public IPage<GoodVO> findGoodsByPage(QueryGoodForm form) {
        Page<GoodVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<GoodVO> pageInfo = this.baseMapper.findGoodsByPage(page,form);
        return pageInfo;
    }

    @Override
    public List<GoodVO> getList(QueryGoodForm form) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(form.getCustomerName()!=null){
            queryWrapper.like("customer_name",form.getCustomerName());
        }
        if(form.getName()!=null){
            queryWrapper.like("name",form.getName());
        }
        if(form.getSku()!=null){
            queryWrapper.like("sku",form.getSku());
        }
        List list = this.baseMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public boolean isCommodity(String sku) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sku",sku);
        Good good = this.baseMapper.selectOne(queryWrapper);
        if(good!=null){
            return true;
        }
        return false;
    }
}
