package com.jayud.wms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.WmsShippingReviewToBox;
import com.jayud.wms.mapper.WmsShippingReviewToBoxMapper;
import com.jayud.wms.service.IWmsShippingReviewToBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 发运复核-箱数关系 服务实现类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Service
public class WmsShippingReviewToBoxServiceImpl extends ServiceImpl<WmsShippingReviewToBoxMapper, WmsShippingReviewToBox> implements IWmsShippingReviewToBoxService {


    @Autowired
    private WmsShippingReviewToBoxMapper wmsShippingReviewToBoxMapper;

    @Override
    public IPage<WmsShippingReviewToBox> selectPage(WmsShippingReviewToBox wmsShippingReviewToBox,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsShippingReviewToBox> page=new Page<WmsShippingReviewToBox>(currentPage,pageSize);
        IPage<WmsShippingReviewToBox> pageList= wmsShippingReviewToBoxMapper.pageList(page, wmsShippingReviewToBox);
        return pageList;
    }

    @Override
    public List<WmsShippingReviewToBox> selectList(WmsShippingReviewToBox wmsShippingReviewToBox){
        return wmsShippingReviewToBoxMapper.list(wmsShippingReviewToBox);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsShippingReviewToBoxMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsShippingReviewToBoxForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsShippingReviewToBoxForExcel(paramMap);
    }

}
