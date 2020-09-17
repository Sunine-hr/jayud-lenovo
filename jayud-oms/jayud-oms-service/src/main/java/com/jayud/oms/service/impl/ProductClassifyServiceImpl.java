package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.model.po.ProductClassify;
import com.jayud.oms.service.IProductClassifyService;
import com.jayud.oms.mapper.ProductClassifyMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class ProductClassifyServiceImpl extends ServiceImpl<ProductClassifyMapper, ProductClassify> implements IProductClassifyService {

}
