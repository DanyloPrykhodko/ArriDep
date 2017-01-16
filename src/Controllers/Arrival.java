package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Classes.Support.*;

public class Arrival {
    public Label Load;
    public Label Read;
    public TextField Barcode;
    public TextField Code;
    public TextField Type;
    public TextField Color;
    public ComboBox Size;
    public Button OpenButton;
    public Button SaveFile;
    public Button AddButton;
    public Button DepartureButton;
    public Button ViewDataButton;
    public CheckBox FactoryFile;
    public CheckBox RightCode;
    public CheckBox RightType;
    public CheckBox RightColor;
    public CheckBox RightSize;
    public AnchorPane AP;

    private Stage stage;                                                    //Сцена
    public static boolean priceContains;                                    //Наличие цены
    public static HashMap<String, ArrayList<String>> base = new HashMap();  //База данных
    public static ArrayList<String> accept = new ArrayList();               //Принято
    public static ArrayList<String> excess = new ArrayList();               //Лишнее
    public static ArrayList<String> notFound = new ArrayList();             //Не найдено

    //Инициализация класса
    public void initialize() {
        ObservableList<String> listColor = FXCollections.observableArrayList("XS", "S", "M", "L", "XL");
        Size.setItems(listColor);
    }

    //Назначение сцены
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //Выбор файла
    public void OpenFile() {
        if (FactoryFile.isSelected())
            try {
                File file = ChooseOpenFile(stage);
                if (file == null)
                    return;
                FileInputStream fileInputStream = new FileInputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
                for (int i = 2; i < workbook.getSheetAt(0).getPhysicalNumberOfRows(); i++) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    String code = workbook.getSheetAt(0).getRow(i).getCell(2).getStringCellValue();
                    for (int j = 0; j < code.length(); j++)
                        if (code.charAt(j) == ' ') {
                            arrayList.add(RemoveSpaces(code.substring(0, j)));
                            break;
                        }
                    arrayList.add(RemoveSpaces(workbook.getSheetAt(0).getRow(i).getCell(3).getStringCellValue()).toUpperCase());
                    arrayList.add(RemoveSpaces(workbook.getSheetAt(0).getRow(i).getCell(4).getStringCellValue()).toUpperCase());
                    if (workbook.getSheetAt(0).getRow(i).getCell(6).getCellTypeEnum() == CellType.NUMERIC)
                        arrayList.add(Integer.toString((int) workbook.getSheetAt(0).getRow(i).getCell(6).getNumericCellValue()));
                    else
                        arrayList.add(RemoveSpaces(workbook.getSheetAt(0).getRow(i).getCell(6).getStringCellValue()).toUpperCase());
                    int count = (int) workbook.getSheetAt(0).getRow(i).getCell(7).getNumericCellValue();
                    arrayList.add(String.valueOf(workbook.getSheetAt(0).getRow(i).getCell(8).getNumericCellValue()));
                    String barcode;
                    if (workbook.getSheetAt(0).getRow(i).getCell(9).getCellTypeEnum() == CellType.NUMERIC)
                        barcode = Long.toString((long) workbook.getSheetAt(0).getRow(i).getCell(9).getNumericCellValue());
                    else
                        barcode = RemoveSpaces(workbook.getSheetAt(0).getRow(i).getCell(9).getStringCellValue());
                    if (!base.containsKey(barcode))
                        base.put(barcode, arrayList);
                    for (int j = 0; j < count; j++)
                        excess.add(barcode);
                }

                Barcode.setDisable(false);
                OpenButton.setDisable(true);
                FactoryFile.setDisable(true);
                DepartureButton.setDisable(true);
                ViewDataButton.setDisable(false);
                SaveFile.setDisable(false);
                Load.setText("В базе: " + excess.size());
            } catch (Exception e) {
                base.clear();
                excess.clear();
                ShowAlert(stage, "Не удалось получить базу-двнных!");
            }
        else
            try {
                File file = ChooseOpenFile(stage);
                if (file == null)
                    return;
                FileInputStream fileInputStream = new FileInputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
                for (int i = 0; i < workbook.getSheetAt(0).getPhysicalNumberOfRows(); i++) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    String barcode = RemoveSpaces(workbook.getSheetAt(0).getRow(i).getCell(0).getStringCellValue());
                    if (barcode.equals(""))
                        continue;
                    arrayList.add(RemoveSpaces(workbook.getSheetAt(0).getRow(i).getCell(1).getStringCellValue()));
                    arrayList.add(RemoveSpaces(workbook.getSheetAt(0).getRow(i).getCell(2).getStringCellValue()).toUpperCase());
                    arrayList.add(RemoveSpaces(workbook.getSheetAt(0).getRow(i).getCell(3).getStringCellValue()).toUpperCase());
                    arrayList.add(RemoveSpaces(workbook.getSheetAt(0).getRow(i).getCell(4).getStringCellValue()).toUpperCase());
                    priceContains = workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells() == 6;
                    if (priceContains)
                        arrayList.add(RemoveSpaces(workbook.getSheetAt(0).getRow(i).getCell(5).getStringCellValue()));
                    if (!base.containsKey(barcode))
                        base.put(barcode, arrayList);
                    excess.add(barcode);
                }

                Barcode.setDisable(false);
                OpenButton.setDisable(true);
                FactoryFile.setDisable(true);
                DepartureButton.setDisable(true);
                ViewDataButton.setDisable(false);
                SaveFile.setDisable(false);
                Load.setText("В базе: " + excess.size());
            } catch (Exception e) {
                base.clear();
                excess.clear();
                ShowAlert(stage, "Не удалось получить базу-двнных!");
            }
        if (!(base.size() > 0) || !(excess.size() > 0)) {
            ShowAlert(stage, "Не удалось получить ни одного объекта для базы-данных!");
            ResetDefault();
        }
    }

    //Запись в файл
    public void SaveFile() {
        try {
            File file = ChooseSaveFile(stage);
            if (file == null)
                return;
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            HSSFWorkbook HSSFWorkbook = new HSSFWorkbook();
            HSSFSheet sheetAccept = HSSFWorkbook.createSheet("Принято");
            Save(sheetAccept, base, accept);
            HSSFSheet sheetNotFound = HSSFWorkbook.createSheet("Нет в базе");
            Save(sheetNotFound, base, notFound);
            HSSFSheet sheetExcess = HSSFWorkbook.createSheet("Лишнее");
            Save(sheetExcess, base, excess);
            HSSFWorkbook.write(fileOutputStream);
            HSSFWorkbook.close();
            fileOutputStream.close();
            ResetDefault();
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось записать данные в файл!");
        }
    }

    //Установка всего по умолчанию
    private void ResetDefault() {
        Load.setText("В базе: 0");
        Read.setText("Считано: 0");
        Barcode.setDisable(true);
        Barcode.clear();
        Code.setDisable(true);
        Code.clear();
        Type.setDisable(true);
        Type.clear();
        Color.setDisable(true);
        Color.clear();
        Size.setDisable(true);
        Size.setValue("");
        OpenButton.setDisable(false);
        OpenButton.setFocusTraversable(false);
        SaveFile.setDisable(true);
        AddButton.setDisable(true);
        DepartureButton.setDisable(false);
        DepartureButton.setFocusTraversable(false);
        FactoryFile.setDisable(false);
        FactoryFile.setSelected(false);
        FactoryFile.setFocusTraversable(false);
        RightCode.setSelected(false);
        RightType.setSelected(false);
        RightColor.setSelected(false);
        RightSize.setSelected(false);
        ViewDataButton.setDisable(true);
        base.clear();
        accept.clear();
        excess.clear();
        notFound.clear();
    }

    //Чтения штрих-кода
    public void ReadBarcode(KeyEvent keyEvent) {
        try {
            if (keyEvent.getCode().equals(KeyCode.ENTER) && !Barcode.getText().equals("")) {
                Code.clear();
                Type.clear();
                Color.clear();
                Size.setValue("");
                String barcode = Barcode.getText();
                if (base.containsKey(barcode)) {
                    if (excess.contains(barcode)) {
                        excess.remove(barcode);
                        accept.add(barcode);
                    } else
                        notFound.add(barcode);
                    Barcode.clear();
                    Read.setText("Считано: " + (accept.size() + notFound.size()));
                    Code.setText(base.get(barcode).get(0));
                    Type.setText(base.get(barcode).get(1));
                    Color.setText(base.get(barcode).get(2));
                    Size.setValue(base.get(barcode).get(3));
                    PlaySignal("Signal");
                } else {
                    Code.setDisable(false);
                    Type.setDisable(false);
                    Color.setDisable(false);
                    Size.setDisable(false);
                    Barcode.setDisable(true);
                    SaveFile.setDisable(true);
                    PlaySignal("Warning");
                }
                DepartureButton.setDisable(true);
            }
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось проврить штрих-код!");
        }
    }

    //Заполнение нового штрих-кода
    public void AddBarcode(){
        try {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(RemoveSpaces(Code.getText()));
            arrayList.add(RemoveSpaces(Type.getText().toUpperCase()));
            arrayList.add(RemoveSpaces(Color.getText().toUpperCase()));
            arrayList.add(RemoveSpaces(Size.getValue().toString().toUpperCase()));
            for (Map.Entry<String, ArrayList<String>> entry : base.entrySet()) {
                if (entry.getValue().get(0).equals(RemoveSpaces(Code.getText()))) {
                    if (entry.getValue().size() != 5)
                        break;
                    arrayList.add(entry.getValue().get(4));
                    break;
                }
            }
            base.put(Barcode.getText(), arrayList);
            notFound.add(Barcode.getText());

            Read.setText("Считано: " + (accept.size() + notFound.size()));
            Code.setDisable(true);
            Type.setDisable(true);
            Color.setDisable(true);
            Size.setDisable(true);
            AddButton.setDisable(true);
            ViewDataButton.setFocusTraversable(false);
            Barcode.setDisable(false);
            Barcode.clear();
            Barcode.setFocusTraversable(true);
            SaveFile.setDisable(false);
            RightCode.setSelected(false);
            RightType.setSelected(false);
            RightColor.setSelected(false);
            RightSize.setSelected(false);
            SaveFile.setDisable(false);
            PlaySignal("Signal");
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось добавить новый штрих-код!");
        }
    }

    //Переключение в режим отправки
    public void Departure() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxmls/Departure.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 400, 300));
            Departure controller = loader.getController();
            controller.setStage(stage);
            stage.show();
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось переключится в режим отправки!");
        }
    }

    //Переключение в режим просмотра
    public void ViewData() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxmls/ArrivalShowData.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 900, 400));
            ArrivalShowData controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Просмотр данных");
            stage.setFullScreen(false);
            stage.setMinHeight(200);
            stage.setMinWidth(900);
            stage.setMaxWidth(900);
            initModality(stage, this.stage);
            stage.showAndWait();
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось переключится в режим просмотра!");
        }
    }

    //Проверка на заполнение

    public void CodeEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.TAB))
            RightCode.setSelected(true);
        else
            RightCode.setSelected(false);
        if (Code.getText().isEmpty())
            RightCode.setSelected(false);

    }

    public void TypeEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.TAB))
            RightType.setSelected(true);
        else
            RightType.setSelected(false);
        if (Type.getText().isEmpty())
            RightType.setSelected(false);
        allRight();
    }

    public void ColorEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.TAB))
            RightColor.setSelected(true);
        else
            RightColor.setSelected(false);
        if (Color.getText().isEmpty())
            RightColor.setSelected(false);
        allRight();
    }

    public void SizeEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.TAB))
            RightSize.setSelected(true);
        else
            RightSize.setSelected(false);
        if (Size.getValue().equals(""))
            RightSize.setSelected(false);
        allRight();
    }

    public void SizeAction() {
        if (!Size.isDisable()) {
            RightSize.setSelected(true);
            allRight();
        }
    }

    private void allRight() {
        if (RightCode.isSelected() && RightType.isSelected() && RightColor.isSelected() && RightSize.isSelected())
            AddButton.setDisable(false);
        else
            AddButton.setDisable(true);
    }
}
