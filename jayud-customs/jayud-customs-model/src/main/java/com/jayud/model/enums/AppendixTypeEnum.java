package com.jayud.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 委托单上传文件类型枚举
 *
 * @author william
 * @description
 * @Date: 2020-09-08 09:21
 */
@Getter
@AllArgsConstructor
public enum AppendixTypeEnum {
    发票("00000001","发票"),

    装箱单("00000002","装箱单"),

    提运单("00000003","提运单"),

    合同("00000004","合同"),

    其他1("00000005","其他"),

    其他2("00000006","其他"),

    其他3("00000007","其他"),

    纸质代理报关委托协议扫描件("00000008","纸质代理报关委托协议扫描件"),

    电子代理委托协议编号("10000001","电子代理委托协议编号"),

    减免税货物税款担保证明("10000002","减免税货物税款担保证明"),

    减免税货物税款担保延期证明("10000003","减免税货物税款担保延期证明"),

    所有其他的("99999999","所有其他的"),
            ;

    private String id;
    private String name;
}
