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

public class ArrivalShowData {
    public ToggleButton AcceptButton;
    public ToggleButton NotFoundButton;
    public ToggleButton ExcessButton;
    public TableView<Unit> Table;
    public TableColumn<Unit, String> Barcode;
    public TableColumn<Unit, String> Code;
    public TableColumn<Unit, String> Type;
    public TableColumn<Unit, String> Color;
    public TableColumn<Unit, String> Size;
    public TableColumn<Unit, String> Price;
    public AnchorPane AP;

    private Stage stage;                                                                //Сцена
    private ObservableList<Unit> observableList = FXCollections.observableArrayList();  //Элементы таблицы

    //Заполнение таблици по умолчанию
    public void initialize() {
        for (String barcode : Arrival.accept)
            observableList.add(getUnit(barcode));
        Barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        Code.setCellValueFactory(new PropertyValueFactory<>("code"));
        Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        Color.setCellValueFactory(new PropertyValueFactory<>("color"));
        Size.setCellValueFactory(new PropertyValueFactory<>("size"));
        if (Arrival.priceContains)
            Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        else
            Table.getColumns().remove(Price);
        Table.setItems(observableList);
    }

    //Назначение сцены
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //Заполнение таблици принятым
    public void Accept() {
        try {
            observableList.clear();
            for (String barcode : Arrival.accept)
                observableList.add(getUnit(barcode));
            Table.setItems(observableList);
            AcceptButton.setSelected(true);
            NotFoundButton.setSelected(false);
            ExcessButton.setSelected(false);
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось загрузить таблицу принятого!");
        }
    }

    //Заполнение таблици не найденым
    public void NotFound() {
        try {
            observableList.clear();
            for (String barcode : Arrival.notFound)
                observableList.addAll(getUnit(barcode));
            Table.setItems(observableList);
            NotFoundButton.setSelected(true);
            ExcessButton.setSelected(false);
            AcceptButton.setSelected(false);
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось загрузить таблицу не найденого!");
        }
    }

    //Заполнение таблици лишним
    public void Excess() {
        try {
            observableList.clear();
            for (String barcode : Arrival.excess)
                observableList.addAll(getUnit(barcode));
            Table.setItems(observableList);
            ExcessButton.setSelected(true);
            AcceptButton.setSelected(false);
            NotFoundButton.setSelected(false);
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось загрузить таблицу лишнего!");
        }
    }

    //Получиния еденици
    private Unit getUnit(String barcode) {
        return new Unit(barcode, Arrival.base.get(barcode));
    }
}
