/*
Author - Israel Ben Menachem

this java file is manage the main activity
 */

package com.benmenachemis.puzzle15;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Switch musicSwich;
    private Button btnClick;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("puzzle15_sp", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("level", 5);
        setContentView(R.layout.activity_main);
        musicSwich = findViewById(R.id.switch1);
        if(sp.getBoolean("music_swich", false))
            musicSwich.setChecked(true);
        btnClick = findViewById(R.id.button1);
        btnClick.setOnClickListener(new Listener());

        musicSwich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged (CompoundButton buttonView,boolean isChecked)
            {
                if (isChecked)
                {
                    editor.putBoolean("music_swich", true);
                    editor.apply();
                    editor.commit();
                }
                else
                {
                    editor.putBoolean("music_swich", false);
                    editor.apply();
                    editor.commit();
                }
            }
        });
    }


    private class Listener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            open_game_activity();
        }
    }

    public void open_game_activity()
    {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuItem menuDifficulty = menu.add("Difficulty");
        MenuItem menuAbout = menu.add("About");
        MenuItem menuExit = menu.add("Exit");

        menuDifficulty.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                showDifficultyDialog();
                return true;
            }
        });

        menuAbout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                showAboutDialog();
                return true;
            }
        });

        menuExit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                showExitDialog();
                return true;
            }
        });
        return true;
    }

    private void showDifficultyDialog()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setIcon(R.drawable.puzzle15);
        alertDialog.setTitle("Select a difficulty level");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Easy", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                editor.putInt("level", 5);  // shuffle 5 times
                editor.apply();
                editor.commit();
            }
        });
        //alertDialog.show();
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Medium", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                editor.putInt("level", 15);  // shuffle 10 times
                editor.apply();
                editor.commit();
            }
        });
        //alertDialog.show();
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"Hard", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                editor.putInt("level", 100);  // shuffle 15 times
                editor.apply();
                editor.commit();
            }
        });
        alertDialog.show();
    }

    private void showAboutDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.puzzle15);
        alertDialog.setTitle("About Puzzle 15");
        alertDialog.setMessage("This app imlements the Game of Fifteen.\n\nBy ISRAEL BEN MENACHEM(c).");
        alertDialog.show();
        Toast.makeText(this, "ABOUT", Toast.LENGTH_SHORT).show();
    }

    private void showExitDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.puzzle15);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("Do you really want to exit?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();  // destroy this activity
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(MainActivity.this, "NO", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

}


