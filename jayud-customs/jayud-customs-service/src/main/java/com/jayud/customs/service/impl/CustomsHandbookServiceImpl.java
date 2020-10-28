package com.jayud.customs.service.impl;

import com.google.common.collect.Lists;
import com.jayud.common.dto.SheetDTO;
import com.jayud.common.utils.excel.ExcelPageBase;
import com.jayud.common.utils.excel.ExcelUtils;
import com.jayud.customs.model.po.CustomsHandbookBlacklist;
import com.jayud.customs.service.CustomsHandbookService;
import com.jayud.customs.service.ICustomsHandbookBlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomsHandbookServiceImpl implements CustomsHandbookService {
    @Autowired
    ICustomsHandbookBlacklistService handbookBlacklistService;

    @Override
    public void processHandbookBlacklistCheck(String className, MultipartFile file, HttpServletResponse response) throws Exception {
        Class<?> clz = null;
        String classPath = "com.jayud.customs.model.bo." + className;
        clz = Class.forName(classPath);

        if (null == clz) {
            log.error("找不到数据类{}", classPath);
            throw new RuntimeException("找不到数据类");
        }


        //读取数据
        ExcelPageBase excelPage = (ExcelPageBase) clz.newInstance();
        List<ExcelPageBase> originData = ExcelUtils.getCustomizedExcelInfo(file, excelPage, 0);

        //处理数据
        Map<String, CustomsHandbookBlacklist> blackListMap = handbookBlacklistService.list()
                .stream()
                .collect(Collectors.toMap(CustomsHandbookBlacklist::getName, e -> e));

        List<ExcelPageBase> cleanPage = new ArrayList<>();

        List<ExcelPageBase> dirtyPage = originData.stream().filter(e -> {
            try {
                Method getGoodName = excelPage.getClass().getMethod("getGoodName");
                Object invoke = getGoodName.invoke(e);

                Method setMayError = excelPage.getClass().getMethod("setMayError", Boolean.class);
                Method setReplacement = excelPage.getClass().getMethod("setReplacement", String.class);

                if (null != invoke && blackListMap.containsKey(invoke.toString())) {
                    //命中黑名单
                    setMayError.invoke(e, true);
                    setReplacement.invoke(e, blackListMap.get(invoke.toString()).getReplacement());
                    return true;
                } else {
                    //未命中黑名单
                    cleanPage.add(e);
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());


        SheetDTO passed = new SheetDTO("通过筛查", clz, cleanPage, Lists.newArrayList("replacement", "mayError"));
        SheetDTO unSupported = new SheetDTO("包含敏感词", clz, dirtyPage, new ArrayList<>());
        List<SheetDTO> list = Lists.newArrayList(passed, unSupported);

        ExcelUtils.exportMultiPageExcel(list, "导出文件", response);

    }
}

