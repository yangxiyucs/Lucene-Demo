package lucene;

import models.CollectionDocModel;
import models.RelevanceModel;

import java.util.ArrayList;

class calculation {

    /**
     * Calculates the recall and precision for the variables docs, queries,
     * relevs.
     */
    // queries to // the
    // documents
    // queries

    static float totalRecall;
    static float totalPrecision;
    static float totalAvp;
    long endTime;
    long startTime; // start timer
    ArrayList<CollectionDocModel> docs;
    ArrayList<CollectionDocModel> queries;
    ArrayList<RelevanceModel> relevs;
    ArrayList<RelevanceModel> queryHits;
    int i;
    static float map;// used to calculate the mean average precision
    float recall = 0;    // calculate recall
    float precision = 0;   // used to calculate precision
    int denominator = 0; // used to calculate precision

    int x = 0;
    float avp = 0;

    public calculation(ArrayList<CollectionDocModel> docs, ArrayList<CollectionDocModel> queries,
                       ArrayList<RelevanceModel> relevs, ArrayList<RelevanceModel> queryHits, int i) {
        this.docs = docs;
        this.queries = queries;
        this.relevs = relevs;
        this.queryHits = queryHits;
        this.i = i;
    }

    public void calculate() {
        startTime = System.nanoTime();
        int truePositive = 0;
        float sumAvp = 0;
        String value;

        // CharArraySet myStopSet =
        // CharArraySet.copy(StopAnalyzer.ENGLISH_STOP_WORDS_SET);

        /* calculate recall & precision */
        int x = 0;
        for (Integer j : queryHits.get(i).getDocsIDs()) { // for every
            // hit of
            // the
            // current
            // query
            x += 1;
            if (relevs.get(i).getDocsIDs().contains(j)) { // check if
                // the hit
                // is
                // relevant
                // (it is
                // relevant
                // if it in
                // the
                // relevs
                // ArrayList)

                recall += (float) 1 / relevs.get(i).getDocsIDs().size();
                truePositive++;
                avp = (float) truePositive / x;
                sumAvp += avp;
            }

            denominator++;

            //System.out.println(i + "HITS" + queryHits.get(i).getDocsIDs();+ "Original"+relevs.get(i).getDocsIDs());
        }
        if (truePositive != 0) {
            sumAvp = sumAvp / truePositive;
            totalAvp += sumAvp;
        } else {
            avp = 0;
        }
        denominator++;
        precision = (float) truePositive / denominator;
        // the current query
        totalRecall += recall;
        totalPrecision += precision;
        //map += precision / x;

        value = String.format("%1$-9s %2$-7s %3$4s %4$-10s %5$4s", "(Q " + (i + 1) + ")", "Recall:",
                (recall) + "", "    Precision :", (precision) + "   map :" + sumAvp);

        System.out.println(value); // print the recall and precision of
        // System.out.println(totalPrecision+" "+ totalRecall);
        endTime = System.nanoTime(); // end
        // timer


    }

    void printTotal() {
        /* total recall & precision */

        totalRecall /= (float) queries.size();
        totalPrecision /= (float) queries.size();
        totalAvp /= (float) queries.size();
        System.out.println("total Recall is " + totalRecall + "," + "totalPrecision is " + totalPrecision + ","
                + "  totalMap :" + totalAvp + (endTime - startTime));
    }


}



