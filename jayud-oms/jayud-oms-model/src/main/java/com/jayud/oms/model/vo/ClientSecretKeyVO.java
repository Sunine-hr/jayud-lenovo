package com.jayud.oms.model.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 客户对外接口秘钥表
 * </p>
 *
 * @author wh
 * @since 2021-11-20
 */

@Data
@Accessors(chain = true)
public class ClientSecretKeyVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户ID")
    private String customerInfoId;

    @ApiModelProperty(value = "应用APPID")
    private String appId;

    @ApiModelProperty(value = "应用公钥密钥")
    private String appSecret;

    @ApiModelProperty(value = "应用私钥密钥")
    private String appPrivateSecret;

    @ApiModelProperty(value = "'状态（0 禁用 1启用 2删除)默认1 启用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
