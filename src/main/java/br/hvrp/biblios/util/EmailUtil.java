package br.hvrp.biblios.util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailUtil {
	public static void sendConfirmationEmail(String to, String name, String token) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("hugovrp@gmail.com", "kwumajxanovgjjmv");
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("hugovrp@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("Biblios - Confirmação");
        
        message.setText("Olá " + name + ",\n\nConfirme o seu cadastro no link: http://localhost:8080/Biblios/confirm.xhtml?token=" + token);
        
        Transport.send(message);
    }
	
	public static void sendOverdueNotice(String to, String name, String magazineTitle) throws MessagingException {
	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

	    Session session = Session.getInstance(props, new Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication("hugovrp@gmail.com", "kwumajxanovgjjmv");
	        }
	    });

	    Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress("hugovrp@gmail.com"));
	    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	    message.setSubject("Biblios - Aviso de Atraso");
	    
	    message.setText("Olá " + name + ",\n\nIdentificamos que a revista '" + magazineTitle + 
	                    "' está com a devolução atrasada. Por favor, entregue-a o quanto antes.\n\nAtenciosamente,\nEquipe Biblios.");
	    
	    Transport.send(message);
	}
}
