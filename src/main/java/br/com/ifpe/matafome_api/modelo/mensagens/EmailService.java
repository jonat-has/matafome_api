package br.com.ifpe.matafome_api.modelo.mensagens;

import java.nio.charset.StandardCharsets;
import java.util.Properties;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import br.com.ifpe.matafome_api.modelo.cliente.Cliente;
import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * @author Jonathas Gabriel
 */
@Component
public class EmailService {

    @Value("${spring.mail.username}")
    String username;

    @Value("${spring.mail.password}")
    String password;

    @Value("${spring.mail.host}")
    String host;

    @Value("${spring.mail.port}")
    int port;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    String smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    String starttls;

    private JavaMailSender emailSender;

    @Async
    public void enviarEmailConfirmacaoCadastroCliente(Cliente cliente) throws MessagingException {

        String assuntoEmail = "Bem vindo ao nosso aplicativo";

        Context params = new Context();
        params.setVariable("codigo", cliente.getUsuario().getPasswordCode());
        params.setVariable("cliente", cliente);
        params.setVariable("idUser", cliente.getUsuario().getId());
        

        this.sendMailTemplate("bem_vindo_cliente.html", cliente.getUsuario().getUsername(), assuntoEmail, params);
    }

    @Async
    public void enviarEmailConfirmacaoCadastroEmpresa(Empresa empresa) throws MessagingException {

        String assuntoEmail = "Bem vindo ao nosso aplicativo";
        Context params = new Context();

        params.setVariable("codigo", empresa.getUsuario().getPasswordCode());
        params.setVariable("empresa", empresa); 
        params.setVariable("idUser", empresa.getUsuario().getId());
        
        
        this.sendMailTemplate("bem_vindo_empresa.html", empresa.getUsuario().getUsername(), assuntoEmail, params);
    }


    @Async
    private void sendMailTemplate(String template, String to, String subject, Context params) throws MessagingException {

         TemplateEngine templateEngine = new SpringTemplateEngine();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(0);

        templateEngine.setTemplateResolver(templateResolver);

        String content = templateEngine.process(template, params);
        this.sendMail(to, subject, content, Boolean.TRUE);
    }

    @Async
    private void sendMail(String to, String subject, String content, Boolean html) throws MessagingException {

        emailSender = getJavaMailSender();

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {

            helper.setFrom(new InternetAddress("not.reply@delifacil.com.br"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(new String(content.getBytes(), StandardCharsets.ISO_8859_1), html);
            helper.setEncodeFilenames(true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        emailSender.send(message);
    }

    private JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", "false");
        props.put("spring.mail.properties.mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}