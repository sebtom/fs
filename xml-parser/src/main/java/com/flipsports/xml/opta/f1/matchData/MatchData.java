package com.flipsports.xml.opta.f1.matchData;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MatchData {
    private String id;
    private ZonedDateTime updatedAt;

    private ZonedDateTime matchStartTime;

    private String venue;
    private String city;

    private String homeTeam;
    private String awayTeam;

    private int homeTeamScore;
    private int awayTeamScore;

    private String winner;
}