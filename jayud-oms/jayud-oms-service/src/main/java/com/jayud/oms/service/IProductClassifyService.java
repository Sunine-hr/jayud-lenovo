package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.po.ProductClassify;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IProductClassifyService extends IService<ProductClassify> {

    List<ProductClassify> findProductClassify(Map<String,Object> param);
}
