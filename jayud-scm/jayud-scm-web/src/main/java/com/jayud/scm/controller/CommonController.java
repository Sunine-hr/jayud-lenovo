package com.jayud.scm.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.db.meta.Table;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.Query;
import com.jayud.scm.model.bo.AddCommodityModelForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.PermissionForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.TableEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.BDataDicEntryVO;
import com.jayud.scm.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import net.sf.saxon.expr.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 公共服务实现类
 * </p>
 *
 * @author
 * @since 2020-07-20
 */
@RestController
@RequestMapping("/common")
@Api(tags = "scm公共接口")
public class CommonController {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IBUnitService bUnitService;

    @Autowired
    private IHsCodeService hsCodeService;

    @Autowired
    private IBPublicFilesService bPublicFilesService;

    @Autowired
    private ISystemRoleActionService systemRoleActionService;

    @Autowired
    private ISystemUserRoleRelationService systemUserRoleRelationService;

    @Autowired
    private ISystemActionService systemActionService;

    @Autowired
    private ISystemRoleActionCheckService systemRoleActionCheckService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerRelationerService customerRelationerService;

    @Autowired
    private IHubReceivingService hubReceivingService;

    @Autowired
    private IHubShippingService hubShippingService;

    @Autowired
    private ICheckOrderService checkOrderService;

    @Autowired
    private IHgBillService hgBillService;

    @Autowired
    private IHgTruckService hgTruckService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @Autowired
    private ICustomerTruckPlaceService customerTruckPlaceService;

    @Autowired
    private IAccountBankBillService accountBankBillService;

    @Autowired
    private IAccountBankBillEntryService accountBankBillEntryService;

    @Autowired
    private IAcctReceiptService acctReceiptService;

    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private IExportTaxInvoiceService exportTaxInvoiceService;

    @Autowired
    private IAcctPayService acctPayService;

    @Autowired
    private IHubShippingDeliverService hubShippingDeliverService;


    @ApiOperation(value = "根据id通用查询方法")
    @PostMapping(value = "/getClassById")
    public CommonResult<Map> getClassById(@Valid @RequestBody QueryCommonForm form) {
        form.setTable(TableEnum.getDesc(form.getKey()));
        Map map = customerService.getClassById(form);
        return CommonResult.success(map);
    }

    @ApiOperation(value = "删除通用方法")
    @PostMapping(value = "/delete")
    public CommonResult delete(@Valid @RequestBody DeleteForm deleteForm) {

        //获取当前登录人信息
        SystemUser systemUserBySystemName = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        deleteForm.setName(systemUserBySystemName.getUserName());
        deleteForm.setId(systemUserBySystemName.getId());
        deleteForm.setDeleteTime(LocalDateTime.now());
        deleteForm.setTable(TableEnum.getDesc(deleteForm.getKey()));

        if(deleteForm.getKey().equals(1)){
            for (Long id : deleteForm.getIds()) {
                Commodity byId = commodityService.getById(id);
                if(byId.getHsCodeNo() != null && byId.getStateFlag().equals("Y")){
                    return CommonResult.error(444,byId.getSkuName()+"已审核且已归类无法进行删除");
                }
            }
        }
        if(deleteForm.getTable().equals(TableEnum.hg_truck.getDesc())){
            for (Long id : deleteForm.getIds()) {
                List<BookingOrder> bookingOrderByHgTrackId = bookingOrderService.getBookingOrderByHgTrackId(id.intValue());
                if(CollectionUtils.isNotEmpty(bookingOrderByHgTrackId)){
                    return CommonResult.error(444,"该车辆绑定了委托单，无法进行删除");
                }
            }

        }

        for (Long id : deleteForm.getIds()) {
            QueryCommonForm form = new QueryCommonForm();
            form.setId(id.intValue());
            form.setTable(deleteForm.getTable());
            Map map = customerService.getClassById(form);
            if(map.get("checkStateFlag") != null && !map.get("checkStateFlag").equals("N0")){
                return CommonResult.error(444,"该数据已审核，无法进行删除");
            }
            if(deleteForm.getKey().equals(1)){
                if(map.get("stateFlag") != null && map.get("stateFlag").equals(2)){
                    return CommonResult.error(444,"该数据已审核，无法进行删除");
                }
            }
        }

        boolean result = commodityService.commonDelete(deleteForm);
        if(!result){
            return CommonResult.error(444,"删除失败");
        }

        switch (deleteForm.getKey()){
            case 1:
                result = commodityService.delete(deleteForm);
                break;
//            case 2:
//                result = hsCodeService.delete(deleteForm);
//                break;
            case 3:
                result = bPublicFilesService.delete(deleteForm);
                break;
            case 7:
                result = customerService.delete(deleteForm);
                break;
//            case 17:
//                result = hubReceivingService.delete(deleteForm);
//                break;
//            case 18:
//                result = hubShippingService.delete(deleteForm);
//                break;
            case 21:
                result = checkOrderService.delete(deleteForm);
                break;
            case 23:
                result = hgTruckService.delete(deleteForm);
                break;
            case 24:
                result = hgBillService.delete(deleteForm);
                break;
            case 25:
                result = customerTruckPlaceService.delete(deleteForm);
                break;
            case 27:
                result = accountBankBillService.delete(deleteForm);
                break;
            case 29:
                result = acctReceiptService.delete(deleteForm);
                break;
            case 30:
                result = invoiceService.delete(deleteForm);
                break;
            case 31:
                result = exportTaxInvoiceService.delete(deleteForm);
                break;
            case 32:
                result = acctPayService.delete(deleteForm);
                break;
            case 37:
                result = hubShippingDeliverService.delete(deleteForm);
                break;
        }

        if(result){
            return CommonResult.success();
        }else{
            return CommonResult.error(444,"删除失败");
        }
    }

    @ApiOperation(value = "添加或修改商品下拉列表框")
    @PostMapping(value = "/commodityCombox")
    public CommonResult commodityCombox() {
        //获取商品单位下拉
        List<String> units = bUnitService.getUnits();
        return CommonResult.success(units);
    }

    @ApiOperation(value = "海关编码模糊查询")
    @PostMapping(value = "/findHsCode")
    public CommonResult findHsCode(@RequestBody Map<String,Object> map) {
        String codeNo = MapUtil.getStr(map, "codeNo");
        //获取商品单位下拉
        List<String> hsCode = hsCodeService.findHsCode(codeNo);
        return CommonResult.success(hsCode);
    }

    /**
     * 导出入库商品模板
     */
    @ApiOperation(value = "导出商品模板")
    @GetMapping(value = "/exportCommodityTemplate")
    public void exportInProductTemplate( HttpServletResponse response) throws IOException {
//        ExcelUtils.exportSinglePageHeadExcel("商品模板", AddCommodityModelForm.class,response);

        ExcelWriter excelWriter = null;
        OutputStream outputStream = null;
        String fileName = "商品模板";
        try {
            outputStream = response.getOutputStream();
            fileName = fileName + ".xlsx";
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
            excelWriter = EasyExcel.write(response.getOutputStream(), AddCommodityModelForm.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("入库商品").build();
            List<AddCommodityModelForm> commodityModelForms = new ArrayList<>();
            excelWriter.write(commodityModelForms, writeSheet);
        }  catch (Exception e) {
            outputStream.close();
            e.printStackTrace();
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }

        }
    }

    @ApiOperation(value = "上传文件-导入商品信息")
    @RequestMapping(value = "/importExcelByCustomerGoods", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperationSupport(order = 4)
    public CommonResult importExcelByCustomerGoods(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return CommonResult.error(-1, "文件为空！");
        }
        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调用用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);

        //配置别名
        Map<String,String> aliasMap=new HashMap<>();
        aliasMap.put("商品型号","skuModel");
        aliasMap.put("商品名称","skuName");
        aliasMap.put("报关名称","skuNameHs");
        aliasMap.put("单位","skuUnit");
        aliasMap.put("品牌","skuBrand");
        aliasMap.put("产地","skuOrigin");
        aliasMap.put("商品描述","skuNotes");
        aliasMap.put("配件","accessories");
        aliasMap.put("单位净重","unitNw");
        aliasMap.put("参考价","referencePrice");
        aliasMap.put("料号","itemNo");
        aliasMap.put("备注","remark");

        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<AddCommodityModelForm> list = excelReader.read(0, 1, AddCommodityModelForm.class);

        return CommonResult.success(list);
    }

    @ApiOperation(value = "获取编号")
    @PostMapping(value = "/getOrderNo")
    public CommonResult getOrderNo() {
        String orderNo = this.commodityService.getOrderNo(NoCodeEnum.COMMODITY.getCode(), LocalDateTime.now());
        return CommonResult.success(orderNo);
    }

    @ApiOperation(value = "判断是否有按钮权限")
    @PostMapping(value = "/isPermission")
    public CommonResult isPermission(@RequestBody PermissionForm form) {

        //获取登录用户
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        //获取按钮权限
//        SystemAction systemAction = systemActionService.getSystemActionByActionCode(form.getActionCode());

        if(!systemUser.getUserName().equals("admin")){
            //获取登录用户所属角色
            List<SystemRole> enabledRolesByUserId = systemUserRoleRelationService.getEnabledRolesByUserId(systemUser.getId());
            List<Long> longs = new ArrayList<>();
            for (SystemRole systemRole : enabledRolesByUserId) {
                longs.add(systemRole.getId());
//                if(systemRoleAction == null){
//                    return CommonResult.error(444,"该用户没有该按钮权限");
//                }
            }
            List<SystemRoleAction> systemRoleActions = systemRoleActionService.getSystemRoleActionByRoleIdsAndActionCode(longs,form.getActionCode());
            if(CollectionUtil.isEmpty(systemRoleActions)){
                return CommonResult.error(444,"该用户没有该按钮权限");
            }
        }

        //拥有按钮权限，判断是否为审核按钮
        if(!form.getType().equals(0)){
            if(form.getCustomerAudit()){
                List<CustomerRelationer> customerRelationers = customerRelationerService.getCustomerRelationerByCustomerIdAndType(form.getId(),"客户下单人");
                if(CollectionUtils.isEmpty(customerRelationers)){
                    return CommonResult.error(444,"该审核客户没有对应下单人，无法审核");
                }
            }
            //
            form.setTable(TableEnum.getDesc(form.getKey()));
            form.setUserId(systemUser.getId().intValue());
            form.setUserName(systemUser.getUserName());
            if(form.getType().equals(1)){//审核
                return customerService.toExamine(form);
            }
            if(form.getType().equals(2)){
                return customerService.deApproval(form);
            }
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "通过配置编码获取对应的下拉列表")
    @PostMapping(value = "/getDropDownList")
    public CommonResult getDropDownList(@RequestBody Map<String,Object> map) {
        String dicCode = MapUtil.getStr(map, "dicCode");
        List<BDataDicEntryVO> bDataDicEntryVOS = ibDataDicEntryService.getDropDownList(dicCode);
        return CommonResult.success(bDataDicEntryVOS);
    }

}
