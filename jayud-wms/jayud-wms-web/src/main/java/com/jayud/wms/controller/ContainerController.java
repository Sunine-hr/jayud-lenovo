package com.jayud.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.aop.annotations.SysLog;
import com.jayud.wms.model.bo.ContainerForm;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.po.Container;
import com.jayud.wms.model.po.Receipt;
import com.jayud.wms.service.AuthService;
import com.jayud.wms.service.IContainerService;
import com.jayud.wms.service.IReceiptService;
import com.jayud.wms.model.vo.ContainerVO;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * 容器信息 控制类
 *
 * @author jyd
 * @since 2021-12-13
 */
@Api(tags = "容器信息")
@RestController
@RequestMapping("/container")
public class ContainerController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    public IContainerService containerService;

    @Autowired
    private AuthService authService;

    @Autowired
    public IReceiptService receiptService;

    /**
     * 分页查询数据
     *
     * @param containerForm 查询条件
     * @return
     */
    @SysDataPermission(clazz = ContainerForm.class)
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<ContainerVO>> selectPage(ContainerForm containerForm,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {

        return BaseResult.ok(containerService.selectPage(containerForm, pageNo, pageSize, req));
    }

    /**
     * 列表查询数据
     *
     * @param containerForm 查询条件
     * @return
     */
    @SysDataPermission(clazz = ContainerForm.class)
    @SysLog(value = "容器信息-列表查询数据")
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<ContainerVO>> selectList(ContainerForm containerForm,
                                                HttpServletRequest req) {
        return BaseResult.ok(containerService.selectList(containerForm));
    }

    /**
     * 新增
     *
     * @param containerForm
     **/
    @ApiOperation("新增")
    @PostMapping("/saveOrUpdate")
    public BaseResult add(@Valid @RequestBody ContainerForm containerForm) {

        if (containerForm.getId() == null) {
            ContainerForm containerForm1 = containerService.getContainerOne(containerForm.getCode(), null, null, containerForm.getWarehouseId());
            if (containerForm1 != null) {
                return BaseResult.error("容器编号已存在");
            }
        }

        boolean wmsOwerInfo = containerService.saveOrUpdateContainerForm(containerForm);

        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 启用/禁用客户
     *
     * @param
     **/
    @ApiOperation(value = "启用/禁用客户地址,或容器")
    @PostMapping(value = "/enableOrDisable")
    public BaseResult enableOrDisable(@RequestBody ContainerForm containerForm) {
        if (containerForm.getId() == null) {
            return BaseResult.error("id不为空");
        }
        Container container = new Container();
        container.setId(containerForm.getId());
        container.setStatus(containerForm.getStatus());

        containerService.updateById(container);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


//    /**
//     * 删除
//     * @param  id
//     **/
//    @ApiOperation("删除多个")
//    @PostMapping("/deleteWmsCustomerInfoList")
//    public BaseResult deleteWmsCustomerInfoList(@RequestBody List<Long> id){

    /**
     * 删除
     *
     * @param ids
     **/
    @ApiOperation("删除")
    @PostMapping(value = "/delContainer")
    public BaseResult delContainer(@RequestBody DeleteForm ids) {
        if (ids.getIds().size() == 0) {
            return BaseResult.error("id不为空");
        }
        for (int i = 0; i < ids.getIds().size(); i++) {
            Container container = new Container();
            container.setId(ids.getIds().get(i));
            container.setIsDeleted(true);

            containerService.updateById(container);
        }

        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @GetMapping(value = "/queryById")
    public BaseResult queryById(@RequestParam(name = "id", required = true) Long id) {

        ContainerForm containerForm1 = containerService.getContainerOne(null, id, null, null);


        return BaseResult.ok(containerForm1);
    }

    /**
     * 根据查询条件导出容器信息
     *
     * @param response      响应对象
     * @param containerForm 参数Map
     */
    @SysDataPermission(clazz = ContainerForm.class)
    @ApiOperation("根据查询条件导出容器信息")
    @GetMapping("/exportContainerLocation")
    public void exportContainerLocation(HttpServletResponse response, ContainerForm containerForm,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req) {
        try {
            List<String> headList = Arrays.asList("容器编号", "容器类型", "所属仓库", "长*宽*高", "备注", "是否可用", "创建人", "创建时间");
            List<LinkedHashMap<String, Object>> dataList = containerService.queryContainerForExcel(containerForm, pageNo, pageSize, req);
            ExcelUtils.exportExcel(headList, dataList, "容器信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    // bases 项目使用
    @ApiOperation("容器类型下拉值")
    @GetMapping(value = "/getByDataClientType")
    public BaseResult getByData() {
        List<LinkedHashMap<String, Object>> containerType = authService.queryDictByDictType("containerType");

        Map<String, Object> map = new HashMap<>();
        map.put("containerType", containerType);
        return BaseResult.ok(map);
    }


    /**
     * 根据收货单id查询 仓库id  根据仓库id  和容器编码 去查询 容器类型
     *
     * @param id
     */
    @ApiOperation("仓库id和容器编码去查询容器类型")
    @GetMapping(value = "/selectContainerWarehouseIdCodeOne")
    public BaseResult selectContainerWarehouseIdCodeOne(@RequestParam(name = "id", required = true) Long id, @RequestParam(name = "code", required = true) String code) {

        Receipt byId = receiptService.getById(id);
        QueryWrapper<Container> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Container::getCode, code)
                .eq(Container::getWarehouseId, byId.getWarehouseId())
                .eq(Container::getStatus, true)  // true 启用
                .eq(Container::getIsDeleted, false);  //  false 0 否  是否删除
        Container one = containerService.getOne(queryWrapper);
        if (one == null) {
            return BaseResult.error(SysTips.CONTAINER_DOES_NOT_EXIST);
        }
        return BaseResult.ok(one);
    }


    /**
     * app外部调用根据收货单id查询 仓库id  根据仓库id  和容器编码 去查询 容器类型
     *
     * @param id
     */
    @ApiOperation("app外部调用仓库id和容器编码去查询容器类型")
    @GetMapping(value = "/client/selectContainerWarehouseIdCodeOne")
    public BaseResult selectContainerWarehouseIdCodeOneFeign(@RequestParam(name = "id", required = true) Long id, @RequestParam(name = "code", required = true) String code) {

        Receipt byId = receiptService.getById(id);

        QueryWrapper<Container> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Container::getCode, code)
                .eq(Container::getWarehouseId, byId.getWarehouseId())
                .eq(Container::getStatus, true) // true 启用
                .eq(Container::getIsDeleted, false);  // false 0 否  是否删除
        Container one = containerService.getOne(queryWrapper);
        if (one == null) {
            return BaseResult.error(SysTips.CONTAINER_DOES_NOT_EXIST);
        }
        return BaseResult.ok(one);
    }
}
