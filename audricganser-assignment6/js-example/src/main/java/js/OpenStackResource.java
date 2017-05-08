package js;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


@Path("/getMeetings")
public class OpenStackResource {
	
	public OpenStackResource() {
		
	}
	
	@GET
	@Path("/solum")
	@Produces("text/html")
	public String meetings() {
		String info = "";
		String prevMeeting = "";
		int counter = 0;
		try {
			Document projDoc = Jsoup.connect("http://eavesdrop.openstack.org/meetings/solum_team_meeting").get();
			Elements atags = projDoc.select("a");
			if(projDoc != null) {
				ListIterator<Element> iter = atags.listIterator();
				while(iter.hasNext()) {
					Element e = (Element) iter.next();
					String years = e.html();
					if(!years.equals("Name") && !years.equals("Last modified") && !years.equals("Size") && !years.equals("Description") &&  !years.equals("Last modified") && !years.equals("Parent Directory")) {
						info += years.substring(0, 4) + " ";
						
						try {
							Document doc = Jsoup.connect("http://eavesdrop.openstack.org/meetings/solum_team_meeting/" + years).get();
							Elements a = doc.select("a");
							if(doc != null) {
								ListIterator<Element> iter2 = a.listIterator();
								while(iter2.hasNext()) {
									Element x = (Element) iter2.next();
									String meetings = x.html();
									if(!meetings.equals("Name") && !meetings.equals("Last modified") && !meetings.equals("Size") && !meetings.equals("Description") &&  !meetings.equals("Last modified") && !meetings.equals("Parent Directory")) {
										if(meetings.contains("log.html")) {
											String currentMeeting = meetings.substring(19, 29);
											if(!prevMeeting.equals(currentMeeting)) {
												prevMeeting = currentMeeting;
												counter++;
											}
										}
									}
								}
								info += counter+ " ";
								counter = 0;
							}
						}
						catch (Exception exp) {
							exp.printStackTrace();
							return null;
					}
				}
			}
		}
		} catch (Exception exp) {
			exp.printStackTrace();
			return null;
		}
		//return years; 
		 return info;
	}
}
