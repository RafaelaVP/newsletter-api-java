package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.Mail;
import org.acme.domain.Client;
import org.acme.domain.News;
import org.acme.repository.NewsRepository;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EmailService {

    private static final Logger LOG = Logger.getLogger(EmailService.class);

    @Inject
    NewsRepository newsRepository;

    @Inject
    Mailer mailer;

    private final Handlebars handlebars = new Handlebars();

    public void sendDailyEmails(List<Client> clients) {
        List<News> unprocessedNews = newsRepository.findUnprocessed();

        for (Client client : clients) {
            String birthdayMessage = getBirthdayMessage(client);
            String newsList = generateNewsList(unprocessedNews);

            LOG.info("Sending email to: " + client.getEmail());
            LOG.info("Client Name: " + client.getName());
            LOG.info("Birthday Message: " + birthdayMessage);
            LOG.info("News List: " + newsList);

            try {
                Path templatePath = Path.of("src/main/resources/templates/email/daily-news-template.html");
                String templateContent = Files.readString(templatePath);

                Template template = handlebars.compileInline(templateContent);

                String htmlContent = template.apply(Map.of(
                        "clientName", client.getName(),
                        "birthdayMessage", birthdayMessage,
                        "newsList", new Handlebars.SafeString(newsList) 
                ));

                mailer.send(Mail.withHtml(
                        client.getEmail(),
                        "Notícias do dia!",
                        htmlContent
                ));

            } catch (IOException e) {
                LOG.error("Erro ao carregar o template ou enviar o e-mail: ", e);
            }
        }

        unprocessedNews.forEach(news -> news.setProcessed(true));
        newsRepository.persist(unprocessedNews);
    }

    public String getBirthdayMessage(Client client) {
        if (client.getBirthDate() != null) {
            LocalDate today = LocalDate.now();
            if (client.getBirthDate().getMonth() == today.getMonth() && client.getBirthDate().getDayOfMonth() == today.getDayOfMonth()) {
                return "Feliz aniversário!";
            }
        }
        return ""; 
    }

    public String generateNewsList(List<News> newsList) {
        StringBuilder htmlBuilder = new StringBuilder();
        for (News news : newsList) {
            htmlBuilder.append("<div>").append(news.getTitle()).append("</div>");
            if (news.getLink() != null) {
                htmlBuilder.append("<a href=\"").append(news.getLink().toString()).append("\">").append(news.getLink().toString()).append("</a>");
            } else {
                htmlBuilder.append("<p>link inválido</p>");
            }
        }
        return htmlBuilder.toString();
    }
    
}
