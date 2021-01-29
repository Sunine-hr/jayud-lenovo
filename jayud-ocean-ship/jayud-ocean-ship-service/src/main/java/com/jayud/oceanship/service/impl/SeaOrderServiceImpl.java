package com.jayud.oceanship.service.impl;

import com.jayud.oceanship.model.po.SeaOrder;
import com.jayud.oceanship.mapper.SeaOrderMapper;
import com.jayud.oceanship.service.ISeaOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 海运订单表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Service
public class SeaOrderServiceImpl extends ServiceImpl<SeaOrderMapper, SeaOrder> implements ISeaOrderService {

}
