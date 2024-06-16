package com.hust.ict.aims.controller.admin;

import com.hust.ict.aims.entity.user.User;
import com.hust.ict.aims.subsystem.email.IEmail;
import com.hust.ict.aims.subsystem.email.simplejavamail.SimplejavamailManager;

public class UserEmailSender {
    public static void sendUserEmail(User user, String action){
        IEmail mail = new SimplejavamailManager();
        String role = "";
        if (user.getIsAdmin()){
            role = "Admin";
        }
        else{
            role = "Product Manager";
        }
        String mailContent = "AIMS GROUP-10 NOTIFICATION\n\n" + action + "username: " + user.getUsername() + "\npassword: " + user.getPassword() + "\nemail: " + user.getEmail() + "\nrole: " + role;
        mail.sendEmail(user.getEmail(), mailContent, "AIMS GROUP-10 NOTIFICATION");
    }

}
