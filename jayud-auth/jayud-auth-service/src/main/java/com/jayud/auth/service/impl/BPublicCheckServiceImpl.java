package com.jayud.auth.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.CheckForm;
import com.jayud.auth.model.po.BNoRule;
import com.jayud.auth.service.IBNoRuleService;
import com.jayud.auth.service.IPublicCheckService;
import com.jayud.auth.service.ISysRoleService;
import com.jayud.auth.service.ISysUserRoleService;
import com.jayud.common.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.BPublicCheck;
import com.jayud.auth.mapper.BPublicCheckMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审核记录表 服务实现类
 *
 * @author jayud
 * @since 2022-02-28
 */
@Slf4j
@Service
public class BPublicCheckServiceImpl extends ServiceImpl<BPublicCheckMapper, BPublicCheck> implements IPublicCheckService {


    @Autowired
    private BPublicCheckMapper bPublicCheckMapper;

    @Autowired
    private IBNoRuleService bNoRuleService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Override
    public IPage<BPublicCheck> selectPage(BPublicCheck bPublicCheck,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<BPublicCheck> page=new Page<BPublicCheck>(currentPage,pageSize);
        IPage<BPublicCheck> pageList= bPublicCheckMapper.pageList(page, bPublicCheck);
        return pageList;
    }

    @Override
    public List<BPublicCheck> selectList(BPublicCheck bPublicCheck){
        return bPublicCheckMapper.list(bPublicCheck);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        bPublicCheckMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        bPublicCheckMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public BaseResult check(CheckForm checkForm) {
        //根据当前单据编号获取表名、库名
        BNoRule bNoRule = bNoRuleService.getNoRulesBySheetCode(checkForm.getSheetCode());
        String checkTable = bNoRule.getCheckTable();

        //根据id获取当前记录
        Map<String,Object> map = new HashMap<>();
        if(null == bNoRule.getCheckDatabase()){
            map  = bPublicCheckMapper.getData(checkForm.getRecordId(),checkTable);
        }else{
            map  = bPublicCheckMapper.getCheckData(checkForm.getRecordId(),checkTable,bNoRule.getCheckDatabase());
        }

        //判断当前单据审核状态  s_level <= s_step  已审核完成
        Integer sLevel = (Integer)map.get("sLevel");
        Integer sStep = (Integer)map.get("sStep");
        if(sLevel <= sStep){
            return BaseResult.error(444,"该订单已审核完成");
        }
        //当前用户不为管理员，判断该用户的审核权限
        int count = sysUserRoleService.getCountByUserNameAndRoleName(CurrentUserUtil.getUsername(),"super_admin",CurrentUserUtil.getUserTenantCode());
        if(count <= 0){
            //判断该用户有无审核按钮权限
            int check = sysUserRoleService.getCountByUserNameAndRoleName(CurrentUserUtil.getUsername(),"super_admin",CurrentUserUtil.getUserTenantCode());
            //判断该用户是否有该级审核权限
            //判断是否审核金额  暂时不控制

            //判断连续两次审核能否为同一人

        }


        //执行审核

        //添加审核记录

        return null;
    }

    @Override
    public BaseResult unCheck(CheckForm checkForm) {
        //根据当前单据编号获取表名、库名

        //根据id获取当前记录

        //判断当前审核步长  为0 当前订单未审核

        //当前用户不为管理员，判断该用户的审核权限
        //判断该用户有无审核按钮权限
        //判断该用户是否有该级审核权限
        //判断是否审核金额  暂时不控制

        //执行反审核

        //添加反审核记录
        return null;
    }

}
