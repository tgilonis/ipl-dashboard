package com.tgilonis.ipldashboard.repository;

import com.tgilonis.ipldashboard.model.Match;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Long>
{
    // JPA interprets the name as a query - get all matches where team1 == teamname1 or team2 == teamname2
    // sort by date descending
    List<Match> getByTeam1OrTeam2OrderByDateDesc(
            String teamName1,
            String teamName2,
            Pageable pageable); // pageable allows us to specify later how many pages worth of information we want

    // we could use a DAO (https://www.baeldung.com/java-dao-pattern) but default method is simple to implement
    default List<Match> findLatestMatchesByTeam(
            String teamName,
            int count)
    {
        return getByTeam1OrTeam2OrderByDateDesc(teamName, teamName, PageRequest.of(0, count));
    }

}
