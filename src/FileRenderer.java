import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileRenderer extends DefaultListCellRenderer{

    public Component getListCellRendererComponent(
            JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        Complex complesso = (Complex) value;

        setText("Complesso "+complesso.getComplexname());

        return this;
    }

}
