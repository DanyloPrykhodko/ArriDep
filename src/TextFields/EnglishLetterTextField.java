package TextFields;

import javafx.scene.control.TextField;

public class EnglishLetterTextField extends TextField {
    @Override
    public void replaceText(int i, int j, String string){
        if (string.matches("[a-zA-Z ]") || string.isEmpty())
            super.replaceText(i, j, string.toUpperCase());
    }

    @Override
    public void replaceSelection(String string){
        super.replaceSelection(string);
    }
}
