package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.bo.MaterialForm;
import com.jayud.wms.model.bo.QualityMaterialForm;
import com.jayud.wms.model.po.Material;
import com.jayud.wms.model.enums.MaterialStatusEnum;
import com.jayud.wms.mapper.MaterialMapper;
import com.jayud.wms.model.po.Receipt;
import com.jayud.wms.service.IMaterialService;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.service.IReceiptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 货单物料信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-21
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements IMaterialService {

    @Autowired
    private IReceiptService receiptService;

    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public IPage<Material> selectPage(Material material,
                                      Integer pageNo,
                                      Integer pageSize,
                                      HttpServletRequest req) {

        Page<Material> page = new Page<Material>(pageNo, pageSize);
        IPage<Material> pageList = materialMapper.pageList(page, material);
        return pageList;
    }

    @Override
    public List<Material> selectList(Material material) {
        return materialMapper.list(material);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Material saveOrUpdateMaterial(Material material) {
        Long id = material.getId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            material.setCreateBy(CurrentUserUtil.getUsername());
            material.setCreateTime(new Date());

            QueryWrapper<Material> materialQueryWrapper = new QueryWrapper<>();
            materialQueryWrapper.lambda().eq(Material::getIsDeleted, 0);
            List<Material> list = this.list(materialQueryWrapper);
            if (CollUtil.isNotEmpty(list)) {
                throw new IllegalArgumentException("编号已存在，操作失败");
            }

        } else {
            //修改 --> update 更新人、更新时间
            material.setUpdateBy(CurrentUserUtil.getUsername());
            material.setUpdateTime(new Date());

            QueryWrapper<Material> materialQueryWrapper = new QueryWrapper<>();
            materialQueryWrapper.lambda().ne(Material::getId, id);
            materialQueryWrapper.lambda().eq(Material::getIsDeleted, 0);
            List<Material> list = this.list(materialQueryWrapper);
            if (CollUtil.isNotEmpty(list)) {
                throw new IllegalArgumentException("编号已存在，操作失败");
            }
        }
        this.saveOrUpdate(material);
        return material;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMaterial(int id) {
        Material material = this.baseMapper.selectById(id);
        if (ObjectUtil.isEmpty(material)) {
            throw new IllegalArgumentException("货单物料信息不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        material.setUpdateBy(CurrentUserUtil.getUsername());
        material.setUpdateTime(new Date());
        material.setIsDeleted(true);
        this.saveOrUpdate(material);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryMaterialForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryMaterialForExcel(paramMap);
    }

    @Override
    public void updateByCondition(Material material, Material condition) {
        material.setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(new Date());
        this.update(material, new QueryWrapper<>(condition));
    }

    @Override
    public List<Material> getByCondition(Material condition) {
        return this.baseMapper.selectList(new QueryWrapper<>(condition));
    }

    @Override
    public MaterialForm copyMaterial(MaterialForm form) {
        form.setStatus(MaterialStatusEnum.ONE.getCode()).setNum(0.0).setActualNum(0.0).setContainerNum(null).setId(null).setCreateTime(new Date())
                .setCreateBy(CurrentUserUtil.getUsername()).setUpdateTime(null).setUpdateBy(null);
        Material material = ConvertUtil.convert(form, Material.class);
        this.save(material);
        form.setId(material.getId());
        return form;
    }

    //复制并且赋值实收数量
    @Override
    public MaterialForm establishMaterial(MaterialForm form) {
        form.setStatus(MaterialStatusEnum.ONE.getCode()).setNum(0.0).setId(null).setCreateTime(new Date())
                .setCreateBy(CurrentUserUtil.getUsername()).setUpdateTime(null).setUpdateBy(null);
        Material material = ConvertUtil.convert(form, Material.class);
        this.save(material);
        form.setId(material.getId());
        return form;
    }

    @Override
    public   List<Material>  findMaterialOne(Material material) {
        return this.baseMapper.findMaterialOne(material);
    }

    @Override
    public   List<Material> findMaterialSNOne(QualityMaterialForm qualityMaterialForm) {
        return this.baseMapper.findMaterialSNOne(qualityMaterialForm);
    }

    @Override
    public int updateAllMaterialList(Material material) {
        return this.baseMapper.updateAllMaterialList(material);
    }

    @Override
    public BaseResult confirmReceipt(DeleteForm deleteForm) {
        if (CollUtil.isNotEmpty(deleteForm.getIds())){
            return BaseResult.error(SysTips.IDS_ISEMPTY);
        }
        List<String> errList = new ArrayList<>();
        Material material = new Material();
        material.setIds(deleteForm.getIds());
        List<Material> materialList = selectList(material);
        List<Material> successList = new ArrayList<>();
        if (CollUtil.isNotEmpty(materialList)){
            materialList.forEach(materials -> {
                if (materials.getStatus().equals(MaterialStatusEnum.THREE)){
                    errList.add(materials.getMaterialCode());
                }else {
                    materials.setStatus(MaterialStatusEnum.THREE.getCode());
                    successList.add(materials);
                }
            });
        }
        if (CollUtil.isNotEmpty(successList)){
            this.updateBatchById(successList);
            checkIsAllComfirmReceipt(successList.get(0).getOrderId());
        }
        if (CollUtil.isNotEmpty(errList)){
            return BaseResult.error(StringUtils.join(errList, StrUtil.C_COMMA) + " 已确认收货，请勿重复确认！");
        }
        return BaseResult.ok(SysTips.SUCCESS_MSG);
    }

    @Override
    public BaseResult cancelConfirmReceipt(DeleteForm deleteForm) {
        if (CollUtil.isNotEmpty(deleteForm.getIds())){
            return BaseResult.error(SysTips.IDS_ISEMPTY);
        }
        Material material = new Material();
        material.setIds(deleteForm.getIds());
        List<Material> materialList = selectList(material);
        List<String> errList = new ArrayList<>();
        List<Material> successList = new ArrayList<>();
        if (CollUtil.isNotEmpty(materialList)){
            if (checkIsAllComfirm(materialList.get(0).getOrderId())){
                return BaseResult.error(SysTips.ALL_COMFIRM_CANCEL_ERR);
            }
            materialList.forEach(material1 -> {
                //未确认收货
                if (!material1.getStatus().equals(MaterialStatusEnum.THREE)){
                    errList.add(material1.getMaterialCode());
                }else {
                    successList.add(material1);
                }
            });
        }
        if (CollUtil.isNotEmpty(successList)){
            this.updateBatchById(successList);
        }
        if (CollUtil.isNotEmpty(errList)){
            return BaseResult.error(StringUtils.join(errList,StrUtil.C_COMMA)+" 未确认收货，请勿撤销收货！");
        }
        return BaseResult.ok(SysTips.SUCCESS_MSG);
    }

    /**
     * @description 判断是否全部确认收货并修改为待上架
     * @author  ciro
     * @date   2022/3/29 14:00
     * @param: orderId   收货单id
     * @return: void
     **/
    private void checkIsAllComfirmReceipt(Long orderId){
        Material material = new Material();
        material.setOrderId(orderId);
        boolean isEnd = true;
        List<Material> materialList = selectList(material);
        if (CollUtil.isNotEmpty(materialList)){
            for (Material material1:materialList){
                if (!material1.getStatus().equals(MaterialStatusEnum.THREE.getCode())){
                    isEnd = false;
                    break;
                }
            }
        }
        if (isEnd){
            Receipt receipt = receiptService.getById(orderId);
            receipt.setStatus(MaterialStatusEnum.FIVE.getCode());
            receiptService.updateById(receipt);
        }
    }

    /**
     * @description 判断是否全部确认
     * @author  ciro
     * @date   2022/3/29 14:16
     * @param: orderId
     * @return: boolean
     **/
    private boolean checkIsAllComfirm(Long orderId){
        Receipt receipt = receiptService.getById(orderId);
        if (receipt.getStatus().equals(MaterialStatusEnum.FIVE.getCode())){
            return true;
        }
        return false;
    }


}
