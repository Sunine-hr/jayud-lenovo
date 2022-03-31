package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.mapper.QualityInspectionMapper;
import com.jayud.wms.model.bo.QualityInspectionForm;
import com.jayud.wms.model.bo.QualityInspectionMaterialForm;
import com.jayud.wms.model.bo.QueryQualityInspectionForm;
import com.jayud.wms.model.bo.QueryQualityInspectionMaterialForm;
import com.jayud.wms.model.enums.InboundOrderProStatusEnum;
import com.jayud.wms.model.enums.MaterialStatusEnum;
import com.jayud.wms.model.po.OrderTrack;
import com.jayud.wms.model.po.QualityInspection;
import com.jayud.wms.model.po.QualityInspectionMaterial;
import com.jayud.wms.model.po.Receipt;
import com.jayud.wms.model.vo.MaterialVO;
import com.jayud.wms.model.vo.QualityInspectionMaterialVO;
import com.jayud.wms.model.vo.QualityInspectionVO;
import com.jayud.wms.model.vo.ReceiptVO;
import com.jayud.wms.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 质检检测 服务实现类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Service
public class QualityInspectionServiceImpl extends ServiceImpl<QualityInspectionMapper, QualityInspection> implements IQualityInspectionService {


    @Autowired
    private QualityInspectionMapper qualityInspectionMapper;
    @Autowired
    private AuthClient authClient;
    @Autowired
    private IQualityInspectionMaterialService qualityInspectionMaterialService;
    @Autowired
    private IReceiptService receiptService;
    @Autowired
    private IQualityInspectionService qualityInspectionService;
    @Autowired
    private IOrderProcessService orderProcessService;
    @Autowired
    private IOrderTrackService orderTrackService;
    @Autowired
    private IIncomingSeedingService incomingSeedingService;
    @Autowired
    private IShelfOrderService shelfOrderService;

    @Override
    public IPage<QualityInspectionVO> selectPage(QueryQualityInspectionForm queryQualityInspectionForm,
                                                 Integer pageNo,
                                                 Integer pageSize,
                                                 HttpServletRequest req) {

        Page<QueryQualityInspectionForm> page = new Page<QueryQualityInspectionForm>(pageNo, pageSize);
        IPage<QualityInspectionVO> pageList = qualityInspectionMapper.pageList(page, queryQualityInspectionForm);
        return pageList;
    }

    @Override
    public List<QualityInspection> selectList(QualityInspection qualityInspection) {
        return qualityInspectionMapper.list(qualityInspection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QualityInspection saveOrUpdateQualityInspection(QualityInspection qualityInspection) {
        Long id = qualityInspection.getId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            qualityInspection.setCreateBy(CurrentUserUtil.getUsername());
            qualityInspection.setCreateTime(new Date());

            QueryWrapper<QualityInspection> qualityInspectionQueryWrapper = new QueryWrapper<>();
//            qualityInspectionQueryWrapper.lambda().eq(QualityInspection::getCode, qualityInspection.getCode());
            qualityInspectionQueryWrapper.lambda().eq(QualityInspection::getIsDeleted, 0);
            List<QualityInspection> list = this.list(qualityInspectionQueryWrapper);
            if (CollUtil.isNotEmpty(list)) {
                throw new IllegalArgumentException("编号已存在，操作失败");
            }

        } else {
            //修改 --> update 更新人、更新时间
            qualityInspection.setUpdateBy(CurrentUserUtil.getUsername());
            qualityInspection.setUpdateTime(new Date());

            QueryWrapper<QualityInspection> qualityInspectionQueryWrapper = new QueryWrapper<>();
            qualityInspectionQueryWrapper.lambda().ne(QualityInspection::getId, id);
//            qualityInspectionQueryWrapper.lambda().eq(QualityInspection::getCode, qualityInspection.getCode());
            qualityInspectionQueryWrapper.lambda().eq(QualityInspection::getIsDeleted, 0);
            List<QualityInspection> list = this.list(qualityInspectionQueryWrapper);
            if (CollUtil.isNotEmpty(list)) {
                throw new IllegalArgumentException("编号已存在，操作失败");
            }
        }
        this.saveOrUpdate(qualityInspection);
        return qualityInspection;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delQualityInspection(Long id) {
//        QualityInspection qualityInspection = this.baseMapper.selectById(id);
//        if (ObjectUtil.isEmpty(qualityInspection)) {
//            throw new IllegalArgumentException("质检检测不存在，无法删除");
//        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        QualityInspection qualityInspection = new QualityInspection();
        qualityInspection.setId(id);
        qualityInspection.setUpdateBy(CurrentUserUtil.getUsername());
        qualityInspection.setUpdateTime(new Date());
        qualityInspection.setIsDeleted(true);
        this.updateById(qualityInspection);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryQualityInspectionForExcel(QueryQualityInspectionForm queryQualityInspectionForm, HttpServletRequest req) {
        return this.baseMapper.queryQualityInspectionForExcel(queryQualityInspectionForm);
    }

    /**
     * 生成质检单
     *
     * @param details
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void generateQualityInspection(ReceiptVO details) {
        QualityInspection qualityInspection = ConvertUtil.convert(details, QualityInspection.class);
        String receiptOrderNo = authClient.getOrder("quality_inspection", new Date()).getResult();
        Date date = new Date();
        qualityInspection.setStatus(1).setProcessFlag(null).setQcNo(receiptOrderNo).setRemark(null).setCreateBy(CurrentUserUtil.getUsername())
                .setUpdateTime(date).setUpdateTime(null).setUpdateBy(null);

        this.save(qualityInspection);
        List<MaterialVO> materialList = details.getMaterialForms();
        Map<String, List<MaterialVO>> group = materialList.stream().collect(Collectors.groupingBy(e -> e.getMaterialCode()));
        List<QualityInspectionMaterial> inspectionMaterials = new ArrayList<>();
        group.forEach((k, v) -> {
            MaterialVO tmp = v.get(0);
            QualityInspectionMaterial inspectionMaterial = ConvertUtil.convert(tmp, QualityInspectionMaterial.class);
            Double totalActualNum = 0.0;
            for (MaterialVO materialVO : v) {
                if (MaterialStatusEnum.FOUR.getCode().equals(materialVO.getStatus())||materialVO.getActualNum() <= 0.0) {continue;}
                totalActualNum += materialVO.getActualNum();
            }
            if (totalActualNum > 0.0) {
                inspectionMaterial.setQualityInspectionId(qualityInspection.getId()).setInspectionQuantity(0).setQualifiedQuantity(0).setUnqualifiedQuantity(0)
                        .setNum(totalActualNum).setActualNum(totalActualNum).setCreateTime(date).setCreateBy(CurrentUserUtil.getUsername())
                        .setUpdateBy(null).setUpdateTime(null);
                inspectionMaterials.add(inspectionMaterial);
            }

        });
        this.qualityInspectionMaterialService.saveBatch(inspectionMaterials);

        this.receiptService.update(new Receipt().setQcNo(qualityInspection.getQcNo()),
                new QueryWrapper<>(new Receipt().setReceiptNum(qualityInspection.getReceiptNum()).setIsDeleted(false)));
    }

    @Override
    public void createOrder(QualityInspectionForm form) {
        Date date = new Date();
        QualityInspection qualityInspection = new QualityInspection().setRemark(form.getRemark());
        qualityInspection.setId(form.getId()).setUpdateTime(date).setUpdateBy(CurrentUserUtil.getUsername());

        this.updateById(qualityInspection);

        List<QualityInspectionMaterial> materials = this.qualityInspectionMaterialService.getByCondition(new QualityInspectionMaterial().setQualityInspectionId(qualityInspection.getId()).setIsDeleted(false));
        Map<Long, QualityInspectionMaterial> oldMap = materials.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        List<QualityInspectionMaterialForm> tmps = form.getMaterialForms();
        List<QualityInspectionMaterial> list = new ArrayList<>();
        for (QualityInspectionMaterialForm tmp : tmps) {
            QualityInspectionMaterial inspectionMaterial = oldMap.get(tmp.getId());
            QualityInspectionMaterial convert = ConvertUtil.convert(tmp, QualityInspectionMaterial.class);
            if (ObjectUtil.equal(inspectionMaterial, convert)) {
                continue;
            }
            inspectionMaterial.setIsDeleted(true).setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(date);
            list.add(inspectionMaterial);
            convert.setId(null).setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
            list.add(convert);
        }
        if (list.size() > 0) {
            this.qualityInspectionMaterialService.saveOrUpdateBatch(list);
        }
    }

    @Override
    public QualityInspectionVO getDetails(Long id) {
        //获取收货通知单
        QualityInspection qualityInspection = this.getById(id);
        QualityInspectionVO inspectionVO = ConvertUtil.convert(qualityInspection, QualityInspectionVO.class);
        //获取物理信息
        List<QualityInspectionMaterial> materials = this.qualityInspectionMaterialService.getByCondition(new QualityInspectionMaterial().setQualityInspectionId(inspectionVO.getId()).setIsDeleted(false));
        List<QualityInspectionMaterialVO> materialList = ConvertUtil.convertList(materials, QualityInspectionMaterialVO.class);
        inspectionVO.setMaterialForms(materialList);
        return inspectionVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(Long id) {
        //获取详情数据
        QualityInspectionVO details = this.getDetails(id);
        if (details.getStatus().equals(2) || details.getIsSkip()) {
            throw new ServiceException("该状态无法执行操作");
        }

        String status = this.orderProcessService.getNextNode(details.getReceiptNoticeNum(), details.getProcessFlag());
        if (StringUtils.isEmpty(status)) {
            throw new ServiceException("不存在该流程节点");
        }

        Receipt receipt = this.receiptService.getOne(new QueryWrapper<>(new Receipt().setReceiptNum(details.getReceiptNum()).setIsDeleted(false)));
        ReceiptVO receiptDetails = this.receiptService.getDetails(receipt.getId());

        Map<String, QualityInspectionMaterialVO> qualityInspectionMaterialMap = details.getMaterialForms().stream().filter(e -> e.getUnqualifiedQuantity() > 0).collect(Collectors.toMap(e -> e.getMaterialCode(), e -> e));

        List<MaterialVO> materialList = receiptDetails.getMaterialForms();
        materialList.forEach(e -> {
            if (qualityInspectionMaterialMap.get(e.getMaterialCode()) != null) {
                e.setColumnOne("2");
            }
        });

        switch (InboundOrderProStatusEnum.getEnum(Integer.valueOf(status))) {
            case FOUR:
                this.incomingSeedingService.doWarehousingSeeding(receiptDetails);
                break;
            case FIVE:
                this.shelfOrderService.generateShelfOrder(receiptDetails);
                break;
        }

        QualityInspection qualityInspection = new QualityInspection();
        qualityInspection.setId(details.getId());

        AuthUserDetail authUserDetail = CurrentUserUtil.getUserDetail();
        qualityInspection.setStatus(2).setQualityInspectionDeptId(authUserDetail.getDepartId())
                .setQualityInspectionDept(authUserDetail.getDepartName()).setQualityInspector(CurrentUserUtil.getUsername()).setQualityInspectionTime(LocalDate.now());
        this.updateById(qualityInspection);

        OrderTrack orderTrack = new OrderTrack().setOrderNo(details.getReceiptNoticeNum()).setSubOrderNo(details.getQcNo()).setType(1)
                .setStatus(details.getStatus() + "").setStatusName(details.getStatusDesc());
        orderTrack.setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(new Date());
        this.orderTrackService.save(orderTrack);
    }

    @Override
    public void transferPendingShelf(Long id) {
        //获取详情数据
        QualityInspectionVO details = this.getDetails(id);
        if (details.getStatus().equals(2) || details.getIsSkip()) {
            throw new ServiceException("该状态无法执行操作");
        }
        Receipt receipt = this.receiptService.getOne(new QueryWrapper<>(new Receipt().setReceiptNum(details.getReceiptNum()).setIsDeleted(false)));
        ReceiptVO receiptDetails = this.receiptService.getDetails(receipt.getId());

        this.shelfOrderService.generateShelfOrder(receiptDetails);

        QualityInspection qualityInspection = new QualityInspection();
        qualityInspection.setIsSkip(true).setId(details.getId()).setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(new Date());
        this.updateById(qualityInspection);


        OrderTrack orderTrack = new OrderTrack().setOrderNo(details.getReceiptNoticeNum()).setSubOrderNo(details.getQcNo()).setType(1)
                .setRemark("转为待上架");
        orderTrack.setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(new Date());
        this.orderTrackService.save(orderTrack);
    }


    @Override
    public void copyQualityInspectionMaterial(QueryQualityInspectionMaterialForm queryQualityInspectionMaterialForm) {

        Date date = new Date();
        QualityInspectionMaterial qualityInspectionMaterialOne = qualityInspectionMaterialService.findQualityInspectionMaterialOne(queryQualityInspectionMaterialForm);

        queryQualityInspectionMaterialForm.checkParam();
        QualityInspectionMaterial qualityInspectionMaterial = qualityInspectionMaterialOne;
        List<QualityInspectionMaterial> list = new ArrayList<>();
        QualityInspectionMaterial convert = ConvertUtil.convert(qualityInspectionMaterial, QualityInspectionMaterial.class);
        convert.setInspectionQuantity(queryQualityInspectionMaterialForm.getInspectionQuantity());//检验数量
        convert.setQualifiedQuantity(queryQualityInspectionMaterialForm.getQualifiedQuantity()); //合格数量
        convert.setUnqualifiedQuantity(queryQualityInspectionMaterialForm.getUnqualifiedQuantity());//不合格数量
        convert.setCauseNonconformity(queryQualityInspectionMaterialForm.getCauseNonconformity());//不合格原因(字典值)
        convert.setDescription(queryQualityInspectionMaterialForm.getDescription()); //后期会有图片
        qualityInspectionMaterial.setIsDeleted(true).setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(date);
        list.add(qualityInspectionMaterial);
        convert.setId(null).setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
        list.add(convert);
        if (list.size() > 0) {
            this.qualityInspectionMaterialService.saveOrUpdateBatch(list);
        }
    }

    @Override
    public void changeQualityUser(Long qcno) {
        QualityInspection qualityInspection = this.getById(qcno);
        qualityInspection.setQualityInspector(CurrentUserUtil.getUsername());
        qualityInspection.setQualityInspectionTime(LocalDate.now());
        this.updateById(qualityInspection);
    }
}
