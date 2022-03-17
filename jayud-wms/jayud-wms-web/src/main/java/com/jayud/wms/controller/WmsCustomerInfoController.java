package com.jayud.wms.controller;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.model.bo.WmsCustomerInfoForm;
import com.jayud.wms.model.dto.WmsCustomerInfoDTO;
import com.jayud.wms.model.po.WmsCustomerInfo;
import com.jayud.wms.model.vo.WmsCustomerInfoVO;
import com.jayud.wms.service.IWmsCustomerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

/**
 * <p>
 * 客户信息 前端控制器
 * </p>
 */

@RestController
@Api("客户信息")
@RequestMapping("/wmsCustomerInfo")
public class WmsCustomerInfoController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IWmsCustomerInfoService wmsCustomerInfoService;

    @Autowired
    private AuthClient authClient;


    /**
     * 分页查询客户信息数据
     *
     * @param wmsCustomerInfo 查询条件
     * @return
     */
    @ApiOperation("分页查询客户信息数据")
    @GetMapping("/selectWmsCustomerInfoPage")
    public BaseResult<IPage<WmsCustomerInfoVO>> selectWmsCustomerInfoPage(WmsCustomerInfo wmsCustomerInfo,
                                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                      HttpServletRequest req) {
        return BaseResult.ok(wmsCustomerInfoService.selectPage(wmsCustomerInfo, pageNo, pageSize, req));
    }

    /**
     * 列表查询数据
     *
     * @param wmsCustomerInfo 查询条件
     * @return
     */
    @ApiOperation("查询客户信息数据")
    @GetMapping("/selectWmsCustomerInfoList")
    public BaseResult<List<WmsCustomerInfoVO>> selectList(WmsCustomerInfo wmsCustomerInfo,
                                                      HttpServletRequest req) {
        return BaseResult.ok(wmsCustomerInfoService.selectList(wmsCustomerInfo));
    }

    /**
     * 新增客户信息
     *
     * @param wmsCustomerInfoForm
     **/
    @ApiOperation("新增")
    @PostMapping("/addWmsCustomerInfo")
    public BaseResult addWmsCustomerInfo(@RequestBody WmsCustomerInfoForm wmsCustomerInfoForm) {
//        if (wmsCustomerInfoForm.getId() == null) {
//            WmsCustomerInfo wmsCustomerInfo1 = this.wmsCustomerInfoService.getWmsCustomerInfoByName(null, wmsCustomerInfoForm.getCustomerName());
//            if (wmsCustomerInfo1 != null) {
//                return BaseResult.error("客户名称已存在");
//            }
//            WmsCustomerInfo wmsCustomerInfo2 = this.wmsCustomerInfoService.getWmsCustomerInfoByCode(wmsCustomerInfoForm.getCustomerCode(), null);
//            if (wmsCustomerInfo2 != null) {
//                return BaseResult.error("客户编号已存在");
//            }
//        }

        wmsCustomerInfoService.saveOrUpdateWmsCustomerInfo(wmsCustomerInfoForm);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 删除
     *
     * @param
     **/
    @ApiOperation("删除单个客户 ")
    @PostMapping("/deleteWmsCustomerInfoOne")
    public BaseResult deleteWmsCustomerInfoOne(@RequestBody WmsCustomerInfoForm wmsCustomerInfoForm) {
        if (wmsCustomerInfoForm.getId() == null) {
            return BaseResult.error("id不为空");
        }
        WmsCustomerInfo wmsCustomerInfo = new WmsCustomerInfo();
        wmsCustomerInfo.setId(wmsCustomerInfoForm.getId());
        wmsCustomerInfo.setIsDeleted(true);
        wmsCustomerInfo.setUpdateBy(CurrentUserUtil.getUsername());
        wmsCustomerInfo.setUpdateTime(new Date());
        wmsCustomerInfoService.updateById(wmsCustomerInfo);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * 删除
     *
     * @param id
     **/
    @ApiOperation("删除多个")
    @PostMapping("/deleteWmsCustomerInfoList")
    public BaseResult deleteWmsCustomerInfoList(@RequestBody List<Long> id) {
        if (id.size() == 0) {
            return BaseResult.error("id不为空");
        }
        for (int i = 0; i < id.size(); i++) {
            WmsCustomerInfo wmsCustomerInfo = new WmsCustomerInfo();
            wmsCustomerInfo.setId(id.get(i));
            wmsCustomerInfo.setIsDeleted(true);
            wmsCustomerInfo.setUpdateBy(CurrentUserUtil.getUsername());
            wmsCustomerInfo.setUpdateTime(new Date());
            wmsCustomerInfoService.updateById(wmsCustomerInfo);
        }

        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    //启动和不启动


    /**
     * 启用/禁用客户
     *
     * @param wmsCustomerInfoForm
     **/
    @ApiOperation(value = "启用/禁用客户地址,id是银行主键")
    @PostMapping(value = "/enableOrDisable")
    public BaseResult enableOrDisable(@RequestBody WmsCustomerInfoForm wmsCustomerInfoForm) {
        if (wmsCustomerInfoForm.getId() == null) {
            return BaseResult.error("id不为空");
        }
        WmsCustomerInfo wmsCustomerInfo = new WmsCustomerInfo();
        wmsCustomerInfo.setId(wmsCustomerInfoForm.getId());
        wmsCustomerInfo.setStatus(wmsCustomerInfoForm.getStatus());

        wmsCustomerInfoService.updateById(wmsCustomerInfo);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


//    @ApiOperation(value = "启用/禁用客户地址,id是银行主键")
//    @PostMapping(value = "/enableOrDisable")
//    public CommonResult enableOrDisable(@RequestBody Map<String, String> map) {
//        Long id = MapUtil.getLong(map, "id");
//        if (id == null) {
//            return CommonResult.error(500, "id is required");
//        }
//        BankAccount tmp = this.bankAccountService.getById(id);
//        Integer status = StatusEnum.ENABLE.getCode().equals(tmp.getStatus()) ? StatusEnum.DISABLE.getCode() : StatusEnum.ENABLE.getCode();
//        BankAccount bankAccount = new BankAccount().setId(id).setStatus(Integer.valueOf(status));
//        return CommonResult.success(this.bankAccountService.saveOrUpdateAddr(bankAccount));
//    }
//

    //
//    @ApiOperation("客户类型下拉值")
//    @GetMapping(value = "/getByDataClientType")
//    public BaseResult getByData () {
//
//        List<LinkedHashMap<String, Object>> warehouseType =  sysDictService.queryDictByDictType("clientType");
//        Map<String, Object> map = new HashMap<>();
//        map.put("warehouseType",warehouseType);
//        return BaseResult.ok(map);
//    }
    //供应商类型的客户类型下拉值
    @ApiOperation("供应商类型的客户类型下拉值")
    @GetMapping(value = "/getByDataSupplierType")
    public BaseResult<List<WmsCustomerInfoVO>> getByDataSupplierType() {
        //查询客户类型为供应商的ID
        List<SysDictItem> sysDictItemList = authClient.selectItemByDictCode("clientType").getResult();
        String id = "";
        for (SysDictItem item : sysDictItemList){
            if (item.getItemText().equals("供应商")){
                id = item.getId().toString();
            }
        }
        WmsCustomerInfo wmsCustomerInfo = new WmsCustomerInfo();
        wmsCustomerInfo.setCustomerTypeId(Long.parseLong(id));
        List<WmsCustomerInfoVO> wmsCustomerInfos = wmsCustomerInfoService.selectList(wmsCustomerInfo);
        return BaseResult.ok(wmsCustomerInfos);
    }

    // bases 项目使用
    @ApiOperation("客户类型下拉值")
    @GetMapping(value = "/getByDataClientType")
    public BaseResult getByData() {
        List<SysDictItem> sysDictItemList = authClient.selectItemByDictCode("clientType").getResult();
        List<LinkedHashMap<String, Object>> warehouseType = new ArrayList<>();
        sysDictItemList.forEach(sysDictItem -> {
            LinkedHashMap<String, Object> maps = new LinkedHashMap<>();
            maps.put("name",sysDictItem.getItemText());
            maps.put("value",sysDictItem.getItemValue());
            warehouseType.add(maps);
        });

        Map<String, Object> map = new HashMap<>();
        map.put("warehouseType", warehouseType);
        return BaseResult.ok(map);
    }


    /**
     * 根据查询条件导出客户信息
     *
     * @param response        响应对象
     * @param wmsCustomerInfo 参数Map
     */
    @ApiOperation("根据查询条件导出客户信息")
    @GetMapping("/exportWmsCustomerInfoLocation")
    public void exportWmsCustomerInfoLocation(HttpServletResponse response, WmsCustomerInfo wmsCustomerInfo,
                                              HttpServletRequest req) {
        try {
            List<String> headList = Arrays.asList("客户编号", "客户名称", "客户类型", "联系人", "手机号", "电子邮箱", "联系地址", "是否可用", "创建人", "创建时间");
            List<LinkedHashMap<String, Object>> dataList = wmsCustomerInfoService.queryWmsCustomerInfoForExcel(wmsCustomerInfo, req);
            ExcelUtils.exportExcel(headList, dataList, "客户信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }


    @ApiOperation(value = "上传文件-导入客户信息")
    @RequestMapping(value = "/importWmsCustomerInfoLocation", method = RequestMethod.POST)
    public BaseResult importWmsCustomerInfoLocation(HttpServletRequest req, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return BaseResult.error("文件为空！");
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
        Map<String, String> aliasMap = new HashMap<>();
        aliasMap.put("客户编号", "customerCode");
        aliasMap.put("客户名称", "customerName");
        aliasMap.put("客户类型", "customerTypeDetails");
        aliasMap.put("联系人", "userName");
        aliasMap.put("手机号", "phoneNumber");
        aliasMap.put("电子邮箱", "email");
        aliasMap.put("联系地址", "address");
        aliasMap.put("是否可用", "statusString");
        aliasMap.put("创建人", "createBy");
        aliasMap.put("创建时间", "createTime");

        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<WmsCustomerInfoForm> list = excelReader.read(0, 1, WmsCustomerInfoForm.class);
        for (WmsCustomerInfoForm wmsCustomerInfoForm : list) {
            String s = wmsCustomerInfoForm.checkParam();
            if (!s.equals("成功")) {
                return BaseResult.error(s);
            }
            List<SysDictItem> sysDictItemList = authClient.selectItemByDictCode("clientType").getResult();
            List<LinkedHashMap<String, Object>> linkedHashMapEx = new LinkedList<>();
            sysDictItemList.stream().forEach(vo -> {
                LinkedHashMap<String, Object> linkedList = new LinkedHashMap<String, Object>();
                if (vo.getItemText().equals(wmsCustomerInfoForm.getCustomerTypeDetails())) {
                    linkedList.put("id", vo.getId().toString());
                    linkedHashMapEx.add(linkedList);
                }
            });

            //客户名称 和客户编号校验
            WmsCustomerInfo wmsCustomerInfo1 = this.wmsCustomerInfoService.getWmsCustomerInfoByName(null, wmsCustomerInfoForm.getCustomerName());
//            if (wmsCustomerInfo1 != null) {
//                return BaseResult.error("客户名称已存在");
//            }
            WmsCustomerInfo wmsCustomerInfo2 = this.wmsCustomerInfoService.getWmsCustomerInfoByCode(wmsCustomerInfoForm.getCustomerCode(), null);
//            if (wmsCustomerInfo2 != null) {
//                return BaseResult.error("客户编号已存在");
//            }

            if (wmsCustomerInfo1 == null && wmsCustomerInfo2 == null) {

                WmsCustomerInfoForm wmsCustomerInfoForm1 = new WmsCustomerInfoForm();
                wmsCustomerInfoForm1.setCustomerCode(wmsCustomerInfoForm.getCustomerCode());//客户编号
                wmsCustomerInfoForm1.setCustomerName(wmsCustomerInfoForm.getCustomerName());//客户名称
                wmsCustomerInfoForm1.setCustomerTypeId(Long.parseLong(linkedHashMapEx.get(0).get("id").toString()));//客户类型
                wmsCustomerInfoForm1.setUserName(wmsCustomerInfoForm.getUserName());//联系人
                wmsCustomerInfoForm1.setPhoneNumber(wmsCustomerInfoForm.getPhoneNumber());//手机号
                wmsCustomerInfoForm1.setEmail(wmsCustomerInfoForm.getEmail());//电子邮箱
                wmsCustomerInfoForm1.setAddress(wmsCustomerInfoForm.getAddress());//联系地址
//            wmsCustomerInfoForm1.setStatus();//是否可用
                wmsCustomerInfoForm1.setCreateBy(CurrentUserUtil.getUsername());//创建人
                wmsCustomerInfoForm1.setCreateTime(new Date());//时间

                String statusString = wmsCustomerInfoForm.getStatusString();
                System.out.println(statusString);
                if (wmsCustomerInfoForm.getStatusString().equals("是")) {
                    wmsCustomerInfoForm1.setStatus(true);
                } else {
                    wmsCustomerInfoForm1.setStatus(false);
                }
                wmsCustomerInfoService.saveOrUpdateWmsCustomerInfo(wmsCustomerInfoForm1);
            }
        }
        return BaseResult.ok();
    }

    /**
     * 获取仓库DTO
     * @param appId
     * @return
     */
    @ApiOperation("获取仓库DTO")
    @GetMapping(value = "/client/getCustomerInfoDTOByAppId")
    public BaseResult<WmsCustomerInfoDTO> getCustomerInfoDTOByAppId(@RequestParam(name = "appId", required = true) String appId) {
        WmsCustomerInfoDTO dto = wmsCustomerInfoService.getCustomerInfoDTOByAppId(appId);
        return BaseResult.ok(dto);
    }

}
