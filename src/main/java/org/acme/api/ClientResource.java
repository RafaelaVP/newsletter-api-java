package org.acme.api;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Client;
import org.acme.repository.ClientRepository;

import java.util.List;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {

    @Inject
    ClientRepository clientRepository;

    @POST
    @Transactional
    public Response create(Client client) {
        clientRepository.persist(client);
        return Response.status(Response.Status.CREATED).entity(client).build();
    }

    @GET
    public List<Client> listAll() {
        return clientRepository.listAll();
    }
}
