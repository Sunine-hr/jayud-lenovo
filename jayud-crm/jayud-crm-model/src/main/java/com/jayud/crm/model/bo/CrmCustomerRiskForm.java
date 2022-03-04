package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.crm.model.constant.CrmDictCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * CrmCustomerRisk 实体类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "基本档案_客户_风险客户（crm_customer_risk）对象", description = "基本档案_客户_风险客户（crm_customer_risk）")
public class CrmCustomerRiskForm extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @NotBlank(message = "客户名称不能为空")
    @ApiModelProperty(value = "客户名称")
    private String custName;

    @NotBlank(message = "风险类型不能为空")
    @ApiModelProperty(value = "风险类型")
    private String riskType;

    @ApiModelProperty(value = "经营范围")
    private String introduction;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "统一信用代码")
    private String unCreditCode;

    @ApiModelProperty(value = "拜访人员集合s")
    private List<Long> visitNameList;

    @ApiModelProperty(value = "创建时间")
    private List<String> creationTime;


    @ApiModelProperty(value = "创建时间前")
    private String creationTimeOne;

    @ApiModelProperty(value = "创建时间后")
    private String creationTimeTwo;


    public void setCreationTime(List<String> creationTime) {
        this.creationTime = creationTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String format = sdf.format(date);
        // 只传第一个 计算设置时间到 当前时间
        if(creationTime.get(0)!=null&&creationTime.get(1)==null){
            this.creationTimeOne = creationTime.get(0);
//            if(creationTime.get(1)!=null){
//                this.creationTimeTwo = creationTime.get(1);
//            }else {
            this.creationTimeTwo = format ;
//            }
        }
        // 只传第二个 计算 到第二个时间之前的所有的
        if(creationTime.get(1)!=null&&creationTime.get(0)==null){
            this.creationTimeOne = CrmDictCode.CRM_FILE_TYPE;
            this.creationTimeTwo = creationTime.get(1);
        }
    }
}
