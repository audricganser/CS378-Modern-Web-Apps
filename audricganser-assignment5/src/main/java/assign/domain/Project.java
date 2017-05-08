package assign.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAccessType;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;



@Entity
@Table(name = "project")
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {
	@XmlTransient
	private long projectId;
	private String name;
	String description;
	@XmlElementWrapper(name = "meetings")
	private Set<Meeting> meeting;

	public Project() {
		
	}
	
	public Project(String projectName, String description) {
		this.name = projectName;
		this.description = description;
	}
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@XmlAttribute(name = "id")
	public long getProjectId() {
		return projectId;
	}
	
	@SuppressWarnings("unused")
	private void setProjectId(long projectId) {
		this.projectId = projectId;
		
	}
	
	@Column(name="projectName")
	public String getName() {
		return name;
	}
	
	public void setName(String projectName) {
		this.name = projectName;
	}
	
	@Column(name ="description")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@OneToMany(mappedBy="project")
	@Cascade({CascadeType.DELETE})
	public Set<Meeting> getMeetings() {
		return this.meeting;
	}
	
	public void setMeetings(Set<Meeting> meetings) {
		this.meeting = meetings;
	}
	
}

//@XmlRootElement(name = "project")
//@XmlAccessorType(XmlAccessType.FIELD)
//public class Project {
//
//	String name;
//	String description;
//	int projectId;
//	
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	public int getProjectId() {
//		return projectId;
//	}
//	public void setProjectId(int projectId) {
//		this.projectId = projectId;
//	}
//}
