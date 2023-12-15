//I have neither given nor received unauthorized aid on this program
import java.util.*;

public class QLearningNimGame {
    private static final double GAMMA = 0.9;
    private static int [] piles;
    private Map<String, Map<String, Double>> qValues;
    private static int games_simed;
    private static int playAgain = 1;
    public QLearningNimGame(int[] piles) {
        this.piles = piles;
        this.qValues = new HashMap<>();
    }

    public static void main (String [] args){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter how many sticks should be in the first pile: ");
        int pile1Num = scanner.nextInt();
        System.out.print("Enter how many sticks should be in the second pile: ");
        int pile2Num = scanner.nextInt();
        System.out.print("Enter how many sticks should be in the third pile: ");
        int pile3Num = scanner.nextInt();
        System.out.print("Enter the number of games of Nim you want to be simulated: ");
        games_simed = scanner.nextInt();
        System.out.println("Initial board is " + pile1Num + "-" + pile2Num + "-" + pile3Num + ", simulating " + games_simed + " games.\n");

        // initializing the piles
        piles = new int[3];
        piles[0] = pile1Num;
        piles[1] = pile2Num;
        piles[2] = pile3Num;

        QLearningNimGame game = new QLearningNimGame(piles);

        System.out.println("Final Q-values:\n");
        game.createQTable();

        // while loop for replaying functionality
        while(playAgain == 1) {
            game.playGame(piles);
            System.out.print("Play again? (1) Yes (2) No: ");
            playAgain = scanner.nextInt();
            //resets the piles each new game
            piles = new int[3];
            piles[0] = pile1Num;
            piles[1] = pile2Num;
            piles[2] = pile3Num;
        }
    }

    private void createQTable() {
        String gameInfo = "";
        int player = 1;
        int[] pileTemp = new int[3];

        Random rand = new Random();
        // loop to simulate multiple games
        for (int gameNum = 0; gameNum < games_simed; gameNum++) {
            for (int i = 0; i < piles.length; i++) {
                pileTemp[i] = piles[i];
            }
            ArrayList<String> states = new ArrayList<>();
            // plays the game until all piles are empty
            while (pileTemp[0] > 0 || pileTemp[1] > 0 || pileTemp[2] > 0) {
                // sort state before action was computed because of the new format
                Arrays.sort(pileTemp);
                gameInfo = "";
                int randPileNum = rand.nextInt(3);
                // Selects a non-empty pile randomly
                while (pileTemp[randPileNum] == 0) {
                    randPileNum = rand.nextInt(3);
                }
                int randNumElementsRemoved = rand.nextInt(pileTemp[randPileNum]) + 1;
                // adds A or B to the string depending on which players turn it is and changes the player for the next turn
                if (player == 1) {
                    gameInfo += "A";
                    player += 1;
                } else if (player == 2) {
                    gameInfo += "B";
                    player -= 1;
                }

                // puts the state info into gameInfo
                for (int j = 0; j < pileTemp.length; j++) {
                    gameInfo += String.valueOf(pileTemp[j]);
                }
                // action of pile picked
                gameInfo += String.valueOf(randPileNum);
                // action of removing a stick from pile
                gameInfo += String.valueOf(randNumElementsRemoved);
                //Updates the pile after the action
                pileTemp[randPileNum] -= randNumElementsRemoved;
                states.add(gameInfo);
            }
            double score;
            if (player == 1) {
                score = 1000;
            } else {
                score = -1000;
            }
            String state = Sort(gameInfo.substring(0, 4));
            String action = gameInfo.substring(4, 6);
            if (!qValues.containsKey(state)) {
                qValues.put(state, new HashMap<>());
            }
            qValues.get(state).put(action, score);
            // backtracks and assigns values to state action pairs
            for (int k = states.size() - 1; k > 0; k--) {
                String s = Sort(states.get(k).substring(0, 4));
                Double MIN_VALUE = null;
                Double MAX_VALUE = null;
                Map<String, Double> temp = qValues.get(s);
                // Find the value of the current state by finding the best action from the current state
                for (Map.Entry<String, Double> entry : temp.entrySet()) {
                    Double value = entry.getValue();
                    if (MIN_VALUE == null || MIN_VALUE > value) {
                        MIN_VALUE = value;
                    }
                    if (MAX_VALUE == null || MAX_VALUE < value) {
                        MAX_VALUE = value;
                    }

                }
                state = Sort(states.get(k - 1).substring(0, 4));
                action = states.get(k - 1).substring(4, 6);
                // Update the Q-value of the previous state-action pair
                if (state.charAt(0) == 'A') {
                    if (MIN_VALUE != null) {
                        if (!qValues.containsKey(state)) {
                            qValues.put(state, new HashMap<>());
                        }
                        qValues.get(state).put(action, MIN_VALUE * GAMMA);
                    }
                } else {
                    if (MAX_VALUE != null) {
                        if (!qValues.containsKey(state)) {
                            qValues.put(state, new HashMap<>());
                        }
                        qValues.get(state).put(action, MAX_VALUE * GAMMA);
                    }
                }
            }
        }

        // puts the keys into a list for sorting
        List<String> sortedKeys = new ArrayList<>(qValues.keySet());
        // sorts the list from least to greatest for output
        Collections.sort(sortedKeys);

        // prints out the Q-values
        for (int i = 0; i < sortedKeys.size(); i++) {
            String state = sortedKeys.get(i);
            Map<String, Double> actionMap = qValues.get(state);

            List<String> actions = new ArrayList<>(actionMap.keySet());
            for (int j = 0; j < actions.size(); j++) {
                String action = actions.get(j);
                System.out.print("Q[" + state + ", " + action + "] = ");
                System.out.println(actionMap.get(action));
            }
        }
    }

    // Helper method to sort a string representing a state
    public String Sort(String state){
        char arr [] = state.substring(1,4).toCharArray(); // don't sort the player index A or B
        Arrays.sort(arr);
        state = state.substring(0,1) + new String(arr); // add the player index
        return state;
    }

    public String policy(String state){
        Double MIN_VALUE = null;
        Double MAX_VALUE = null;
        String minAction = "";
        String maxAction = "";
        String s = Sort(state.substring(0,4)); // sort the current state for consistency in finding Q-Values

        // Find the min and max action for the current state
        // Iterate over each entry (action and value) in the map.
        for(Map.Entry<String, Double> entry : qValues.get(s).entrySet()){
            String key = entry.getKey(); // get the action
            Double value = entry.getValue(); // get the value for this action

            // Update minAction and min value if a lower value is found.
            if(MIN_VALUE == null || MIN_VALUE > value){
                MIN_VALUE = value;
                minAction = key;
            }
            // Update maxAction and max value if a higher value is found.
            if(MAX_VALUE == null || MAX_VALUE < value){
                MAX_VALUE = value;
                maxAction = key;
            }
        }

        // gets the best (max or min) action based on if it's player A's or B's turn respectively
        if(state.charAt(0) == 'A'){
            char pileSize = s.charAt(Character.getNumericValue(maxAction.charAt(0)) + 1);
            int idx = state.indexOf(pileSize) - 1; // Find the original pile index before sorting.
            return "" + idx + maxAction.substring(1,2); // Return the pile index and the number of sticks to remove.
        }
        else{
            char pileSize = s.charAt(Character.getNumericValue(minAction.charAt(0)) + 1);
            int idx = state.indexOf(pileSize) - 1; // Find the original pile index before sorting.
            return "" + idx + minAction.substring(1,2); // Return the pile index and the number of sticks to remove.
        }
    }

    public void playGame(int piles []){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Who moves first, (1) User or (2) Computer? ");
        int firstMove = scanner.nextInt();
        System.out.println();
        char compPlayer;
        char humanPlayer;
        if(firstMove == 1){
            humanPlayer = 'A';
            compPlayer = 'B';
        }
        else{
            compPlayer = 'A';
            humanPlayer = 'B';
        }
        // Play until all piles are empty
        while (piles[0] > 0 || piles[1] > 0 || piles[2] > 0) {
            if(firstMove == 1){
                System.out.println("Player " + humanPlayer + " (user)'s turn; board is " + "(" + piles[0] + ", " + piles[1] + ", " + piles[2] + ").");
                int userPile = -1;
                int userStickNum = 0;
                boolean validInput = false;

                while(!validInput){
                    System.out.print("What pile? ");
                    userPile = scanner.nextInt();
                    if (userPile >= 0 && userPile < piles.length) {
                        System.out.print("How many? ");
                        userStickNum = scanner.nextInt();
                        if (userStickNum > 0 && userStickNum <= piles[userPile]) {
                            validInput = true;
                        } else {
                            System.out.println("Not a valid input. Please enter a valid number of sticks.");
                        }
                    } else {
                        System.out.println("Not a valid pile. Please enter a valid pile number.");
                    }
                }
                piles[userPile] -= userStickNum;
                System.out.println();
            }
            else if(firstMove == 2){
                System.out.println("Player " + compPlayer + " (computer)'s turn; board is " + "(" + piles[0] + ", " + piles[1] + ", " + piles[2] + ").");
                String pol = policy(String.valueOf(compPlayer) + piles[0] + piles[1] + piles[2]);
                char pileNum = pol.charAt(0);
                char compStickNum = pol.charAt(1);
                System.out.println("Computer chooses pile " + pileNum + " and removes " + compStickNum);
                System.out.println();
                piles[Character.getNumericValue(pileNum)] -= Character.getNumericValue(compStickNum);
            }
            // Switching turns
            if(firstMove == 1){
                firstMove += 1;
            }
            else if(firstMove == 2){
                firstMove -= 1;
            }
        }
        System.out.println("Game over.");
        if(firstMove == 1){
            System.out.println("Winner is " + humanPlayer + " (user).\n");
        }
        else{
            System.out.println("Winner is " + compPlayer + " (computer).\n");
        }
    }
}