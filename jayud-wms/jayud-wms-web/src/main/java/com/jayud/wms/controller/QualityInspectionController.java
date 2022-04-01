package com.jayud.wms.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.QueryClientQualityInspectionForm;
import com.jayud.common.result.BasePage;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.bo.QualityInspectionForm;
import com.jayud.wms.model.bo.QueryQualityInspectionForm;
import com.jayud.wms.model.bo.QueryQualityInspectionMaterialForm;
import com.jayud.wms.model.po.QualityInspection;
import com.jayud.wms.model.po.QualityInspectionMaterial;
import com.jayud.wms.model.vo.QualityInspectionVO;
import com.jayud.wms.service.AuthService;
import com.jayud.wms.service.IQualityInspectionMaterialService;
import com.jayud.wms.service.IQualityInspectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * 质检检测 控制类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Api(tags = "质检检测")
@RestController
@RequestMapping("/qualityInspection")
public class QualityInspectionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IQualityInspectionService qualityInspectionService;

    @Autowired
    private AuthService authService;

    @Autowired
    public IQualityInspectionMaterialService qualityInspectionMaterialService;


    /**
     * 分页查询数据
     *
     * @param queryQualityInspectionForm 查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<BasePage<QualityInspectionVO>> selectPage(@RequestBody QueryQualityInspectionForm queryQualityInspectionForm, HttpServletRequest req) {
        IPage<QualityInspectionVO> qualityInspectionVOIPage = qualityInspectionService.selectPage(queryQualityInspectionForm, queryQualityInspectionForm.getCurrentPage(), queryQualityInspectionForm.getPageSize(), req);
        BasePage<QualityInspectionVO> basePage = new BasePage<>();
        BeanUtils.copyProperties(qualityInspectionVOIPage,basePage);
        return BaseResult.ok(basePage);
    }

    /**
     * app外部调用方法
     * @param queryClientQualityInspectionForm
     * @return
     */
    @ApiOperation("app外部调用分页查询数据")
    @PostMapping(value ="/client/selectPage")
    public BaseResult selectPageFeign(@RequestBody QueryClientQualityInspectionForm queryClientQualityInspectionForm) {
        HttpServletRequest req=null;
        QueryQualityInspectionForm queryQualityInspectionForm = ConvertUtil.convert(queryClientQualityInspectionForm, QueryQualityInspectionForm.class);
        IPage<QualityInspectionVO> qualityInspectionVOIPage = qualityInspectionService.selectPage(queryQualityInspectionForm, queryQualityInspectionForm.getCurrentPage(), queryQualityInspectionForm.getPageSize(), req);
        return BaseResult.ok(qualityInspectionVOIPage);
    }

    /**
     * 列表查询数据
     *
     * @param qualityInspection 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @PostMapping("/selectList")
    public BaseResult<List<QualityInspection>> selectList(@RequestBody QualityInspection qualityInspection,
                                                      HttpServletRequest req) {
        return BaseResult.ok(qualityInspectionService.selectList(qualityInspection));
    }

    /**
     * 新增
     *
     * @param form
     **/
//    @ApiOperation("新增")
//    @PostMapping("/add")
//    public BaseResult add(@Valid @RequestBody QualityInspection qualityInspection ){
//        qualityInspectionService.save(qualityInspection);
//        return BaseResult.ok(SysTips.ADD_SUCCESS);
//    }
    @ApiOperation("创建/编辑订单")
    @PostMapping("/createOrder")
    public BaseResult createOrder(@Valid @RequestBody QualityInspectionForm form) {
        form.checkParam();
        qualityInspectionService.createOrder(form);
        return BaseResult.ok();
    }

    @ApiOperation("获取质检单详情信息")
    @GetMapping(value = "/getDetails")
    public BaseResult<QualityInspectionVO> getDetails(@RequestParam(name = "id") Long id) {
        QualityInspectionVO tmp = qualityInspectionService.getDetails(id);
        return BaseResult.ok(tmp);
    }

    /**
     * app外部调用获取质检单详情信息
     * @param id
     * @return
     */
    @ApiOperation("外部调用获取质检单详情信息")
    @GetMapping(value = "/client/getDetails")
    public BaseResult  getDetailsFeign(@RequestParam(name = "id") Long id) {
        QualityInspectionVO tmp = qualityInspectionService.getDetails(id);
        return BaseResult.ok(tmp);
    }

    /**
     * 质检确认
     */
    @ApiOperation("质检确认")
    @PostMapping(value = "/confirm")
    public BaseResult<QualityInspection> confirm(@RequestBody Map<String,Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return BaseResult.error(SysTips.PARAM_ERROR);
        }
        qualityInspectionService.confirm(id);
        return BaseResult.ok();
    }


    @ApiOperation("转为待上架单")
    @PostMapping(value = "/transferPendingShelf")
    public BaseResult<QualityInspection> transferPendingShelf(@RequestBody Map<String,Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return BaseResult.error(SysTips.PARAM_ERROR);
        }
        qualityInspectionService.transferPendingShelf(id);
        return BaseResult.ok();
    }
    /**
     * 编辑
     * @param qualityInspection
     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody QualityInspection qualityInspection ){
//        qualityInspectionService.updateById(qualityInspection);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }

    /**
     * 保存(新增+编辑)
     * @param qualityInspection
     **/
//    @ApiOperation("保存(新增+编辑)")
//    @PostMapping("/save")
//    public BaseResult save(@Valid @RequestBody QualityInspection qualityInspection ){
//        QualityInspection qualityInspection1 = qualityInspectionService.saveOrUpdateQualityInspection(qualityInspection);
//        return BaseResult.ok(qualityInspection1);
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
        qualityInspectionService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 逻辑删除
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        qualityInspectionService.delQualityInspection(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<QualityInspection> queryById(@RequestParam(name = "id", required = true) int id) {
        QualityInspection qualityInspection = qualityInspectionService.getById(id);
        return BaseResult.ok(qualityInspection);
    }

    /**
     * 根据查询条件导出质检检测
     *
     * @param response 响应对象
     * @param queryQualityInspectionForm 参数Map
     */
    @ApiOperation("根据查询条件导出质检检测")
    @PostMapping(path = "/exportQualityInspection")
    public void exportQualityInspection(HttpServletResponse response,@RequestBody QueryQualityInspectionForm queryQualityInspectionForm, HttpServletRequest req) {
        try {
            List<String> headList = Arrays.asList(
                    "质检单号",
                    "收货单号",
                    "所属仓库名称",
                    "质检人",
                    "质检时间",
                    "质检状态",
                    "创建人",
                    "创建时间" );
            List<LinkedHashMap<String, Object>> dataList = qualityInspectionService.queryQualityInspectionForExcel(queryQualityInspectionForm,req);
            ExcelUtils.exportExcel(headList, dataList, "质检检测", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }


    @ApiOperation("质量检测部分下拉值")
    @GetMapping(value = "/getQualityInspectionStatusType")
    public BaseResult getReceivingOrderStatusType() {
        //收货单订单状态下拉值
        List<LinkedHashMap<String, Object>> theQualityStatus = authService.queryDictByDictType("theQualityStatus");
        List<LinkedHashMap<String, Object>> causeOfUnqualifiedInspection = authService.queryDictByDictType("causeOfUnqualifiedInspection");
        theQualityStatus.stream().forEach(ro->{
            ro.put("value",Integer.parseInt(ro.get("value").toString()));
        });
        Map<String, Object> map = new HashMap<>();
        map.put("theQualityStatus", theQualityStatus);
        map.put("causeOfUnqualifiedInspection", causeOfUnqualifiedInspection);
        return BaseResult.ok(map);
    }




    @ApiOperation("APP调用质量检测部分下拉值")
    @GetMapping(value = "/client/getCauseOfUnqualifiedInspection")
    public BaseResult getCauseOfUnqualifiedInspection() {
        List<LinkedHashMap<String, Object>> causeOfUnqualifiedInspection = authService.queryDictByDictType("causeOfUnqualifiedInspection");

        Map<String, Object> map = new HashMap<>();
        map.put("causeOfUnqualifiedInspection", causeOfUnqualifiedInspection);
        return BaseResult.ok(map);
    }



// 根据质检单号和物料单号查询校验质检信息/
    @ApiOperation("app根据质检单号和物料单号查询校验质检信息")
    @PostMapping(value = "/client/checkoutQualityInspectionMaterial")
    public BaseResult  checkoutQualityInspectionMaterial(@RequestBody QueryClientQualityInspectionForm form) {

        QueryQualityInspectionMaterialForm queryQualityInspectionMaterialForm = new QueryQualityInspectionMaterialForm();
        queryQualityInspectionMaterialForm.setQcNo(form.getQcNo());//质检单号
        queryQualityInspectionMaterialForm.setMaterialCode(form.getMaterialCode()); //物料编号

        QualityInspectionMaterial qualityInspectionMaterialOne = qualityInspectionMaterialService.findQualityInspectionMaterialOne(queryQualityInspectionMaterialForm);

        if(qualityInspectionMaterialOne==null){
            return BaseResult.error(SysTips.NOT_THIS_ORDER_MATERIALS_INFORMATION);
        }
        return BaseResult.ok(qualityInspectionMaterialOne);
    }

    //根据质检单号 物料编号      查询质检物料信息

    @ApiOperation("app根据质检单号和物料单号修改质检物料信息")
    @PostMapping(value = "/client/updateQualityInspectionMaterial")
    public BaseResult  updateQualityInspectionMaterial(@RequestBody QueryClientQualityInspectionForm form) {

        QueryQualityInspectionMaterialForm queryQualityInspectionMaterialFormOne = new QueryQualityInspectionMaterialForm();
        queryQualityInspectionMaterialFormOne.setQcNo(form.getQcNo());//质检单号
        queryQualityInspectionMaterialFormOne.setMaterialCode(form.getMaterialCode()); //物料编号
        QualityInspectionMaterial qualityInspectionMaterialOne = qualityInspectionMaterialService.findQualityInspectionMaterialOne(queryQualityInspectionMaterialFormOne);
        if(qualityInspectionMaterialOne==null){
            return BaseResult.error(SysTips.NOT_THIS_ORDER_MATERIALS_INFORMATION);
        }

        //app修改质检单的物料信息
        QueryQualityInspectionMaterialForm queryQualityInspectionMaterialForm = new QueryQualityInspectionMaterialForm();
        queryQualityInspectionMaterialForm.setActualNum(qualityInspectionMaterialOne.getActualNum());// 实收数量
        queryQualityInspectionMaterialForm.setQcNo(form.getQcNo());//质检单号
        queryQualityInspectionMaterialForm.setMaterialCode(form.getMaterialCode()); //物料编号
        queryQualityInspectionMaterialForm.setInspectionQuantity(form.getInspectionQuantity());//检验数量
        queryQualityInspectionMaterialForm.setQualifiedQuantity(form.getQualifiedQuantity()); //合格数量
        queryQualityInspectionMaterialForm.setUnqualifiedQuantity(form.getUnqualifiedQuantity());//不合格数量
        queryQualityInspectionMaterialForm.setCauseNonconformity(form.getCauseNonconformity());//不合格原因(字典值)
        queryQualityInspectionMaterialForm.setDescription(StringUtils.join(form.getFileList(),StrUtil.C_COMMA));  //后面可能还有图片字符串

         qualityInspectionService.copyQualityInspectionMaterial(queryQualityInspectionMaterialForm);


        return BaseResult.ok();
    }


    @ApiOperation("保存质检数据")
    @PostMapping(value = "/saveDetail")
    public BaseResult saveDetail(@RequestBody QualityInspectionVO qualityInspectionVO){
        return qualityInspectionService.saveDetail(qualityInspectionVO);
    }

    @ApiOperation("删除质检集合")
    @PostMapping(value = "/deleteIds")
    public BaseResult deleteIds(@RequestBody DeleteForm deleteForm){
        return qualityInspectionService.deleteIds(deleteForm);
    }








}
