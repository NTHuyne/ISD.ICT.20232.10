package com.hust.ict.aims.subsystem.email.simplejavamail;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class SimplejavamailController {
    protected Email prepareEmail(String destEmail, String content, String subject){
        return EmailBuilder.startingBlank().from("tronghuy230903@gmail.com").to(destEmail).withPlainText(content).withSubject(subject).buildEmail();
    }

    public void sendMail(Email email){
        Mailer mailer = MailerBuilder
                .withSMTPServer(SimplejavamailConfigs.getInstance().getHost(), SimplejavamailConfigs.getInstance().getPort(), SimplejavamailConfigs.getInstance().getUsername(), SimplejavamailConfigs.getInstance().getPassword())
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(2 * 1000)
                .clearEmailValidator() // turns off email validation
                .withProperty("mail.smtp.sendpartial", true)
                .withDebugLogging(true)
                .async()
                .buildMailer();
        mailer.sendMail(email);
    }
}
