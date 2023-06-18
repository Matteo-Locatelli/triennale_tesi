package com.example.disparities;

public class singleResult {

    public static int depth;	// da 0 a maxDIsparity
    public static int seen;		// variabile che tiene conto del numero di volte che è stata vista tale depth
    public static final String sep = "/";
    public static int requested;	// variabile che tiene conto del numero di volte che è stata chiesta tale depth

    public singleResult (int depth, int seen, int requested) {
        this.depth = depth;
        this.seen = seen;
        this.requested = requested;
    }

    public String toString() {
        return "Disparità " + this.depth + ": " + this.seen + sep + this.requested + " ;";
    }

}
