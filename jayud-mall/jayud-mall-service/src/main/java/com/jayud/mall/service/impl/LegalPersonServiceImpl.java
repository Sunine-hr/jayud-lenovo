package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.LegalPersonMapper;
import com.jayud.mall.model.bo.LegalPersonForm;
import com.jayud.mall.model.po.LegalPerson;
import com.jayud.mall.model.vo.LegalPersonVO;
import com.jayud.mall.service.ILegalPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 法人表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Service
public class LegalPersonServiceImpl extends ServiceImpl<LegalPersonMapper, LegalPerson> implements ILegalPersonService {

    @Autowired
    LegalPersonMapper legalPersonMapper;

    @Override
    public CommonResult<List<LegalPersonVO>> findLegalPerson(LegalPersonForm form) {

        QueryWrapper<LegalPerson> queryWrapper = new QueryWrapper<>();
        Long id = form.getId();
        String legalEntity = form.getLegalEntity();
        if(id != null){
            queryWrapper.eq("id", id);
        }
        if(legalEntity != null){
            queryWrapper.like("legal_entity", legalEntity);
        }
        List<LegalPerson> list = this.list(queryWrapper);
        List<LegalPersonVO> legalPersonVOS = ConvertUtil.convertList(list, LegalPersonVO.class);
        return CommonResult.success(legalPersonVOS);
    }
}
