package com.jayud.storage.service.impl;

import com.jayud.storage.model.po.GoodsLocationRecord;
import com.jayud.storage.mapper.GoodsLocationRecordMapper;
import com.jayud.storage.service.IGoodsLocationRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品对应库位表（记录入库的商品所在库位以及数量） 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
@Service
public class GoodsLocationRecordServiceImpl extends ServiceImpl<GoodsLocationRecordMapper, GoodsLocationRecord> implements IGoodsLocationRecordService {

}
