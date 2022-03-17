package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.bo.NoticeSnMaterialForm;
import com.jayud.wms.model.po.NoticeSnMaterial;
import com.jayud.wms.mapper.NoticeSnMaterialMapper;
import com.jayud.wms.service.INoticeSnMaterialService;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 通知单物料sn信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-18
 */
@Service
public class NoticeSnMaterialServiceImpl extends ServiceImpl<NoticeSnMaterialMapper, NoticeSnMaterial> implements INoticeSnMaterialService {


    @Autowired
    private NoticeSnMaterialMapper noticeSnMaterialMapper;

    @Override
    public IPage<NoticeSnMaterial> selectPage(NoticeSnMaterial noticeSnMaterial,
                                              Integer pageNo,
                                              Integer pageSize,
                                              HttpServletRequest req) {

        Page<NoticeSnMaterial> page = new Page<NoticeSnMaterial>(pageNo, pageSize);
        IPage<NoticeSnMaterial> pageList = noticeSnMaterialMapper.pageList(page, noticeSnMaterial);
        return pageList;
    }

    @Override
    public List<NoticeSnMaterial> selectList(NoticeSnMaterial noticeSnMaterial) {
        return noticeSnMaterialMapper.list(noticeSnMaterial);
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public NoticeSnMaterial saveOrUpdateNoticeSnMaterial(NoticeSnMaterial noticeSnMaterial) {
//        Long id = noticeSnMaterial.getId();
//        if(ObjectUtil.isEmpty(id)){
//            //新增 --> add 创建人、创建时间
//            noticeSnMaterial.setCreateBy(CurrentUserUtil.getUsername());
//            noticeSnMaterial.setCreateTime(new Date());
//
//            QueryWrapper<NoticeSnMaterial> noticeSnMaterialQueryWrapper = new QueryWrapper<>();
//            noticeSnMaterialQueryWrapper.lambda().eq(NoticeSnMaterial::getCode, noticeSnMaterial.getCode());
//            noticeSnMaterialQueryWrapper.lambda().eq(NoticeSnMaterial::getIsDeleted, 0);
//            List<NoticeSnMaterial> list = this.list(noticeSnMaterialQueryWrapper);
//            if(CollUtil.isNotEmpty(list)){
//                throw new IllegalArgumentException("编号已存在，操作失败");
//            }
//
//        }else{
//            //修改 --> update 更新人、更新时间
//            noticeSnMaterial.setUpdateBy(CurrentUserUtil.getUsername());
//            noticeSnMaterial.setUpdateTime(new Date());
//
//            QueryWrapper<NoticeSnMaterial> noticeSnMaterialQueryWrapper = new QueryWrapper<>();
//            noticeSnMaterialQueryWrapper.lambda().ne(NoticeSnMaterial::getId, id);
//            noticeSnMaterialQueryWrapper.lambda().eq(NoticeSnMaterial::getMaterialCode, noticeSnMaterial.getCode());
//            noticeSnMaterialQueryWrapper.lambda().eq(NoticeSnMaterial::getIsDeleted, 0);
//            List<NoticeSnMaterial> list = this.list(noticeSnMaterialQueryWrapper);
//            if(CollUtil.isNotEmpty(list)){
//                throw new IllegalArgumentException("编号已存在，操作失败");
//            }
//        }
//        this.saveOrUpdate(noticeSnMaterial);
//        return noticeSnMaterial;
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delNoticeSnMaterial(int id) {
        NoticeSnMaterial noticeSnMaterial = this.baseMapper.selectById(id);
        if (ObjectUtil.isEmpty(noticeSnMaterial)) {
            throw new IllegalArgumentException("通知单物料sn信息不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        noticeSnMaterial.setUpdateBy(CurrentUserUtil.getUsername());
        noticeSnMaterial.setUpdateTime(new Date());
        noticeSnMaterial.setIsDeleted(true);
        this.saveOrUpdate(noticeSnMaterial);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryNoticeSnMaterialForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryNoticeSnMaterialForExcel(paramMap);
    }

    @Override
    public void createOrder(Long id, String receiptNoticeNum, List<NoticeSnMaterialForm> snMaterialForms) {
        Date date = new Date();
        Map<Long, NoticeSnMaterial> oldSnMaterialMap = new HashMap<>();
        if (id != null) {
            List<NoticeSnMaterial> oldNoticeSnMaterials = this.getByCondition(new NoticeSnMaterial().setIsDeleted(false).setReceiptNoticeId(id));
            oldSnMaterialMap = oldNoticeSnMaterials.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        }

        for (NoticeSnMaterialForm snMaterialForm : snMaterialForms) {
            oldSnMaterialMap.remove(snMaterialForm.getId());
            snMaterialForm.setReceiptNoticeId(id).setReceiptNoticeNum(receiptNoticeNum);
            if (snMaterialForm.getId() == null) {
                snMaterialForm.setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
            } else {
                snMaterialForm.setUpdateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
            }
        }
        List<NoticeSnMaterial> snMaterials = ConvertUtil.convertList(snMaterialForms, NoticeSnMaterial.class);
        //删除物料
        List<NoticeSnMaterial> deleteSnMaterials = new ArrayList<>();
        oldSnMaterialMap.forEach((k, v) -> {
            NoticeSnMaterial noticeSnMaterial = new NoticeSnMaterial();
            noticeSnMaterial.setId(v.getId());
            noticeSnMaterial.setIsDeleted(true).setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(date);
            deleteSnMaterials.add(noticeSnMaterial);
        });
        if (deleteSnMaterials.size() > 0) {
            this.updateBatchById(deleteSnMaterials);
        }
        if (!CollectionUtil.isEmpty(snMaterials)) {
            this.saveOrUpdateBatch(snMaterials);
        }

    }

    @Override
    public List<NoticeSnMaterial> getByCondition(NoticeSnMaterial condition) {
        return this.baseMapper.selectList(new QueryWrapper<>(condition));
    }

}
