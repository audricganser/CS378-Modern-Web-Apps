import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OpenStackMeetingsParamImpl implements OpenStackService {

	
	public String getMeetings(String project, String year) {
		int meetings = 0;		
		project = project.toLowerCase();
		
		//check if project and year exist
		if(project == "" && year == "") {
			return "Required parameter < project > < year > missing";
		}
		
		if(year == "") {
			return "Required parameter < year > missing";
		}
		
		try {
			Jsoup.connect("http://eavesdrop.openstack.org/meetings/"+ project).get();
		} catch (Exception e){
			return("Project with < "+project+" > not found");
		}
		
		try {
			Jsoup.connect("http://eavesdrop.openstack.org/meetings/"+ project +"/" + year + "/").get();
		} catch (Exception e){
			return("Project with < "+year+" > not found");
		}

		try {
			Document Doc = Jsoup.connect("http://eavesdrop.openstack.org/meetings/"+ project + "/" + year).get();
			Elements atags = Doc.select("td");
			if(Doc != null) {
				ListIterator<Element> iter = atags.listIterator();
				while(iter.hasNext()) {
					Element e = (Element) iter.next();
					String s = e.text();
					if(s != null && s.contains(project)) {
						meetings++;
					}
				}
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return "Number of meeting files: " + meetings;
	}
}