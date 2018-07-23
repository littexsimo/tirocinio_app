import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterfaceGUI extends JFrame{

    private JList complexList;
    private JButton apriFileButton;
    private JPanel mainPanel;
    private JCheckBox selezionaTuttiCheckBox;
    private JButton startCluster;
    private JTextArea errorTextArea;
    private JTextArea outputTextArea;
    private JTextField thresholdTextField;
    private JRadioButton mzdockRadioButton;
    private JRadioButton zdockRadioButton;
    private JButton calcolaDistanzeButton;
    private JScrollPane scrollpaneComplexList;
    private JTable distanceTabel;
    private JSplitPane splitPane;
    private JPanel tabelPannel;
    private JButton showTableButton;
    private JTextArea costraintTextArea;
    private JTextField cod_AAPos_AAAtomDistanceTextField;
    private JButton filtraComplessiPerIButton;
    private String pathFile;
    private List<Complex> fileList;
    private List<Complex> selectedComplex;
    private Double[][] distanceComplex;
    private String [][] data;
    private  String [] nomicolonne;
    private Histogram his = new Histogram();
    private boolean cdist = false;
    private int [] cluster;
    private ArrayList <Integer> clus;
    private JPopupMenu popupDistanceMethod = new JPopupMenu("Metodo per calcolare la distanza");
    private boolean rmsdChoice;
    private boolean averageChoice;
    private int index_showCa;
    private DefaultListModel<Complex> modelListComplex;



    public InterfaceGUI(String path){

        super("Interfaccia principale");

        this.setContentPane(mainPanel);

        Dimension preferredDimension = new Dimension(1000, 800);
        mainPanel.setPreferredSize(preferredDimension);

        splitPane.setOrientation(0);
        splitPane.setDividerLocation(300);

        errorTextArea.setForeground(Color.RED);
        this.outputTextArea.setEditable(false);

        thresholdTextField.setText("10.00");

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.pack();

        this.setLocationRelativeTo(null);

        this.setVisible(true);

        this.fileList = new ArrayList<>();
        this.selectedComplex = new ArrayList<>();

        JMenuItem RMSD = new JMenuItem("RMSD");
        JMenuItem average = new JMenuItem("media distanze Ca");

        RMSD.addActionListener(e -> {


            rmsdChoice = true;
            if (averageChoice) averageChoice = false;

            if(selectedComplex.size() <2){ errorTextArea.setText("Devi selezionare almeno due complessi"); cdist = false;}
            else {

                cdist = true;

                if (!zdockRadioButton.isSelected() && !mzdockRadioButton.isSelected())
                    errorTextArea.setText("Selezionare Mzdok o Zdock");
                else if (zdockRadioButton.isSelected()) obtainAlpha("Z");
                else if (mzdockRadioButton.isSelected()) obtainAlpha("M");
            }

        });

        average.addActionListener(e -> {


            averageChoice = true;
            if (rmsdChoice) rmsdChoice = false;

            if(selectedComplex.size() <2){ errorTextArea.setText("Devi selezionare almeno due complessi"); cdist = false;}
            else {

                cdist = true;

                if (!zdockRadioButton.isSelected() && !mzdockRadioButton.isSelected())
                    errorTextArea.setText("Selezionare Mzdok o Zdock");
                else if (zdockRadioButton.isSelected()) obtainAlpha("Z");
                else if (mzdockRadioButton.isSelected()) obtainAlpha("M");
            }

        });


        popupDistanceMethod.add(RMSD);
        popupDistanceMethod.add(average);


        apriFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                fileList.clear();

                pathFile = UtilityMethod.mYFileChooser(path);
                if(pathFile != null){

                    importComplex();

                }else{

                    errorTextArea.setText("La cartella scelta non è valida");

                }

            }
        });

        selezionaTuttiCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(selezionaTuttiCheckBox.isSelected()){

                    int start = 0;
                    int end = complexList.getModel().getSize() - 1;
                    if (end >= 0) {
                        complexList.setSelectionInterval(start, end);
                    }

                }

            }
        });
        startCluster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(cdist) {
                    createSequentialCluster();
                }else{

                    errorTextArea.setText("devi prima calcolare le distanze");

                }
            }
        });
        calcolaDistanzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                selectedComplex = complexList.getSelectedValuesList();
                popupDistanceMethod.show(calcolaDistanzeButton,5,5);

            }
        });
        mzdockRadioButton.addActionListener(e -> {

            if(zdockRadioButton.isSelected()) zdockRadioButton.setSelected(false);

        });
        zdockRadioButton.addActionListener(e -> {

            if(mzdockRadioButton.isSelected()) mzdockRadioButton.setSelected(false);

        });
        

        JPopupMenu popShowAlphaCarbo = new JPopupMenu("");
        JMenuItem showalphacarbon = new JMenuItem("mostra carboni alpha");
        popShowAlphaCarbo.add(showalphacarbon);



        showalphacarbon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(index_showCa >= 0){

                    Complex complesso = fileList.get(index_showCa);

                    outputTextArea.setText("LISTA DELLE COORDINATE DEI Ca DEL COMPLESSO "+complesso.getComplexname()+"\n\n");
                    List<Double []> carbonialpha = complesso.getAlphaCarbon();

                    for(int i = 0; i<complesso.getAlphaCarbon().size(); i++){

                        outputTextArea.append("Ca"+i+"\tX="+carbonialpha.get(i)[0]+"\tY="+carbonialpha.get(i)[1]+"\tZ="+carbonialpha.get(i)[2]+"\n");

                    }
                }

            }
        });

        complexList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                if(e.getButton() == MouseEvent.BUTTON3){

                    index_showCa = complexList.locationToIndex(e.getPoint());
                    popShowAlphaCarbo.show(complexList,e.getX(),e.getY());


                }
            }
        });
        filtraComplessiPerIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (fileList.size()>2){

                    String costraint = costraintTextArea.getText();

                    List<Complex> complexWithCostraint = UtilityMethod.filterCostraint(fileList,costraint);

                    modelListComplex.removeAllElements();

                    int i = 0;

                    for(Complex co : complexWithCostraint){

                        modelListComplex.add(i,co);
                        i++;

                    }

                    scrollpaneComplexList.repaint();
                    scrollpaneComplexList.revalidate();


                }else{

                    errorTextArea.setText("Devi prima aprire la cartella contenente i complessi");

                }

            }
        });
    }

    private void importComplex() {


        File actual = new File(pathFile);


        for(File f : actual.listFiles()){
            //System.out.println(f.getName());
            fileList.add( new Complex(f,f.getName()) );
        }


        fileList = UtilityMethod.sort(fileList);


        initList();
    }

    public void initList(){

        modelListComplex = new DefaultListModel<>();
        complexList.setModel(modelListComplex);
        complexList.setCellRenderer(new FileRenderer());


        complexList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if(super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                }
                else {
                    super.addSelectionInterval(index0, index1);
                }
            }
        });



        int i = 0;

        for(Complex complex : fileList) {

            modelListComplex.add(i,complex);
            i++;

        }

    }

    public void obtainAlpha(String version){



        boolean onetime = true;
        for (Complex co : selectedComplex){

            /*if(!UtilityMethod.split(co.getFile().getName(),3,2).equals("txt") && onetime) {

                ProcessBuilder pb = new ProcessBuilder("rename 's/\\.pdb$/\\.txt/' ./complessi/*.pdb");
                pb.inheritIO();
                pb.directory(co.getFile().getParentFile().getAbsoluteFile());
                try {
                    pb.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onetime=false;

            }*/

            List<Double[]> alphacarbon = null;

            alphacarbon = UtilityMethod.obtainCarbon(co.getFile(),version,"CA");

            if(alphacarbon!= null)co.setAlphaCarbon(alphacarbon);
            else errorTextArea.append("il file "+co.getFile().getName()+"non è di tipo txt rinomina pdb in txt\n");

        }

        countDistance();

    }

    public void countDistance(){

        distanceComplex = new Double[selectedComplex.size()][selectedComplex.size()];

        for(int i = 0; i<selectedComplex.size(); i++){
            for(int j = 0; j<selectedComplex.size(); j++){

                distanceComplex[i][j] = 0.00;

            }
        }




        if (averageChoice) calculateDistanceAverage();
        else if (rmsdChoice) calculateDistanceRMSD();

        stampaRisultatiDistanze();


    }

    private void calculateDistanceRMSD() {

        int dim = selectedComplex.size();
        int number_ca = selectedComplex.get(0).getAlphaCarbon().size();

        for(int i = 0; i<dim; i++){

            for( int j = i; j<dim; j++) {

                List<Double[]> ca_firstComplex = selectedComplex.get(i).getAlphaCarbon();
                List<Double[]> ca_secondComplex = selectedComplex.get(j).getAlphaCarbon();


                for( int z = 0; z<number_ca; z++){

                    distanceComplex[i][j] = (distanceComplex[i][j] +

                                    Math.pow( ca_secondComplex.get(z)[0]-ca_firstComplex.get(z)[0] ,2 ) +
                                    Math.pow( ca_secondComplex.get(z)[1]-ca_firstComplex.get(z)[1] ,2 ) +
                                    Math.pow( ca_secondComplex.get(z)[2]-ca_firstComplex.get(z)[2] ,2 ));

                }

                distanceComplex[i][j] = Math.sqrt(distanceComplex[i][j] / number_ca );

            }

        }



    }

    private void calculateDistanceAverage() {

        int dim = selectedComplex.size();
        int number_ca = selectedComplex.get(0).getAlphaCarbon().size();

        for(int i = 0; i<dim; i++){

            for( int j = i+1; j<dim; j++) {

                List<Double[]> ca_firstComplex = selectedComplex.get(i).getAlphaCarbon();
                List<Double[]> ca_secondComplex = selectedComplex.get(j).getAlphaCarbon();


                for( int z = 0; z<number_ca; z++){

                    distanceComplex[i][j] = distanceComplex[i][j] +

                            Math.sqrt(Math.pow( ca_secondComplex.get(z)[0]-ca_firstComplex.get(z)[0] ,2 ) +
                                    Math.pow( ca_secondComplex.get(z)[1]-ca_firstComplex.get(z)[1] ,2 ) +
                                    Math.pow( ca_secondComplex.get(z)[2]-ca_firstComplex.get(z)[2] ,2 ));


                }



                distanceComplex[i][j] = distanceComplex[i][j] /number_ca;
                //System.out.println("distanceComplex["+i+"]["+j+"] = "+distanceComplex[i][j]);

            }

        }



    }


    public void stampaRisultatiDistanze(){

        int dim = selectedComplex.size();

        nomicolonne = new String[dim+1];

        nomicolonne[0] = "";

        for(int i = 1; i<dim+1; i++){

            nomicolonne[i] = selectedComplex.get(i-1).getComplexname();

        }

        data = new String[dim+1][dim+1];

        outputTextArea.setText("");


        for (int i = 0; i<dim; i++){

            for( int j = 0; j<dim+1; j++){

                if(j == 0) data[i][j] = selectedComplex.get(i).getComplexname();

                else{
                    data[i][j] = new DecimalFormat("##.##").format(distanceComplex[i][j-1]);
                    //outputTextArea.append("distanze tra il complesso " + selectedComplex.get(i).getComplexname() + " e il complesso " + selectedComplex.get(j-1).getComplexname() + " = " + distanceComplex[i][j-1] + "\n");
                }
            }

        }




        for( int i = 0; i<dim; i++){

            for(int j = i+1; j<dim; j++){

                outputTextArea.append("distanze tra il complesso " + selectedComplex.get(i).getComplexname() + " e il complesso " + selectedComplex.get(j).getComplexname() + " = " + new DecimalFormat("##.###").format(distanceComplex[i][j]) + "\n");

            }

        }



        distanceTabel = new JTable(data,nomicolonne);
        distanceTabel.setPreferredScrollableViewportSize(new Dimension(200,200));
        distanceTabel.setFillsViewportHeight(true);

        distanceTabel.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        distanceTabel.getColumnModel().getColumn(0).setPreferredWidth(7);



        this.tabelPannel.setLayout(new BorderLayout());

        JScrollPane scrollTable = new JScrollPane(distanceTabel);

        this.tabelPannel.removeAll();
        this.tabelPannel.add(scrollTable);
        //this.tabelPannel.add(showTableButton);

        this.tabelPannel.repaint();
        this.tabelPannel.revalidate();



    }


    public void createSequentialCluster(){


        double threshold = Double.parseDouble(thresholdTextField.getText());

        clus=new ArrayList <>();
        cluster = new int[selectedComplex.size()];
        clus.add(0);
        cluster[0]= 0;


        for(int i = 1; i<selectedComplex.size(); i++){

            int min_i=0;
            double min=Double.MAX_VALUE;

            for(int j = 0; j< clus.size(); j++) {

                if(min > distanceComplex[clus.get(j)][i]){

                    //System.out.println("1="+1+" j="+j+" min="+min+" distancecomplex[clus.get(j)][i] ="+distanceComplex[clus.get(j)][i]);
                    min = distanceComplex[clus.get(j)][i];
                    min_i = j;

                }

            }

            //System.out.println("i="+i+" min="+min+" min_i="+min_i);

            if(min < threshold){

                //System.out.println("cluster[i] = cluster[clus[min_i]]"+"  clus.get(min_i) ="+clus.get(min_i));
                cluster[i] = cluster[clus.get(min_i)];

            }else{

                //System.out.println("aggiungo un cluster");
                clus.add(i);
                cluster[i] = i;

            }
        }

        stampaRisultatiCluster();

    }

    private void stampaRisultatiCluster(){

        outputTextArea.setText("");
        outputTextArea.append("\n ______CLUSTER TRA I COMPLESSI______ \n\n");

        for(int i = 0; i<selectedComplex.size(); i++){

            outputTextArea.append("complesso "+selectedComplex.get(i).getComplexname() + " cluster : "+selectedComplex.get(cluster[i]).getComplexname() +"\n");

        }

        int [] clusdensity = new int [selectedComplex.size()];
        Arrays.fill(clusdensity,0);

        for(int i = 0; i<selectedComplex.size(); i++){

            clusdensity[cluster[i]] = clusdensity[cluster[i]] +1;

        }

        /*for(int i = 0; i<selectedComplex.size(); i++){

            System.out.println("cluster "+selectedComplex.get(i).getComplexname()+" ha "+clusdensity[cluster[i]]);

        }*/





        int [] top_5 = new int[5];

        for(int i = 0; i<selectedComplex.size(); i++){


            if(i<5) {

                top_5[i] = i;

            }

            if(i == 4) {
                boolean control;

                do {


                    control = false;

                    for (int j = 0; j < 4; j++) {

                        if (clusdensity[top_5[j]] < clusdensity[top_5[j + 1]]) {

                            int change = top_5[j];
                            top_5[j] = top_5[j + 1];
                            top_5[j + 1] = change;
                            control = true;
                        }
                    }


                } while (control);

            }

            if(i > 4){


                if (clusdensity[i]>clusdensity[top_5[4]]){

                    top_5[4]= i;


                    for (int j = 4; j>0; j--){

                        if (clusdensity[top_5[j]] > clusdensity[top_5[j-1]]){

                            int change = top_5[j];

                            top_5[j] = top_5[j-1];
                            top_5[j-1] = change;


                        }else{break;}

                    }


                }

            }


        }
        outputTextArea.append("\n\nTOP 5 DENSITY CLUSTERS\n\n");

        for(int i = 0; i<5; i++){

            outputTextArea.append("il complesso "+selectedComplex.get(top_5[i]).getComplexname()+" ha "+clusdensity[top_5[i]]+" elementi simili a lui\n");

        }


        outputTextArea.append("\n");

        for(int i = 0; i<5; i++){

            outputTextArea.append("\n\nComplessi nel cluster avente medioide il complesso "+ selectedComplex.get(top_5[i]).getComplexname()+":\n");
            for(int j = 0; j<selectedComplex.size(); j++){

                if(cluster[j] == cluster[top_5[i]]){

                    outputTextArea.append("-Complesso"+selectedComplex.get(j).getComplexname()+"\n");

                }

            }

        }

    }




}
