package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.po.QuotationType;
import com.jayud.mall.model.vo.QuotationTypeReturnVO;

import java.util.List;

/**
 * <p>
 * 报价类型表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-08
 */
public interface IQuotationTypeService extends IService<QuotationType> {

    /**
     * 报价类型下拉选择
     * @return
     */
    List<QuotationTypeReturnVO> findQuotationTypeBy();
}
