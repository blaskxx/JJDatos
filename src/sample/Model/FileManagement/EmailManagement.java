package sample.Model.FileManagement;

import sample.Main;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Properties;

/**
 * Created by Jose on 21/09/2014.
 */
public class EmailManagement {
    private final String host = "smtp.gmail.com";
    private String email;
    private String password;
    private ServerInformation serverInformation;
    private final String pathServiceInformation="src\\FileMonitor\\serverConfiguration.ser";

    public EmailManagement(){
        loadServerInformation();
    }
    public boolean loadServerInformation(){

        File encrypth=new File(pathServiceInformation);
        File deEncrypth= new File(pathServiceInformation);
        try
        {
            Encrypth.decrypt(Main.keyEncrypth, encrypth, deEncrypth);
            FileInputStream fileIn = new FileInputStream(pathServiceInformation);
            System.out.println(fileIn.available());
            if(fileIn.available()>1){
                ObjectInputStream in = new ObjectInputStream(fileIn);
                serverInformation = (ServerInformation) in.readObject();
                email=serverInformation.getEmailAddress();
                password=serverInformation.getPasswordAddress();
                in.close();
                fileIn.close();

                Encrypth.encrypt(Main.keyEncrypth,deEncrypth,encrypth);
                return true;
            }else{
                Encrypth.encrypt(Main.keyEncrypth,deEncrypth,encrypth);
                return false;
            }


        }catch(CryptoException|IOException i)
        {
            i.printStackTrace();
            return false;
        }catch(ClassNotFoundException c)
        {
            System.out.println("class not found");
            c.printStackTrace();
            return false;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean send(String from, String[] recipientsTo, String subject, String body) {

        try {

            Properties properties = System.getProperties();
            setMailServerProperties(this.email+"@gmail.com", this.password, host, properties);

            Session session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication(email+"@gmail.com", password);
                        }
                    });

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            setAllRecipients(recipientsTo, message);
            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);



            message.setContent(multipart);

            Transport.send(message);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

    }

    private static void setAllRecipients(String[] recipientsTo, MimeMessage message) throws MessagingException {
        setRecipients(recipientsTo, MimeMessage.RecipientType.TO, message);

    }

    private static void setRecipients(String[] recipients, Message.RecipientType recipientsType, MimeMessage message) throws MessagingException {
        if (recipients != null) {
            for (String recipient : recipients) {
                message.addRecipient(recipientsType, new InternetAddress(recipient));
            }
        }
    }

    private static void setMailServerProperties(String username, String password, String host, Properties properties) {
        properties.setProperty("mail.user", username);
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.password", password);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }

}
