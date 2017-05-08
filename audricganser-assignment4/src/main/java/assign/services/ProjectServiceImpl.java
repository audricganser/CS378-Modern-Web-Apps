package assign.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import assign.domain.*;

public class ProjectServiceImpl implements ProjectService {

	String dbURL = "";
	String dbUsername = "";
	String dbPassword = "";
	DataSource ds;

	// DB connection information would typically be read from a config file.
	public ProjectServiceImpl(String dbUrl, String username, String password) {
		this.dbURL = dbUrl;
		this.dbUsername = username;
		this.dbPassword = password;
		
		ds = setupDataSource();
	}
	
	public DataSource setupDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUsername(this.dbUsername);
        ds.setPassword(this.dbPassword);
        ds.setUrl(this.dbURL);
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        return ds;
    }
	
	public Project getProject(int project_id) throws Exception {
		String query = "SELECT * FROM projects WHERE project_id=?";
		try {
			Connection conn = ds.getConnection();
			PreparedStatement s = conn.prepareStatement(query);
			s.setString(1, String.valueOf(project_id));
			ResultSet r = s.executeQuery();
			
			if (!r.next()) {
				return null;
			}
			Project proj = new Project();
			proj.setName(r.getString("name"));
			proj.setDescription(r.getString("description"));
			proj.setProjectId(r.getInt("project_id"));
			
			return proj;	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("failed to get project");
		return null;
	}
	
	
	
	
	
	public Project addProject(Project p) throws Exception {
		try {
			Connection conn = ds.getConnection();
			
			String insert = "INSERT INTO projects(name, description) VALUES(?, ?)";
			PreparedStatement stmt = conn.prepareStatement(insert,
	                Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, p.getName());
			stmt.setString(2, p.getDescription());
			
			int affectedRows = stmt.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Creating project failed, no rows affected.");
	        }
	        
	        ResultSet generatedKeys = stmt.getGeneratedKeys();
	        if (generatedKeys.next()) {
	        	p.setProjectId(generatedKeys.getInt(1));
	        }
	        else {
	            throw new SQLException("Creating project failed, no ID obtained.");
	        }
	        
	        // Close the connection
	        conn.close();
	        
			return p;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public boolean updateProject(Project project) throws Exception {
		String query = "UPDATE projects SET name=? , description=? WHERE project_id=?";
		try {
			Connection conn = ds.getConnection();
			PreparedStatement s = conn.prepareStatement(query);
			s.setString(1, project.getName());
			s.setString(2, project.getDescription());
			s.setString(3, String.valueOf(project.getProjectId()));
			int affectedRows = s.executeUpdate();
			
			if (affectedRows > 0) {
				return true;
			}
			else {
				return false;
			}
				
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("failed to update project");
		return false;
	}
	
	
	public boolean deleteProject(int project_id) throws Exception {
		String query = "DELETE from projects WHERE project_id=?";
		try {
			Connection conn = ds.getConnection();
			PreparedStatement s = conn.prepareStatement(query);
			s.setString(1, String.valueOf(project_id));
			int affectedRows = s.executeUpdate();
			
			if (affectedRows > 0) {
				return true;
			}
			else {
				System.out.println("failed to delete project");
				return false;
			}
				
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("failed to delete project");
		return false;
	}
	

}