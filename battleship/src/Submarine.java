class Submarine
{
    public int xPos[];
    public int yPos[];

    public Submarine(int x, int y)
    {
        xPos = new int[1];
        yPos = new int[1];

        isHit = new boolean[1];

        for (int i = 0; i < 1; i++)
        {
            xPos[i] = x;
            yPos[i] = y;
            isHit[i] = false;
        }

        numHits = 0;
    }

    public String name()
    {
        return "Submarine";
    }

    public boolean registerHit(int x, int y)
    {
        for (int i = 0; i < 1; i++)
        {
            if (xPos[i] == x && yPos[i] == y)
            {
                if (!isHit[i])
                {
                    isHit[i] = true;
                    numHits++;
                }
                return true;
            }
        }

        return false;
    }

    public boolean isSunk()
    {
        return (numHits == 1) ? true : false;
    }

    private boolean isHit[];
    private int numHits;
}