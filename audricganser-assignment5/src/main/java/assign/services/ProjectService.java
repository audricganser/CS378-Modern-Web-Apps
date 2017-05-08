package assign.services;

import assign.domain.*;

public interface ProjectService {

	public Project addProject(Project p) throws Exception;
	
	public boolean updateProject(Project project) throws Exception;
	
	public boolean deleteProject(int project_id) throws Exception;
        
    public Project getProject(int project_id) throws Exception;

}
