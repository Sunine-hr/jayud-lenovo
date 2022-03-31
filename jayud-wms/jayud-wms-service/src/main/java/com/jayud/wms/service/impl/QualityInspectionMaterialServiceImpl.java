package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.wms.model.bo.QueryQualityInspectionMaterialForm;
import com.jayud.wms.model.po.QualityInspectionMaterial;
import com.jayud.wms.mapper.QualityInspectionMaterialMapper;
import com.jayud.wms.model.vo.QualityInspectionMaterialVO;
import com.jayud.wms.model.vo.QualityInspectionVO;
import com.jayud.wms.service.IQualityInspectionMaterialService;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.service.IQualityInspectionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 质检物料信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Service
public class QualityInspectionMaterialServiceImpl extends ServiceImpl<QualityInspectionMaterialMapper, QualityInspectionMaterial> implements IQualityInspectionMaterialService {

    @Autowired
    private IQualityInspectionService qualityInspectionService;

    @Autowired
    private QualityInspectionMaterialMapper qualityInspectionMaterialMapper;

    @Override
    public IPage<QualityInspectionMaterial> selectPage(QualityInspectionMaterial qualityInspectionMaterial,
                                                       Integer pageNo,
                                                       Integer pageSize,
                                                       HttpServletRequest req) {

        Page<QualityInspectionMaterial> page = new Page<QualityInspectionMaterial>(pageNo, pageSize);
        IPage<QualityInspectionMaterial> pageList = qualityInspectionMaterialMapper.pageList(page, qualityInspectionMaterial);
        return pageList;
    }

    @Override
    public List<QualityInspectionMaterial> selectList(QualityInspectionMaterial qualityInspectionMaterial) {
        return qualityInspectionMaterialMapper.list(qualityInspectionMaterial);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QualityInspectionMaterial saveOrUpdateQualityInspectionMaterial(QualityInspectionMaterial qualityInspectionMaterial) {
        Long id = qualityInspectionMaterial.getId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            qualityInspectionMaterial.setCreateBy(CurrentUserUtil.getUsername());
            qualityInspectionMaterial.setCreateTime(new Date());

            QueryWrapper<QualityInspectionMaterial> qualityInspectionMaterialQueryWrapper = new QueryWrapper<>();
//            qualityInspectionMaterialQueryWrapper.lambda().eq(QualityInspectionMaterial::getCode, qualityInspectionMaterialg.getCode());
            qualityInspectionMaterialQueryWrapper.lambda().eq(QualityInspectionMaterial::getIsDeleted, 0);
            List<QualityInspectionMaterial> list = this.list(qualityInspectionMaterialQueryWrapper);
            if (CollUtil.isNotEmpty(list)) {
                throw new IllegalArgumentException("编号已存在，操作失败");
            }

        } else {
            //修改 --> update 更新人、更新时间
            qualityInspectionMaterial.setUpdateBy(CurrentUserUtil.getUsername());
            qualityInspectionMaterial.setUpdateTime(new Date());

            QueryWrapper<QualityInspectionMaterial> qualityInspectionMaterialQueryWrapper = new QueryWrapper<>();
            qualityInspectionMaterialQueryWrapper.lambda().ne(QualityInspectionMaterial::getId, id);
//            qualityInspectionMaterialQueryWrapper.lambda().eq(QualityInspectionMaterial::getCode, qualityInspectionMaterial.getCode());
            qualityInspectionMaterialQueryWrapper.lambda().eq(QualityInspectionMaterial::getIsDeleted, 0);
            List<QualityInspectionMaterial> list = this.list(qualityInspectionMaterialQueryWrapper);
            if (CollUtil.isNotEmpty(list)) {
                throw new IllegalArgumentException("编号已存在，操作失败");
            }
        }
        this.saveOrUpdate(qualityInspectionMaterial);
        return qualityInspectionMaterial;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delQualityInspectionMaterial(int id) {
        QualityInspectionMaterial qualityInspectionMaterial = this.baseMapper.selectById(id);
        if (ObjectUtil.isEmpty(qualityInspectionMaterial)) {
            throw new IllegalArgumentException("质检物料信息不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        qualityInspectionMaterial.setUpdateBy(CurrentUserUtil.getUsername());
        qualityInspectionMaterial.setUpdateTime(new Date());
        qualityInspectionMaterial.setIsDeleted(true);
        this.saveOrUpdate(qualityInspectionMaterial);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryQualityInspectionMaterialForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryQualityInspectionMaterialForExcel(paramMap);
    }

    @Override
    public List<QualityInspectionMaterial> getByCondition(QualityInspectionMaterial condition) {
        return this.baseMapper.selectList(new QueryWrapper<>(condition));
    }

    @Override
    public QualityInspectionMaterial findQualityInspectionMaterialOne(QueryQualityInspectionMaterialForm qualityInspectionMaterial) {
        return qualityInspectionMaterialMapper.findQualityInspectionMaterialOne(qualityInspectionMaterial);
    }

    @Override
    public BaseResult comfirmQuality(QualityInspectionVO qualityInspectionVO) {
        List<QualityInspectionMaterialVO> materalList = qualityInspectionVO.getMaterialForms();
        List<Long> ids = materalList.stream().map(x->x.getId()).collect(Collectors.toList());
        LambdaQueryWrapper<QualityInspectionMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(QualityInspectionMaterial::getIsDeleted,false);
        lambdaQueryWrapper.in(QualityInspectionMaterial::getId,ids);
        List<Long> successIdList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        List<QualityInspectionMaterial> qualityInspectionMaterials = this.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(qualityInspectionMaterials)){
            qualityInspectionMaterials.forEach(x->{
                if (x.getIsComfirm()){
                    errorList.add(x.getMaterialName());
                }else {
                    successIdList.add(x.getId());
                }
            });
        }else {
            return BaseResult.error("物料筛选数据为空！");
        }
        List<QualityInspectionMaterial> saveMateralList = new ArrayList<>();
        if (CollUtil.isNotEmpty(successIdList)){
            materalList.forEach(x->{
                if (successIdList.contains(x.getId())){
                    QualityInspectionMaterial material = new QualityInspectionMaterial();
                    BeanUtils.copyProperties(x,material);
                    material.setIsComfirm(true);
                    saveMateralList.add(material);
                }
            });
        }
        if (CollUtil.isNotEmpty(saveMateralList)){
            this.updateBatchById(saveMateralList);
            //修改质检人
            qualityInspectionService.changeQualityUser(saveMateralList.get(0).getQualityInspectionId());
        }
        if (CollUtil.isNotEmpty(errorList)){
            return BaseResult.error(StringUtils.join(errorList, StrUtil.C_COMMA)+" 已确认质检，请勿重复确认质检！");
        }
        return BaseResult.ok(SysTips.SUCCESS_MSG);
    }

    @Override
    public BaseResult cancelQuality(QualityInspectionVO qualityInspectionVO) {
        List<QualityInspectionMaterialVO> materalList = qualityInspectionVO.getMaterialForms();
        List<Long> ids = materalList.stream().map(x->x.getId()).collect(Collectors.toList());
        LambdaQueryWrapper<QualityInspectionMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(QualityInspectionMaterial::getIsDeleted,false);
        lambdaQueryWrapper.in(QualityInspectionMaterial::getId,ids);
        List<Long> successIdList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        List<QualityInspectionMaterial> qualityInspectionMaterials = this.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(qualityInspectionMaterials)){
            qualityInspectionMaterials.forEach(x->{
                if (!x.getIsComfirm()){
                    errorList.add(x.getMaterialName());
                }else {
                    successIdList.add(x.getId());
                }
            });
        }else {
            return BaseResult.error("物料筛选数据为空！");
        }
        List<QualityInspectionMaterial> saveMateralList = new ArrayList<>();
        if (CollUtil.isNotEmpty(successIdList)){
            materalList.forEach(x->{
                if (successIdList.contains(x.getId())){
                    QualityInspectionMaterial material = new QualityInspectionMaterial();
                    BeanUtils.copyProperties(x,material);
                    material.setIsComfirm(false);
                    saveMateralList.add(material);
                }
            });
        }
        if (CollUtil.isNotEmpty(saveMateralList)){
            this.updateBatchById(saveMateralList);
            //修改质检人
            qualityInspectionService.changeQualityUser(saveMateralList.get(0).getQualityInspectionId());
        }
        if (CollUtil.isNotEmpty(errorList)){
            return BaseResult.error(StringUtils.join(errorList, StrUtil.C_COMMA)+" 未确认质检，请勿取消质检！");
        }
        return BaseResult.ok(SysTips.SUCCESS_MSG);
    }

}
