package com.jayud.finance.service;

import java.util.List;

public interface FinanceService {

    /**
     * 是否可以推送金蝶
     *
     * @param list
     * @param type 1-应付 2-应收
     */
    public void isPushKingdee(List list, Integer type);
}
