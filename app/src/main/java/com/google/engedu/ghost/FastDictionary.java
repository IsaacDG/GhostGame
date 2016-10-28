package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class FastDictionary implements GhostDictionary {

    private TrieNode root;
    private static final String TRIE = "TRIE";
    private static final String TRIEADD = "TRIEADD";

    public FastDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        //Log.d(TRIE, "is this even happening");
        root = new TrieNode();
        String line = null;
        //Log.d(TRIE, "is this even happening");
        //Log.d(TRIE, "FAST DICTIONARY CONST: " + in.readLine());
        while((line = in.readLine()) != null) {
            String word = line.trim();
            //Log.d(TRIEADD, "Adding word: " + word);
            if (word.length() >= MIN_WORD_LENGTH)
                root.add(line.trim());
        }
    }
    @Override
    public boolean isWord(String word) {
        Log.d(TRIE, "here FastDictionary");
        return root.isWord(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
//        root.getLetters("a");
//        return null;
        return root.getAnyWordStartingWith(prefix);
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return root.getGoodWordStartingWith(prefix);
    }
}
