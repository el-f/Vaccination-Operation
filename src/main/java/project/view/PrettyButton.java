package project.view;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PrettyButton extends VBox {

    private final ImageView imgView;

    public PrettyButton(String text, String imgURL) {
        this(imgURL);
        Text description = MainView.getPrettyText(text, 15, null, null, FontWeight.SEMI_BOLD);
        getChildren().add(description);
    }

    public PrettyButton(String imgURL) {
        imgView = new ImageView(imgURL);
        setSpacing(10);
        setAlignment(Pos.CENTER);

        getChildren().add(imgView);

        MainView.setCursorAsSelectInRegion(this);
        setOnMouseEntered(event -> imgView.setOpacity(0.7));
        setOnMouseExited(event -> imgView.setOpacity(1.0));
        setSize(150, 150);
        setFillWidth(false);
    }

    public void setSize(int width, int height) {
        imgView.setFitWidth(width);
        imgView.setFitHeight(height);
    }

}
