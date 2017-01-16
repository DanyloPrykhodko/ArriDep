package Classes;

public class Point {
    private String name;        //Имя точки
    private int count = 0;      //Надо отправить
    private int accept = 0;     //Всего оправленно

    public Point(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }
}
