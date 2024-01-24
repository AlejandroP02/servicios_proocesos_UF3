package multicast;

public class Palabra {

    String[] palabras;
    public Palabra() {
        palabras = new String[]{"kevin", "chino", "kai", "comeflorez", "polrex"};
    }

    public String getPalabra() {
        return palabras[(int)(Math.random()*5)];
    }


}
