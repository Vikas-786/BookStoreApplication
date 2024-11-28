package com.gkp.onlinebookstore.service;

public interface EmailService {


    void sendRegistrationEmail(String to, String username, String password);

  
    void sendEmail(String to, String subject, String body);
}
