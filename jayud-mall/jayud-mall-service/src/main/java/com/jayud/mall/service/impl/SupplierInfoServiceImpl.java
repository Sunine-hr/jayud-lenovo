package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.admin.security.domain.AuthUser;
import com.jayud.mall.admin.security.service.BaseService;
import com.jayud.mall.mapper.SupplierInfoMapper;
import com.jayud.mall.model.bo.QuerySupplierInfoForm;
import com.jayud.mall.model.bo.SupplierInfoForm;
import com.jayud.mall.model.po.SupplierInfo;
import com.jayud.mall.model.vo.SupplierInfoVO;
import com.jayud.mall.service.INumberGeneratedService;
import com.jayud.mall.service.ISupplierInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 供应商信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class SupplierInfoServiceImpl extends ServiceImpl<SupplierInfoMapper, SupplierInfo> implements ISupplierInfoService {

    @Autowired
    SupplierInfoMapper supplierInfoMapper;
    @Autowired
    INumberGeneratedService numberGeneratedService;
    @Autowired
    BaseService baseService;

    @Override
    public List<SupplierInfoVO> findSupplierInfo(QuerySupplierInfoForm form) {
        QueryWrapper<SupplierInfo> queryWrapper = new QueryWrapper<>();
        String keyword = form.getKeyword();
        if(keyword != null && keyword != ""){
            queryWrapper.like("supplier_code", keyword).or().like("supplier_ch_name", keyword);
        }
        List<SupplierInfo> supplierInfos = supplierInfoMapper.selectList(queryWrapper);
        List<SupplierInfoVO> list = new ArrayList<>();
        supplierInfos.forEach(supplierInfo -> {
            SupplierInfoVO supplierInfoVO = ConvertUtil.convert(supplierInfo, SupplierInfoVO.class);
            list.add(supplierInfoVO);
        });
        return list;
    }

    @Override
    public IPage<SupplierInfoVO> findSupplierInfoByPage(QuerySupplierInfoForm form) {
        //定义分页参数
        Page<SupplierInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<SupplierInfoVO> pageInfo = supplierInfoMapper.findSupplierInfoByPage(page, form);
        return pageInfo;
    }

    @Override
    public CommonResult saveSupplierInfo(SupplierInfoForm form) {
        SupplierInfo supplierInfo = ConvertUtil.convert(form, SupplierInfo.class);
        Long id = form.getId();
        String supplierCode = form.getSupplierCode();
        //供应商代码
        String supplier_code = numberGeneratedService.getOrderNoByCode("supplier_code");
        supplierInfo.setSupplierCode(supplier_code);
        //创建人
        AuthUser user = baseService.getUser();
        supplierInfo.setUserId(user.getId().intValue());
        supplierInfo.setUserName(user.getName());
        supplierInfo.setCreateTime(LocalDateTime.now());

        this.saveOrUpdate(supplierInfo);
        return CommonResult.success("保存供应商，成功！");
    }
}
