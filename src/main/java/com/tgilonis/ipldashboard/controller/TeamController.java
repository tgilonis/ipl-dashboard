package com.tgilonis.ipldashboard.controller;

import com.tgilonis.ipldashboard.model.Team;
import com.tgilonis.ipldashboard.repository.MatchRepository;
import com.tgilonis.ipldashboard.repository.TeamRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamController
{
    private TeamRepository teamRepository;
    private MatchRepository matchRepository;

    public TeamController(
            TeamRepository teamRepository,
            MatchRepository matchRepository)
    {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(
            @PathVariable String teamName)
    {
        Team team = this.teamRepository.findByTeamName(teamName);

        // we can specify to JPA how many pages worth of information we want by using pageable, however we do not want
        // to include a data element within the controller class (too tightly coupled)
        /*Pageable pageable = PageRequest.of(0,4);*/
        // get all matches where this team was playing
        team.setMatches(this.matchRepository.findLatestMatchesByTeam(teamName, 4));

        return team;
    }
}
