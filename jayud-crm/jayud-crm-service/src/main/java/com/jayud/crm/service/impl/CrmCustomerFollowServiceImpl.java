package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.mapper.CrmFileMapper;
import com.jayud.crm.model.bo.CrmCustomerFollowForm;
import com.jayud.crm.model.constant.CrmDictCode;
import com.jayud.crm.model.po.CrmCreditVisit;
import com.jayud.crm.model.po.CrmFile;
import com.jayud.crm.model.vo.CrmCustomerFollowVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerFollow;
import com.jayud.crm.mapper.CrmCustomerFollowMapper;
import com.jayud.crm.service.ICrmCustomerFollowService;
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
 * 基本档案_客户_跟进记录(crm_customer_follow) 服务实现类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Service
public class CrmCustomerFollowServiceImpl extends ServiceImpl<CrmCustomerFollowMapper, CrmCustomerFollow> implements ICrmCustomerFollowService {


    @Autowired
    private CrmCustomerFollowMapper crmCustomerFollowMapper;
    @Autowired
    private CrmFileMapper crmFileMapper;

    @Override
    public IPage<CrmCustomerFollowVO> selectPage(CrmCustomerFollow crmCustomerFollow,
                                                 Integer currentPage,
                                                 Integer pageSize,
                                                 HttpServletRequest req){

        Page<CrmCustomerFollow> page=new Page<CrmCustomerFollow>(currentPage,pageSize);
        IPage<CrmCustomerFollowVO> pageList= crmCustomerFollowMapper.pageList(page, crmCustomerFollow);

        pageList.getRecords().stream().forEach(v->{

            //根据跟进记录id查询关联文件表
            CrmFile crmFile = new CrmFile();
            crmFile.setCode(CrmDictCode.crmCustomerFollowCode);
            crmFile.setBusinessId(v.getId());
            List<CrmFile> list = crmFileMapper.list(crmFile);
            v.setCrmFiles(list);

        });

        return pageList;
    }

    @Override
    public List<CrmCustomerFollow> selectList(CrmCustomerFollow crmCustomerFollow){
        return crmCustomerFollowMapper.list(crmCustomerFollow);
    }

    @Override
    public BaseResult saveOrUpdateCrmCustomerFollow(CrmCustomerFollowForm crmCustomerFollowForm) {
        Boolean result = null;
        CrmCustomerFollow convert = ConvertUtil.convert(crmCustomerFollowForm, CrmCustomerFollow.class);

        if(convert.getId()!=null){
            convert.setUpdateBy(CurrentUserUtil.getUsername());
            convert.setUpdateTime(new Date());
            result = this.updateById(convert);
            //修改待定

        }else {

            convert.setUpdateBy(CurrentUserUtil.getUsername());
            convert.setUpdateTime(new Date());
            result= this.saveOrUpdate(convert);
            Long id = convert.getId();

            for (int i = 0; i < crmCustomerFollowForm.getQueryCrmFiles().size(); i++) {
                CrmFile crmFile = new CrmFile();
                //客户跟进记录表id
                crmFile.setBusinessId(id);
                //客户表示
                crmFile.setCode(CrmDictCode.crmCustomerFollowCode);
                //文件名称
                crmFile.setFileName(crmCustomerFollowForm.getQueryCrmFiles().get(i).getFileName());
                //文件路径
                crmFile.setUploadFileUrl(crmCustomerFollowForm.getQueryCrmFiles().get(i).getUploadFileUrl());
                crmFile.setCreateBy(CurrentUserUtil.getUsername());
                crmFile.setCreateTime(new Date());
                crmFileMapper.insert(crmFile);
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
    public void phyDelById(Long id){
        crmCustomerFollowMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCustomerFollowMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerFollowForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerFollowForExcel(paramMap);
    }

}
