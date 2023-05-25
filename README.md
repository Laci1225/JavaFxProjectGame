# [JavaFX](https://openjfx.io/) Project Game


#### This repository contains the source code for a game application with a graphical user interface (GUI).
The game is a turn-based strategy game where players take turns moving their game pieces on a grid board.
The goal of the game is to reach a target state before the opponent.

## Features

- Graphical user interface implemented using JavaFX.
- Turn-based gameplay.
- Interactive grid board where players can select and move game pieces.
- Visual representation of game pieces using circles of different colors.
- Target state detection to determine the winner of the game.
- Leaderboard functionality to track and display game statistics.
- Serialization of leaderboard data to a JSON file.

## Prerequisites

To run the game application, make sure you have the following installed:

- Java Development Kit (JDK) 8 or later
- JavaFX SDK

## Running the Game

1. Clone this repository to your local machine or download the source code as a ZIP file.
2. Open the project in your preferred Java IDE.
3. Set up the JavaFX SDK in your IDE if it's not already configured.
4. Build and run the `Main` class to start the game application.

## Gameplay Instructions

1. Upon launching the game, you will see the game board displayed as a grid.
2. The game pieces are represented by circles, with each player having a different color (blue or red).
3. Players take turns selecting their own game piece by clicking on it.
4. Once a game piece is selected, it can be moved to an adjacent empty cell on the grid.
5. The available movement options will be highlighted on the grid as rectangles.
6. To move a selected game piece, click on the desired destination rectangle.
7. The game continues until one of the players reaches the target state, triggering the end of the game.
8. The winner will be displayed in an alert dialog, and their information will be added to the leaderboard.
9. The leaderboard can be found in the `leaderboard.json` file, which stores the winners' names, steps, and timestamps.

## Modifying the Game

If you wish to modify the game or add new features, you can make changes to the existing code. Some possible enhancements include:

- Adding additional gameplay mechanics, for example obstacles.
- Implementing different board layouts or grid sizes.
- Enhancing the user interface with additional graphical elements.
- Extending the leaderboard functionality to include more statistics.

## Contributing

Contributions to this game application are welcome. If you have any suggestions, bug fixes, or feature enhancements, please feel free to submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE). Feel free to use and modify the code according to the terms of the license.

![#gameLayout.png](src/main/resources/gameLayout.png 'gameLayout.png')
