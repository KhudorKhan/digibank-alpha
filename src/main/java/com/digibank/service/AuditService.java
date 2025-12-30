package com.digibank.service;

import com.digibank.model.AuditLog;
import com.digibank.model.Transaction;
import com.digibank.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);
    @Autowired
    private AuditLogRepository auditLogRepository;

    public void logPayment(Long userId, String paymentType, java.math.BigDecimal amount, Transaction.ServiceType serviceType) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setEventType(AuditLog.AuditEventType.PAYMENT);
        log.setAction("PAYMENT_PROCESSED");
        log.setDetails(String.format("Payment: %s, Amount: %s, Service: %s", paymentType, amount, serviceType));
        auditLogRepository.save(log);
    }

    public void logSecurityEvent(Long userId, String details) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setEventType(AuditLog.AuditEventType.SECURITY_ALERT);
        log.setAction("SECURITY_ALERT");
        log.setDetails(details);
        auditLogRepository.save(log);
    }

    public void logAuthentication(Long userId, String action, String ipAddress) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setEventType(AuditLog.AuditEventType.AUTHENTICATION);
        log.setAction(action);
        log.setIpAddress(ipAddress);
        auditLogRepository.save(log);
    }
}


