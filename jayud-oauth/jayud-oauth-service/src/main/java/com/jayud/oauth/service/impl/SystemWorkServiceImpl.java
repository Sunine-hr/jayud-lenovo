package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.po.Work;
import com.jayud.model.vo.WorkVO;
import com.jayud.oauth.mapper.SystemWorkMapper;
import com.jayud.oauth.service.ISystemWorkService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-21
 */
@Service
public class SystemWorkServiceImpl extends ServiceImpl<SystemWorkMapper, Work> implements ISystemWorkService {


    @Override
    public List<WorkVO> findWork(Long departmentId) {
        QueryWrapper queryWrapper = null;
        if(departmentId != null){
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("department_id",departmentId);
        }
        return ConvertUtil.convertList(baseMapper.selectList(queryWrapper),WorkVO.class);
    }


}
