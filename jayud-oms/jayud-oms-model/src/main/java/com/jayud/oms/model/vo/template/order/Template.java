package com.jayud.oms.model.vo.template.order;

import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Template<T> {

    @ApiModelProperty
    private List<T> data;

    @ApiModelProperty(value = "头部")
    private List<Map<String, Object>> head;

    public Template() {
        Class clazz = this.getClass();
        ParameterizedType pt = (ParameterizedType) clazz.getGenericSuperclass();
        clazz = (Class) pt.getActualTypeArguments()[0];
        this.head = Utilities.assembleEntityHead(clazz);
    }
}
