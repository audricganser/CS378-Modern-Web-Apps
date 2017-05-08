package assign.resources;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.xml.sax.InputSource;

public class TestUTCoursesResource {
	
	@Test //1
	public void testProjectsResource() throws Exception
		{
		String expectedProjects[] =  {
				"3rd_party_ci/", 
				"17_12_2015/", 
				"2015_09_17/", 
				"2015_10_15/",
				"2015_10_29/", 
				"____freezer_meeting_23_07_2015____/",
				"___endmeeting/", 
				"__ironic_neutron/",
			    "__networking_l2gw/", 
				"__poppy/"
				};
		Client client = ClientBuilder.newClient();
		Response response = client.target("http://localhost:8080/assignment3/myeavesdrop/projects/").request(MediaType.APPLICATION_XML).get();	
		String retXML = response.readEntity(String.class);
	
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource src = new InputSource();
		src.setCharacterStream(new StringReader(retXML));
		org.w3c.dom.Document doc = builder.parse(src);
	
		for(int i = 0; i < expectedProjects.length; i++) {
			assertEquals(expectedProjects[i], doc.getElementsByTagName("project").item(i).getTextContent());
		}
	}
	
	@Test //2
	public void testExistingProj() throws Exception
	{	
		String expectedYears[] =  {"2013/", "2014/", "2015/", "2016/"};
		Client client = ClientBuilder.newClient();
		Response response = client.target("http://localhost:8080/assignment3/myeavesdrop/projects/solum_team_meeting/meetings").request(MediaType.APPLICATION_XML).get();	
		String retXML = response.readEntity(String.class);
	
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource src = new InputSource();
		src.setCharacterStream(new StringReader(retXML));
		org.w3c.dom.Document doc = builder.parse(src);
	
		for(int i = 0; i < expectedYears.length; i++) {
			assertEquals(expectedYears[i], doc.getElementsByTagName("year").item(i).getTextContent());
		}
	}
	
	@Test //3
	public void testCorrectYear() throws Exception
	{			
		Client client = ClientBuilder.newClient();
		Response response = client.target("http://localhost:8080/assignment3/myeavesdrop/projects/3rd_party_ci/meetings").request(MediaType.APPLICATION_XML).get();
		String retXML = response.readEntity(String.class);
	
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource src = new InputSource();
		src.setCharacterStream(new StringReader(retXML));
		org.w3c.dom.Document doc = builder.parse(src);
		assertEquals("2014/", doc.getElementsByTagName("year").item(0).getTextContent());
	}
	
	@Test //4
	public void testWrongProj() throws Exception
	{
		Client client = ClientBuilder.newClient();
		Response response = client.target("http://localhost:8080/assignment3/myeavesdrop/projects/non-existent-project/meetings").request(MediaType.APPLICATION_XML).get();
		String retXML = response.readEntity(String.class);
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource src = new InputSource();
		src.setCharacterStream(new StringReader(retXML));
		org.w3c.dom.Document doc = builder.parse(src);
		assertEquals("Project non-existent-project does not exist", doc.getElementsByTagName("error").item(0).getTextContent());
		
		
	}
		
}
