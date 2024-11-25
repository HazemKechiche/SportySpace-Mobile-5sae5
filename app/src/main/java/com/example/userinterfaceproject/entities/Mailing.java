package com.example.userinterfaceproject.entities;

import android.os.AsyncTask;
import android.content.Context;
import android.widget.Toast;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class Mailing {

    public void sendVerificationEmail(Context context, String email, String code) {
        new SendEmailTask(context, email, code).execute();
    }

    private static class SendEmailTask extends AsyncTask<Void, Void, Void> {
        private Context context;
        private String email;
        private String code;

        SendEmailTask(Context context, String email, String code) {
            this.context = context;
            this.email = email;
            this.code = code;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final String username = "hazemkechiche@gmail.com";
            final String password = "qivywqdvnbkcapih"; // Use a secure way to store passwords

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("hazemkechiche@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject("Password Reset Code");
                message.setText("Your verification code is: " + code);

                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Update UI after email is sent
            Toast.makeText(context, "Verification code sent!", Toast.LENGTH_SHORT).show();
        }
    }
}
