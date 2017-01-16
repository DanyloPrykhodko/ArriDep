package TextFields;

public class MultiLetterTextField extends javafx.scene.control.TextField {
    @Override
    public void replaceText(int i, int j, String string){
        if (string.matches("[a-zA-Zа-яА-Я ]") || string.isEmpty())
            super.replaceText(i, j, string.toUpperCase());
    }

    @Override
    public void replaceSelection(String string){
        super.replaceSelection(string);
    }
}
