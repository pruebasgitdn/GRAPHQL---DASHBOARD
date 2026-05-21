package com.back.services;

import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailService {

    @Value("${resend.token}")
    private String emailToken;

    @Value("${resend.email.to}")
    private String emailTo;

    @Value("${app.frontend.url}")
    private String clientUrl;


    public void sendEmail() {
        Resend resend = new Resend(emailToken);

        try {
            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("Acme <onboarding@resend.dev>")
                    .to("pruebasgitdn@gmail.com")
                    .subject("it works!")
                    .html("<h1>Hi there!</h1><p>Here's your email from Resend</p> <p>SUSCRIBE TO ERICKDEVV</p>")
                    .build();
            resend.emails().send(params); //Enviar
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    // a 1
    public void sendWorkspaceInvitation(String email,
                                        String token,
                                        String wspaceName){

        Resend resend = new Resend(emailToken);

        // ? => opcional parametro en la urldel cliente
    String link = clientUrl + "/dashboard/worskpace?token=" + token;

    try{
        //options / config
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("App <onboarding@resend.dev>")
                .to(email)
                .subject("Te invitaron a " + wspaceName)
                .html("""
                        <h2>Fuiste invitado a unirte a %s</h2>
                        <p>Hacé click en el botón para aceptar:</p>
                        <a href="%s" 
                           style="background:#16a34a;color:white;padding:10px 20px;
                                  border-radius:6px;text-decoration:none;">
                            Aceptar invitación
                        </a>
                        <p>Este link expira en 48 horas.</p>
                    """.formatted(wspaceName, link))
                .build();

        resend.emails().send(params);//Enviar


    } catch (ResendException ex) {
        ex.printStackTrace(); //debug seguimiento del errro en caso de
    }


    }
}
