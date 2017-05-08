package assign.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import assign.domain.Errors;
import assign.domain.Meetings;
import assign.domain.Projects;
import assign.services.EavesdropService;

@Path("/projects") //changed
public class UTCoursesResource {
	
	EavesdropService eavesdropService;
	
	public UTCoursesResource() {
		this.eavesdropService = new EavesdropService();
	}
	
	@GET
	@Path("/")
	@Produces("application/xml")
	public StreamingOutput getAllProjects() throws Exception {	
		final Projects projects = this.eavesdropService.getAllProjects();
	    return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	            outputCourses(outputStream, projects);
	         }
	      };	    
	}	
	
	@GET
	@Path("/{project}/meetings")
	@Produces("application/xml")
	public StreamingOutput getMeetings(@PathParam("project") final String project) throws Exception {	
		final Meetings meetings = this.eavesdropService.getMeetings(project);
	    return new StreamingOutput() {
	         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
	        	if(meetings == null) {
	        		final Errors error = new Errors();
	        		error.setErrors("Project " + project + " does not exist");
	        		outputCourses(outputStream, error);
	        	}
	        	else
	        		outputCourses(outputStream, meetings);
	         }
	      };	    
	}	
	
	protected void outputCourses(OutputStream os, Projects projects) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Projects.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(projects, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	
	protected void outputCourses(OutputStream os, Meetings meetings) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Meetings.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(meetings, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	
	protected void outputCourses(OutputStream os, Errors error) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Errors.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(error, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
}