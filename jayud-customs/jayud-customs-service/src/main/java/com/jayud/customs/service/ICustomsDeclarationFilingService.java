package com.jayud.customs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.customs.model.bo.AddCustomsDeclarationFilingForm;
import com.jayud.customs.model.bo.QueryCustomsDeclarationFiling;
import com.jayud.customs.model.po.CustomsDeclarationFiling;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.customs.model.vo.CustomsDeclarationFilingVO;

/**
 * <p>
 * 报关归档 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-08-24
 */
public interface ICustomsDeclarationFilingService extends IService<CustomsDeclarationFiling> {


    boolean exitBoxNum(CustomsDeclarationFiling customsDeclarationFiling);


    void saveOrUpdate(AddCustomsDeclarationFilingForm form);


    Integer getSerialNumber(String filingDate, Integer bizModel, Integer imAndExType);

    String generateBoxNum(String filingDate, Integer bizModel, Integer imAndExType);

    IPage<CustomsDeclarationFilingVO> findByPage(QueryCustomsDeclarationFiling form);

    CustomsDeclarationFilingVO getDetails(Long id);
}
