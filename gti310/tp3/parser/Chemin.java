package gti310.tp3.parser;

public class Chemin implements Comparable<Chemin> {

    private int depart;
    private int arrivee;
    private int distance;

    public Chemin(int depart, int arrivee, int distance) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.distance = distance;
    }


    public int getDepart() {
        return depart;
    }

    public int getArrivee() {
        return arrivee;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    @NotNull
    public int compareTo(Chemin chemin) {
        final int SMALLER = -1;
        final int EQUAL = 0;
        final int BIGGER = 1;
        if (this.getPointSource() < chemin.getPointSource()) {

            return SMALLER;
        } else if (this.getPointSource() > chemin.getPointSource()) {
            return BIGGER;
        }

        return EQUAL;
    }
}

