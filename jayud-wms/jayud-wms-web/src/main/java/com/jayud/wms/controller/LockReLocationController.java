package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.LockReLocation;
import com.jayud.wms.service.ILockReLocationService;
import io.swagger.annotations.Api;
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
 * 推荐库位锁定 控制类
 *
 * @author jyd
 * @since 2022-01-17
 */
@Api(tags = "推荐库位锁定")
@RestController
@RequestMapping("/lockReLocation")
public class LockReLocationController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ILockReLocationService lockReLocationService;

    /**
     * 分页查询数据
     *
     * @param lockReLocation   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<LockReLocation>>> selectPage(LockReLocation lockReLocation,
                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(lockReLocationService.selectPage(lockReLocation,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param lockReLocation   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<LockReLocation>> selectList(LockReLocation lockReLocation,
                                                HttpServletRequest req) {
        return BaseResult.ok(lockReLocationService.selectList(lockReLocation));
    }

    /**
    * 新增
    * @param lockReLocation
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody LockReLocation lockReLocation ){
        lockReLocationService.save(lockReLocation);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param lockReLocation
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody LockReLocation lockReLocation ){
        lockReLocationService.updateById(lockReLocation);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
    * 保存(新增+编辑)
    * @param lockReLocation
    **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody LockReLocation lockReLocation ){
        LockReLocation lockReLocation1 = lockReLocationService.saveOrUpdateLockReLocation(lockReLocation);
        return BaseResult.ok(lockReLocation1);
    }

    /**
     * 物理删除
     * @param id
     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
//    @GetMapping("/del")
//    public BaseResult del(@RequestParam int id){
//        lockReLocationService.removeById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * 逻辑删除
     * @param id
     **/
//    @ApiOperation("逻辑删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
//    @GetMapping("/logicDel")
//    public BaseResult logicDel(@RequestParam int id){
//        lockReLocationService.delLockReLocation(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
    * 根据id查询
    * @param id
    */
//    @ApiOperation("根据id查询")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
//    @GetMapping(value = "/queryById")
//    public BaseResult<LockReLocation> queryById(@RequestParam(name="id",required=true) int id) {
//        LockReLocation lockReLocation = lockReLocationService.getById(id);
//        return BaseResult.ok(lockReLocation);
//    }

    /**
     * 根据查询条件导出推荐库位锁定
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出推荐库位锁定")
    @PostMapping(path = "/exportLockReLocation")
    public void exportLockReLocation(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键",
                "所属仓库id",
                "所属仓库库区id",
                "库位编号",
                "是否删除"
            );
            List<LinkedHashMap<String, Object>> dataList = lockReLocationService.queryLockReLocationForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "推荐库位锁定", response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.toString());
        }
    }

}
