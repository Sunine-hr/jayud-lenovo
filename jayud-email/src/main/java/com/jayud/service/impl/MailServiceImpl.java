package com.jayud.service.impl;

import com.jayud.common.utils.FileUtil;
import com.jayud.common.utils.FileView;
import com.jayud.model.po.Email;
import com.jayud.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daneil
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Override
    public Boolean sendMailWithAttachments(Email emailForm) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        List<File> delFiles = new ArrayList<>();
        try {
            /*
            第二个参数true表示构造一个multipart message类型的邮件，multipart message类型的邮件包含多个正文、附件以及内嵌资源，
            邮件的表现形式更丰富
             */
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            mimeMessageHelper.setSubject(emailForm.getSubject());
            mimeMessageHelper.setText(emailForm.getText());

            // 设置多个收件人
            String[] toAddress = emailForm.getTo().split(",");
            mimeMessageHelper.setTo(toAddress);
            if (!StringUtils.isEmpty(emailForm.getCc())) {
                mimeMessageHelper.setCc(emailForm.getCc());
            }
            // 添加附件
            if (null != emailForm.getFileViews()) {
                for (FileView fileView : emailForm.getFileViews()) {
                    File file = FileUtil.downloadFromUrl(fileView.getAbsolutePath(), fileView.getFileName());
                    delFiles.add(file);
                    mimeMessageHelper.addAttachment(file.getName(), file);
                }
            }
            //发送邮件
            mailSender.send(mimeMessage);

            //删除生成的附件
            delFiles.forEach(File::delete);
        } catch (MessagingException e) {
            log.info("邮件发送失败 异常：{}", e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Boolean send(com.jayud.common.entity.Email email) {
        try {
            javaMailSender.setHost(email.getHost());
            javaMailSender.setUsername(email.getFrom());
            javaMailSender.setPassword(email.getPassword());
            javaMailSender.setDefaultEncoding("Utf-8");
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(email.getFrom(), email.getTo());                //sender为自定义显示发件人名称
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText());
            javaMailSender.send(mimeMessage);//邮件发送完毕
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.info("邮件发送失败 异常：{}", e.getMessage());
            return false;
        }
        return true;
    }
}
