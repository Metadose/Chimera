package com.cebedo.pmsys.system.search.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.security.securityaccess.model.SecurityAccess;
import com.cebedo.pmsys.staff.dao.StaffDAO;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.system.helper.AuthHelper;
import com.cebedo.pmsys.system.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.system.search.model.SearchResult;
import com.cebedo.pmsys.task.dao.TaskDAO;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.team.dao.TeamDAO;
import com.cebedo.pmsys.team.model.Team;

@Service
public class SearchServiceImpl implements SearchService {

	private AuthHelper authHelper = new AuthHelper();
	private TaskDAO taskDAO;
	private ProjectDAO projectDAO;
	private StaffDAO staffDAO;
	private TeamDAO teamDAO;

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setStaffDAO(StaffDAO staffDAO) {
		this.staffDAO = staffDAO;
	}

	public void setTeamDAO(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}

	public void setTaskDAO(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	@Override
	@Transactional
	public List<SearchResult> getData() {
		// TODO Cache this method.
		AuthenticationToken auth = this.authHelper.getAuth();
		Long companyID = auth.isSuperAdmin() ? null : auth.getCompany().getId();
		Collection<GrantedAuthority> authorities = auth.getAuthorities();
		List<SearchResult> results = new ArrayList<SearchResult>();

		// Search tasks.
		if (authorities.contains(new SimpleGrantedAuthority(
				SecurityAccess.ACCESS_TASK))) {
			// TODO Evict cache when update is made on tasks.
			List<Task> taskList = this.taskDAO.listTaskFromCache(companyID);
			List<SearchResult> resultList = new ArrayList<SearchResult>();
			for (Task task : taskList) {
				SearchResult result = new SearchResult(task.getTitle(),
						Task.OBJECT_NAME, String.valueOf(task.getId()),
						Task.OBJECT_NAME + "-" + String.valueOf(task.getId()));
				resultList.add(result);
			}
			results.addAll(resultList);
		}

		// Search projects.
		if (authorities.contains(new SimpleGrantedAuthority(
				SecurityAccess.ACCESS_PROJECT))) {
			List<Project> list = this.projectDAO
					.listProjectFromCache(companyID);
			List<SearchResult> resultList = new ArrayList<SearchResult>();
			for (Project obj : list) {
				SearchResult result = new SearchResult(obj.getName(),
						Project.OBJECT_NAME, String.valueOf(obj.getId()),
						Project.OBJECT_NAME + "-" + String.valueOf(obj.getId()));
				resultList.add(result);
			}
			results.addAll(resultList);
		}

		// TODO Search staff.
		// Cannot get full name of staff.
		if (authorities.contains(new SimpleGrantedAuthority(
				SecurityAccess.ACCESS_PROJECT))) {
			// TODO Evict cache when update is made on staff.
			List<Staff> list = this.staffDAO.listStaffFromCache(companyID);
			List<SearchResult> resultList = new ArrayList<SearchResult>();
			for (Staff obj : list) {
				SearchResult result = new SearchResult(obj.getFullName(),
						Staff.OBJECT_NAME, String.valueOf(obj.getId()),
						Staff.OBJECT_NAME + "-" + String.valueOf(obj.getId()));
				resultList.add(result);
			}
			results.addAll(resultList);
		}

		// Search teams.
		if (authorities.contains(new SimpleGrantedAuthority(
				SecurityAccess.ACCESS_TEAM))) {
			// TODO Evict cache when update is made on team.
			List<Team> list = this.teamDAO.listTeamFromCache(companyID);
			List<SearchResult> resultList = new ArrayList<SearchResult>();
			for (Team obj : list) {
				SearchResult result = new SearchResult(obj.getName(),
						Staff.OBJECT_NAME, String.valueOf(obj.getId()),
						Staff.OBJECT_NAME + "-" + String.valueOf(obj.getId()));
				resultList.add(result);
			}
			results.addAll(resultList);
		}

		return results;
	}
}
