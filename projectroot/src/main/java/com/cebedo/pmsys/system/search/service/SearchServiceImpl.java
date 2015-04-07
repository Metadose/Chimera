package com.cebedo.pmsys.system.search.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthHelper;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.security.securityaccess.model.SecurityAccess;
import com.cebedo.pmsys.staff.dao.StaffDAO;
import com.cebedo.pmsys.system.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.task.dao.TaskDAO;
import com.cebedo.pmsys.team.dao.TeamDAO;

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
	public List<String> getData() {
		AuthenticationToken auth = this.authHelper.getAuth();
		Collection<GrantedAuthority> authorities = auth.getAuthorities();
		List<String> results = new ArrayList<String>();

		// Search tasks.
		if (authorities.contains(new SimpleGrantedAuthority(
				SecurityAccess.ACCESS_TASK))) {
			List<String> nameList = this.taskDAO.listNames();
			results.addAll(nameList);
		}

		// Search projects.
		if (authorities.contains(new SimpleGrantedAuthority(
				SecurityAccess.ACCESS_PROJECT))) {
			List<String> nameList = this.projectDAO.listNames();
			results.addAll(nameList);
		}

		// TODO Search staff.
		// Cannot get full name of staff.
		if (authorities.contains(new SimpleGrantedAuthority(
				SecurityAccess.ACCESS_STAFF))) {
			List<String> nameList = this.staffDAO.listNames();
			results.addAll(nameList);
		}

		// Search teams.
		if (authorities.contains(new SimpleGrantedAuthority(
				SecurityAccess.ACCESS_TEAM))) {
			List<String> nameList = this.teamDAO.listNames();
			results.addAll(nameList);
		}

		return results;
	}

}
