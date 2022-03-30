package com.jayud.wms.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.dto.QueryClientReceiptNoticeForm;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.mapper.ReceiptNoticeMapper;
import com.jayud.wms.model.bo.*;
import com.jayud.wms.model.enums.*;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.NoticeMaterialVO;
import com.jayud.wms.model.vo.NoticeSnMaterialVO;
import com.jayud.wms.model.vo.ReceiptNoticeVO;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.wms.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 收货通知单 服务实现类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Service
public class ReceiptNoticeServiceImpl extends ServiceImpl<ReceiptNoticeMapper, ReceiptNotice> implements IReceiptNoticeService {

    @Autowired
    private INoticeMaterialService noticeMaterialService;
    @Autowired
    private ReceiptNoticeMapper receiptNoticeMapper;
    @Autowired
    private IOrderProcessService orderProcessService;
    @Autowired
    private IWarehouseProcessConfService warehouseProcessConfService;
    @Autowired
    private INoticeSnMaterialService noticeSnMaterialService;
    @Autowired
    private IOrderTrackService orderTrackService;
    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;
    @Autowired
    private IWmsMaterialPackingSpecsService wmsMaterialPackingSpecsService;
    @Autowired
    private AuthClient authClient;
    @Autowired
    private IReceiptService receiptService;
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private IMaterialSnService materialSnService;
    @Autowired
    private IWmsOutboundOrderInfoToMaterialService wmsOutboundOrderInfoToMaterialService;
    @Autowired
    public IWarehouseService warehouseService;
    @Autowired
    public IWmsOwerInfoService wmsOwerInfoService;

    @Override
    public IPage<ReceiptNoticeVO> selectPage(QueryReceiptNoticeForm queryReceiptNoticeForm,
                                             Integer pageNo,
                                             Integer pageSize,
                                             HttpServletRequest req) {

        Page<QueryReceiptNoticeForm> page = new Page<QueryReceiptNoticeForm>(pageNo, pageSize);


//        List<String> strList = Arrays.asList("1","2","3","4");

//        List<Long> longListOwer= new ArrayList<>();
//// 把String类型集合转成 long 类型的 集合
//        CollectionUtils.collect(receiptNoticeFindForm.getOwerIdList() , input -> Long.valueOf(input.toString()), longListOwer);
//        receiptNoticeFindForm.setOwerIdListLong(longListOwer);
//
//
//        List<Long> longListWarehouse= new ArrayList<>();
//// 把String类型集合转成 long 类型的 集合
//        CollectionUtils.collect(receiptNoticeFindForm.getWarehouseIdList() , input -> Long.valueOf(input.toString()), longListWarehouse);
//        receiptNoticeFindForm.setWarehouseIdListLong(longListWarehouse);
//

        IPage<ReceiptNoticeVO> pageList = receiptNoticeMapper.pageList(page, queryReceiptNoticeForm);
        return pageList;
    }

    @Override
    public List<ReceiptNotice> selectList(ReceiptNotice receiptNotice) {
        return receiptNoticeMapper.list(receiptNotice);
    }

    /**
     * 创建收货通知单
     *
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReceiptNotice createOrder(ReceiptNoticeForm form) {

        ReceiptNotice receiptNotice = ConvertUtil.convert(form, ReceiptNotice.class);
        Date date = new Date();
        Map<Long, NoticeMaterial> oldMaterialMap = new HashMap<>();
        form.calculateTotalQuantity();
        //创建收货通知单
        if (form.getId() == null) {
            receiptNotice.setReceiptNoticeNum(authClient.getOrder("receipt_notice", new Date()).getResult())
                    .setStatus(1).setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
            //创建订单流程配置  待定
//            this.orderProcessService.generationProcess(receiptNotice.getReceiptNoticeNum(), form.getWarehouseId(), 1);
            //运行轨迹
//            OrderTrack orderTrack = new OrderTrack().setOrderNo(receiptNotice.getReceiptNoticeNum()).setType(1)
//                    .setStatus(receiptNotice.getStatus() + "").setStatusName(ReceiptNoticeStatusEnum.CREATE.getDesc());
//            orderTrack.setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
//            this.orderTrackService.save(orderTrack);
            this.save(receiptNotice);
        } else {
            System.out.println("进入到了方法！");
            receiptNotice.setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(date);
            List<NoticeMaterial> oldNoticeMaterials = this.noticeMaterialService.getByCondition(new NoticeMaterial().setIsDeleted(false).setReceiptNoticeId(form.getId()));
            oldMaterialMap = oldNoticeMaterials.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
            this.update(receiptNotice, Wrappers.<ReceiptNotice>lambdaUpdate()
                    .set(ReceiptNotice::getSupplierId, receiptNotice.getSupplierId() == null ? null : receiptNotice.getSupplierId())
                    .eq(ReceiptNotice::getId, receiptNotice.getId()));
        }
        //存储物料数据
        List<NoticeMaterialForm> noticeMaterialForms = form.getNoticeMaterialForms();

        for (NoticeMaterialForm noticeMaterialForm : noticeMaterialForms) {
            oldMaterialMap.remove(noticeMaterialForm.getId());
            noticeMaterialForm.setReceiptNoticeId(receiptNotice.getId()).setReceiptNoticeNum(receiptNotice.getReceiptNoticeNum());
            if (noticeMaterialForm.getId() == null) {
                noticeMaterialForm.setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
            } else {
                noticeMaterialForm.setUpdateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
            }
        }
        List<NoticeMaterial> noticeMaterials = ConvertUtil.convertList(noticeMaterialForms, NoticeMaterial.class);
        //删除物料
        List<NoticeMaterial> deleteMaterials = new ArrayList<>();
        oldMaterialMap.forEach((k, v) -> {
            NoticeMaterial noticeMaterial = new NoticeMaterial();
            noticeMaterial.setId(v.getId());
            noticeMaterial.setIsDeleted(true).setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(date);
            deleteMaterials.add(noticeMaterial);
        });
        if (deleteMaterials.size() > 0) {
            this.noticeMaterialService.updateBatchById(deleteMaterials);
        }
        noticeMaterialService.saveOrUpdateBatch(noticeMaterials);

        //存储物料SN信息表
//        List<NoticeSnMaterialForm> snMaterialForms = form.getNoticeSnMaterialForms();
//        this.noticeSnMaterialService.createOrder(receiptNotice.getId(), receiptNotice.getReceiptNoticeNum(), snMaterialForms);

        return receiptNotice;//返回收货通知单
    }


    @Override
    public ReceiptNoticeVO getDetails(Long id) {
        //获取收货通知单
        ReceiptNotice receiptNotice = this.getById(id);
        ReceiptNoticeVO receiptNoticeVO = ConvertUtil.convert(receiptNotice, ReceiptNoticeVO.class);
        //获取物理信息
        List<NoticeMaterial> materials = this.noticeMaterialService.getByCondition(new NoticeMaterial().setReceiptNoticeId(receiptNoticeVO.getId()).setIsDeleted(false));
        List<NoticeMaterialVO> noticeMaterialList = ConvertUtil.convertList(materials, NoticeMaterialVO.class);
        //获取物料sn信息
        List<NoticeSnMaterial> snMaterials = this.noticeSnMaterialService.getByCondition(new NoticeSnMaterial().setReceiptNoticeId(receiptNoticeVO.getId()).setIsDeleted(false));
        List<NoticeSnMaterialVO> noticeSnMaterialList = ConvertUtil.convertList(snMaterials, NoticeSnMaterialVO.class);

        receiptNoticeVO.setNoticeMaterialForms(noticeMaterialList);
        receiptNoticeVO.setNoticeSnMaterialForms(noticeSnMaterialList);

        noticeMaterialList.stream().forEach(e -> {
            WmsMaterialBasicInfoVO wmsMaterialBasicInfoFrom = new WmsMaterialBasicInfoVO();
            wmsMaterialBasicInfoFrom.setMaterialCode(e.getMaterialCode());
            WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO1 = wmsMaterialPackingSpecsService.selectByMaterialId(wmsMaterialBasicInfoFrom);
            e.setPackingList(wmsMaterialBasicInfoVO1.getPackingList());
        });


        return receiptNoticeVO;
    }

    @Override
    public void addMaterialToTerminal(Map<String, Object> map) {
        String serialNum = MapUtil.getStr(map, "serialNum");
        String materialCode = MapUtil.getStr(map, "materialCode");
        //查询物料sn信息
        NoticeSnMaterial condition = new NoticeSnMaterial();
        if (StringUtils.isEmpty(serialNum)) {
            condition = new NoticeSnMaterial().setSerialNum(serialNum);
        }
        if (StringUtils.isEmpty(materialCode)) {
            condition = new NoticeSnMaterial().setMaterialCode(materialCode);
        }
        List<NoticeSnMaterial> noticeSnMaterials = this.noticeSnMaterialService.getByCondition(condition);
        if (CollectionUtil.isEmpty(noticeSnMaterials)) {
            throw new ServiceException("请添加物料SN信息才能进行操作");
        }
        NoticeSnMaterial noticeSnMaterial = noticeSnMaterials.get(0);

        List<WmsMaterialBasicInfo> materialBasics = this.wmsMaterialBasicInfoService.getByCondition(new WmsMaterialBasicInfo().setMaterialCode(noticeSnMaterial.getMaterialCode()));
        if (CollectionUtil.isEmpty(materialBasics)) {
            throw new ServiceException("不存在物料基础信息");
        }
        WmsMaterialBasicInfo materialBasicInfo = materialBasics.get(0);

        WmsMaterialPackingSpecs wmsMaterialPackingSpecs = new WmsMaterialPackingSpecs();
        wmsMaterialPackingSpecs.setMaterialBasicInfoId(materialBasicInfo.getId()).setUnit(noticeSnMaterial.getUnit());
        List<WmsMaterialPackingSpecs> packingSpecsList = this.wmsMaterialPackingSpecsService.getByCondition(wmsMaterialPackingSpecs);
        if (CollectionUtil.isEmpty(packingSpecsList)) {
            throw new ServiceException("不存在包装信息");
        }
        WmsMaterialPackingSpecs packingSpecs = packingSpecsList.get(0);
        List<NoticeMaterial> noticeMaterials = this.noticeMaterialService.getByCondition(new NoticeMaterial().setMaterialCode(noticeSnMaterial.getMaterialCode()).setIsDeleted(false));
        if (CollectionUtil.isEmpty(noticeMaterials)) {
            throw new ServiceException("该物料不存在列表");
        }
        //数量
        Double num = noticeSnMaterial.getNum() * packingSpecs.getAccount();

        NoticeMaterial noticeMaterial = noticeMaterials.get(0);
        NoticeMaterial tmp = new NoticeMaterial().setNum(num);
        tmp.setId(noticeMaterial.getId()).setUpdateTime(new Date()).setUpdateBy(CurrentUserUtil.getUsername());
        this.noticeMaterialService.updateById(tmp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Receipt transferReceipt(Long id) {
        //获取详情数据
        ReceiptNoticeVO details = this.getDetails(id);
        if (!details.getStatus().equals(ReceiptNoticeStatusEnum.CREATE.getCode())) {
            throw new ServiceException("该阶段无法转收货单");
        }
        String status = this.orderProcessService.getNextNode(details.getReceiptNoticeNum(), details.getProcessFlag());
        if (StringUtils.isEmpty(status)) {
            throw new ServiceException("不存在该流程节点");
        }
        if (status.equals(InboundOrderProStatusEnum.TWO.getCode() + "")) {
            Date date = new Date();
            Receipt receipt = ConvertUtil.convert(details, Receipt.class);
            //把数据转移到收货单
            String receiptOrderNo = authClient.getOrder("receipt", new Date()).getResult();
            receipt.setIsPutShelf(false).setActualNum(0.0).setActualVolume(0.0)
                    .setActualWeight(0.0).setProcessFlag(null).setStatus(ReceiptStatusEnum.ONE.getCode()).setPlannedReceivingTime(details.getEstimatedArrivalTime())
                    .setReceiptNum(receiptOrderNo).setId(null);

            this.receiptService.save(receipt);

            List<NoticeMaterialVO> noticeMaterialList = details.getNoticeMaterialForms();
            List<Material> materials = new ArrayList<>();
            for (NoticeMaterialVO noticeMaterialVO : noticeMaterialList) {
                BigDecimal account = wmsOutboundOrderInfoToMaterialService.getDistributionAccount(noticeMaterialVO.getMaterialCode(), noticeMaterialVO.getUnit(), new BigDecimal(noticeMaterialVO.getNum()));
                Material material = ConvertUtil.convert(noticeMaterialVO, Material.class);
                material.setId(null).setCreateTime(date).setCreateBy(CurrentUserUtil.getUsername()).setUpdateBy(null).setUpdateTime(null);
                material.setStatus(MaterialStatusEnum.ONE.getCode()).setUnit("EA").setNum(account.doubleValue()).setActualNum(0.0).setIsPutShelf(false).setOrderNum(receiptOrderNo).setOrderId(receipt.getId());
                materials.add(material);
            }
            this.materialService.saveBatch(materials);

            List<NoticeSnMaterialVO> noticeSnMaterialList = details.getNoticeSnMaterialForms();
            List<MaterialSn> materialSns = new ArrayList<>();
            for (NoticeSnMaterialVO noticeSnMaterialVO : noticeSnMaterialList) {
                MaterialSn materialSn = ConvertUtil.convert(noticeSnMaterialVO, MaterialSn.class);
                materialSn.setId(null).setCreateTime(date).setCreateBy(CurrentUserUtil.getUsername()).setUpdateBy(null).setUpdateTime(null);
                materialSn.setStatus(MaterialSnStatusEnum.ONE.getCode()).setOrderNum(receiptOrderNo).setOrderId(receipt.getId());
                materialSns.add(materialSn);
            }
            this.materialSnService.saveBatch(materialSns);

            //收货通知单更改数据
            ReceiptNotice receiptNotice = new ReceiptNotice();
            receiptNotice.setId(details.getId());
            receiptNotice.setReceiptNum(receipt.getReceiptNum()).setConfirmedBy(CurrentUserUtil.getUsername())
                    .setConfirmationTime(LocalDate.now()).setStatus(ReceiptNoticeStatusEnum.RECEIVING.getCode());
            this.updateById(receiptNotice);

            //添加流程记录
            OrderTrack orderTrack = new OrderTrack().setOrderNo(details.getReceiptNoticeNum()).setType(1)
                    .setStatus(receiptNotice.getStatus() + "").setStatusName(ReceiptNoticeStatusEnum.RECEIVING.getDesc());
            orderTrack.setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
            this.orderTrackService.save(orderTrack);

            return receipt;
        }else{

            return null;
        }
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryReceiptNoticeForExcel(QueryReceiptNoticeForm queryReceiptNoticeForm,
                                                                          HttpServletRequest req) {
        return receiptNoticeMapper.queryReceiptNoticeForExcel(queryReceiptNoticeForm);
    }

    @Override
    public void updateByCondition(ReceiptNotice receiptNotice, ReceiptNotice condition) {
        receiptNotice.setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(new Date());
        this.update(receiptNotice, new QueryWrapper<>(condition));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletedReceiptNotice(Long id) {

        ReceiptNotice receiptNotice = new ReceiptNotice();
        receiptNotice.setId(id);
        receiptNotice.setUpdateBy(CurrentUserUtil.getUsername());
        receiptNotice.setUpdateTime(new Date());
        receiptNotice.setIsDeleted(true);
        receiptNoticeMapper.updateById(receiptNotice);
        //查询物料单信息
        List<NoticeMaterial> oldNoticeMaterials = this.noticeMaterialService.getByCondition(new NoticeMaterial().setIsDeleted(false).setReceiptNoticeId(id));
        //删除物料
        oldNoticeMaterials.forEach(v -> {
            NoticeMaterial noticeMaterial = new NoticeMaterial();
            noticeMaterial.setId(v.getId());
            noticeMaterial.setIsDeleted(true).setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(new Date());
            this.noticeMaterialService.updateById(noticeMaterial);
        });

//        //查询物料sn编码单
//        List<NoticeSnMaterial> snMaterials = this.noticeSnMaterialService.getByCondition(new NoticeSnMaterial().setReceiptNoticeId(id).setIsDeleted(false));
//        //删除物料sn编码单
//        snMaterials.stream().forEach(v -> {
//            NoticeSnMaterial noticeSnMaterial = new NoticeSnMaterial();
//            noticeSnMaterial.setId(v.getId());
//            noticeSnMaterial.setIsDeleted(true);
//            noticeSnMaterial.setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(new Date());
//            //删除物料单sn
//            noticeSnMaterialService.updateById(noticeSnMaterial);
//        });
        return true;
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public BaseResult addOrUpdateFeign(QueryClientReceiptNoticeForm queryClientReceiptNoticeForm) {

        QueryClientReceiptNoticeForm convert = ConvertUtil.convert(queryClientReceiptNoticeForm, QueryClientReceiptNoticeForm.class);

        //得到仓库id
        QueryWarehouseForm queryWarehouseForm = new QueryWarehouseForm();
        queryWarehouseForm.setCode(convert.getWarehouseCode()); //仓库编码
        queryWarehouseForm.setName(convert.getWarehouse()); //仓库名称
        queryWarehouseForm.setStatus(1);//启用
        List<Warehouse> warehouses = warehouseService.selectList(queryWarehouseForm);

        //得到货主id
        WmsOwerInfoForm WmsOwerInfoForm = new WmsOwerInfoForm();
        WmsOwerInfoForm.setWarehouseId(warehouses.get(0).getId());//仓库id
        WmsOwerInfoForm.setIsOn(true);//是否启用   启用 true
        WmsOwerInfoForm.setOwerCode(convert.getOwerCode());//货主code
        WmsOwerInfoForm.setOwerName(convert.getOwer());//货主名称
        List<WmsOwerInfo> wmsOwerInfos = wmsOwerInfoService.selectWmsOwerInfoWarehouseIdList(WmsOwerInfoForm);

        ReceiptNoticeForm form = ConvertUtil.convert(queryClientReceiptNoticeForm, ReceiptNoticeForm.class);
        if (!ReceiptNoticeStatusEnum.CREATE.getCode().equals(form.getStatus())) {

            return BaseResult.error("该状态无法操作");
        }
//        form.setOwerId(wmsOwerInfos.get(0).getId());//货主id
//        form.setWarehouseId(warehouses.get(0).getId());//仓库id

        form.setOwerId(convert.getOwerId());//货主id
        form.setWarehouseId(convert.getWarehouseId());//仓库id


        form.setOrderSource("MES下发"); //创建订单源值
        form.setOrderSourceCode(2);//

        List<NoticeSnMaterialForm> noticeSnMaterialForms = new ArrayList<>();
//        form.setNoticeSnMaterialForms(noticeSnMaterialForms);
        createOrderFeign(form);
        return BaseResult.ok();
    }


    /**
     * ERP创建收货通知单
     *
     * @param form
     */
//    @Transactional(rollbackFor = Exception.class)
    public BaseResult createOrderFeign(ReceiptNoticeForm form) {

        ReceiptNotice receiptNotice = ConvertUtil.convert(form, ReceiptNotice.class);
        Date date = new Date();
        Map<Long, NoticeMaterial> oldMaterialMap = new HashMap<>();
        form.calculateTotalQuantity();
        //创建收货通知单
        if (form.getId() == null) {
            receiptNotice.setReceiptNoticeNum(authClient.getOrder("receipt_notice", new Date()).getResult())
                    .setStatus(1).setCreateBy(form.getCreateBy()).setCreateTime(date);
            //创建订单流程配置
            this.orderProcessService.generationProcess(receiptNotice.getReceiptNoticeNum(), form.getWarehouseId(), 1);
            OrderTrack orderTrack = new OrderTrack().setOrderNo(receiptNotice.getReceiptNoticeNum()).setType(1)
                    .setStatus(receiptNotice.getStatus() + "").setStatusName(ReceiptNoticeStatusEnum.CREATE.getDesc());
            orderTrack.setCreateBy(form.getCreateBy()).setCreateTime(date);
            this.orderTrackService.save(orderTrack);
            this.save(receiptNotice);
        } else {
            receiptNotice.setUpdateBy(form.getCreateBy()).setUpdateTime(date);
            List<NoticeMaterial> oldNoticeMaterials = this.noticeMaterialService.getByCondition(new NoticeMaterial().setIsDeleted(false).setReceiptNoticeId(form.getId()));
            oldMaterialMap = oldNoticeMaterials.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
            this.update(receiptNotice, Wrappers.<ReceiptNotice>lambdaUpdate()
                    .set(ReceiptNotice::getSupplierId, receiptNotice.getSupplierId() == null ? null : receiptNotice.getSupplierId())
                    .eq(ReceiptNotice::getId, receiptNotice.getId()));
        }
        //存储物料数据
        List<NoticeMaterialForm> noticeMaterialForms = form.getNoticeMaterialForms();

        for (NoticeMaterialForm noticeMaterialForm : noticeMaterialForms) {
            oldMaterialMap.remove(noticeMaterialForm.getId());
            noticeMaterialForm.setReceiptNoticeId(receiptNotice.getId()).setReceiptNoticeNum(receiptNotice.getReceiptNoticeNum());
            if (noticeMaterialForm.getId() == null) {
                noticeMaterialForm.setCreateBy(form.getCreateBy()).setCreateTime(date);
            } else {
                noticeMaterialForm.setUpdateBy(form.getCreateBy()).setCreateTime(date);
            }
        }
        List<NoticeMaterial> noticeMaterials = ConvertUtil.convertList(noticeMaterialForms, NoticeMaterial.class);
        //删除物料
        List<NoticeMaterial> deleteMaterials = new ArrayList<>();
        oldMaterialMap.forEach((k, v) -> {
            NoticeMaterial noticeMaterial = new NoticeMaterial();
            noticeMaterial.setId(v.getId());
            noticeMaterial.setIsDeleted(true).setUpdateBy(form.getCreateBy()).setUpdateTime(date);
            deleteMaterials.add(noticeMaterial);
        });
        if (deleteMaterials.size() > 0) {
            this.noticeMaterialService.updateBatchById(deleteMaterials);
        }
        noticeMaterialService.saveOrUpdateBatch(noticeMaterials);

        //存储物料SN信息表
//        List<NoticeSnMaterialForm> snMaterialForms = form.getNoticeSnMaterialForms();
//        this.noticeSnMaterialService.createOrder(receiptNotice.getId(), receiptNotice.getReceiptNoticeNum(), snMaterialForms);

        return BaseResult.ok();
    }


}
