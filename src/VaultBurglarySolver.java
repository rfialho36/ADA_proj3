/**
 * @author Rafael Fialho (62945) r.fialho@campus.fct.unl.pt
 * @author Tomas Dias (63411) tfc.dias@campus.fct.unl.pt
 * <p>
 * This class solves the problem "Vault Burglary".
 * It's responsible for building the graph and finding the maximum amount of gold bars that can be stolen.
 * <p>
 * Copilot and chatGPT were used to help structuring the solutions logic, simplifying and documenting the code.
 */

// -------- IMPORTS --------
import java.util.*;

public class VaultBurglarySolver {

    // -------- CONSTANTS --------
    private static final int INF = Integer.MAX_VALUE;

    // -------- VARIABLES --------
    private int[][] capacity;
    private List<Integer>[] adj;
    private int numThieves, numGoldBars, numLocations;
    private int vaultLocation, destinationLocation;

    // -------- CONSTRUCTOR --------

    /**
     * Constructor of the VaultBurglarySolver class.
     * It initializes the variables of the solver.
     *
     * @param numThieves   The number of thieves.
     * @param numGoldBars  The number of gold bars.
     * @param numLocations The number of locations.
     * @param numRoads     The number of roads.
     */
    @SuppressWarnings("unchecked")
    public VaultBurglarySolver(int numThieves, int numGoldBars, int numLocations, int numRoads) {
        this.numThieves = numThieves;
        this.numGoldBars = numGoldBars;
        this.numLocations = numLocations;
        this.capacity = new int[2 * numLocations + 2][2 * numLocations + 2];
        this.adj = new ArrayList[2 * numLocations + 2];
        for (int i = 0; i < adj.length; i++) {
            adj[i] = new ArrayList<>();
        }
        this.vaultLocation = -1;
        this.destinationLocation = -1;
        initializeCapacities(numLocations);
    }

    /**
     * Initialize capacities and adjacency list for each location.
     * Each location has a capacity of 1 because only one group can pass through it.
     *
     * @param numLocations The number of locations.
     */
    private void initializeCapacities(int numLocations) {
        for (int i = 1; i <= numLocations; i++) {
            addEdge(2 * i, 2 * i + 1, 1); // Edge from in-vertex to out-vertex
        }
    }

    /**
     * Adds a road to the solver.
     * It adds the road to the graph.
     *
     * @param location1 The first location.
     * @param location2 The second location.
     */
    public void addRoad(int location1, int location2) {
        addEdge(2 * location1 + 1, 2 * location2, 1); // From location1 out to location2 in
        addEdge(2 * location2 + 1, 2 * location1, 1); // From location2 out to location1 in
    }

    /**
     * Helper method to add an edge to the graph.
     */
    private void addEdge(int from, int to, int cap) {
        capacity[from][to] = cap;
        adj[from].add(to);
        adj[to].add(from); // Add reverse path for residual graph
    }

    /**
     * Sets the vault and destination locations.
     * It sets the vault and destination locations and assigns infinite capacity to them because
     * all the thieves can (and must) pass through them.
     *
     * @param vaultLoc The vault location.
     * @param destLoc  The destination location.
     */
    public void setVaultAndDestination(int vaultLoc, int destLoc) {
        this.vaultLocation = vaultLoc;
        this.destinationLocation = destLoc;
        addEdge(2 * vaultLoc, 2 * vaultLoc + 1, INF); // Infinite capacity for vault
        addEdge(2 * destLoc, 2 * destLoc + 1, INF); // Infinite capacity for destination
    }

    /**
     * Solves the problem.
     * It uses Dinic's algorithm to find the maximum flow in the graph.
     * The maximum flow represents the maximum amount of gold bars that can be stolen.
     * If the maximum flow is greater than or equal to the number of thieves, the result is
     * the number of thieves times the number of gold bars.
     *
     * @return The maximum amount of gold bars that can be stolen.
     */
    public int solve() {
        int source = 2 * vaultLocation;
        int sink = 2 * destinationLocation + 1;
        int[] level = new int[2 * numLocations + 2];
        int[] ptr = new int[2 * numLocations + 2];
        int maxFlow = 0;

        while (bfs(source, sink, level)) {
            Arrays.fill(ptr, 0);
            int flow;
            while ((flow = dfs(level, ptr, source, sink, INF)) > 0) {
                maxFlow += flow;
                if (maxFlow >= numThieves) {
                    return maxFlow * numGoldBars;
                }
            }
        }
        return Math.min(numThieves, maxFlow) * numGoldBars;
    }

    /**
     * Breadth-first search algorithm.
     * It finds the shortest path from the source to the sink.
     *
     * @param source The source vertex.
     * @param sink   The sink vertex.
     * @param level  The level of each vertex.
     * @return True if there is a path from the source to the sink, false otherwise.
     */
    private boolean bfs(int source, int sink, int[] level) {
        Arrays.fill(level, -1);
        level[source] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v : adj[u]) {
                if (level[v] < 0 && capacity[u][v] > 0) {
                    level[v] = level[u] + 1;
                    queue.add(v);
                }
            }
        }
        return level[sink] >= 0;
    }

    /**
     * Depth-first search algorithm.
     * It finds the maximum flow from the source to the sink.
     *
     * @param level The level of each vertex.
     * @param ptr   The pointer of each vertex.
     * @param u     The current vertex.
     * @param sink  The sink vertex.
     * @param flow  The current flow.
     * @return The maximum flow from the source to the sink.
     */
    private int dfs(int[] level, int[] ptr, int u, int sink, int flow) {
        if (u == sink) {
            return flow;
        }
        for (; ptr[u] < adj[u].size(); ptr[u]++) {
            int v = adj[u].get(ptr[u]);
            if (level[v] == level[u] + 1 && capacity[u][v] > 0) {
                int newFlow = Math.min(flow, capacity[u][v]);
                int pushed = dfs(level, ptr, v, sink, newFlow);
                if (pushed > 0) {
                    capacity[u][v] -= pushed;
                    capacity[v][u] += pushed;
                    return pushed;
                }
            }
        }
        return 0;
    }
}
