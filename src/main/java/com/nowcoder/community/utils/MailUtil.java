package com.nowcoder.community.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author 刘逸菲
 * @create 2022-03-18 22:30
 * 发送邮件工具类
 **/
@Component
public class MailUtil {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    /**
     * 框架注入
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 邮件发送者
     */
    @Value("${spring.mail.username}")
    private String mailFrom;

    /**
     * tymeleaf模板引擎
     */
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送邮件内容
     * @param mailTo 邮件收件人
     * @param content 邮件内容
     * @param mailSubject 邮件主题
     * 主要是通过构建MimeMessage[邮件信息类],并交由框架 JavaMailSender 执行send(MimeMessage)
     * 方法 进行邮件信息的发送
     */
    public void sendMailMessage(String mailTo,String content,String mailSubject){

        try {

            //构建MimeMessage，设置邮件主体信息
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
            helper.setFrom(mailFrom);
            helper.setTo(mailTo);
            helper.setSubject(mailSubject);
            //邮件内容设置为HTML格式
            helper.setText(content,true);

            //发送邮件
            mailSender.send(helper.getMimeMessage());

        } catch (MessagingException e) {
            logger.error("邮件发送失败：{}",e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 使用邮件模板引擎发送相关信息
     * @param mailTo 收件人
     * @param mailTemplatePath 邮件模板引擎的地址
     * @param param 邮件参数 与设定的模板引擎有关
     * @param mailSubject 邮件主旨
     */
    public void sendMailMessageHtml(String mailTo,String mailTemplatePath,Object param,String mailSubject){

    }
}
