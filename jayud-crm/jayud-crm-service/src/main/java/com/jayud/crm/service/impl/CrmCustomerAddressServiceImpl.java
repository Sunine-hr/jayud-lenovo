package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.model.bo.CrmCustomerAddressForm;
import com.jayud.crm.model.po.CrmCreditVisit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerAddress;
import com.jayud.crm.mapper.CrmCustomerAddressMapper;
import com.jayud.crm.service.ICrmCustomerAddressService;
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
 * 基本档案_客户_地址 服务实现类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Service
public class CrmCustomerAddressServiceImpl extends ServiceImpl<CrmCustomerAddressMapper, CrmCustomerAddress> implements ICrmCustomerAddressService {


    @Autowired
    private CrmCustomerAddressMapper crmCustomerAddressMapper;

    @Override
    public IPage<CrmCustomerAddress> selectPage(CrmCustomerAddress crmCustomerAddress,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CrmCustomerAddress> page=new Page<CrmCustomerAddress>(currentPage,pageSize);
        IPage<CrmCustomerAddress> pageList= crmCustomerAddressMapper.pageList(page, crmCustomerAddress);
        return pageList;
    }

    @Override
    public List<CrmCustomerAddress> selectList(CrmCustomerAddress crmCustomerAddress){
        return crmCustomerAddressMapper.list(crmCustomerAddress);
    }

    @Override
    public BaseResult saveOrUpdateCrmCustomerAddress(CrmCustomerAddressForm crmCustomerAddressForm) {
        Boolean result = null;
        CrmCustomerAddress convert = ConvertUtil.convert(crmCustomerAddressForm, CrmCustomerAddress.class);
        if(convert.getId()!=null){
            convert.setUpdateBy(CurrentUserUtil.getUsername());
            convert.setUpdateTime(new Date());
            result = this.updateById(convert);
        }else {
            convert.setUpdateBy(CurrentUserUtil.getUsername());
            convert.setUpdateTime(new Date());
            result = this.saveOrUpdate(convert);
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
        crmCustomerAddressMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCustomerAddressMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerAddressForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerAddressForExcel(paramMap);
    }

}
