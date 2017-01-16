package Controllers;

import Classes.Point;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static Classes.Support.*;

public class Departure {
    public Label Load;
    public Label Read;
    public TextField Barcode;
    public ComboBox Input;
    public Button OpenButton;
    public Button SaveButton;
    public Button UpdateButton;
    public Button ViewDataButton;
    public CheckBox Sort;
    public CheckBox Remember;
    public TableView<Point> Table;
    public TableColumn<Point, String> NameColumn;
    public TableColumn<Point, Integer> CountColumn;
    public TableColumn<Point, Integer> AcceptColumn;
    public AnchorPane AP;

    private Stage stage;                                                                //Сцена
    private String barcode;                                                             //Штрих-код
    public static boolean priceContains;                                                //Наличие цены
    private int memoryInput = -1;                                                       //В памяти
    public static boolean rightExit = false;                                            //Правильный выход из AddDot
    public static ArrayList<Point> points = new ArrayList<>();                          //Точки
    public static HashMap<String, ArrayList<String>> base = new HashMap();              //База данных
    public static ArrayList<ArrayList<String>> accept = new ArrayList();                //Принято
    public static ArrayList<ArrayList<String>> inputs = new ArrayList();                //Нужно
    public static ArrayList<String> excess = new ArrayList();                           //Лишнее
    private ObservableList<Point> observableList = FXCollections.observableArrayList(); //Элементы таблици

    //Назначение сцены
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //Открыть файла
    public void OpenFile() {
        stage = (Stage) AP.getScene().getWindow();
        try {
            File file = ChooseOpenFile(stage);
            if (file == null)
                return;
            FileInputStream fileInputStream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            priceContains = workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells() == 6;
            if (Sort.isSelected()) {
                int numberOfSheets = 2;
                if (workbook.getNumberOfSheets() == 1)
                    numberOfSheets = 1;
                for (int i = 0; i < numberOfSheets; i++)
                    for (int j = 0; j < workbook.getSheetAt(i).getPhysicalNumberOfRows(); j++) {
                        String barcode = workbook.getSheetAt(i).getRow(j).getCell(0).getStringCellValue();
                        if (barcode.equals(""))
                            continue;
                        excess.add(barcode);
                        if (!base.containsKey(barcode)) {
                            ArrayList<String> arrayList = new ArrayList<>();
                            for (int k = 1; k < 6 && k < workbook.getSheetAt(i).getRow(j).getPhysicalNumberOfCells(); k++)
                                arrayList.add(RemoveSpaces(workbook.getSheetAt(i).getRow(j).getCell(k).getStringCellValue()).toUpperCase());
                            base.put(barcode, arrayList);
                        }
                    }
                if  (!(excess.size() > 0)) {
                    ShowAlert(stage, "Не удалось получить ни одного объекта для базы-данных!");
                    ResetDefault();
                }
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxmls/AddDot.fxml"));
                Parent root = loader.load();
                stage.setScene(new Scene(root, 200, 300));
                AddDot controller = loader.getController();
                controller.setStage(stage);
                stage.setTitle("Создание точек");
                stage.setResizable(false);
                stage.setFullScreen(false);
                initModality(stage, this.stage);
                stage.showAndWait();
                if (!rightExit)
                    return;
                for (int i = 0; i < points.size(); i++) {
                    inputs.add(new ArrayList<>());
                    accept.add(new ArrayList<>());
                }
                Sort();
            } else {
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    points.add(new Point(workbook.getSheetName(i).toUpperCase()));
                    inputs.add(new ArrayList<>());
                    accept.add(new ArrayList<>());
                    for (int j = 0; j < workbook.getSheetAt(i).getPhysicalNumberOfRows(); j++) {
                        String barcode = workbook.getSheetAt(i).getRow(j).getCell(0).getStringCellValue();
                        excess.add(barcode);
                        if (!base.containsKey(barcode)) {
                            ArrayList<String> arrayList = new ArrayList<>();
                            for (int k = 1; k < 6 && k < workbook.getSheetAt(i).getRow(j).getPhysicalNumberOfCells(); k++)
                                arrayList.add(RemoveSpaces(workbook.getSheetAt(i).getRow(j).getCell(k).getStringCellValue()).toUpperCase());
                            base.put(barcode, arrayList);
                        }
                        inputs.get(i).add(barcode);
                    }
                }
            }
            for (int i = 0; i < points.size(); i++)
                points.get(i).setCount(inputs.get(i).size());
            observableList.addAll(points);
            NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            CountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
            AcceptColumn.setCellValueFactory(new PropertyValueFactory<>("accept"));
            Table.setItems(observableList);
            Table.setDisable(false);
            ObservableList<String> inputObservableList = FXCollections.observableArrayList();
            inputObservableList.add("СКЛАД");
            for (Point point : points)
                inputObservableList.add(point.getName());
            Input.setItems(inputObservableList);

            Load.setText("В базе: " + excess.size());
            OpenButton.setDisable(true);
            Sort.setDisable(true);
            Remember.setDisable(false);
            Barcode.setDisable(false);
            SaveButton.setDisable(false);
            ViewDataButton.setDisable(false);
            Table.setDisable(false);
        } catch (Exception e) {
            base.clear();
            excess.clear();
            inputs.clear();
            accept.clear();
            points.clear();
            observableList.clear();
            if (Sort.isSelected())
                ShowAlert(stage, "Не удалось получить базу-двнных и рассортировать её!");
            else
                ShowAlert(stage, "Не удалось получить базу-двнных!");
        }
    }

    //Сортировка
    private void Sort() {
        ArrayList<String> excessCopy = new ArrayList<>(excess);
        for (int ex = 0; ex < excess.size(); ex++) {
            String barcode = excess.get(ex);
            ArrayList<String> data = new ArrayList<>(base.get(barcode));
            String size = data.get(3);
            boolean a = true;
            for (String s : inputs.get(0))
                if (base.get(s).get(0).equals(data.get(0)) && base.get(s).get(2).equals(data.get(2)))
                    a = false;
            if (!inputs.get(0).contains(barcode) && a)
                if (size.equals("M") || size.equals("28") || size.equals("38")) {
                    ArrayList<String> row = new ArrayList<>();
                    row.add(barcode);
                        for (int s = 1; s < 4; s++) {
                            switch(size) {
                                case "M":
                                    sw:
                                    switch (s) {
                                        case 1:
                                            data.set(3, "S");
                                            break;
                                        case 2:
                                            data.set(3, "L");
                                            break;
                                        case 3:
                                            data.set(3, "XS");
                                            for (String x : excess)
                                                if (base.get(x).equals(data))
                                                    break sw;
                                            data.set(3, "XS");
                                    }
                                    break;
                                case "28":
                                    sw:
                                    switch (s) {
                                        case 1:
                                            data.set(3, "27");
                                            break;
                                        case 2:
                                            data.set(3, "29");
                                            break;
                                        case 3:
                                            data.set(3, "26");
                                            for (String x : excess)
                                                if (base.get(x).equals(data))
                                                    break sw;
                                            data.set(3, "30");
                                    }
                                    break;
                                case "38":
                                    sw:
                                    switch (s) {
                                        case 1:
                                            data.set(3, "37");
                                            break;
                                        case 2:
                                            data.set(3, "39");
                                            break;
                                        case 3:
                                            data.set(3, "36");
                                            for (String x : excess)
                                                if (base.get(x).equals(data))
                                                    break sw;
                                            data.set(3, "40");
                                    }
                            }
                            for (String barcodeSecond : excess) {
                                if (base.get(barcodeSecond).equals(data)) {
                                    row.add(barcodeSecond);
                                    break;
                                }
                            }
                        }
                    if (row.size() != 4) continue;
                    if (size.equals("M") || size.equals("28"))
                        for (ArrayList<String> input : inputs) {
                            boolean b = false;
                            for (int i = 0; i < row.size(); i++) {
                                if (!excess.contains(row.get(i))) {
                                    b = true;
                                    size = base.get(row.get(i)).get(3);
                                    data = base.get(row.get(i));
                                    switch (size) {
                                        case "XS":
                                            data.set(3, "XL");
                                            break;
                                        case  "XL":
                                            data.set(3, "XS");
                                            break;
                                        case "26":
                                            data.set(3, "30");
                                            break;
                                        case "30":
                                            data.set(3, "26");
                                    }
                                    for (Map.Entry<String, ArrayList<String>> entry: base.entrySet())
                                        if (entry.getValue().equals(data) && excess.contains(entry.getKey())) {
                                            row.set(i, entry.getKey());
                                            b = false;
                                            break;
                                        }
                                    break;
                                }
                            }
                            if (b) break;
                            input.addAll(row);
                            for (String s : row)
                                excess.remove(s);
                        }
                    else {
                        inputs.get(0).addAll(row);
                        for (String s : row)
                            excess.remove(s);
                        row.clear();
                        String ar = base.get(barcode).get(0);
                        for (int i = 0; i < excess.size(); i++)
                            if (base.get(excess.get(i)).get(0).equals(ar) && excess.contains(excess.get(i))) {
                                row.add(excess.get(i));
                                excess.remove(i);
                                i--;
                            }
                        for (int i = 0; i < row.size() && i < inputs.size() - 1; i++)
                            inputs.get(i + 1).add(row.get(i));
                    }
                    ex = 0;
                }
        }
        excess.clear();
        excess.addAll(excessCopy);
    }

    //Зохранить файл
    public void SaveFile() {
        try {
            boolean save = true;
            for (Point id : points)
                if (id.getCount() > 0) {
                    save = false;
                    ButtonType buttonTypeYes = new ButtonType("Да");
                    ButtonType buttonTypeNo = new ButtonType("Нет");
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Отсуствует несколько едениц которые надо отправить на точку!\nЗаписать " +
                            "данные?", buttonTypeYes, buttonTypeNo);
                    alert.setHeaderText("Внимание!");
                    alert.initModality(Modality.WINDOW_MODAL);
                    alert.initOwner(stage.getScene().getWindow());
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get().equals(buttonTypeYes))
                        save = true;
                    break;
                }
            if (!save)
                return;
            File file = ChooseDirectory(stage);
            if (file == null)
                return;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
            String date = simpleDateFormat.format(new Date());
            File filePath = new File(file + "/" + date);
            if (filePath.exists())
                for (int i = 2; ; i++) {
                    filePath = new File(file + "/" + date + " " + i);
                    if (!filePath.exists())
                        break;
                }
            filePath.mkdir();
            for (int i = 0; i < points.size(); i++) {
                if  (points.get(i).getAccept() < 1)
                    continue;
                HSSFWorkbook workbook = new HSSFWorkbook();
                FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/" + date + " " + points.get(i).getName() + ".xls");
                HSSFSheet sheetAccept = workbook.createSheet("Отправленно");
                Save(sheetAccept, base, accept.get(i));
                workbook.write(fileOutputStream);
                workbook.close();
                fileOutputStream.close();
            }
            if (excess.size() > 0) {
                HSSFWorkbook workbook = new HSSFWorkbook();
                FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/" + date + " СКЛАД.xls");
                HSSFSheet sheet = workbook.createSheet("На складе");
                for (int i = 0; i < excess.size(); i++)
                    Save(sheet, base, excess);
                workbook.write(fileOutputStream);
                workbook.close();
                fileOutputStream.close();
            }

            ResetDefault();
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось записать данные в файл!");
        }
    }

    //Установка всего по умолчанию
    public void ResetDefault() {
        Load.setText("В базе: 0");
        Read.setText("Считано: 0");
        Sort.setSelected(false);
        Sort.setDisable(false);
        ViewDataButton.setDisable(true);
        SaveButton.setDisable(true);
        Table.setDisable(true);
        OpenButton.setDisable(false);
        Table.setItems(null);
        Table.refresh();
        Barcode.setDisable(true);
        Barcode.clear();
        memoryInput = -1;
        Input.getSelectionModel().select(-1);
        Input.setDisable(true);
        UpdateButton.setDisable(true);
        Remember.setSelected(false);
        Remember.setDisable(true);
        observableList.clear();
        base.clear();
        excess.clear();
        accept.clear();
        points.clear();
        inputs.clear();
        barcode = null;
    }

    //Чтения штрих-кода
    public void ReadBarcode(KeyEvent keyEvent) {
        try {
            if (keyEvent.getCode().equals(KeyCode.ENTER) && !Barcode.getText().equals("")) {
                barcode = Barcode.getText();
                if (excess.contains(barcode)) {
                    boolean presence = false;
                    for (int i = 0; i < inputs.size(); i++)
                        if (inputs.get(i).contains(barcode)) {
                            presence = true;
                            excess.remove(barcode);
                            Input.getSelectionModel().select(i + 1);
                            inputs.get(i).remove(barcode);
                            accept.get(i).add(barcode);
                            points.get(i).setCount(points.get(i).getCount() - 1);
                            points.get(i).setAccept(points.get(i).getAccept() + 1);
                            Table.refresh();
                            ViewDataButton.setDisable(false);
                            if (Remember.isSelected() && !(memoryInput == (i + 1)))
                                PlaySignal("Warning");
                            else
                                PlaySignal("Signal");
                            break;
                        }
                    if (!presence) {
                        if (Remember.isSelected()) {
                            Input.getSelectionModel().select(memoryInput);
                            UpdateBarcode();
                        } else {
                            Input.setDisable(false);
                            Input.getSelectionModel().select(0);
                            UpdateButton.setDisable(false);
                            memoryInput = Input.getSelectionModel().getSelectedIndex();
                        }
                    }
                } else
                    Input.setDisable(true);
                Barcode.clear();
                int readCount = 0;
                for (ArrayList<String> arrayList : accept)
                    readCount += arrayList.size();
                Read.setText("Считано: " + readCount);
            } else {
                Input.getSelectionModel().select(memoryInput);
                UpdateButton.setDisable(true);
            }
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось проврить штрих-код!");
        }
    }

    //Обновить штрих-код
    public void UpdateBarcode() {
        try {
            int input = Input.getSelectionModel().getSelectedIndex() - 1;
            if (input >= 0) {
                points.get(input).setAccept(points.get(input).getAccept() + 1);
                Table.refresh();
                excess.remove(barcode);
                accept.get(input).add(barcode);
                Input.setDisable(true);
                UpdateButton.setDisable(true);
                PlaySignal("Signal");
            }
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось обновить штрих-код!");
        }
    }

    //Изминение в памяти
    public void MemoryChanged() {
        if (Input.getSelectionModel().getSelectedIndex() > 0) {
            if (Remember.isSelected())
                memoryInput = Input.getSelectionModel().getSelectedIndex();
        } else
            Remember.setSelected(false);
    }

    //Перейти в режим просмотра
    public void ViewData() {
        try {
            //Создаем новое окно
            Stage stage = new Stage();
            //Установка интерфейса
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxmls/DepartureShowData.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 900, 400));
            DepartureShowData controller = loader.getController();
            controller.setStage(stage);
            stage.setFullScreen(false);
            stage.setTitle("Просмотр данных");
            stage.setMinHeight(200);
            stage.setMinWidth(900);
            stage.setMaxWidth(900);
            initModality(stage, this.stage);
            stage.showAndWait();
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось переключится в режим просмотра!");
        }
    }

    //Проверки на возможность обновления штрих-кода

    public void InputAction() {
        if (Input.isDisable())
            UpdateButton.setDisable(true);
        else
            UpdateButton.setDisable(false);
    }

    public void InputEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            UpdateButton.setDisable(false);
        else {
            UpdateButton.setDisable(true);
        }
    }
}