package com.hust.ict.aims.subsystem.email.simplejavamail;

import com.hust.ict.aims.subsystem.email.IEmail;
import org.simplejavamail.api.email.Email;

public class SimplejavamailManager implements IEmail {
    private final SimplejavamailController simplejavamailController =  new SimplejavamailController();
    @Override
    public void sendEmail(String destEmail, String content, String subject) {
        Email email = simplejavamailController.prepareEmail(destEmail, content, subject);
        simplejavamailController.sendMail(email);
    }

    public static void main(String[] args) {
        IEmail mail = new SimplejavamailManager();
        mail.sendEmail("haidang2003.namha@gmail.com", "Hello this is the message from the AIMS-GROUP 10. This is your order of total 1080000VND. You will get the order at your own home soon", "AIMS-GROUP 10 Notification");
    }
}