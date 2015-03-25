package com.cebedo.pmsys.common;

import org.springframework.security.core.context.SecurityContextHolder;

import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.photo.model.Photo;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.projectfile.model.ProjectFile;
import com.cebedo.pmsys.security.securityaccess.model.SecurityAccess;
import com.cebedo.pmsys.security.securityrole.model.SecurityRole;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.subcontractor.model.Subcontractor;
import com.cebedo.pmsys.systemconfiguration.model.SystemConfiguration;
import com.cebedo.pmsys.systemuser.model.SystemUser;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.team.model.Team;

public class AuthHelper {

	/**
	 * Get the authentication object from the context.
	 * 
	 * @return
	 */
	public AuthenticationToken getAuth() {
		return (AuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
	}

	public boolean notNullObjNotSuperAdmin(Object obj) {
		if (!(obj == null || getAuth().isSuperAdmin())) {
			return true;
		}
		return false;
	}

	/**
	 * Is this operation on the project table authorized?
	 * 
	 * @param stf
	 * @return
	 */
	public boolean isActionAuthorized(Project proj) {
		AuthenticationToken auth = getAuth();

		// If the auth is a super admin,
		// action is good.
		if (auth.isSuperAdmin()) {
			return true;
		}
		// If not, check if the user has the same company with
		// the returned user.
		else if (proj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	/**
	 * Is this operation on the staff table authorized?
	 * 
	 * @param stf
	 * @return
	 */
	public boolean isActionAuthorized(Staff stf) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (stf.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public boolean isActionAuthorized(Team team) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (team.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public boolean isActionAuthorized(SystemConfiguration obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public boolean isActionAuthorized(SystemUser obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public boolean isActionAuthorized(Task obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public boolean isActionAuthorized(ProjectFile obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public boolean isActionAuthorized(Photo obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public boolean isActionAuthorized(Subcontractor obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	/**
	 * TODO Check if we really need this since we're just check if it's super
	 * admin.
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isActionAuthorized(SecurityAccess obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		}
		return false;
	}

	/**
	 * TODO Check if we really need this since we're just check if it's super
	 * admin.
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isActionAuthorized(SecurityRole obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		}
		return false;
	}
}
