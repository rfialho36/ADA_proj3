import java.io.*;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

            int numThieves = Integer.parseInt(tokenizer.nextToken());
            int numGoldBars = Integer.parseInt(tokenizer.nextToken());
            int numLocations = Integer.parseInt(tokenizer.nextToken());
            int numRoads = Integer.parseInt(tokenizer.nextToken());

            VaultBurglarySolver solver = new VaultBurglarySolver(numLocations);

            // Read the roads and build the graph
            for (int i = 0; i < numRoads; i++) {
                tokenizer = new StringTokenizer(reader.readLine());
                int loc1 = Integer.parseInt(tokenizer.nextToken());
                int loc2 = Integer.parseInt(tokenizer.nextToken());
                solver.addRoad(loc1, loc2);
            }

            solver.assignCapacities(numLocations);

            // Read the vault and destination locations
            tokenizer = new StringTokenizer(reader.readLine());
            int vaultLocation = Integer.parseInt(tokenizer.nextToken());
            int destinationLocation = Integer.parseInt(tokenizer.nextToken());

            solver.setVaultAndDestination(vaultLocation, destinationLocation);

            // Use the Dinic's algorithm to find the maximum flow
            int maxFlow = solver.solve(2 * vaultLocation, 2 * destinationLocation + 1, numThieves);

            // Calculate the maximum possible number of stolen gold bars
            int maxGoldBars = Math.min(numThieves, maxFlow) * numGoldBars;

            // Print the result
            System.out.println(maxGoldBars);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
