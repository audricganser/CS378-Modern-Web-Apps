package assign.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Entity
@Table( name = "meeting" )
@XmlRootElement(name = "meeting")
@XmlAccessorType(XmlAccessType.FIELD)
public class Meeting {
	@XmlAttribute
	private Long id;
    private String name;
    private String year;
    @XmlTransient
    private Project project;
    
    public Meeting() {
		
	}
	
	public Meeting(String name, String year) {
		this.name = name;
		this.year = year;
	}
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getMeetingId() {
		return id;
    }

    public void setMeetingId(Long meetingId) {
		this.id = meetingId;
    }
	
	@Column(name="meetingName")
	public String getName() {
		return name;
	}
	
	public void setName(String meetingName) {
		this.name = meetingName;
	}

	@Column(name = "year")
    public String getYear() {
		return year;
    }

    public void setYear(String year) {
		this.year = year;
    }
    
    @ManyToOne
    @JoinColumn(name="project_id")
    public Project getProject() {
    	return this.project;
    }
    
    public void setProject(Project c) {
    	this.project = c;
    }
}