package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.GoodsTypeForm;
import com.jayud.mall.model.po.GoodsType;
import com.jayud.mall.model.vo.GoodsTypeReturnVO;
import com.jayud.mall.model.vo.GoodsTypeVO;

import java.util.List;

/**
 * <p>
 * (报价&货物)类型表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface IGoodsTypeService extends IService<GoodsType> {

    /**
     * 查询货物型表list
     * @param form
     * @return
     */
    List<GoodsTypeVO> findGoodsType(GoodsTypeForm form);

    /**
     * 货物类型下拉选择
     * @return
     */
    GoodsTypeReturnVO findGoodsTypeBy();
}
