package org.acme.scheduler;

import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.repository.ClientRepository;
import org.acme.service.EmailService;

public class DailyEmailScheduler {

    @Inject
    ClientRepository clientRepository;

    @Inject
    EmailService emailService;

    @Scheduled(cron = "0 0 */8 * * ?")
    @Transactional
    public void sendEmails() {
        emailService.sendDailyEmails(clientRepository.listAll());
    }
}
