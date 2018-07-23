
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.lang.Object;

public class UtilityMethod {


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

    public static List<Double[]> obtainCarbon(File file, String version, String atom){


          List<Double[]> alphaCarbon = new ArrayList<>();
          int index=0;



          if(!split(file.getName(),3,2).equals("txt")){

              System.out.println("ritorno null preche l'enstensione è diversa da txt");
              return null;

          }else {
              Scanner sc = null;
              try {
                  sc = new Scanner(file);
              } catch (FileNotFoundException e) {
                  e.printStackTrace();
              }
              boolean column13;

                String firstcomplexline1 =  sc.next()+sc.next();

                sc.next();sc.next();

                if (sc.next().equals("1"))column13 = false;
                else column13 = true;


               String secondcomplexline1;


                if(version.equals("Z")) {

                    do {

                        sc.nextLine();
                        secondcomplexline1 = sc.next()+sc.next();


                    } while (!secondcomplexline1.equals(firstcomplexline1));

                }

               sc.nextLine();

                while(sc.hasNext()){

                    sc.next();sc.next();

                    String carbonType = sc.next();

                    if (carbonType.equals(atom)) {

                        sc.next();sc.next();

                        if (column13) sc.next();

                        Double[] coordinate = new Double[3];

                        coordinate[0] = Double.valueOf(sc.next());
                        coordinate[1] = Double.valueOf(sc.next());
                        coordinate[2] = Double.valueOf(sc.next());

                        alphaCarbon.add(coordinate);

                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();


                    }else{

                        sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();

                        if (column13) sc.next();

                    }
                }


          }

            return alphaCarbon;

    }


    public static String split(String splitstring,int numsplit,int split_take){

        String [] splitted = new String[numsplit+1];

        for( int i = 0; i<numsplit+1; i++){

            splitted[i] = "";

        }

        int j = 0;


        for(int i = 0; i< splitstring.length(); i++){

            if(splitstring.charAt(i) == '.')j++;
            else splitted[j] = splitted[j]+String.valueOf(splitstring.charAt(i));

        }

        return splitted[split_take];


    }


    public static  List<Complex> sort (List<Complex> toOrder){

        List<Complex> toOrder1 = toOrder;

        boolean control;

        do{

            control = false;

            for(int i = 1; i<toOrder1.size(); i++){

                if (Integer.parseInt(toOrder1.get(i).getComplexname()) < Integer.parseInt(toOrder1.get(i - 1).getComplexname())) {

                    Complex tmp = toOrder1.get(i);

                    toOrder1.set(i, toOrder1.get(i - 1));
                    toOrder1.set(i - 1, tmp);
                    control = true;


                }

            }

        }while(control);

        return toOrder1;

    }

    public static List<Complex> filterCostraint(List<Complex> complexList, String costraint){

        if(costraint.isEmpty()){return null;}

        List<Complex> filterList = new ArrayList<>();

        List<Costraint> costraintList = new ArrayList<>();

        char [] costchar = costraint.toCharArray();

        int i = 0;
        int j = 0;

        String costraintnumber = "";


        while(i<costchar.length){

            if(costchar[i]=='\n'){


                costraintList.add(j,UtilityMethod.splitFiler(costraintnumber,costraintnumber.length()));
                costraintnumber="";
                j++;
                i++;


            }
            else{

                costraintnumber = costraintnumber+costchar[i];
                i++;

            }

        }

        if (i == costchar.length){costraintList.add(j,UtilityMethod.splitFiler(costraintnumber,costraintnumber.length()));}


        for(int k = 0; k<costraintList.size(); k++){

            System.out.println(costraintList.get(k).aminoacid +" "+costraintList.get(k).position+" "+costraintList.get(k).atom);

        }




        boolean tcol = true;

        int complesso = 0;

        for(Complex co : complexList){

            List<Double []> costraintcoordinate1 = new ArrayList<>();
            List<Double []> costraintcoordinate2 = new ArrayList<>();

            complesso++;

            Scanner sc = null;
            try {
                sc = new Scanner(co.getFile());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            boolean column13;

            String firstcomplexline1 =  sc.next()+sc.next();

            sc.next();sc.next();

            if (sc.next().equals("1"))column13 = false;
            else column13 = true;


            String secondcomplexline1;

            sc.nextLine();

            int costraintcounter = 0;

            boolean finish = false;

            int carbon = 0;

            boolean cat2 = true;



            while(sc.hasNext() && !finish){

                boolean req = true;
                sc.next();sc.next();

                String carbonType = sc.next();

                if(costraintcounter<costraintList.size()) {

                    //System.out.println("carbo = "+carbon+" costraintcounter = "+costraintcounter +" 1cat");
                    if (carbonType.equals(costraintList.get(costraintcounter).atom)) {

                        String aa = sc.next();

                        if (!aa.equals(costraintList.get(costraintcounter).aminoacid)) {
                            req = false;
                        }

                        if (column13) sc.next();


                        String pos = sc.next();

                        if (Integer.parseInt(pos) != costraintList.get(costraintcounter).position) {
                            req = false;
                        }

                        Double[] coordinate = new Double[3];

                        coordinate[0] = Double.valueOf(sc.next());
                        coordinate[1] = Double.valueOf(sc.next());
                        coordinate[2] = Double.valueOf(sc.next());

                        if (req) {
                            costraintcoordinate1.add(coordinate);
                            costraintcounter++;
                        }

                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();

                        carbon++;


                    } else {

                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();

                        carbon++;

                        if (column13) sc.next();

                    }

                }else{

                    if(cat2){ carbon = 0; cat2=false;}
                    carbon++;
                    //System.out.println("carbo = "+carbon+"costlist.size ="+costraintList.size()+" costraintcounter = "+costraintcounter+" 2cat");
                    if (carbonType.equals(costraintList.get(costraintcounter-costraintList.size()).atom)) {

                        String aa = sc.next();

                        if (!aa.equals(costraintList.get(costraintcounter-costraintList.size()).aminoacid)) {
                            req = false;
                        }

                        if (column13) sc.next();


                        String pos = sc.next();

                        if (Integer.parseInt(pos) != costraintList.get(costraintcounter-costraintList.size()).position) {
                            req = false;
                        }

                        Double[] coordinate = new Double[3];

                        coordinate[0] = Double.valueOf(sc.next());
                        coordinate[1] = Double.valueOf(sc.next());
                        coordinate[2] = Double.valueOf(sc.next());

                        if (req) {
                            costraintcoordinate2.add(coordinate);
                            costraintcounter++;
                            if (costraintcounter == (costraintList.size()*2)) finish = true;
                        }

                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();


                    } else {

                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();
                        sc.next();

                        if (column13) sc.next();

                    }

                }

            }


            boolean isincostraint = true;

            for(int k = 0; k<costraintList.size(); k++) {


                Double dist = Math.sqrt(Math.pow( costraintcoordinate1.get(k)[0]-costraintcoordinate2.get(k)[0] ,2 ) +
                        Math.pow( costraintcoordinate1.get(k)[1]-costraintcoordinate2.get(k)[1] ,2 ) +
                        Math.pow( costraintcoordinate1.get(k)[2]-costraintcoordinate2.get(k)[2] ,2 ));

                if(dist < (costraintList.get(k).distance-costraintList.get(k).limit) || dist > (costraintList.get(k).distance+costraintList.get(k).limit)){

                    isincostraint = false;

                }

            }

            if(isincostraint){

                filterList.add(co);

            }


        }


        return filterList;


    }


    public static Costraint splitFiler(String s, int l){


        String [] splitted = new String[5];

        for(int i = 0; i<5; i++){

            splitted[i] = "";

        }

        String aminoacid = "";
        int position = 0;
        String atom = "";
        double distance = 0;
        double limit = 0;

        int j = 0;
        int i = 0;

        char [] charstring = s.toCharArray();


        while(i<l){

            if(charstring[i] == ' '){

                j++;
                i++;

            }else {

                splitted[j] = splitted[j] + charstring[i];
                i++;

            }

        }

        Costraint cos = new Costraint(splitted[0],Integer.parseInt(splitted[1]),splitted[2],Double.parseDouble(splitted[3]),Double.parseDouble(splitted[4]));

        return cos;


    }
}
