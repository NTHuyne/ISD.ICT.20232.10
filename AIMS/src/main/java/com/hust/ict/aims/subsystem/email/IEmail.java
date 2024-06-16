package com.hust.ict.aims.subsystem.email;

public interface IEmail{
    void sendEmail(String destEmail, String content, String subject);
}
