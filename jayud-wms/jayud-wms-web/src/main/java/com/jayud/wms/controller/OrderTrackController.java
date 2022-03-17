package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.OrderTrack;
import com.jayud.wms.service.IOrderTrackService;
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
 * 订单轨迹 控制类
 *
 * @author jyd
 * @since 2021-12-18
 */
@Api(tags = "订单轨迹")
@RestController
@RequestMapping("/orderTrack")
public class OrderTrackController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IOrderTrackService orderTrackService;

    /**
     * 分页查询数据
     *
     * @param orderTrack   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<OrderTrack>>> selectPage(OrderTrack orderTrack,
                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(orderTrackService.selectPage(orderTrack,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param orderTrack   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<OrderTrack>> selectList(OrderTrack orderTrack,
                                                HttpServletRequest req) {
        return BaseResult.ok(orderTrackService.selectList(orderTrack));
    }

    /**
    * 新增
    * @param orderTrack
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody OrderTrack orderTrack ){
        orderTrackService.save(orderTrack);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param orderTrack
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody OrderTrack orderTrack ){
        orderTrackService.updateById(orderTrack);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
    * 保存(新增+编辑)
    * @param orderTrack
    **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody OrderTrack orderTrack ){
        OrderTrack orderTrack1 = orderTrackService.saveOrUpdateOrderTrack(orderTrack);
        return BaseResult.ok(orderTrack1);
    }

    /**
     * 物理删除
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id){
        orderTrackService.removeById(id);
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
        orderTrackService.delOrderTrack(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<OrderTrack> queryById(@RequestParam(name="id",required=true) int id) {
        OrderTrack orderTrack = orderTrackService.getById(id);
        return BaseResult.ok(orderTrack);
    }

    /**
     * 根据查询条件导出订单轨迹
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出订单轨迹")
    @PostMapping(path = "/exportOrderTrack")
    public void exportOrderTrack(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键",
                "关联订单编号",
                "关联订单ID",
                "子订单号",
                "状态码",
                "状态名",
                "业务类型(1:出库,2:入库)",
                "备注信息",
                "是否删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = orderTrackService.queryOrderTrackForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "订单轨迹", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

}
