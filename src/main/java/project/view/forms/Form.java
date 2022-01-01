package project.view.forms;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

public abstract class Form extends VBox {

    protected final Button submitButton;

    protected Form(String submitButtonText) {
        submitButton = new Button(submitButtonText);
    }

    public void addEventHandlerToSubmitButton(EventHandler<ActionEvent> eventHandler) {
        submitButton.setOnAction(eventHandler);

        // add support for enter key to submit form
        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitButton.fire();
            }
        });
    }

    public abstract boolean isFormReady();

}
