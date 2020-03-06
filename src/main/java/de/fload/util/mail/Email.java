package de.fload.util.mail;

import de.fload.core.Settings;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class Email {
    public static void sendMail(String subject, String body) {
        var settings = Settings.getInstance();

        String fromEmail = settings.getFromEmail();
        String password = settings.getPassword();
        String toEmail = settings.getToEmail();

        Properties props = new Properties();
        props.put("mail.smtp.host", settings.getSmtpHost());
        props.put("mail.smtp.port", settings.getSmtpPort());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, fromEmail, toEmail, subject, body);
    }

    public static void sendError(String exceptionMessage, String stacktrace) {
        String subject = "Exception with Message: " + exceptionMessage;
        String body = "Stacktrace:\n" + stacktrace;
        sendMail(subject, body);
    }
}
