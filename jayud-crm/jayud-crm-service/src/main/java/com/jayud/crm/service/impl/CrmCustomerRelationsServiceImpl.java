package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.model.bo.CrmCustomerRelationsForm;
import com.jayud.crm.model.po.CrmCreditVisit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerRelations;
import com.jayud.crm.mapper.CrmCustomerRelationsMapper;
import com.jayud.crm.service.ICrmCustomerRelationsService;
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
 * 基本档案_客户_联系人(crm_customer_relations) 服务实现类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Service
public class CrmCustomerRelationsServiceImpl extends ServiceImpl<CrmCustomerRelationsMapper, CrmCustomerRelations> implements ICrmCustomerRelationsService {


    @Autowired
    private CrmCustomerRelationsMapper crmCustomerRelationsMapper;

    @Override
    public IPage<CrmCustomerRelations> selectPage(CrmCustomerRelations crmCustomerRelations,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CrmCustomerRelations> page=new Page<CrmCustomerRelations>(currentPage,pageSize);
        IPage<CrmCustomerRelations> pageList= crmCustomerRelationsMapper.pageList(page, crmCustomerRelations);
        return pageList;
    }

    @Override
    public List<CrmCustomerRelations> selectList(CrmCustomerRelations crmCustomerRelations){
        return crmCustomerRelationsMapper.list(crmCustomerRelations);
    }

    @Override
    public BaseResult saveOrUpdateCrmCustomerRelations(CrmCustomerRelationsForm crmCustomerRelationsForm) {
        Boolean result = null;
        CrmCustomerRelations convert = ConvertUtil.convert(crmCustomerRelationsForm, CrmCustomerRelations.class);

        if(convert.getId()!=null){
            convert.setUpdateBy(CurrentUserUtil.getUsername());
            convert.setUpdateTime(new Date());
            result = this.updateById(convert);
            Long id = convert.getId();
        }else {
            convert.setUpdateBy(CurrentUserUtil.getUsername());
            convert.setUpdateTime(new Date());

            result = this.saveOrUpdate(convert);
            Long id = convert.getId();
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
        crmCustomerRelationsMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCustomerRelationsMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerRelationsForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerRelationsForExcel(paramMap);
    }

}
