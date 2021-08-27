package com.jayud.customs.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;

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
public class QueryCustomsDeclarationFiling extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "箱单号")
    private String boxNum;

    @ApiModelProperty(value = "报关单号")
    private String num;

//    @ApiModelProperty(value = "归档日期(年月)")
//    private LocalDate filingDate;

    @ApiModelProperty(value = "创建时间")
    private List<String> createTime;


//    public void setCreateTime(List<String> createTime) {
//        if (CollectionUtils.isNotEmpty(createTime) && createTime.size() > 1) {
//            createTime.set(1, DateUtils.strMaximumTime(createTime.get(1)));
//        }
//        this.createTime = createTime;
//
//    }


}
