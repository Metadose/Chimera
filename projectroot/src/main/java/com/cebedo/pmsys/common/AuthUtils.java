package com.cebedo.pmsys.common;

import org.springframework.security.core.context.SecurityContextHolder;

import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.photo.model.Photo;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.projectfile.model.ProjectFile;
import com.cebedo.pmsys.security.securityaccess.model.SecurityAccess;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.subcontractor.model.Subcontractor;
import com.cebedo.pmsys.systemconfiguration.model.SystemConfiguration;
import com.cebedo.pmsys.systemuser.model.SystemUser;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.team.model.Team;

public abstract class AuthUtils {

	/**
	 * Get the authentication object from the context.
	 * 
	 * @return
	 */
	public static AuthenticationToken getAuth() {
		return (AuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
	}

	public static boolean notNullObjNotSuperAdmin(Object obj) {
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
	public static boolean isActionAuthorized(Project proj) {
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
	public static boolean isActionAuthorized(Staff stf) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (stf.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public static boolean isActionAuthorized(Team team) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (team.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public static boolean isActionAuthorized(SystemConfiguration obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public static boolean isActionAuthorized(SystemUser obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public static boolean isActionAuthorized(Task obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public static boolean isActionAuthorized(ProjectFile obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public static boolean isActionAuthorized(Photo obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public static boolean isActionAuthorized(Subcontractor obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public static boolean isActionAuthorized(SecurityAccess obj) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		}
		return false;
	}
}
