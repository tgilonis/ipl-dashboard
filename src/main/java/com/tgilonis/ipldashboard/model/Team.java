package com.tgilonis.ipldashboard.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Team
{
    // gives JPA a default contructor to use
    public Team()
    {}

    @Id
    //ask JPA to create own ID for the table
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private long id;
    private String teamName;
    private long totalMatches;
    private long totalWins;

    @Transient // tells JPA to ignore this field - i.e. we do not want to persist it as a data entry into the db
    private List<Match> matches;
    // transient field used just to pass information for a purpose without adding it to the db


    public Team(
            String teamName,
            long totalMatches)
    {
        this.teamName = teamName;
        this.totalMatches = totalMatches;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTeamName()
    {
        return teamName;
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    public long getTotalMatches()
    {
        return totalMatches;
    }

    public void setTotalMatches(long totalMatches)
    {
        this.totalMatches = totalMatches;
    }

    public long getTotalWins()
    {
        return totalWins;
    }

    public void setTotalWins(long totalWins)
    {
        this.totalWins = totalWins;
    }

    @Override
    public String toString()
    {
        return "Team{" +
                "teamName='" + teamName + '\'' +
                ", totalMatches=" + totalMatches +
                ", totalWins=" + totalWins +
                '}';
    }

    public List<Match> getMatches()
    {
        return matches;
    }

    public void setMatches(List<Match> matches)
    {
        this.matches = matches;
    }
}
