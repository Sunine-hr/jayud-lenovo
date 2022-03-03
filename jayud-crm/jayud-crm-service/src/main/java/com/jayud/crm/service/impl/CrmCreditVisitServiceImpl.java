package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.mapper.CrmCreditVisitRoleMapper;
import com.jayud.crm.model.bo.CrmCreditVisitForm;
import com.jayud.crm.model.po.CrmCreditVisitRole;
import com.jayud.crm.model.vo.CrmCreditVisitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCreditVisit;
import com.jayud.crm.mapper.CrmCreditVisitMapper;
import com.jayud.crm.service.ICrmCreditVisitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 基本档案_客户_客户走访记录 服务实现类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Service
public class CrmCreditVisitServiceImpl extends ServiceImpl<CrmCreditVisitMapper, CrmCreditVisit> implements ICrmCreditVisitService {


    @Autowired
    private CrmCreditVisitMapper crmCreditVisitMapper;

    @Autowired
    private CrmCreditVisitRoleMapper crmCreditVisitRoleMapper;

    @Override
    public IPage<CrmCreditVisitVO> selectPage(CrmCreditVisitForm crmCreditVisitForm,
                                              Integer currentPage,
                                              Integer pageSize,
                                              HttpServletRequest req) {

        Page<CrmCreditVisitForm> page = new Page<CrmCreditVisitForm>(currentPage, pageSize);
        IPage<CrmCreditVisitVO> pageList = crmCreditVisitMapper.pageList(page, crmCreditVisitForm);
        return pageList;
    }

    @Override
    public List<CrmCreditVisit> selectList(CrmCreditVisit crmCreditVisit) {
        return crmCreditVisitMapper.list(crmCreditVisit);
    }

    @Override
    public BaseResult saveOrUpdateCrmCreditVisit(CrmCreditVisitForm crmCreditVisitForm) {
        Boolean result = null;
        CrmCreditVisit convert = ConvertUtil.convert(crmCreditVisitForm, CrmCreditVisit.class);
        if (convert.getId() != null) {
            //先删除掉之前的信息关联信息
            CrmCreditVisitRole crmCreditVisitRole = new CrmCreditVisitRole();
            crmCreditVisitRole.setVisitId(convert.getId());
            crmCreditVisitRoleMapper.updateCrmCreditVisitRole(crmCreditVisitRole);


            convert.setUpdateBy(CurrentUserUtil.getUsername());
            convert.setUpdateTime(new Date());
            result = this.updateById(convert);
            Long id = convert.getId();
            //然后再添加拜访表和员工表关联表
            for (int i = 0; i < crmCreditVisitForm.getVisitNameList().size(); i++) {

                CrmCreditVisitRole crmCreditVisitRole1 = new CrmCreditVisitRole();
                //拜访表
                crmCreditVisitRole1.setVisitId(id);
                //员工id
                crmCreditVisitRole1.setUserId(crmCreditVisitForm.getVisitNameList().get(i));
                crmCreditVisitRole1.setCreateBy(CurrentUserUtil.getUsername());
                crmCreditVisitRole1.setCreateTime(new Date());
                crmCreditVisitRoleMapper.insert(crmCreditVisitRole1);
            }
        } else {
            convert.setCreateBy(CurrentUserUtil.getUsername());
            convert.setCreateTime(new Date());
            result = this.saveOrUpdate(convert);
            Long id = convert.getId();

            //然后再添加拜访表和员工表关联表
            for (int i = 0; i < crmCreditVisitForm.getVisitNameList().size(); i++) {
                CrmCreditVisitRole crmCreditVisitRole1 = new CrmCreditVisitRole();
                //拜访表
                crmCreditVisitRole1.setVisitId(id);
                //员工id
                crmCreditVisitRole1.setUserId(crmCreditVisitForm.getVisitNameList().get(i));
                crmCreditVisitRole1.setCreateBy(CurrentUserUtil.getUsername());
                crmCreditVisitRole1.setCreateTime(new Date());
                crmCreditVisitRoleMapper.insert(crmCreditVisitRole1);
            }
        }
        if (result) {
            log.warn("新增或修改成功");
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }
        return BaseResult.error(SysTips.EDIT_FAIL);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        crmCreditVisitMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(List<Long> ids) {
        List<CrmCreditVisit> crmCreditVisitList = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            CrmCreditVisit crmCreditVisit = new CrmCreditVisit();
            crmCreditVisit.setId(ids.get(i));
            crmCreditVisit.setIsDeleted(true);
            crmCreditVisitList.add(crmCreditVisit);
        }
        this.updateBatchById(crmCreditVisitList);
    }

    @Override
    public CrmCreditVisitVO findCrmCreditVisitIdOne(Long id) {
        return crmCreditVisitMapper.findCrmCreditVisitIdOne(id);
    }

}
