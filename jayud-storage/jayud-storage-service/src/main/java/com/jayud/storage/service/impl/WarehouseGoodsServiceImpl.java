package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.model.po.WarehouseGoods;
import com.jayud.storage.mapper.WarehouseGoodsMapper;
import com.jayud.storage.model.vo.WarehouseGoodsVO;
import com.jayud.storage.service.IWarehouseGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 仓储商品信息表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
@Service
public class WarehouseGoodsServiceImpl extends ServiceImpl<WarehouseGoodsMapper, WarehouseGoods> implements IWarehouseGoodsService {

    @Override
    public List<WarehouseGoodsVO> getList(Long id, String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id",id);
        queryWrapper.eq("order_no",orderNo);
        List list = this.baseMapper.selectList(queryWrapper);
        List list1 = ConvertUtil.convertList(list, WarehouseGoodsVO.class);
        return list1;
    }
}
