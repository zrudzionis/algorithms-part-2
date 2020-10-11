import java.util.HashMap;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final int teamVertexCount;
    private final int pairVertexCount;
    private final int vertexCount;
    private final int startVertexIndex;
    private final int firstTeamVertexIndex;
    private final int endVertexIndex;
    private final HashMap<String, Integer> teamNameToIndex = new HashMap<String, Integer>();

    private int teamCount;
    private Team[] teams;
    private int[][] remainingGames;

    public BaseballElimination(String filename) {
        readTeamsFromFile(filename);
        for (int i = 0; i < teams.length; i++) {
            Team team = teams[i];
            teamNameToIndex.put(team.name, i);
        }

        teamVertexCount = teamCount - 1;
        pairVertexCount = teamVertexCount*(int) Math.floor(1.0d*teamVertexCount/2);
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
        int team2Index = getTeamIndex(team2);
        return remainingGames[team1Index][team2Index];
    }

    public Iterable<String> certificateOfElimination(String teamName) {
        // subset R of teams that eliminates given team; null if not eliminated
        teamNameGuard(teamName);
        int questionedTeamIndex = teamNameToIndex.get(teamName);
        FlowNetwork flowNetwork = getFlowNetwork(questionedTeamIndex);
        FordFulkerson fulkerson = new FordFulkerson(flowNetwork, startVertexIndex, endVertexIndex);
        boolean isEliminated = isTeamEliminated(flowNetwork, questionedTeamIndex);
        if (isEliminated) {
            return getCertificate(questionedTeamIndex, fulkerson);
        }
        return null;
    }

    private Stack<String> getCertificate(int questionedTeamIndex, FordFulkerson fulkerson) {
        Team questionedTeam = teams[questionedTeamIndex];
        int questionedTeamMaxWins = questionedTeam.wins + questionedTeam.remainingGames;
        Stack<String> teamNames = new Stack<String>();
        for (int i = firstTeamVertexIndex; i < endVertexIndex; i++) {
            int teamIndex = i - firstTeamVertexIndex;
            if (teamIndex >= questionedTeamIndex) {
                teamIndex += 1;
            }
            Team team = teams[teamIndex];

            if (fulkerson.inCut(i)) {
                teamNames.push(team.name);
            } else if (team.wins > questionedTeamMaxWins) {
                teamNames.push(team.name);
            }
        }
        return teamNames;
    }

    public boolean isEliminated(String teamName) {
        teamNameGuard(teamName);
        int questionedTeamIndex = teamNameToIndex.get(teamName);
        FlowNetwork flowNetwork = getFlowNetwork(questionedTeamIndex);
        new FordFulkerson(flowNetwork, startVertexIndex, endVertexIndex);
        return isTeamEliminated(flowNetwork, questionedTeamIndex);
    }

    private FlowNetwork getFlowNetwork(int questionedTeamIndex) {
        Team questionedTeam = teams[questionedTeamIndex];
        FlowNetwork flowNetwork = new FlowNetwork(vertexCount);

        int pairVertexIndex = 0;
        for (int i = 0; i < teamVertexCount; i++) {
            int iTeamIndex = i;
            if (i >= questionedTeamIndex) {
                iTeamIndex += 1;
            }
            for (int j = i + 1; j < teamVertexCount; j++) {
                int jTeamIndex = j;
                if (j >= questionedTeamIndex) {
                    jTeamIndex += 1;
                }
                pairVertexIndex += 1;
                flowNetwork.addEdge(new FlowEdge(startVertexIndex, pairVertexIndex, remainingGames[iTeamIndex][jTeamIndex]));
                flowNetwork.addEdge(new FlowEdge(pairVertexIndex, firstTeamVertexIndex + i, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(pairVertexIndex, firstTeamVertexIndex + j, Double.POSITIVE_INFINITY));
            }
            Team team = teams[iTeamIndex];
            double endCapacity = questionedTeam.wins + questionedTeam.remainingGames - team.wins;
            if (endCapacity < 0) {
                endCapacity = 0;
            }
            flowNetwork.addEdge(new FlowEdge(firstTeamVertexIndex + i, endVertexIndex, endCapacity));
        }

        return flowNetwork;
    }

    private boolean isTeamEliminated(FlowNetwork flowNetwork, int questionedTeamIndex) {
        Team questionedTeam = teams[questionedTeamIndex];
        int questionedTeamMaxWins = questionedTeam.wins + questionedTeam.remainingGames;
        for (int i = 0; i < teamCount; i++) {
            Team team = teams[i];
            if (team.wins > questionedTeamMaxWins) {
                return true;
            }
        }

        for (FlowEdge endEdge : flowNetwork.adj(endVertexIndex)) {
            boolean isEndEdgeFull = endEdge.flow() == endEdge.capacity();
            boolean areStartEdgesFull = areAllStartEdgesFull(flowNetwork, endEdge, endVertexIndex);
            if (isEndEdgeFull && !areStartEdgesFull) {
                return true;
            }
        }
        return false;
    }


    private boolean areAllStartEdgesFull(FlowNetwork flowNetwork, FlowEdge startEdge, int visitedVertexIndex) {
        if (startEdge.from() == startVertexIndex || startEdge.to() == startVertexIndex) {
            return startEdge.flow() == startEdge.capacity();
        }
        boolean allAreFull = true;
        int nextVertexIndex = startEdge.other(visitedVertexIndex);
        for (FlowEdge edge : flowNetwork.adj(nextVertexIndex)) {
            if (edge.from() >= visitedVertexIndex || edge.to() >= visitedVertexIndex) {
                continue;
            }
            allAreFull = allAreFull && areAllStartEdgesFull(flowNetwork, edge, nextVertexIndex);
        }
        return allAreFull;
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
        if (args.length != 1) {
            throw new IllegalArgumentException("Please provide input filename!");
        }
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
