package assign.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
	
	ProjectService projectService;
	String password;
	String username;
	String dburl;	
	
	public ProjectResource(@Context ServletContext servletContext) {
		dburl = servletContext.getInitParameter("DBURL");
		username = servletContext.getInitParameter("DBUSERNAME");
		password = servletContext.getInitParameter("DBPASSWORD");
		this.projectService = new ProjectServiceImpl(dburl, username, password);		
	}
	
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
	public Response getProject(@PathParam("project_id") Integer project_id ) throws Exception {
		//check if project id is a correct number or not null
		//if it is then send a bad response
		if (project_id == null || project_id < 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		else {
			try {
				final Project proj = projectService.getProject(project_id);  
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
	@Path("/")
	@Consumes("application/xml")
	public Response createProject(Project p) throws Exception {
		//check if project exist and if it is was actually passed in
		if (p == null || p.getName().length() <= 0 || p.getDescription().length() <= 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			Project proj = projectService.addProject(p);
			if (proj != null) {
				return Response.created(URI.create("/projects/" + proj.getProjectId() )).build();
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	
	@PUT //update an existing project
	@Path("/{project_id}")
	@Consumes("application/xml")
	public Response updateProject(@PathParam("project_id") final Integer project_id, Project update ) throws Exception {
		if ((project_id == null || project_id < 0) && update == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		else {
			try {
				int counter = 0;
				Project proj = projectService.getProject(project_id);
				if (proj == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);
				
				if (update.getName() != null && update.getName().length() > 0 ) {
					proj.setName(update.getName());
					counter++;
				}
				if (update.getDescription() != null && update.getDescription().length() > 0) {
					proj.setDescription(update.getDescription());
					counter++;
				}
				if (counter == 2 && projectService.updateProject(proj)) {
					return Response.status(Response.Status.OK).build();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}		
	
	//delete a project from the table
	@DELETE
	@Path("/{project_id}")
	@Produces("application/xml")
	public Response deleteProject(@PathParam("project_id") final Integer project_id) throws Exception {
		if (project_id == null || project_id < 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		else {
			try {
				if (projectService.deleteProject(project_id)) {
					return Response.status(Response.Status.OK).build();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}	
	
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