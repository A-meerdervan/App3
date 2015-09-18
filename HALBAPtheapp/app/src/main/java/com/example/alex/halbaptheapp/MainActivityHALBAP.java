package com.example.alex.halbaptheapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivityHALBAP extends AppCompatActivity {

    // This is the word to be quessed by the user
    public static String TheWord = "nogniets";
    // This is wat shows the user the posistions of the correct letters
    private char[] WordStatus = {};
    // there are 7 images of hangman, after start there are 6 remaining
    private int RemainingBodyParts = 6;
    // When the person lost this becomes true
    public static boolean IsDead = false;
    // letters that were entered so far
    private String EnteredLetters = "";

    //Own Methods
    // When a quess is entered
    public void quessWasEntered(View view){
        TextView tv = (TextView) findViewById(R.id.WordStatus);
        TextView ShowLetters = (TextView) findViewById(R.id.ActiveUsedLetters);
        EditText et = (EditText) findViewById(R.id.UserInput);
        String Quess = et.getText().toString();
        char[] QuessArray = Quess.toCharArray();
        if (Quess.length() == 1){
            // Add the quess to the shown letters
            EnteredLetters += Quess + ", ";
            ShowLetters.setText(EnteredLetters);
            // Check if the quess was right or not
            boolean LetterInWord = false;
            boolean LetterAtIndex = false;
            for (int i = 0; i < TheWord.length(); i++){
                LetterAtIndex = (TheWord.charAt(i)== QuessArray[0]);
                if (LetterAtIndex){
                    LetterInWord = true;
                    WordStatus[i] = QuessArray[0];
                }
            }
            // if user was wrong another body part must be hanged
            if (LetterInWord == false) {
                hangBodyPart();
            }
            else{
                Log.d("test2", "hierna volgt WordStatus:");
                String wordstatus = new String(WordStatus);
                Log.d("test1", wordstatus);
                Log.d("test", "komt in else die zegt dat de letter WEL in het woord zat");
                if(wordstatus.equals(TheWord)){
                    Log.d("test", "komt op de plek waar Wordstatus aan TheWord word gelijkgesteld.");
                    userWon();
                }
            }
        }
        // if the guess is multiple letters:
        else {
            if (Quess.equals(TheWord)){
                  userWon();
              }
             else {
                  hangBodyPart();
            }
        }
        // Clean input window
        char[] emptyArray = {};
        et.setText(emptyArray, 0, 0);
        // Show the user he was right or not
        if(!IsDead) tv.setText(WordStatus, 0, WordStatus.length);
    }
    // User was wrong, so Another body part must be hanged
    public void hangBodyPart() {
        ImageView HangMan = (ImageView) findViewById(R.id.HangMan);
        RemainingBodyParts -= 1;
        switch (RemainingBodyParts) {
            case 0:
                HangMan.setImageResource(R.drawable.hangman0);
                userLost();
                break;
            case 1:
                HangMan.setImageResource(R.drawable.hangman1);
                break;
            case 2:
                HangMan.setImageResource(R.drawable.hangman2);
                break;
            case 3:
                HangMan.setImageResource(R.drawable.hangman3);
                break;
            case 4:
                HangMan.setImageResource(R.drawable.hangman4);
                break;
            case 5:
                HangMan.setImageResource(R.drawable.hangman5);
                break;
        }
    }
    // The user lost the game, the full body is hanged
    public void userLost(){
        IsDead = true;
        Intent intent = new Intent(getApplicationContext(), WinLoseActivity.class);
        intent.putExtra("isdead", true);
        intent.putExtra("theword", TheWord);

        SharedPreferences prefs = this.getSharedPreferences("settings",this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("gamehasended", true);
        editor.commit();
        startActivity(intent);
        this.finish();
    }
    // The user won
    public void userWon(){
        Intent intent = new Intent(getApplicationContext(), WinLoseActivity.class);
        intent.putExtra("isdead", false);
        intent.putExtra("theword", TheWord);
        startActivity(intent);
        this.finish();
    }
    // When the user presses restart a function on top of resetGame is needed because the funtion must take
    // a View argument, but resetGame is also used at oncreate() and there is no view object
    public void restartGame(View view){
        resetGame();
    }
    // Reset the game when a person wants to play again
    public void resetGame(){
        IsDead = false;
        // Choose a word randomly for this game
        String[] WordsArray = getResources().getStringArray(R.array.WordsArray);
        int RandomIndex = (int )(Math.random() * (WordsArray.length - 1) + 1);
        TheWord = WordsArray[RandomIndex];
        // Fill the display with points where the letters will come, when quessed
        WordStatus = new char[TheWord.length()];
        for( int i = 0; i < TheWord.length(); i++){
            WordStatus[i] = '.';
        }
        TextView tv = (TextView)findViewById(R.id.WordStatus);
        tv.setText(WordStatus, 0, WordStatus.length);
        // clear the entered letters textview
        EnteredLetters = "";
        TextView Tv = (TextView)findViewById(R.id.ActiveUsedLetters);
        Tv.setText(EnteredLetters);
        // Reset remaining body parts
        RemainingBodyParts = 6;
        // Reset image hangman
        ImageView HangMan = (ImageView) findViewById(R.id.HangMan);
        HangMan.setImageResource(R.drawable.hangman6);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_halbap);
        resetGame();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = this.getSharedPreferences("settings",this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Log.d("test", "komt in OnStop...");
        editor.putInt("remainingbodyparts", RemainingBodyParts);
        editor.putString("enteredletters", EnteredLetters);
        Log.d("test", " dit is wordStatus to string: ");
        Log.d("test", WordStatus.toString());
        editor.putString("wordstatus", WordStatus.toString());
        editor.putString("theword", TheWord);
        editor.putBoolean("isdead", IsDead);

        editor.commit();
    }

    // This will save the data when the screen rotates
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("test", "komt in OnSaveInst...");
        super.onSaveInstanceState(outState);
        outState.putInt("remainingbodyparts", RemainingBodyParts);
        outState.putString("enteredletters", EnteredLetters);
        outState.putCharArray("wordstatus", WordStatus);
        outState.putString("theword", TheWord);
        outState.putBoolean("isdead",IsDead);

    }
    // When the screen is flipped and the activity is made restore variables
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("test", "komt in OnrestoreInst....");
        super.onRestoreInstanceState(savedInstanceState);
        RemainingBodyParts = savedInstanceState.getInt("remainingbodyparts");
        EnteredLetters = savedInstanceState.getString("enteredletters");
        WordStatus = savedInstanceState.getCharArray("wordstatus");
        TheWord = savedInstanceState.getString("theword");
        IsDead = savedInstanceState.getBoolean("isdead");
        TextView enteredLetters = (TextView)findViewById(R.id.ActiveUsedLetters);
        enteredLetters.setText(EnteredLetters);
        String test = new String(savedInstanceState.getCharArray("wordstatus"));
        Log.d("test11", "Hierna volgt teh WordStatus zoals ie m ontvangen heeft na de flip");
        Log.d("test", test);
        TextView status = (TextView)findViewById(R.id.WordStatus);
        Log.d("test", "komt na de status aanmaak");
        status.setText(WordStatus, 0, WordStatus.length);
        Log.d("test1", "hij komt na de setText  van WordStatus");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_halba, menu);
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
}
