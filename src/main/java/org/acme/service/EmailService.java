package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.Mail;
import org.acme.domain.Client;
import org.acme.domain.News;
import org.acme.repository.NewsRepository;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class EmailService {

    @Inject
    NewsRepository newsRepository;

    @Inject
    Mailer mailer;

    public void sendDailyEmails(List<Client> clients) {
        List<News> unprocessedNews = newsRepository.findUnprocessed();

        for (Client client : clients) {
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Bom dia ").append(client.getName()).append("!\n");

            if (client.getBirthDate() != null && client.getBirthDate().equals(LocalDate.now())) {
                emailContent.append("Feliz aniversário!\n");
            }

            emailContent.append("Notícias do dia:\n");
            for (News news : unprocessedNews) {
                emailContent.append("- ").append(news.getTitle());
                if (news.getLink() != null) {
                    emailContent.append(" (").append(news.getLink()).append(")");
                }
                emailContent.append("\n");
            }

            mailer.send(Mail.withText(
                client.getEmail(),
                "Notícias do dia!",
                emailContent.toString()
            ));
        }

        unprocessedNews.forEach(news -> news.setProcessed(true));
        newsRepository.persist(unprocessedNews);
    }
}
