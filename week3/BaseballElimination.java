import java.util.HashMap;
import java.util.Iterator;

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

    private int teamCount;
    private Team[] teams;
    private HashMap<String, Integer> teamNameToIndex = new HashMap<String, Integer>();
    private int[][] remainingGames;

    public BaseballElimination(String filename) {
        readTeamsFromFile(filename);
        for (int i = 0; i < teams.length; i++) {
            Team team = teams[i];
            //            System.out.println(String.format("%d = %s", i, team.name));
            teamNameToIndex.put(team.name, i);
        }

        teamVertexCount = teamCount - 1;
        pairVertexCount = teamVertexCount*(int)Math.floor(1.0d*teamVertexCount/2);
        vertexCount = 1 + pairVertexCount + teamVertexCount + 1;
        startVertexIndex = 0;
        firstTeamVertexIndex = pairVertexCount + 1;
        endVertexIndex = vertexCount - 1;
        //        System.out.println(String.format("pairVertexCount: %d", pairVertexCount));
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

    public Iterable<String> certificateOfElimination(String teamName) {
        // subset R of teams that eliminates given team; null if not eliminated
        teamNameGuard(teamName);
        FlowNetwork flowNetwork = getFlowNetwork(teamName);
        FordFulkerson fulkerson = new FordFulkerson(flowNetwork, startVertexIndex, endVertexIndex);
        boolean isEliminated = areStartEdgesNotFullAndEndEdgesFull(flowNetwork.edges());
        if (isEliminated) {
            return getCertificate(teamName, fulkerson);
        }
        return null;
    }

    private Stack<String> getCertificate(String teamName, FordFulkerson fulkerson) {
        Stack<String> teamNames = new Stack<String>();
        int questionedTeamIndex = getTeamIndex(teamName);
        for(int i = firstTeamVertexIndex; i < endVertexIndex; i++) {
            if (fulkerson.inCut(i)) {
                int teamIndex = i - firstTeamVertexIndex;
                if (teamIndex >= questionedTeamIndex) {
                    teamIndex += 1;
                }
                Team team = teams[teamIndex];
                teamNames.push(team.name);
            }
        }
        return teamNames;
    }

    public boolean isEliminated(String teamName) {
        teamNameGuard(teamName);
        FlowNetwork flowNetwork = getFlowNetwork(teamName);
        new FordFulkerson(flowNetwork, startVertexIndex, endVertexIndex);
        return areStartEdgesNotFullAndEndEdgesFull(flowNetwork.edges());
    }

    private FlowNetwork getFlowNetwork(String questionedTeamName) {
        int questionedTeamIndex = getTeamIndex(questionedTeamName);
        Team questionedTeam = getTeam(questionedTeamName);
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

    private boolean areStartEdgesNotFullAndEndEdgesFull(Iterable<FlowEdge> edges) {
        boolean areStartEdgesFull = true;
        boolean areEndEdgesFull = true;

        for (Iterator<FlowEdge> iterator = edges.iterator(); iterator.hasNext();) {
            FlowEdge edge = iterator.next();
            int vertexIndex = edge.to();
            if (vertexIndex != startVertexIndex && vertexIndex != endVertexIndex) {
                vertexIndex = edge.other(vertexIndex);
            }

            //            System.out.println(String.format("from: %s to: %s, %f/%f", toTeamIndex(edge.from()), toTeamIndex(edge.to()), edge.flow(), edge.capacity()));

            if (vertexIndex == startVertexIndex) {
                if (edge.flow() < edge.capacity()) {
                    areStartEdgesFull = false;
                }
            }
            if (vertexIndex == endVertexIndex) {
                if (edge.flow() < edge.capacity()) {
                    areEndEdgesFull = false;
                }
            }
        }

        //        System.out.println(String.format("%b %b", areStartEdgesFull, areEndEdgesFull));

        return !areStartEdgesFull && areEndEdgesFull;
    }

    private String toTeamIndex(int index) {
        int questionedTeamIndex = 3;
        if (index == startVertexIndex) {
            return "start";
        }
        if (index == endVertexIndex) {
            return "end";
        }
        if (index < firstTeamVertexIndex) {
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
                    if (pairVertexIndex == index) {
                        return String.format("(%s vs %s)", teams[iTeamIndex].name, teams[jTeamIndex].name);
                    }
                }
            }
            return "Pair not found";

        } else {
            int idx = index - firstTeamVertexIndex;
            if (idx >= questionedTeamIndex) {
                idx += 1;
            }
            return teams[idx].name;
        }

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
            // TODO
            //            if (team.compareTo("Montreal") != 0) {
            //                continue;
            //            }

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
