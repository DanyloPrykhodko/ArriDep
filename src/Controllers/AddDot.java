package Controllers;

import Classes.Point;
import Classes.Support;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

import static Classes.Support.ShowAlert;

public class AddDot {
    public TableView<Point> Table;
    public TableColumn<Point, String> NameColumn;
    public TextField Name;
    public AnchorPane AP;

    private Stage stage;                                                                    //Сцена
    private ObservableList<Point> observableList = FXCollections.observableArrayList();     //Элементы таблици
    private ArrayList<String> names = new ArrayList<>();                                    //Названия точек

    //Назначение сцены
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //Добавить точку
    public void AddNewDot() {
        try {
            String name = Support.RemoveSpaces(Name.getText().toUpperCase());
            Name.setText("");
            if (!names.contains(name)) {
                if (!name.equals("")) {
                    names.add(name);
                    Departure.points.add(new Point(name));
                    observableList.add(new Point(name));
                    NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                    Table.setItems(observableList);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Такое имя точки уже используеться!", ButtonType.OK);
                alert.setHeaderText("Внимание!");
                alert.initModality(Modality.WINDOW_MODAL);
                alert.initOwner(Name.getScene().getWindow());
                alert.show();
                Support.PlaySignal("Warning");
            }
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось добавить точку!");
        }
    }

    //Записать точки
    public void SaveDots() {
        try {
            if (Departure.points.size() > 0) {
                Departure.rightExit = true;
                observableList.clear();
                Stage stage = (Stage) Name.getScene().getWindow();
                stage.close();
            }
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось записать точки!");
        }
    }

    //Обработка клавиатуры
    public void NameEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            AddNewDot();
    }
}
