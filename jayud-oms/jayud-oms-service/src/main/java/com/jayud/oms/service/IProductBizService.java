package com.jayud.oms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.po.ProductBiz;

import java.util.List;

/**
 * 合同信息
 */
public interface IProductBizService extends IService<ProductBiz> {

    List<ProductBiz> findProductBiz();



}
