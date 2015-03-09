package com.cebedo.pmsys.common;

import org.springframework.security.core.context.SecurityContextHolder;

import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.Staff;
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
	public static boolean isProjectActionAuthorized(Project proj) {
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
	public static boolean isStaffActionAuthorized(Staff stf) {
		AuthenticationToken auth = getAuth();

		// If the auth is a super admin,
		// return the user.
		if (auth.isSuperAdmin()) {
			return true;
		}
		// If not, check if the user has the same company with
		// the returned user.
		else if (stf.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

	public static boolean isTeamActionAuthorized(Team team) {
		AuthenticationToken auth = getAuth();
		if (auth.isSuperAdmin()) {
			return true;
		} else if (team.getCompany().getId() == auth.getCompany().getId()) {
			return true;
		}
		return false;
	}

}
