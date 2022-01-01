package project.view;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PrettyButton extends VBox {

    public PrettyButton(String text, String imgURL) {
        ImageView imgView = new ImageView(imgURL);
        imgView.setFitWidth(150);
        imgView.setFitHeight(150);

        Text description = MainView.getPrettyText(text, 20, Color.WHITE, Color.BLACK);

        setSpacing(10);
        setAlignment(Pos.CENTER);

        getChildren().addAll(imgView, description);

        MainView.setCursorAsSelectInRegion(this);
    }

}
