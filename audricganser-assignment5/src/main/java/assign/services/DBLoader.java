package assign.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import assign.domain.Meeting;
import assign.domain.Project;

import java.util.logging.*;

public class DBLoader {
	private SessionFactory sessionFactory;

	Logger logger;
	
	public DBLoader() {
		// A SessionFactory is set up once for an application
        sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
        
        logger = Logger.getLogger("EavesdropReader");
	}
	
	public void loadData(Map<String, List<String>> data) {
		logger.info("Inside loadData");
	}

	public Project addProject(Project p) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Project newProject = null;
		try {
			tx = session.beginTransaction();
			newProject = new Project(p.getName(), p.getDescription());
			session.save(newProject);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();
		}
		return newProject;
	}
	
	public Meeting getMeeting(long meeting_id) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Meeting.class).
        		add(Restrictions.eq("meetingId", meeting_id));
		
		List<Meeting> meetings = criteria.list();
		
		if (meetings.size() > 0) {
			session.close();
			return meetings.get(0);	
		} else {
			session.close();
			return null;
		}
	}
	
	public Project getProject(long project_id) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Project.class).
        		add(Restrictions.eq("projectId", project_id));
		
		List<Project> projects = criteria.list();
		
		if (projects.size() > 0) {
			Hibernate.initialize(projects.get(0).getMeetings());
			session.close();
			return projects.get(0);	
		} else {
			session.close();
			return null;
		}
	}
	
	//add meeting
	public Meeting addMeeting(Meeting m, Project p) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Meeting newMeeting = null;
		try {
			tx = session.beginTransaction();
			newMeeting = new Meeting(m.getName(), m.getYear());
			p.getMeetings().add(m);
			newMeeting.setMeetingId(m.getMeetingId());
			newMeeting.setProject(p);
			session.save(newMeeting);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();
		}
		return newMeeting;
	}
	
	
	public boolean deleteProject(long project_id) throws Exception {
		Session session = sessionFactory.openSession();		
		session.beginTransaction();		
		Project project = getProject(project_id);
		session.delete(project);
	    session.getTransaction().commit();
	    session.close();
	    return true;
	}
		
			
	public boolean updateMeeting(Meeting meeting, long meeting_id) throws Exception {
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		
		Meeting meetings = getMeeting(meeting_id);
		if(meetings == null) {
			return false;
		}
		meetings.setName(meeting.getName());
		meetings.setYear(meeting.getYear());
		session.update(meetings);
		
	    session.getTransaction().commit();
	    session.close();
	    return true;
	}
}

	

