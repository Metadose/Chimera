package com.cebedo.pmsys.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.TeamDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.TeamAssignment;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxFactory;
import com.cebedo.pmsys.wrapper.TeamWrapper;

@Service
public class TeamServiceImpl implements TeamService {

    private static Logger logger = Logger.getLogger(Team.OBJECT_NAME);
    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private LogHelper logHelper = new LogHelper();
    private TeamDAO teamDAO;
    private ProjectDAO projectDAO;
    private CompanyDAO companyDAO;

    public void setCompanyDAO(CompanyDAO companyDAO) {
	this.companyDAO = companyDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setTeamDAO(TeamDAO teamDAO) {
	this.teamDAO = teamDAO;
    }

    /**
     * Create a team.
     */
    @Override
    @Transactional
    public String create(Team team) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Company authCompany = auth.getCompany();
	team.setCompany(authCompany);
	if (this.authHelper.isActionAuthorized(team)) {
	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.CREATE, team);

	    // Do action.
	    this.teamDAO.create(team);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateCreate(Team.OBJECT_NAME,
		    team.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		Team.OBJECT_NAME, team.getId(), team.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateCreate(Team.OBJECT_NAME,
		team.getName());
    }

    /**
     * Get team by id.
     */
    @Override
    @Transactional
    public Team getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Team team = this.teamDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(team)) {
	    // Log info.
	    logger.info(this.logHelper.logGetObject(auth, Team.OBJECT_NAME, id,
		    team.getName()));

	    // Return obj.
	    return team;
	}

	// Log warn.
	logger.info(this.logHelper.logUnauthorized(auth, AuditAction.GET,
		Team.OBJECT_NAME, id, team.getName()));

	// Return empty.
	return new Team();
    }

    /**
     * Update a team.
     */
    @SuppressWarnings("deprecation")
    @Override
    @Transactional
    public String update(Team team) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Company company = this.companyDAO.getCompanyByObjID(Team.TABLE_NAME,
		Team.COLUMN_PRIMARY_KEY, team.getId());
	team.setCompany(company);
	if (this.authHelper.isActionAuthorized(team)) {
	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.UPDATE, team);

	    // Do service.
	    this.teamDAO.update(team);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateUpdate(Team.OBJECT_NAME,
		    team.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		Team.OBJECT_NAME, team.getId(), team.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateUpdate(Team.OBJECT_NAME,
		team.getName());
    }

    /**
     * Delete a team.
     */
    @Override
    @Transactional
    public String delete(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Team team = this.teamDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(team)) {
	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.DELETE, team);

	    // Do service.
	    this.teamDAO.delete(id);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateDelete(Team.OBJECT_NAME,
		    team.getName());
	}
	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE,
		Team.OBJECT_NAME, id, team.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateDelete(Team.OBJECT_NAME,
		team.getName());
    }

    /**
     * List all teams.
     */
    @Override
    @Transactional
    public List<Team> list() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    // Log list as admin.
	    logger.info(this.logHelper.logListAsSuperAdmin(token,
		    Team.OBJECT_NAME));

	    // Return list.
	    return this.teamDAO.list(null);
	}
	// Log list.
	logger.info(this.logHelper.logListFromCompany(token, Team.OBJECT_NAME,
		token.getCompany()));

	// Return list.
	return this.teamDAO.list(token.getCompany().getId());
    }

    /**
     * Assign a team under a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String assignProjectTeam(long projectID, long teamID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(projectID);
	Team team = this.teamDAO.getByID(teamID);

	if (this.authHelper.isActionAuthorized(project)
		&& this.authHelper.isActionAuthorized(team)) {

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.ASSIGN, project,
		    team);

	    // Do action.
	    TeamAssignment assignment = new TeamAssignment();
	    assignment.setProjectID(projectID);
	    assignment.setTeamID(teamID);
	    this.teamDAO.assignProjectTeam(assignment);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateAssign(Team.OBJECT_NAME,
		    team.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Project.OBJECT_NAME, project.getId(), project.getName()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Team.OBJECT_NAME, team.getId(), team.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateAssign(Team.OBJECT_NAME,
		team.getName());
    }

    /**
     * Unassign a team under a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String unassignProjectTeam(long projectID, long teamID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(projectID);
	Team team = this.teamDAO.getByID(teamID);

	if (this.authHelper.isActionAuthorized(project)
		&& this.authHelper.isActionAuthorized(team)) {
	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.UNASSIGN,
		    project, team);

	    // Do service.
	    this.teamDAO.unassignProjectTeam(projectID, teamID);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateUnassign(Team.OBJECT_NAME,
		    team.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Project.OBJECT_NAME, project.getId(), project.getName()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Team.OBJECT_NAME, team.getId(), team.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateUnassign(Team.OBJECT_NAME,
		team.getName());
    }

    /**
     * Unassign all teams under project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String unassignAllProjectTeams(long projectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(projectID);

	if (this.authHelper.isActionAuthorized(project)) {
	    // Log and notify.
	    this.messageHelper.sendUnassignAll(Team.OBJECT_NAME, project);

	    // Do service.
	    this.teamDAO.unassignAllProjectTeams(projectID);

	    // Return success.
	    return AlertBoxFactory.SUCCESS
		    .generateUnassignAll(Team.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, Project.OBJECT_NAME, projectID,
		project.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateUnassignAll(Team.OBJECT_NAME);
    }

    /**
     * Get an object with all collections given an id.
     */
    @Override
    @Transactional
    public Team getWithAllCollectionsByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Team team = this.teamDAO.getWithAllCollectionsByID(id);

	if (this.authHelper.isActionAuthorized(team)) {
	    // Log info.
	    logger.info(this.logHelper.logGetObjectWithAllCollections(auth,
		    Team.OBJECT_NAME, id, team.getName()));

	    // Return obj.
	    return team;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.GET_WITH_COLLECTIONS, Team.OBJECT_NAME, id,
		team.getName()));

	// Return empty.
	return new Team();
    }

    /**
     * Unassign all staff under a team.
     */
    @Override
    @Transactional
    public String unassignAllMembers(long teamID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Team team = this.teamDAO.getByID(teamID);

	if (this.authHelper.isActionAuthorized(team)) {
	    // Log and notify.
	    this.messageHelper.sendUnassignAll(Staff.OBJECT_NAME, team);

	    // Do service.
	    this.teamDAO.unassignAllMembers(teamID);

	    // Return success.
	    return AlertBoxFactory.SUCCESS
		    .generateUnassignAll(Staff.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, Team.OBJECT_NAME, teamID,
		team.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateUnassignAll(Staff.OBJECT_NAME);
    }

    /**
     * Unassigns all associated projects under the team.
     */
    @Override
    @Transactional
    public String unassignAllProjectsAssociatedToTeam(long teamID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Team team = this.teamDAO.getByID(teamID);

	if (this.authHelper.isActionAuthorized(team)) {

	    // Log and notify.
	    this.messageHelper.sendUnassignAll(Project.OBJECT_NAME, team);

	    // Do service.
	    this.teamDAO.unassignAllTeamsFromProject(teamID);

	    // Return success.
	    return AlertBoxFactory.SUCCESS
		    .generateUnassignAll(Project.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, Team.OBJECT_NAME, teamID,
		team.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateUnassignAll(Project.OBJECT_NAME);
    }

    /**
     * List all teams.
     */
    @Override
    @Transactional
    public List<Team> list(Long companyID) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (auth.isSuperAdmin()) {
	    // Log info super admin.
	    logger.info(this.logHelper.logListAsSuperAdmin(auth,
		    Team.OBJECT_NAME));

	    // Return list.
	    return this.teamDAO.list(null);
	}

	// Log info non-super.
	Company company = this.companyDAO.getByID(companyID);
	logger.info(this.logHelper.logListFromCompany(auth, Team.OBJECT_NAME,
		company));

	// Return list.
	return this.teamDAO.list(companyID);
    }

    /**
     * List teams that are not assigned in project.
     */
    @Override
    @Transactional
    public List<Team> listUnassignedInProject(Long companyID, Project project) {
	if (this.authHelper.isActionAuthorized(project)) {
	    List<Team> companyTeamList = this.teamDAO.list(companyID);
	    List<TeamWrapper> wrappedTeamList = TeamWrapper
		    .wrap(companyTeamList);
	    List<TeamWrapper> wrappedAssignedList = TeamWrapper.wrap(project
		    .getAssignedTeams());
	    wrappedTeamList.removeAll(wrappedAssignedList);
	    return TeamWrapper.unwrap(wrappedTeamList);
	}
	return new ArrayList<Team>();
    }

    /**
     * List teams with initiated tasks.
     */
    @Override
    @Transactional
    public List<Team> listWithTasks() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    // Log info super admin.
	    logger.info(this.logHelper.logListPartialCollectionsAsSuperAdmin(
		    token, Team.OBJECT_NAME, Task.OBJECT_NAME));

	    // Return list.
	    return this.teamDAO.listWithTasks(null);
	}

	// Log info non-super admin.
	Company company = token.getCompany();
	logger.info(this.logHelper.logListPartialCollectionsFromCompany(token,
		Team.OBJECT_NAME, Task.OBJECT_NAME, company));

	// Return list.
	return this.teamDAO.listWithTasks(company.getId());
    }

    /**
     * Get name of team given an id.
     */
    @Override
    @Transactional
    public String getNameByID(long teamID) {
	return this.teamDAO.getNameByID(teamID);
    }

}
