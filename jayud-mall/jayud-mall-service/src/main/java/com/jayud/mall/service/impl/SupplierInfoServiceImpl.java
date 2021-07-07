package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.SupplierInfoMapper;
import com.jayud.mall.mapper.SupplierServeMapper;
import com.jayud.mall.model.bo.QuerySupplierInfoForm;
import com.jayud.mall.model.bo.SupplierCostForm;
import com.jayud.mall.model.bo.SupplierInfoForm;
import com.jayud.mall.model.po.SupplierInfo;
import com.jayud.mall.model.po.SupplierInfoServiceTypeRelation;
import com.jayud.mall.model.vo.SupplierCostVO;
import com.jayud.mall.model.vo.SupplierInfoVO;
import com.jayud.mall.model.vo.SupplierServeVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.*;
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
    SupplierInfoMapper supplierInfoMapper;
    @Autowired
    SupplierServeMapper supplierServeMapper;
    @Autowired
    BaseService baseService;
    @Autowired
    INumberGeneratedService numberGeneratedService;
    @Autowired
    ISupplierInfoServiceTypeRelationService supplierInfoServiceTypeRelationService;
    @Autowired
    ISupplierCostService supplierCostService;

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
        String companyName = form.getCompanyName();

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

            QueryWrapper<SupplierInfo> qw = new QueryWrapper<>();
            qw.eq("company_name", companyName);
            List<SupplierInfo> list = this.list(qw);
            if(CollUtil.isNotEmpty(list)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "公司名称不能重复");
            }

        }else{
            QueryWrapper<SupplierInfo> qw = new QueryWrapper<>();
            qw.ne("id", id);
            qw.eq("company_name", companyName);
            List<SupplierInfo> list = this.list(qw);
            if(CollUtil.isNotEmpty(list)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "公司名称不能重复");
            }
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<SupplierInfoVO> enableSupplierInfo(Long id) {
        SupplierInfo supplierInfo = this.getById(id);
        if(ObjectUtil.isEmpty(supplierInfo)){
            return CommonResult.error(-1, "没有找到供应商");
        }
        //  `status` char(1) DEFAULT NULL COMMENT '状态(0无效 1有效)',
        supplierInfo.setStatus("1");
        this.saveOrUpdate(supplierInfo);
        SupplierInfoVO supplierInfoVO = ConvertUtil.convert(supplierInfo, SupplierInfoVO.class);
        return CommonResult.success(supplierInfoVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<SupplierInfoVO> disableSupplierInfo(Long id) {
        SupplierInfo supplierInfo = this.getById(id);
        if(ObjectUtil.isEmpty(supplierInfo)){
            return CommonResult.error(-1, "没有找到供应商");
        }
        //  `status` char(1) DEFAULT NULL COMMENT '状态(0无效 1有效)',
        supplierInfo.setStatus("0");
        this.saveOrUpdate(supplierInfo);
        SupplierInfoVO supplierInfoVO = ConvertUtil.convert(supplierInfo, SupplierInfoVO.class);
        return CommonResult.success(supplierInfoVO);
    }

    @Override
    public List<SupplierServeVO> findSupplierSerCostInfoById(Long supplierInfoId) {
        List<SupplierServeVO> supplierServeVOS = supplierServeMapper.findSupplierSerCostInfoById(supplierInfoId);
        if(ObjectUtil.isNotEmpty(supplierServeVOS)){
            supplierServeVOS.forEach(supplierServeVO -> {
                Long supplierInfoId2 = supplierServeVO.getSupplierInfoId();//供应商id
                Long serviceId = supplierServeVO.getId();//供应商服务id
                SupplierCostForm form1 = new SupplierCostForm();
                form1.setSupplierInfoId(supplierInfoId2);
                form1.setServiceId(serviceId);
                List<SupplierCostVO> supplierCostVOS = supplierCostService.findSupplierCost(form1);
                supplierServeVO.setSupplierCostVOList(supplierCostVOS);
            });
        }
        return supplierServeVOS;
    }
}
