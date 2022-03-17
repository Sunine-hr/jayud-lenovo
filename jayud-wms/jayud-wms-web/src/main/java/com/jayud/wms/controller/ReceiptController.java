package com.jayud.wms.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.QueryClientQualityMaterialForm;
import com.jayud.common.dto.QueryClientReceiptForm;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.MaterialForm;
import com.jayud.wms.model.bo.QueryReceiptForm;
import com.jayud.wms.model.bo.ReceiptForm;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.ReceiptVO;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.wms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 收货单 控制类
 *
 * @author jyd
 * @since 2021-12-20
 */
@Api(tags = "收货单")
@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IReceiptService receiptService;
    @Autowired
    private AuthService authService;
    @Autowired
    private IMaterialService materialService;

    @Autowired
    private IMaterialSnService materialSnService;
    @Autowired
    private IContainerService containerService;

    @Autowired
    public IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;

    /**
     * 分页查询数据
     *
     * @param queryReceiptForm 查询条件
     * @return
     */
    @SysDataPermission(clazz = QueryReceiptForm.class)
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<IPage<ReceiptVO>> selectPage(@RequestBody QueryReceiptForm queryReceiptForm, HttpServletRequest req) {
        IPage<ReceiptVO> receiptVOIPage = receiptService.selectPage(queryReceiptForm, queryReceiptForm.getCurrentPage(), queryReceiptForm.getPageSize(), req);
        return BaseResult.ok(receiptVOIPage);
    }


    /**
     * 外部调用 分页查询数据
     *
     * @param queryClientReceiptForm 查询条件
     * @return
     */
    @SysDataPermission(clazz = QueryClientReceiptForm.class)
    @ApiOperation("外部调用分页查询数据")
    @PostMapping(value = "/client/selectPage")
    public BaseResult selectPageFeign(@RequestBody QueryClientReceiptForm queryClientReceiptForm) {
        HttpServletRequest req = null;
        QueryReceiptForm queryReceiptForm = ConvertUtil.convert(queryClientReceiptForm, QueryReceiptForm.class);
        IPage<ReceiptVO> receiptVOIPage = receiptService.selectPageFeign(queryReceiptForm, queryReceiptForm.getCurrentPage(), queryReceiptForm.getPageSize(), req);
        return BaseResult.ok(receiptVOIPage);
    }

    /**
     * 列表查询数据
     *
     * @param receipt 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<Receipt>> selectList(Receipt receipt,
                                                HttpServletRequest req) {
        return BaseResult.ok(receiptService.selectList(receipt));
    }

    /**
     * 新增
     *
     * @param receipt
     **/
//    @ApiOperation("新增")
//    @PostMapping("/add")
//    public BaseResult add(@Valid @RequestBody Receipt receipt) {
//        receiptService.save(receipt);
//        return BaseResult.ok(SysTips.ADD_SUCCESS);
//    }

    /**
     * 编辑
     *
     * @param receipt
     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody Receipt receipt) {
//        receiptService.updateById(receipt);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }

//    /**
//     * 保存(新增+编辑)
//     *
//     * @param receipt
//     **/
//    @ApiOperation("保存(新增+编辑)")
//    @PostMapping("/save")
//    public BaseResult save(@Valid @RequestBody Receipt receipt) {
//        Receipt receipt1 = receiptService.saveOrUpdateReceipt(receipt);
//        return BaseResult.ok(receipt1);
//    }

    /**
     * 物理删除
     *
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id) {
        receiptService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 逻辑删除
     *
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam(name = "id") Long id) {
        boolean b = receiptService.delReceipt(id);
        if (b) {
            return BaseResult.ok(SysTips.DEL_SUCCESS);
        }
        return BaseResult.error(SysTips.ERROR_MSG);
    }

//    /**
//     * 根据id查询
//     *
//     * @param id
//     */
//    @ApiOperation("根据id查询")
//    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
//    @GetMapping(value = "/queryById")
//    public BaseResult<Receipt> queryById(@RequestParam(name = "id", required = true) int id) {
//        Receipt receipt = receiptService.getById(id);
//        return BaseResult.ok(receipt);
//    }

    /**
     * 创建/编辑订单
     */
    @ApiOperation("创建/编辑订单")
    @PostMapping(value = "/createOrder")
    public BaseResult createOrder(@RequestBody ReceiptForm form) {
        form.checkParam();
        //TODO 远程调用容器
        List<Container> containers = containerService.getEnableByWarehouseId(form.getWarehouseId());
        Map<String, Container> containerMap = containers.stream().collect(Collectors.toMap(e -> e.getCode(), e -> e));
        form.checkContainer(containerMap);
        receiptService.createOrder(form);
        return BaseResult.ok();
    }

    @ApiOperation("复制物料")
    @PostMapping(value = "/copyMaterial")
    public BaseResult<MaterialForm> copyMaterial(@RequestBody MaterialForm form) {
        form.checkParam();
        return BaseResult.ok(this.materialService.copyMaterial(form));
    }

    @ApiOperation("收货确认")
    @PostMapping(value = "/receiptConfirmation")
    public BaseResult receiptConfirmation(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return BaseResult.error(SysTips.PARAM_ERROR);
        }
        this.receiptService.receiptConfirmation(id);
        return BaseResult.ok();
    }

    /**
     * app外部调用收货确认
     *
     * @param id
     * @return
     */
    @ApiOperation("app收货确认")
    @GetMapping(value = "/client/receiptConfirmation")
    public BaseResult receiptConfirmationFeign(@RequestParam(name = "id", required = true) Long id) {
        this.receiptService.receiptConfirmation(id);
        return BaseResult.ok();
    }

    /**
     * 获取收货单详情信息
     *
     * @param id
     */
    @ApiOperation("获取收货单详情信息")
    @GetMapping(value = "/getDetails")
    public BaseResult<ReceiptVO> getDetails(@RequestParam(name = "id") Long id) {
        ReceiptVO receiptVO = receiptService.getDetails(id);
        return BaseResult.ok(receiptVO);
    }


    /**
     * 外部调用 获取收货单详情信息
     *
     * @param id
     */
    @ApiOperation("app获取收货单详情信息")
    @GetMapping(value = "/client/getDetails")
    public BaseResult<ReceiptVO> getDetailsFeign(@RequestParam(name = "id") Long id) {
        ReceiptVO receiptVO = receiptService.getDetails(id);
        return BaseResult.ok(receiptVO);
    }


    /**
     * 根据id查询
     */
    @ApiOperation("整单撤销")
    @PostMapping(value = "/cancel")
    public BaseResult<Receipt> cancel(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return BaseResult.error(SysTips.PARAM_ERROR);
        }
        receiptService.cancel(id);
        return BaseResult.ok();
    }


    @ApiOperation("收货单订单状态下拉值")
    @GetMapping(value = "/getReceivingOrderStatusType")
    public BaseResult getReceivingOrderStatusType() {
        //收货单订单状态下拉值
        List<LinkedHashMap<String, Object>> receivingOrderStatus = authService.queryDictByDictType("receivingOrderStatus");

        receivingOrderStatus.stream().forEach(ro -> {
            ro.put("value", Integer.parseInt(ro.get("value").toString()));

        });
        Map<String, Object> map = new HashMap<>();
        map.put("receivingOrderStatus", receivingOrderStatus);
        return BaseResult.ok(map);
    }


    /**
     * 根据查询条件导出收货单
     *
     * @param response         响应对象
     * @param queryReceiptForm 参数queryReceiptFormZ
     */
    @SysDataPermission(clazz = QueryReceiptForm.class)
    @ApiOperation("根据查询条件导出收货单")
    @PostMapping(path = "/exportReceiptLocation")
    public void exportReceipt(HttpServletResponse response, @RequestBody QueryReceiptForm queryReceiptForm, HttpServletRequest req) {
        try {
            List<String> headList = Arrays.asList(

                    "收货单号",
                    "所属仓库名称",
                    "收货通知单号",
                    "仓库名称",
                    "货主名称",
                    "订单状态",
                    "合计数量",
                    "合计重量",
                    "合计体积",
                    "收货人",
                    "收货时间",
                    "预计到货时间",
                    "质检单号",
                    "创建人",
                    "创建时间");
            List<LinkedHashMap<String, Object>> dataList = receiptService.queryReceiptForExcel(queryReceiptForm, req);
            ExcelUtils.exportExcel(headList, dataList, "收货单", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * app根据物料编码 查询物料详情和物料规格
     * w h
     *
     * @param materialCode
     * @return
     */
    @ApiOperation("app根据物料编码 查询物料详情和物料规格")
    @GetMapping(value = "/client/queryByMaterialCodeOne")
    public BaseResult queryByMaterialCodeOne(@RequestParam(name = "materialCode", required = true) String materialCode) {

        QueryWrapper<WmsMaterialBasicInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(WmsMaterialBasicInfo::getMaterialCode, materialCode);
        WmsMaterialBasicInfo wmsMaterialBasicInfoOne = wmsMaterialBasicInfoService.getOne(condition);
        if (wmsMaterialBasicInfoOne == null) {
            return BaseResult.error(SysTips.INEXISTENCE_MATERIALS_INFORMATION);
        }
        WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO = wmsMaterialBasicInfoService.selectById(wmsMaterialBasicInfoOne.getId());
        if (wmsMaterialBasicInfoVO == null) {
            return BaseResult.error(SysTips.INEXISTENCE_MATERIALS_INFORMATION);
        }
        return BaseResult.ok(wmsMaterialBasicInfoVO);
    }

    // 1  扫码校验  查询  收货单 校验是否存在
    @ApiOperation("扫码校验这个收货单号是否存在该物料编码")
    @PostMapping(value = "/client/checkoutMaterial")
    public BaseResult checkoutMaterial(@RequestBody QueryClientQualityMaterialForm form) {
        //直接去查询物料
        if (form.getMaterialCode() != null && form.getOrderNum() != null) {

            //根据物料单号查询某条物料信息去复制
            Material material = new Material();
            material.setOrderNum(form.getOrderNum());//收货单号
            material.setMaterialCode(form.getMaterialCode());//物料编号

            List<Material> materialOne = materialService.findMaterialOne(material);

            if (materialOne.size() == 0) {
                return BaseResult.error(SysTips.NOT_THIS_ORDER_MATERIALS_INFORMATION);
            }
            return BaseResult.ok(materialOne.get(0));
        }
        return BaseResult.error(SysTips.PARAM_ERROR);
    }


    // 2  扫码校验  查询  收货单 校验是否存在
    @ApiOperation("扫码校验这个收货单号是否存在该SN物料编码")
    @PostMapping(value = "/client/checkoutMaterialSN")
    public BaseResult checkoutMaterialSN(@RequestBody QueryClientQualityMaterialForm form) {
        //查询的是Sn物料信息
        if (form.getSerialNum() != null && form.getOrderNum() != null) {
            //物料信息   根据物料编号去查询物料信息
            QueryWrapper<MaterialSn> condition = new QueryWrapper<>();
            condition.lambda().eq(MaterialSn::getOrderNum, form.getOrderNum())
                    .eq(MaterialSn::getSerialNum, form.getSerialNum());
            MaterialSn one = materialSnService.getOne(condition);
            if (one == null) {
                return BaseResult.error(SysTips.NOT_THIS_ORDER_MATERIALS_INFORMATION);
            }
            return BaseResult.ok(one);
        }
        return BaseResult.error(SysTips.PARAM_ERROR);
    }


    /**
     * 能拿到的数据     收货单号  收货id      物料编号      实收数量       容器号      图片字段（暂时没有）
     */
    //远程调用保存物料信息
    // 1.根据单号查询收货单号
    // 2.根据收货单号和 物料编号查询  物料信息
    // 3. 拿到收货数量和  容器号   去复制  物料信息
    // 4.调用复制物料信息
    @ApiOperation("外部调用复制物料信息")
    @PostMapping(value = "/client/recordCopyMaterial")
    public BaseResult recordCopyMaterial(@RequestBody QueryClientQualityMaterialForm form) {
//        Result recordCopyMaterial = receiptService.createRecordCopyMaterial(form);
        return receiptService.createRecordCopyMaterial(form);
    }


    /**
     * 能拿到的数据     收货单号  收货id      物料编号      实收数量       容器号      图片字段（暂时没有）
     */

    //远程调用保存 有SN的物料信息
    // 1.根据单号查询收货单号
    // 2.根据收货单号和 物料编号查询  物料信息
    // 3. 拿到收货数量和  容器号   去复制  物料信息
    // 4.调用复制物料信息
    @ApiOperation("外部调用复制SN物料信息")
    @PostMapping(value = "/client/recordCopyMaterialSN")
    public BaseResult recordCopyMaterialSN(@RequestBody QueryClientQualityMaterialForm form) {
//        Result recordCopyMaterialSN = receiptService.createRecordCopyMaterialSN(form);
        return receiptService.createRecordCopyMaterialSN(form);
    }




    @ApiOperation("测试接口")
    @PostMapping(value = "/client/test")
    public BaseResult test(@RequestBody QueryClientQualityMaterialForm form){


        receiptService.updateMaterial(form);
       return BaseResult.ok();
    }


}
