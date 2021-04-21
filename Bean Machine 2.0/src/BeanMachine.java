import javafx.application.Application;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class BeanMachine extends Application {
    StackPane pane = new StackPane(); // Stores everything
    public Group group = new Group(); // Stores the entire machine

    Slider taBalls = new Slider(1, 50, 25);
    TextField tfBalls = new TextField();

    @Override
    public void start(Stage stage) {
        Label lblSize = new Label("Enter a machine size: ");
        Slider taSize = new Slider(1, 10, 1);
        taSize.setBlockIncrement(1);
        taSize.setMaxSize(75, 25);
        taSize.setMinSize(75, 25);
        TextField tfSize = new TextField();
        tfSize.setEditable(false);
        tfSize.setMinSize(50, 25);
        tfSize.setMaxSize(50, 25);
        HBox size = new HBox();
        size.getChildren().addAll(lblSize, taSize, tfSize);

        Label lblSlot = new Label("How many slots do you want? ");
        Slider taSlot = new Slider(5, 25, 8);
        taSlot.setBlockIncrement(1);
        taSlot.setMaxSize(75, 25);
        taSlot.setMinSize(75, 25);
        TextField tfSlot = new TextField();
        tfSlot.setEditable(false);
        tfSlot.setMinSize(50, 25);
        tfSlot.setMaxSize(50, 25);
        HBox slot = new HBox();
        slot.getChildren().addAll(lblSlot, taSlot, tfSlot);

        Label lblBalls = new Label("How many balls do you want? ");
        taBalls.setBlockIncrement(1);
        taBalls.setMaxSize(75, 25);
        taBalls.setMinSize(75, 25);
        tfBalls.setEditable(false);
        tfBalls.setMinSize(50, 25);
        tfBalls.setMaxSize(50, 25);
        HBox balls = new HBox();
        balls.getChildren().addAll(lblBalls, taBalls, tfBalls);
        
        Label lblSpeed = new Label("Enter a machine speed: ");
        Slider taSpeed = new Slider(1, 10, 1);
        taSpeed.setBlockIncrement(1);
        taSpeed.setMaxSize(75, 25);
        taSpeed.setMinSize(75, 25);
        TextField tfSpeed = new TextField();
        tfSpeed.setEditable(false);
        tfSpeed.setMinSize(50, 25);
        tfSpeed.setMaxSize(50, 25);
        HBox speed = new HBox();
        speed.getChildren().addAll(lblSpeed, taSpeed, tfSpeed);
        
        tfSize.setText(String.valueOf((int)taSize.getValue()));
        tfSlot.setText(String.valueOf((int)taSlot.getValue()));
        tfBalls.setText(String.valueOf((int)taBalls.getValue()));
        tfSpeed.setText(String.valueOf((int)taSpeed.getValue()));
        
        MachineBase base = new MachineBase(); // Creates the default machine
        base.createPins(group);
        base.createOutline(group);
        base.move(group);

        Scene scene = new Scene(pane);
        stage.setTitle("Bean Machine");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        
        Rectangle background = new Rectangle(scene.getWidth(), scene.getHeight(), Color.LAVENDER);
        pane.getChildren().add(background);
        
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, (MouseEvent mouseEvent) -> {
            tfSize.setText(String.valueOf((int)taSize.getValue()));
            tfSlot.setText(String.valueOf((int)taSlot.getValue()));
            tfBalls.setText(String.valueOf((int)taBalls.getValue()));
            tfSpeed.setText(String.valueOf((int)taSpeed.getValue()));
        });
        
        taSlot.valueProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
            if (newValue == null) {
                tfBalls.setText("");
                return;
            }
            balls.getChildren().remove(taBalls);
            taBalls = new Slider(1, newValue.intValue() * 2, newValue.intValue());
            taBalls.setBlockIncrement(1);
            taBalls.setMaxSize(75, 25);
            taBalls.setMinSize(75, 25);
            balls.getChildren().add(1, taBalls);
            tfBalls.setText(String.valueOf(newValue.intValue()));
        });
        
        Button button = new Button("Refresh");
        button.setMinSize(300, 40);
        button.setMaxSize(300, 40);
        button.setOnAction((ActionEvent event) -> {
            int machineSize = (int)taSize.getValue();
            int totalSlots = (int)taSlot.getValue();
            int totalBalls = (int)taBalls.getValue();
            int totalSpeed = (int)taSpeed.getValue();
            
            group.getChildren().clear(); // Clears the machine
            MachineBase base1 = new MachineBase(machineSize * 50 + 150, totalSlots, totalBalls, totalSpeed); // Creates a new machine
            base1.createPins(group);
            base1.createOutline(group);
            base1.move(group);
        });

        VBox vbox = new VBox(); // Stores all the text stuff
        vbox.getChildren().addAll(size, slot, balls, speed, button);
        vbox.setAlignment(Pos.TOP_RIGHT);
        vbox.setSpacing(20);
        
        HBox hbox = new HBox(); // Stores the machine and the text
        hbox.getChildren().addAll(group, vbox);
        hbox.setSpacing(20);
        
        // Centers everything
        Group center = new Group();
        center.getChildren().add(hbox);
        pane.getChildren().add(center);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
