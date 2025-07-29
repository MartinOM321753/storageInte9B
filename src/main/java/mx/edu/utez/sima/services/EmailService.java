package mx.edu.utez.sima.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mx.edu.utez.sima.modules.Email.Emails;
import mx.edu.utez.sima.utils.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(Emails email , int plantilla) throws MessagingException {
        try {

            Context context = new Context();

            context.setVariable("message", email.getMensaje());
            context.setVariable("name", email.getSubject());

            String[] plantillaAlerta = new String[]{"verify","email","welcome"};

            String htmlContent = templateEngine.process(plantillaAlerta[plantilla], context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email.getDestinatario());

            helper.setText(htmlContent, true);
            helper.setFrom("ultranet.sa.d.cv@gmail.com");
            javaMailSender.send(message);

            log.info("Correo HTML enviado con exito a {}", email.getDestinatario());

            new ResponseEntity<>(new APIResponse("",email,false, HttpStatus.OK ), HttpStatus.OK);

        } catch (Exception e) {

            log.error("Error al enviar el coreo {}", e.getMessage());
            new ResponseEntity<>(new APIResponse("No se envio el email",true, HttpStatus.BAD_REQUEST), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}
