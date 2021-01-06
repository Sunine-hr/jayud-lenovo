package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.SupplierInfoMapper;
import com.jayud.mall.model.bo.QuerySupplierInfoForm;
import com.jayud.mall.model.bo.SupplierInfoForm;
import com.jayud.mall.model.po.SupplierInfo;
import com.jayud.mall.model.po.SupplierInfoServiceTypeRelation;
import com.jayud.mall.model.vo.SupplierInfoVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.INumberGeneratedService;
import com.jayud.mall.service.ISupplierInfoService;
import com.jayud.mall.service.ISupplierInfoServiceTypeRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    BaseService baseService;
    @Autowired
    INumberGeneratedService numberGeneratedService;
    @Autowired
    SupplierInfoMapper supplierInfoMapper;
    @Autowired
    ISupplierInfoServiceTypeRelationService supplierInfoServiceTypeRelationService;

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
        page.addOrder(OrderItem.asc("t.id"));
        IPage<SupplierInfoVO> pageInfo = supplierInfoMapper.findSupplierInfoByPage(page, form);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveSupplierInfo(SupplierInfoForm form) {
        SupplierInfo supplierInfo = ConvertUtil.convert(form, SupplierInfo.class);
        Long id = form.getId();
        if(id == null){
            //新增
            //供应商代码
            String supplier_code = numberGeneratedService.getOrderNoByCode("supplier_code");
            supplierInfo.setSupplierCode(supplier_code);
            //创建人
            AuthUser user = baseService.getUser();
            supplierInfo.setUserId(user.getId().intValue());
            supplierInfo.setUserName(user.getName());
            supplierInfo.setCreateTime(LocalDateTime.now());
        }
        //1.保存供应商
        this.saveOrUpdate(supplierInfo);
        Long infoId = supplierInfo.getId();

        QueryWrapper<SupplierInfoServiceTypeRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("info_id", infoId);
        //2.删除供应商和服务类型关联信息
        supplierInfoServiceTypeRelationService.remove(queryWrapper);

        List<Long> serviceTypeIds = form.getServiceTypeIds();
        List<SupplierInfoServiceTypeRelation> supplierInfoServiceTypeRelations = new ArrayList<>();
        serviceTypeIds.forEach(serviceTypeId -> {
            SupplierInfoServiceTypeRelation supplierInfoServiceTypeRelation = new SupplierInfoServiceTypeRelation();
            supplierInfoServiceTypeRelation.setInfoId(infoId);
            supplierInfoServiceTypeRelation.setServiceTypeId(serviceTypeId);
            supplierInfoServiceTypeRelations.add(supplierInfoServiceTypeRelation);
        });
        //3.保存供应商和服务类型关联信息
        supplierInfoServiceTypeRelationService.saveOrUpdateBatch(supplierInfoServiceTypeRelations);

        return CommonResult.success("保存供应商，成功！");
    }

    @Override
    public CommonResult<SupplierInfoVO> findSupplierInfoById(Long id) {
        SupplierInfo supplierInfo = this.getById(id);
        Long infoId = supplierInfo.getId();
        QueryWrapper<SupplierInfoServiceTypeRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("info_id", infoId);
        List<SupplierInfoServiceTypeRelation> list = supplierInfoServiceTypeRelationService.list(queryWrapper);
        List<Long> serviceTypeIds = new ArrayList<>();
        list.forEach(supplierInfoServiceTypeRelation -> {
            Long serviceTypeId = supplierInfoServiceTypeRelation.getServiceTypeId();
            serviceTypeIds.add(serviceTypeId);
        });
        SupplierInfoVO supplierInfoVO = ConvertUtil.convert(supplierInfo, SupplierInfoVO.class);
        supplierInfoVO.setServiceTypeIds(serviceTypeIds);
        return CommonResult.success(supplierInfoVO);
    }
}
