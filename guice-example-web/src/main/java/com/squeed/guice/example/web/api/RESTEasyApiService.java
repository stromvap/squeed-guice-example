package com.squeed.guice.example.web.api;

import com.google.inject.Inject;
import com.squeed.guice.example.business.BusinessLogic;
import com.squeed.guice.example.business.Transaction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Arrays;

@Path("api")
public class RESTEasyApiService {

    @Inject
    private BusinessLogic businessLogic;

    @Inject
    private Transaction transaction;

    @GET
    @Produces("application/json")
    public Response get() {
        return Response.status(200).entity(Arrays.asList(businessLogic.getBusiness(), transaction.getTransaction())).build();
    }
}
