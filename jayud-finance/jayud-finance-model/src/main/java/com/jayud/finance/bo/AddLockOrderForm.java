package com.jayud.finance.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.Utilities;
import com.jayud.finance.vo.LockOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
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
public class AddLockOrderForm extends Model<AddLockOrderForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "锁单时间区间")
    @NotNull
    private List<String> times;

    @ApiModelProperty(value = "状态（0无效 1有效）")
    @NotNull
    private Integer status;

    @ApiModelProperty(value = "类型 (0:应收,1:应付)")
    @NotNull
    private Integer type;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
