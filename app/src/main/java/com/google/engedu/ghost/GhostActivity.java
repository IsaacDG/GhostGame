package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;

    private FastDictionary dictionaryFast;

    private boolean userTurn = false;
    private Random random = new Random();
    private String wordFragment = "";
    private TextView fragmentText;
    private boolean timeToRestart = false;
    private Button challengeButton;
    private boolean cpuWasFirst;
    static final String STATE_FRAGMENT = "wFragment";
    static final String STATE_STATUS = "gameStatus";
    static final String STATE_TURN = "userTurn";
    static final String STATE_BUTTON = "buttonStatus";
    static final String STATE_RESTART = "shouldRestart";
    private static final String TAG = "TAG";
    private static final String TRIE = "TRIE";
    private boolean buttonStat = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create");
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        fragmentText = (TextView) findViewById(R.id.ghostText);
        challengeButton = (Button) findViewById(R.id.challenge_button);

        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);

            InputStream inputStream2 = assetManager.open("words.txt");
            dictionaryFast = new FastDictionary(inputStream2);
            //Log.d(TRIE, "Is it a word?" + dictionaryFast.isWord("glasses"));

            if(savedInstanceState != null){
                Log.d(TAG, "Detected not null");
                wordFragment = savedInstanceState.getString(STATE_FRAGMENT);
                fragmentText.setText(wordFragment);
                TextView label = (TextView)findViewById(R.id.gameStatus);
                label.setText(savedInstanceState.getString(STATE_STATUS));
                userTurn = savedInstanceState.getBoolean(STATE_TURN);
                buttonStat = savedInstanceState.getBoolean(STATE_BUTTON);
                challengeButton.setEnabled(buttonStat);
                timeToRestart = savedInstanceState.getBoolean(STATE_RESTART);

            } else {
                Log.d(TRIE,"" + dictionaryFast.isWord("cune"));
                onStart(null);
            }
            //dictionary = new FastDictionary(inputStream);
            } catch (IOException e) {
                Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
                toast.show();
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(!timeToRestart && (keyCode >= 29 && keyCode <= 54)){
            wordFragment += (char)event.getUnicodeChar();
            updateText(wordFragment);
            //if(dictionary.isWord(wordFragment)){
            if(dictionaryFast.isWord(wordFragment)){
                TextView gameStat = (TextView)findViewById(R.id.gameStatus);
                //gameStat.setText("WORD");
            } else {
                TextView gameStat = (TextView)findViewById(R.id.gameStatus);
                //gameStat.setText("NO");
            }
            //goodComputerTurn();
            fastComputerTurn();
        } else {
            return super.onKeyUp(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        if(userTurn == false){
            cpuWasFirst = true;
        } else {
            cpuWasFirst = false;
        }
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        wordFragment = "";
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            //goodComputerTurn();
            goodComputerTurn();
            //fastComputerTurn();
        }
        return true;
    }

    public void resetClick(View view){
        timeToRestart = false;
        buttonStat = true;
        challengeButton.setEnabled(buttonStat);
        onStart(null);
    }

    private void goodComputerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (wordFragment.length() >= 4 && dictionary.isWord(wordFragment)) {
            label.setText("That was a word, CPU Wins!");
            gameOver();
        } else {
            //String longer = dictionary.getAnyWordStartingWith(wordFragment);


            ArrayList<String> possibleWords = (ArrayList<String>) dictionary.getGoodWordStartingWith(wordFragment);
            String longer;

            if(cpuWasFirst){
                longer = possibleWords.get(0);
                if(longer == null) {
                    longer = possibleWords.get(1);
                }
            } else {
                longer = possibleWords.get(1);
                if(longer == null){
                    longer = possibleWords.get(0);
                }
            }
            if(longer == null){
                label.setText("The CPU challenges you, that is not the beginning to a longer word. You lose!");
                gameOver();
            } else {
                String sub = longer.substring(wordFragment.length(), wordFragment.length() + 1);
                wordFragment += sub;
                fragmentText.setText(wordFragment);
            }



//            if (possibleWords.size() != 0) {
//                if (cpuWasFirst) {
//                    longer = possibleWords.get(0);
//                    if (longer == null) {
//                        longer = possibleWords.get(1);
//                    }
//                } else {
//                    longer = possibleWords.get(1);
//                    if (longer == null) {
//                        longer = possibleWords.get(0);
//                    }
//                }
//                //The CPU adds another letter to the fragment based on what it found
//                String sub = longer.substring(wordFragment.length(), wordFragment.length() + 1);
//                wordFragment += sub;
//                fragmentText.setText(wordFragment);
//            } else {
//                label.setText("The CPU challenges you, that is not the beginning to a longer word. You lose!");
//                gameOver();
//            }
//            // Do computer turn stuff then make it the user's turn again
//            userTurn = true;
//            //label.setText(USER_TURN);
        }
        userTurn = true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if(wordFragment.length() >= 4 && dictionary.isWord(wordFragment)){
                label.setText("That was a word, CPU Wins!");
                gameOver();
        } else {
            String longer = dictionary.getAnyWordStartingWith(wordFragment);

            //String longer = dictionary.getGoodWordStartingWith(wordFragment);
            if(longer == null){
                label.setText("The CPU challenges you, that is not the beginning to a longer word. You lose!");
                gameOver();
            } else {
                //The CPU adds another letter to the fragment based on what it found
                String sub = longer.substring(wordFragment.length(), wordFragment.length() + 1);
                wordFragment += sub;
                fragmentText.setText(wordFragment);
            }
        }
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        //label.setText(USER_TURN);
    }

    public void fastComputerTurn(){
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if(wordFragment.length() >= 4 && dictionaryFast.isWord(wordFragment)){
            label.setText("That was a word, CPU Wins!");
            gameOver();
        } else {
            //String longer = dictionary.getAnyWordStartingWith(wordFragment);
            String longer = dictionaryFast.getAnyWordStartingWith(wordFragment);
            Log.d(TAG, longer);

            //String longer = dictionary.getGoodWordStartingWith(wordFragment);
            if(longer == null){
                label.setText("The CPU challenges you, that is not the beginning to a longer word. You lose!");
                gameOver();
            } else {
                //The CPU adds another letter to the fragment based on what it found
                String sub = longer.substring(wordFragment.length(), wordFragment.length() + 1);
                wordFragment += sub;
                fragmentText.setText(wordFragment);
            }
        }
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        //label.setText(USER_TURN);
    }

    public void updateText(String fragment){
        fragmentText.setText(fragment);
    }

    public void challengeFast(View view){
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if(wordFragment.length() >= 4){
            if(dictionaryFast.isWord(wordFragment)){
                label.setText("You win!");
                gameOver();
            } else {
                String longer = dictionaryFast.getAnyWordStartingWith(wordFragment);
                if(longer != null){
                    label.setText("That is a valid prefix, CPU wins!");
                    gameOver();
                } else {
                    label.setText("That was not a valid prefix, you win!");
                    gameOver();
                }
            }

        }
    }

    public void challenge(View view){
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if(wordFragment.length() >= 4){
            if(dictionary.isWord(wordFragment)){
                label.setText("You win!");
                gameOver();
            } else {
                String longer = dictionary.getAnyWordStartingWith(wordFragment);
                if(longer != null){
                    label.setText("That is a valid prefix, CPU wins!");
                    gameOver();
                } else {
                    label.setText("That was not a valid prefix, you win!");
                    gameOver();
                }
            }

        }
    }

    public void gameOver(){
        timeToRestart = true;
        buttonStat = false;
        challengeButton.setEnabled(buttonStat);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        Log.d(TAG, "onSaveInstanceState");
        TextView label = (TextView)findViewById(R.id.gameStatus);
        savedInstanceState.putString(STATE_FRAGMENT, wordFragment);
        savedInstanceState.putString(STATE_STATUS, label.getText().toString());
        savedInstanceState.putBoolean(STATE_TURN, userTurn);
        savedInstanceState.putBoolean(STATE_BUTTON, buttonStat);
        savedInstanceState.putBoolean(STATE_RESTART, timeToRestart);

        super.onSaveInstanceState(savedInstanceState);
    }
}
