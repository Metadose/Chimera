package com.cebedo.pmsys.service;

import java.util.List;
import java.util.Set;

import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Team;

public interface TeamService {

    public String create(Team team);

    public Team getByID(long id);

    public String update(Team team);

    public String delete(long id);

    public List<Team> list();

    public List<Team> list(Long companyID);

    public String assignProjectTeam(long projectID, long teamID);

    public String unassignProjectTeam(long projectID, long teamID);

    public String unassignAllProjectTeams(long projectID);

    public Team getWithAllCollectionsByID(long id);

    public String unassignAllMembers(long teamID);

    public String unassignAllProjectsAssociatedToTeam(long teamID);

    public List<Team> listUnassignedInProject(Long companyID, Project project);

    public List<Team> listWithTasks();

    public String getNameByID(long teamID);

    /**
     * List all the company teams except a list of teams.
     * 
     * @param coID
     * @param teams
     * @return
     */
    public List<Team> listExcept(Long coID, Set<Team> teams);

    /**
     * List all the company Project-based teams except a list of teams.
     * 
     * @param coID
     * @param teams
     * @return
     */
    public List<Team> listTaskBasedExcept(Long coID, Set<Team> teams);

}
