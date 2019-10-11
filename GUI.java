import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GUI extends Application{

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCards = new HBox(20);
    private HBox playerCards = new HBox(20);

    private Parent createContent(){
        Pane root = new Pane();
        HBox rootLayout = new HBox(5);
        rootLayout.setPadding(new Insets(5,5,5,5));

        StackPane leftStackPane = new StackPane();
        //LEFT RECTANGLE
        Rectangle playZone = new Rectangle(800,663);
        playZone.setFill(Color.GREEN);
        playZone.setArcHeight(10);
        playZone.setArcWidth(10);



        VBox leftVBox = new VBox(150); //this VBox holds the discard pile and the player's hand
        leftVBox.setAlignment(Pos.CENTER_LEFT);

        HBox discardPileHolder = new HBox(50); //this HBox will hold the discard pile
        //discardPileHolder.setAlignment(Pos.LEFT);
        Image discardImage = new Image("2_of_clubs.png");
        ImageView discardImageView = new ImageView(discardImage);
        String cssLayout = "-fx-border-color: #3BB1E3;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: solid;\n";

        VBox gameInfo = new VBox(5);
        gameInfo.setStyle(cssLayout);
        gameInfo.setPrefWidth(300.0);
        gameInfo.setPrefHeight(155.0);
        discardPileHolder.setAlignment(Pos.CENTER);
        discardPileHolder.getChildren().addAll(gameInfo,  discardImageView);

        HBox handHolder = new HBox(35); //this will hold the player's hand
        handHolder.setAlignment(Pos.BOTTOM_CENTER);
        ObservableList<ImageView> buttonArray = FXCollections.observableArrayList();


        Image img = new Image("2_of_clubs.png");
        ImageView imv = new ImageView(img);

        ImageView[] imvArray = {new ImageView(img),new ImageView(img),new ImageView(img), new ImageView(img), new ImageView(img), new ImageView(img), new ImageView(img), new ImageView(img), new ImageView(img)};
        buttonArray.addAll(imvArray);



        ListView<ImageView> buttonView = new ListView(buttonArray);
        buttonView.setOrientation(Orientation.HORIZONTAL);
        buttonView.setMaxHeight(175);
        buttonView.setPrefWidth(600.0);
        handHolder.getChildren().addAll(buttonView);

        leftVBox.getChildren().addAll(discardPileHolder, handHolder);
        leftStackPane.getChildren().addAll(playZone, leftVBox); //the left StackPane is completed



        StackPane rightStackPane = new StackPane();
        //RIGHT RECTANGLE
        Rectangle playMenu = new Rectangle(400,663);
        playMenu.setFill(Color.ORANGE);
        playMenu.setArcHeight(10);
        playMenu.setArcWidth(10);

        VBox rightVBox = new VBox(50); //this VBox holds the action buttons' VBox and the VBox containing text detailing the last played cards
        rightVBox.setAlignment(Pos.CENTER);

        VBox buttonsVBox = new VBox(10); //this VBox holds the action buttons
        VBox textBox = new VBox(10); //this VBox holds the text, which details the last played hands

        buttonsVBox.setAlignment(Pos.CENTER);
        textBox.setAlignment(Pos.CENTER);

        Text lastPlayed = new Text("No cards have been played yet"); //this text will hold details about the last played cards
        //Image img = new Image("2_of_clubs.png");
        //ImageView imv = new ImageView(img);
        //action buttons
        Button one  = new Button("Single Card");
        //Button one = new Button();
        Button two  = new Button("Stack");
        Button three  = new Button("Draw Cards");




        buttonsVBox.getChildren().addAll(one, two, three);

        textBox.getChildren().add(lastPlayed);

        rightVBox.getChildren().addAll(buttonsVBox, textBox, imv);
        rightStackPane.getChildren().addAll(playMenu, rightVBox); //the right StackPane is completed

        rootLayout.getChildren().addAll(leftStackPane, rightStackPane); //the completed left and right stackPanes are added to the rootLayout HBox

       root.getChildren().add(rootLayout); //the rootLayout HBox is added to the scene

        return root; //the scene is returned

    }

    public void start(Stage primaryStage){
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setWidth(1229);
        primaryStage.setHeight(710);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Crazy Eights");
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}