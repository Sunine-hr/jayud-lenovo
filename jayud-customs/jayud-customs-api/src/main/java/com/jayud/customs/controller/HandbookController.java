package com.jayud.customs.controller;

import com.jayud.common.CommonResult;
import com.jayud.common.utils.excel.ExcelUtils;
import com.jayud.customs.model.bo.HandbookBlacklistForm;
import com.jayud.customs.service.IHandbookBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/handbook")
public class HandbookController {
    @Autowired
    IHandbookBlacklistService handbookBlacklistService;
    @PostMapping("/process/name/excel")
    public CommonResult processExcel(@RequestPart(name = "file") MultipartFile file) {
        List<HandbookBlacklistForm> excelInfo = ExcelUtils.getExcelInfo(file, HandbookBlacklistForm.class, 0);



        //        CustomsHandbookBlacklist blacklist = new CustomsHandbookBlacklist();
//        blacklist.setName("123123");
//        blacklist.setCreateUser("admin");
//        blacklist.setCreateTime(LocalDateTime.now());
//        blacklist.setReplacement("234234");
//        handbookBlacklistService.save(blacklist);
        return CommonResult.success();
    }
}
