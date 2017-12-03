package tikape.runko.domain;

public class SmoothieRaakaAine implements Comparable<SmoothieRaakaAine> {

    private RaakaAine raakaAine;
    private Integer jarjestys;
    private Integer maara;
    private String ohje;

    public SmoothieRaakaAine(RaakaAine raakaAine, Integer jarjestys, Integer maara, String ohje) {
        this.raakaAine = raakaAine;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }

    public RaakaAine getRaakaAine() {
        return this.raakaAine;
    }
    public int getJarjestys() {
        return this.jarjestys;
    }
    public String getOhje() {
        return this.ohje;
    }

    public int getMaara() {
        return this.maara;
    }

    @Override
    public int compareTo(SmoothieRaakaAine a) {
        if (this.maara != a.maara) {
            return this.maara - a.maara;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "SmoothieRaakaAine{" + "raakaAine=" + raakaAine + ", jarjestys=" + jarjestys + ", maara=" + maara + ", ohje=" + ohje + '}';
    }
    
}
