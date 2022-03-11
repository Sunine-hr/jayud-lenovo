package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.constant.SysTips;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.BigDecimalUtil;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.model.bo.AddCrmCreditForm;
import com.jayud.crm.model.enums.FileModuleEnum;
import com.jayud.crm.model.po.CrmCreditDepart;
import com.jayud.crm.model.po.CrmCustomerAgreement;
import com.jayud.crm.model.vo.CrmCreditVO;
import com.jayud.crm.service.ICrmCreditDepartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCredit;
import com.jayud.crm.mapper.CrmCreditMapper;
import com.jayud.crm.service.ICrmCreditService;
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
 * 基本档案_额度_额度总量(crm_credit) 服务实现类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Service
public class CrmCreditServiceImpl extends ServiceImpl<CrmCreditMapper, CrmCredit> implements ICrmCreditService {


    @Autowired
    private CrmCreditMapper crmCreditMapper;
    @Autowired
    private ICrmCreditDepartService crmCreditDepartService;

    @Override
    public IPage<CrmCreditVO> selectPage(CrmCredit crmCredit,
                                         Integer currentPage,
                                         Integer pageSize,
                                         HttpServletRequest req) {

        Page<CrmCredit> page = new Page<CrmCredit>(currentPage, pageSize);
        IPage<CrmCreditVO> pageList = crmCreditMapper.pageList(page, crmCredit);
        for (CrmCreditVO record : pageList.getRecords()) {
//            List<CrmCre
//            ditDepart> creditDeparts = crmCreditDepartService.selectList(new CrmCreditDepart().setCreditId(record.getCreditId()).setIsDeleted(false).setTenantCode(CurrentUserUtil.getUserTenantCode()));
//            BigDecimal amount = new BigDecimal(0);
//            creditDeparts.forEach(e -> {
//                amount=BigDecimalUtil.add(e.getCreditAmt())
//            });
            BigDecimal creditGrantedMoney = record.getCreditGrantedMoney() == null ? new BigDecimal(0) : record.getCreditGrantedMoney();
            if (creditGrantedMoney.compareTo(new BigDecimal(0)) == 0) {
                record.setCreditRate(0 + "%");
            } else {
                record.setCreditRate(creditGrantedMoney.divide(record.getCreditMoney(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
            }
        }
        return pageList;
    }

    @Override
    public List<CrmCreditVO> selectList(CrmCredit crmCredit) {
        return crmCreditMapper.list(crmCredit);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        crmCreditMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id) {
        crmCreditMapper.logicDel(id, CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCreditForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCreditForExcel(paramMap);
    }

    @Override
    public void saveOrUpdate(AddCrmCreditForm form) {
        CrmCredit tmp = ConvertUtil.convert(form, CrmCredit.class);
        if (form.getId() == null) {
            if (this.exitNumber(form.getCreditValue())) {
                throw new JayudBizException(400, SysTips.TYPE_ALREADY_EXISTS);
            }
            tmp.setTenantCode(CurrentUserUtil.getUserTenantCode());
        } else {
            tmp.setUpdateBy(CurrentUserUtil.getUsername());
        }
        this.saveOrUpdate(tmp);
    }

    @Override
    public boolean exitNumber(String creditValue) {
        QueryWrapper<CrmCredit> condition = new QueryWrapper<>(new CrmCredit().setCreditValue(creditValue).setIsDeleted(false).setTenantCode(CurrentUserUtil.getUserTenantCode()));
        return this.count(condition) > 0;
    }

}
