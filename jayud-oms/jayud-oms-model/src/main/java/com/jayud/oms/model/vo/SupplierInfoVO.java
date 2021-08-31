package com.jayud.oms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import com.jayud.oms.model.enums.AuditStatusEnum;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 供应商信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-29
 */
@Data
public class SupplierInfoVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增ID")
    private Long id;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "服务类型")
    private String productClassify;

    @ApiModelProperty(value = "服务类型id集合")
    private List<Long> productClassifyIds;

    @ApiModelProperty(value = "供应商名称(中)")
    private String supplierChName;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "结算类型")
    private String settlementType;

    @ApiModelProperty(value = "账期")
    private String paymentDay;

    @ApiModelProperty(value = "税票种类型")
    private String taxReceipt;

    @ApiModelProperty(value = "税率")
    private String rate;

    @ApiModelProperty(value = "采购员id")
    private Long buyerId;

    @ApiModelProperty(value = "采购员")
    private String buyer;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "审核描述")
    private String auditComment;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "法人主体id集合", required = true)
    @NotEmpty(message = "legal_entity_ids is required")
    private List<Long> legalEntityIds = new ArrayList<>();

    @ApiModelProperty(value = "法人主体", required = true)
    private String legalEntityIdStr;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "客户/供应商id")
    private Long bindId;

    @ApiModelProperty(value = "是否可以编辑")
    public Boolean isEdit;

    @ApiModelProperty(value = "国家征信")
    private Integer nationalCredit;

    @ApiModelProperty(value = "海关征信")
    private Integer customsCredit;

    @ApiModelProperty(value = "海关信用等级(0:一般认证企业,1:一般信用企业,2:高级信用企业,3:失信企业)")
    private Integer customsCreditRating;

    @ApiModelProperty(value = "是否高级认证")
    private Boolean isAdvancedCertification;

    @ApiModelProperty(value = "文件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "文件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViews = new ArrayList<>();

    @ApiModelProperty(value = "对接GPS所需要的key值")
    private String appKey;

    @ApiModelProperty(value = "对接GPS公用路径前缀")
    private String gpsAddress;

    @ApiModelProperty(value = "gps厂商(1:云港通,2:北斗)")
    private Integer gpsType;

    @ApiModelProperty(value = "gps请求参数(json格式)")
    private String gpsReqParam;


    /**
     * 组装服务类型id集合
     */
    public void packageProductClassifyId(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return;
        }
        this.productClassifyIds = new ArrayList<>();
        for (String id : ids.split(",")) {
            this.productClassifyIds.add(Long.parseLong(id));
        }
    }

    public List<Long> getLegalEntityIds() {
        if (!StringUtil.isNullOrEmpty(this.legalEntityIdStr)) {
            String[] strs = this.legalEntityIdStr.split(",");
            for (String str : strs) {
                legalEntityIds.add(Long.valueOf(str));
            }
        }
        return legalEntityIds;
    }

    public void setAuditStatus(String auditStatus) {
        if (AuditStatusEnum.SUCCESS.getDesc().equals(auditStatus)
                || AuditStatusEnum.FAIL.getDesc().equals(auditStatus)) {
            this.isEdit = true;
        } else {
            this.isEdit = false;
        }
        this.auditStatus = auditStatus;
    }

    public void assembleAccessories(Object url) {
        if (url == null) return;
        this.fileViews = com.jayud.common.utils.StringUtils.getFileViews(this.filePath, this.fileName, url.toString());
    }
}
