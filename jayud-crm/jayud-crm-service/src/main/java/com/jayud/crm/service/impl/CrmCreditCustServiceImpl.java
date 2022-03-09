package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.utils.BigDecimalUtil;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.model.bo.AddCrmCreditCustForm;
import com.jayud.crm.model.po.CrmCredit;
import com.jayud.crm.model.po.CrmCreditDepart;
import com.jayud.crm.service.ICrmCreditDepartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCreditCust;
import com.jayud.crm.mapper.CrmCreditCustMapper;
import com.jayud.crm.service.ICrmCreditCustService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_额度_额度授信管理(crm_credit_cust) 服务实现类
 *
 * @author jayud
 * @since 2022-03-04
 */
@Slf4j
@Service
public class CrmCreditCustServiceImpl extends ServiceImpl<CrmCreditCustMapper, CrmCreditCust> implements ICrmCreditCustService {


    @Autowired
    private CrmCreditCustMapper crmCreditCustMapper;
    @Autowired
    private ICrmCreditDepartService crmCreditDepartService;

    @Override
    public IPage<CrmCreditCust> selectPage(CrmCreditCust crmCreditCust,
                                           Integer currentPage,
                                           Integer pageSize,
                                           HttpServletRequest req) {

        Page<CrmCreditCust> page = new Page<CrmCreditCust>(currentPage, pageSize);
        IPage<CrmCreditCust> pageList = crmCreditCustMapper.pageList(page, crmCreditCust);
        return pageList;
    }

    @Override
    public List<CrmCreditCust> selectList(CrmCreditCust crmCreditCust) {
        return crmCreditCustMapper.list(crmCreditCust);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        crmCreditCustMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id) {
        crmCreditCustMapper.logicDel(id, CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCreditCustForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCreditCustForExcel(paramMap);
    }

    @Override
    public BigDecimal calculationRemainingCreditLine(Long departId, String creditId, String userTenantCode) {
        //获取总量
        List<CrmCreditDepart> crmCredits = this.crmCreditDepartService.selectList(new CrmCreditDepart().setDepartId(departId).setCreditId(creditId).setTenantCode(CurrentUserUtil.getUserTenantCode()).setIsDeleted(false));
        CrmCreditDepart tmp = new CrmCreditDepart();
        if (CollectionUtil.isEmpty(crmCredits)) {
            tmp.setCreditGrantedMoney(new BigDecimal(0)).setCreditAmt(new BigDecimal(0));
        } else {
            tmp = crmCredits.get(0);
        }
        //计算剩余额度
        BigDecimal amount = BigDecimalUtil.subtract(tmp.getCreditAmt(), tmp.getCreditGrantedMoney());
        return amount;
    }

    @Override
    @Transactional
    public void saveOrUpdate(AddCrmCreditCustForm form) {
        CrmCreditCust convert = ConvertUtil.convert(form, CrmCreditCust.class);
        //获取总量
        List<CrmCreditDepart> tmps = this.crmCreditDepartService.selectList(new CrmCreditDepart().setDepartId(form.getDepartId()).setCreditId(form.getCreditId()).setTenantCode(CurrentUserUtil.getUserTenantCode()).setIsDeleted(false));
        CrmCreditDepart creditDepart = tmps.get(0);
        if (form.getId() == null) {
            convert.setTenantCode(CurrentUserUtil.getUserTenantCode());
        } else {
            convert.setUpdateBy(CurrentUserUtil.getUsername());
        }
        this.saveOrUpdate(convert);
        //扣除剩余量
        List<CrmCreditCust> crmCreditCusts = this.selectList(new CrmCreditCust().setDepartId(form.getDepartId()).setCreditId(form.getCreditId()).setTenantCode(CurrentUserUtil.getUserTenantCode()).setIsDeleted(false));
        BigDecimal departAmout = new BigDecimal(0);
        for (CrmCreditCust creditCust : crmCreditCusts) {
            departAmout = BigDecimalUtil.add(creditCust.getCreditAmt(), departAmout);
        }
        CrmCreditDepart update = new CrmCreditDepart();
        update.setId(creditDepart.getId());
        this.crmCreditDepartService.updateById(update.setCreditGrantedMoney(departAmout));
    }

}
