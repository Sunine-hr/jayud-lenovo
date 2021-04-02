package com.jayud.oceanship.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单地址表
 * </p>
 *
 * @author
 * @since
 */
@Data
public class OrderAddressVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "类型(0:发货,1:收货,2:通知)")
    private Integer type;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "公司")
    private String company;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "邮箱")
    private String mailbox;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "附件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "附件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "发货人/送货人")
    private String consignee;

    @ApiModelProperty(value = "附件")
    private List<FileView> takeFiles = new ArrayList<>();

    public void getFile(String path){
        this.takeFiles = StringUtils.getFileViews(this.getFilePath(),this.getFileName(),path);
    }


}
