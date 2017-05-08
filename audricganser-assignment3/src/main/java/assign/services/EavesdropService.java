package assign.services;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import assign.domain.Meetings;
import assign.domain.Projects;

public class EavesdropService {

	public Projects getAllProjects() throws Exception {
		final Projects projects = new Projects();
		projects.setProjects(new ArrayList<String>());
		
		try {
			Document projDoc = Jsoup.connect("http://eavesdrop.openstack.org/meetings").get();
			Elements atags = projDoc.select("a");
			if(projDoc != null) {
				ListIterator<Element> iter = atags.listIterator();
				while(iter.hasNext()) {
					Element e = (Element) iter.next();
					String s = e.html();
					if(!s.equals("Name") && !s.equals("Last modified") && !s.equals("Size") && !s.equals("Description") &&  !s.equals("Last modified") && !s.equals("Parent Directory")) {
						projects.getProjects().add(s);
					}
				}
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return projects; 
	}	
	
	public Meetings getMeetings(String project) throws IOException {
		final Meetings meetings = new Meetings();
		meetings.setMeetings(new ArrayList<String>());
		
		try {
			Document projDoc = Jsoup.connect("http://eavesdrop.openstack.org/meetings/" + project).get();
			Elements atags = projDoc.select("a");
			if(projDoc != null) {
				ListIterator<Element> iter = atags.listIterator();
				while(iter.hasNext()) {
					Element e = (Element) iter.next();
					String s = e.html();
					if(!s.equals("Name") && !s.equals("Last modified") && !s.equals("Size") && !s.equals("Description") &&  !s.equals("Last modified") && !s.equals("Parent Directory")) {
						meetings.getMeetings().add(s);
					}
				}
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
			return null;
		}
		return meetings; 
	}
}
