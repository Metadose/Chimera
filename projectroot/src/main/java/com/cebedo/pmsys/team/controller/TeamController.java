package com.cebedo.pmsys.team.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.service.TeamService;

@Controller
@RequestMapping(Team.OBJECT_NAME)
public class TeamController {

	public static final String ATTR_LIST = "teamList";

	public static final String REQUEST_ROOT = "/";
	public static final String REQUEST_LIST = "list";
	public static final String REQUEST_EDIT = "edit";
	public static final String REQUEST_CREATE = "create";

	public static final String JSP_LIST = "teamList";
	public static final String JSP_EDIT = "teamEdit";

	private TeamService teamService;

	@Autowired(required = true)
	@Qualifier(value = "teamService")
	public void setTeamService(TeamService ps) {
		this.teamService = ps;
	}

	@RequestMapping(value = { REQUEST_ROOT, REQUEST_LIST }, method = RequestMethod.GET)
	public String listTeam(Model model) {
		model.addAttribute(ATTR_LIST, this.teamService.list());
		return JSP_LIST;
	}

	@RequestMapping(value = REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(Team.OBJECT_NAME) Team team) {
		if (team.getId() == 0) {
			this.teamService.create(team);
		} else {
			this.teamService.update(team);
		}
		return "redirect:/" + Team.OBJECT_NAME + "/" + REQUEST_LIST;
	}

	@RequestMapping("/delete/{" + Team.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Team.COLUMN_PRIMARY_KEY) int id) {
		this.teamService.delete(id);
		return "redirect:/" + Team.OBJECT_NAME + "/" + REQUEST_LIST;
	}

	@RequestMapping("/edit/{" + Team.COLUMN_PRIMARY_KEY + "}")
	public String editTeam(@PathVariable(Team.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		if (id == 0) {
			model.addAttribute(Team.OBJECT_NAME, new Team());
			return JSP_EDIT;
		}
		model.addAttribute(Team.OBJECT_NAME, this.teamService.getByID(id));
		return JSP_EDIT;
	}
}