package com.jayud.service;

import com.jayud.model.po.Email;

/**
 * @author Daneil
 */
public interface MailService {
    /**
     * 发送邮件带附件
     * @param emailForm
     * @return
     */
    Boolean sendMailWithAttachments(Email emailForm);
}
