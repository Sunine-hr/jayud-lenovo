package com.jayud.wms.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.RecursiveListUtils;
import com.jayud.wms.mapper.MaterialTypeInfoMapper;
import com.jayud.wms.model.bo.MaterialTypeInfoForm;
import com.jayud.wms.model.po.MaterialTypeInfo;
import com.jayud.wms.service.MaterialTypeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 物料类型逻辑实现层
 */
@Service
@Transactional
public class MaterialTypeInfoServiceImpl extends ServiceImpl<MaterialTypeInfoMapper, MaterialTypeInfo> implements MaterialTypeInfoService {


    @Autowired
    private MaterialTypeInfoMapper materialTypeInfoMapper;

    /**
     * 查询用户下面的所有的 分类
     *
     * @param createBy
     * @return
     */
    @Override
    public List<LinkedHashMap<String, Object>> findMaterialTypeInfo(String createBy, Long parentId) {
        List<LinkedHashMap<String, Object>> materialTypeInfo = null;
        if (parentId != null) {
            materialTypeInfo = materialTypeInfoMapper.findMaterialTypeInfo(createBy, CurrentUserUtil.getUserTenantCode(), parentId);//根据用户名称和租户编码查询分类

        } else {
            List<LinkedHashMap<String, Object>> materialTypeInfos = materialTypeInfoMapper.findMaterialTypeInfo(createBy, CurrentUserUtil.getUserTenantCode(), parentId);//根据用户名称和租户编码查询分类
            materialTypeInfo = RecursiveListUtils.queryRecursiveList(materialTypeInfos);
        }
        return materialTypeInfo;
    }


    @Override
    public boolean addMaterialTypeInfo(MaterialTypeInfoForm form) {


        System.out.println("进入到方法拿到数据：" + form);

        if (form.getId() == null) {
            MaterialTypeInfo materialTypeInfo = new MaterialTypeInfo();

//            materialTypeInfo.setId(sequenceGenerator.nextId()); //id分布式id
            materialTypeInfo.setParentId(form.getParentId());
            materialTypeInfo.setMaterialTypeName(form.getMaterialTypeName());//物料类型名称
            materialTypeInfo.setMaterialTypeCode(String.valueOf(System.currentTimeMillis()));//物料类型编码
//            materialTypeInfo.setRemark(form.getRemark());//
//            materialTypeInfo.setTenantCode(null); //当前租户编码
            materialTypeInfo.setOrder(1L);//排序

            this.save(materialTypeInfo);
        } else {
            MaterialTypeInfo materialTypeInfo = new MaterialTypeInfo();
            materialTypeInfo.setId(form.getId());
            materialTypeInfo.setParentId(form.getParentId());
            materialTypeInfo.setMaterialTypeName(form.getMaterialTypeName());//物料名称
            materialTypeInfo.setOrder(form.getOrder());//排序
            this.updateById(materialTypeInfo);
        }

        return true;
    }

    /**
     * 级联删除物料信息
     *
     * @param form
     * @return
     */
    @Override
    public boolean deleteMaterialTypeInfo(MaterialTypeInfoForm form) {

        //删除一级或者二级
        MaterialTypeInfo materialTypeInfo = new MaterialTypeInfo();
        materialTypeInfo.setId(form.getId());
        materialTypeInfo.setIsDeleted(true);
        materialTypeInfo.setUpdateBy(form.getUpdateBy());//更新人
        materialTypeInfo.setUpdateTime(new Date());//更新时间

        this.materialTypeInfoMapper.updateById(materialTypeInfo);
        //再去删除分类
        if (form.getParentId() == 0) {
            MaterialTypeInfo materialTypeInfoTwo = new MaterialTypeInfo();
            materialTypeInfoTwo.setParentId(form.getId());
            materialTypeInfoTwo.setIsDeleted(true);
            materialTypeInfoTwo.setUpdateBy(form.getUpdateBy());//更新人
            materialTypeInfoTwo.setUpdateTime(new Date());//更新时间
//            this.materialTypeInfoMapper.updateById(materialTypeInfoTwo);
            this.materialTypeInfoMapper.updateMaterialTypeInfo(materialTypeInfoTwo);
        }
        return true;
    }

    @Override
    public List<LinkedHashMap<String, Object>> findMaterialTypeInfoList(MaterialTypeInfoForm form) {

        List<LinkedHashMap<String, Object>> materialTypeInfo = null;
        //  第一种查询一级标题和二级标题 列表


        if (form != null && !Objects.isNull(form.getParentId()) && form.getParentId() == 0) {
            materialTypeInfo = this.materialTypeInfoMapper.findMaterialTypeInfoListStair(form.getId(), form.getId());
        }
        //输入的是二级菜单 id
        if (form != null && !Objects.isNull(form.getParentId()) && form.getParentId() != 0) {
            materialTypeInfo = this.materialTypeInfoMapper.findMaterialTypeInfoListTwo(form.getId(), form.getParentId());

        }

//        if (ObjectUtil.isEmpty(form.getId())) {
//            Map<String, Object> paramMap = new HashMap<>();
//            materialTypeInfo = this.materialTypeInfoMapper.findMaterialTypeInfoListFrom(paramMap);
//
//            materialTypeInfo.stream().forEach(vo -> {
//                System.out.println(vo);
//                String parentId = vo.get("parentId").toString();
//                if (parentId.equals("0")) {
//                    vo.put("materialTypeClassification", "所有类别");
//                }
//
//
//                //  String id = warehouseLocationType.get(0).get("id").toString();
//            });
//        }
        //根据id查询数据
//        QueryWrapper<MaterialTypeInfo> condition = new QueryWrapper<>();
////        condition.lambda().eq(MaterialTypeInfo::getId, id);
//         this.getOne(condition);
        return materialTypeInfo;
    }

    //创建类别  类别不重复校验
    @Override
    public MaterialTypeInfo findMaterialTypeInfoMaterialTypeName(String materialTypeName, Long parentId) {

        MaterialTypeInfo materialTypeInfo = new MaterialTypeInfo();
        materialTypeInfo.setMaterialTypeName(materialTypeName);
        materialTypeInfo.setParentId(parentId);
        MaterialTypeInfo materialTypeInfoName = this.materialTypeInfoMapper.findMaterialTypeInfoName(materialTypeInfo);
        return materialTypeInfoName;
    }

    //创建类别  类别不重复校验
//    @Override
//    public boolean findMaterialTypeInfoMaterialTypeName(String materialTypeName) {
//        List<LinkedHashMap<String, Object>> materialTypeInfo = this.materialTypeInfoMapper.findMaterialTypeInfoName(materialTypeName);
//
//        if (materialTypeInfo != null) {
//            return true;
//        }
//        return false;// 不通过
//    }

    //根据类别查询 详情
    @Override
    public List<LinkedHashMap<String, Object>> findMaterialTypeInfoMaterialTypeNameList(String materialTypeName) {
        List<LinkedHashMap<String, Object>> materialTypeInfo = null;

        if (ObjectUtil.isEmpty(materialTypeName)) {
            Map<String, Object> paramMap = new HashMap<>();
            materialTypeInfo = this.materialTypeInfoMapper.findMaterialTypeInfoListFrom(paramMap);

            materialTypeInfo.stream().forEach(vo -> {
                System.out.println(vo);
                String parentId = vo.get("parentId").toString();
                if (parentId.equals("0")) {
                    vo.put("materialTypeClassification", "所有类别");
                }
            });
        } else {
            materialTypeInfo = this.materialTypeInfoMapper.findMaterialTypeInfoListIdParentId(materialTypeName);
        }
        return materialTypeInfo;
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryMaterialTypeInfoFormForExcel(Map<String, Object> paramMap) {
        List<LinkedHashMap<String, Object>> linkedHashMaps = this.materialTypeInfoMapper.queryMaterialTypeInfoFormForExcel(paramMap);
        linkedHashMaps.stream().forEach(vo -> {
            System.out.println(vo);
            String parentId = vo.get("parentId").toString();
            if (parentId.equals("0")) {
                vo.put("materialTypeClassification", "所有类别");
            }

        });
        // list去掉多余字段
//        linkedHashMaps.stream().forEach(vo -> {
//            String parentId = vo.get("parentId").toString();
//            vo.remove("parentId");
//        });
        List<LinkedHashMap<String, Object>> linkedHashMapEx=new LinkedList<>();
        linkedHashMaps.stream().forEach(vo -> {
            LinkedHashMap<String, Object> linkedList=new LinkedHashMap<String, Object>();
            linkedList.put("id",vo.get("id").toString());
            linkedList.put("materialTypeName ",vo.get("materialTypeName").toString());
            linkedList.put("materialTypeClassification",vo.get("materialTypeClassification").toString());
            linkedList.put("createBy",vo.get("createBy").toString());
            linkedList.put("createTime",vo.get("createTime").toString());
            linkedHashMapEx.add(linkedList);

        });
        return linkedHashMapEx;
    }

    @Override
    public List<LinkedHashMap<String, Object>> findMaterialTypeInfoMaterialTypeIdOne(Map<String, Object> paramMap) {


        List<LinkedHashMap<String, Object>> materialTypeInfoListFrom = this.materialTypeInfoMapper.findMaterialTypeInfoListFrom(paramMap);
        return materialTypeInfoListFrom;
    }


    //创建类别校验
//    @Override
//    public boolean findMaterialTypeInfoMaterialTypeName(MaterialTypeInfoForm form) {
//ne
//        QueryWrapper<MaterialTypeInfoForm> aterialTypeInfoO = new QueryWrapper<>();
//        orderInlandTransportOne.lambda().select(OrderInlandTransport::getId, OrderInlandTransport::getMainOrderNo, OrderInlandTransport::getOrderNo)
//                .in(OrderInlandTransport::getThirdPartyOrderNo, thirdPartyOrderNo);
//        OrderInlandTransport orderInlandTransport = this.getOne(orderInlandTransportOne, false);
//        materialTypeInfoService.getOne()
//
//
//        return false;
//    }


    /**
     * 生成编号
     *
     * @return
     */
    public String materialTypeInfoNum() {
        StringBuilder orderNo = new StringBuilder();
//        int count = this.count();
//        orderNo.append("BK").append(DateUtils.LocalDateTime2Str(LocalDateTime.now(), "yyyyMMdd"))
//                .append(StringUtils.zeroComplement(4, count + 1));
        return orderNo.toString();
    }

//
//	/**
//	 * 查询租户的导出数据列表
//	 */
//	@Override
//	public List<LinkedHashMap<String, Object>> querySysTenantForExcel(Map<String, Object> paramMap) {
//		return sysTenantMapper.querySysTenant(paramMap);
//	}
//
//	/**
//	 * 新增租户
//	 */
//	@Override
//	public void insertSysTenant(SysTenant sysTenant) {
//		Integer existing = sysTenantMapper.getSysTenantByTenantCode(sysTenant.getId(), sysTenant.getTenantCode().trim());
//		if (existing != null && existing > 0) {
//			throw new IllegalArgumentException("租户编码已存在");
//		}
//		sysTenant.setId(sequenceGenerator.nextId());
//		sysTenantMapper.insertSysTenant(sysTenant);
//		logger.info("租户已新增： {}", sysTenant.getTenantName());
//	}

}
