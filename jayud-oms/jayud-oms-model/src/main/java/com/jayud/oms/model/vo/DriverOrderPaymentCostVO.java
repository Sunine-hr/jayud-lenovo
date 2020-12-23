package com.jayud.oms.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 司机录用费用明细
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
public class DriverOrderPaymentCostVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "录用费用id")
    private Long id;

    @ApiModelProperty(value = "中港订单号")
    private String orderNo;

    @ApiModelProperty(value = "费用名称")
    private String costName;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "附件路径,多个时用逗号分隔")
    @JsonIgnore
    private String files;

    @ApiModelProperty(value = "附件名称,多个时用逗号分隔")
    @JsonIgnore
    private String fileName;

    @ApiModelProperty(value = "附件详情")
    private List<FileView> fileDetails;

    /**
     * 组装附件
     */
    public void assemblyAnnex() {
        if (!StringUtils.isEmpty(this.files)) {
            String[] paths = files.split(",");
            String[] names = fileName.split(",");
            fileDetails = new ArrayList<>();
            for (int i = 0; i < paths.length; i++) {
                FileView fileView = new FileView();
                fileView.setFileName(names[i]);
                fileView.setAbsolutePath(paths[i]);
                fileDetails.add(fileView);
            }

        }
    }

}
