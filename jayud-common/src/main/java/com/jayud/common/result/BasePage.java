package com.jayud.common.result;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author ciro
 * @date 2022/3/31 16:18
 * @description:    基础分页
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="基础分页", description="基础分页")
public class BasePage<T> {

    @JsonProperty(value = "countId")
    String countId;

    @JsonProperty(value = "current")
    long current;

    @JsonProperty(value = "hitCount")
    boolean hitCount;

    @JsonProperty(value = "isSearchCount")
    boolean isSearchCount;

    @JsonProperty(value = "maxLimit")
    Long maxLimit;

    @JsonProperty(value = "optimizeCountSql")
    boolean optimizeCountSql;

    @JsonProperty(value = "orders;")
    List<OrderItem> orders;

    @ApiModelProperty(value = "数据对象")
    List<T> records;

    @JsonProperty(value = "size")
    long size;

    @JsonProperty(value = "total")
    long total;


}
