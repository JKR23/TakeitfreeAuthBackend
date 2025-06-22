package com.takeitfree.auth.service.impl;

import com.takeitfree.auth.dto.ResponseLinkForResetPassword;
import com.takeitfree.auth.exceptions.TokenExpiredException;
import com.takeitfree.auth.models.PasswordResetToken;
import com.takeitfree.auth.models.User;
import com.takeitfree.auth.repositories.PasswordResetTokenRepository;
import com.takeitfree.auth.repositories.UserRepository;
import com.takeitfree.auth.service.PasswordResetTokenService;
import com.takeitfree.auth.validators.ObjectValidator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final ObjectValidator objectValidator;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(PasswordResetTokenServiceImpl.class);

    @Value("${frontend.url}")
    private String frontendPath;

    @Override
    @Transactional
    public ResponseLinkForResetPassword sendLinkResetPassword(String email) {

        Optional<PasswordResetToken> existingTokenOptional = getTokenLinkedWithThatEmail(email);

        // If a token is associated to this user, then we delete it
        existingTokenOptional.ifPresent(passwordResetTokenRepository::delete);

        PasswordResetToken passwordResetToken = generateTokenAndCreatePasswordResetToken(email);

        this.passwordResetTokenRepository.save(passwordResetToken);

        String bodyMessage = generateBodyMessageWithResetLink(passwordResetToken.getToken());

        String subject = "Password Reset Link";

        return sendWithSmtpJavaMail(email, subject, bodyMessage);
    }

    @Override
    @Transactional
    public String resetPassword(String token, String newPassword) {
        this.objectValidator.validate(token);
        this.objectValidator.validate(newPassword);

        PasswordResetToken passwordResetTokenAlreadyUsed = validatePasswordResetToken(token);

        User user = userRepository.findByEmail(passwordResetTokenAlreadyUsed.getEmail());

        user.setPassword(this.passwordEncoder.encode(newPassword));

        this.passwordResetTokenRepository.delete(passwordResetTokenAlreadyUsed);

        return "password reset successful";
    }

    @PostConstruct
    public void checkJavaMailConfig() {
        if (javaMailSender instanceof JavaMailSenderImpl sender) {
            logger.info("=== MAIL CONFIGURATION ===");
            logger.info("Host: {}", sender.getHost());
            logger.info("Port: {}", sender.getPort());
            logger.info("Username: {}", sender.getUsername());
            logger.info("Protocol: {}", sender.getProtocol());
            logger.info("Properties: {}", sender.getJavaMailProperties());
        } else {
            logger.warn("JavaMailSender is not an instance of JavaMailSenderImpl");
        }
    }

    @Override
    public void checkTokenValidity(String token) {
        validatePasswordResetToken(token);
    }

    private PasswordResetToken validatePasswordResetToken(String token) {
        this.objectValidator.validate(token);

        PasswordResetToken passwordResetToken = this.passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null) {
            throw new EntityNotFoundException("This token doesn't exist or is expired");
        }

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new TokenExpiredException("This token has expired");
        }

        return passwordResetToken;
    }

    private void sendWithSmtpJavaMailAuxiliary(String recipientEmail, String subject, String body) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kuntualajulio7@gmail.com"); //String sendFrom = "support@takeitfree.com";
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(body);
        this.javaMailSender.send(message);

    }

    private ResponseLinkForResetPassword sendWithSmtpJavaMail(String recipientEmail, String subject, String bodyMessage){
        try {
            sendWithSmtpJavaMailAuxiliary(recipientEmail,subject,bodyMessage);
            logger.info("Password reset email sent successfully to {}", recipientEmail);

            return ResponseLinkForResetPassword.builder()
                    .message("Please, check your e-mail")
                    .build();

        } catch (MailException e) {
            logger.error("Failed to send password reset email to {}: {}", recipientEmail, e.getMessage());
            throw new RuntimeException("Failed to send password reset email. Please try again later.", e);
        }
    }

    private Optional<PasswordResetToken> getTokenLinkedWithThatEmail(String email) {

        this.objectValidator.validate(email);

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("This email doesn't exist");
        }

        return Optional
                .ofNullable(passwordResetTokenRepository.findByEmail(email));
    }

    private PasswordResetToken generateTokenAndCreatePasswordResetToken(String email) {
        //generate a new token
        String token = UUID.randomUUID().toString();

        //build object PasswordResetToken : necessary -> for being sur that user exist in the db and check token
        return PasswordResetToken.builder()
                // Because it's a generation.identity so automatic.id(1L)
                .token(token)
                .email(email)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
    }

    private String generateBodyMessageWithResetLink(String token) {

        String resetLink = frontendPath+"?token="+token;

        return "Dear user,\n\nPlease use the following link to reset your password:\n"
                + resetLink + "\n\nNote: This link will expire in 30 minutes.\n\nBest regards,\nYour Support Team";
    }
}
