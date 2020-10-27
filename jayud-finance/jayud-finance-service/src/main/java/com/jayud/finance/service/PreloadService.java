package com.jayud.finance.service;

import com.jayud.finance.po.CustomsFinanceCoRelation;
import com.jayud.finance.po.CustomsFinanceFeeRelation;

import java.util.Map;

public interface PreloadService {
    public Map<String, CustomsFinanceCoRelation> getCompanyRelationMap();

    public Map<String, CustomsFinanceFeeRelation> getFeeRelationMap();

    public Map<String, CustomsFinanceCoRelation> refreshCompanyRelationMap();

    public Map<String, CustomsFinanceFeeRelation> refreshFeeRelationMap();

    public Map<String, CustomsFinanceCoRelation> addCompanyRelation(CustomsFinanceCoRelation coRelation);

    public Map<String, CustomsFinanceFeeRelation> addFeeRelation(CustomsFinanceFeeRelation feeRelation);

}
