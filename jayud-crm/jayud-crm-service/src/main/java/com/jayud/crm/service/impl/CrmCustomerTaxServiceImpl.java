package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.model.bo.CrmCustomerTaxForm;
import com.jayud.crm.model.po.CrmCustomerRelations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerTax;
import com.jayud.crm.mapper.CrmCustomerTaxMapper;
import com.jayud.crm.service.ICrmCustomerTaxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 开票资料 服务实现类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Service
public class CrmCustomerTaxServiceImpl extends ServiceImpl<CrmCustomerTaxMapper, CrmCustomerTax> implements ICrmCustomerTaxService {


    @Autowired
    private CrmCustomerTaxMapper crmCustomerTaxMapper;

    @Override
    public IPage<CrmCustomerTax> selectPage(CrmCustomerTax crmCustomerTax,
                                            Integer currentPage,
                                            Integer pageSize,
                                            HttpServletRequest req){

        Page<CrmCustomerTax> page=new Page<CrmCustomerTax>(currentPage,pageSize);
        IPage<CrmCustomerTax> pageList= crmCustomerTaxMapper.pageList(page, crmCustomerTax);
        return pageList;
    }

    @Override
    public List<CrmCustomerTax> selectList(CrmCustomerTax crmCustomerTax){
        return crmCustomerTaxMapper.list(crmCustomerTax);
    }

    @Override
    public BaseResult saveOrUpdateCrmCustomerTax(CrmCustomerTaxForm crmCustomerTaxForm) {

        Boolean result = null;
        CrmCustomerTax convert = ConvertUtil.convert(crmCustomerTaxForm, CrmCustomerTax.class);

        if(convert.getId()!=null){
            Long custId = convert.getCustId();//客户id
            if(convert.getIsDefault()==true){
                updateCrmCustomerTax(custId);
            }
            convert.setUpdateBy(CurrentUserUtil.getUsername());
            convert.setUpdateTime(new Date());
            result = this.updateById(convert);

            return BaseResult.ok(SysTips.EDIT_SUCCESS);
        }else {
            Long custId = convert.getCustId();//客户id
            if(convert.getIsDefault()==true){
                updateCrmCustomerTax(custId);
            }
            convert.setCreateBy(CurrentUserUtil.getUsername());
            convert.setCreateTime(new Date());
            result= this.saveOrUpdate(convert);

            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmCustomerTaxMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(List<Long> ids){
        List<CrmCustomerTax> crmCustomerTaxList = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            CrmCustomerTax crmCustomerTax = new CrmCustomerTax();
            crmCustomerTax.setId(ids.get(i));
            crmCustomerTax.setIsDeleted(true);
            crmCustomerTaxList.add(crmCustomerTax);
        }
        this.updateBatchById(crmCustomerTaxList);
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerTaxForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerTaxForExcel(paramMap);
    }


    public void updateCrmCustomerTax(Long custId){
        CrmCustomerTax crmCustomerTax = new CrmCustomerTax();
        crmCustomerTax.setCustId(custId);
        //创建开票资料修改这个用户下的所有的开票资料为已失效
        crmCustomerTaxMapper.updateCrmCustomerTaxList(crmCustomerTax);
    }

}
