package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.model.bo.CrmCustomerFeaturesForm;
import com.jayud.crm.model.po.CrmCustomerRelations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerFeatures;
import com.jayud.crm.mapper.CrmCustomerFeaturesMapper;
import com.jayud.crm.service.ICrmCustomerFeaturesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 基本档案_客户_业务特征 服务实现类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Service
public class CrmCustomerFeaturesServiceImpl extends ServiceImpl<CrmCustomerFeaturesMapper, CrmCustomerFeatures> implements ICrmCustomerFeaturesService {


    @Autowired
    private CrmCustomerFeaturesMapper crmCustomerFeaturesMapper;

    @Override
    public IPage<CrmCustomerFeatures> selectPage(CrmCustomerFeatures crmCustomerFeatures,
                                                 Integer currentPage,
                                                 Integer pageSize,
                                                 HttpServletRequest req){

        Page<CrmCustomerFeatures> page=new Page<CrmCustomerFeatures>(currentPage,pageSize);
        IPage<CrmCustomerFeatures> pageList= crmCustomerFeaturesMapper.pageList(page, crmCustomerFeatures);
        return pageList;
    }

    @Override
    public List<CrmCustomerFeatures> selectList(CrmCustomerFeatures crmCustomerFeatures){
        return crmCustomerFeaturesMapper.list(crmCustomerFeatures);
    }

    @Override
    public BaseResult saveOrUpdateCrmCustomerFeatures(CrmCustomerFeaturesForm crmCustomerFeaturesForm) {
        Boolean result = null;
        CrmCustomerFeatures convert = ConvertUtil.convert(crmCustomerFeaturesForm, CrmCustomerFeatures.class);

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
            return BaseResult.ok(SysTips.EDIT_SUCCESS);
        }
        return BaseResult.error(SysTips.EDIT_FAIL);
    }

    @Override
    public void saveCrmCustomerFeatures(Long custId) {
        List<CrmCustomerFeatures>    CrmCustomerFeaturesLists=new  ArrayList<>();
        List<String> requirement = requirement();
        for (int i = 0; i <requirement.size() ; i++) {
            CrmCustomerFeatures crmCustomerFeatures=new CrmCustomerFeatures();
            crmCustomerFeatures.setId(null);
            crmCustomerFeatures.setCustId(custId);
            crmCustomerFeatures.setFeaturesType(requirement.get(i));
            crmCustomerFeatures.setCreateBy(CurrentUserUtil.getUsername());
            crmCustomerFeatures.setCreateTime(new Date());
            CrmCustomerFeaturesLists.add(crmCustomerFeatures);
        }
        this.saveOrUpdateBatch(CrmCustomerFeaturesLists);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmCustomerFeaturesMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCustomerFeaturesMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerFeaturesForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerFeaturesForExcel(paramMap);
    }


    //业务要求类型
    public static List<String> requirement(){
        List<String> objects = new ArrayList<>();
        objects.add("提单要求");
        objects.add("结算要求");
        objects.add("订舱要求");
        objects.add("报关要求");
        objects.add("门对门要求");
        objects.add("仓储要求");
        objects.add("到港清关要求");
        objects.add("到港送货要求");
        objects.add("其他要求");
        return objects;
    }

    public static void main(String[] args) {
        List<String> requirement = requirement();
        for (int i = 0; i <requirement.size() ; i++) {
            System.out.println("   "+requirement.get(i));
        }
    }
}
