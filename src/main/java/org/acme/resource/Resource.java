package org.acme.resource;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.SimProjection;
import org.acme.enitty.SimCard;
import org.acme.repository.SimCardRepository;
import org.eclipse.microprofile.openapi.annotations.links.Link;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;
import java.util.Optional;

@Path("/")
public class Resource {
    @Inject
    SimCardRepository simCardRepository;

//    @POST
//    @Transactional
//    @Path("save_simcard")
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response saveSimCard() {
//        String[] provider = {"Jio", "Airtel", "VI", "Aircel", "BSNL"};
//
//        for (int i = 0; i < 20; i++) {
//            SimCard simCard = new SimCard();
//            simCard.setNumber(8876223210L+i);
//            simCard.setProvider(provider[(int)i%provider.length]);
//            simCard.setActive(i/3L == 0);
//            simCardRepository.persist(simCard);
//            if(simCardRepository.isPersistent(simCard)){
//                System.out.println(simCard+" saved successfully");
//            }
//            else {
//                System.out.println(simCard + " not saved. Please check");
//            }
//        }
//        return Response.ok(new String("Sim card saved successfully")).build();
//    }
//
//    @GET
//    @Path("test_methods")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response testMethods() {
//        List<SimCard> simCards = simCardRepository.listAll();
//        return Response.ok(simCards).build();
//    }

    @POST
    @Path("/persist_SimCard")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveSimCard(@RequestBody SimCard simCard){
        simCardRepository.persist(simCard);

        if(simCardRepository.isPersistent(simCard)) {
            return Response.ok(new String("Sim card saved successfully")).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/listAll_SimCard")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllSimCard(){
        List<SimCard> simCards = simCardRepository.listAll();
        return Response.ok(simCards).build();
    }

    @GET
    @Path("/findById_SimCard/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByIdSimCard(@PathParam("id") long simCardId){
        SimCard simCard = simCardRepository.findById(simCardId);
        return Response.ok(simCard).build();
    }

    @GET
    @Path("/count_SimCard")
    @Produces(MediaType.TEXT_PLAIN)
    public Response countSimCard(){
        Long count = simCardRepository.count();
        return Response.ok(count).build();
    }

    @GET
    @Path("/provider_list_SimCard/{provider}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response providerListSimCard(@PathParam("provider") String provider){
        List<SimCard> simCardList = simCardRepository.list("provider",provider);
        return Response.ok(simCardList).build();
    }

    @GET
    @Path("/active_list_SimCard")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activeListSimCard(){
        List<SimCard> activeSimCardList = simCardRepository.list("isActive",true);
        return Response.ok(activeSimCardList).build();
    }

    @GET
    @Path("/findByIdOptional_SimCard/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByIdOptionalSimCard(@PathParam("id") long id){
        Optional<SimCard> optionalSimCard = simCardRepository.findByIdOptional(id);
        if(optionalSimCard.isPresent()){
            SimCard simCard = optionalSimCard.get();
            return Response.ok(simCard).build();
        }
        else {
            return Response.noContent().build();
        }
    }
    @GET
    @Path("/conditional_count_SimCard/{provider}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response conditionalCountSimCard(@PathParam("provider")String simProvider){
        Long count = simCardRepository.count("provider",simProvider);
        return Response.ok(count).build();
    }

    @DELETE
    @Path("/conditional_delete_SimCard/{provider}")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response conditionalDeleteSimCard(@PathParam("provider")String simProvider){
        System.out.println(simProvider);
        Long rowsAffected = simCardRepository.delete("provider",simProvider);
        return Response.ok(rowsAffected).build();
    }
    @DELETE
    @Path("/conditionalDeleteById_SimCard/{id}")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response conditionalDeleteByIdSimCard(@PathParam("id")Long id){
       System.out.println(id);
        //update SimCard set provider = @PathParam("provider") where id = @PathParam("id")
        boolean isDeleted = simCardRepository.deleteById(id);
        if(isDeleted)
            return Response.ok("Sim card deleted successfully").build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/update/{id}/{provider}")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response conditionalUpdateByIdSimCard(@PathParam("id")Long id,@PathParam("provider") String provider){
        //update SimCard set provider = @PathParam("provider") where id = @PathParam("id")
        int rowsAffected = simCardRepository.update("provider=?1 where id=?2",provider,id);
        if(rowsAffected == 1)
             return Response.ok("Sim card updated successfully").build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/sortBy")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sortBySimCard(){
        //select * from simcard where isActive=false order by provider desc;
        List<SimCard> simCardList = simCardRepository.list("isActive", Sort.descending("provider"),false);
        return Response.ok(simCardList).build();
    }

    @GET
    @Path("/getSimCard/{pageNo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response paginationSimCard(@PathParam("pageNo")int pgNo){
        PanacheQuery<SimCard>  allSimCardPanacheQuery=simCardRepository.findAll();
        //select * from SimCard offset 0 limit 5;
        //allSimCardPanacheQuery.page(Page.ofSize(5));

        //List<SimCard> simCardList= allSimCardPanacheQuery.list();
        //List<SimCard> simCardList= allSimCardPanacheQuery.nextPage().list();

        //List<SimCard> simCardList = allSimCardPanacheQuery.page(Page.of(pgNo,5)).list();

        //long numberOfPages = allSimCardPanacheQuery.pageCount();
        //System.out.println(numberOfPages);

        List<SimProjection> simProjectionList = allSimCardPanacheQuery.page(Page.of(pgNo, 5)).project(SimProjection.class).list();

        return Response.ok(simProjectionList).build();
    }
}
