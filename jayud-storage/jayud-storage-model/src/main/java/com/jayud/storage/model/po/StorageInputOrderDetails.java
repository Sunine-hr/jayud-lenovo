package com.jayud.storage.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 入库订单详情表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="StorageInputOrderDetails对象", description="入库订单详情表")
public class StorageInputOrderDetails extends Model<StorageInputOrderDetails> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "入仓操作id（可多选，以逗号隔开）")
    private String operationId;

    @ApiModelProperty(value = "卡板类型id（可多选，以逗号隔开）")
    @TableField("cardType_id")
    private String cardtypeId;

    @ApiModelProperty(value = "YES(1为是，2为否)")
    @TableField("YES")
    private Integer yes;

    @ApiModelProperty(value = "NO(1为是，2为否)")
    @TableField("NO")
    private Integer no;

    @ApiModelProperty(value = "已影相(1为是，2为否)")
    private Integer isGone;

    @ApiModelProperty(value = "(不可叠放)指示(1为是，2为否)")
    private Integer isInstructions;

    @ApiModelProperty(value = "数量1")
    private Integer num1;

    @ApiModelProperty(value = "上门收件(1为是，2为否)")
    private Integer isDoorCollection;

    @ApiModelProperty(value = "客户自送(1为是，2为否)")
    private Integer isSelfDelivery;

    @ApiModelProperty(value = "已贴Gold Labels(1为是，2为否)")
    private Integer isGoldLabels;

    @ApiModelProperty(value = "包装不当(1为是，2为否)")
    private Integer isImproperPacking;

    @ApiModelProperty(value = "数量2")
    private Integer num2;

    @ApiModelProperty(value = "Tom Open 已开口(1为是，2为否)")
    private Integer isTomOpen;

    @ApiModelProperty(value = "Tom Open 已开口的数量")
    private Integer tomOpenNumber;

    @ApiModelProperty(value = "Re Taped 重贴胶纸(1为是，2为否)")
    private Integer isReTaped;

    @ApiModelProperty(value = "Crushed Collapsed 已压破/摺曲(1为是，2为否)")
    private Integer isCrushedCollapsed;

    @ApiModelProperty(value = "Crushed Collapsed 已压破/摺曲的数量")
    private Integer crushedCollapsedNumber;

    @ApiModelProperty(value = "Re Taped 重贴胶纸的数量")
    private Integer reTapedNumber;

    @ApiModelProperty(value = "Water Greased 有水渍/油渍(1为是，2为否)")
    private Integer isWaterGreased;

    @ApiModelProperty(value = "Water Greased 有水渍/油渍的数量")
    private Integer waterGreasedNumber;

    @ApiModelProperty(value = "Punctured/Holes 外箱破损/有洞(1为是，2为否)")
    private Integer isPuncturedHoles;

    @ApiModelProperty(value = "Punctured/Holes 外箱破损/有洞的数量")
    private Integer puncturedHolesNumber;

    @ApiModelProperty(value = "Damaged Ctn.No.PCS(1为是，2为否)")
    private Integer isDamagedCtn;

    @ApiModelProperty(value = "Damaged Ctn.No.PCS的数量")
    private Integer damagedCtnNumber;

    @ApiModelProperty(value = "备注")
    private String beizhu;

    @ApiModelProperty(value = "Remarks")
    @TableField("Remarks")
    private String remarks;

    @ApiModelProperty(value = "Marks")
    @TableField("Marks")
    private String marks;

    @ApiModelProperty(value = "单证")
    private String documents;

    @ApiModelProperty(value = "仓管")
    private String warehouseManagement;

    @ApiModelProperty(value = "司机")
    private String driver;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "仓库id")
    private String warehouseId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
