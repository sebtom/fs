package com.flipsports.xml.opta.f1.matchData;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MatchData {
    private String id;
    private String updatedAt;

    private ZonedDateTime matchStartTime;

    private String venue;
    private String city;

    private String homeTeam;
    private String awayTeam;

    private int homeTeamScore;
    private int awayTeamScore;

    private String winner;
}


//
//<MatchData detail_id="1" last_modified="2016-08-14T19:01:43+00:00" timestamp_accuracy_id="1" timing_id="1" uID="g855174">
//<MatchInfo MatchDay="1" MatchType="Regular" MatchWinner="t80" Period="FullTime" Venue_id="88">
//<Date>2016-08-13 15:00:00</Date>
//<TZ>BST</TZ>
//</MatchInfo>
//<Stat Type="Venue">Turf Moor</Stat>
//<Stat Type="City">Burnley</Stat>
//<TeamData HalfScore="0" Score="0" Side="Home" TeamRef="t90" />
//<TeamData HalfScore="0" Score="1" Side="Away" TeamRef="t80">
//<Goal Period="SecondHalf" PlayerRef="p49277" Type="Goal" />
//</TeamData>
//</MatchData>
