/**
 * foi usado chatgpt
 */

import java.io.*;
import java.util.*;

public class Main {
    static final int INF = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

        int T = Integer.parseInt(tokenizer.nextToken()); // Number of thieves (not used directly)
        int B = Integer.parseInt(tokenizer.nextToken()); // Number of gold bars in the seized bag
        int L = Integer.parseInt(tokenizer.nextToken()); // Number of locations
        int R = Integer.parseInt(tokenizer.nextToken()); // Number of roads

        // Initialize the capacity graph
        int[][] capacity = new int[L + 1][L + 1];

        // Read the roads and build the graph
        for (int i = 0; i < R; i++) {
            tokenizer = new StringTokenizer(reader.readLine());
            int l1 = Integer.parseInt(tokenizer.nextToken());
            int l2 = Integer.parseInt(tokenizer.nextToken());
            capacity[l1][l2] = 1;
            capacity[l2][l1] = 1; // Since the roads are bidirectional
        }

        // Read the vault and destination locations
        tokenizer = new StringTokenizer(reader.readLine());
        int lv = Integer.parseInt(tokenizer.nextToken()); // Location of the vault
        int ld = Integer.parseInt(tokenizer.nextToken()); // Location of the destination

        // Use the Edmonds-Karp algorithm to find the maximum number of disjoint paths
        int maxPaths = edmondsKarp(capacity, lv, ld, L, T);

        // Calculate the maximum possible number of stolen gold bars
        int maxGoldBars = maxPaths * B;

        // Print the result
        System.out.println(maxGoldBars);
    }

    // BFS function to find if there is an augmenting path
    private static boolean bfs(int[][] capacity, int source, int sink, int[] parent, int nodes) {
        boolean[] visited = new boolean[nodes + 1];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 1; v <= nodes; v++) {
                if (!visited[v] && capacity[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;

                    if (v == sink)
                        return true;
                }
            }
        }
        return false;
    }

    // Edmonds-Karp algorithm to find the maximum flow

    private static int edmondsKarp(int[][] capacity, int source, int sink, int nodes, int thieves) {
        int u, v;
        int[] parent = new int[nodes + 1];
        int maxFlow = 0;

        // Augment the flow while there is a path from source to sink
        while (maxFlow < thieves && bfs(capacity, source, sink, parent, nodes)) {
            // Find the maximum flow through the path found.
            int pathFlow = INF;
            for (v = sink; v != source; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, capacity[u][v]);
            }

            // update capacities of the edges and reverse edges along the path
            for (v = sink; v != source; v = parent[v]) {
                u = parent[v];
                capacity[u][v] -= pathFlow;
                capacity[v][u] += pathFlow;
            }

            // Add path flow to overall flow
            maxFlow += pathFlow;
        }

        return maxFlow;
    }
}
