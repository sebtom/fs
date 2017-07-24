package com.flipsports.xml.opta.f1.matchData;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchData {
    private String id;
    private LocalDateTime updatedAt;

    private LocalDateTime matchStartTime;

    private String venue;
    private String city;

    private String homeTeam;
    private String awayTeam;

    private int homeTeamScore;
    private int awayTeamScore;

    private String winner;
}