import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Complex {

    private File complexfile;
    private String complexname;
    private List<Double[]> alphaCarbon = new ArrayList<>();


    public Complex(File complexfile, String complexname){

        this.complexname = UtilityMethod.split(complexname,2,1);
        this.complexfile = complexfile;

    }

    public File getFile(){ return this.complexfile;}
    public String getComplexname(){ return this.complexname;}
    public void setAlphaCarbon(List<Double[]> alphaCarbon){this.alphaCarbon = alphaCarbon;}
    public List<Double[]> getAlphaCarbon(){ return this.alphaCarbon;}




}