package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.crm.model.bo.CrmCustomerAddressForm;
import com.jayud.crm.model.po.CrmCreditVisit;
import com.jayud.crm.model.po.CrmCustomerRisk;
import com.jayud.crm.model.vo.CrmCustomerAddressVO;
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
import java.util.*;

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
    public IPage<CrmCustomerAddressVO> selectPage(CrmCustomerAddress crmCustomerAddress,
                                                  Integer currentPage,
                                                  Integer pageSize,
                                                  HttpServletRequest req){

        Page<CrmCustomerAddress> page=new Page<CrmCustomerAddress>(currentPage,pageSize);
        IPage<CrmCustomerAddressVO> pageList= crmCustomerAddressMapper.pageList(page, crmCustomerAddress);
        pageList.getRecords().stream().forEach(v->{
            v.setAddress(StringUtils.getConvenientAreaString(v.getProvince(),v.getCity(),v.getCounty(),v.getAddress()));
        });
        return pageList;
    }

    @Override
    public List<CrmCustomerAddressVO> selectList(CrmCustomerAddress crmCustomerAddress){
        List<CrmCustomerAddressVO> list = crmCustomerAddressMapper.list(crmCustomerAddress);
        list.stream().forEach(v->{
            v.setAddress(StringUtils.getConvenientAreaString(v.getProvince(),v.getCity(),v.getCounty(),v.getAddress()));
        });
        return list;
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
    public void logicDel(List<Long> ids){
        List<CrmCustomerAddress> crmCustomerAddressList = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            CrmCustomerAddress crmCustomerAddress = new CrmCustomerAddress();
            crmCustomerAddress.setId(ids.get(i));
            crmCustomerAddress.setIsDeleted(true);
            crmCustomerAddressList.add(crmCustomerAddress);
        }
        this.updateBatchById(crmCustomerAddressList);
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerAddressForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerAddressForExcel(paramMap);
    }

}
