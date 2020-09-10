package com.jayud.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ContractInfo对象", description="客户合同信息表")
public class ContractInfo extends Model<ContractInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同地址")
    private String contractUrl;

    @ApiModelProperty(value = "业务类型(1-中港 2-报关 3-空海运 4-仓储)")
    private String businessType;

    @ApiModelProperty(value = "法人主体")
    private String legalEntity;

    @ApiModelProperty(value = "合同起期")
    private Date startDate;

    @ApiModelProperty(value = "合同止期")
    private Date endDate;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "更新人")
    private String updatedUser;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updatedTime;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createdTime;


}
