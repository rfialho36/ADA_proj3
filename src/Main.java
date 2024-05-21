/**
 * chatgpt was used
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
        List<Integer>[] adj = new List[L + 1];
        for (int i = 0; i <= L; i++) {
            adj[i] = new ArrayList<>();
        }

        // Read the roads and build the graph
        for (int i = 0; i < R; i++) {
            tokenizer = new StringTokenizer(reader.readLine());
            int l1 = Integer.parseInt(tokenizer.nextToken());
            int l2 = Integer.parseInt(tokenizer.nextToken());
            capacity[l1][l2] = 1;
            capacity[l2][l1] = 1; // Since the roads are bidirectional
            adj[l1].add(l2);
            adj[l2].add(l1);
        }

        // Read the vault and destination locations
        tokenizer = new StringTokenizer(reader.readLine());
        int lv = Integer.parseInt(tokenizer.nextToken()); // Location of the vault
        int ld = Integer.parseInt(tokenizer.nextToken()); // Location of the destination

        // Use the Dinic algorithm to find the maximum number of disjoint paths
        Dinic dinic = new Dinic(L);
        for (int i = 1; i <= L; i++) {
            for (int j : adj[i]) {
                dinic.addEdge(i, j, capacity[i][j]);
            }
        }

        int maxPaths = dinic.maxFlow(lv, ld);

// Calculate the maximum possible number of stolen gold bars
        int maxGoldBars = Math.min(T, maxPaths) * B;

// Print the result
        System.out.println(maxGoldBars);
    }

    static class Dinic {
        int nodes;
        List<Edge>[] adj;
        int[] level;
        int[] ptr;
        int source;
        int sink;

        static class Edge {
            int v, rev, cap, flow;
            Edge(int v, int rev, int cap) {
                this.v = v;
                this.rev = rev;
                this.cap = cap;
                this.flow = 0;
            }
        }

        Dinic(int nodes) {
            this.nodes = nodes;
            adj = new List[nodes + 1];
            for (int i = 0; i <= nodes; i++) {
                adj[i] = new ArrayList<>();
            }
            level = new int[nodes + 1];
            ptr = new int[nodes + 1];
        }

        void addEdge(int u, int v, int cap) {
            adj[u].add(new Edge(v, adj[v].size(), cap));
            adj[v].add(new Edge(u, adj[u].size() - 1, 0)); // reverse edge for residual graph
        }

        boolean bfs() {
            Arrays.fill(level, -1);
            level[source] = 0;
            Queue<Integer> q = new LinkedList<>();
            q.add(source);
            while (!q.isEmpty()) {
                int u = q.poll();
                for (Edge e : adj[u]) {
                    if (level[e.v] < 0 && e.flow < e.cap) {
                        level[e.v] = level[u] + 1;
                        q.add(e.v);
                    }
                }
            }
            return level[sink] >= 0;
        }

        int dfs(int u, int flow) {
            if (u == sink) return flow;
            for (; ptr[u] < adj[u].size(); ptr[u]++) {
                Edge e = adj[u].get(ptr[u]);
                if (level[e.v] == level[u] + 1 && e.flow < e.cap) {
                    int df = dfs(e.v, Math.min(flow, e.cap - e.flow));
                    if (df > 0) {
                        e.flow += df;
                        adj[e.v].get(e.rev).flow -= df;
                        return df;
                    }
                }
            }
            return 0;
        }

        int maxFlow(int source, int sink) {
            this.source = source;
            this.sink = sink;
            int flow = 0;
            while (bfs()) {
                Arrays.fill(ptr, 0);
                while (true) {
                    int df = dfs(source, INF);
                    if (df == 0) break;
                    flow += df;
                }
            }
            return flow;
        }
    }
}
