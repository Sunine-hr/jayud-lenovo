//package com.jayud.airfreight.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.jayud.airfreight.model.po.Goods;
//import com.jayud.airfreight.mapper.GoodsMapper;
//import com.jayud.airfreight.service.IGoodsService;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.jayud.common.utils.Query;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * <p>
// * 货品信息表 服务实现类
// * </p>
// *
// * @author 李达荣
// * @since 2020-11-30
// */
//@Service
//public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
//
//    /**
//     * 根据业务主键id查询商品信息
//     */
//    @Override
//    public List<Goods> getGoodsByBusIds(List<Long> busIds, Integer busType) {
//        QueryWrapper<Goods> condition = new QueryWrapper<>();
//        condition.lambda().in(Goods::getBusinessId, busIds);
//        condition.lambda().eq(Goods::getBusinessType, busType);
//        return this.baseMapper.selectList(condition);
//    }
//}
