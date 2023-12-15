# Q-Learning Nim Game

**General Project Description**  
This repository contains my academic project for implementing the classic strategy game of Nim using Q-learning, a reinforcement learning technique. In Nim, two players alternately remove objects from one of three piles, and the goal is to avoid taking the last object. The project showcases the application of Q-learning to derive optimal strategies for both players in various game configurations. 

**Key Functions and Implementation** 


**Key Features**  
- **Constructor (`QLearningNimGame`)**
Initializes the game with the specified piles and sets up a HashMap to store Q-values, which represent the learned strategy.

- **`createQTable` Method**
This method simulates multiple games of Nim to generate Q-values. It involves generating random actions for each state, applying these actions to change the state of the game, and then calculating Q-values based on the results. The key aspect here is the use of a nested HashMap structure (`Map<String, Map<String, Double>> qValues`), where the outer map's key is the state, and the inner map contains action-Q-value pairs.

- **State-Action Pair Optimization**
To address the challenge of making Q-learning more efficient, the program implements a state merging technique. It treats equivalent states (e.g., 1-2-1, 1-1-2, 2-1-1) as the same by sorting the pile sizes in ascending order. This sorting ensures that different permutations of the same pile configuration are treated as a single state, significantly reducing the number of unique states to learn, thereby speeding up the learning process.

- **`Sort` Method**
A utility function to sort the pile sizes within the state string. It's crucial for the state merging technique, ensuring consistency in representing states.

- **`policy` Method**
This method determines the optimal move for the current player based on the learned Q-values. It uses a sorted version of the current state to look up the Q-values and then chooses the action that either maximizes or minimizes the value depending on whether it's player A or B's turn.

- **`playGame` Method**
Facilitates the actual gameplay between a user and the AI. It alternates turns between the player and the computer, using the policy method to select moves for the computer.

**Challenge Problem Solution**
For this project, my professor presented a challenge to enhance the efficiency of the Q-learning algorithm in the game of Nim. I rose to this challenge by innovatively implementing a canonical representation of states, paired with a sophisticated data structure - a map of maps - to facilitate this process.

The key to this approach lies in sorting the piles in ascending order before storing or retrieving Q-values. This sorting transforms different permutations of the same piles into a single, canonical state. For example, variations like 1-2-1, 1-1-2, and 2-1-1, though distinct in arrangement, essentially represent the same strategic situation. By treating these permutations as one after sorting, the algorithm consolidates the state space, greatly reducing its complexity.

To efficiently manage these states and their corresponding Q-values, I utilized a nested HashMap structure. The outer map's key represents the sorted state, while the inner map holds pairs of actions and their Q-values. This map of maps structure is not only crucial for organizing the data effectively but also ensures quick access and updates to the Q-values, enhancing the algorithm's overall performance.

This optimization leverages the symmetry inherent in Nim, allowing the Q-learning algorithm to generalize across similar states, thereby reducing redundancy. The result is a more streamlined and efficient learning process, which not only accelerates the algorithm but also significantly reduces the memory footprint of the Q-table. The implementation of the map of maps, in conjunction with the state sorting, demonstrates a deep understanding of both the technical intricacies of Q-learning and the strategic dynamics of Nim. It reflects a sophisticated approach to algorithm optimization, ensuring enhanced scalability and performance.



**Technical Details**  
- **Languages and Technologies:** Java.  
- **Concepts Used:** Q-learning, reinforcement learning, game theory.  

**Running the Program**  
- **Initialize Piles:** Input the starting number of objects in each pile.  
- **Simulate Games:** Specify the number of games for Q-learning simulation.  
- **Observe Learning:** The program displays the learned Q-values, representing the AI's understanding of the game.  
- **Engage in Gameplay:** Choose to play as Player A or B against the AI.  
- **Continue Playing or Exit:** After each game, you can opt to play another round or exit the program.

**Learning and Strategy**  
- **Q-learning Parameters:** γ=0.9 (discount factor) and α=1 (learning rate).  
- **Reward System:** Winning moves for Player A are rewarded with 1000 points, and for Player B, -1000 points.  
- **State-Action Representation:** Each state-action pair is encoded as a string for efficient Q-value mapping.

**Educational Value**  
- **AI and Game Theory:** This project serves as a practical application of AI concepts in a controlled, rule-based environment.  
- **Problem-Solving Skills:** Demonstrates the use of algorithms to solve and optimize strategies in a competitive scenario.  
- **Code Efficiency and Optimization:** Highlights the importance of efficient data structuring and algorithmic thinking.

**Project Context**  
This project was completed as part of my COMP 372 course at Rhodes College. It showcases my skills in artificial intelligence, particularly in reinforcement learning and its application in game theory.
