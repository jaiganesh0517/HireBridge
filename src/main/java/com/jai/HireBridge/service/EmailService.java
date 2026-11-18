package com.jai.HireBridge.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.jai.HireBridge.event.ApplicationStatusChangedEvent;
import com.jai.HireBridge.exception.EmailSendFailedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.SendFailedException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private static final int MAX_ATTEMPTS = 3;
    private static final long INITIAL_DELAY_MS = 2000;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String from;

    public EmailService(JavaMailSender mailSender,
                        TemplateEngine templateEngine,
                        @Value("${spring.mail.username}") String from) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.from = from;
    }

    public int sendStatusEmail(ApplicationStatusChangedEvent e) throws Exception {   // returns attempts used
        MimeMessage mime = buildMessage(e);
        return sendWithRetry(mime, e.studentEmail());
    }

    private int sendWithRetry(MimeMessage mime, String to) {
        long delay = INITIAL_DELAY_MS;

        for (int attempt = 1; ; attempt++) {
            try {
                mailSender.send(mime);
                return attempt;
            } catch (MailException ex) {
                if (!isTransient(ex)) {
                    log.error("Permanent mail failure for {}, not retrying", to);
                    throw new EmailSendFailedException(attempt, ex);
                }
                if (attempt == MAX_ATTEMPTS) {
                    log.error("Giving up on {} after {} attempts", to, attempt);
                    throw new EmailSendFailedException(attempt, ex);
                }
                log.warn("Attempt {}/{} to email {} failed ({}), retrying in {} ms",
                        attempt, MAX_ATTEMPTS, to, ex.getMessage(), delay);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new EmailSendFailedException(attempt, ex);
                }
                delay *= 2;
            }
        }
    }

    private boolean isTransient(MailException ex) {
        if (ex instanceof MailAuthenticationException
                || ex instanceof MailParseException
                || ex instanceof MailPreparationException) {
            return false;                                 // wrong password, bad message
        }
        if (ex instanceof MailSendException) {
            for (Throwable t = ex.getCause(); t != null; t = t.getCause()) {
                if (t instanceof SendFailedException) {
                    return false;                         // e.g. invalid recipient address
                }
            }
            return true;                                  // timeouts, dropped connections
        }
        return false;
    }

    private MimeMessage buildMessage(ApplicationStatusChangedEvent e) throws Exception {
        Context ctx = new Context();
        ctx.setVariable("studentName", e.studentName());
        ctx.setVariable("jobTitle", e.jobTitle());
        ctx.setVariable("companyName", e.companyName());
        ctx.setVariable("status", e.newStatus());
        ctx.setVariable("headline", headline(e.newStatus().name()));
        ctx.setVariable("message", message(e.newStatus().name()));

        String html = templateEngine.process("status-update", ctx);

        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, "UTF-8");
        helper.setFrom(from);
        helper.setTo(e.studentEmail());
        helper.setSubject("Update on your application: " + e.jobTitle());
        helper.setText(html, true);
        return mime;
    }

	
	public String headline(String status) {
		return switch (status) {
		    case "SHORTLISTED" -> "Good news, you're shortlisted!";
		    case "SELECTED" -> "Congratulations, you're selected!";
		    case "REJECTED" -> "An update on your application";
		    default -> "Your application status changed";
		};	
	}
	
	public String message(String status) {
		return switch (status) {
		case "SHORTLISTED" -> "The recruiter will reach out about next steps.";
		case "SELECTED" -> "The recruiter will contact you with the details.";
		case "REJECTED" -> "Thank you for applying. Keep going, the right role is out there.";
		default -> "Log in to HireBridge to see the details.";
		};
	}
}
