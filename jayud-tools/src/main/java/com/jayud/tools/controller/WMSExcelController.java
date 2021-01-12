package com.jayud.tools.controller;

import cn.hutool.poi.excel.sax.Excel03SaxReader;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jayud.common.exception.ApiException;
import com.jayud.tools.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/wmsexcel")
@Api(tags = "T005-WMS-Excel数据导出")
@ApiSort(value = 5)
@Slf4j
public class WMSExcelController {

    /**
     * Excel 拆分
     */
    public final static String EXCELSPLIT = "EXCELSPLIT";

    public final static String EXPORTWMSDATALIST = "EXPORTWMSDATALIST";

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
                Object o = rowlist.get(0);
                String s = o != null ? o.toString() : null;
                //第一列，不为 null 或 "" ,加入数据
                if(o != null && !s.equals("")){
                    excelList.add(rowlist);
                }
            }
        };
    }

    @ApiOperation(value = "WMS，龙岗仓库，博亚通制单程序,excel拆分")
    @PostMapping(value = "/excelSplit")
    @ApiOperationSupport(order = 1)
    public void excelSplit(
            @RequestParam(value = "userId",required=false) Long userId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response){

        userId = (userId != null) ? userId : 1;//userId,不存在，设置为1

        //准备导出的数据
        prepareData(userId, file);

        String json = redisUtils.get(EXPORTWMSDATALIST+"_"+userId);
        List<ExportWMSData> list2 = JSON.parseObject(json, new TypeReference<List<ExportWMSData>>() {});
        System.out.println(list2);

    }

    /**
     * 准备导出的数据
     * @param userId
     * @param file
     */
    private void prepareData(Long userId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApiException("文件为空！");
        }
        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        excelList.clear();

        //判断excel是03版本还是07版本
        InputStream is = FileMagic.prepareToCheckMagic(inputStream);
        try {
            FileMagic fm = FileMagic.valueOf(is);
            switch(fm) {
                case OLE2:
                    //excel是03版本
                    Excel03SaxReader reader03 = new Excel03SaxReader(createRowHandler());
                    reader03.read(is, 0);
                    break;
                case OOXML:
                    //excel是07版本
                    Excel07SaxReader reader07 = new Excel07SaxReader(createRowHandler());
                    reader07.read(is, 0);
                    break;
                default:
                    throw new IOException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //当前登录用户Id
        userId = (userId != null) ? userId : 1;
        //导入前先删除数据,根据登录用户的用户id做删除

        //redisUtils.set(EXCELSPLIT+"_"+userId, JSONObject.toJSONString(excelList), 60*60*2);//保存两小时
        redisUtils.set(EXCELSPLIT+"_"+userId, JSONObject.toJSONString(excelList), -1);


        String s1 = redisUtils.get(EXCELSPLIT+"_"+userId);
        List<List<Object>> list = JSON.parseObject(s1, new TypeReference<List<List<Object>>>() {});


        List<ExportWMSData> exportWMSDataList = new ArrayList<>();

        //以备注作为`拆分明细`计算条件，统计备注
        String countRemarks = null;
        List<ExportWMSDetail> exportWMSDetailList = new ArrayList<>();
        ExportWMSData exportWMSData = new ExportWMSData();

        //int i=1,跳过第一条，表头列
        for(int i=1; i<list.size(); i++){
            List<Object> objects = list.get(i);

            String contractNumber = objects.get(0) != null ? objects.get(0).toString().replaceAll("\\s", "") : null;//合同号
            String caseNumber = objects.get(1) != null ? objects.get(1).toString() : null;//箱号
            String tradeName = objects.get(2) != null ? objects.get(2).toString() : null;//品名
            String specification = objects.get(3) != null ? objects.get(3).toString() : null;//型号及规格
            String units = objects.get(4) != null ? objects.get(4).toString() : null;//单位
            String deliveryNumber = objects.get(5) != null ? objects.get(5).toString() : null;//送货数量
            String boxNumber = objects.get(6) != null ? objects.get(6).toString() : null;//箱数
            String remarks = objects.get(7) != null ? objects.get(7).toString() : null;//备注

            //以备注作为拆分计算条件,备注不为null，并且不是第一个数据
            if(remarks != null){
                if(i != 1){
                    //导出WMS数据
                    exportWMSData.setRemarks(countRemarks);
                    List<ExportWMSDetail> newExportWMSDetailList = new ArrayList<>();
                    newExportWMSDetailList.addAll(exportWMSDetailList);
                    exportWMSData.setDetails(newExportWMSDetailList);
                    BigDecimal totalDeliveryQuantity = new BigDecimal("0");//合计(送货数量)
                    BigDecimal totalBoxes = new BigDecimal("0");//合计(箱数)
                    for(int j = 0; j<newExportWMSDetailList.size(); j++){
                        ExportWMSDetail exportWMSDetail = newExportWMSDetailList.get(j);
                        String det_deliveryNumber = exportWMSDetail.getDeliveryNumber() != null ? exportWMSDetail.getDeliveryNumber() : "0";//送货数量
                        String det_boxNumber = exportWMSDetail.getBoxNumber() != null ? exportWMSDetail.getBoxNumber() : "0";//箱数

                        try{
                            totalDeliveryQuantity = totalDeliveryQuantity.add(new BigDecimal(det_deliveryNumber));
                            totalBoxes = totalBoxes.add(new BigDecimal(det_boxNumber));
                        }catch(Exception e){
                            e.printStackTrace();
                            log.error("java.lang.NumberFormatException: null, det_deliveryNumber: " + det_deliveryNumber);
                        }

                    }
                    exportWMSData.setTotalDeliveryQuantity(totalDeliveryQuantity.toString());
                    exportWMSData.setTotalBoxes(totalBoxes.toString());
                    exportWMSDataList.add(exportWMSData);//保存上一批的数据

                    //清空
                    exportWMSData = new ExportWMSData();
                    exportWMSDetailList.clear();
                }

                countRemarks = remarks;
                //导出WMS明细
                ExportWMSDetail exportWMSDetail = new ExportWMSDetail();
                exportWMSDetail.setContractNumber(contractNumber);
                exportWMSDetail.setTradeName(tradeName);
                exportWMSDetail.setSpecification(specification);
                exportWMSDetail.setDeliveryNumber(deliveryNumber);
                exportWMSDetail.setUnits(units);
                exportWMSDetail.setBoxNumber(boxNumber);
                exportWMSDetail.setCaseNumber(caseNumber);
                exportWMSDetailList.add(exportWMSDetail);

            }else{
                //导出WMS明细
                ExportWMSDetail exportWMSDetail = new ExportWMSDetail();
                exportWMSDetail.setContractNumber(contractNumber);
                exportWMSDetail.setTradeName(tradeName);
                exportWMSDetail.setSpecification(specification);
                exportWMSDetail.setDeliveryNumber(deliveryNumber);
                exportWMSDetail.setUnits(units);
                exportWMSDetail.setBoxNumber(boxNumber);
                exportWMSDetail.setCaseNumber(caseNumber);
                exportWMSDetailList.add(exportWMSDetail);
            }
            //最后一个
            if(i==list.size()-1){
                //导出WMS数据
                exportWMSData.setRemarks(countRemarks);
                List<ExportWMSDetail> newExportWMSDetailList = new ArrayList<>();
                newExportWMSDetailList.addAll(exportWMSDetailList);
                exportWMSData.setDetails(newExportWMSDetailList);
                BigDecimal totalDeliveryQuantity = new BigDecimal("0");//合计(送货数量)
                BigDecimal totalBoxes = new BigDecimal("0");//合计(箱数)
                for(int j = 0; j<newExportWMSDetailList.size(); j++){
                    ExportWMSDetail exportWMSDetail = newExportWMSDetailList.get(j);
                    String det_deliveryNumber = exportWMSDetail.getDeliveryNumber() != null ? exportWMSDetail.getDeliveryNumber() : "0";//送货数量
                    String det_boxNumber = exportWMSDetail.getBoxNumber() != null ? exportWMSDetail.getBoxNumber() : "0";//箱数
                    totalDeliveryQuantity = totalDeliveryQuantity.add(new BigDecimal(det_deliveryNumber));
                    totalBoxes = totalBoxes.add(new BigDecimal(det_boxNumber));

                }
                exportWMSData.setTotalDeliveryQuantity(totalDeliveryQuantity.toString());
                exportWMSData.setTotalBoxes(totalBoxes.toString());
                exportWMSDataList.add(exportWMSData);//保存上一批的数据

                //清空
                exportWMSData = new ExportWMSData();
                exportWMSDetailList.clear();
            }

        }

        redisUtils.set(EXPORTWMSDATALIST+"_"+userId, JSON.toJSONString(exportWMSDataList, SerializerFeature.DisableCircularReferenceDetect), -1);


    }
}

/**
 * ExportWMSData,导出WMS数据
 */
@Data
class ExportWMSData {

    //发货单号
    @JSONField(ordinal = 1)
    private String consignNum;

    //客户名称
    @JSONField(ordinal = 2)
    private String customerName;

    //送货日期
    @JSONField(ordinal = 3)
    private String deliveryDate;

    //客户地址
    @JSONField(ordinal = 4)
    private String customerAddress;

    //收货人及电话
    @JSONField(ordinal = 5)
    private String consigneeAndTelephone;

    //备注
    @JSONField(ordinal = 6)
    private String remarks;

    //合计(送货数量)
    @JSONField(ordinal = 7)
    private String totalDeliveryQuantity;

    //合计(箱数)
    @JSONField(ordinal = 8)
    private String totalBoxes;

    //明细
    @JSONField(ordinal = 9)
    List<ExportWMSDetail> details;

}

/**
 * ExportWMSDetail,导出WMS明细
 */
@Data
class ExportWMSDetail {

    //合同号
    @JSONField(ordinal = 1)
    private String contractNumber;

    //品名
    @JSONField(ordinal = 2)
    private String tradeName;

    //型号及规格
    @JSONField(ordinal = 3)
    private String specification;

    //送货数量
    @JSONField(ordinal = 4)
    private String deliveryNumber;

    //单位
    @JSONField(ordinal = 5)
    private String units;

    //箱数
    @JSONField(ordinal = 6)
    private String boxNumber;

    //箱号
    @JSONField(ordinal = 7)
    private String caseNumber;

}
