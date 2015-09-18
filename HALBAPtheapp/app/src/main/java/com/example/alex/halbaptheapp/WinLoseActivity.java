package com.example.alex.halbaptheapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.content.res.ColorStateList.createFromXml;
import static com.example.alex.halbaptheapp.R.drawable.bloodspat;

public class WinLoseActivity extends AppCompatActivity {
    private String LoseText = "YOU LOSE!";
    private String WinText = "You are THE winner";
    private String LoseTextSmall = "Your stupidity made the entire body hang, the crows will be pleased with you";
    private String WinTextSmall = "You won!! how awesome of you";
    private String TheWord = "leeg woord";
    private boolean IsDead = false;
    // Own methods
    public void initializeScreen() {
        TextView Word = (TextView)findViewById(R.id.ShowTheWord);
        TextView WinLose = (TextView) findViewById(R.id.WinLose);
        TextView WinLoseSmall = (TextView) findViewById(R.id.WinLoseSmall);
        ImageView IV = (ImageView) findViewById(R.id.ImageWinLose);
        RelativeLayout RL = (RelativeLayout) findViewById(R.id.RelaLayoutWinLose);
        // user lost
        if (IsDead == true){
            WinLose.setText(LoseText);
            WinLoseSmall.setText(LoseTextSmall);
            IV.setImageResource(R.drawable.rageface);
            RL.setBackgroundResource(R.drawable.bloodspat);
        }
        // user won
        else{
            WinLose.setText(WinText);
            WinLoseSmall.setText(WinTextSmall);
            // make tekst black (white background)
            WinLose.setTextColor(Color.parseColor("#050505"));
            WinLoseSmall.setTextColor(Color.parseColor("#050505"));
            // Source: http://www.greetz.nl/webservice/products/images.png?productcode=gift_balloon_foil_smiley_grijs
            IV.setImageResource(R.drawable.wonsmiley);
            // Source: http://www.peggysuesdancestore.com/wp-content/uploads/2015/01/8-412644-Holiday-Happy-New-Year-background.jpg
            RL.setBackgroundResource(R.drawable.gewonnenbg);
        }
        // Show the word of this game
        Word.setText("The word was: " + TheWord);
    }
    public void playAgain(View view){
        // Go to the game screen
        Intent intent = new Intent(getApplicationContext(), MainActivityHALBAP.class);
        startActivity(intent);
        // close this screen
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_win_lose);
        //boolean boolisdead = getIntent().getExtras().getBoolean("isdead");
        Bundle bundle = getIntent().getExtras();
        IsDead = bundle.getBoolean("isdead");
        TheWord = bundle.getString("theword");
        //String whatever = bundle.getString("stringname");
        initializeScreen();
    }

    // Dit werkt niet om de een of andere reden
    // Hij zou de grote afbeelding op onzichtbaar moeten zetten zodat het scherm er een beetje normaal uit ziet
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("test", "komt in OnConfig..changed van win/lose");
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            ImageView IV = (ImageView)findViewById(R.id.ImageWinLose);
            IV.setVisibility(View.GONE);
            Log.d("test", "komt in de ja hij is net landscape geworden statement");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_win_lose, menu);
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
