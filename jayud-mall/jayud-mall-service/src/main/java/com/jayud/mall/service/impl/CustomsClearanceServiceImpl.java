package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CustomsClearanceMapper;
import com.jayud.mall.model.bo.CustomsClearanceForm;
import com.jayud.mall.model.bo.QueryCustomsClearanceForm;
import com.jayud.mall.model.po.CustomsClearance;
import com.jayud.mall.model.vo.CustomsClearanceVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ICustomsClearanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 清关资料表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class CustomsClearanceServiceImpl extends ServiceImpl<CustomsClearanceMapper, CustomsClearance> implements ICustomsClearanceService {

    @Autowired
    CustomsClearanceMapper customsClearanceMapper;
    @Autowired
    BaseService baseService;

    @Override
    public IPage<CustomsClearanceVO> findCustomsClearanceByPage(QueryCustomsClearanceForm form) {
        //定义分页参数
        Page<CustomsClearanceVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<CustomsClearanceVO> pageInfo = customsClearanceMapper.findCustomsClearanceByPage(page, form);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveCustomsData(CustomsClearanceForm form) {
        CustomsClearance customsClearance = ConvertUtil.convert(form, CustomsClearance.class);
        Long id = form.getId();
        String idCode = form.getIdCode();
        if(id == null){
            //新增
            QueryWrapper<CustomsClearance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_code", idCode);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error(-1, "idCode,已存在");
            }
            AuthUser user = baseService.getUser();
            customsClearance.setStatus("1");
            customsClearance.setUserId(user.getId().intValue());
            customsClearance.setUserName(user.getName());
            customsClearance.setCreateTime(LocalDateTime.now());
        }else{
            //修改
            QueryWrapper<CustomsClearance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_code", idCode);
            queryWrapper.ne("id", id);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error(-1, "idCode,已存在");
            }
        }
        this.saveOrUpdate(customsClearance);
        return CommonResult.success("保存清关，成功！");
    }

    @Override
    public List<CustomsClearanceVO> findCustomsClearance() {
        QueryWrapper<CustomsClearance> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("","");
        List<CustomsClearance> list = this.list(queryWrapper);
        List<CustomsClearanceVO> customsClearanceVOS = ConvertUtil.convertList(list, CustomsClearanceVO.class);
        return customsClearanceVOS;
    }
}
