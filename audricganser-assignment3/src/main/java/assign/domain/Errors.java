package assign.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "ouput")
@XmlAccessorType(XmlAccessType.FIELD)
public class Errors {
	    
    private String error = null;
    
    public String getMeetings() {
    	return error;
    }
    
    public void setErrors(String error) {
    	this.error = error;
    }
}