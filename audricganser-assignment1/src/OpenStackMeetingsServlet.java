import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@WebServlet("/openstackmeetings")
public class OpenStackMeetingsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Boolean sessionStart = false;
	ArrayList<String> visitedUrls = new ArrayList<String>();
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
                    throws ServletException, IOException
    {
		PrintWriter w = response.getWriter();
		
		String project = request.getParameter("project");
		String year = request.getParameter("year");
		String session = request.getParameter("session");
		
		Boolean sessionProj = false;
		Boolean sessionYear = false;
		
		//project = project.toLowerCase();
		
		//check if site is barely being visited
		//if it is then print out empty "visited URLs" and "URL Data"
		if(session == null && project == null && year == null) {
			w.println("Visited URLs\n");
			w.println("\nURL Data\n");
			return;
		}
		
		if(session != null) {
			//check if user types in the correct parameter for started
			if(!session.equalsIgnoreCase("start") && !session.equalsIgnoreCase("end")) {
				w.println("you must enter \"start\" or \"end\"");
			}
			//print out visited URLs list and add session=start to list
			if(session.equalsIgnoreCase("start")) {
				sessionStart = true;
				visitedUrls.clear();
				w.println("Visited URLs\n");
				for (int x = 0; x < visitedUrls.size(); x++) {
					w.println(visitedUrls.get(x));
				}
				w.println("\nURL Data\n");
				visitedUrls.add(request.getRequestURL().toString() + "?" + request.getQueryString());
				return;
			}
			//print out visited URLs list then delete the list
			if(sessionStart) {
				if(session.equalsIgnoreCase("end") && sessionStart) {
					sessionStart = false;
					w.println("Visited URLs\n");
					for (int x = 0; x < visitedUrls.size(); x++ ) {
						w.println(visitedUrls.get(x));
					}
					w.println("\nURL Data\n");
					visitedUrls.clear();
					return;
				}
			}
			else {
				if(session.equalsIgnoreCase("end")) {
					sessionStart = false;
					w.println("Visited URLs\n");
					
					w.println("\nURL Data\n");
					visitedUrls.clear();
					return;
				}
			}
		}
		
		//check if year is passed in only or project passed in only
		if(year!=null && project == null) {
			w.print("Required parameter <project> missing");
			return;
		}
		else if (year==null && project != null) {
			w.print("Required parameter <year> missing");
			return;
		}
		
		//loop through Array list and print
		if(sessionStart) {
			w.println("Visited URLs\n");
			for (int x = 0; x < visitedUrls.size(); x++ ) {
				w.println(visitedUrls.get(x));
			}
			w.println("\nURL Data\n");
		}
		else {
			w.println("Visited URLs\n");
			w.println("\nURL Data\n");
			visitedUrls.clear();
		}
		
		
		//error check if project exists
		if(project != null) {
			try {
				Jsoup.connect("http://eavesdrop.openstack.org/meetings/"+ project.toLowerCase()).get();
			}
			catch (Exception e){
				w.print("Project with <"+project.toLowerCase()+"> not found");
				return;
			}
			sessionProj = true;
		}
		
		//error check if year exists
		if(year != null) {
			try {
				Jsoup.connect("http://eavesdrop.openstack.org/meetings/"+ project.toLowerCase() +"/" + year + "/").get();
			}
			catch (Exception e){
				w.print("Invalid year <"+year+"> for project <"+project.toLowerCase()+">.");
				return;
			}
			sessionYear = true;
		}
		
		//print the meeting data onto "URL Data" and add visited URL to list
		if(sessionProj && sessionYear) {
			try {
				Document projDoc = Jsoup.connect("http://eavesdrop.openstack.org/meetings/"+ project.toLowerCase() + "/" + year).get();
				visitedUrls.add(request.getRequestURL().toString() + "?" + request.getQueryString());
				Elements atags = projDoc.select("td");
				if(projDoc != null) {
					ListIterator<Element> iter = atags.listIterator();
					while(iter.hasNext()) {
						Element e = (Element) iter.next();
						String s = e.text();
						if(s != null && s.contains(project.toLowerCase())) {
							w.println(s +" "+e.nextElementSibling().text());
						}
					}
				}
				
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
    }
}