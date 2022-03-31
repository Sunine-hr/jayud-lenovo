package com.jayud.crm.controller;

import com.jayud.auth.model.po.SysDictItem;
import com.jayud.crm.feign.SysDictClient;
import com.jayud.crm.model.bo.DeleteForm;
import com.jayud.crm.model.bo.QueryCrmFile;
import com.jayud.crm.model.constant.CrmDictCode;
import com.jayud.crm.model.vo.CrmFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmFileService;
import com.jayud.crm.model.po.CrmFile;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基本档案_文件(crm_file) 控制类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Api(tags = "基本档案_文件(crm_file)")
@RestController
@RequestMapping("/crmFile")
public class CrmFileController {



    @Autowired
    public ICrmFileService crmFileService;


    @Autowired
    private SysDictClient sysDictClient;
    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmFile
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmFile>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmFile>> selectPage(CrmFile crmFile,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(crmFileService.selectPage(crmFile,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-02
    * @param: crmFile
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmFile>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmFile>> selectList(CrmFile crmFile,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmFileService.selectList(crmFile));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-02
    * @param: crmFile
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@RequestBody QueryCrmFile queryCrmFile ){
        crmFileService.saveOrUpdateCrmFile(queryCrmFile);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

//
//    /**
//     * @description 编辑
//     * @author  jayud
//     * @date   2022-03-02
//     * @param: crmFile
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody CrmFile crmFile ){
//        crmFileService.updateById(crmFile);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }



//    /**
//     * @description 物理删除
//     * @author  jayud
//     * @date   2022-03-02
//     * @param: id
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
//    @GetMapping("/phyDel")
//    public BaseResult phyDel(@RequestParam Long id){
//        crmFileService.phyDelById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @PostMapping("/logicDel")
    public BaseResult logicDel(@RequestBody DeleteForm ids){
        crmFileService.logicDel(ids.getIds());
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmFile>
     **/
    @ApiOperation("根据id查询")
    @GetMapping(value = "/queryById")
    public BaseResult<CrmFileVO> queryById(@RequestParam(name="id",required=true) Long id) {
        CrmFileVO crmFileVO = crmFileService.findCrmFileById(id);
        return BaseResult.ok(crmFileVO);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-02
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出基本档案_文件(crm_file)")
    @PostMapping(path = "/exportCrmFile")
    public void exportCrmFile(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "业务标识code",
                "业务主键",
                "附件类型",
                "文件名称",
                "文件上传url",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmFileService.queryCrmFileForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_文件(crm_file)", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }

    /**
     * @description 获取客户信息字典项目
     * @author  ciro
     * @date   2022/3/2 11:07
     * @param:
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.bo.CrmCodeFrom>
     **/
    @ApiOperation("获取客户管理附件类型字典下拉")
    @GetMapping(path = "/getCrmFileCode")
    public BaseResult  getCrmCode(){
        BaseResult<List<SysDictItem>> custNormalStatus= sysDictClient.selectItemByDictCode(CrmDictCode.CRM_FILE_TYPE);
        return BaseResult.ok(custNormalStatus.getResult());
    }


    @ApiOperation("新增附件")
    @PostMapping(path = "/addFile")
    public BaseResult addFile(@RequestParam("crmFiles")List<CrmFile> crmFiles,@RequestParam("business")Long business,@RequestParam("code")String code){
        this.crmFileService.doFileProcessing(crmFiles,business,code);
        return BaseResult.ok();
    }

    @ApiOperation("获取附件集合")
    @PostMapping(path = "/getFileList")
    public BaseResult<List<CrmFile>> getFileList(@RequestParam("business")Long business,@RequestParam("code")String code){
        List<CrmFile> crmFiles = this.crmFileService.getFiles(business,code);
        return BaseResult.ok(crmFiles);
    }


}
