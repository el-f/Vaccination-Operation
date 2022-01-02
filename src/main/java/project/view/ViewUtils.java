package project.view;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;
import project.model.entities.Worker;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

public class ViewUtils {

    public static void setCursorAsSelectInRegion(Region region) {
        region.setCursor(Cursor.HAND);
    }

    public static String getSingularUserInput(String message, String expectedInput) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText(message);
        dialog.setContentText(expectedInput + ":");
        Optional<String> result = dialog.showAndWait();
        return result.map(String::valueOf).orElse(null);
    }

    public static boolean getUserConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(null) == ButtonType.OK;
    }

    public static Text getPrettyText(String msg, int size, Color fill, Color stroke, FontWeight fontWeight) {
        Text text = new Text(msg);
        text.setFont(Font.font("Tahoma Bold", fontWeight, size));
        if (fill != null) text.setFill(fill);
        if (stroke != null) text.setStroke(stroke);
        return text;
    }

    public static void enforceNumericalField(TextField tf, String empty) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tf.getText().isEmpty()) {
                tf.setText(empty);
            } else if (!newValue.matches("\\d*")) {
                tf.setText(oldValue);
            }
        });
    }

    public static void centerColumn(TableColumn<?, ?> tc) {
        tc.setStyle("-fx-alignment: center;");
    }

    public static void unHighlightTable(TableView<?> tableView) {
        tableView.getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, oldV, newV) -> Platform.runLater(() -> tableView.getSelectionModel().clearSelection()));
    }

    public static void initBorderPane(BorderPane bp, EventHandler<MouseEvent> homeButtonEventHandler) {
        PrettyButton homeButton = new PrettyButton("project/images/home.png");
        homeButton.setSize(50);
        homeButton.setAlignment(Pos.TOP_LEFT);
        homeButton.setOnMouseClicked(homeButtonEventHandler);
        bp.setTop(new HBox(homeButton));
        bp.setBackground(MainView.DEFAULT_BLANK_BG);
    }

    public static <T> void addActionableColumnToTableView(TableView<T> tableView, String description, String imgURL, Consumer<T> action) {
        TableColumn<T, Void> replaceCol = new TableColumn<>(description);

        replaceCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<T, Void> call(final TableColumn<T, Void> param) {
                return new TableCell<>() {
                    private final PrettyButton removeBtn = new PrettyButton(imgURL);

                    {
                        removeBtn.setOnMouseClicked(click -> action.accept(getTableRow().getItem()));
                        removeBtn.setSize(30);
                        setAlignment(Pos.CENTER);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) setGraphic(null);
                        else setGraphic(removeBtn);
                    }
                };
            }
        });
        tableView.getColumns().add(replaceCol);
    }

    @SuppressWarnings("unchecked")
    public static TableView<Worker> getWorkerTableView(Collection<Worker> workers) {
        TableView<Worker> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set Columns
        TableColumn<Worker, String> ID = new TableColumn<>(MainView.TableColumns.ID.toString());
        TableColumn<Worker, String> name = new TableColumn<>(MainView.TableColumns.NAME.toString());
        TableColumn<Worker, String> phone = new TableColumn<>(MainView.TableColumns.PHONE.toString());
        TableColumn<Worker, String> license = new TableColumn<>(MainView.TableColumns.LICENSE.toString());
        TableColumn<Worker, String> seniority = new TableColumn<>(MainView.TableColumns.SENIORITY.toString());

        tableView.getColumns().addAll(ID, name, phone, license, seniority);

        Arrays.asList(
                ID,
                name,
                phone,
                license,
                seniority
        ).forEach(ViewUtils::centerColumn);

        // generate column values
        ID.setCellValueFactory(new PropertyValueFactory<>("workerId"));
        name.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getFirstName() + " " + param.getValue().getLastName()
        ));
        phone.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        license.setCellValueFactory(new PropertyValueFactory<>("medicalLicense"));
        seniority.setCellValueFactory(new PropertyValueFactory<>("seniority"));

        HBox.setMargin(tableView, MainView.TABLE_INSETS);
        tableView.getItems().addAll(workers);
        tableView.setMinWidth(500);

        ViewUtils.unHighlightTable(tableView);


        return tableView;
    }

}

