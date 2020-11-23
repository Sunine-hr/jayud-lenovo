package com.jayud.mall.model.bo;

import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.model.vo.OrderShopVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderInfoForm {

    @ApiModelProperty(value = "订单ID，由系统生成")
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "客户ID，对应customer.id")
    private Integer customerId;

    @ApiModelProperty(value = "集货仓库代码 warehouse.code")
    private String storeGoodsWarehouseCode;

    @ApiModelProperty(value = "集货仓库名称 warehouse.name")
    private String storeGoodsWarehouseName;

    @ApiModelProperty(value = "目的仓库代码  warehouse.code")
    private String destinationWarehouseCode;

    @ApiModelProperty(value = "目的仓库名称 warehouse.name")
    private String destinationWarehouseName;

    @ApiModelProperty(value = "是否上门提货(0否 1是,shop_pick)")
    private Integer isPick;

    @ApiModelProperty(value = "状态码枚举: -1,0,10,20,30,40,50枚举备注: -1 已取消 查看详情 0 草稿-----提交、取消、查看订单详情（后台不记录数据） 10 已下单：编辑、查看订单详情 20 已收货：编辑、查看订单详情 30 订单确认：确认计柜重（不可修改订单信息） 40 转运中：查看订单详情 50 已签收：账单确认、查看订单详情")
    private Integer status;

    @ApiModelProperty(value = "状态名称")
    private String statusName;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人ID")
    private Integer createUserId;

    @ApiModelProperty(value = "创建人名称")
    private String createUserName;

    @ApiModelProperty(value = "订单来源，默认为1，web端；")
    private String orderOrigin;

    @ApiModelProperty(value = "提单号，根据配载单关联规则生成")
    private Integer bolNo;

    @ApiModelProperty(value = "是否需要报关0-否，1-是 (订单对应报关文件:order_customs_file)")
    private Integer needDeclare;

    @ApiModelProperty(value = "是否需要清关0-否，1-是 (订单对应清关文件:order_clearance_file)")
    private Integer needClearance;

    @ApiModelProperty(value = "备注")
    private String remark;

    /*订单对应箱号信息:order_case*/
    private List<OrderCaseVO> orderCaseVOList;

    /*订单对应商品：order_shop*/
    private List<OrderShopVO> orderShopVOList;

    /*订单对应提货信息表：order_pick*/
    private List<OrderPickVO> orderPickVOList;




}
