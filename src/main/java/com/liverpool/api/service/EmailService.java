package com.liverpool.api.service;

import com.liverpool.api.dto.EmailProperties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Properties;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String MSG_PREPARE_SEND_EMAIL = "Preparing to send email to {}";
    private static final String MSG_EMAIL_SUCCESS = "Email successfully sent to {}";
    private static final String MSG_EMAIL_FAILED = "Failed to send email to {}";
    private final EmailProperties emailProperties;
    private final SpringTemplateEngine springTemplateEngine;
    private final Session session;

    public EmailService(EmailProperties emailProperties, SpringTemplateEngine springTemplateEngine) {
        this.emailProperties = emailProperties;
        this.springTemplateEngine = springTemplateEngine;

        Properties properties = new Properties();
        properties.put("mail.smtp.from", emailProperties.getEmail_user());
        properties.put("mail.smtp.host", emailProperties.getHost());
        properties.put("mail.smtp.port", emailProperties.getPort());
        properties.put("mail.transport.protocol", emailProperties.getProtocol());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // STARTTLS habilitado
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Necesario para Gmail
        properties.put("mail.smtp.connectiontimeout", "5000");
        properties.put("mail.smtp.timeout", "5000");
        properties.put("mail.smtp.writetimeout", "5000");

        this.session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailProperties.getEmail_user(), emailProperties.getEmail_password());
            }
        });
    }

    public void sendEmail(String adresses, String tittleEmail, String htmlTemplateName, Context context) {
        logger.info(MSG_PREPARE_SEND_EMAIL, adresses);
        try {
            String htmlContent = springTemplateEngine.process(htmlTemplateName, context);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailProperties.getEmail_user(), emailProperties.getEmail_user()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adresses));
            message.setSubject(tittleEmail);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlContent, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            Transport.send(message);
            logger.info(MSG_EMAIL_SUCCESS, adresses);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MSG_EMAIL_FAILED, adresses);
        }
    }
}