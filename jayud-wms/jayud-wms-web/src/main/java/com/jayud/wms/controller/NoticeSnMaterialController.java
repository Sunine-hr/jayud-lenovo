package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.NoticeSnMaterial;
import com.jayud.wms.service.INoticeSnMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知单物料sn信息 控制类
 *
 * @author jyd
 * @since 2021-12-18
 */
@Api(tags = "通知单物料sn信息")
@RestController
@RequestMapping("/noticeSnMaterial")
public class NoticeSnMaterialController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public INoticeSnMaterialService noticeSnMaterialService;

    /**
     * 分页查询数据
     *
     * @param noticeSnMaterial   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<NoticeSnMaterial>>> selectPage(NoticeSnMaterial noticeSnMaterial,
                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(noticeSnMaterialService.selectPage(noticeSnMaterial,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param noticeSnMaterial   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<NoticeSnMaterial>> selectList(NoticeSnMaterial noticeSnMaterial,
                                                HttpServletRequest req) {
        return BaseResult.ok(noticeSnMaterialService.selectList(noticeSnMaterial));
    }

    /**
    * 新增
    * @param noticeSnMaterial
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody NoticeSnMaterial noticeSnMaterial ){
        noticeSnMaterialService.save(noticeSnMaterial);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param noticeSnMaterial
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody NoticeSnMaterial noticeSnMaterial ){
        noticeSnMaterialService.updateById(noticeSnMaterial);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

//    /**
//    * 保存(新增+编辑)
//    * @param noticeSnMaterial
//    **/
//    @ApiOperation("保存(新增+编辑)")
//    @PostMapping("/save")
//    public BaseResult save(@Valid @RequestBody NoticeSnMaterial noticeSnMaterial ){
//        NoticeSnMaterial noticeSnMaterial1 = noticeSnMaterialService.saveOrUpdateNoticeSnMaterial(noticeSnMaterial);
//        return BaseResult.ok(noticeSnMaterial1);
//    }

    /**
     * 物理删除
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id){
        noticeSnMaterialService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 逻辑删除
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam int id){
        noticeSnMaterialService.delNoticeSnMaterial(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<NoticeSnMaterial> queryById(@RequestParam(name="id",required=true) int id) {
        NoticeSnMaterial noticeSnMaterial = noticeSnMaterialService.getById(id);
        return BaseResult.ok(noticeSnMaterial);
    }

    /**
     * 根据查询条件导出通知单物料sn信息
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出通知单物料sn信息")
    @PostMapping(path = "/exportNoticeSnMaterial")
    public void exportNoticeSnMaterial(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "通知单id",
                "通知单号",
                "物料编号",
                "物料序列号(SN)",
                "物料名称",
                "物料类型id",
                "物料类型",
                "重量",
                "体积",
                "物料规格",
                "数量",
                "单位",
                "外部订单号",
                "批次号",
                "生产日期",
                "自定义字段1",
                "自定义字段2",
                "自定义字段3",
                "租户编码",
                "备注信息",
                "是否删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = noticeSnMaterialService.queryNoticeSnMaterialForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "通知单物料sn信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

}
