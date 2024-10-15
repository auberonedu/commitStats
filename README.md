# commitStats (Alston and Diana)

A program written in poor style to help give practice with the VS Code debugger and refactoring.

## Setup and Exploration
1. Have one partner fork the repository and add the other partner as a collaborator.
1. Have both partners clone that fork, NOT the base repository.
1. Open the repository in VS Code. Make sure you open the repository root, not the directory above. It should say COMMITSTATS directly under EXPLORER when the left file explorer is open.
1. Open `src/Main.java`.
1. Run the file by clicking the grey Run button over `main` (this sometimes takes a while to pop up when you first open the file and VS Code extensions start up).
1. When prompted for a data file, use `data/commit_data.csv`.
1. Try using the program and discuss with your partner what information it gives the user. Do not worry about reading the code yet.
1. Open `data/commit_data.csv` in VS Code. Look through it and discuss with your partner what each of the 3 fields represents.

## Wave 1
1. Take an initial scan over the code. Discuss with your partner broadly what it looks like each part is doing. Add comments to general blocks of code giving broad descriptions. For example, `// This block of code reads data from the CSV`.
1. Look at the `mp1` variable. Strategically choose a position to set a breakpoint that will allow you to look in `mp1` and see the structure of what it contains in an iteration of a loop after a few key/value pairs have been put into it. Click the grey Debug button above `main` and use the debugger to look inside the variable. Discuss with your partner what you find.
1. Decide with your partner what a better variable name for `mp1` would be. Have one partner right click on the variable and choose "Rename symbol" to refactor the code. Save the file, and re-run it to make sure it still works. Fix any bugs if any are introduced.
1. Do the same for `dta`.
1. Add, commit, and push your code, pulling where appropriate.

## Wave 2
1. Extract the file parsing logic into a helper method that has the following signature:
```
public static List<Map<String, String>> parseCSV(String filename)
```
1. Run the code and verify that it still works.

## Wave 3 (Optional, but strongly recommended)
1. Implement `testParseCSV` in `MainTest.java`. There are two `TODO` comments that explain what needs to be added
    > We generally try to avoid reading from an actual file when doing unit tests because reading from a file can be slow. We're doing so here just for the sake of simplicity. Optional: Can you come up with a way to test this functionality without reading from a file? You may need to change the method signature of parseCSV or add an additional helper method that you test instead. Consider researching online or looking at how the tests in `ramblebot` handled a similar problem.
1. Add, commit, and push your code, pulling where appropriate.

## Wave 4 (Optional)
1. Refactor the rest of the program. Give better variable names, add intermediate variables, make helper methods, create tests, and consider making additional classes like `Commit` or `Fork`.

## Submission
Please make a PR against the original repository. Copy the link to the PR and submit it in Canvas. Each partner should submit in Canvas.
