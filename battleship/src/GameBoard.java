import java.util.Random;

// A Battleship! game board

class GameBoard
{
    public int height;
    public int weight;
    public final static char ShipMask      = 0x07;

    public final static char isEmpty       = 0x00; // location is empty
    public final static char hasCarrier    = 0x01; // location contains the carrier
    public final static char hasBattleship = 0x02; // location contains the battleship
    public final static char hasCruiser    = 0x03; // location contains the cruiser
    public final static char hasSubmarine  = 0x04; // location contains the submarine
    public final static char hasDestroyer  = 0x05; // location contains the destroyer

    public final static char GuessMask     = 0x18;

    public final static char noGuess       = 0x00; // location has not been guessed yet
    public final static char isHit         = 0x08; // guess is a hit
    public final static char isMiss        = 0x10; // guess is a miss
    public final static char isSunk        = 0x18; // guess is sunk

    public char board[][]; // the game board itself

    public Carrier carrier;        // the carrier
    public Battleship battleship;  // the battleship
    public Cruiser cruiser;        // the cruiser
    public Submarine submarine;    // the submarine
    public Destroyer destroyer;    // the destroyer

    public int number_carrier;     // number of the carrier
    public int number_battleship;  // number of the battleship
    public int number_cruiser;     // number of the cruiser
    public int number_destroyer;   // number of the destroyer
    public int number_submarine;   // number of the submarine

    // Create a new game board

    GameBoard(int num1, int num2, int num3, int num4, int num5, int h, int w)
    {
        height = h;
        weight = w;
        number_carrier = num1;
        number_battleship = num2;
        number_cruiser = num3;
        number_destroyer = num4;
        number_submarine = num5;

        board = new char[10][10];

        // initialize the board to the empty state

        for (int x = 0; x < 10; x++)
            for (int y = 0; y < 10; y++)
                board[x][y] = 0;

        // select random positions for the ships

        Random rand = new Random();

        // select orientation and position for the carrier

        for (int i = 0; i < number_carrier; i++){
            Select_position_carrier(rand);
        }

        // select orientation and position for the battleship

        for (int i = 0; i < number_battleship; i++)
            Select_position_battleship(rand);

        // select orientation and position for the cruiser

        for (int i = 0; i < number_cruiser; i++)
            Select_position_cruiser(rand);

        // select orientation and position for the destroyer

        for (int i = 0; i < number_destroyer; i++)
            Select_position_destroyer(rand);

        // select orientation and position for the submarine

        for (int i = 0; i < number_submarine; i++)
            Select_position_submarine(rand);

        // initialize game statistics

        numberSunk = 0;
        numberOfHits = 0;
        numberOfGuesses = 0;
        duplicateGuesses = 0;
    }

    private void Select_position_submarine(Random rand) {
        int initX;
        int initY;
        do
        {
            initX = Math.abs(rand.nextInt() % (height));
            initY = Math.abs(rand.nextInt() % (weight));
        }
        while (board[initX][initY] != 0);
        board[initX][initY] = hasSubmarine;
        submarine = new Submarine(initX, initY);
    }

    private void Select_position_destroyer(Random rand) {
        int initX;
        boolean isVertical;
        int initY;
        do
        {
            isVertical = (rand.nextInt() % 2 != 0);
            initX = Math.abs(rand.nextInt() % (isVertical ? height : height/2 + 4));
            initY = Math.abs(rand.nextInt() % (isVertical ? weight/2 + 4 : weight));
        }
        while ((board[initX][initY] != 0) ||
                (isVertical && (board[initX][initY+1] != 0)) || (!isVertical && (board[initX+1][initY] != 0)));
        if (isVertical)
            board[initX][initY] = board[initX][initY+1] = hasDestroyer;
        else
            board[initX][initY] = board[initX+1][initY] = hasDestroyer;
        destroyer = new Destroyer(initX, initY, isVertical);
    }

    private void Select_position_cruiser(Random rand) {
        int initX;
        boolean isVertical;
        int initY;
        do
        {
            isVertical = (rand.nextInt() % 2 != 0);
            initX = Math.abs(rand.nextInt() % (isVertical ? height : height/2 + 3));
            initY = Math.abs(rand.nextInt() % (isVertical ? weight/2 + 3 : weight));
        }
        while ((board[initX][initY] != 0) ||
                (isVertical && ((board[initX][initY+1] != 0) || (board[initX][initY+2] != 0))) ||
                (!isVertical && ((board[initX+1][initY] != 0) || (board[initX+2][initY] != 0))));
        if (isVertical)
            board[initX][initY] = board[initX][initY+1] = board[initX][initY+2] = hasCruiser;
        else
            board[initX][initY] = board[initX+1][initY] = board[initX+2][initY] = hasCruiser;
        cruiser = new Cruiser(initX, initY, isVertical);
    }

    private void Select_position_battleship(Random rand) {
        int initX;
        boolean isVertical;
        int initY;
        do
        {
            isVertical = (rand.nextInt() % 2 != 0);
            initX = Math.abs(rand.nextInt() % (isVertical ? height : height/2 + 2));
            initY = Math.abs(rand.nextInt() % (isVertical ? weight/2 + 2 : weight));
        }
        while ((board[initX][initY] != 0) ||
                (isVertical &&
                        ((board[initX][initY+1] != 0) || (board[initX][initY+2] != 0) || (board[initX][initY+3] != 0))) ||
                (!isVertical &&
                        ((board[initX+1][initY] != 0) || (board[initX+2][initY] != 0) || (board[initX+3][initY] != 0))));
        if (isVertical)
            board[initX][initY] = board[initX][initY+1] = board[initX][initY+2] = board[initX][initY+3] =
                    hasBattleship;
        else
            board[initX][initY] = board[initX+1][initY] = board[initX+2][initY] = board[initX+3][initY] =
                    hasBattleship;
        battleship = new Battleship(initX, initY, isVertical);
    }

    private void Select_position_carrier(Random rand) {
        int initX;
        boolean isVertical;
        int initY;
        do
        {
            isVertical = rand.nextInt() % 2 != 0;
            initX = Math.abs(rand.nextInt() % (isVertical ? height : height/2 + 1));
            initY = Math.abs(rand.nextInt() % (isVertical ? weight/2 + 1 : weight));
        }
        while ((board[initX][initY] != 0) ||
                (isVertical && ((board[initX][initY+1] != 0) ||
                        (board[initX][initY+2] != 0) || (board[initX][initY+3] != 0) || (board[initX][initY+4] != 0))) ||
                (!isVertical && ((board[initX+1][initY] != 0) ||
                        (board[initX+2][initY] != 0) || (board[initX+3][initY] != 0) || (board[initX+4][initY] != 0))));
        if (isVertical)
            board[initX][initY] = board[initX][initY+1] = board[initX][initY+2] = board[initX][initY+3] =
                    board[initX][initY+4] = hasBattleship;
        else
            board[initX][initY] = board[initX+1][initY] = board[initX+2][initY] = board[initX+3][initY] =
                    board[initX+4][initY] = hasBattleship;
        battleship = new Battleship(initX, initY, isVertical);
    }

    // Returns true if the game has been won

    public boolean isGameOver()
    {
        return (numberSunk == (number_carrier + number_submarine + number_cruiser + number_destroyer + number_battleship));
    }

    // Records a guess

    public char guess(int x, int y)
    {
        numberOfGuesses++;

        if ((board[x][y] & GuessMask) != noGuess)
        {
            duplicateGuesses++;
            System.out.println("You already hitted this cell ;)");
            return (char)(board[x][y] & GuessMask);
        }

        switch (board[x][y] & ShipMask)
        {
            case isEmpty:
                board[x][y] |= isMiss;
                Print();
                System.out.println("You missed a hit.");
                return isMiss;

            case hasCarrier:
                board[x][y] |= isHit;
                numberOfHits++;
                carrier.registerHit(x, y);
                if (carrier.isSunk())
                {
                    for (int i = 0; i < 5; i++)
                        board[carrier.xPos[i]][carrier.yPos[i]] |= isSunk;
                    numberSunk++;
                    Print();
                    System.out.println("Boom! You sunk the carrier!");
                    return isSunk;
                }
                else
                    Print();
                    System.out.println("You hitted the ship!");
                    return isHit;

            case hasBattleship:
                board[x][y] |= isHit;
                numberOfHits++;
                battleship.registerHit(x, y);
                if (battleship.isSunk())
                {
                    for (int i = 0; i < 4; i++)
                        board[battleship.xPos[i]][battleship.yPos[i]] |= isSunk;
                    numberSunk++;
                    Print();
                    System.out.println("Boom! You sunk the battleship!");
                    return isSunk;
                }
                else
                    Print();
                    System.out.println("You hitted the ship!");
                    return isHit;

            case hasCruiser:
                board[x][y] |= isHit;
                numberOfHits++;
                cruiser.registerHit(x, y);
                if (cruiser.isSunk())
                {
                    for (int i = 0; i < 3; i++)
                        board[cruiser.xPos[i]][cruiser.yPos[i]] |= isSunk;
                    numberSunk++;
                    Print();
                    System.out.println("Boom! You sunk the cruiser!");
                    return isSunk;
                }
                else
                    Print();
                    System.out.println("You hitted the ship!");
                    return isHit;

            case hasSubmarine:
                board[x][y] |= isHit;
                numberOfHits++;
                submarine.registerHit(x, y);
                if (submarine.isSunk())
                {
                    for (int i = 0; i < 1; i++)
                        board[submarine.xPos[i]][submarine.yPos[i]] |= isSunk;
                    numberSunk++;
                    Print();
                    System.out.println("Boom! You sunk the submarine!");
                    return isSunk;
                }

            case hasDestroyer:
                board[x][y] |= isHit;
                numberOfHits++;
                destroyer.registerHit(x, y);
                if (destroyer.isSunk())
                {
                    for (int i = 0; i < 2; i++)
                        board[destroyer.xPos[i]][destroyer.yPos[i]] |= isSunk;
                    numberSunk++;
                    Print();
                    System.out.println("Boom! You sunk the destroyer!");
                    return isSunk;
                }
                else
                    Print();
                    System.out.println("You hitted the ship!");
                    return isHit;
        }

        return isMiss; // should never get here
    }

    // Displays the game board
    public void display()
    {
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < weight; x++)
            {
                if (board[x][y] == 0)
                    System.out.print(".");
                else
                {
                    switch (board[x][y] & ShipMask)
                    {
                        case isEmpty:
                            System.out.print("M");
                            break;
                        case hasCarrier:
                            if ((board[x][y] & GuessMask) == noGuess)
                                System.out.print("a");
                            else
                                System.out.print("A");
                            break;
                        case hasBattleship:
                            if ((board[x][y] & GuessMask) == noGuess)
                                System.out.print("b");
                            else
                                System.out.print("B");
                            break;
                        case hasCruiser:
                            if ((board[x][y] & GuessMask) == noGuess)
                                System.out.print("c");
                            else
                                System.out.print("C");
                            break;
                        case hasSubmarine:
                            if ((board[x][y] & GuessMask) == noGuess)
                                System.out.print("s");
                            else
                                System.out.print("S");
                            break;
                        case hasDestroyer:
                            if ((board[x][y] & GuessMask) == noGuess)
                                System.out.print("d");
                            else
                                System.out.print("D");
                            break;
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void Print() {
        System.out.print("  ");
        for(int i = 0; i < weight; i++)
            System.out.print(i);
        System.out.println();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < weight; x++) {
                if (x == 0) {
                    if (board[x][y] == 0)
                        System.out.print(y + "|~");
                    else {
                        switch (board[x][y] & ShipMask) {
                            case isEmpty:
                                System.out.print(y + "|o");
                                break;
                            case hasCarrier:
                            case hasCruiser:
                            case hasBattleship:
                            case hasSubmarine:
                            case hasDestroyer:
                                if ((board[x][y] & GuessMask) == isHit)
                                    System.out.print(y + "|x");
                                else if ((board[x][y] & GuessMask) == isSunk)
                                    System.out.print(y + "|X");
                                else
                                    System.out.print(y + "|~");
                                break;
                        }
                    }
                }
                else {
                    if (board[x][y] == 0)
                        System.out.print("~");
                    else {
                        switch (board[x][y] & ShipMask) {
                            case isEmpty:
                                System.out.print("o");
                                break;
                            case hasCarrier:
                            case hasCruiser:
                            case hasBattleship:
                            case hasSubmarine:
                            case hasDestroyer:
                                if ((board[x][y] & GuessMask) == isHit)
                                    System.out.print("x");
                                else if ((board[x][y] & GuessMask) == isSunk)
                                    System.out.print("X");
                                else
                                    System.out.print("~");
                                break;
                        }
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public int numberSunk;       // number of ships sunk in this game
    public int numberOfHits;     // number of hits recorded in this game (duplicates aren't counted
    public int numberOfGuesses;  // number of guesses made in this game
    public int duplicateGuesses; // number of duplicated guesses made in this game
}