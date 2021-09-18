package com.jayud.customs.model.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.customs.model.po.CustomsDeclFilingRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
@Accessors(chain = true)
@ApiModel(value = "CustomsDeclarationFiling对象", description = "报关归档")
public class CustomsDeclarationFilingVO extends Model<CustomsDeclarationFilingVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "箱单号")
    private String boxNum;

    @ApiModelProperty(value = "归档日期(年月)")
    private String filingDate;

    @ApiModelProperty(value = "业务模式(1-陆路运输 2-空运 3-海运 4-快递 5-内陆)")
    private Integer bizModel;

    @ApiModelProperty(value = "进出口类型(1进口 2出口)")
    @TableField("Im_and_ex_type")
    private Integer imAndExType;

    @ApiModelProperty(value = "状态（0禁用 1启用）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "归档记录表")
    private List<CustomsDeclFilingRecord> nums;

    @ApiModelProperty(value = "报关单号")
    private String num;

    @ApiModelProperty(value = "份数")
    private Integer number;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public void handleNums() {
        if (CollectionUtil.isNotEmpty(nums)) {
            StringBuilder sb = new StringBuilder();
            nums.forEach(e -> sb.append(e.getNum()).append(","));
            this.num = sb.toString();
            this.number = nums.size();
        }
    }
}
