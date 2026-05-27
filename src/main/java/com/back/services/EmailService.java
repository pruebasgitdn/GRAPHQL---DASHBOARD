package com.back.services;

import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${resend.token}")
    private String emailToken;

    @Value("${resend.email.to}")
    private String emailTo;

    @Value("${app.frontend.url}")
    private String clientUrl;


    @Async
    public void sendEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("daniloospina45@gmail.com");
            message.setSubject("test 01 SUBJ");
            message.setText("test 01 TEXT");

            javaMailSender.send(message);

            System.out.println("EMAIL ENVIADO");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // a 1
    @Async
    public void sendWorkspaceInvitation(
            String email,
            String token,
            String wspaceName
    ) {

        String link = clientUrl +
                "/dashboard/workspaces?token=" + token;

        try {

            //mimemessage => me permite el envio de lniks imagenes archivos etc
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);

            helper.setSubject("Te invitaron a " + wspaceName);

            helper.setText("""
                <h2>Fuiste invitado a unirte a %s</h2>

                <p>Hacé click en el botón para aceptar:</p>

                <a href="%s"
                   style="
                        background:#16a34a;
                        color:white;
                        padding:10px 20px;
                        border-radius:4px;
                        text-decoration:none;
                        display:inline-block;
                   ">
                    Aceptar invitación
                </a>

                <p>Este link expira en 48 horas.</p>
                """
                    .formatted(wspaceName, link), true);

            javaMailSender.send(message);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
