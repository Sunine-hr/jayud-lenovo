package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.admin.security.domain.AuthUser;
import com.jayud.mall.admin.security.service.BaseService;
import com.jayud.mall.mapper.CustomsDataMapper;
import com.jayud.mall.model.bo.CustomsDataForm;
import com.jayud.mall.model.bo.QueryCustomsDataForm;
import com.jayud.mall.model.po.CustomsData;
import com.jayud.mall.model.vo.CustomsDataVO;
import com.jayud.mall.service.ICustomsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 报关资料表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class CustomsDataServiceImpl extends ServiceImpl<CustomsDataMapper, CustomsData> implements ICustomsDataService {

    @Autowired
    CustomsDataMapper customsDataMapper;

    @Autowired
    BaseService baseService;

    @Override
    public IPage<CustomsDataVO> findCustomsDataByPage(QueryCustomsDataForm form) {
        //定义分页参数
        Page<CustomsDataVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<CustomsDataVO> pageInfo = customsDataMapper.findCustomsDataByPage(page, form);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveCustomsData(CustomsDataForm form) {
        CustomsData customsData = ConvertUtil.convert(form, CustomsData.class);
        Long id = form.getId();
        if(id == null){
            //新增
            String idCode = form.getIdCode();
            QueryWrapper<CustomsData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_code", idCode);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error( -1, "idCode,已存在。");
            }
            AuthUser user = baseService.getUser();
            customsData.setStatus("1");
            customsData.setUserId(user.getId().intValue());
            customsData.setUserName(user.getName());
            customsData.setCreateTime(LocalDateTime.now());
        }else{
            //修改
            String idCode = form.getIdCode();
            QueryWrapper<CustomsData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_code", idCode);
            queryWrapper.ne("id", id);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error( -1, "idCode,已存在。");
            }
        }
        this.saveOrUpdate(customsData);
        return CommonResult.success("保存报关资料，成功!");
    }
}
