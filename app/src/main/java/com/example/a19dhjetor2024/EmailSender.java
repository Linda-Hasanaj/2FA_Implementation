package com.example.a19dhjetor2024;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class EmailSender {

    public static void sendEmail(String recipient, String code){
        final String senderEmail = "here goes your email";
        final String senderAppPassword = "here goes your generated app password";

        new Thread(() -> {
            try {
                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderAppPassword);
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject("Your Verification Code");
                message.setText("Your verification code is: " + code);

                Transport.send(message);
                System.out.println("Email sent successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    }

