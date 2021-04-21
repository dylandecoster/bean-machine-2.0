import java.util.Random;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class MachineBase {
    public double width, height;
    public int balls;
    private final int slots;
    private int speed;
    private double y, f = 0;
    
    // Default machine
    public MachineBase() {
        width = 200;
        height = width;
        slots = 8;
        balls = 25;
        speed = 5;
    }
    
    // Constructor for a machine with given values
    public MachineBase(int width, int slots, int balls, int speed) {
        this.width = width;
        height = width;
        this.slots = slots;
        this.balls = balls;
        this.speed = speed;
    }
    
    public void createOutline(Group group) {
        final double finalHeight = y-(y/slots);

        // Creates the main part of the outline
        Polyline outline = new Polyline();
        outline.getPoints().addAll(new Double[] {
            width/2-(width/(slots+(slots/2)+8)), -(height/.75), // TOP LEFT
            width/2-(width/(slots+(slots/2)+8)), -(finalHeight), // UPPER LEFT CENTER
            0.0, -(height/3.5), // BOTTOM LEFT CENTER
            0.0, 0.0, // BOTTOM LEFT
            width, 0.0, // BOTTOM RIGHT
            width, -(height/3.5), // BOTTOM RIGHT CENTER
            width/2+(width/(slots+(slots/2)+8)), -(finalHeight), // BOTTOM LEFT CENTER 
            width/2+(width/(slots+(slots/2)+8)), -(height/.75) // TOP RIGHT
        });
        group.getChildren().add(outline); // Adds the main outline to the group
        
        // Creates every slot
        for(double i = 1; i < slots; i++) {
            Line line = new Line(i * (width/slots), 0, i * (width/slots), -1*(height/3.5)); // Creates a slot based on the size of the machine
            group.getChildren().add(line); // Adds the slot to the group
        }
    }

    public void createPins(Group group) {
        int numPerRow = slots; // Changes based on the amount in the current row
        y = height/3.5; // Gets the height of the area the pins will be created in
        for(int i = 0; i <= slots; i++) {
            for(int j = 1; j < numPerRow; j++) {
                Circle circle = new Circle((j+f) * (width/slots), -y, (width/slots)/5, Color.BLACK);
                group.getChildren().add(circle); // Adds the circle to the group
            }
            numPerRow--; // Removes a pin per row
            y += height/(slots + 1); // Moves the y of where the next row will be created
            f+=.5; // Something with the spacing of the balls thats hard to explain
        }
    }
    
    private int n = 0; // Used for delaying the animation
    public void move(Group group) {
        final double ballSize = (width/slots)/7; // Creates the balls size based on the machine size
        Random rand = new Random(); // Creates a new random
        
        // Gets the bottom of all the slots in the machine and stores them in an array
        double[] slotsY = new double[slots];
        for(int i = 0; i < slotsY.length; i++)
            slotsY[i] = (width/3.5) - (ballSize*2) - (ballSize/2);

        // Gets the x of all of the slots in the machine and stores them in an array
        double[] slotsX = new double[slots];
        for(int i = 0; i < slotsX.length; i++)
            slotsX[i] = (i+1) * (width/slots) - ((width/slots)/2);
        
        for(int i = 0; i < balls; i++) {
            double currentX = width/2, currentY = -(height/.75) + ballSize; // Sets the starting point of the ball    

            Circle ball = new Circle(currentX, currentY, ballSize, Color.GRAY); // Creates a new ball at the starting point
            Path path = new Path(); // Creates a new path
            PathTransition anim = new PathTransition(Duration.millis((11 - speed) * 500), path, ball); // Creates an animation for the path that lasts the specified milliseconds

            path.getElements().add(new MoveTo(currentX, currentY)); // Creates the default path location
            path.getElements().add(new LineTo(currentX, currentY = -((y-(y/slots)) + (y - ((height/(slots + 1)*3))))/2)); // Drops the ball down into the big part

            double left = 0, right = 0, count = 25;
            // Repeats for the amount of rows there is
            for(int j = 1; j < slots; j++) {
                int num = rand.nextInt(50);
                double moveChance = rand.nextDouble();
                if(num > count) {
                    path.getElements().add(new LineTo(currentX += (width/slots) - ((width/slots)/2), currentY += height/(slots + 1))); // LEFT
                    left+=moveChance; // Increased the chance of the ball moving toward the left slightly
                    count-=left;
                } else {
                    path.getElements().add(new LineTo(currentX -= (width/slots) - ((width/slots)/2), currentY += height/(slots + 1))); // RIGHT
                    right+=moveChance; // Increased the chance of the ball moving toward the right slightly
                    count+=right;
                }
            }
            
            // Puts the ball at the bottom of the slot
            for(int j = 0; j < slotsX.length; j++) {
                if(Math.floor(slotsX[j]) == Math.floor(currentX)) { // Checks which slot the ball lands into
                    path.getElements().add(new LineTo(currentX, currentY += slotsY[j])); // Sets the ball in the slot
                    slotsY[j] -= (ballSize*2)-1; // Stacks the balls that are in the same slot
                }
            }
            
//            anim.setDelay(Duration.millis(1000));
            anim.setDelay(Duration.millis(1000 * n++)); // Delays the animations so they don't all run together
            anim.play(); // Plays the animation
            group.getChildren().add(ball);// Adds the ball into the group
        }
    }
}
