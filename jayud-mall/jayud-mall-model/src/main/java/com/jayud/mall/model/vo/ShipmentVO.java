package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel(value = "ShipmentVO", description = "南京新智慧-运单装货信息")
@Data
public class ShipmentVO {

    private String shipment_id;
    private String client_reference;
    private String service;
    private int parcel_count;
    private List<String> attrs;
    private To_address to_address;
    private From_address from_address;
    private String chargeable_weight;
    private String charge_amount;
    private List<Charge_list> charge_list;
    private List<Parcels> parcels;
    private String remark;
    private String status;
    private long picking_time;
    private long rates_time;
    private long creat_time;
    private long created;
}

@Data
class To_address {
    private String name;
    private String company;
    private String tel;
    private String mobile;
    private String address_1;
    private String address_2;
    private String address_3;
    private String city;
    private String state;
    private String state_code;
    private String country;
    private String postcode;
    private String email;
}

@Data
class From_address {
    private String name;
    private String company;
    private String tel;
    private String mobile;
    private String address_1;
    private String address_2;
    private String address_3;
    private String city;
    private String state;
    private String state_code;
    private String country;
    private String postcode;
    private String email;
}

@Data
class Charge_list {
    private long invoice_time;
    private String charge_type;
    private String currency;
    private String charge;
    private String unit;
    private String unit_price;
}

@Data
class Parcels {

    private String number;
    private String ext_number;
    private String client_weight;
    private String client_length;
    private String client_width;
    private String client_height;
    private String actual_weight;
    private String chargeable_weight;
    private String chargeable_length;
    private String chargeable_width;
    private String chargeable_height;
    private List<Declarations> declarations;
    private long picking_time;
}

@Data
class Declarations {
    private String sku;
    private String name_zh;
    private String name_en;
    private int unit_value;
    private int qty;
    private String material;
    private String usage;
    private String brand;
    private int sale_price;
    private String sale_url;
    private String asin;
    private String fnsku;
    private int weight;
    private String size;
    private String photo_url;
    private String hscode;
    private int duty_rate;
    private String photos;
    private int is_battery;
}