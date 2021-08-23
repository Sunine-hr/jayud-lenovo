package com.jayud.oms.model.bo;

import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.oms.model.po.RegionCity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 客户地址
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
public class AddCustomerAddrForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "地址id,修改时必传")
    private Long id;

    @ApiModelProperty(value = "地址类型（0 提货地址 1送货地址）")
    @NotNull(message = "type is required")
    private Integer type;

    @ApiModelProperty(value = "客户id")
    @NotNull(message = "customerId is required")
    private Long customerId;

    @ApiModelProperty(value = "联系人")
    @NotEmpty(message = "contacts is required")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @NotEmpty(message = "phone is required")
    private String phone;

    @ApiModelProperty(value = "省主键")
    @NotNull(message = "province is required")
    private Long province;

    @ApiModelProperty(value = "市主键")
    @NotNull(message = "city is required")
    private Long city;

    @ApiModelProperty(value = "区主键")
    private Long area;

    @ApiModelProperty(value = "详细地址")
    @NotEmpty(message = "address is required")
    private String address;

    @ApiModelProperty(value = "邮编")
    private String postCode;

    @ApiModelProperty(value = "百度经纬度")
    private String baiduLaAndLo;

    @ApiModelProperty(value = "省code")
    private String provinceCode;

    @ApiModelProperty(value = "市code")
    private String cityCode;

    @ApiModelProperty(value = "区code")
    private String areaCode;

    @ApiModelProperty(value = "最终地址")
    private String finalAddress;

    public static void main(String[] args) {
        System.out.println(Utilities.printFieldsInfo(AddCustomerAddrForm.class));
    }

    public void assemblyLastAddr(RegionCity province, RegionCity city, RegionCity area) {
        List<String> keys = new ArrayList<>();
        keys.add(province.getName());
        keys.add(city.getName());
        if (area != null && !StringUtils.isEmpty(area.getName())) {
            keys.add(area.getName());
        }
        for (String key : keys) {
            this.finalAddress = this.address.replace(key, "");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(province.getName()).append(" ").append(city.getName()).append(" ");
        if (area != null && !StringUtils.isEmpty(area.getName())) {
            sb.append(area.getName()).append(" ");
        }
        sb.append(this.finalAddress);
        this.finalAddress = sb.toString();
    }
}
