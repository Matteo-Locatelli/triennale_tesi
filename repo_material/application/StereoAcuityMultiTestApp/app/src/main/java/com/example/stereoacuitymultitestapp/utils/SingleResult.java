package com.example.stereoacuitymultitestapp.utils;

import androidx.annotation.NonNull;

public class SingleResult {

    /** Parameters to biuld a SingleResult */
    public int depth;	// from 0 to maxDisparity
    public int seen;		// number of times that depth is seen
    public final String sep = "/";
    public int requested;	// number og times that depth is asked

    public SingleResult(int depth, int seen, int requested) {
        this.depth = depth;
        this.seen = seen;
        this.requested = requested;
    }

    @NonNull
    public String toString() {
        return "Disparità " + this.depth + " :" + this.seen + sep + this.requested ;
    }

}
