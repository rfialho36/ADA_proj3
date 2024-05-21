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
        int maxPaths = dinic(capacity, lv, ld, L, T);

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

    private static int dinic(int[][] capacity, int source, int sink, int nodes, int thieves) {
        int[] level = new int[nodes + 1];
        int[] ptr = new int[nodes + 1];
        int[] queue = new int[nodes + 1];

        int maxFlow = 0;

        while (true) {
            Arrays.fill(level, -1);
            level[source] = 0;

            int head = 0, tail = 0;
            queue[tail++] = source;

            while (head < tail) {
                int u = queue[head++];
                for (int v = 1; v <= nodes; v++) {
                    if (level[v] == -1 && capacity[u][v] > 0) {
                        queue[tail++] = v;
                        level[v] = level[u] + 1;
                    }
                }
            }

            if (level[sink] == -1) {
                break;
            }

            Arrays.fill(ptr, 0);

            while (true) {
                int flow = dfs(source, sink, INF, ptr, level, capacity);
                if (flow == 0) {
                    break;
                }
                maxFlow += flow;
                if (maxFlow >= thieves) {
                    break;
                }
            }

            if (maxFlow >= thieves) {
                break;
            }
        }

        return maxFlow;
    }

    private static int dfs(int u, int sink, int flow, int[] ptr, int[] level, int[][] capacity) {
        if (u == sink || flow == 0) {
            return flow;
        }

        for (; ptr[u] <= level[u]; ptr[u]++) {
            int v = ptr[u];
            if (level[v] != level[u] + 1 || capacity[u][v] == 0) {
                continue;
            }

            int newFlow = dfs(v, sink, Math.min(flow, capacity[u][v]), ptr, level, capacity);

            if (newFlow > 0) {
                capacity[u][v] -= newFlow;
                capacity[v][u] += newFlow;
                return newFlow;
            }
        }

        return 0;
    }
}