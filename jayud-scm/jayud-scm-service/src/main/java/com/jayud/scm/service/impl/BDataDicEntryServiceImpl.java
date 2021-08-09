package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddBDataDicEntryForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BDataDic;
import com.jayud.scm.model.po.BDataDicEntry;
import com.jayud.scm.mapper.BDataDicEntryMapper;
import com.jayud.scm.model.po.SystemRoleActionCheck;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.BDataDicEntryVO;
import com.jayud.scm.model.vo.BDataDicVO;
import com.jayud.scm.service.IBDataDicEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据字典明细表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class BDataDicEntryServiceImpl extends ServiceImpl<BDataDicEntryMapper, BDataDicEntry> implements IBDataDicEntryService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public List<BDataDicEntryVO> getDropDownList(String dicCode) {
        QueryWrapper<BDataDicEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BDataDicEntry::getDicCode,dicCode);
        queryWrapper.lambda().eq(BDataDicEntry::getVoided,0);
        List<BDataDicEntry> list = this.list(queryWrapper);
        if(CollectionUtils.isNotEmpty(list)){
            return ConvertUtil.convertList(list,BDataDicEntryVO.class);
        }
        return new ArrayList<>();
    }

    @Override
    public IPage<BDataDicEntryVO> findByPage(QueryCommonForm form) {
        Page<BDataDicEntryVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page,form);
    }

    @Override
    public BDataDicEntry getBDataDicEntryByDicCode(String dicCode, String dataValue) {
        QueryWrapper<BDataDicEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BDataDicEntry::getDicCode,dicCode);
        queryWrapper.lambda().eq(BDataDicEntry::getDataValue,dataValue);
        queryWrapper.lambda().eq(BDataDicEntry::getVoided,0);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean saveOrUpdateBDataDicEntry(AddBDataDicEntryForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        BDataDicEntry bDataDicEntry = ConvertUtil.convert(form, BDataDicEntry.class);

        if(form.getId() != null){
            bDataDicEntry.setMdyBy(systemUser.getId().intValue());
            bDataDicEntry.setMdyByDtm(LocalDateTime.now());
            bDataDicEntry.setMdyByName(UserOperator.getToken());
        }else{
            bDataDicEntry.setCrtBy(systemUser.getId().intValue());
            bDataDicEntry.setCrtByDtm(LocalDateTime.now());
            bDataDicEntry.setCrtByName(UserOperator.getToken());
        }
        boolean update = this.saveOrUpdate(bDataDicEntry);

        return update;
    }

    @Override
    public BDataDicEntryVO getBDataDicEntryId(Integer id) {
        return ConvertUtil.convert(this.getById(id),BDataDicEntryVO.class);
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<BDataDicEntry> bDataDicEntries = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {
            BDataDicEntry bDataDicEntry = new BDataDicEntry();
            bDataDicEntry.setId(id.intValue());
            bDataDicEntry.setVoided(1);
            bDataDicEntry.setVoidedBy(deleteForm.getId().intValue());
            bDataDicEntry.setVoidedByDtm(deleteForm.getDeleteTime());
            bDataDicEntry.setVoidedByName(deleteForm.getName());
            bDataDicEntries.add(bDataDicEntry);
        }
        boolean b = this.updateBatchById(bDataDicEntries);
        if(b){
            log.warn("数据字典删除成功："+bDataDicEntries);
        }
        return b;
    }
}
