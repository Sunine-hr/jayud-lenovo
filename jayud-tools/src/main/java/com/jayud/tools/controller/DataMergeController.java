package com.jayud.tools.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.jayud.tools.utils.RedisUtils;
import com.jayud.tools.utils.StringUtils;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;

@RestController
@RequestMapping("/datamerge")
@Api(tags = "T004-工具-数据合并")
@ApiSort(value = 4)
@Slf4j
public class DataMergeController {

    /**
     * Z01  ->  12月-1.docx
     * 获取`物料号`
     */
    public final static String DataMerge_Z01 = "DataMerge_Z01";
    /**
     * Z02  ->  12月-2.docx
     * 获取`PN`
     */
    public final static String DataMerge_Z02 = "DataMerge_Z02";
    /**
     * Z03  ->  12月-3.xlsx
     * 获取`PN`
     */
    public final static String DataMerge_Z03 = "DataMerge_Z03";
    /**
     * Z04  ->  更新台账2020年12月23日.xlsx
     */
    public final static String DataMerge_Z04 = "DataMerge_Z04";
    /**
     * Z05  ->  FY20成本更新-关税明细-202004更新.xlsx
     */
    public final static String DataMerge_Z05 = "DataMerge_Z05";

    @Autowired
    private RedisUtils redisUtils;
    /**
     * excelList
     */
    private static List<List<Object>> excelList = Collections.synchronizedList(new ArrayList<List<Object>>());

    /**
     * 读取大数据Excel
     * @return
     */
    private RowHandler createRowHandler() {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, long rowIndex, List<Object> rowlist) {
                excelList.add(rowlist);
            }
        };
    }

    @ApiOperation(value = "数据合并，导出数据")
    @GetMapping(value = "/dataMergeExport")
    @ApiOperationSupport(order = 1)
    public void dataMergeExport(@RequestParam(value = "userId",required=false) Long userId,
                                HttpServletResponse response){

        //TODO 1.准备数据
        prepareData();

        //TODO 2.组合数据
        List<DataMergeVO> dataMergeVOS = combinationData();

        //TODO 3.导出数据
        List<DataMergeVO> rows = dataMergeVOS;
        ExcelWriter writer = ExcelUtil.getBigWriter();

        //自定义标题别名
        writer.addHeaderAlias("materialNumber", "物料号");
        writer.addHeaderAlias("pn", "PN");
        writer.addHeaderAlias("prlitm", "PRLITM");
        writer.addHeaderAlias("chineseDescription", "中文描述");
        writer.addHeaderAlias("hs", "H.S");
        writer.addHeaderAlias("hsbit", "H.S(截取前8位)");
        writer.addHeaderAlias("tariffCode", "税则号列①");
        writer.addHeaderAlias("commodityAbbreviation", "商品简称②");

        Field[] s = DataMergeVO.class.getDeclaredFields();
        int lastColumn = s.length-1;

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(lastColumn, "数据合并导出");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);

        String name = StringUtils.toUtf8String("数据合并导出");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");
        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
            writer.flush(out,true);
            writer.close();
            IoUtil.close(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 组合数据
     * @return
     */
    private List<DataMergeVO> combinationData() {
        //查询的数据源
        String s1 = redisUtils.get(DataMerge_Z01);
        List<String> list1 = JSON.parseObject(s1, new TypeReference<List<String>>() {});
        String s2 = redisUtils.get(DataMerge_Z02);
        List<String> list2 = JSON.parseObject(s2, new TypeReference<List<String>>() {});
        String s3 = redisUtils.get(DataMerge_Z03);
        List<String> list3 = JSON.parseObject(s3, new TypeReference<List<String>>() {});

        //对比的数据源
        String s5 = redisUtils.get(DataMerge_Z05);
        List<Z05> list5 = JSON.parseObject(s5, new TypeReference<List<Z05>>() {});

        String json = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("template_data/exclusionCommodityList.json");
            json = IOUtils.toString(classPathResource.getInputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //例外商品清单 list
        List<ExclusionCommodityList> exclusionCommodityLists = JSON.parseObject(json, new TypeReference<List<ExclusionCommodityList>>() {});


        List<DataMergeVO> dataMergeVOS1 = dataMerge1(list1, list5, exclusionCommodityLists);
        List<DataMergeVO> dataMergeVOS2 = dataMerge2(list2, list5, exclusionCommodityLists);
        List<DataMergeVO> dataMergeVOS3 = dataMerge2(list3, list5, exclusionCommodityLists);

        List<DataMergeVO> dataMergeExportList = new ArrayList<>();
        dataMergeExportList.addAll(dataMergeVOS1);
        dataMergeExportList.addAll(dataMergeVOS2);
        dataMergeExportList.addAll(dataMergeVOS3);

        return dataMergeExportList;
    }

    private List<DataMergeVO> dataMerge2(List<String> list2, List<Z05> list5, List<ExclusionCommodityList> exclusionCommodityLists) {
        List<DataMergeVO> dataMergeVOS2 = new ArrayList<>();
        list2.forEach(code -> {
            DataMergeVO dataMergeVO = new DataMergeVO();
            dataMergeVO.setPn(code);
            list5.forEach(data -> {
                String prlitm = data.getPrlitm();
                if(code.equalsIgnoreCase(prlitm)){
                    dataMergeVO.setPrlitm(prlitm);
                    dataMergeVO.setChineseDescription(data.getChineseDescription());
                    dataMergeVO.setHs(data.getHs());
                    dataMergeVO.setHsbit(data.getHs().length() > 8 ? data.getHs().substring(0,8) : data.getHs());
                }
            });
            dataMergeVOS2.add(dataMergeVO);
        });
        dataMergeVOS2.forEach(dataMergeVO -> {
            String hsbit = dataMergeVO.getHsbit();
            if(hsbit != null){
                exclusionCommodityLists.forEach(exclusionCommodityList -> {
                    //税则号列①
                    String tariffCode = exclusionCommodityList.getTariffCode();
                    if(hsbit.equalsIgnoreCase(tariffCode)){
                        dataMergeVO.setTariffCode(tariffCode);
                        dataMergeVO.setCommodityAbbreviation(exclusionCommodityList.getCommodityAbbreviation());
                    }
                });
            }
        });
        System.out.println(dataMergeVOS2);
        return dataMergeVOS2;
    }

    private List<DataMergeVO> dataMerge1(List<String> list1, List<Z05> list5, List<ExclusionCommodityList> exclusionCommodityLists) {
        List<DataMergeVO> dataMergeVOS1 = new ArrayList<>();
        list1.forEach(code -> {
            DataMergeVO dataMergeVO = new DataMergeVO();
            dataMergeVO.setMaterialNumber(code);
            list5.forEach(data -> {
                String prlitm = data.getPrlitm();
                if(code.equalsIgnoreCase(prlitm)){
                    dataMergeVO.setPn(null);
                    dataMergeVO.setPrlitm(prlitm);
                    dataMergeVO.setChineseDescription(data.getChineseDescription());
                    dataMergeVO.setHs(data.getHs());
                    dataMergeVO.setHsbit(data.getHs().length() > 8 ? data.getHs().substring(0,8) : data.getHs());
                }
            });
            dataMergeVOS1.add(dataMergeVO);
        });
        dataMergeVOS1.forEach(dataMergeVO -> {
            String hsbit = dataMergeVO.getHsbit();
            if(hsbit != null){
                exclusionCommodityLists.forEach(exclusionCommodityList -> {
                    //税则号列①
                    String tariffCode = exclusionCommodityList.getTariffCode();
                    if(hsbit.equalsIgnoreCase(tariffCode)){
                        dataMergeVO.setTariffCode(tariffCode);
                        dataMergeVO.setCommodityAbbreviation(exclusionCommodityList.getCommodityAbbreviation());
                    }
                });
            }
        });
        System.out.println(dataMergeVOS1);
        return dataMergeVOS1;
    }

    /**
     * 准备数据
     */
    private void prepareData() {
        z01();
        z02();
        z03();
        //z04();//跳过第4张表
        z05();
        //z06();//准备的`例外商品清单`
    }

    /**
     * z01
     */
    private void z01() {
        excelList.clear();
        Excel07SaxReader reader = new Excel07SaxReader(createRowHandler());
        //Excel07SaxReader read = reader.read(inputStream, 0);
        reader.read("D:\\文档\\work-A05-jayud-tools-exce数据合并\\源文件\\Z01.xlsx", 0);
        Set<String> materialNumberSet = new HashSet<>();
        for(int i=1; i<excelList.size(); i++){
            List<Object> o = excelList.get(i);
            log.info("Z01.xlsx"+i+"对象: " + o);
            String materialNumber = o.get(0).toString().trim();//物料号
            materialNumberSet.add(materialNumber);
        }
        List<String> materialNumberList = new ArrayList<>(materialNumberSet);
        redisUtils.set(DataMerge_Z01, JSONObject.toJSONString(materialNumberList), 60*60*2);//保存两小时
    }

    private void z02() {
        excelList.clear();
        Excel07SaxReader reader = new Excel07SaxReader(createRowHandler());
        //Excel07SaxReader read = reader.read(inputStream, 0);
        reader.read("D:\\文档\\work-A05-jayud-tools-exce数据合并\\源文件\\Z02.xlsx", 0);

        Set<String> pnSet = new HashSet<>();
        for(int i=1; i<excelList.size(); i++){
            List<Object> o = excelList.get(i);
            log.info("Z02.xlsx"+i+"对象: " + o);
            String pn = o.get(5).toString().trim();//PN
            pnSet.add(pn);
        }
        List<String> pnList = new ArrayList<>(pnSet);//利用set去重，在放入list
        System.out.println(pnList);
        redisUtils.set(DataMerge_Z02, JSONObject.toJSONString(pnList), 60*60*2);//保存两小时
    }

    private void z03() {
        excelList.clear();
        Excel07SaxReader reader = new Excel07SaxReader(createRowHandler());
        //Excel07SaxReader read = reader.read(inputStream, 0);
        reader.read("D:\\文档\\work-A05-jayud-tools-exce数据合并\\源文件\\Z03.xlsx", 0);

        Set<String> pnSet = new HashSet<>();
        for(int i=1; i<excelList.size(); i++){
            List<Object> o = excelList.get(i);
            log.info("Z03.xlsx"+i+"对象: " + o);
            String pn = o.get(0).toString().trim();//PN
            pnSet.add(pn);
        }
        List<String> pnList = new ArrayList<>(pnSet);//利用set去重，在放入list
        System.out.println(pnList);
        redisUtils.set(DataMerge_Z03, JSONObject.toJSONString(pnList), 60*60*2);//保存两小时
    }

    private void z04() {
        excelList.clear();
        Excel07SaxReader reader = new Excel07SaxReader(createRowHandler());
        //Excel07SaxReader read = reader.read(inputStream, 0);
        reader.read("D:\\文档\\work-A05-jayud-tools-exce数据合并\\源文件\\Z04-更新台账2020年12月23日.xlsx", 0);

        Set<Z04> pnSet = new HashSet<>();
        for(int i=1; i<excelList.size(); i++){
            List<Object> o = excelList.get(i);
            log.info("Z04-更新台账2020年12月23日.xlsx-"+i+"-对象: " + o);
            String productName = o.get(10).toString().trim();//DescriptionofGoods商品名称
            String pn = o.get(13).toString().trim();//PartNo.料号
            Z04 z04 = new Z04();
            z04.setNo(pn);
            z04.setProductName(productName);
            pnSet.add(z04);
        }
        List<Z04> pnList = new ArrayList<>(pnSet);//利用set去重，在放入list
        System.out.println(pnList);
        redisUtils.set(DataMerge_Z04, JSONObject.toJSONString(pnList), 60*60*2);//保存两小时
    }

    private void z05() {
        excelList.clear();
        Excel07SaxReader reader = new Excel07SaxReader(createRowHandler());
        //Excel07SaxReader read = reader.read(inputStream, 0);
        reader.read("D:\\文档\\work-A05-jayud-tools-exce数据合并\\源文件\\Z05-FY20成本更新-关税明细-202004更新.xlsx", 0);

        Set<Z05> pnSet = new HashSet<>();
        for(int i=1; i<excelList.size(); i++){
            List<Object> o = excelList.get(i);
            log.info("Z05-FY20成本更新-关税明细-202004更新.xlsx-"+i+"-对象: " + o);
            String prlitm = (o.get(2) == null) ? "" : o.get(2).toString().trim();//PRLITM,对应物料号、PN
            String chineseDescription = (o.get(3) == null) ? "" : o.get(3).toString().trim();//中文描述
            String hs = (o.get(4) == null) ? "" : o.get(4).toString().trim();//H.S
            Z05 z05 = new Z05();
            z05.setPrlitm(prlitm);
            z05.setChineseDescription(chineseDescription);
            z05.setHs(hs);
            pnSet.add(z05);
        }
        List<Z05> pnList = new ArrayList<>(pnSet);//利用set去重，在放入list
        System.out.println(pnList);
        redisUtils.set(DataMerge_Z05, JSONObject.toJSONString(pnList), 60*60*2);//保存两小时
    }


    private void z06() {
        excelList.clear();
        Excel07SaxReader reader = new Excel07SaxReader(createRowHandler());
        //Excel07SaxReader read = reader.read(inputStream, 0);
        reader.read("D:\\文档\\work-A05-jayud-tools-exce数据合并\\附件：可申请排除商品清单.xlsx", 0);

        List<ExclusionCommodityList> exclusionCommodityLists = new ArrayList<>();
        for(int i=1; i<excelList.size(); i++){
            List<Object> o = excelList.get(i);
            log.info("附件：可申请排除商品清单.xlsx-"+i+"-对象: " + o);
            String serialNumber = (o.get(0) == null) ? "" : o.get(0).toString().trim();//序号
            String tariffCode = (o.get(1) == null) ? "" : o.get(1).toString().trim();//税则号列①
            String commodityAbbreviation = (o.get(2) == null) ? "" : o.get(2).toString().trim();//商品简称②
            ExclusionCommodityList exclusionCommodityList = new ExclusionCommodityList();
            exclusionCommodityList.setSerialNumber(serialNumber);
            exclusionCommodityList.setTariffCode(tariffCode);
            exclusionCommodityList.setCommodityAbbreviation(commodityAbbreviation);
            exclusionCommodityLists.add(exclusionCommodityList);
        }
        System.err.println(JSONObject.toJSONString(exclusionCommodityLists));
    }


}

/**
 * Z04-更新台账2020年12月23日.xlsx
 * 废弃了
 */
@Data
class Z04{
    @ApiModelProperty(value = "PartNo.料号", position = 1)
    @JSONField(ordinal = 1)
    private String no;

    @ApiModelProperty(value = "DescriptionofGoods商品名称", position = 2)
    @JSONField(ordinal = 2)
    private String productName;
}

/**
 * Z05-FY20成本更新-关税明细-202004更新.xlsx
 */
@Data
class Z05{
    @ApiModelProperty(value = "PRLITM,对应物料号、PN", position = 1)
    @JSONField(ordinal = 1)
    private String prlitm;

    @ApiModelProperty(value = "中文描述", position = 2)
    @JSONField(ordinal = 2)
    private String chineseDescription;

    @ApiModelProperty(value = "H.S", position = 3)
    @JSONField(ordinal = 3)
    private String hs;
}

/**
 * 合并数据VO
 */
@Data
class DataMergeVO{

    @ApiModelProperty(value = "物料号(Z01)", position = 1)
    @JSONField(ordinal = 1)
    private String materialNumber;

    @ApiModelProperty(value = "PN(Z02,Z03)", position = 2)
    @JSONField(ordinal = 2)
    private String pn;

    @ApiModelProperty(value = "PRLITM,对应物料号、PN", position = 3)
    @JSONField(ordinal = 3)
    private String prlitm;

    @ApiModelProperty(value = "中文描述", position = 4)
    @JSONField(ordinal = 4)
    private String chineseDescription;

    @ApiModelProperty(value = "H.S", position = 5)
    @JSONField(ordinal = 5)
    private String hs;

    @ApiModelProperty(value = "H.S(截取前8位)", position = 6)
    @JSONField(ordinal = 6)
    private String hsbit;

    @ApiModelProperty(value = "税则号列①", position = 2)
    @JSONField(ordinal = 2)
    private String tariffCode;

    @ApiModelProperty(value = "商品简称②", position = 3)
    @JSONField(ordinal = 3)
    private String commodityAbbreviation;


}

/**
 * 附件：可申请排除商品清单.xlsx
 * 例外商品清单
 */
@Data
class ExclusionCommodityList{

    @ApiModelProperty(value = "序号", position = 1)
    @JSONField(ordinal = 1)
    private String serialNumber;

    @ApiModelProperty(value = "税则号列①", position = 2)
    @JSONField(ordinal = 2)
    private String tariffCode;

    @ApiModelProperty(value = "商品简称②", position = 3)
    @JSONField(ordinal = 3)
    private String commodityAbbreviation;

}