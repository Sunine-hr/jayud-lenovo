package com.jayud.finance.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * 账单模板
 */
public interface BillTemplateService {

    public void export(Map<String, Object> param) throws IOException;
}
