package Classes;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public abstract class Support {
    //Запись в лист
    public static void Save(HSSFSheet sheet, HashMap<String, ArrayList<String>> base, ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            String barcode = arrayList.get(i);
            HSSFRow row = sheet.createRow(i);
            HSSFCell cellBarcode = row.createCell(0);
            cellBarcode.setCellType(CellType.STRING);
            cellBarcode.setCellValue(barcode);
            for (int j = 0; j < 5 && j < base.get(barcode).size(); j++) {
                HSSFCell cellData = row.createCell(j + 1);
                cellData.setCellType(CellType.STRING);
                cellData.setCellValue(base.get(barcode).get(j));
            }
        }
    }

    //Блокировка других окон
    public static void initModality(Stage stage, Stage owner) {
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
    }

    //Воспроизвидение звуков
    public static void PlaySignal( String signal) {
        //Загружаем файл
        File file = new File(Support.class.getResource("/Sounds/" + signal + ".wav").toString());
        //Инициализируем и проигрываем звук
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(file.toString()));
        mediaPlayer.play();
    }

    //Вызов сообщения об ошибке
    public static void ShowAlert(Stage stage, String message) {
        //Инициализируем оповещение
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("Ошибка!");
        //Блокировка других окон
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(stage);
        alert.show();
        //Проигруем звук
        PlaySignal("Error");
    }

    //Убрать лишние пробелы
    public static String RemoveSpaces(String string) {
        //Заменяем мнежественные пробелы на один
        string = string.replaceAll("\\s+", " ");
        //Убрать первый пробел
        if (string.length() > 0 && Character.isSpaceChar(string.charAt(0)))
            string = string.substring(1, string.length());
        //Убрать последний пробел
        if (string.length() > 0 && Character.isSpaceChar(string.charAt(string.length() - 1)))
            string = string.substring(0, string.length() - 1);
        return string;
    }

    //Получить путь файла для открытия
    public static File ChooseOpenFile(Stage stage) {
        try {
            FileChooser fileChooser = new FileChooser();
            //Настраиваем фильтр
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel (*.xls)", "*.xls"));
            //Возвращаем путь файла для открытия
            return fileChooser.showOpenDialog(stage);
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось выбрать файл!");
            return null;
        }
    }

    //Получить путь файла для сохранения
    public static File ChooseSaveFile(Stage stage) {
        try {
            FileChooser fileChooser = new FileChooser();
            //Настраиваем фильтр
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xls"));
            //Задаем название файла
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
            String date = simpleDateFormat.format(new Date());
            fileChooser.setInitialFileName(date);
            //Возвращаем путь файла для сохранения
            return fileChooser.showSaveDialog(stage);
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось выбрать файл для сохранения!");
            return null;
        }
    }

    //Получить путь папки для сохранения
    public static File ChooseDirectory(Stage stage) {
        try {
            //Возвращаем выбранный путь папки
            return new DirectoryChooser().showDialog(stage);
        } catch (Exception e) {
            ShowAlert(stage, "Не удалось выбрать путь для сохранения!");
            return null;
        }
    }
}
