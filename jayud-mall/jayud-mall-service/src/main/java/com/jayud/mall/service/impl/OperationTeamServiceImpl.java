package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OperationTeamMapper;
import com.jayud.mall.mapper.OperationTeamMemberMapper;
import com.jayud.mall.model.bo.OperationTeamForm;
import com.jayud.mall.model.bo.QueryOperationTeamForm;
import com.jayud.mall.model.po.OperationTeam;
import com.jayud.mall.model.po.OperationTeamMember;
import com.jayud.mall.model.vo.OperationTeamMemberVO;
import com.jayud.mall.model.vo.OperationTeamVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IOperationTeamMemberService;
import com.jayud.mall.service.IOperationTeamService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 运营(服务)小组 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
@Service
public class OperationTeamServiceImpl extends ServiceImpl<OperationTeamMapper, OperationTeam> implements IOperationTeamService {

    @Autowired
    OperationTeamMapper operationTeamMapper;
    @Autowired
    OperationTeamMemberMapper operationTeamMemberMapper;
    @Autowired
    BaseService baseService;
    @Autowired
    IOperationTeamMemberService operationTeamMemberService;

    @Override
    public List<OperationTeamVO> findOperationTeam(OperationTeamForm form) {
        Long id = form.getId();
        QueryWrapper<OperationTeam> queryWrapper = new QueryWrapper<>();
        if(id != null){
            queryWrapper.eq("id", id);
        }
        String groupCode = form.getGroupCode();
        if(groupCode != null && groupCode != ""){
            queryWrapper.like("group_code", groupCode);
        }
        String groupName = form.getGroupName();
        if(groupName != null && groupName != ""){
            queryWrapper.like("group_name", groupName);
        }
        String status = form.getStatus();
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        List<OperationTeam> operationTeams = operationTeamMapper.selectList(queryWrapper);
        List<OperationTeamVO> operationTeamVOS = ConvertUtil.convertList(operationTeams, OperationTeamVO.class);
        return operationTeamVOS;
    }

    @Override
    public IPage<OperationTeamVO> findOperationTeamByPage(QueryOperationTeamForm form) {
        //定义分页参数
        Page<OperationTeamVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("t.id"));
        IPage<OperationTeamVO> pageInfo = operationTeamMapper.findOperationTeamByPage(page, form);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<OperationTeamVO> saveOperationTeam(OperationTeamForm form) {
        OperationTeam operationTeam = ConvertUtil.convert(form, OperationTeam.class);
        AuthUser user = baseService.getUser();
        Long id = operationTeam.getId();
        String groupName = form.getGroupName();
        if(ObjectUtil.isEmpty(id)){
            //id为空，代表新增
            QueryWrapper<OperationTeam> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("group_name", groupName);
            List<OperationTeam> list = this.list(queryWrapper);
            if(!ObjectUtil.isEmpty(list)){
                return CommonResult.error(-1, "["+groupName+"],运营组名称已存在");
            }
            String groupCode = NumberGeneratedUtils.getOrderNoByCode2("operation_team_code");
            operationTeam.setGroupCode(groupCode);
            operationTeam.setCreator(user.getId());
        }else{
            QueryWrapper<OperationTeam> queryWrapper = new QueryWrapper<>();
            queryWrapper.ne("id", id);
            queryWrapper.eq("group_name", groupName);
            List<OperationTeam> list = this.list(queryWrapper);
            if(!ObjectUtil.isEmpty(list)){
                return CommonResult.error(-1, "["+groupName+"],运营组名称已存在");
            }
        }
        //1.保存运营组
        this.saveOrUpdate(operationTeam);
        Long operationTeamId = operationTeam.getId();//分组id
        List<OperationTeamMemberVO> operationTeamMemberVOS = form.getOperationTeamMemberVOS();
        List<OperationTeamMember> operationTeamMembers = ConvertUtil.convertList(operationTeamMemberVOS, OperationTeamMember.class);
        operationTeamMembers.forEach(operationTeamMember -> {
            operationTeamMember.setOperationTeamId(operationTeamId);
            operationTeamMember.setCreator(user.getId());
        });
        QueryWrapper<OperationTeamMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operation_team_id", operationTeamId);
        operationTeamMemberService.remove(queryWrapper);
        operationTeamMemberService.saveOrUpdateBatch(operationTeamMembers);
        OperationTeamVO operationTeamVO = ConvertUtil.convert(operationTeam, OperationTeamVO.class);
        return CommonResult.success(operationTeamVO);
    }

    @Override
    public CommonResult<OperationTeamVO> findOperationTeamById(Long id) {
        OperationTeamVO operationTeamVO = operationTeamMapper.findOperationTeamById(id);
        if(ObjectUtil.isEmpty(operationTeamVO)){
            return CommonResult.error(-1, "运营组不存在");
        }
        Long operationTeamId = operationTeamVO.getId();
        //运营组人员list
        List<OperationTeamMemberVO> operationTeamMemberVOS = operationTeamMemberMapper.findOperationTeamMemberByOperationTeamId(operationTeamId);
        operationTeamVO.setOperationTeamMemberVOS(operationTeamMemberVOS);
        return CommonResult.success(operationTeamVO);
    }
}
