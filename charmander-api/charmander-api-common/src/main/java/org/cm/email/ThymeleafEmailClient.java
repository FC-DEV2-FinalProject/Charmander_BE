package org.cm.email;

import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.cm.exception.CommonApiException;
import org.cm.exception.CommonApiExceptionCode;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
public class ThymeleafEmailClient implements EmailClient {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void send(EmailDto dto) {
        try {
            var message = javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            var context = new Context();
            context.setVariables(dto.getVariables());

            var html = templateEngine.process(dto.getTemplate(), context);

            helper.setSubject(dto.getSubject());
            helper.setFrom(dto.getFrom());
            helper.setTo(dto.getTo());
            helper.setText(html, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new CommonApiException(CommonApiExceptionCode.EMAIL_SENT_FAIL);
        }
    }
}
