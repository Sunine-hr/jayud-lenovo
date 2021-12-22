package com.jayud.tools.service.impl;

import com.jayud.tools.model.po.FbaOrder;
import com.jayud.tools.mapper.FbaOrderMapper;
import com.jayud.tools.service.IFbaOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * FBA订单 服务实现类
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
@Service
public class FbaOrderServiceImpl extends ServiceImpl<FbaOrderMapper, FbaOrder> implements IFbaOrderService {

}
