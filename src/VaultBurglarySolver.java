import java.util.*;

public class VaultBurglarySolver {
  private static final int INF = Integer.MAX_VALUE;

  private int[][] capacity;
  private List<List<Integer>> adj;
  private int numNodes;

  public VaultBurglarySolver(int numNodes) {
    this.numNodes = numNodes;
    this.capacity = new int[2 * numNodes + 2][2 * numNodes + 2];
    this.adj = new ArrayList<>();
    for (int i = 0; i <= 2 * numNodes + 1; i++) {
      adj.add(new ArrayList<>());
    }
  }

  public void addRoad(int loc1, int loc2) {
    int uOut = 2 * loc1 + 1;
    int vIn = 2 * loc2;
    int vOut = 2 * loc2 + 1;
    int uIn = 2 * loc1;

    capacity[uOut][vIn] = 1; // Edge from loc1 out to loc2 in
    capacity[vOut][uIn] = 1; // Edge from loc2 out to loc1 in

    adj.get(uOut).add(vIn);
    adj.get(vOut).add(uIn);
  }

  public void assignCapacities(int numLocations) {
    for (int i = 1; i <= numLocations; i++) {
      capacity[2 * i][2 * i + 1] = INF; // Edge from in-vertex to out-vertex
      adj.get(2 * i).add(2 * i + 1);
    }
  }

  public void setVaultAndDestination(int vaultLoc, int destLoc) {
    // Vault and destination vertices should have infinite capacity
    capacity[2 * vaultLoc][2 * vaultLoc + 1] = INF;
    capacity[2 * destLoc][2 * destLoc + 1] = INF;
  }

  public int solve(int source, int sink, int thieves) {
    int[] level = new int[2 * numNodes + 2];
    int[] ptr = new int[2 * numNodes + 2];
    int maxFlow = 0;

    while (bfs(source, sink, level)) {
      Arrays.fill(ptr, 0);
      int flow;
      while ((flow = dfs(level, ptr, source, sink, INF)) > 0) {
        maxFlow += flow;
        if (maxFlow >= thieves) {
          return maxFlow;
        }
      }
    }
    return maxFlow;
  }

  private boolean bfs(int source, int sink, int[] level) {
    Arrays.fill(level, -1);
    level[source] = 0;
    Queue<Integer> queue = new LinkedList<>();
    queue.add(source);

    while (!queue.isEmpty()) {
      int u = queue.poll();

      for (int v : adj.get(u)) {
        if (level[v] < 0 && capacity[u][v] > 0) {
          level[v] = level[u] + 1;
          queue.add(v);
        }
      }
    }
    return level[sink] >= 0;
  }

  private int dfs(int[] level, int[] ptr, int u, int sink, int flow) {
    if (u == sink) {
      return flow;
    }
    for (; ptr[u] < adj.get(u).size(); ptr[u]++) {
      int v = adj.get(u).get(ptr[u]);
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
