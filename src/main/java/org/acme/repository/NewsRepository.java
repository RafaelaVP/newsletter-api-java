package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.News;

import java.util.List;

@ApplicationScoped
public class NewsRepository implements PanacheRepository<News> {

    public List<News> findUnprocessed() {
        return list("processed", false);
    }
}
