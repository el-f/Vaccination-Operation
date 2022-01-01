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
        imgView = new ImageView(imgURL);
        imgView.setFitWidth(150);
        imgView.setFitHeight(150);

        Text description = MainView.getPrettyText(text, 15, null, null, FontWeight.SEMI_BOLD);

        setSpacing(10);
        setAlignment(Pos.CENTER);

        getChildren().addAll(imgView, description);

        MainView.setCursorAsSelectInRegion(this);
        setOnMouseEntered(event -> imgView.setOpacity(0.7));
        setOnMouseExited(event -> imgView.setOpacity(1.0));
    }

}
