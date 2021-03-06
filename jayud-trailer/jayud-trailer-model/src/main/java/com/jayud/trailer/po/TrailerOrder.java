package com.jayud.trailer.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 拖车订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TrailerOrder对象", description="拖车订单表")
public class TrailerOrder extends Model<TrailerOrder> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "拖车订单编号")
    private String orderNo;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "状态(TRAILER_0待接单,TRAILER_1拖车接单,TRAILER_2拖车派车,TRAILER_3拖车提柜,TRAILER_4拖车到仓,TRAILER_5拖车离仓,TRAILER_6拖车过磅,TRAILER_7确认还柜)")
    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "结算单位姓名")
    private String unitCodeName;

    @ApiModelProperty(value = "起运港/目的港代码")
    private String portCode;

    @ApiModelProperty(value = "车型尺寸id")
    private Long cabinetSize;

    @ApiModelProperty(value = "提运单")
    private String billOfLading;

    @ApiModelProperty(value = "提运单附件路径")
    private String bolFilePath;

    @ApiModelProperty(value = "提运单附件名称")
    private String bolFileName;

    @ApiModelProperty(value = "封条")
    private String paperStripSeal;

    @ApiModelProperty(value = "封条附件路径")
    private String pssFilePath;

    @ApiModelProperty(value = "封条附件名称")
    private String pssFileName;

    @ApiModelProperty(value = "柜号")
    private String cabinetNumber;

    @ApiModelProperty(value = "柜号附件路径")
    private String cnFilePath;

    @ApiModelProperty(value = "柜号附件名称")
    private String cnFileName;

    @ApiModelProperty(value = "SO")
    private String so;

    @ApiModelProperty(value = "SO附件路径")
    private String soFilePath;

    @ApiModelProperty(value = "SO附件名称")
    private String soFileName;

    @ApiModelProperty(value = "到港时间")
    private LocalDateTime arrivalTime;

    @ApiModelProperty(value = "截仓期时间")
    private LocalDateTime closingWarehouseTime;

    @ApiModelProperty(value = "截柜租时间")
    private LocalDateTime timeCounterRent;

    @ApiModelProperty(value = "开仓时间")
    private LocalDateTime openTime;

    @ApiModelProperty(value = "截补料时间")
    private LocalDateTime cuttingReplenishingTime;

    @ApiModelProperty(value = "截关时间")
    private LocalDateTime closingTime;

    @ApiModelProperty(value = "放行时间")
    private LocalDateTime releaseTime;

    @ApiModelProperty(value = "是否过磅(1代表true,0代表false)")
    private Boolean isWeighed;

    @ApiModelProperty(value = "是否做补料(1代表true,0代表false)")
    private Boolean isMakeUp;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否需要录入费用(0:false,1:true)")
    private Boolean needInputCost;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "流程描述")
    private String processDescription;

    @ApiModelProperty(value = "接单日期")
    private LocalDateTime receivingOrdersDate;

    @ApiModelProperty(value = "是否资料齐全")
    private Boolean isInfoComplete;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
