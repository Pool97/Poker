package server.algorithm;

import java.util.ArrayList;

public class CommonClss {

    private boolean high;
    private ArrayList<Integer> index;
    private HandEvaluator tmpHigh;

    public CommonClss() {
    }

    public CommonClss(HandEvaluator tmpHigh, ArrayList<Integer> index) {
        this.tmpHigh = tmpHigh;
        this.index = index;
    }

    public CommonClss(boolean high, HandEvaluator tmpHigh ) {
        this.high = high;
        this.tmpHigh = tmpHigh;
    }

    public CommonClss(boolean high, ArrayList<Integer> index) {
        this.high = high;
        this.index = index;
    }

    public CommonClss(boolean high, ArrayList<Integer> index, HandEvaluator tmpHigh) {
        this.high = high;
        this.index = index;
        this.tmpHigh = tmpHigh;
    }

    public boolean isHigh() {
        return high;
    }

    public void setHigh(boolean high) {
        this.high = high;
    }

    public ArrayList<Integer> getIndex() {
        return index;
    }

    public void setIndex(ArrayList<Integer> index) {
        this.index = index;
    }

    public HandEvaluator getTmpHigh() {
        return tmpHigh;
    }

    public void setTmpHigh(HandEvaluator tmpHigh) {
        this.tmpHigh = tmpHigh;
    }
}
