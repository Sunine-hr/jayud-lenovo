package com.jayud.customs.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.Utilities;
import com.jayud.customs.model.po.CustomsDeclFilingRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报关归档
 * </p>
 *
 * @author LDR
 * @since 2021-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CustomsDeclarationFiling对象", description = "报关归档")
public class AddCustomsDeclarationFilingForm extends Model<AddCustomsDeclarationFilingForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "箱单号")
    @NotEmpty(message = "请输入箱单号")
    private String boxNum;

    @ApiModelProperty(value = "归档日期")
    @NotNull(message = "请输入归档日期")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String filingDate;

    @ApiModelProperty(value = "业务模式(1-陆路运输 2-空运 3-海运 4-快递 5-内陆)")
    @NotNull(message = "请输入业务模式")
    private Integer bizModel;

    @ApiModelProperty(value = "进出口类型(1进口 2出口)")
    @NotNull(message = "请选择进出口类型")
    private Integer imAndExType;

    @ApiModelProperty(value = "报关单号集合数组")
    @NotNull(message = "请输入报关单号")
    private List<CustomsDeclFilingRecord> nums;


    @ApiModelProperty(value = "用户id")
    private Long loginUserId;

//    @ApiModelProperty(value = "报关单号多个逗号隔开")
//    private String num;

    public static void main(String[] args) {
        System.out.println(Utilities.printFieldsInfo(AddCustomsDeclarationFilingForm.class));


    }


    //    public void setNums(List<Map<String, String>> nums) {
//        this.nums = nums;
//        StringBuilder sb = new StringBuilder();
//        for (Map<String, String> num : nums) {
//            String tmp = num.get("num");
//            sb.append(tmp).append(",");
//        }
//        this.num = sb.toString();
//    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
