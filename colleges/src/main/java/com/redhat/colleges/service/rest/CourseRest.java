package com.redhat.colleges.service.rest;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.redhat.colleges.entity.Course;

@Path("/course")
public class CourseRest {
	
	@PersistenceContext(unitName="college")
    private transient EntityManager em;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String createCourse(
    		@FormParam("claimant") String claimant,
    		@FormParam("objective") String objective)  {
		final Course entity = new Course();
		entity.setClaimant(claimant);
		entity.setRegisteredOn(new Date());
		entity.setObjective(objective);
		try {
			em.persist(entity);
		}
		catch (PersistenceException e) {
			throw new WebApplicationException(buildResponse(Status.NOT_ACCEPTABLE, "Failed to create the claim - " + e.getMessage()));
		} finally {
			em.flush();
		}

        return entity.getId().toString();
    }
    
	@GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
	public Course getClaim(
			@PathParam("id") long id)
	{
		try
		{
			final Course result = em.find(Course.class, id);
			// we do this to initialize the Items collection, but this looks ugly.
			result.getStudents().size();
			return result;
		} catch (NoResultException e)
		{
			throw new WebApplicationException(buildResponse(Status.NOT_FOUND, "No matching claim for " + id));
		}
	}
	
	@DELETE
    @Path("{id}")
    public String deleteClaim(
    		@PathParam("id") long id)  {
    	try {
    		final Course entity = em.find(Course.class, id);
	    	if (entity.getStudents().size() > 0)
	    		throw new WebApplicationException(buildResponse(Status.NOT_ACCEPTABLE, "Claim not EMPTY, delete associated items first"));
	    	else
	    		em.remove(entity);
    	}
    	catch (EntityNotFoundException nfe) {
			throw new WebApplicationException(buildResponse(Status.NOT_FOUND, "No matching claim for " + id));
    	}
    	catch (PersistenceException pe) {
			throw new WebApplicationException(buildResponse(Status.BAD_REQUEST, "Failed to delete claim " + id + " - " + pe.getMessage()));
    	}
    	catch (Exception e) {
			throw new WebApplicationException(buildResponse(Status.EXPECTATION_FAILED, "Failed to delete claim " + id + " - " + e.getMessage()));
		} finally {
			em.flush();
		}

        return "Claim deleted: " + id;
	}
	
	/***********************
	 *
	 * UTILITY METHODS (hint: can be ignored)
	 *
	 **********************/

   /**
    * Private helper to create a response with a message.
    *
    * @param s status to be returned
    * @param msg accompanying message
    * @return the corresponding Response
    */
   private Response buildResponse(Status s, String msg) {
   	return Response.status(s).entity(msg).build();
   }

}
