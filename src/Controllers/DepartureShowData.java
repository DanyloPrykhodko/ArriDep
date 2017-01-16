package Controllers;

import Classes.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static Classes.Support.ShowAlert;

public class DepartureShowData {
    public ToggleButton SendButton;
    public ToggleButton NeedButton;
    public ToggleButton LeftButton;
    public TableView<Unit> Table;
    public TableColumn<Unit, String> Barcode;
    public TableColumn<Unit, String> Code;
    public TableColumn<Unit, String> Type;
    public TableColumn<Unit, String> Color;
    public TableColumn<Unit, String> Size;
    public TableColumn<Unit, String> Price;
    public TableColumn<Unit, String> Point;
    public AnchorPane AP;

    private Stage stage;                                                                //Сцена
    private ObservableList<Unit> observableList = FXCollections.observableArrayList();  //Элементы таблицы

    //Заполнение таблици по умолчанию
    public void initialize() {
        for (int i = 0; i < Departure.accept.size(); i++)
            for (String barcode: Departure.accept.get(i))
                observableList.add(getUnit(barcode, Departure.points.get(i).getName()));
        Barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        Code.setCellValueFactory(new PropertyValueFactory<>("code"));
        Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        Color.setCellValueFactory(new PropertyValueFactory<>("color"));
        Size.setCellValueFactory(new PropertyValueFactory<>("size"));
        if (Departure.priceContains)
            Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        else
            Table.getColumns().remove(Price);
        Point.setCellValueFactory(new PropertyValueFactory<>("point"));
        Table.setItems(observableList);
    }

    //Назначение сцены
    void setStage(Stage stage) {
        this.stage = stage;
    }

    //Заполнение таблици отправленым
    public void Send() {
        try {
            observableList.clear();
            for (int i = 0; i < Departure.accept.size(); i++)
                for (String barcode: Departure.accept.get(i))
                    observableList.add(getUnit(barcode, Departure.points.get(i).getName()));
            Table.setItems(observableList);
            SendButton.setSelected(true);
            NeedButton.setSelected(false);
            LeftButton.setSelected(false);
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось загрузить таблицу отправленного!");
        }
    }

    //Заполнение таблици нужным
    public void Need() {
        try {
            observableList.clear();
            for (int i = 0; i < Departure.inputs.size(); i++)
                for (String barcode: Departure.inputs.get(i))
                    observableList.add(getUnit(barcode, Departure.points.get(i).getName()));
            Table.setItems(observableList);
            NeedButton.setSelected(true);
            SendButton.setSelected(false);
            LeftButton.setSelected(false);
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось загрузить таблицу нужного!");
        }
    }

    //Заполнение таблици оставшимся
    public void Left() {
        try {
            observableList.clear();
            for (String barcode : Departure.excess)
                observableList.add(getUnit(barcode, "СКЛАД"));
            Table.setItems(observableList);
            LeftButton.setSelected(true);
            SendButton.setSelected(false);
            NeedButton.setSelected(false);
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось загрузить таблицу оставшегося!");
        }
    }

    //Получиния еденици
    private Unit getUnit(String barcode, String name) {
        return new Unit(barcode, Departure.base.get(barcode), name);
    }
}
