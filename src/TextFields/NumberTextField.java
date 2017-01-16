package TextFields;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {
    @Override
    public void replaceText(int i, int j, String string){
        if (string.matches("[0-9]") || string.isEmpty())
            super.replaceText(i, j, string);
    }

    @Override
    public void replaceSelection(String string){
        super.replaceSelection(string);
    }
}
