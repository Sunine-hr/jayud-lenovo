package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.Query;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.StateFlagEnum;
import com.jayud.scm.model.enums.TableEnum;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.po.SystemRole;
import com.jayud.scm.model.po.SystemRoleAction;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.BookingOrderEntryVO;
import com.jayud.scm.model.vo.BookingOrderFollowVO;
import com.jayud.scm.model.vo.BookingOrderFormVO;
import com.jayud.scm.model.vo.BookingOrderVO;
import com.jayud.scm.service.*;
import com.jayud.scm.utils.ExcelTemplateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.velocity.util.ArrayListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托订单主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Api(tags = "委托订单")
@RestController
@RequestMapping("/bookingOrder")
public class BookingOrderController {

    @Autowired
    IBookingOrderService bookingOrderService;//委托订单主表
    @Autowired
    IBookingOrderEntryService bookingOrderEntryService;//委托订单明细表
    @Autowired
    IBookingOrderFollowService bookingOrderFollowService;//委托单跟踪记录表

    @Autowired
    ISystemUserService systemUserService;//后台用户表
    @Autowired
    ISystemUserRoleRelationService systemUserRoleRelationService;//服务类
    @Autowired
    ISystemRoleActionService systemRoleActionService;//角色权限设置（按钮）

    /*
        TODO 出口委托单：主表 查询 新增 修改 删除 查看 审核 反审 打印 复制
    */
    //出口委托单，分页查询
    @ApiOperation(value = "出口委托单，分页查询")
    @PostMapping(value = "/findBookingOrderByPage")
    public CommonResult<CommonPageResult<BookingOrderVO>> findBookingOrderByPage(@Valid @RequestBody QueryBookingOrderForm form){
        IPage<BookingOrderVO> page = bookingOrderService.findBookingOrderByPage(form);
        CommonPageResult<BookingOrderVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    //出口委托单，准备新增数据
    @ApiOperation(value = "出口委托单，准备新增数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="modelType", dataType = "Integer", value = "业务类型/工作单类型 1进口  2出口 3国内 4香港  5采购  6销售", required = true)
    })
    @PostMapping(value = "/prepareBookingOrder")
    public CommonResult<BookingOrderVO> prepareBookingOrder(@RequestBody Map<String,Object> map){
        Integer modelType = MapUtil.getInt(map, "modelType");
        if(ObjectUtil.isEmpty(modelType)){
            return CommonResult.error(-1,"工作单类型，不能为空");
        }
        BookingOrderVO bookingOrderVO = bookingOrderService.prepareBookingOrder(modelType);
        return CommonResult.success(bookingOrderVO);
    }


    //出口委托单，保存(新增、修改)
    @ApiOperation(value = "出口委托单，保存(新增、修改)")
    @PostMapping(value = "/saveBookingOrder")
    public CommonResult saveBookingOrder(@Valid @RequestBody BookingOrderForm form){
        bookingOrderService.saveBookingOrder(form);
        return CommonResult.success("保存成功!");
    }

    //出口委托单，删除
    @ApiOperation(value = "出口委托单，删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", dataType = "Integer", value = "自动id", required = true)
    })
    @PostMapping(value = "/delBookingOrder")
    public CommonResult delBookingOrder(@RequestBody Map<String,Object> map){
        Integer id = MapUtil.getInt(map, "id");
        if(ObjectUtil.isEmpty(id)){
            return CommonResult.error(-1,"id不能为空");
        }
        bookingOrderService.delBookingOrder(id);
        return CommonResult.success("操作成功!");
    }

    //出口委托单，查看
    @ApiOperation(value = "出口委托单，查看")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", dataType = "Integer", value = "自动ID", required = true)
    })
    @PostMapping(value = "/getBookingOrderById")
    public CommonResult<BookingOrderVO> getBookingOrderById(@RequestBody Map<String,Object> map){
        Integer id = MapUtil.getInt(map, "id");
        if(ObjectUtil.isEmpty(id)){
            return CommonResult.error(-1,"id不能为空");
        }
        BookingOrderVO bookingOrderVO = bookingOrderService.getBookingOrderById(id);
        return CommonResult.success(bookingOrderVO);

    }

    //出口委托单，审核
    @ApiOperation(value = "出口委托单，审核")
    @PostMapping(value = "/auditBookingOrder")
    public CommonResult auditBookingOrder(@Valid @RequestBody PermissionForm form){
        //获取登录用户
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        if(!systemUser.getUserName().equalsIgnoreCase("Admin")){
            //获取登录用户所属角色
            List<SystemRole> enabledRolesByUserId = systemUserRoleRelationService.getEnabledRolesByUserId(systemUser.getId());
            for (SystemRole systemRole : enabledRolesByUserId) {
                SystemRoleAction systemRoleAction = systemRoleActionService.getSystemRoleActionByRoleIdAndActionCode(systemRole.getId(),form.getActionCode());
                if(systemRoleAction == null){
                    return CommonResult.error(444,"该用户没有该按钮权限");
                }
            }
        }
        //拥有按钮权限，判断是否为审核按钮
        if(!form.getType().equals(0)){
            form.setTable(TableEnum.getDesc(form.getKey()));
            form.setUserId(systemUser.getId().intValue());
            form.setUserName(systemUser.getUserName());
            if(form.getType().equals(1)){//审核
                bookingOrderService.auditBookingOrder(form);
            }
        }
        return CommonResult.success("操作成功!");
    }


    //出口委托单，反审
    @ApiOperation(value = "出口委托单，反审")
    @PostMapping(value = "/cancelAuditBookingOrder")
    public CommonResult cancelAuditBookingOrder(@Valid @RequestBody PermissionForm form){
        //获取登录用户
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        if(!systemUser.getUserName().equalsIgnoreCase("Admin")){
            //获取登录用户所属角色
            List<SystemRole> enabledRolesByUserId = systemUserRoleRelationService.getEnabledRolesByUserId(systemUser.getId());
            for (SystemRole systemRole : enabledRolesByUserId) {
                SystemRoleAction systemRoleAction = systemRoleActionService.getSystemRoleActionByRoleIdAndActionCode(systemRole.getId(),form.getActionCode());
                if(systemRoleAction == null){
                    return CommonResult.error(444,"该用户没有该按钮权限");
                }
            }
        }
        //拥有按钮权限，判断是否为审核按钮
        if(!form.getType().equals(0)){
            form.setTable(TableEnum.getDesc(form.getKey()));
            form.setUserId(systemUser.getId().intValue());
            form.setUserName(systemUser.getUserName());
            if(form.getType().equals(2)){
                bookingOrderService.cancelAuditBookingOrder(form);
            }
        }
        return CommonResult.success("操作成功!");
    }


    //出口委托单，打印
    @ApiOperation(value = "出口委托单，打印")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", dataType = "Integer", value = "自动ID", required = true)
    })
    @PostMapping(value = "/printBookingOrder")
    public CommonResult printBookingOrder(@RequestBody Map<String,Object> map){
        Integer id = MapUtil.getInt(map, "id");
        if(ObjectUtil.isEmpty(id)){
            return CommonResult.error(-1,"id不能为空");
        }
        bookingOrderService.printBookingOrder(id);
        return CommonResult.success("操作成功!");
    }

    //出口委托单，复制
    @ApiOperation(value = "出口委托单，复制")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", dataType = "Integer", value = "自动ID", required = true)
    })
    @PostMapping(value = "/copyBookingOrder")
    public CommonResult<BookingOrderVO> copyBookingOrder(@RequestBody Map<String,Object> map){
        Integer id = MapUtil.getInt(map, "id");
        if(ObjectUtil.isEmpty(id)){
            return CommonResult.error(-1,"id不能为空");
        }
        BookingOrderVO bookingOrderVO = bookingOrderService.copyBookingOrder(id);
        return CommonResult.success(bookingOrderVO);
    }

    @ApiOperation(value = "出口委托单，获取状态list")
    @GetMapping("/getStateFlagEnumList")
    public CommonResult getStateFlagEnumList(){
        List<Map<String, Object>> stateFlagEnumList = StateFlagEnum.getStateFlagEnumList();
        return CommonResult.success(stateFlagEnumList);
    }

    /*
        TODO 商品明细表： 新增 修改 删除 复制 导入
    */
    //商品明细表，list查询
    @ApiOperation(value = "商品明细表，list查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name="bookingId", dataType = "Integer", value = "委托单id", required = true)
    })
    @PostMapping(value = "/findBookingOrderEntryByBookingId")
    public CommonResult<List<BookingOrderEntryVO>>  findBookingOrderEntryByBookingId(@Valid @RequestBody Map<String,Object> map){
        Integer bookingId = MapUtil.getInt(map, "bookingId");
        if(ObjectUtil.isEmpty(bookingId)){
            return CommonResult.error(-1,"委托单id不能为空");
        }
        List<BookingOrderEntryVO> bookingOrderEntryList = bookingOrderEntryService.findBookingOrderEntryByBookingId(bookingId);
        return CommonResult.success(bookingOrderEntryList);
    }

    //商品明细表，保存(新增、修改)
    @ApiOperation(value = "商品明细表，保存(新增、修改)")
    @PostMapping(value = "/saveBookingOrderEntry")
    public CommonResult saveBookingOrderEntry(@Valid @RequestBody BookingOrderEntryForm form){
        bookingOrderEntryService.saveBookingOrderEntry(form);
        return CommonResult.success("操作成功!");
    }

    //商品明细表，删除
    @ApiOperation(value = "商品明细表，删除")
    @PostMapping(value = "/delBookingOrderEntry")
    public CommonResult delBookingOrderEntry(@RequestBody Map<String,Object> map){
        Integer id = MapUtil.getInt(map, "id");
        if(ObjectUtil.isEmpty(id)){
            return CommonResult.error(-1,"id不能为空");
        }
        bookingOrderEntryService.delBookingOrderEntry(id);
        return CommonResult.success("操作成功!");
    }

    //商品明细表，查看
    @ApiOperation(value = "商品明细表，查看")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", dataType = "Integer", value = "自动ID", required = true)
    })
    @PostMapping(value = "/getBookingOrderEntryById")
    public CommonResult<BookingOrderEntryVO> getBookingOrderEntryById(@RequestBody Map<String,Object> map){
        Integer id = MapUtil.getInt(map, "id");
        if(ObjectUtil.isEmpty(id)){
            return CommonResult.error(-1,"id不能为空");
        }
        BookingOrderEntryVO bookingOrderEntryVO = bookingOrderEntryService.getBookingOrderEntryById(id);
        return CommonResult.success(bookingOrderEntryVO);
    }

    //商品明细表，复制
    @ApiOperation(value = "商品明细表，复制")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", dataType = "Integer", value = "自动ID", required = true)
    })
    @PostMapping(value = "/copyBookingOrderEntry")
    public CommonResult<BookingOrderEntryVO> copyBookingOrderEntry(@RequestBody Map<String,Object> map){
        Integer id = MapUtil.getInt(map, "id");
        if(ObjectUtil.isEmpty(id)){
            return CommonResult.error(-1,"id不能为空");
        }
        BookingOrderEntryVO bookingOrderEntryVO = bookingOrderEntryService.copyBookingOrderEntry(id);
        return CommonResult.success(bookingOrderEntryVO);
    }

    //商品明细表，下载模板
    @ApiOperation(value = "商品明细表，下载模板")
    @GetMapping(value = "/downloadTemplateByBookingOrderEntry")
    public void downloadTemplateByBookingOrderEntry(HttpServletResponse response){
        new ExcelTemplateUtil().downloadExcel(response, "booking_order_entry.xlsx", "出口委托单-商品明细-导入模板.xlsx");
    }

    //商品明细表，导入
    @ApiOperation(value = "商品明细表，导入")
    @PostMapping(value = "/importByBookingOrderEntry")
    public CommonResult importByBookingOrderEntry(@RequestParam("file") MultipartFile file, @RequestHeader(value = "bookingId") Integer bookingId){
        //用HttpServletRequest，获取请求头内容
        if (file.isEmpty()) {
            return CommonResult.error(-1, "文件为空！");
        }
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);
        bookingOrderEntryService.importByBookingOrderEntry(file, bookingId);
        return CommonResult.success("导入成功!");
    }


    /*
        TODO 附件资料:   上传，删除  使用公用方法，不用写，参数要说明清楚
        委托单，附件资料

          `file_model` int DEFAULT NULL COMMENT '附件类型,1:商品库,2:客户主体,3委托订单,4:付款单,5:收款单,6:入库单,7:出库单,8:应收款,9:提验货,10:中港运输',
          `business_id` int DEFAULT NULL COMMENT '业务单据ID',

        1.委托单，附件资料，查询 -> 通过类型和订单id查询所有附件
        bPublicFiles/findPublicFile
        {"fileModel":3, "businessId": ?}

        2.委托单，附件资料，上传 -> 添加附件
        /bPublicFiles/AddPublicFile
        {
        "fileModel":3,
        "fileModelCopy":3,
        "businessId":?,
        "remark":?,
        "fileView":?
        }

        fileView -> 通过 http://test.oms.jayud.com:9448/jayudFile/file/uploadFile ,返回对象，前端有公用的全局配置。

        3.委托单，附件资料，删除 -> 删除通用方法
        /common/delete
        {
        "ids":?, 	//删除记录的ID集合
        "key":3		//上传附件表 b_public_files(3,"b_public_files"),
        }

    */
    //附件资料，上传

    //附件资料，删除

    /*
        TODO 跟踪记录：查询 新增
    */
    //跟踪记录，查询
    @ApiOperation(value = "跟踪记录，查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name="bookingId", dataType = "Integer", value = "委托单ID", required = true)
    })
    @PostMapping(value = "/findBookingOrderFollow")
    public CommonResult<List<BookingOrderFollowVO>> findBookingOrderFollow(@RequestBody Map<String,Object> map){
        Integer bookingId = MapUtil.getInt(map, "bookingId");
        if(ObjectUtil.isEmpty(bookingId)){
            return CommonResult.error(-1,"委托单ID，不能为空");
        }
        List<BookingOrderFollowVO> bookingOrderFollowList = bookingOrderFollowService.findBookingOrderFollow(bookingId);
        return CommonResult.success(bookingOrderFollowList);
    }

    //跟踪记录，新增
    @ApiOperation(value = "跟踪记录，新增")
    @PostMapping(value = "/saveBookingOrderFollow")
    public CommonResult saveBookingOrderFollow(@Valid @RequestBody BookingOrderFollowForm form){
        bookingOrderFollowService.saveBookingOrderFollow(form);
        return CommonResult.success("操作成功");
    }


    @ApiOperation(value = "根据报关单id获取委托单明细信息")
    @PostMapping(value = "/getBookingEntryByBillId")
    public CommonResult getBookingEntryByBillId(@RequestBody QueryCommonForm form) {
        BookingOrder bookingOrder = bookingOrderService.getBookingOrderByBillId(form.getId());
        if(bookingOrder != null){
            List<BookingOrderEntryVO> bookingOrderEntryByBookingId = bookingOrderEntryService.findBookingOrderEntryByBookingId(bookingOrder.getId());
            return CommonResult.success(bookingOrderEntryByBookingId);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据港车id获取委托单信息")
    @PostMapping(value = "/getBookingOrderByHgTrackId")
    public CommonResult getBookingOrderByHgTrackId(@RequestBody QueryCommonForm form) {
        List<BookingOrder> bookingOrders = bookingOrderService.getBookingOrderByHgTrackId(form.getId());

        List<BookingOrderFormVO> bookingOrderFormVOS = new ArrayList<>();
        List<BookingOrderVO> bookingOrderVOS = ConvertUtil.convertList(bookingOrders, BookingOrderVO.class);
        for (BookingOrderVO bookingOrderVO : bookingOrderVOS) {
            List<BookingOrderEntryVO> bookingOrderEntryByBookingId = bookingOrderEntryService.findBookingOrderEntryByBookingId(bookingOrderVO.getId());
            for (BookingOrderEntryVO bookingOrderEntryVO : bookingOrderEntryByBookingId) {
                BookingOrderFormVO bookingOrderFormVO = ConvertUtil.convert(bookingOrderVO,BookingOrderFormVO.class);
                bookingOrderFormVO.setGw(bookingOrderEntryVO.getGw());
                bookingOrderFormVO.setQty(bookingOrderEntryVO.getQty());
                bookingOrderFormVO.setTotalCipPrice(bookingOrderEntryVO.getCipPrice().multiply(bookingOrderEntryVO.getQty()));
                bookingOrderFormVO.setTotalHgMoney(bookingOrderEntryVO.getTotalHgMoney());
                bookingOrderFormVO.setTotalMoney(bookingOrderEntryVO.getTotalMoney());
                bookingOrderFormVO.setPackages(bookingOrderEntryVO.getPackages());
                bookingOrderFormVOS.add(bookingOrderFormVO);
            }
        }
        return CommonResult.success(bookingOrderFormVOS);
    }

    @ApiOperation(value = "出口委托单，分页查询")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<BookingOrderVO>> findByPage(@Valid @RequestBody QueryBookingOrderForm form){
        IPage<BookingOrderVO> page = bookingOrderService.findByPage(form);
        CommonPageResult<BookingOrderVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }


}

