package me.maxim.cvcontactme.controller;

import me.maxim.cvcontactme.dto.ContactRequest;
import me.maxim.cvcontactme.dto.ContactResponse;
import me.maxim.cvcontactme.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/contact")
    public ResponseEntity<ContactResponse> contact(@Valid @RequestBody ContactRequest request,
                                                   HttpServletRequest httpRequest) {
        try {
            String ip = resolveClientIp(httpRequest);
            String userAgent = httpRequest.getHeader("User-Agent");
            String referer = httpRequest.getHeader("Referer");
            String acceptLanguage = httpRequest.getHeader("Accept-Language");

            contactService.send(request.getName(), request.getEmail(), request.getMessage(),
                    ip, userAgent, referer, acceptLanguage);
            return ResponseEntity.ok(ContactResponse.success());
        } catch (RuntimeException ex) {
            log.error("Failed to deliver contact message", ex);
            return ResponseEntity.internalServerError()
                    .body(ContactResponse.failure("Не удалось отправить сообщение. Попробуйте позже."));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ContactResponse> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(ContactResponse.failure(errors));
    }

    private String resolveClientIp(HttpServletRequest request) {
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
