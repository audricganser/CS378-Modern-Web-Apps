package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "meetings")
@XmlAccessorType(XmlAccessType.FIELD)
public class Meetings {
	    
    private List<String> year = null;
    
    public List<String> getMeetings() {
    	return year;
    }
    
    public void setMeetings(List<String> year) {
    	this.year = year;
    }
}
