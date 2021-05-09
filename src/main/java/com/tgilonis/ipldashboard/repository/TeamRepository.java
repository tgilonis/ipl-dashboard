package com.tgilonis.ipldashboard.repository;

import com.tgilonis.ipldashboard.model.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long>
{
    // we know that team names are unique - if they weren't we could specify this method to return List<Team>
    // findBy tells JPA to find a value according to a param, with TeamName as that param
    Team findByTeamName(
            String teamName);
}
