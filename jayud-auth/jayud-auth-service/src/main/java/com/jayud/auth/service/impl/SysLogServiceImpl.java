package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.SysLogForm;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.model.vo.SysLogVO;
import com.jayud.auth.service.ISysUserService;
import com.jayud.common.HttpContextUtils;
import com.jayud.common.enums.SysLogTypeEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysLog;
import com.jayud.auth.mapper.SysLogMapper;
import com.jayud.auth.service.ISysLogService;
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
 * 系统日志表 服务实现类
 *
 * @author jayud
 * @since 2022-03-22
 */
@Slf4j
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {


    @Autowired
    private SysLogMapper sysLogMapper;
    @Autowired
    private ISysUserService sysUserService;

    @Override
    public IPage<SysLogVO> selectPage(SysLogForm sysLogForm,
                                    Integer currentPage,
                                    Integer pageSize,
                                    HttpServletRequest req){

        Page<SysLogForm> page=new Page<SysLogForm>(currentPage,pageSize);
        IPage<SysLogVO> pageList= sysLogMapper.pageList(page, sysLogForm);
        return pageList;
    }

    @Override
    public List<SysLog> selectList(SysLog sysLog){
        return sysLogMapper.list(sysLog);
    }

    @Override
    public void saveOrUpdateSysLog(SysLog sysLog) {
        SysLog convert = ConvertUtil.convert(sysLog, SysLog.class);

          this.saveOrUpdate(convert);
    }

    @Override
    public void saveOrUpdateSysLogClient(String logContent,Long businessId) {

        //拿到用户真实名称
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getName, CurrentUserUtil.getUsername());
        SysUser one = sysUserService.getOne(lambdaQueryWrapper);


        SysLog sysLog = new SysLog();
        //内容
        sysLog.setLogContent(logContent);
        sysLog.setIp("127.0.0.1");
        sysLog.setOperateType(SysLogTypeEnum.COMMON.getType());
        sysLog.setBusinessId(businessId);
        sysLog.setLogType(1);
        sysLog.setCostTime(100L);
        //操作用户名称
        sysLog.setUsername(CurrentUserUtil.getUsername());
        //用户真实名称
        sysLog.setTrueName(one.getUserName());

        sysLog.setCreateBy(CurrentUserUtil.getUsername());
        sysLog.setCreateTime(new Date());


        this.saveOrUpdate(sysLog);

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysLogMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysLogMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> querySysLogForExcel(SysLogForm sysLogForm) {
        return this.baseMapper.querySysLogForExcel(sysLogForm);
    }

}
