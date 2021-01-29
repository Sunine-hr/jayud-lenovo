package com.jayud.oceanship.service.impl;

import com.jayud.oceanship.model.po.Goods;
import com.jayud.oceanship.mapper.GoodsMapper;
import com.jayud.oceanship.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 货品信息表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

}
