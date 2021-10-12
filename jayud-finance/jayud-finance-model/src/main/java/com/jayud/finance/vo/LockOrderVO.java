package com.jayud.finance.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 锁单表
 * </p>
 *
 * @author chuanmei
 * @since 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="LockOrder对象", description="锁单表")
public class LockOrderVO extends Model<LockOrderVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "锁核算期")
    private List<String> times;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "状态（0无效 1有效）")
    private Integer status;

    @ApiModelProperty(value = "类型 (0:应收,1:应付)")
    private Integer type;

//    @ApiModelProperty(value = "创建时间")
//    private LocalDateTime createTime;
//
//    @ApiModelProperty(value = "创建人")
//    private String createUser;
//
//    @ApiModelProperty(value = "更新人")
//    private String updateUser;
//
//    @ApiModelProperty(value = "更新时间")
//    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
