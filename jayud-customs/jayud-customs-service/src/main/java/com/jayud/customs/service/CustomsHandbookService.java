package com.jayud.customs.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 手册处理方法接口
 */
public interface CustomsHandbookService {
    void processHandbookBlacklistCheck(String className, MultipartFile file, HttpServletResponse response) throws Exception;
}
