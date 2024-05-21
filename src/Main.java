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

        // Use the Dinic's algorithm to find the maximum number of disjoint paths
        int maxPaths = dinic(capacity, lv, ld, L, T);

        // Calculate the maximum possible number of stolen gold bars
        int maxGoldBars = maxPaths * B;

        // Print the result
        System.out.println(maxGoldBars);
    }

    private static boolean bfs(int[][] capacity, int source, int sink, int[] level, int nodes) {
        Arrays.fill(level, -1);
        level[source] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 1; v <= nodes; v++) {
                if (level[v] < 0 && capacity[u][v] > 0) {
                    level[v] = level[u] + 1;
                    queue.add(v);
                }
            }
        }
        return level[sink] >= 0;
    }

    private static int dfs(int[][] capacity, int[] level, int[] ptr, int u, int sink, int flow) {
        if (u == sink) {
            return flow;
        }
        for (; ptr[u] <= level[u]; ptr[u]++) {
            int v = ptr[u];
            if (level[v] == level[u] + 1 && capacity[u][v] > 0) {
                int newFlow = Math.min(flow, capacity[u][v]);
                int pushed = dfs(capacity, level, ptr, v, sink, newFlow);
                if (pushed > 0) {
                    capacity[u][v] -= pushed;
                    capacity[v][u] += pushed;
                    return pushed;
                }
            }
        }
        return 0;
    }

    private static int dinic(int[][] capacity, int source, int sink, int nodes, int thieves) {
        int[] level = new int[nodes + 1];
        int[] ptr = new int[nodes + 1];
        int maxFlow = 0;

        while (bfs(capacity, source, sink, level, nodes)) {
            Arrays.fill(ptr, 1);
            int flow;
            while ((flow = dfs(capacity, level, ptr, source, sink, INF)) > 0) {
                maxFlow += flow;
                if (maxFlow >= thieves) {
                    return maxFlow;
                }
            }
        }
        return maxFlow;
    }
}
