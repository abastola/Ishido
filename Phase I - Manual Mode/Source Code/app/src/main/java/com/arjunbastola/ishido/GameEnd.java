package com.arjunbastola.ishido;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameEnd extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
        Button playAgain=(Button) findViewById(R.id.playAgain);
        TextView HumanScoreUpdate = (TextView) findViewById(R.id.finalScore);
        board Danielle= new board();
        HumanScoreUpdate.setText(Integer.toString(Danielle.HumanScore));
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent(GameEnd.this, MainActivity.class);
                startActivity(myIntent); //starts activity board
            }
        });
    }

}
