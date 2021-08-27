package com.jayud.finance.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.HttpUtil;
import com.jayud.common.utils.HttpUtils;
import com.jayud.common.utils.excel.EasyExcelEntity;
import com.jayud.common.utils.excel.EasyExcelUtils;
import com.jayud.finance.enums.BillTemplateEnum;
import com.jayud.finance.feign.OauthClient;
import com.jayud.finance.service.BillTemplateService;
import com.jayud.finance.service.CommonService;
import com.jayud.finance.service.IOrderBillCostTotalService;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.jayud.finance.util.StringUtils;
import com.jayud.finance.vo.SheetHeadVO;
import com.jayud.finance.vo.ViewBillVO;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 应付账单模板
 */
@Service
public class PayBillTemplateServiceImpl implements BillTemplateService {

    @Autowired
    IOrderPaymentBillDetailService billDetailService;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private IOrderBillCostTotalService orderBillCostTotalService;
    @Autowired
    private CommonService commonService;

    @Override
    public void export(Map<String, Object> param) throws IOException {
        JSONObject paramJson = new JSONObject(param);
        String billNo = paramJson.getStr("billNo");
        String cmd = paramJson.getStr("cmd");
        String templateCmd = paramJson.getStr("templateCmd");
        JSONArray datas = paramJson.getJSONArray("datas");

        ViewBillVO viewBillVO = billDetailService.getViewBill(billNo);

        Map<String, Object> callbackArg = new HashMap<>();
        //头部数据重组
        List<SheetHeadVO> sheetHeadVOS = billDetailService.findSSheetHeadInfo(billNo, callbackArg, cmd, templateCmd);
        int index = Integer.parseInt(callbackArg.get("fixHeadIndex").toString()) - 1;
        LinkedHashMap<String, String> headMap = new LinkedHashMap<>();
        LinkedHashMap<String, String> dynamicHead = new LinkedHashMap<>();
        for (int i = 0; i < sheetHeadVOS.size(); i++) {
            SheetHeadVO sheetHeadVO = sheetHeadVOS.get(i);
            headMap.put(sheetHeadVO.getName(), sheetHeadVO.getViewName());
            if (i > index) {
                dynamicHead.put(sheetHeadVO.getName(), sheetHeadVO.getViewName());
            }
        }

        //内部往来关系
        List<String> settlementRelationship = this.commonService.getInternalSettlementRelationship(datas);

        //计算结算币种
        this.orderBillCostTotalService.exportSettlementCurrency(cmd, headMap, dynamicHead, datas, "1");

        //查询人主体信息
        cn.hutool.json.JSONArray tmp = new cn.hutool.json.JSONArray(this.oauthClient
                .getLegalEntityByLegalIds(Collections.singletonList(viewBillVO.getLegalEntityId())).getData());
        cn.hutool.json.JSONObject legalEntityJson = tmp.getJSONObject(0);

        EasyExcelEntity entity = new EasyExcelEntity();
        entity.setSheetName("客户应付对账单");
        //组装标题
        List<String> titles = new ArrayList<>();
        titles.add(viewBillVO.getLegalName());
        titles.add("客户应付款对帐单");
        StringBuilder sb = new StringBuilder();
        titles.add(sb.append("对账日期:")
                .append(viewBillVO.getAccountTermStr()).toString());
        entity.setTitle(titles);
        //组装台头
        List<String> stageHeads = new ArrayList<>();
        stageHeads.add("TO:" + viewBillVO.getCustomerName() + settlementRelationship.get(1));
        sb = new StringBuilder();
        stageHeads.add(sb.append("FR:").append(viewBillVO.getLegalName()).append(settlementRelationship.get(0))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
                .append("账单编号:").append(viewBillVO.getBillNo()).toString());
        entity.setStageHead(stageHeads);
        //组装表头信息
        entity.setTableHead(headMap);
        //组装数据
        entity.setTableData(datas);
        //合计
        LinkedHashMap<String, BigDecimal> costTotal = new LinkedHashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            JSONObject jsonObject = datas.getJSONObject(i);
            dynamicHead.forEach((k, v) -> {
                BigDecimal cost = jsonObject.getBigDecimal(k);
                if (costTotal.get(k) == null) {
                    costTotal.put(k, cost);
                } else {
                    costTotal.put(k, costTotal.get(k).add(cost == null ? new BigDecimal(0) : cost));
                }
            });

        }
        entity.setTotalData(costTotal);
        entity.setTotalIndex(index);

        //尾部
        List<String> bottomData = new ArrayList<>();
        bottomData.add(new StringBuilder()
                .append("公司名称:").append(legalEntityJson.getStr("legalName", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
                .append("制 单 人:").append(viewBillVO.getMakeUser()).toString());
        bottomData.add(new StringBuilder()
                .append("开户银行:").append(legalEntityJson.getStr("bank", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
                .append("制单时间:").append(DateUtils.format(viewBillVO.getMakeTimeStr(), DateUtils.DATE_PATTERN)).toString());
        bottomData.add(new StringBuilder()
                .append("开户账号:").append(legalEntityJson.getStr("accountOpen", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
                .append("审 单 人:").append(viewBillVO.getMakeUser()).toString());
        bottomData.add(new StringBuilder()
                .append("纳税人识别号:").append(legalEntityJson.getStr("taxIdentificationNum", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
                .append("审单时间:").append(DateUtils.format(viewBillVO.getAuditTimeStr(), DateUtils.DATE_PATTERN)).toString());
        bottomData.add(new StringBuilder()
                .append("公司地址:").append(legalEntityJson.getStr("rigisAddress", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL).toString());
        bottomData.add(new StringBuilder()
                .append("联系电话:").append(legalEntityJson.getStr("phone", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL).toString());
        entity.setBottomData(bottomData);

        Workbook workbook = EasyExcelUtils.autoGeneration("", entity);

        HttpServletResponse response = HttpUtils.getHttpResponseServletContext();
        ServletOutputStream out = response.getOutputStream();
        String name = StringUtils.toUtf8String("客户应付对账单");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");

        workbook.write(out);
        workbook.close();
        IoUtil.close(out);
    }
}
