package com.cebedo.pmsys.team.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TeamWrapper {

	private final Team team;

	public TeamWrapper(Team stf) {
		this.team = stf;
	}

	public Team getTeam() {
		return team;
	}

	public static List<TeamWrapper> wrap(List<Team> teamList) {
		List<TeamWrapper> wrappedList = new ArrayList<TeamWrapper>();
		for (Team team : teamList) {
			wrappedList.add(new TeamWrapper(team));
		}
		return wrappedList;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TeamWrapper
				&& this.team.getId() == (((TeamWrapper) obj).team.getId());
	}

	@Override
	public int hashCode() {
		return team.hashCode();
	}

	/**
	 * Remove team items without names.
	 * 
	 * @param wrappedTeamList
	 * @return
	 */
	public static List<TeamWrapper> removeEmptyNames(
			List<TeamWrapper> wrappedTeamList) {
		int i = 0;
		List<Integer> toRemove = new ArrayList<Integer>();
		for (TeamWrapper wrappedTeam : wrappedTeamList) {
			Team wrp = wrappedTeam.getTeam();
			if (wrp.getName() == null || wrp.getName().isEmpty()) {
				toRemove.add(i);
			}
			i++;
		}
		int removedIndices = 0;
		for (int index : toRemove) {
			wrappedTeamList.remove(index - removedIndices);
			removedIndices++;
		}
		return wrappedTeamList;
	}

	public static List<Team> unwrap(List<TeamWrapper> wrappedTeamList) {
		List<Team> unwrappedList = new ArrayList<Team>();
		for (TeamWrapper wrappedTeam : wrappedTeamList) {
			unwrappedList.add(wrappedTeam.getTeam());
		}
		return unwrappedList;
	}

	public static List<TeamWrapper> wrap(Set<Team> teamList) {
		List<TeamWrapper> wrappedList = new ArrayList<TeamWrapper>();
		for (Team team : teamList) {
			wrappedList.add(new TeamWrapper(team));
		}
		return wrappedList;
	}
}
