package com.jayud.oms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.po.ProductBiz;

import java.util.List;

/**
 * 合同信息
 */
public interface IProductBizService extends IService<ProductBiz> {

    List<ProductBiz> findProductBiz();

    /**
     * 获取作业类型
     * @param idCode
     * @return
     */
    ProductBiz getProductBizByCode(String idCode);


}
