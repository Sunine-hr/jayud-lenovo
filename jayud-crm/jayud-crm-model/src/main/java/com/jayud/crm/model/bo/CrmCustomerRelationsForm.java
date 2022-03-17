package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.exception.JayudBizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CrmCustomerRelations 实体类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_联系人(crm_customer_relations)对象", description="基本档案_客户_联系人(crm_customer_relations)")
public class CrmCustomerRelationsForm extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @ApiModelProperty(value = "联系人类型")
    private Integer relationType;

    @ApiModelProperty(value = "是否默认联系人")
    private Boolean isDefault;

    @ApiModelProperty(value = "姓名联系人")
    private String cName;

    @ApiModelProperty(value = "姓名联系人")
    private String contactName;

    @ApiModelProperty(value = "证件类型")
    private String idType;

    @ApiModelProperty(value = "证件号码")
    private String idCard;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "地址")
    private String address;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "生日")
    private LocalDateTime birthday;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "持股比例")
    private Double shareholdingRatio;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "生日时间")
    private String birthdayString;


    public void setBirthdayString(String birthdayString) {
        this.birthdayString = birthdayString;
        String dateStr = "2021-09-03 21:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedDate = LocalDateTime.parse(birthdayString, formatter);
        this.birthday =parsedDate ;
    }


    /**
     * 校验参数
     */
    public void checkParam() {
        if (StringUtils.isEmpty(contactName)){throw new JayudBizException(400,"联系人不能为空"); }
        if (custId==null){throw new JayudBizException(400,"客户ID不能为空"); }
        if (StringUtils.isEmpty(mobile)){throw new JayudBizException(400,"电话不能为空"); }
    }

}
