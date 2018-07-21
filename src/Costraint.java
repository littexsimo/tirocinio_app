public class Costraint {

    public String aminoacid;
    public int position;
    public String atom;
    public double distance;
    public double limit;

    public Costraint(String aminoacid,int position,String atom,double distance,double limit){

        this.aminoacid = aminoacid;
        this.atom = atom;
        this.distance = distance;
        this.limit = limit;
        this.position = position;

    }


}
