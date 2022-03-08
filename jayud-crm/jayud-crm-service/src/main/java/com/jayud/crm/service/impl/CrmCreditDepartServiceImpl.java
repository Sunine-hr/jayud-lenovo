package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.constant.SysTips;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.BigDecimalUtil;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.model.bo.AddCrmCreditDepartForm;
import com.jayud.crm.model.po.CrmCredit;
import com.jayud.crm.model.vo.CrmCreditDepartVO;
import com.jayud.crm.model.vo.CrmCreditVO;
import com.jayud.crm.service.ICrmCreditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCreditDepart;
import com.jayud.crm.mapper.CrmCreditDepartMapper;
import com.jayud.crm.service.ICrmCreditDepartService;
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
 * 基本档案_额度_部门额度授信管理(crm_credit_depart) 服务实现类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Service
public class CrmCreditDepartServiceImpl extends ServiceImpl<CrmCreditDepartMapper, CrmCreditDepart> implements ICrmCreditDepartService {


    @Autowired
    private CrmCreditDepartMapper crmCreditDepartMapper;
    @Autowired
    private ICrmCreditService crmCreditService;

    @Override
    public IPage<CrmCreditDepartVO> selectPage(CrmCreditDepart crmCreditDepart,
                                             Integer currentPage,
                                             Integer pageSize,
                                             HttpServletRequest req) {

        Page<CrmCreditDepart> page = new Page<CrmCreditDepart>(currentPage, pageSize);
        IPage<CrmCreditDepartVO> pageList = crmCreditDepartMapper.pageList(page, crmCreditDepart);
        for (CrmCreditDepartVO record : pageList.getRecords()) {
            record.setRemainingQuota(BigDecimalUtil.subtract(record.getCreditAmt(),record.getCreditGrantedMoney()));
        }
        return pageList;
    }

    @Override
    public List<CrmCreditDepart> selectList(CrmCreditDepart crmCreditDepart) {
        return crmCreditDepartMapper.list(crmCreditDepart);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        crmCreditDepartMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id) {
        crmCreditDepartMapper.logicDel(id, CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCreditDepartForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCreditDepartForExcel(paramMap);
    }

    @Override
    @Transactional
    public void saveOrUpdate(AddCrmCreditDepartForm form) {
        CrmCreditDepart convert = ConvertUtil.convert(form, CrmCreditDepart.class);

        if (form.getId() == null) {
            convert.setTenantCode(CurrentUserUtil.getUserTenantCode());
        } else {
            convert.setUpdateBy(CurrentUserUtil.getUsername());
        }
        this.saveOrUpdate(convert);

        //获取总量
        List<CrmCreditVO> crmCredits = this.crmCreditService.selectList(new CrmCredit().setCreditId(form.getCreditId()).setTenantCode(CurrentUserUtil.getUserTenantCode()).setIsDeleted(false));
        CrmCreditVO crmCredit = crmCredits.get(0);
        //扣除剩余量
        List<CrmCreditDepart> creditDeparts = this.selectList(new CrmCreditDepart().setCreditId(form.getCreditId()).setTenantCode(CurrentUserUtil.getUserTenantCode()).setIsDeleted(false));
        BigDecimal departAmout = new BigDecimal(0);
        for (CrmCreditDepart creditDepart : creditDeparts) {
            departAmout = BigDecimalUtil.add(creditDepart.getCreditAmt(), departAmout);
        }
        CrmCredit update = new CrmCredit();
        update.setId(crmCredit.getId());
        this.crmCreditService.updateById(update.setCreditGrantedMoney(departAmout));
    }

    @Override
    public BigDecimal calculationRemainingCreditLine(String creditId, String tenantCode) {
        //获取总量
        List<CrmCreditVO> crmCredits = this.crmCreditService.selectList(new CrmCredit().setCreditId(creditId).setTenantCode(CurrentUserUtil.getUserTenantCode()).setIsDeleted(false));
        CrmCreditVO crmCredit = new CrmCreditVO();
        if (CollectionUtil.isEmpty(crmCredits)) {
            crmCredit.setCreditGrantedMoney(new BigDecimal(0)).setCreditMoney(new BigDecimal(0));
        } else {
            crmCredit = crmCredits.get(0);
        }
        //计算剩余额度
        BigDecimal amount = BigDecimalUtil.subtract(crmCredit.getCreditMoney(), crmCredit.getCreditGrantedMoney());
        return amount;
    }
//8+8+8+1 2^7 192.168.3.128/25
}
