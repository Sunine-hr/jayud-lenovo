package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 司机录入费用表(小程序使用)
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-13
 */
@Data
public class DriverEmploymentFeeVO extends Model<DriverEmploymentFeeVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "司机录用费用id")
    private Long id;

    @ApiModelProperty(value = "司机id")
    private Long driverId;

    @ApiModelProperty(value = "主订单")
    private String mainOrderNo;

    @ApiModelProperty(value = "中港订单id")
    private Long orderId;

    @ApiModelProperty(value = "订单编码")
    private String orderNo;

    @ApiModelProperty(value = "费用代码")
    private String costCode;

    @ApiModelProperty(value = "费用名称")
    private String costName;

    @ApiModelProperty(value = "费用金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "多个文件，用逗号隔开")
    private String fileName;

    @ApiModelProperty(value = "多个文件路径,用逗号隔开")
    private String files;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "状态(0:待提交，1:已提交, 2:草稿)")
    @JsonIgnore
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "附件详情")
    private List<FileView> fileDetails;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public void assemblyAnnex() {
        if (!StringUtils.isEmpty(this.files)) {
            String[] paths = files.split(",");
            String[] names = fileName.split(",");
            fileDetails = new ArrayList<>();
            for (int i = 0; i < paths.length; i++) {
                FileView fileView = new FileView();
                fileView.setFileName(names[i]);
                fileView.setAbsolutePath(paths[i]);
                fileView.setRelativePath(paths[i]);
            }

        }
    }

}
