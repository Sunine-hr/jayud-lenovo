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
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.bo.InventoryDetailForm;
import com.jayud.wms.model.bo.MaterialForm;
import com.jayud.wms.model.bo.QualityMaterialForm;
import com.jayud.wms.model.enums.ReceiptStatusEnum;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.enums.MaterialStatusEnum;
import com.jayud.wms.mapper.MaterialMapper;
import com.jayud.wms.model.vo.WarehouseAreaVO;
import com.jayud.wms.service.*;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private IInventoryDetailService inventoryDetailService;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    public IWarehouseLocationService warehouseLocationService;

    @Autowired
    public IWarehouseAreaService warehouseAreaService;

    @Autowired
    public IWarehouseService warehouseService;

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
    public List<Material> findMaterialOne(Material material) {
        return this.baseMapper.findMaterialOne(material);
    }

    @Override
    public List<Material> findMaterialSNOne(QualityMaterialForm qualityMaterialForm) {
        return this.baseMapper.findMaterialSNOne(qualityMaterialForm);
    }

    @Override
    public int updateAllMaterialList(Material material) {
        return this.baseMapper.updateAllMaterialList(material);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult confirmReceipt(DeleteForm deleteForm) {

        List<MaterialForm> materialForms = deleteForm.getMaterialForms();

        for (int i = 0; i < materialForms.size(); i++) {
            Material materials = ConvertUtil.convert(materialForms.get(i), Material.class);
            this.updateById(materials);
        }
        List<Long> collect = materialForms.stream().map(MaterialForm::getId).collect(Collectors.toList());

        if (CollUtil.isEmpty(collect)) {
            return BaseResult.error(SysTips.IDS_ISEMPTY);
        }
        List<String> errList = new ArrayList<>();
        Material material = new Material();
        material.setIds(collect);
        List<Material> materialList = selectList(material);
        List<Material> successList = new ArrayList<>();
        if (CollUtil.isNotEmpty(materialList)) {
            materialList.forEach(materials -> {
                if (materials.getStatus().equals(MaterialStatusEnum.THREE.getCode())) {
                    errList.add(materials.getMaterialCode());
                } else {
                    boolean isAdd = false;
                    if (materials.getIsPutShelf()!=null){
                        if (!materials.getIsPutShelf()){
                            isAdd = true;
                        }
                    }else {
                        isAdd = true;
                    }
                    if(isAdd){
                        materials.setStatus(MaterialStatusEnum.THREE.getCode());
                        successList.add(materials);
                    }
                }
            });
        }
        if (CollUtil.isNotEmpty(successList)) {
            this.updateBatchById(successList);
            checkIsAllComfirmReceipt(successList.get(0).getOrderId());
        }
        if (CollUtil.isNotEmpty(errList)) {
            return BaseResult.error(StringUtils.join(errList, StrUtil.C_COMMA) + " 已确认收货，请勿重复确认！");
        }
        return BaseResult.ok(SysTips.SUCCESS_MSG);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult cancelConfirmReceipt(DeleteForm deleteForm) {

        List<MaterialForm> materialForms = deleteForm.getMaterialForms();

        for (int i = 0; i < materialForms.size(); i++) {
            Material materials = ConvertUtil.convert(materialForms.get(i), Material.class);
            this.updateById(materials);
        }
        List<Long> collect = materialForms.stream().map(MaterialForm::getId).collect(Collectors.toList());

        if (CollUtil.isEmpty(collect)) {
            return BaseResult.error(SysTips.IDS_ISEMPTY);
        }
        Material material = new Material();
        material.setIds(collect);
        List<Material> materialList = selectList(material);
        List<String> errList = new ArrayList<>();
        List<Material> successList = new ArrayList<>();
        if (CollUtil.isNotEmpty(materialList)) {
            if (checkIsAllComfirm(materialList.get(0).getOrderId())) {
                return BaseResult.error(SysTips.ALL_COMFIRM_CANCEL_ERR);
            }
            materialList.forEach(material1 -> {
                //未确认收货
                if (!material1.getStatus().equals(MaterialStatusEnum.THREE.getCode())) {
                    errList.add(material1.getMaterialCode());
                } else {
                    material1.setStatus(MaterialStatusEnum.FOUR.getCode());
                    successList.add(material1);
                }
            });
        }
        if (CollUtil.isNotEmpty(successList)) {
            this.updateBatchById(successList);
            checkIsAllComfirmReceipt(successList.get(0).getOrderId());
        }
        if (CollUtil.isNotEmpty(errList)) {
            return BaseResult.error(StringUtils.join(errList, StrUtil.C_COMMA) + " 未确认收货，请勿撤销收货！");
        }
        return BaseResult.ok(SysTips.SUCCESS_MSG);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult confirmPullShelf(DeleteForm deleteForm) {

        List<MaterialForm> materialForms = deleteForm.getMaterialForms();

        for (int i = 0; i < materialForms.size(); i++) {
            Material materials = ConvertUtil.convert(materialForms.get(i), Material.class);
            materials.setWarehouseLocationCode(materialForms.get(i).getWarehouseLocationCode());
            this.updateById(materials);
        }
        List<Long> collect = materialForms.stream().map(MaterialForm::getId).collect(Collectors.toList());


        if (CollUtil.isNotEmpty(collect)) {
            //去掉null
//            deleteForm.setIds(deleteForm.getIds().stream().filter(x->x!=null).distinct().collect(Collectors.toList()));

            List<Long> collect1 = collect.stream().filter(x -> x != null).distinct().collect(Collectors.toList());

            LambdaQueryWrapper<Material> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(Material::getId, collect1);
            lambdaQueryWrapper.eq(Material::getIsDeleted, false);
//            List<Material> materialList = this.list(lambdaQueryWrapper);

            List<Material> materialList = ConvertUtil.convertList(deleteForm.getMaterialForms(), Material.class);
            List<Material> successList = new ArrayList<>();
            List<String> errList = new ArrayList<>();
            if (CollUtil.isNotEmpty(materialList)) {
                materialList.forEach(material1 -> {
                    boolean isChange = true;
                    if (material1.getIsPutShelf() != null) {
                        if (material1.getIsPutShelf()) {
                            errList.add(material1.getMaterialName());
                            isChange = false;
                        }
                    }
                    if (isChange) {
                        material1.setIsPutShelf(true);
                        successList.add(material1);
                    }
                });
            }
            if (CollUtil.isNotEmpty(successList)) {
                this.updateBatchById(successList);
                initInventoryDetail(successList);
                //修改 订单货物信息状态

                for (int i = 0; i < materialForms.size(); i++) {
                    Material materials = ConvertUtil.convert(materialForms.get(i), Material.class);
                    materials.setWarehouseLocationCode(materialForms.get(i).getWarehouseLocationCode());
                    materials.setStatus(MaterialStatusEnum.six.getCode());
                    this.updateById(materials);
                }


                //修改主订单 状态为已上架
                Receipt receipt = receiptService.getById(successList.get(0).getOrderId());
                receipt.setStatus(ReceiptStatusEnum.SIX.getCode());
                receiptService.updateById(receipt);

            }
            if (CollUtil.isNotEmpty(errList)) {
                return BaseResult.error(StringUtils.join(errList, StrUtil.C_COMMA) + " 已上架，请勿重复上架！");
            }
            return BaseResult.ok(SysTips.SUCCESS_MSG);
        }
        return null;
    }

    /**
     * @description 判断是否全部确认收货并修改为待上架
     * @author ciro
     * @date 2022/3/29 14:00
     * @param: orderId   收货单id
     * @return: void
     **/
    private void checkIsAllComfirmReceipt(Long orderId) {
        Material material = new Material();
        material.setOrderId(orderId);
        boolean isEnd = true;
        List<Material> materialList = selectList(material);
        int counts = 0;
        if (CollUtil.isNotEmpty(materialList)) {
            for (Material material1 : materialList) {
                if (!material1.getStatus().equals(MaterialStatusEnum.THREE.getCode())) {
                    isEnd = false;
                    break;
                } else {
                    counts += 1;
                }
            }
        }
        Receipt receipt = receiptService.getById(orderId);
        if (isEnd) {
            receipt.setStatus(MaterialStatusEnum.FIVE.getCode());
        } else {
            if (counts > 0) {
                receipt.setStatus(MaterialStatusEnum.TWO.getCode());
            } else {
                receipt.setStatus(MaterialStatusEnum.ONE.getCode());
            }
        }
        receiptService.updateById(receipt);
    }

    /**
     * @description 判断是否全部确认
     * @author ciro
     * @date 2022/3/29 14:16
     * @param: orderId
     * @return: boolean
     **/
    private boolean checkIsAllComfirm(Long orderId) {
        Receipt receipt = receiptService.getById(orderId);
        if (receipt.getStatus().equals(MaterialStatusEnum.FIVE.getCode())) {
            return true;
        }
        return false;
    }

    private void initInventoryDetail(List<Material> materialList) {
        Receipt receipt = receiptService.getById(materialList.get(0).getOrderId());
        List<InventoryDetailForm> inventoryDetailForms = new ArrayList<>();
        LocalTime localTime = LocalTime.now();
        materialList.forEach(material -> {
            InventoryDetailForm detailForm = new InventoryDetailForm();

            QueryWrapper<Warehouse> warehouseQueryWrapper = new QueryWrapper<>();
            warehouseQueryWrapper.lambda().eq(Warehouse::getId,receipt.getWarehouseId());
            warehouseQueryWrapper.lambda().eq(Warehouse::getIsDeleted, 0);
            Warehouse one = warehouseService.getOne(warehouseQueryWrapper);
            //仓库
            detailForm.setWarehouseId(receipt.getWarehouseId());
            detailForm.setWarehouseCode(one.getCode());
            detailForm.setWarehouseName(receipt.getWarehouse());
            //货主
            detailForm.setOwerId(receipt.getSupplierId());
            detailForm.setOwerCode(receipt.getSupplierCode());
            detailForm.setOwerName(receipt.getSupplier());

            //库位  根据库位编号查询库位id
            QueryWrapper<WarehouseLocation> warehouseLocationQueryWrapper = new QueryWrapper<>();
            warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getCode, material.getWarehouseLocationCode());
            warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getIsDeleted, 0);
            WarehouseLocation warehouseLocationOne = warehouseLocationService.getOne(warehouseLocationQueryWrapper);

            detailForm.setWarehouseLocationId(warehouseLocationOne.getId());
            detailForm.setWarehouseLocationCode(warehouseLocationOne.getCode());

            //库区
            //根据库位查询库区信息
            QueryWrapper<WarehouseArea> warehouseAreaQueryWrapper = new QueryWrapper<>();
            warehouseAreaQueryWrapper.lambda().eq(WarehouseArea::getId, warehouseLocationOne.getWarehouseAreaId());
            warehouseAreaQueryWrapper.lambda().eq(WarehouseArea::getIsDeleted, 0);
            WarehouseArea warehouseAreaOne = warehouseAreaService.getOne(warehouseAreaQueryWrapper);

            detailForm.setWarehouseAreaId(warehouseAreaOne.getId());
            detailForm.setWarehouseAreaCode(warehouseAreaOne.getCode());
            detailForm.setWarehouseAreaName(warehouseAreaOne.getName());

            //库位状态
            detailForm.setWarehouseLocationStatus(0);
            detailForm.setWarehouseLocationStatus2(0);
            //容器
            detailForm.setContainerCode(material.getContainerNum());
            //物料
            detailForm.setMaterialCode(material.getMaterialCode());
            detailForm.setMaterialName(material.getMaterialName());
            //自定义字段-5个
            detailForm.setBatchCode(material.getBatchNum());
            if (material.getProductionDate() != null) {
                detailForm.setMaterialProductionDate(LocalDateTime.of(material.getProductionDate(), localTime));
            }
            detailForm.setCustomField1(material.getColumnOne());
            detailForm.setCustomField2(material.getColumnTwo());
            detailForm.setCustomField3(material.getColumnThree());
            //入仓号
            detailForm.setInWarehouseNumber(material.getInWarehouseNumber());
            if (material.getWeight() == null) {
                detailForm.setWeight(new BigDecimal(0));
            } else {
                detailForm.setWeight(material.getWeight());
            }
            if (material.getVolume() == null) {
                detailForm.setWeight(new BigDecimal(0));
            } else {
                detailForm.setVolume(material.getVolume());
            }
            detailForm.setUnit(material.getUnit());
            //数量
            detailForm.setOperationCount(new BigDecimal(material.getNum()));
            inventoryDetailForms.add(detailForm);
        });
        if (CollUtil.isNotEmpty(inventoryDetailForms)) {
            inventoryDetailService.input(inventoryDetailForms);
        }
    }


}
