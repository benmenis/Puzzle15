/*
Author - Israel Ben Menachem

this java file is manage the game activity
is creating a new Game_board.
 */

package com.benmenachemis.puzzle15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import android.os.SystemClock;


public class GameActivity extends AppCompatActivity {

    private TextView moves;
    private TextView time;
    private Button start_new_game;
    private final int board_size = 4;
    private int min = 0;
    private int sec = 0;
    private boolean is_game_over = false;
    private boolean start_new_game_clicked = false;
    private boolean on_pause = false;
    private TextView[][] txv_arr = new TextView[board_size][board_size];

    Game_board gb = new Game_board();
    MediaPlayer mp;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mp = MediaPlayer.create(this, R.raw.sound_got);
        sp = getSharedPreferences("puzzle15_sp", Context.MODE_PRIVATE);
        if(sp.getBoolean("music_swich", false))
            mp.start();
        moves = findViewById(R.id.moves);
        time = findViewById(R.id.time);
        start_new_game = findViewById(R.id.StartNewGame);

        txv_arr[0][0] = findViewById(R.id.table11);
        txv_arr[0][1] = findViewById(R.id.table12);
        txv_arr[0][2] = findViewById(R.id.table13);
        txv_arr[0][3] = findViewById(R.id.table14);
        txv_arr[1][0] = findViewById(R.id.table21);
        txv_arr[1][1] = findViewById(R.id.table22);
        txv_arr[1][2] = findViewById(R.id.table23);
        txv_arr[1][3] = findViewById(R.id.table24);
        txv_arr[2][0] = findViewById(R.id.table31);
        txv_arr[2][1] = findViewById(R.id.table32);
        txv_arr[2][2] = findViewById(R.id.table33);
        txv_arr[2][3] = findViewById(R.id.table34);
        txv_arr[3][0] = findViewById(R.id.table41);
        txv_arr[3][1] = findViewById(R.id.table42);
        txv_arr[3][2] = findViewById(R.id.table43);
        txv_arr[3][3] = findViewById(R.id.table44);

        gb.shuffle(sp.getInt("level", 5));

        set_the_table();

        start_new_game.setOnClickListener(new Listener());
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                txv_arr[i][j].setOnClickListener(new Listener());
            }
        }

        //new thread for counting the time
        new_thread_for_time();
    }

    @Override
    protected void onPause() {
        super.onPause();
        on_pause = true;
        if(mp.isPlaying())
            mp.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp.isPlaying())
            mp.stop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        on_pause = false;
        if(!mp.isPlaying() && sp.getBoolean("music_swich", false))
            mp.start();
    }

    private class Listener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.StartNewGame)
            {
                gb.shuffle(sp.getInt("level", 5));
                gb.moves_counter = 0;
                for (int i = 0; i < board_size; i++)
                    for (int j = 0; j < board_size; j++)
                        txv_arr[i][j].setClickable(true);
                if(is_game_over)
                {
                    is_game_over = false;
                    min = 0;
                    sec = 0;
                    new_thread_for_time();
                }
                else
                {
                    min = 0;
                    sec = 0;
                }
            }
            else
            {
                int i_index = 0, j_index = 0;
                for (int i = 0; i < board_size; i++) //getting the indexes of clicked square
                    for (int j = 0; j < board_size; j++)
                        if(v == txv_arr[i][j])
                        {
                            i_index = i;
                            j_index = j;
                        }
                gb.swap_when_clicked(i_index, j_index);
            }

            for (int i = 0; i < board_size; i++)//recopy the table from gb.board_list
                for (int j = 0; j < board_size; j++)
                    set_the_table();

            String counter_str = String.format("Moves: %04d", gb.moves_counter);
            moves.setText(counter_str);

            if(gb.is_game_over())//if game over - enable click, toast wining massage
            {
                for (int i = 0; i < board_size; i++)
                    for (int j = 0; j < board_size; j++)
                        txv_arr[i][j].setClickable(false);
                Toast toast = Toast.makeText(getApplicationContext(), "Game Over - Puzzle Solved!", Toast.LENGTH_LONG);
                toast.show();
                is_game_over = true;
            }
        }
    }

    public void set_the_table()
    {
        for (int i = 0; i < board_size; i++)
            for (int j = 0; j < board_size; j++)
            {
                txv_arr[i][j].setText(gb.board_list[i][j]);
                txv_arr[i][j].setBackgroundResource(R.drawable.back);
                if(gb.board_list[i][j] == "")
                    txv_arr[i][j].setBackgroundResource(R.drawable.back2);
            }
    }

    private void new_thread_for_time()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                while(true)
                {
                    if(is_game_over)
                        return;
                    if(on_pause)
                        continue;
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            final String time_to_show = String.format("Time: %02d:%02d", min, sec);
                            time.setText(time_to_show);

                            sec++;
                            if(sec == 60)
                            {
                                min++;
                                sec = 0;
                            }
                        }
                    });

                    SystemClock.sleep(1000); // wait 1 sec
                }
            }
        }).start();
    }


}
