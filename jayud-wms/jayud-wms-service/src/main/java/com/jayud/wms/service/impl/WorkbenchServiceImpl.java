package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.*;
import com.jayud.wms.model.bo.*;
import com.jayud.wms.model.enums.ColorCodeEnum;
import com.jayud.wms.model.enums.ContainerNumEnum;
import com.jayud.wms.model.enums.ShelfMoveTaskEnum;
import com.jayud.wms.model.enums.ShelfQueryTypeEnum;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.MaterialDetailVO;
import com.jayud.wms.model.vo.MaterialVO;
import com.jayud.wms.model.vo.ReceiptVO;
import com.jayud.wms.model.vo.WarehouseShelfSketchMapVO;
import com.jayud.wms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作台 服务实现类
 *
 * @author jyd
 * @since 2021-12-17
 */
@Service
public class WorkbenchServiceImpl extends ServiceImpl<WorkbenchMapper, Workbench> implements IWorkbenchService {


    @Autowired
    private WorkbenchMapper workbenchMapper;
    @Autowired
    private IBreakoutWorkbenchService breakoutWorkbenchService;
    @Autowired
    private ISeedingWallService seedingWallService;
    @Autowired
    private ISeedingWallLayoutService seedingWallLayoutService;
    @Autowired
    private WarehouseShelfMapper warehouseShelfMapper;
    @Autowired
    private WarehouseLocationMapper warehouseLocationMapper;
    @Autowired
    private InventoryDetailMapper inventoryDetailMapper;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private IShelfMoveTaskService shelfMoveTaskService;
    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;
    @Autowired
    private IReceiptNoticeService receiptNoticeService;
    @Autowired
    private IWmsOwerInfoService wmsOwerInfoService;
    @Autowired
    private IReceiptService receiptService;
    @Autowired
    private IShelfOrderTaskService shelfOrderTaskService;

    @Override
    public IPage<Workbench> selectPage(Workbench workbench,
                                       Integer pageNo,
                                       Integer pageSize,
                                       HttpServletRequest req) {

        Page<Workbench> page = new Page<Workbench>(pageNo, pageSize);
        IPage<Workbench> pageList = workbenchMapper.pageList(page, workbench);
        return pageList;
    }

    @Override
    public List<Workbench> selectList(Workbench workbench) {
        return workbenchMapper.list(workbench);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Workbench saveOrUpdateWorkbench(Workbench workbench) {
        Long id = workbench.getId();
        Long warehouseId = workbench.getWarehouseId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            workbench.setCreateBy(CurrentUserUtil.getUsername());
            workbench.setCreateTime(new Date());

            QueryWrapper<Workbench> workbenchQueryWrapper = new QueryWrapper<>();
            //workbenchQueryWrapper.lambda().eq(Workbench::getWarehouseId, warehouseId);
            workbenchQueryWrapper.lambda().eq(Workbench::getCode, workbench.getCode());
            workbenchQueryWrapper.lambda().eq(Workbench::getIsDeleted, 0);
            List<Workbench> list = this.list(workbenchQueryWrapper);
            if (CollUtil.isNotEmpty(list)) {
                throw new IllegalArgumentException("编号已存在，操作失败");
            }

        } else {
            //修改 --> update 更新人、更新时间
            workbench.setUpdateBy(CurrentUserUtil.getUsername());
            workbench.setUpdateTime(new Date());

            QueryWrapper<Workbench> workbenchQueryWrapper = new QueryWrapper<>();
            workbenchQueryWrapper.lambda().ne(Workbench::getId, id);
            //workbenchQueryWrapper.lambda().eq(Workbench::getWarehouseId, warehouseId);
            workbenchQueryWrapper.lambda().eq(Workbench::getCode, workbench.getCode());
            workbenchQueryWrapper.lambda().eq(Workbench::getIsDeleted, 0);
            List<Workbench> list = this.list(workbenchQueryWrapper);
            if (CollUtil.isNotEmpty(list)) {
                throw new IllegalArgumentException("编号已存在，操作失败");
            }
        }
        this.saveOrUpdate(workbench);
        return workbench;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delWorkbench(int id) {
        Workbench workbench = this.baseMapper.selectById(id);
        if (ObjectUtil.isEmpty(workbench)) {
            throw new IllegalArgumentException("工作台不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        workbench.setUpdateBy(CurrentUserUtil.getUsername());
        workbench.setUpdateTime(new Date());
        workbench.setIsDeleted(true);
        this.saveOrUpdate(workbench);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryWorkbenchForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWorkbenchForExcel(paramMap);
    }

    @Override
    public boolean exitSeedingPositionNumber(String seedingPositionNum, int type) {
        //查询工作台信息
        Workbench workbenche = this.getOne(new QueryWrapper<>(new Workbench().setCode(CurrentUserUtil.getCurrrentUserWorkbenchCode()).setStatus(1).setIsDeleted(false)));
        if (workbenche.getId() == null) {
            throw new ServiceException("暂时没有可用的工作台");
        }
        //然后查询工作台分播墙列表
        if (workbenche.getType() != 2) {
            throw new ServiceException("该工作台类型不是分播类型");
        }
        List<BreakoutWorkbench> breakoutWorkbenchs = this.breakoutWorkbenchService.getBaseMapper().selectList(new QueryWrapper<>(new BreakoutWorkbench().setWorkbenchId(workbenche.getId())));
        if (CollectionUtil.isEmpty(breakoutWorkbenchs)) {
            throw new ServiceException("该工作台暂无绑定分播墙");
        }
        List<String> seedingWallCodes = breakoutWorkbenchs.stream().map(BreakoutWorkbench::getCode).collect(Collectors.toList());
        //过滤类型,没有设置,说明返回所有类型
        QueryWrapper<SeedingWall> condition = new QueryWrapper<>();
        condition.lambda().eq(SeedingWall::getType, type).eq(SeedingWall::getIsDeleted, false).eq(SeedingWall::getStatus, 1).in(SeedingWall::getCode, seedingWallCodes);
        List<SeedingWall> seedingWalls = this.seedingWallService.list(condition);
        if (CollectionUtil.isEmpty(seedingWalls)) {
            throw new ServiceException("该工作台暂无可用分播墙");
        }
        List<Long> seedingWallIds = seedingWalls.stream().map(e -> e.getId()).collect(Collectors.toList());
        QueryWrapper<SeedingWallLayout> tmpCondition = new QueryWrapper<>();
        tmpCondition.lambda().in(SeedingWallLayout::getSeedingWallId, seedingWallIds).eq(SeedingWallLayout::getCode, seedingPositionNum).eq(SeedingWallLayout::getStatus, 1);
        List<SeedingWallLayout> seedingWallLayouts = seedingWallLayoutService.getBaseMapper().selectList(tmpCondition);
        return seedingWallLayouts.size() > 0;
    }

    /**
     * 入库
     *     工作台收货上架
     *
     *     白色:库位为空    white
     *     橙色:本次已收货使用    orange
     *     灰色:之前收货已使用    gray
     *
     * 出库
     *     工作台拣货出库
     *
     *     白色:本次无关库位    white
     *     橙色:待拣货下架库位    orange
     *     绿色: 已拣货下架库位    green
     */
    @Override
    public List<WarehouseShelfSketchMapVO> selectSketchMapByShelf(QueryWarehouseShelfForm form) {
        String shelfcode = form.getShelfcode();//货架编号
        String queryType = form.getQueryType();//查询类型(input入库, output出库)
        if(StrUtil.isEmpty(queryType)){
            throw new IllegalArgumentException("查询类型不能为空");
        }
        List<WarehouseShelfSketchMapVO> list = new ArrayList<>();
        if(queryType.equals(ShelfQueryTypeEnum.INPUT.getTypeCode())){
            //入库 查询货架示意图逻辑

            //1、根据货架号查询货架，判断货架号是否存在
            QueryWrapper<WarehouseShelf> warehouseShelfQueryWrapper = new QueryWrapper<>();
            warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getIsDeleted, 0);
            warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getCode, shelfcode);
            warehouseShelfQueryWrapper.lambda().groupBy(WarehouseShelf::getCode);
            WarehouseShelf warehouseShelf = warehouseShelfMapper.selectOne(warehouseShelfQueryWrapper);

            //2、根据货架id查询库位，判断货架下是否有库位
            Map<String, Object> shelfMap = new HashMap<>();
            shelfMap.put("warehouseId", warehouseShelf.getWarehouseId());
            shelfMap.put("warehouseAreaId", warehouseShelf.getWarehouseAreaId());
            shelfMap.put("shelfId", warehouseShelf.getId());
            List<WarehouseLocation> warehouseLocationList = warehouseLocationMapper.selectLocationCapacityByShelfId(shelfMap);
            List<Long> warehouseLocationIds = warehouseLocationList.stream().map(warehouseLocation -> warehouseLocation.getId()).collect(Collectors.toList());

            //3、根据库位id查询库存明细，判断库位下有多少物料，物料有多少物品
            for (int k=0; k<warehouseLocationList.size(); k++){
                WarehouseLocation warehouseLocation = warehouseLocationList.get(k);
                Long warehouseLocationId = warehouseLocation.getId();
                String warehouseLocationCode = warehouseLocation.getCode();

                shelfMap.put("warehouseLocationId", warehouseLocationId);
                List<InventoryDetail> inventoryDetailList = inventoryDetailMapper.selectWarehouseLocationByshelf(shelfMap);

                WarehouseShelfSketchMapVO sketchMapVO = new WarehouseShelfSketchMapVO();
                sketchMapVO.setWarehouseLocationCode(warehouseLocationCode);
                List<MaterialDetailVO> materialDetails = new ArrayList<>();
                inventoryDetailList.forEach(detail -> {
                    MaterialDetailVO materialDetailVO = new MaterialDetailVO();
                    materialDetailVO.setMaterialId(detail.getMaterialId());
                    materialDetailVO.setMaterialCode(detail.getMaterialCode());
                    materialDetailVO.setMaterialName(detail.getMaterialName());
                    materialDetailVO.setExistingCount(detail.getExistingCount());
                    materialDetailVO.setAllocationCount(detail.getAllocationCount());
                    materialDetailVO.setPickingCount(detail.getPickingCount());
                    materialDetailVO.setUsableCount(detail.getUsableCount());
                    materialDetails.add(materialDetailVO);
                });
                sketchMapVO.setMaterialDetails(materialDetails);

                //白色:库位为空    white  库位下没有物料
                //灰色:之前收货已使用    gray  库位下有物料
                if(CollUtil.isEmpty(materialDetails)){
                    sketchMapVO.setColorCode(ColorCodeEnum.WHITE.getTypeCode());
                }else{
                    sketchMapVO.setColorCode(ColorCodeEnum.GRAY.getTypeCode());
                }
                list.add(sketchMapVO);
            }
        }else{
            //出库 查询货架示意图逻辑  TODO 待定
        }
        return list;
    }

    /**
     * 工作台收货上架:工作台货架回库
     *     4、货架回库
     *         4.1、点击货架回库后，根据收货信息自动生成对应的：货架移库任务（工作台至货架-上架）
     *         4.2、自动生成收货单以及上架任务明细
     *             4.2.1、生成收货通知单
     *             4.2.2、生成收货单
     *             4.2.3、生成收货单对应的上架任务明细
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void putawayBackLibrary(PutawayBackLibraryForm form) {
        Long warehouseId = form.getWarehouseId();
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        if(ObjectUtil.isEmpty(warehouse)){
            throw new IllegalArgumentException("仓库不存在");
        }
        String warehouseName = warehouse.getName();
        String warehouseCode = warehouse.getCode();
        //获取当前登陆人工作台编号
        String workbenchCode = CurrentUserUtil.getCurrrentUserWorkbenchCode();
        QueryWrapper<Workbench> workbenchQueryWrapper = new QueryWrapper<>();
        workbenchQueryWrapper.lambda().eq(Workbench::getCode, workbenchCode);
        workbenchQueryWrapper.lambda().eq(Workbench::getIsDeleted, 0);
        workbenchQueryWrapper.lambda().groupBy(Workbench::getCode);
        Workbench workbench = workbenchMapper.selectOne(workbenchQueryWrapper);
        if(ObjectUtil.isEmpty(workbench)){
            throw new IllegalArgumentException("当前用户，没有指定，工作台");
        }
        Long workbenchId = workbench.getId();

        String shelfCode = form.getShelfCode();
        QueryWrapper<WarehouseShelf> warehouseShelfQueryWrapper = new QueryWrapper<>();
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getCode, shelfCode);
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getIsDeleted, 0);
        warehouseShelfQueryWrapper.lambda().groupBy(WarehouseShelf::getCode);
        WarehouseShelf warehouseShelf = warehouseShelfMapper.selectOne(warehouseShelfQueryWrapper);
        if(ObjectUtil.isEmpty(warehouseShelf)){
            throw new IllegalArgumentException("货架信息，不存在");
        }
        Long shelfId = warehouseShelf.getId();

        //验证
        form.setWorkbenchId(workbenchId);
        form.setWorkbenchCode(workbenchCode);
        form.setShelfId(shelfId);
        form.setShelfCode(shelfCode);
        verificationPutawayBackLibrary(form);
        //4.1、点击货架回库后，根据收货信息自动生成对应的：货架移库任务（工作台至货架-上架）
        CreateShelfMoveTaskForm shelfMoveTaskForm = new CreateShelfMoveTaskForm();
        shelfMoveTaskForm.setMovementTypeCode(ShelfMoveTaskEnum.MTC02.getTypeCode());
        shelfMoveTaskForm.setMovementTypeName(ShelfMoveTaskEnum.MTC02.getTypeDesc());
        shelfMoveTaskForm.setWorkbenchId(workbenchId);
        shelfMoveTaskForm.setWorkbenchCode(workbenchCode);
        List<WarehouseShelf> warehouseShelfList = new ArrayList<>();
        warehouseShelfList.add(warehouseShelf);
        shelfMoveTaskForm.setWarehouseShelfList(warehouseShelfList);
        /**
         * 创建货架移库任务
         * @see com.jyd.bases.service.IShelfMoveTaskService#createShelfMoveTask(CreateShelfMoveTaskForm)
         */
        shelfMoveTaskService.createShelfMoveTask(shelfMoveTaskForm);
        //4.2、自动生成收货单以及上架任务明细
        //4.2.1、生成收货通知单
        List<ReceivingInfoForm> receivingInfoList = form.getReceivingInfoList();
        //                         判断是否 创建 物料基本信息`wms_material_basic_info`
        //                         需要新增保存的 物料基本信息
        List<WmsMaterialBasicInfo> wmsMaterialBasicInfoSaveList = new ArrayList<>();
        for (int i=0; i<receivingInfoList.size(); i++){
            ReceivingInfoForm receivingInfoForm = receivingInfoList.get(i);

            String materialCode = receivingInfoForm.getMaterialCode();//物料编码
            Long owerId = receivingInfoForm.getOwerId();//货主
            //判断物料编码是否存在  根据物料编码+货主id  查询
            QueryWrapper<WmsMaterialBasicInfo> wmsMaterialBasicInfoQueryWrapper = new QueryWrapper<>();
            wmsMaterialBasicInfoQueryWrapper.lambda().eq(WmsMaterialBasicInfo::getOwerId, owerId);
            wmsMaterialBasicInfoQueryWrapper.lambda().eq(WmsMaterialBasicInfo::getMaterialCode,materialCode);
            wmsMaterialBasicInfoQueryWrapper.lambda().eq(WmsMaterialBasicInfo::getIsDeleted, 0);
            wmsMaterialBasicInfoQueryWrapper.lambda().groupBy(WmsMaterialBasicInfo::getMaterialCode, WmsMaterialBasicInfo::getOwerId);
            WmsMaterialBasicInfo wmsMaterialBasicInfo = wmsMaterialBasicInfoService.getOne(wmsMaterialBasicInfoQueryWrapper);
            if(ObjectUtil.isEmpty(wmsMaterialBasicInfo)){
                wmsMaterialBasicInfo = new WmsMaterialBasicInfo();
                wmsMaterialBasicInfo.setMaterialCode(materialCode);//物料编码
                wmsMaterialBasicInfo.setMaterialName(materialCode);//物料名称
                wmsMaterialBasicInfo.setOwerId(owerId);//货主id
                wmsMaterialBasicInfo.setStorageConditionsId(0L);//储存条件id
                wmsMaterialBasicInfo.setWeight(new BigDecimal("0"));//重量
                wmsMaterialBasicInfo.setVolume(new BigDecimal("0"));//体积
                wmsMaterialBasicInfo.setIsOn(true);//是否开启，0否，1是
                wmsMaterialBasicInfo.setCreateBy(CurrentUserUtil.getUsername());
                wmsMaterialBasicInfo.setCreateTime(new Date());
                wmsMaterialBasicInfoSaveList.add(wmsMaterialBasicInfo);
            }
        }
        wmsMaterialBasicInfoService.saveOrUpdateBatch(wmsMaterialBasicInfoSaveList);
        List<Long> owerIds = receivingInfoList.stream().map(receivingInfoForm -> receivingInfoForm.getOwerId()).collect(Collectors.toList());
        List<String> materialCodes = receivingInfoList.stream().map(receivingInfoForm -> receivingInfoForm.getMaterialCode()).collect(Collectors.toList());
        QueryWrapper<WmsMaterialBasicInfo> wmsMaterialBasicInfoQueryWrapper = new QueryWrapper<>();
        wmsMaterialBasicInfoQueryWrapper.lambda().in(WmsMaterialBasicInfo::getOwerId, owerIds);
        wmsMaterialBasicInfoQueryWrapper.lambda().in(WmsMaterialBasicInfo::getMaterialCode,materialCodes);
        wmsMaterialBasicInfoQueryWrapper.lambda().eq(WmsMaterialBasicInfo::getIsDeleted, 0);
        wmsMaterialBasicInfoQueryWrapper.lambda().groupBy(WmsMaterialBasicInfo::getMaterialCode, WmsMaterialBasicInfo::getOwerId);
        List<WmsMaterialBasicInfo> wmsMaterialBasicInfoList = wmsMaterialBasicInfoService.list(wmsMaterialBasicInfoQueryWrapper);
        Map<String, WmsMaterialBasicInfo> wmsMaterialBasicInfoMap = wmsMaterialBasicInfoList.stream().collect(Collectors.toMap(k -> k.getOwerId() + "_" + k.getMaterialCode(), w -> w));//货主id + 物料编码

        //                         创建收货通知单 主单 收货通知单 `receipt_notice`
        //                         需要新增保存的 收货通知单
        ReceiptNoticeForm receiptNoticeForm = new ReceiptNoticeForm();
        receiptNoticeForm.setWarehouseId(warehouseId);
        receiptNoticeForm.setWarehouse(warehouseName);
        Long owerId = owerIds.get(0);
        WmsOwerInfo wmsOwerInfo = wmsOwerInfoService.getById(owerId);
        if(ObjectUtil.isEmpty(wmsOwerInfo)){
            throw new IllegalArgumentException("货主不存在");
        }
        String owerName = wmsOwerInfo.getOwerName();
        receiptNoticeForm.setOwerId(owerId);
        receiptNoticeForm.setOwer(owerName);
        //默认：采购入库单-原材料仓	1
        receiptNoticeForm.setDocumentType("采购入库单-原材料仓");
        receiptNoticeForm.setDocumentTypeCode("1");
        //默认：3 ERP下发
        receiptNoticeForm.setOrderSourceCode(3);
        receiptNoticeForm.setOrderSource("ERP下发");
        //物料信息
        List<NoticeMaterialForm> noticeMaterialForms = new ArrayList<>();
        for (int n=0; n<receivingInfoList.size(); n++){
            ReceivingInfoForm receivingInfoForm = receivingInfoList.get(n);
            String materialKey = receivingInfoForm.getOwerId()+"_"+receivingInfoForm.getMaterialCode();
            WmsMaterialBasicInfo wmsMaterialBasicInfo = wmsMaterialBasicInfoMap.get(materialKey);
            if(ObjectUtil.isEmpty(wmsMaterialBasicInfo)){
                NoticeMaterialForm noticeMaterialForm = new NoticeMaterialForm();
                noticeMaterialForm
                        .setMaterialCode(wmsMaterialBasicInfo.getMaterialCode())
                        .setMaterialName(wmsMaterialBasicInfo.getMaterialName())
                        .setMaterialTypeId(wmsMaterialBasicInfo.getMaterialTypeId())
                        .setMaterialType("")
                        .setWeight(wmsMaterialBasicInfo.getWeight())
                        .setVolume(wmsMaterialBasicInfo.getVolume())
                        .setSpecification(receivingInfoForm.getSpecification())//规格型号
                        .setNum(receivingInfoForm.getNum())//数量
                        .setUnit("EA")//单位
                        .setExternalOrderNum("")//外部单号
                        .setExternalLineNum("")//外部单行号
                        .setBatchNum(receivingInfoForm.getBatchCode())//批次号
                        .setProductionDate(receivingInfoForm.getMaterialProductionDate().toLocalDate())//生产日期
                        .setColumnOne(receivingInfoForm.getCustomField1())//自定义字段1
                        .setColumnTwo(receivingInfoForm.getCustomField2())//自定义字段2
                        .setColumnThree(receivingInfoForm.getCustomField3())//自定义字段3
                ;
                noticeMaterialForms.add(noticeMaterialForm);
            }
        }
        receiptNoticeForm.setNoticeMaterialForms(noticeMaterialForms);
        /**
         * 创建收货通知单
         * @see IReceiptNoticeService#createOrder(ReceiptNoticeForm)
         */
        ReceiptNotice receiptNotice = receiptNoticeService.createOrder(receiptNoticeForm);//收货通知单
        Long receiptNoticeId = receiptNotice.getId();//收货通知单 主键Id
        //4.2.2、生成收货单
        //                         创建收货单 主单 收货单 `receipt`
        //                         需要新增保存的 收货单
        /**
         * 收货通知单  转收货   ->  收货单
         * @see com.jyd.bases.service.IReceiptNoticeService#transferReceipt(Long)
         */
        Receipt receipt = receiptNoticeService.transferReceipt(receiptNoticeId);
        Long receiptId = receipt.getId();//收货单Id

        //4.2.3、生成收货单对应的上架任务明细 -> 上架 -> 入库
        /**
         * 获取收货单详情信息
         * @see IReceiptService#getDetails(Long)
         */
        ReceiptVO receiptVO = receiptService.getDetails(receiptId);
        List<MaterialVO> materialVos = receiptVO.getMaterialForms();
        List<MaterialForm> materialForms = ConvertUtil.convertList(materialVos, MaterialForm.class);
        materialForms.forEach(materialForm -> {
            materialForm
                    .setActualNum(materialForm.getActualNum())//预期数量 -直接填入-> 实收数量
                    .setContainerNum(ContainerNumEnum.DEFAULT.getContainerCode())//容器号 默认：0000
            ;
        });
        ReceiptForm receiptForm = ConvertUtil.convert(receiptVO, ReceiptForm.class);
        receiptForm.setIsPutShelf(true);//是否直接上架
        receiptForm.setMaterialForms(materialForms);
        /**
         * 编辑 是否直接上架 -> 填写容器号 -> 保存
         * @See com.jyd.bases.service.IReceiptService#createOrder(com.jayud.model.bo.ReceiptForm)
         */
        receiptService.createOrder(receiptForm);//设置 直接上架，容器号 默认:0000

        /**
         * 确认收货 ->
         *     IsPutShelf true 直接上架 --> 生成 上架任务单 `shelf_order_task`
         *                                         shelfOrderTaskService.directGenerationShelfTask(details);
         * @see IReceiptService#receiptConfirmation(Long)
         */
        receiptService.receiptConfirmation(receiptId);

        //根据 收货单号 查询上架明细单
        String receiptNum = receipt.getReceiptNum();//收货单号
        QueryWrapper<ShelfOrderTask> shelfOrderTaskQueryWrapper = new QueryWrapper<>();
        shelfOrderTaskQueryWrapper.lambda().eq(ShelfOrderTask::getOrderId, receiptId);
        shelfOrderTaskQueryWrapper.lambda().eq(ShelfOrderTask::getOrderNum, receiptNum);
        shelfOrderTaskQueryWrapper.lambda().eq(ShelfOrderTask::getIsDeleted, 0);
        shelfOrderTaskQueryWrapper.lambda().groupBy(ShelfOrderTask::getOrderId, ShelfOrderTask::getOrderNum);
        List<ShelfOrderTask> shelfOrderTaskList = shelfOrderTaskService.list(shelfOrderTaskQueryWrapper);

        //货主id + 物料编码，确定上架库位
        Map<String, ReceivingInfoForm> receivingInfoFormMap = receivingInfoList.stream().collect(Collectors.toMap(r -> r.getOwerId() + "_" + r.getMaterialCode(), r -> r));

        List<ShelfOrderTask> shelfOrderTasks = new ArrayList<>();
        for(int m=0; m<shelfOrderTaskList.size(); m++){
            ShelfOrderTask shelfOrderTask = shelfOrderTaskList.get(m);
            String materialCode = shelfOrderTask.getMaterialCode();
            //货主id + 物料编码
            String taskKey = owerId+"_"+materialCode;
            ReceivingInfoForm receivingInfoForm = receivingInfoFormMap.get(taskKey);
            if(ObjectUtil.isEmpty(receivingInfoForm)){
                Long warehouseLocationId = receivingInfoForm.getWarehouseLocationId();//库位id
                String warehouseLocationCode = receivingInfoForm.getWarehouseLocationCode();//库位编号
                shelfOrderTask.setActualNum(shelfOrderTask.getNum());// 实际上架数量  直接取  待上架数量
                shelfOrderTask.setActualShelfSpace(warehouseLocationCode);//实际上架库位（库位基础数据编号）  取值  扫描的库位编号
                shelfOrderTasks.add(shelfOrderTask);
            }
        }
        /**
         * 强制上架    -->    入库    -->  直接入库
         *                     this.assembleInventoryDetail(e)
         *                  inventoryDetailService.input(inventoryDetailForms);
         * @see IShelfOrderTaskService#forcedShelf(List)
         */
        shelfOrderTaskService.forcedShelf(shelfOrderTasks);

    }

    /**
     * 验证 <br>
     * 工作台收货上架:工作台货架回库
     * @param form
     */
    private void verificationPutawayBackLibrary(PutawayBackLibraryForm form) {
        Long warehouseId = form.getWarehouseId();
        if(ObjectUtil.isEmpty(warehouseId)){
            throw new IllegalArgumentException("仓库ID，不能为空");
        }
        String warehouseCode = form.getWarehouseCode();
        if(StrUtil.isEmpty(warehouseCode)){
            throw new IllegalArgumentException("仓库编号，不能为空");
        }
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        if(ObjectUtil.isEmpty(warehouse)){
            throw new IllegalArgumentException("仓库，不存在");
        }
        String workbenchCode = form.getWorkbenchCode();
        if(StrUtil.isEmpty(workbenchCode)){
            throw new IllegalArgumentException("工作台编号，不能为空");
        }
        QueryWrapper<Workbench> workbenchQueryWrapper = new QueryWrapper<>();
        workbenchQueryWrapper.lambda().eq(Workbench::getCode, workbenchCode);
        workbenchQueryWrapper.lambda().eq(Workbench::getIsDeleted, 0);
        workbenchQueryWrapper.lambda().groupBy(Workbench::getCode);
        Workbench workbench = workbenchMapper.selectOne(workbenchQueryWrapper);
        if(ObjectUtil.isEmpty(workbench)){
            throw new IllegalArgumentException("工作台，不存在");
        }
        Long workbench_warehouseId = workbench.getWarehouseId();
        if(!warehouseId.equals(workbench_warehouseId)){
            throw new IllegalArgumentException("所选工作台的所属仓库，与当前默认仓库不一致");
        }

        String shelfCode = form.getShelfCode();
        QueryWrapper<WarehouseShelf> warehouseShelfQueryWrapper = new QueryWrapper<>();
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getCode, shelfCode);
        warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getIsDeleted, 0);
        warehouseShelfQueryWrapper.lambda().groupBy(WarehouseShelf::getCode);
        WarehouseShelf warehouseShelf = warehouseShelfMapper.selectOne(warehouseShelfQueryWrapper);
        if(ObjectUtil.isEmpty(warehouseShelf)){
            throw new IllegalArgumentException("货架信息，不存在");
        }
        Long warehouseShelf_warehouseId = warehouseShelf.getWarehouseId();
        if(!warehouseId.equals(warehouseShelf_warehouseId)){
            throw new IllegalArgumentException("所选货架的所属仓库，与当前默认仓库不一致");
        }
        List<ReceivingInfoForm> receivingInfoList = form.getReceivingInfoList();
        if(CollUtil.isEmpty(receivingInfoList)){
            throw new IllegalArgumentException("收货信息，不能为空");
        }
        List<Long> collect1 = receivingInfoList.stream().map(k -> k.getOwerId()).collect(Collectors.toList());
        long count1 = collect1.stream().distinct().count();
        if (collect1.size() != count1) {
            throw new IllegalArgumentException("不能有多个货主，重复");
        }

        List<String> collect2 = receivingInfoList.stream().map(k -> k.getOwerId()+"_"+k.getMaterialCode()).collect(Collectors.toList());
        long count2 = collect2.stream().distinct().count();
        if(collect2.size() != count2){
            throw new IllegalArgumentException("同一货主下，不能有相同的物料");
        }
    }


}
