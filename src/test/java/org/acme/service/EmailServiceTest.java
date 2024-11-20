package org.acme.service;

import org.acme.domain.Client;
import org.acme.domain.News;
import org.acme.repository.NewsRepository;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {

    @Mock
    NewsRepository newsRepository; 
    
    @Mock
    Mailer mailer; 
    
    @InjectMocks
    EmailService emailService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testGetBirthdayMessage_BirthdayToday() {

        Client client = new Client();
        client.setName("Uzumaki Naruto");
        client.setBirthDate(LocalDate.now());  

        String message = emailService.getBirthdayMessage(client);

        assertEquals("Feliz aniversário!", message);
    }

    @Test
    void testGetBirthdayMessage_NoBirthday() {

        Client client = new Client();
        client.setName("Uzumaki Naruto");
        client.setBirthDate(LocalDate.of(1990, 1, 1)); 

        String message = emailService.getBirthdayMessage(client);

        assertEquals("", message); 
    }

    @Test
    void testGenerateNewsList() {
        // Arrange
        News news1 = new News();
        news1.setTitle("News 1");
        news1.setLink("http://example.com/news1");

        News news2 = new News();
        news2.setTitle("News 2");
        news2.setLink(null); 
        List<News> newsList = List.of(news1, news2);

        // Act
        String newsHtml = emailService.generateNewsList(newsList);

        // Debugging output
        System.out.println("Generated HTML: " + newsHtml);  

        // Assert
        assertTrue(newsHtml.contains("News 1"));
        assertTrue(newsHtml.contains("http://example.com/news1"));
        assertTrue(newsHtml.contains("News 2"));
        assertTrue(newsHtml.contains("link inválido")); 
    }

    @Test
    void testSendDailyEmails() {

        Client client = new Client();
        client.setName("Uzumaki Naruto");
        client.setEmail("naruto@example.com");

        List<Client> clients = List.of(client);

        News news = new News();
        news.setTitle("Breaking News");
        news.setLink("http://example.com"); 
        List<News> newsList = List.of(news);

        when(newsRepository.findUnprocessed()).thenReturn(newsList);

        
        emailService.sendDailyEmails(clients);

        verify(mailer, times(1)).send(any(Mail.class)); 
        verify(newsRepository, times(1)).persist(newsList); 
    }
}
