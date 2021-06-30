package com.jayud.oceanship.bo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 海运订单列表
 * </p>
 *
 * @author
 * @since
 */
@Data
public class QuerySeaPortForm extends BasePageForm {

    @ApiModelProperty(value = "港口编码")
    private String code;

    @ApiModelProperty(value = "港口名称")
    private String name;

}
