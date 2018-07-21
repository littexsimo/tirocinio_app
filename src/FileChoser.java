import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileChoser {


   public static String mYFileChooser(String path){

        JFileChooser fc = new JFileChooser(new java.io.File((path)));

        fc.setDialogTitle("seleziona la cartella dei complessi");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){

            return String.valueOf(fc.getCurrentDirectory());
            //System.out.println("selected filed:  " + fc.getSelectedFile().getName());

        }

        return null;

    }

}