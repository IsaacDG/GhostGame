package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;
    private static final String TRIE = "TRIE";
    private static final String ADDTRIE = "ADDTRIE";
    private static final String STR = "STR";

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {

        if(s.equals("") && !children.containsKey("")){
            children.put("", null);
            isWord = true;
        } else {
            String letter = s.substring(0, 1);
            String restOfWord = s.substring(1, s.length());
           // Log.d(ADDTRIE, "here add");
            if(!children.containsKey(letter)){
                //Log.d(ADDTRIE, "Adding letter: " + letter);
                children.put(letter, new TrieNode());
            }
            children.get(letter).add(restOfWord);
        }

    }

    public boolean isWord(String s) {
        if (s.equals("")) {
            return true;
        }


        //Log.d(TRIE, "here TrieNode");
        String currentLetter = s.substring(0, 1);
        String currentFragment = s.substring(1, s.length());

        if(children.containsKey(currentLetter)){
            //Log.d(TRIE, "Returning true for: " + currentLetter);
            return children.get(currentLetter).isWord(currentFragment);
        } else {
            return false;
        }
    }

    public String getAnyWordStartingWith(String s) {
//        if(s.equals("")){
//            if(children.size() == 0){
//                return null;
//            }
//           Random rand = new Random();
//            int index = rand.nextInt(children.size());
//            return s + children.get(index);
//        }
        Log.d(TRIE, "s = " + s);
        String currentLetter = s.substring(0, 1);
        String currentFragment = s.substring(1, s.length());
        Log.d(TRIE, "Letter is: " + currentLetter);
        String ret = "";

        if(currentFragment.equals("")){
            if(!children.containsKey(currentLetter)){
                return null;
            }

            boolean found = false;
            TrieNode cursor = children.get(currentLetter);
            while(!found) {
                Random rand = new Random();
                Object[] values = cursor.children.keySet().toArray();
                String index = (String) values[rand.nextInt(values.length)];
                ret += index;

                TrieNode cursorNext = cursor.children.get(index);
                if (cursorNext.children.containsKey("")) {
                    found = true;
                    Log.d(STR, "returning " + ret);
                    return ret;
                } else {
                    cursor = cursorNext;
                }
            }
        }

        if(children.containsKey(currentLetter)){
            String frag = children.get(currentLetter).getAnyWordStartingWith(currentFragment);
            Log.d(TRIE, "Returning " + frag);
            return s + frag;
        } else {
            return null;
        }
        //return null;
    }

    public void getLetters(String s){
        Log.d(TRIE, "s = " + s);
        String currentLetter = s.substring(0, 1);
        String currentFragment = s.substring(1, s.length());
        Log.d(TRIE, "Letter is: " + currentLetter);
        if(currentFragment.equals("")){
            TrieNode cursor = children.get(currentLetter);
            Random rand = new Random();
            Object[] values = cursor.children.keySet().toArray();
            String index = (String) values[rand.nextInt(values.length)];
            for(int i = 0; i < values.length; i++){
                Log.d(STR, (String)values[i]);
            }
        }
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
