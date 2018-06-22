package com.demo.architect.utils.view;

import android.util.Log;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * Created by Hao-Imark on 17/12/2016.
 */

public class SendMailUtil {

    public static void sendMail(final int userid, final String username, final String phone_user, final String filePath, final String version, final String nameData)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    send(userid, username, phone_user , filePath, version, nameData);

                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }).start();

    }

    public static void send(long userid, String userName, String userPhone, String filepath, String version, String nameDatabase)
    {

        // Recipient's email ID needs to be mentioned.
//        String to = "qcimark@gmail.com";
        String to = "imarkcomp@gmail.com";

        // Sender's email ID needs to be mentioned
//        final String from = "qcimark@gmail.com";
        final String from = "imarkcomp@gmail.com";
        // final String username = "xyz";
//        final String pass = "2017Im@rk";
        final String pass = "Imark2017";
        // Assuming you are sending email from localhost
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.user", from);
        properties.put("mail.smtp.password", pass);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.auth", "true");

        //Read more: http://mrbool.com/how-to-work-with-java-mail-api-in-android/27800#ixzz3E2T8ZbpJ

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Scan_AC" + "_" + userName + "_" + userPhone + "_" + version);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText("Message body");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filepath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(userid + "_" + userPhone + "_" +nameDatabase);
            multipart.addBodyPart(messageBodyPart);


            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

            // Send the complete message parts
            message.setContent(multipart);
            // Send message
            Transport.send(message);
            Log.e("Sent message", "successfully....");

        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
