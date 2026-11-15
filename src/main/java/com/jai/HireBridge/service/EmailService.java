package com.jai.HireBridge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.jai.HireBridge.event.ApplicationStatusChangedEvent;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	private final String from;
	
	public EmailService(JavaMailSender mailSender,TemplateEngine templateEngine,
			            @Value("${spring.mail.username}") String from) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
		this.from = from;
	}
	
	public void sendStatusEmail(ApplicationStatusChangedEvent e) throws Exception {
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
		helper.setText(html,true);
		
		mailSender.send(mime);
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
