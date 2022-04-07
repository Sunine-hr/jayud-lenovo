package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.exception.JayudBizException;
import com.jayud.crm.model.po.CrmFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * CrmCustomerFollow 实体类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_跟进记录(crm_customer_follow)对象", description="基本档案_客户_跟进记录(crm_customer_follow)")
public class CrmCustomerFollowForm extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @ApiModelProperty(value = "联系人")
    private String linkmanName;

    @ApiModelProperty(value = "联系人id")
    private Long linkmanNameId;

    @ApiModelProperty(value = "记录类型")
    private String recordType;

    @ApiModelProperty(value = "记录内容")
    private String recordContent;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "文件上传url")
    private String uploadFileUrl;

    @ApiModelProperty(value = "联系时间")
    private LocalDateTime contactTime;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "文件上传表")
    private List<CrmFile> crmFiles;

    @ApiModelProperty(value = "设置联系时间")
    private String contactTimeString;

    public void setContactTimeString(String contactTimeString) {
        if(StringUtils.isNotEmpty(contactTimeString)){
            this.contactTimeString = contactTimeString;
            String dateStr = "2021-09-03 21:00:00";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime parsedDate = LocalDateTime.parse(contactTimeString, formatter);
            this.contactTime =parsedDate ;
        }

    }



    /**
     * 校验参数
     */
    public void checkParam() {
        if (StringUtils.isEmpty(linkmanName)){throw new JayudBizException(400,"联系人不能为空"); }
//        if (linkmanNameId==null){throw new JayudBizException(400,"联系人id不能为空"); }
        if (custId==null){throw new JayudBizException(400,"客户ID不能为空"); }
        if (StringUtils.isEmpty(recordType)){throw new JayudBizException(400,"记录类型不能为空"); }
        if (StringUtils.isEmpty(linkmanName)){throw new JayudBizException(400,"联系人不能为空"); }
        if (StringUtils.isEmpty(contactTimeString)){throw new JayudBizException(400,"联系时间不能为空"); }
    }

}
