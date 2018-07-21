import javax.swing.*;
import java.awt.*;

public class ShowTable extends JFrame {


    private JPanel mainPanel;
    private JScrollPane scrollTable;
    private JTable table;


    public ShowTable(String [][] data, String [] nomicolonne) {


        super("Tabella distanze");

        this.mainPanel.setLayout(new GridLayout());

        this.setContentPane(mainPanel);


        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        table = new JTable(data,nomicolonne);
        table.setPreferredScrollableViewportSize(new Dimension(1000,800));

        this.scrollTable = new JScrollPane(table);

        this.mainPanel.add(scrollTable);


        this.pack();

        this.setLocationRelativeTo(null);

        this.setVisible(true);





    }



}
