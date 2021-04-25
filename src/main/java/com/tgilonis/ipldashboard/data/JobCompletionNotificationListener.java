package com.tgilonis.ipldashboard.data;

import com.tgilonis.ipldashboard.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport
{

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager em;

    @Autowired
    public JobCompletionNotificationListener(
            EntityManager em)
    {
        this.em = em;
    }

    @Override
    @Transactional
    public void afterJob(
            JobExecution jobExecution)
    {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED)
        {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, Team> teamData = new HashMap<>();

            //here we could use union to query distinct team1 and then distinct team 2, but JPA does not support union

            //as such, we are just going to run the query twice separately

            //because of the use of count, we don't need to use distinct
            em.createQuery("select m.team1, count(*) from Match m group by m.team1", Object[].class)
                    .getResultList()
                    .stream()
                    .map(e -> new Team((String) e[0], (long) e[1]))
                    .forEach(team -> teamData.put(team.getTeamName(), team));

            em.createQuery("select m.team2, count(*) from Match m group by m.team2", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e ->
                            //current exception is that a team might have always been in second position, and so we will
                            //not be able to find it - not fixing for now, not too important for exercise
                    {
                        Team team = teamData.get((String)e[0]);
                        team.setTotalMatches(team.getTotalMatches()+((long)e[1]));
                    });

            em.createQuery("select m.matchWinner, count(*) from Match m group by m.matchWinner", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e ->
                    {
                        Team team = teamData.get((String)e[0]);
                        if(team!= null) team.setTotalWins(((long)e[1]));
                    });

            //puts the data into the JPA db
            teamData.values().forEach(em::persist);
            //debug - testing line to ensure teams are created properly
            teamData.values().forEach(System.out::println);




            //simple query to check that db is populating correctly
            /*jdbcTemplate.query("SELECT team1, team2, date FROM match",
                    (rs, row) -> "Team 1 " + rs.getString(1) + " Team 2 " + rs.getString(2) + " Date " + rs.getString(3)
            ).forEach(System.out::println);*/
        }
    }
}
