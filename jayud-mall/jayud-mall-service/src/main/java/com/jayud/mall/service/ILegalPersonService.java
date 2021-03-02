package com.jayud.mall.service;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.LegalPersonForm;
import com.jayud.mall.model.po.LegalPerson;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.LegalPersonVO;

import java.util.List;

/**
 * <p>
 * 法人表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
public interface ILegalPersonService extends IService<LegalPerson> {

    /**
     * 查询法人主体list
     * @param form
     * @return
     */
    CommonResult<List<LegalPersonVO>> findLegalPerson(LegalPersonForm form);
}
