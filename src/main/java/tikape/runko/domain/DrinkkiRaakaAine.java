package tikape.runko.domain;

public class DrinkkiRaakaAine implements Comparable<DrinkkiRaakaAine> {

    private String aineNimi;
    private Integer drinkkiId;
    private Integer raakaAineId;
    Integer jarjestys;
    String maara;
    String ohje;
    

    public DrinkkiRaakaAine(String aineNimi, Integer drinkkiId, Integer raakaAineId, Integer jarjestys, String maara, String ohje) {
        this.aineNimi = aineNimi;
        this.drinkkiId = drinkkiId;
        this.raakaAineId = raakaAineId;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }

    public String aineNimi() {
        return aineNimi;
    }

    public void setAineNimi(String nimi) {
        this.aineNimi = nimi;
    }

    public Integer getDrinkkiId() {
        return drinkkiId;
    }

    public void setDrinkkiId(Integer drinkkiId) {
        this.drinkkiId = drinkkiId;
    }

    public Integer getRaakaAineId() {
        return raakaAineId;
    }

    public void setRaakaAineId(Integer raakaAineId) {
        this.raakaAineId = raakaAineId;
    }

    public Integer getJarjestys() {
        return jarjestys;
    }

    public void setJarjestys(Integer jarjestys) {
        this.jarjestys = jarjestys;
    }

    public String getMaara() {
        return maara;
    }

    public void setMaara(String maara) {
        this.maara = maara;
    }

    public String getOhje() {
        return ohje;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }


    @Override
    public int compareTo(DrinkkiRaakaAine o) {
        if (this.jarjestys == o.getJarjestys()) {
	    return 0;
	} else if (this.jarjestys > o.getJarjestys()) {
	    return 1;
	} else {
	    return -1;
	}
    }
    
    public String toString() {
        String ohjeet = "";
        if (!this.ohje.equals("")) {
            ohjeet = ": "+ ohje;
        }
        return aineNimi + ", " + maara + ohjeet;
    }
    
    


}
