package Classes;

import java.util.ArrayList;

public class Unit {
    private String barcode = "";    //Штрихкод
    private String code = "";       //Артикул
    private String type = "";       //Тип
    private String color = "";      //Цвет
    private String size = "";       //Размер
    private String price = "";      //Цена
    private String point = "";      //Точка

    public Unit(String barcode, ArrayList<String> arrayList) {
        this.barcode = barcode;
        this.code = arrayList.get(0);
        this.type = arrayList.get(1);
        this.color = arrayList.get(2);
        this.size = arrayList.get(3);
        if (arrayList.size() == 5)
            this.price = arrayList.get(4);
    }

    public Unit(String barcode, ArrayList<String> arrayList, String point) {
        this.barcode = barcode;
        this.code = arrayList.get(0);
        this.type = arrayList.get(1);
        this.color = arrayList.get(2);
        this.size = arrayList.get(3);
        if (arrayList.size() == 5)
            this.price = arrayList.get(4);
        this.point = point;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
