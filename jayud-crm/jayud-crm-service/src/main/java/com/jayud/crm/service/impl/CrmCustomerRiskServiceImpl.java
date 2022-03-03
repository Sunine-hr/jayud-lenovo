package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.model.bo.CrmCustomerRiskForm;
import com.jayud.crm.model.po.CrmCreditVisit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerRisk;
import com.jayud.crm.mapper.CrmCustomerRiskMapper;
import com.jayud.crm.service.ICrmCustomerRiskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_客户_风险客户（crm_customer_risk） 服务实现类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Service
public class CrmCustomerRiskServiceImpl extends ServiceImpl<CrmCustomerRiskMapper, CrmCustomerRisk> implements ICrmCustomerRiskService {


    @Autowired
    private CrmCustomerRiskMapper crmCustomerRiskMapper;

    @Override
    public IPage<CrmCustomerRisk> selectPage(CrmCustomerRiskForm crmCustomerRiskForm,
                                             Integer currentPage,
                                             Integer pageSize,
                                             HttpServletRequest req){

        Page<CrmCustomerRiskForm> page=new Page<CrmCustomerRiskForm>(currentPage,pageSize);
        IPage<CrmCustomerRisk> pageList= crmCustomerRiskMapper.pageList(page, crmCustomerRiskForm);
        return pageList;
    }

    @Override
    public List<CrmCustomerRisk> selectList(CrmCustomerRisk crmCustomerRisk){
        return crmCustomerRiskMapper.list(crmCustomerRisk);
    }

    @Override
    public BaseResult saveOrUpdateCrmCustomerRisk(CrmCustomerRiskForm crmCustomerRiskForm) {

        Boolean result = null;
        CrmCustomerRisk convert = ConvertUtil.convert(crmCustomerRiskForm, CrmCustomerRisk.class);
        if(convert.getId()!=null){
            convert.setUpdateBy(CurrentUserUtil.getUsername());
            convert.setUpdateTime(new Date());
            result = this.updateById(convert);
        }else {
            convert.setCreateBy(CurrentUserUtil.getUsername());
            convert.setCreateTime(new Date());
            result= this.saveOrUpdate(convert);
        }
        if (result) {
            log.warn("新增或修改成功");
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }
        return BaseResult.error(SysTips.EDIT_FAIL);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmCustomerRiskMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){

        CrmCustomerRisk crmCustomerRisk = new CrmCustomerRisk();
        crmCustomerRisk.setId(id);
        crmCustomerRisk.setIsDeleted(true);
        crmCustomerRiskMapper.insert(crmCustomerRisk);
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerRiskForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerRiskForExcel(paramMap);
    }

}
