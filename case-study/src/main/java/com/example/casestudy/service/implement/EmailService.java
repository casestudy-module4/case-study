package com.example.casestudy.service.implement;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

//    @Async
//    public void sendOtpEmail(String to, String otp) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("Your OTP Code");
//        message.setText("Your OTP code is: " + otp + ". This code will expire in 5 minutes.");
//        mailSender.send(message);
//    }
@Async
public void sendOtpEmail(String to, String otp) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setTo(to);
    helper.setSubject("Your OTP Code");

    String htmlContent = "<!DOCTYPE html>"
            + "<html lang='en'>"
            + "<head><meta charset='UTF-8'></head>"
            + "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>"
            + "<h2 style='color: #4CAF50;'>Your OTP Code</h2>"
            + "<p>Hello,</p>"
            + "<p>Your OTP code is:</p>"
            + "<h1 style='color: #FF5722;'>" + otp + "</h1>"
            + "<p>This code will expire in 5 minutes. Please do not share it with anyone.</p>"
            + "<br>"
            + "<p style='color: #888;'>Best regards,<br>Your Company Name</p>"
            + "</body>"
            + "</html>";

    helper.setText(htmlContent, true);
    mailSender.send(message);
}
    @Async
    public void sendPaymentSuccessEmail(String to, String customerName, String orderId, String amount) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Payment Successful");

        String htmlContent = "<!DOCTYPE html>"
                + "<html lang='en'>"
                + "<head><meta charset='UTF-8'></head>"
                + "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                + "<h2 style='color: #4CAF50;'>Thank You for Your Payment!</h2>"
                + "<p>Dear " + customerName + ",</p>"
                + "<p>We are pleased to inform you that your payment has been successfully processed.</p>"
                + "<p><strong>Order ID:</strong> " + orderId + "</p>"
                + "<p><strong>Total Amount:</strong> $" + amount + "</p>"
                + "<p>If you have any questions, feel free to contact us.</p>"
                + "<br>"
                + "<p style='color: #888;'>Best regards,<br>Your Company Name</p>"
                + "</body>"
                + "</html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
