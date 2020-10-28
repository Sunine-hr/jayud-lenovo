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
        List<String> blackList = handbookBlacklistService.list()
                .stream()
                .map(CustomsHandbookBlacklist::getName)
                .collect(Collectors.toList());
        List<ExcelPageBase> cleanPage = new ArrayList<>();

        List<ExcelPageBase> dirtyPage = originData.stream().filter(e -> {
            try {
                Method getGoodName = excelPage.getClass().getMethod("getGoodName");
                Object invoke = getGoodName.invoke(e);
                if (null != invoke && blackList.contains(invoke.toString())) {
                    return true;//命中黑名单
                } else {
                    cleanPage.add(e);
                    return false;//未命中黑名单
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());


        SheetDTO passed = new SheetDTO("通过筛查", clz, cleanPage);
        SheetDTO unSupported = new SheetDTO("包含敏感词", clz, dirtyPage);
        List<SheetDTO> list = Lists.newArrayList(passed, unSupported);

        ExcelUtils.exportMultiPageExcel(list, "导出文件", response);

    }
}

