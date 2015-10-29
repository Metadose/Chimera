package com.cebedo.pmsys.helper;

import java.util.Collection;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cebedo.pmsys.constants.ConstantsAuthority.AuthorizedAction;
import com.cebedo.pmsys.constants.ConstantsAuthority.AuthorizedModule;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.EstimationOutput;
import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.token.AuthenticationToken;

public class AuthHelper {

    private Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
    private static final String PASSWORD_SALT = "3edc4rfv5tgb";

    /**
     * Get the authentication object from the context.
     * 
     * @return
     */
    public AuthenticationToken getAuth() {
	return (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean isPasswordValid(String rawPassword, SystemUser user) {
	return this.passwordEncoder.isPasswordValid(user.getPassword(), rawPassword,
		user.getUsername() + PASSWORD_SALT);
    }

    public String encodePassword(String rawPassword, SystemUser user) {
	return this.passwordEncoder.encodePassword(rawPassword, user.getUsername() + PASSWORD_SALT);
    }

    /**
     * Is this operation on the project table authorized?
     * 
     * @param stf
     * @return
     */
    public boolean isActionAuthorized(Project proj) {
	AuthenticationToken auth = getAuth();
	Company projCom = proj.getCompany();
	Company authCom = auth.getCompany();

	// If the auth is a super admin,
	// action is good.
	if (auth.isSuperAdmin()) {
	    return true;
	}
	// If not, check if the user has the same company with
	// the returned user.
	else if (projCom.getId() == authCom.getId()) {
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

    public boolean isActionAuthorized(AuditLog obj) {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
	    return true;
	}
	return false;
    }

    public boolean isSuperAdmin() {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	}
	return false;
    }

    public boolean isCompanyAdmin() {
	AuthenticationToken auth = getAuth();
	if (auth.isCompanyAdmin() || auth.isSuperAdmin()) {
	    return true;
	}
	return false;
    }

    public boolean isActionAuthorized(Attendance attendance) {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	} else if (attendance.getCompany().getId() == auth.getCompany().getId()) {
	    return true;
	}
	return false;
    }

    public boolean isActionAuthorized(Delivery obj) {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
	    return true;
	}
	return false;
    }

    public boolean isActionAuthorized(EstimationOutput obj) {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
	    return true;
	}
	return false;
    }

    public boolean isActionAuthorized(Material obj) {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
	    return true;
	}
	return false;
    }

    public boolean isActionAuthorized(ProjectAux obj) {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
	    return true;
	}
	return false;
    }

    public boolean isActionAuthorized(ProjectPayroll obj) {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
	    return true;
	}
	return false;
    }

    public boolean isActionAuthorized(PullOut obj) {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
	    return true;
	}
	return false;
    }

    public boolean hasAccess(Company company) {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	} else if (company.getId() == auth.getCompany().getId()) {
	    return true;
	}
	return false;
    }

    public boolean isActionAuthorized(EstimateCost obj) {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
	    return true;
	}
	return false;
    }

    public boolean isActionAuthorized(Expense obj) {
	AuthenticationToken auth = getAuth();
	if (auth.isSuperAdmin()) {
	    return true;
	} else if (obj.getCompany().getId() == auth.getCompany().getId()) {
	    return true;
	}
	return false;
    }

    public boolean hasAuthority(AuthorizedModule module, AuthorizedAction action) {
	if (isCompanyAdmin()) {
	    return true;
	}
	Collection<GrantedAuthority> authorities = this.getAuth().getAuthorities();
	String testModAction = String.format("%s_%s", module, action);
	for (GrantedAuthority auth : authorities) {
	    if (auth.toString().equals(testModAction)) {
		return true;
	    }
	}
	return false;
    }
}
