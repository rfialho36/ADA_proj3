/**
 * @author Rafael Fialho (62945) r.fialho@campus.fct.unl.pt
 * @author Tomas Dias (63411) tfc.dias@campus.fct.unl.pt
 * <p>
 * Lincenciatura em Engenharia Informatica
 * Faculdade de Ciencias e Tecnologia da Universidade Nova de Lisboa
 * 2023/2024
 * <p>
 * This program solves the problem "Vault Burglary" for the ADA class from the 2nd semester of 2023/2024.
 * <p>
 * This is the Main class of the program. It reads the input, creates a VaultBurglarySolver object and prints the
 * result of the problem.
 * <p>
 * Copilot and chatGPT were used to help structuring the solutions logic, simplifying and documenting the code.
 */

// -------- IMPORTS --------
import java.io.*;
import java.util.StringTokenizer;

public class Main {

    /**
     * This is the main method of the program.
     * It reads the input, creates a VaultBurglarySolver object and prints the result of the problem.
     *
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

        // Read the input values (number of thieves, gold bars, locations and roads)
        int numThieves = Integer.parseInt(tokenizer.nextToken());
        int numGoldBars = Integer.parseInt(tokenizer.nextToken());
        int numLocations = Integer.parseInt(tokenizer.nextToken());
        int numRoads = Integer.parseInt(tokenizer.nextToken());

        VaultBurglarySolver solver = new VaultBurglarySolver(numThieves, numGoldBars, numLocations, numRoads);

        // Read the roads and build the graph
        for (int i = 0; i < numRoads; i++) {
            tokenizer = new StringTokenizer(reader.readLine());
            int loc1 = Integer.parseInt(tokenizer.nextToken());
            int loc2 = Integer.parseInt(tokenizer.nextToken());
            solver.addRoad(loc1, loc2);
        }

        // Read the vault and destination locations
        tokenizer = new StringTokenizer(reader.readLine());
        int vaultLocation = Integer.parseInt(tokenizer.nextToken());
        int destinationLocation = Integer.parseInt(tokenizer.nextToken());

        // Set the vault and destination locations
        solver.setVaultAndDestination(vaultLocation, destinationLocation);

        // Solve the problem and get the result (maximum amount of gold bars that can be stolen)
        int maxGoldBars = solver.solve();

        // Print the result
        System.out.println(maxGoldBars);
    }

}
