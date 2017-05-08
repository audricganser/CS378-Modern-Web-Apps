package assign.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import assign.domain.*;
import assign.services.*;

@Path("/projects")
public class ProjectResource {
	
	private DBLoader dbloader; 
	String password;
	String username;
	String dburl;	
	
	public ProjectResource(@Context ServletContext servletContext) {
		this.dbloader = new DBLoader();
		//this.projectService = new ProjectServiceImpl(dburl, username, password);		
	}
	
	//test
	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
		System.out.println("Inside helloworld");
		System.out.println("DB creds are:");
		System.out.println("DBURL:" + dburl);
		System.out.println("DBUsername:" + username);
		System.out.println("DBPassword:" + password);		
		return "Hello world " + dburl + " " + username + " " + password;		
	}

	@GET
	@Path("/{project_id}")
	@Produces("application/xml")
	public Response getProject(@PathParam("project_id") Long project_id ) throws Exception {
		if (project_id == null || project_id < 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		else {
			try {
				final Project proj = dbloader.getProject(project_id);  
				if (proj != null) {
					StreamingOutput stream = new StreamingOutput() {
				         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
				        	 outputWriter(outputStream, proj);
				         }
				      };
				      
				      return Response.status(Response.Status.OK).entity(stream).build();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}		
	
	
	@POST //create a new project
	@Path("")
	@Consumes("application/xml")
	public Response createProject(Project p) throws Exception {
		if (p.getDescription().trim().length() <= 0 || p.getName().trim().length() <= 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			Project proj = dbloader.addProject(p);
			if (proj != null) {
				return Response.created(URI.create("/projects/" + proj.getProjectId() )).build();
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	
	@POST //create a new meeting
	@Path("/{project_id}/meetings")
	@Consumes("application/xml")
	public Response createMeeting(Meeting m, @PathParam("project_id") final Long project_id) throws Exception {
		if (m.getYear().trim().length() <= 0 || m.getName().trim().length() <= 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			Project project = dbloader.getProject(project_id);
			
			if (project == null) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			
			Meeting meeting = dbloader.addMeeting(m, project);
			
			if (meeting != null) {
				return Response.status(201).build();
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	@DELETE
	@Path("/{project_id}")
	@Produces("application/xml")
	public Response deleteProject(@PathParam("project_id") final Long project_id) throws Exception {
		if (project_id == null || project_id < 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		try {
			if (dbloader.deleteProject(project_id)) {
				return Response.status(Response.Status.OK).build();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	
	@PUT
	@Path("/{project_id}/meetings/{meeting_id}")
	@Consumes("application/xml")
	public Response updateMeeting(@PathParam("meeting_id") final Long meeting_id, Meeting update, @PathParam("project_id") final Long project_id) throws Exception {
		
			try {
				Project project = dbloader.getProject(project_id);
				if (project == null) {
					return Response.status(Response.Status.NOT_FOUND).build();
				}
				
				if (update.getName() == null || update.getName().trim().length() <= 0 ) {
					return Response.status(Response.Status.BAD_REQUEST).build();
				}
				if (update.getYear().trim().length() <= 0) {
					return Response.status(Response.Status.BAD_REQUEST).build();
				}
				if (dbloader.updateMeeting(update, meeting_id)) {
					return Response.status(Response.Status.OK).build();
				}
				else {
					return Response.status(Response.Status.NOT_FOUND).build();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	
//	@PUT //update an existing project
//	@Path("/{project_id}")
//	@Consumes("application/xml")
//	public Response updateProject(@PathParam("project_id") final Integer project_id, Project update ) throws Exception {
//		if ((project_id == null || project_id < 0) && update == null) {
//			return Response.status(Response.Status.BAD_REQUEST).build();
//		}
//		else {
//			try {
//				Project proj = projectService.getProject(project_id);
//				if (proj == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);
//				
//				if (update.getName() != null && update.getName().length() > 0 ) {
//					proj.setName(update.getName());
//				}
//				if (update.getDescription() != null && update.getDescription().length() > 0) {
//					proj.setDescription(update.getDescription());
//				}
//				if (projectService.updateProject(proj)) {
//					return Response.status(Response.Status.OK).build();
//				}
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return Response.status(Response.Status.BAD_REQUEST).build();
//	}		
	
//	//delete a project from the table
//	@DELETE
//	@Path("/{project_id}")
//	@Produces("application/xml")
//	public Response deleteProject(@PathParam("project_id") final Integer project_id) throws Exception {
//		if (project_id == null || project_id < 0) {
//			return Response.status(Response.Status.NOT_FOUND).build();
//		}
//		else {
//			try {
//				if (projectService.deleteProject(project_id)) {
//					return Response.status(Response.Status.OK).build();
//				}
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return Response.status(Response.Status.NOT_FOUND).build();
//	}	
	
	protected void outputWriter(OutputStream os, Project project) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(project, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	
}