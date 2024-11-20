package org.acme.api;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.News;
import org.acme.repository.NewsRepository;

import java.util.List;

@Path("/noticias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NewsResource {

    @Inject
    NewsRepository newsRepository;

    @POST
    @Transactional
    public Response create(News news) {
        newsRepository.persist(news);
        return Response.status(Response.Status.CREATED).entity(news).build();
    }

    @GET
    public List<News> listUnprocessed() {
        return newsRepository.findUnprocessed();
    }
}
