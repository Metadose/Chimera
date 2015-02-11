package com.cebedo.pmsys.team.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.service.TeamService;

@Controller
@RequestMapping(Team.OBJECT_NAME)
public class TeamController {

	public static final String ATTR_LIST = "teamList";
	public static final String ATTR_PROJECT = Team.OBJECT_NAME;
	public static final String JSP_LIST = "teamList";
	public static final String JSP_EDIT = "teamEdit";

	private TeamService teamService;

	@Autowired(required = true)
	@Qualifier(value = "teamService")
	public void setTeamService(TeamService ps) {
		this.teamService = ps;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listTeams(Model model) {
		model.addAttribute(ATTR_LIST, this.teamService.list());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_PROJECT) Team team) {
		if (team.getId() == 0) {
			this.teamService.create(team);
		} else {
			this.teamService.update(team);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECT + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_DELETE + "/{"
			+ Team.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Team.COLUMN_PRIMARY_KEY) int id) {
		this.teamService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECT + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
			+ Team.COLUMN_PRIMARY_KEY + "}")
	public String editTeam(@PathVariable(Team.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		if (id == 0) {
			model.addAttribute(ATTR_PROJECT, new Team());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_PROJECT, this.teamService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}
}