package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 产品服务对应业务类型
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
public class AddProductBizForm extends Model<AddProductBizForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "编码", required = true)
    @NotEmpty(message = "idCode is required")
    private String idCode;

    @ApiModelProperty(value = "业务名", required = true)
    @NotEmpty(message = "name is required")
    private String name;

    @ApiModelProperty(value = "税率")
    @NotNull(message = "taxRate is required")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "描述")
    private String remarks;


}
