package com.jayud.oms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.AddProductBizForm;
import com.jayud.oms.model.bo.QueryProductBizForm;
import com.jayud.oms.model.po.ProductBiz;
import com.jayud.oms.model.vo.ProductBizVO;

import java.util.List;

/**
 * 业务类型
 */
public interface IProductBizService extends IService<ProductBiz> {

    List<ProductBiz> findProductBiz();

    /**
     * 获取作业类型
     *
     * @param idCode
     * @return
     */
    ProductBiz getProductBizByCode(String idCode);


    /**
     * 列表分页查询
     *
     * @param form
     * @return
     */
    IPage<ProductBizVO> findProductBizByPage(QueryProductBizForm form);


    /**
     * 根据id查询业务类型
     */
    ProductBizVO getById(Long id);

    /**
     * 新增编辑业务类型
     *
     * @param form
     * @return
     */
    boolean saveOrUpdateProductBiz(AddProductBizForm form);


    /**
     * 更新为无效状态
     *
     * @param ids
     * @return
     */
    boolean deleteByIds(List<Long> ids);

}
