package com.google.engedu.ghost;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private static final String TAG = "TAG";
    private boolean cpuFirst;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        String word = null;
        if(prefix.equals("")){
            Random rand = new Random();
            return words.get(rand.nextInt(words.size()));
        } else { // binary search
            word = binSearchString(prefix);
            Log.d(TAG, "Result: " + word);
        }
        return word;
    }

    public String binSearchString(String target){
        Log.d(TAG, "Searching for " + target);
        return binSearchStringHelper(0, words.size() - 1, target);
    }

    public String binSearchStringHelper(int first, int last, String target){
        String word = null;
        if(first < last){
            int midIndex = (first + last) / 2;
            String mid = words.get(midIndex);
            if(mid.length() >= target.length()){
                mid = mid.substring(0, target.length());
            }
            Log.d(TAG, "" + first + " " + last);
            Log.d(TAG, "Mid: " + midIndex);
            if(mid.equalsIgnoreCase(target)){
                word = words.get(midIndex);
            } else {
                if(target.compareTo(mid) < 0){
                    word = binSearchStringHelper(first, midIndex - 1, target);
                } else {
                    word = binSearchStringHelper(midIndex + 1, last, target);
                }
            }
        }
        if(words.get((first + last) / 2).length() >= target.length() && words.get((first + last) / 2).substring(0, target.length()).equals(target)){
            word = words.get((first + last) / 2);
        }
        return word;
    }

    @Override
    public ArrayList<String> getGoodWordStartingWith(String prefix) {
        ArrayList<String> resultsEven = new ArrayList<>();
        ArrayList<String> resultsOdd = new ArrayList<>();
        ArrayList<String> selected = new ArrayList<>(2);
        if(prefix == ""){
            Random rand = new Random();
            String randomWord = words.get(rand.nextInt(words.size()));
            if(randomWord.length() % 2 == 0){
                resultsEven.add(randomWord);
            } else {
                resultsOdd.add(randomWord);
            }
        } else { // binary search
            binSearchStringList(prefix, resultsEven, resultsOdd);
        }

        for(int i = 0; i < resultsEven.size(); i++){
            Log.d(TAG, "EVEN: " +  resultsEven.get(i));
        }

        for(int j = 0; j < resultsOdd.size(); j++){
            Log.d(TAG,"ODD: " + resultsOdd.get(j));
        }

        Random rand = new Random();
        if(resultsEven.size() == 0){
            selected.add(0, null);
        } else {
            selected.add(0, resultsEven.get(rand.nextInt(resultsEven.size())));
        }
        if(resultsOdd.size() == 0){
            selected.add(1, null);
        } else {
            selected.add(1, resultsOdd.get(rand.nextInt(resultsOdd.size())));
            Log.d(TAG, "ODD ADDING: " + selected.get(1));
        }

//        if(resultsEven.size() != 0){
//            selected.add(0, resultsEven.get(rand.nextInt(resultsEven.size())));
//        }
//        if(resultsOdd.size() != 0){
//            selected.add(1, resultsOdd.get(rand.nextInt(resultsOdd.size())));
//        }



        return selected;
    }

    public void binSearchStringList(String target, ArrayList<String> even, ArrayList<String> odd){
        Log.d(TAG, "Searching for " + target);
        binSearchStringListHelper(0, words.size() - 1, target, even, odd);
    }

    public void binSearchStringListHelper(int first, int last, String target, ArrayList<String> even, ArrayList<String> odd){
        String word = null;
        if(first < last){
            int midIndex = (first + last) / 2;
            String mid = words.get(midIndex);
            if(mid.length() >= target.length()){
                mid = mid.substring(0, target.length());
            }
            Log.d(TAG, "Mid: " + mid);
            //System.out.println(mid);
            if(mid.equalsIgnoreCase(target)){
                String fullMid = words.get(midIndex);
                if(fullMid.length() % 2 == 0){
                    even.add(fullMid);
                } else {
                    odd.add(fullMid);
                }
            }
            if(mid.compareTo(target) > 0){
                binSearchStringListHelper(first, midIndex - 1, target, even, odd);
            } else {
                binSearchStringListHelper(midIndex + 1, last, target, even, odd);
            }
        }
        int midIndex = (first + last) / 2;
        if(words.get(midIndex).length() >= target.length() && words.get(midIndex).substring(0, target.length()).equals(target)){
            word = words.get(midIndex);
            if(word.length() % 2 == 0){
                even.add(word);
            } else {
                odd.add(word);
            }
        }
    }

}
