package asu.gunma.ui.util.lives;

public class Life {
    private int xPos;
    private int yPos;
    private boolean isActive;

    public Life(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.isActive = true;
    }

    public int getxPos() {
        return this.xPos;
    }

    public int getyPos() {
        return this.yPos;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getActive() {
        return this.isActive;
    }
}
