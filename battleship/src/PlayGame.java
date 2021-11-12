import java.util.Random;
import java.util.Scanner;

class PlayGame
{
    public static void main(String argv[])
    {
        System.out.println("**** Welcome to Battle Ships game ****");

        Scanner input = new Scanner(System.in);

        int height;
        int weight;

        do {
            System.out.print("Enter height of the sea (from 5 to 100): ");
            height = input.nextInt();
            System.out.print("Enter weight of the sea (from 5 to 100): ");
            weight = input.nextInt();
        } while (height > 100 || height < 5 || weight > 100 || weight < 5);

        boolean alreadyGuessed[][] = new boolean[height][weight];
        for (int x = 0; x < height; x++)
            for (int y = 0; y < weight; y++)
                alreadyGuessed[x][y] = false;

        System.out.print("Enter number of the carriers: ");
        int car = input.nextInt();
        System.out.print("Enter number of the battleships: ");
        int bat = input.nextInt();
        System.out.print("Enter number of the cruisers: ");
        int cru = input.nextInt();
        System.out.print("Enter number of the destroyers: ");
        int des = input.nextInt();
        System.out.print("Enter number of the submarines: ");
        int sub = input.nextInt();

        GameBoard b = new GameBoard(car, bat, cru, des, sub, height, weight);
        b.Print();
        Random rand = new Random();
        while (!b.isGameOver())
        {
            int yPos;
            int xPos;

            do {
                System.out.print("Enter Y coordinate (height - from 0 to " + (height - 1) + "): ");
                yPos = input.nextInt();
                System.out.print("Enter X coordinate (weight - from 0 to " + (weight - 1) + "): ");
                xPos = input.nextInt();
            } while (yPos < 0 || yPos >= height || xPos < 0 || xPos >= weight);

            if (!alreadyGuessed[xPos][yPos])
            {
                alreadyGuessed[xPos][yPos] = true;
                System.out.println("You already hitted this cell ;)");
                b.guess(xPos, yPos);
            }
        }
        b.display();
        System.out.println(b.numberOfGuesses + " guesses, " + b.numberOfHits + " hits, sunk " + b.numberSunk);
    }
}