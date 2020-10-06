import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final int teamVertexCount;
    private final int pairVertexCount;
    private final int vertexCount;
    private final int startVertexIndex;
    private final int firstTeamVertexIndex;
    private final int endVertexIndex;

    private int teamCount;
    private Team[] teams;
    private HashMap<String, Integer> teamNameToIndex;
    private int[][] remainingGames;

    public BaseballElimination(String filename) {
        readTeamsFromFile(filename);
        for (int i = 0; i < teams.length; i++) {
            Team team = teams[i];
            teamNameToIndex.put(team.name, i);
        }

        teamVertexCount = teamCount - 1;
        pairVertexCount = teamVertexCount*(int)Math.floor(1.0d*teamVertexCount/2);
        vertexCount = 1 + pairVertexCount + teamVertexCount + 1;
        startVertexIndex = 0;
        firstTeamVertexIndex = pairVertexCount + 1;
        endVertexIndex = vertexCount - 1;
    }

    private void readTeamsFromFile(String filename) {
        nullGuard(filename);
        In fileIn = new In(filename);
        teamCount = fileIn.readInt();
        teams = new Team[teamCount];
        remainingGames = new int[teamCount][teamCount];

        for (int i = 0; i < teamCount; i++) {
            String teamName = fileIn.readString();
            int wins = fileIn.readInt();
            int losses = fileIn.readInt();
            int gamesToPlay = fileIn.readInt();
            teams[i] = new Team(teamName, wins, losses, gamesToPlay);
            for (int j = 0; j < teamCount; j++) {
                int remaining = fileIn.readInt();
                remainingGames[i][j] = remaining;
            }
        }
        fileIn.close();
    }

    private class Team {
        public String name;
        public int wins;
        public int losses;
        public int remainingGames;

        private Team(String teamName, int winCount, int lossCount, int remainingCount) {
            name = teamName;
            wins = winCount;
            losses = lossCount;
            remainingGames = remainingCount;
        }
    }

    public int numberOfTeams() {
        return teamCount;
    }

    public Iterable<String> teams() {
        return teamNameToIndex.keySet();
    }

    public int wins(String team) {
        teamNameGuard(team);
        return getTeam(team).wins;
    }

    public int losses(String team) {
        teamNameGuard(team);
        return getTeam(team).losses;
    }

    public int remaining(String team) {
        // number of remaining games for given team
        teamNameGuard(team);
        return getTeam(team).remainingGames;
    }

    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        teamNameGuard(team1);
        teamNameGuard(team2);
        int team1Index = getTeamIndex(team1);
        int team2Index = getTeamIndex(team1);
        return remainingGames[team1Index][team2Index];
    }

    public boolean isEliminated(String team) {
        // is given team eliminated?
        teamNameGuard(team);
        // TODO
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        // TODO
        return null;
    }


    private boolean isTeamEliminated(String questionedTeamName) {
        FlowNetwork flowNetwork = getFlowNetwork(questionedTeamName);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, startVertexIndex, endVertexIndex);
        Iterable<FlowEdge> edges = flowNetwork.edges();


        return true;
    }


    private FlowNetwork getFlowNetwork(String questionedTeamName) {
        int quesitonedTeamIndex = getTeamIndex(questionedTeamName);
        Team questionedTeam = getTeam(questionedTeamName);
        FlowNetwork flowNetwork = new FlowNetwork(vertexCount);

        for (int i = 0; i < teamVertexCount; i++) {
            for (int j = i + 1; j < teamVertexCount; j++) {
                int pairIndex = i*teamVertexCount + j + 1;
                flowNetwork.addEdge(new FlowEdge(startVertexIndex, pairIndex, remainingGames[i][j]));
                flowNetwork.addEdge(new FlowEdge(pairIndex, firstTeamVertexIndex + i, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(pairIndex, firstTeamVertexIndex + j, Double.POSITIVE_INFINITY));
            }
            int teamIndex = i;
            if (i >= quesitonedTeamIndex) {
                teamIndex = i + 1;
            }
            Team team = teams[teamIndex];
            double endCapacity = questionedTeam.wins + questionedTeam.remainingGames - team.wins;
            if (endCapacity < 0) {
                endCapacity = 0;
            }
            flowNetwork.addEdge(new FlowEdge(firstTeamVertexIndex + i, endVertexIndex, endCapacity));
        }

        return flowNetwork;
    }

    private boolean areStartEdgesNotFullAndEndEdgesFull(Iterable<FlowEdge> edges) {
        Iterable<FlowEdge> startEdges = new ArrayList<FlowEdge>(pairVertexCount);
        // TODO
        return true;
    }

    private void teamNameGuard(String teamName) {
        nullGuard(teamName);
        unknownTeamGuard(teamName);
    }

    private void nullGuard(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    private int getStartVertexIndex() {
        return 0;
    }

    private void unknownTeamGuard(String teamName) {
        if (!teamNameToIndex.containsKey(teamName)) {
            throw new IllegalArgumentException("Unknown team");
        }
    }

    private Team getTeam(String teamName) {
        int index = getTeamIndex(teamName);
        return teams[index];
    }

    private int getTeamIndex(String teamName) {
        return teamNameToIndex.get(teamName);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
