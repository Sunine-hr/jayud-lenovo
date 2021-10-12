package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.finance.bo.QueryCustomsFinanceCoRelationForm;
import com.jayud.finance.po.CustomsFinanceCoRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 云报关-财务金蝶中的供应商/客户公司名称对应关系表 服务类
 * </p>
 *
 * @author william.chen
 * @since 2020-09-24
 */
public interface ICustomsFinanceCoRelationService extends IService<CustomsFinanceCoRelation> {

    /**
     * 查询
     */
    List<CustomsFinanceCoRelation> list(Map<String, Object> para);

    /**
     * 保存
     */
    void saveCustomsFinanceCoRelation(CustomsFinanceCoRelation customsFinanceCoRelation);

    /**
     * 清理redis数据
     */
    void clearCompanyRelationMap();

    /**
     * 刷新
     * @return
     */
    public Map<String, CustomsFinanceCoRelation> refreshCompanyRelationMap();

    /**
     * 获取
     * @return
     */
    public Map<String, CustomsFinanceCoRelation> getCompanyRelationMap();


    /**
     * page
     */
    IPage<CustomsFinanceCoRelation> findCompanyRelationPage(QueryCustomsFinanceCoRelationForm form);
}
