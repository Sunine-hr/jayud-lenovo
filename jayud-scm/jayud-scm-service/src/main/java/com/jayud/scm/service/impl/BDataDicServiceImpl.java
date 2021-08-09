package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddBDataDicForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.BDataDic;
import com.jayud.scm.mapper.BDataDicMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.BDataDicVO;
import com.jayud.scm.model.vo.SystemRoleActionCheckVO;
import com.jayud.scm.service.IBDataDicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 数据字典主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class BDataDicServiceImpl extends ServiceImpl<BDataDicMapper, BDataDic> implements IBDataDicService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public IPage<BDataDicVO> findByPage(QueryForm form) {
        Page<BDataDicVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page);
    }

    @Override
    public BDataDicVO getBDataDicById(Integer id) {

        return ConvertUtil.convert(this.getById(id),BDataDicVO.class);
    }

    @Override
    public BDataDic getBDataDicByDicCode(String dicCode) {
        QueryWrapper<BDataDic> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(BDataDic::getDicCode,dicCode);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean saveOrUpdateBDataDic(AddBDataDicForm form) {

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        BDataDic bDataDic = ConvertUtil.convert(form, BDataDic.class);

        if(form.getId() != null){
            bDataDic.setMdyBy(systemUser.getId().intValue());
            bDataDic.setMdyByDtm(LocalDateTime.now());
            bDataDic.setMdyByName(UserOperator.getToken());
        }else{
            bDataDic.setCrtBy(systemUser.getId().intValue());
            bDataDic.setCrtByDtm(LocalDateTime.now());
            bDataDic.setCrtByName(UserOperator.getToken());
        }
        boolean update = this.saveOrUpdate(bDataDic);

        return update;
    }
}
