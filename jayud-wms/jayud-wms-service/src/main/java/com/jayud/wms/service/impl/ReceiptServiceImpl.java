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
import com.jayud.common.dto.QueryClientQualityMaterialForm;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.mapper.ReceiptMapper;
import com.jayud.wms.model.bo.*;
import com.jayud.wms.model.enums.InboundOrderProStatusEnum;
import com.jayud.wms.model.enums.MaterialStatusEnum;
import com.jayud.wms.model.enums.ReceiptNoticeStatusEnum;
import com.jayud.wms.model.enums.ReceiptStatusEnum;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.MaterialSnVO;
import com.jayud.wms.model.vo.MaterialVO;
import com.jayud.wms.model.vo.ReceiptVO;
import com.jayud.wms.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 收货单 服务实现类
 *
 * @author jyd
 * @since 2021-12-20
 */
@Service
public class ReceiptServiceImpl extends ServiceImpl<ReceiptMapper, Receipt> implements IReceiptService {


    @Autowired
    private ReceiptMapper receiptMapper;
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private IMaterialSnService materialSnService;
    @Autowired
    private IReceiptNoticeService receiptNoticeService;
    @Autowired
    private IOrderTrackService orderTrackService;
    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;
    @Autowired
    private IOrderProcessService orderProcessService;
    @Autowired
    private IQualityInspectionService qualityInspectionService;
    @Autowired
    private IReceiptService receiptService;
    @Autowired
    private IIncomingSeedingService incomingSeedingService;
    @Autowired
    private IShelfOrderService shelfOrderService;
    @Autowired
    private IShelfOrderTaskService shelfOrderTaskService;
    @Autowired
    private IWmsMaterialPackingSpecsService wmsMaterialPackingSpecsService;
    @Autowired
    private IQualityInspectionMaterialService qualityInspectionMaterialService;

    @Autowired
    private IContainerService containerService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    public IWmsReceiptAppendService wmsReceiptAppendService;


    @Override
    public IPage<ReceiptVO> selectPage(QueryReceiptForm queryReceiptForm,
                                       Integer pageNo,
                                       Integer pageSize,
                                       HttpServletRequest req) {

        Page<QueryReceiptForm> page = new Page<QueryReceiptForm>(pageNo, pageSize);
        IPage<ReceiptVO> pageList = receiptMapper.pageList(page, queryReceiptForm);
        return pageList;
    }

    @Override
    public IPage<ReceiptVO> selectPageFeign(QueryReceiptForm queryReceiptForm, Integer pageNo, Integer pageSize, HttpServletRequest req) {
        Page<QueryReceiptForm> page = new Page<QueryReceiptForm>(pageNo, pageSize);
        IPage<ReceiptVO> pageList = receiptMapper.pageListFeign(page, queryReceiptForm);
        return pageList;
    }

    @Override
    public List<Receipt> selectList(Receipt receipt) {
        return receiptMapper.list(receipt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Receipt saveOrUpdateReceipt(Receipt receipt) {
        Long id = receipt.getId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            receipt.setCreateBy(CurrentUserUtil.getUsername());
            receipt.setCreateTime(new Date());

            QueryWrapper<Receipt> receiptQueryWrapper = new QueryWrapper<>();
//            receiptQueryWrapper.lambda().eq(Receipt::getCode, receipt.getCode());
            receiptQueryWrapper.lambda().eq(Receipt::getIsDeleted, 0);
            List<Receipt> list = this.list(receiptQueryWrapper);
            if (CollUtil.isNotEmpty(list)) {
                throw new IllegalArgumentException("编号已存在，操作失败");
            }

        } else {
            //修改 --> update 更新人、更新时间
            receipt.setUpdateBy(CurrentUserUtil.getUsername());
            receipt.setUpdateTime(new Date());

            QueryWrapper<Receipt> receiptQueryWrapper = new QueryWrapper<>();
            receiptQueryWrapper.lambda().ne(Receipt::getId, id);
//            receiptQueryWrapper.lambda().eq(Receipt::getCode, receipt.getCode());
            receiptQueryWrapper.lambda().eq(Receipt::getIsDeleted, 0);
            List<Receipt> list = this.list(receiptQueryWrapper);
            if (CollUtil.isNotEmpty(list)) {
                throw new IllegalArgumentException("编号已存在，操作失败");
            }
        }
        this.saveOrUpdate(receipt);
        return receipt;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delReceipt(Long id) {

        Receipt receipt = new Receipt();
        //逻辑删除 -->update 修改人、修改时间、是否删除
        receipt.setId(id);
        receipt.setUpdateBy(CurrentUserUtil.getUsername());
        receipt.setUpdateTime(new Date());
        receipt.setIsDeleted(true);
        this.updateById(receipt);
        //逻辑删除
        //删除收货单物料信息表
        List<Material> byCondition = materialService.getByCondition(new Material().setOrderId(id).setIsDeleted(false));
        //删除收货单物料信息表
        byCondition.stream().forEach(v -> {
            Material material = new Material();
            material.setId(v.getId());
            material.setUpdateBy(CurrentUserUtil.getUsername());
            material.setUpdateTime(new Date());
            material.setIsDeleted(true);
            materialService.updateById(material);
        });
        //查询收货单物料sn
        List<MaterialSn> byCondition1 = materialSnService.getByCondition(new MaterialSn().setOrderId(id).setIsDeleted(false));
        //删除收货单物料sn
        byCondition1.stream().forEach(v -> {
            MaterialSn materialSn = new MaterialSn();
            materialSn.setId(v.getId());
            materialSn.setUpdateBy(CurrentUserUtil.getUsername());
            materialSn.setUpdateTime(new Date());
            materialSn.setIsDeleted(true);
            materialSnService.updateById(materialSn);
        });
        return true;
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryReceiptForExcel(QueryReceiptForm queryReceiptForm,
                                                                    HttpServletRequest req) {
        return this.baseMapper.queryReceiptForExcel(queryReceiptForm);
    }

    @Override
    public void cancel(Long id) {
        //更改收货单状态为整单撤销
        Date date = new Date();
        Receipt oldReceipt = this.getById(id);
        //TODO 校验哪些状态能撤销操作
        if (!StringUtils.isEmpty(oldReceipt.getReceiver())
                || oldReceipt.getStatus().equals(ReceiptStatusEnum.FOUR.getCode())) {
            throw new ServiceException("该状态无法操作");
        }

        Receipt receipt = new Receipt();
        receipt.setStatus(ReceiptStatusEnum.FOUR.getCode()).setId(oldReceipt.getId()).setUpdateTime(date).setUpdateBy(CurrentUserUtil.getUsername());
        this.updateById(receipt);
        //把所有物料信息和物料sn信息更改成删除状态
//        this.materialService.updateByCondition(new Material().setIsDeleted(true), new Material().setOrderId(oldReceipt.getId()));
//        this.materialSnService.updateByCondition(new MaterialSn().setIsDeleted(true), new MaterialSn().setOrderId(oldReceipt.getId()));
        //根据收货通知单号更改状态为创建状态
        this.receiptNoticeService.updateByCondition(new ReceiptNotice().setStatus(ReceiptNoticeStatusEnum.CREATE.getCode()), new ReceiptNotice().setReceiptNoticeNum(oldReceipt.getReceiptNoticeNum()));
        //添加流程记录
        OrderTrack orderTrack = new OrderTrack().setOrderNo(oldReceipt.getReceiptNoticeNum()).setSubOrderNo(oldReceipt.getReceiptNum()).setType(1)
                .setStatus(ReceiptStatusEnum.FOUR.getCode() + "").setStatusName(ReceiptStatusEnum.FOUR.getDesc());
        orderTrack.setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
        this.orderTrackService.save(orderTrack);
    }

    @Override
    public ReceiptVO getDetails(Long id) {
        //获取收货通知单
        Receipt receipt = this.getById(id);
        ReceiptVO receiptVO = ConvertUtil.convert(receipt, ReceiptVO.class);
        //获取物理信息
        List<Material> materials = this.materialService.getByCondition(new Material().setOrderId(receiptVO.getId()).setIsDeleted(false));
        List<MaterialVO> materialList = ConvertUtil.convertList(materials, MaterialVO.class);
//        //获取物料sn信息
//        List<MaterialSn> materialSns = this.materialSnService.getByCondition(new MaterialSn().setOrderId(receiptVO.getId()).setIsDeleted(false));
//        List<MaterialSnVO> materialSnList = ConvertUtil.convertList(materialSns, MaterialSnVO.class);

        receiptVO.setMaterialForms(materialList);
        receiptVO.setWmsReceiptAppendForms(null);
//        receiptVO.setMaterialSnForms(materialSnList);




        return receiptVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(ReceiptForm form) {

        //校验容器号是否存在

        form.calculateTotalQuantity();

        //物料超收策略
//        List<String> materialCodes = form.getMaterialForms().stream().map(e -> e.getMaterialCode()).collect(Collectors.toList());
//        Map<String, BigDecimal> overchargePolicyMap = this.wmsMaterialBasicInfoService.getOverchargePolicy(materialCodes);
//        form.checkOverchargePolicy(overchargePolicyMap);
//        form.calculationStatus();
        Receipt receipt = ConvertUtil.convert(form, Receipt.class);
        Date date = new Date();
        receipt.setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(date);

        this.updateById(receipt);
        List<MaterialForm> materialForms = form.getMaterialForms();
        List<Material> materials = new ArrayList<>();
        for (MaterialForm materialForm : materialForms) {
            materialForm.calculationStatus();
            Material material = ConvertUtil.convert(materialForm, Material.class).setStatus(materialForm.getStatus());
            material.setOrderNum(receipt.getReceiptNum()).setOrderId(receipt.getId());
            if (materialForm.getId() == null) {
//                if (material.getStatus().equals(MaterialStatusEnum.FOUR.getCode())) continue;
                material.setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
            } else {
                material.setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(date);
            }
            materials.add(material);
        }
        this.materialService.saveOrUpdateBatch(materials);


        //收货单附加表
        List<WmsReceiptAppendForm> wmsReceiptAppendForms = form.getWmsReceiptAppendForms();
        List<WmsReceiptAppend> wmsReceiptAppends = new ArrayList<>();

        for (WmsReceiptAppendForm wmsReceiptAppendForm : wmsReceiptAppendForms) {

            WmsReceiptAppend convert = ConvertUtil.convert(wmsReceiptAppendForm, WmsReceiptAppend.class);

            if (convert.getId() == null) {
                convert.setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(date);
            } else {
                convert.setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(date);
            }
            wmsReceiptAppends.add(convert);
        }
        wmsReceiptAppendService.saveOrUpdateBatch(wmsReceiptAppends);
    }

    /**
     * 收货确认
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiptConfirmation(Long id) {
        //获取详情数据
        ReceiptVO details = this.getDetails(id);
        details.setMaterialForms(details.getMaterialForms().stream().filter(e -> !MaterialStatusEnum.FOUR.getCode().equals(e.getStatus())).collect(Collectors.toList()));
        details.calculationStatus();
        if (details.getStatus().equals(ReceiptStatusEnum.ONE.getCode())) {
            throw new ServiceException("该阶段无法转收货单");
        }
        if (!StringUtils.isEmpty(details.getReceiver())) {
            throw new ServiceException("该订单已经进行收货操作");
        }
        String status = this.orderProcessService.getNextNode(details.getReceiptNoticeNum(), details.getProcessFlag());
        if (StringUtils.isEmpty(status)) {
            throw new ServiceException("不存在该流程节点");
        }
        if (details.getIsPutShelf()) {
            //直接生成上架任务
            this.shelfOrderTaskService.directGenerationShelfTask(details);
        } else {
            switch (InboundOrderProStatusEnum.getEnum(Integer.valueOf(status))) {
                case THREE://质检
                    this.qualityInspectionService.generateQualityInspection(details);
                    break;
                case FOUR://入库播种
                    this.incomingSeedingService.doWarehousingSeeding(details);
                    break;
                case FIVE://生成待上架单
                    this.shelfOrderService.generateShelfOrder(details);
                    break;
            }
        }

        //收货单增加收货人和收货时间
        Receipt receipt = new Receipt().setStatus(details.getStatus()).setReceivingTime(LocalDate.now()).setReceiver(CurrentUserUtil.getUsername());
        receipt.setId(details.getId());
        this.receiptService.updateById(receipt);
        OrderTrack orderTrack = new OrderTrack().setOrderNo(details.getReceiptNoticeNum()).setSubOrderNo(details.getReceiptNum()).setType(1)
                .setStatus(details.getStatus() + "").setStatusName(ReceiptStatusEnum.getDesc(details.getStatus()));
        orderTrack.setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(new Date());
        this.orderTrackService.save(orderTrack);
    }

    @Override
    public List<Receipt> getByReceiptNums(List<String> orderNums) {
        QueryWrapper<Receipt> condition = new QueryWrapper<>();
        condition.lambda().in(Receipt::getReceiptNum, orderNums);
        return this.baseMapper.selectList(condition);
    }

    //app物料超收比例计算
    @Override
    public void appMaterialFigureUp(QueryClientQualityMaterialForm form) {
        //实收数量总和
//        Double totalNum;
        List<Material> materials = materialService.getByCondition(
                new Material()
                        .setOrderId(form.getReceiptId())
                        .setOrderNum(form.getOrderNum())
                        .setMaterialCode(form.getMaterialCode())
                        .setIsDeleted(false));

        //拿到这个列表实际收货量
        List<Double> collect = materials.stream().map(Material::getActualNum).collect(Collectors.toList());
        Optional<Double> reduce = collect.stream().reduce(Double::sum);
        //实际收货量总计
        Double aDouble = StringUtils.doubleAddTogether(reduce.get(), form.getActualNum());

        //计算预计收货量
        //根据物料单号查询某条物料信息去复制
        Material material = new Material();
        material.setOrderNum(form.getOrderNum());//收货单号
        material.setMaterialCode(form.getMaterialCode());//物料编号
        //当前查询接口为 当前物料 数量不为0的
        List<Material> materialOne = materialService.findMaterialOne(material);
        //预计收货量
        Double num = materialOne.get(0).getNum();


        //查询 某个物料是否有超收比例

        QueryWrapper<WmsMaterialBasicInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(WmsMaterialBasicInfo::getMaterialCode, form.getMaterialCode());

        WmsMaterialBasicInfo one = wmsMaterialBasicInfoService.getOne(condition);

        // 1为true  1是   false  0否
        //判断是否有 超收比例
        if (one.getIsAllowOvercharge() == true) {
            //预期数量 X（1+超收比例）=最大实收数量
            // 最大实收数量
            BigDecimal multiply = (one.getOverchargeRatio().divide(new BigDecimal(100)).add(new BigDecimal(1))).multiply(new BigDecimal(num));
            //实际收货总量
//            BigDecimal bigDecimal = new BigDecimal(aDouble);  //此方式会丢失精度
            BigDecimal bigDecimal = BigDecimal.valueOf(aDouble); // 不会丢失精度
            if (bigDecimal.compareTo(multiply) > 0) {
                throw new ServiceException("此物料实收超过超收比例！");
            }
        }
        if (one.getIsAllowOvercharge() == false) {
            //预计实收数量
            BigDecimal nums = new BigDecimal(num);
            //实际收货总量
            BigDecimal bigDecimal = new BigDecimal(aDouble);
            if (bigDecimal.compareTo(nums) > 0) {
                throw new ServiceException("此物料实收超过预期数量！");
            }
        }
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public BaseResult createRecordCopyMaterial(QueryClientQualityMaterialForm form) {

        //收货单物料校验容器重复
        QueryWrapper<Material> condition = new QueryWrapper<>();
        condition.lambda().eq(Material::getOrderNum, form.getOrderNum())
                .eq(Material::getMaterialCode, form.getMaterialCode())
                .notIn(Material::getStatus, 4)      // 过滤撤销了的 4:撤销
                .eq(Material::getContainerNum, form.getContainerNum());

        Material materialOnes = materialService.getOne(condition);
        if (materialOnes != null) {
            return BaseResult.error(SysTips.THE_SAME_MATERIAL_CANNOT_SAME_CONTAINER);
        }


        // 添加根据物料编号超收比例计算超收 数量
        //订单id 和收货订单编号和物料编号  查询订单 列表
        appMaterialFigureUp(form);

        //容器号校验是否可以用
        QueryWrapper<Container> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Container::getCode, form.getContainerNum())
                .eq(Container::getWarehouseId, form.getWarehouseId())
                .eq(Container::getStatus, true)  // true 启用
                .eq(Container::getIsDeleted, false);  //  false 0 否  是否删除
        Container one1 = containerService.getOne(queryWrapper);
        if (one1 == null) {
            return BaseResult.error(SysTips.CONTAINER_DOES_NOT_EXIST);
        }

        //根据物料单号查询某条物料信息去复制
        Material material = new Material();
        material.setOrderNum(form.getOrderNum());//收货单号
        material.setMaterialCode(form.getMaterialCode());//物料编号
        //当前查询接口为 当前物料 数量不为0的
        List<Material> materialOne = materialService.findMaterialOne(material);
        if (materialOne.size() == 0) {
            return BaseResult.error(SysTips.INEXISTENCE_MATERIALS_INFORMATION);
        }
        System.out.println(materialOne.get(0).getNum());
        if (materialOne.get(0).getContainerNum() != null && !"".equals(materialOne.get(0).getContainerNum())) {
            // 第二次进来

            Material material1 = materialOne.get(0);
            material1.setContainerNum(form.getContainerNum());//容器号
            material1.setActualNum(form.getActualNum());//实收数量
            material1.setDescription(StringUtils.getFileStringList(form.getFileList()));    //文件字段
            material1.setStatus(MaterialStatusEnum.TWO.getCode());// 收货物料的状态

            MaterialForm materialForm = ConvertUtil.convert(material1, MaterialForm.class);
            this.materialService.establishMaterial(materialForm);
            //更新 订单
            updateReceipt(form.getReceiptId());
            //更新物料记录表 状态  根据超收比例 一起更新
            //订单号   收货单id     物料编号
            updateMaterial(form);
            return BaseResult.ok();
        } else {
            //第一次进来
            // 修改 物料单号关联的物料信息 修改
            Material material1 = new Material();
            material1.setId(materialOne.get(0).getId()); //物料id
            material1.setActualNum(form.getActualNum());//实数数量
            material1.setContainerNum(form.getContainerNum());//容器号
            material1.setDescription(StringUtils.getFileStringList(form.getFileList()));      //文件字段
            material1.setStatus(MaterialStatusEnum.TWO.getCode());// 收货物料的状态
            materialService.updateById(material1);
            //更新 订单
            updateReceipt(form.getReceiptId());
            updateMaterial(form);  //修改订单状态
            return BaseResult.ok();
        }

    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public BaseResult createRecordCopyMaterialSN(QueryClientQualityMaterialForm form) {


        //需要一个根据SN序列号 去查询物料编号  根据收货单号和 物料编号查询  物料信息
        //看是不是第一条  第一条是修改
        QualityMaterialForm qualityMaterialForm = new QualityMaterialForm();
        qualityMaterialForm.setSerialNum(form.getSerialNum());//物料序列号
        qualityMaterialForm.setOrderNum(form.getOrderNum());//收货单号
        List<Material> materialSNOne = materialService.findMaterialSNOne(qualityMaterialForm);

        if (materialSNOne.size() == 0) {
            return BaseResult.error(SysTips.SN_NUMBER_INEXISTENCE_MATERIALS_INFORMATION);
        }


        QueryWrapper<Material> condition = new QueryWrapper<>();
        condition.lambda().eq(Material::getOrderNum, form.getOrderNum())
                .eq(Material::getMaterialCode, materialSNOne.get(0).getMaterialCode())
                .notIn(Material::getStatus, 4)      // 过滤撤销了的 4:撤销
                .eq(Material::getContainerNum, form.getContainerNum());
        Material one = materialService.getOne(condition);
        if (one != null) {
            return BaseResult.error(SysTips.THE_SAME_MATERIAL_CANNOT_SAME_CONTAINER);
        }

        //容器号校验是否可以用
        QueryWrapper<Container> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Container::getCode, form.getContainerNum())
                .eq(Container::getWarehouseId, form.getWarehouseId())
                .eq(Container::getStatus, true)  // true 启用
                .eq(Container::getIsDeleted, false);  //  false 0 否  是否删除
        Container one1 = containerService.getOne(queryWrapper);
        if (one1 == null) {
            return BaseResult.error(SysTips.CONTAINER_DOES_NOT_EXIST);
        }


        // 添加根据物料编号超收比例计算超收 数量
        form.setMaterialCode(materialSNOne.get(0).getMaterialCode());
        appMaterialFigureUp(form);

        if (materialSNOne.get(0).getContainerNum() != null && !materialSNOne.get(0).getContainerNum().equals("")) {

            // 第二次进来
            //根据物料单号查询某条物料信息去复制
            Material material = new Material();
            material.setOrderNum(form.getOrderNum());//收货单号
            material.setMaterialCode(materialSNOne.get(0).getMaterialCode());//物料编号
            //当前查询接口为 当前物料 数量不为0的
            List<Material> materialOne = materialService.findMaterialOne(material);


            materialOne.get(0).setContainerNum(form.getContainerNum());//容器号
            materialOne.get(0).setActualNum(form.getActualNum());//实收数量
            materialOne.get(0).setDescription(StringUtils.getFileStringList(form.getFileList()));    // 文件字段
            materialOne.get(0).setStatus(MaterialStatusEnum.TWO.getCode());// 收货物料的状态  2收货中


            MaterialForm materialForm = ConvertUtil.convert(materialOne.get(0), MaterialForm.class);

            this.materialService.establishMaterial(materialForm);
            //更新 订单
            updateReceipt(form.getReceiptId());
            updateMaterial(form);
            return BaseResult.ok();

        } else {
            //第一次进来
            // 修改 物料单号关联的物料信息 修改
            Material material1 = new Material();
            material1.setId(materialSNOne.get(0).getId()); //物料id
            material1.setActualNum(form.getActualNum());//实数数量
            material1.setContainerNum(form.getContainerNum());//容器号
            material1.setDescription(StringUtils.getFileStringList(form.getFileList())); //文件字段
            material1.setStatus(MaterialStatusEnum.TWO.getCode());// 收货物料的状态  2收货中

            materialService.updateById(material1);
            //更新 订单
            updateReceipt(form.getReceiptId());
            updateMaterial(form);
            return BaseResult.ok();
        }
    }


    //更新订单数据
    public void updateReceipt(Long id) {

//        ReceiptVO receiptVO = receiptService.getDetails(id);
        ReceiptVO receiptVO = getDetails(id);
        ReceiptForm convert = ConvertUtil.convert(receiptVO, ReceiptForm.class);
        convert.calculateTotalQuantity();

        Receipt receipt = ConvertUtil.convert(convert, Receipt.class);
        Date date = new Date();
        receipt.setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(date);

        receiptService.updateById(receipt);
    }

    // 修改物料的 收货状态
    @Override
    public void updateMaterial(QueryClientQualityMaterialForm form) {

        // 拿到当前的 数量总和
        //实收数量总和
        List<Material> materials = materialService.getByCondition(
                new Material()
                        .setOrderId(form.getReceiptId())
                        .setOrderNum(form.getOrderNum())
                        .setMaterialCode(form.getMaterialCode())
                        .setIsDeleted(false));

        //拿到这个列表实际收货量
        List<Double> collect = materials.stream().map(Material::getActualNum).collect(Collectors.toList());
        Optional<Double> reduce = collect.stream().reduce(Double::sum);
        //实际收货量总计
        Double aDouble = reduce.get();


        // 预期数量
        //拿到这个列表实际收货量
        List<Double> collectNum = materials.stream().map(Material::getNum).collect(Collectors.toList());
        Optional<Double> reduceNum = collectNum.stream().reduce(Double::sum);
        //实际预收货数量
        Double num = reduceNum.get();


        //查询 某个物料是否有超收比例
        QueryWrapper<WmsMaterialBasicInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(WmsMaterialBasicInfo::getMaterialCode, form.getMaterialCode());
        WmsMaterialBasicInfo one = wmsMaterialBasicInfoService.getOne(condition);

        // 1为true  1是   false  0否
        //判断是否有 超收比例
        if (one.getIsAllowOvercharge() == true) {
            //预期数量 X（1+超收比例）=最大实收数量
            // 最大实收数量
            BigDecimal multiply = (one.getOverchargeRatio().divide(new BigDecimal(100)).add(new BigDecimal(1))).multiply(new BigDecimal(num));
            //实际收货总量
//            BigDecimal bigDecimal = new BigDecimal(aDouble);
            BigDecimal bigDecimal = BigDecimal.valueOf(aDouble);
            //小于收货数量
            if (bigDecimal.compareTo(multiply) == -1) {

                Material material = new Material();
                material.setStatus(2);
                material.setOrderId(form.getReceiptId());
                material.setOrderNum(form.getOrderNum());
                material.setMaterialCode(form.getMaterialCode());
                materialService.updateAllMaterialList(material);
                System.out.println("小于 status   2 收货中  ");
                log.warn("小于 status   2 收货中  ");

            }
            //等于收货数量 收货完成
            if (bigDecimal.compareTo(multiply) == 0) {
                Material material = new Material();
                material.setStatus(3);
                material.setOrderId(form.getReceiptId());
                material.setOrderNum(form.getOrderNum());
                material.setMaterialCode(form.getMaterialCode());
                materialService.updateAllMaterialList(material);
                System.out.println("小于 status   3 已收货  ");
                log.warn("小于 status   3 已收货  ");
            }
        }
        if (one.getIsAllowOvercharge() == false) {
            //预计实收数量
            BigDecimal multiply = new BigDecimal(num);
            //实际收货总量
            BigDecimal bigDecimal = new BigDecimal(aDouble);
            //小于收货量
            if (bigDecimal.compareTo(multiply) == -1) {
                Material material = new Material();
                material.setStatus(2);
                material.setOrderId(form.getReceiptId());
                material.setOrderNum(form.getOrderNum());
                material.setMaterialCode(form.getMaterialCode());
                materialService.updateAllMaterialList(material);
                System.out.println("小于 status   2 收货中  ");
                log.warn("小于 status   2 收货中 ");
            }
            //等于收货数量 收货完成
            if (bigDecimal.compareTo(multiply) == 0) {
                Material material = new Material();
                material.setStatus(3);
                material.setOrderId(form.getReceiptId());
                material.setOrderNum(form.getOrderNum());
                material.setMaterialCode(form.getMaterialCode());
                materialService.updateAllMaterialList(material);
                System.out.println("小于 status   3 已收货  ");
                log.warn("小于 status   3 已收货  ");
            }
        }
    }

    @Override
    public BaseResult convertQualit(DeleteForm deleteForm) {
        if (CollUtil.isEmpty(deleteForm.getIds())){
            return BaseResult.error(SysTips.IDS_ISEMPTY);
        }
        //查询出数据
        List<String> receiptNumberList = deleteForm.getNumberList();
        LambdaQueryWrapper<Receipt> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Receipt::getIsDeleted,false);
        lambdaQueryWrapper.in(Receipt::getReceiptNum,receiptNumberList);
        List<Receipt> receiptList = this.list(lambdaQueryWrapper);
        List<String> errList = new ArrayList<>();
        List<Receipt> successList = new ArrayList<>();
        if (CollUtil.isNotEmpty(receiptList)){
            //查询已生成质检
            LambdaQueryWrapper<QualityInspection> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(QualityInspection::getIsDeleted,false);
            queryWrapper.in(QualityInspection::getReceiptNum,receiptNumberList);
            List<QualityInspection> qualityInspectionList = qualityInspectionService.list(queryWrapper);
            receiptNumberList = qualityInspectionList.stream().map(x->x.getReceiptNum()).distinct().collect(Collectors.toList());
            List<String> finalReceiptNumberList = receiptNumberList;
            //过滤出未生成质检
            receiptList.forEach(receipt -> {
                if (finalReceiptNumberList.contains(receipt.getReceiptNum())){
                    errList.add(receipt.getReceiptNum());
                }else {
                    successList.add(receipt);
                }
            });
        }
        if (CollUtil.isNotEmpty(successList)){
            changeToQuality(receiptList);
        }

        if (CollUtil.isNotEmpty(errList)){
            return BaseResult.error(org.apache.commons.lang3.StringUtils.join(errList,StrUtil.COMMA)+" 已转为质检单请勿重复提交！");
        }
        return BaseResult.ok(SysTips.SUCCESS_MSG);
    }

    /**
     * @description 转换数据
     * @author  ciro
     * @date   2022/4/1 13:28
     * @param: receiptList
     * @return: void
     **/
    private void changeToQuality(List<Receipt> receiptList){
        List<QualityInspectionMaterial> materialList = new ArrayList<>();
        for (Receipt receipt : receiptList){
            QualityInspection qualityInspection = new QualityInspection();
            BeanUtils.copyProperties(receipt,qualityInspection);
            BaseResult  baseResult = authClient.getOrderFeign("quality_inspection", new Date());
            HashMap data = (HashMap)baseResult.getResult();
            qualityInspection.setQcNo(data.get("order").toString());
            receipt.setQcNo(data.get("order").toString());
            qualityInspection.setId(null);
            qualityInspection.setUpdateBy("");
            qualityInspection.setUpdateTime(null);
            qualityInspectionService.save(qualityInspection);
            LambdaQueryWrapper<Material> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Material::getIsDeleted,false);
            lambdaQueryWrapper.eq(Material::getOrderId,receipt.getId());
            List<Material> materials = materialService.list(lambdaQueryWrapper);
            for (Material material : materials){
                QualityInspectionMaterial qualityInspectionMaterial = new QualityInspectionMaterial();
                BeanUtils.copyProperties(material,qualityInspectionMaterial);
                qualityInspectionMaterial.setQualityInspectionId(qualityInspection.getId());
                qualityInspectionMaterial.setId(null);
                qualityInspectionMaterial.setUpdateBy("");
                qualityInspectionMaterial.setUpdateTime(null);
                materialList.add(qualityInspectionMaterial);
            }
        }
        if (CollUtil.isNotEmpty(materialList)){
            qualityInspectionMaterialService.saveBatch(materialList);
        }
        this.updateBatchById(receiptList);
    }
}
