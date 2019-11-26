/*
Author - Israel Ben Menachem

this java file is manage the logic part of the aplication.
Game_board is creating a new table game and shuffling it.
it has the functions -
    shuffle - for shuffle the table
    swap - getting a direction as an input and swap the empty square with the direction square. shuffle function is using swap.
    swap_when_clicked - it swaps between two squares and updating the table
    is_game_over - checking if the game is over and the user is winning the game
 */


package com.benmenachemis.puzzle15;

import android.util.Log;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Game_board
{
    private final int board_size = 4;
    public int moves_counter;
    public String[][] board_list;
    private int[] empty;

    public Game_board()
    {
        board_list = new String[board_size][board_size];
        moves_counter = 0;//for counting how many moves have been made

        //first, the table filled in the way that game over, then shuffle
        int num = 1;
        for (int i = 0; i < board_size; i++)
        {
            for (int j = 0; j < board_size; j++)
            {
                if(num != board_size*board_size)
                {
                    board_list[i][j] = Integer.toString(num);
                    num++;
                }
                else
                    board_list[i][j] = "";
            }
        }

        empty = new int[2];//save the indexes of the empty square
        empty[0] = 3;
        empty[1] = 3;
    }

    //shuffle the table
    public void shuffle(int how_many_shuffles)
    {
        Log.d("debug", Integer.toString(how_many_shuffles));
        List<String> dir = new ArrayList<>();
        dir.add("up");
        dir.add("down");
        dir.add("right");
        dir.add("left");

        Random random = new Random();
        for (int i = 0; i < how_many_shuffles;)
        {
            if(swap(dir.get(random.nextInt(dir.size()))))
                i++;
        }
    }

    //swap the empty square with the square that shuffle send
    public boolean swap(String dir)
    {
        String temp;
        if(dir == "up")
        {
            if(empty[0] - 1 < 0)
                return false;
            temp = board_list[empty[0] - 1][empty[1]];
            board_list[empty[0] - 1][empty[1]] = "";
            board_list[empty[0]][empty[1]] = temp;
            empty[0]--;
        }
        else if(dir == "down")
        {
            if(empty[0] + 1 >= board_size)
                return false;
            temp = board_list[empty[0] + 1][empty[1]];
            board_list[empty[0] + 1][empty[1]] = "";
            board_list[empty[0]][empty[1]] = temp;
            empty[0]++;
        }
        else if(dir == "right")
        {
            if(empty[1] + 1 >= board_size)
                return false;
            temp = board_list[empty[0]][empty[1] + 1];
            board_list[empty[0]][empty[1] + 1] = "";
            board_list[empty[0]][empty[1]] = temp;
            empty[1]++;
        }
        else//dir == "left"
        {
            if(empty[1] - 1 < 0)
                return false;
            temp = board_list[empty[0]][empty[1] - 1];
            board_list[empty[0]][empty[1] - 1] = "";
            board_list[empty[0]][empty[1]] = temp;
            empty[1]--;
        }
        return true;
    }

    //swap between two squares, using by GameActivity in onClicke mthod
    public boolean swap_when_clicked(int i, int j)
    {
        if(empty[1] < board_size-1 && i == empty[0] && j == empty[1]+1)//right
        {
            String temp = board_list[i][j];
            board_list[i][j] = "";
            board_list[empty[0]][empty[1]] = temp;
            empty[1] = empty[1]+1;
            moves_counter++;
        }
        else if(empty[1] > 0 && i == empty[0] && j == empty[1]-1)//left
        {
            String temp = board_list[i][j];
            board_list[i][j] = "";
            board_list[empty[0]][empty[1]] = temp;
            empty[1] = empty[1]-1;
            moves_counter++;
        }
        else if(empty[0] > 0 && i == empty[0]-1 && j == empty[1])//up
        {
            String temp = board_list[i][j];
            board_list[i][j] = "";
            board_list[empty[0]][empty[1]] = temp;
            empty[0] = empty[0]-1;
            moves_counter++;
        }
        else if(empty[0] < board_size-1 && i == empty[0]+1 && j == empty[1])//down
        {
            String temp = board_list[i][j];
            board_list[i][j] = "";
            board_list[empty[0]][empty[1]] = temp;
            empty[0] = empty[0]+1;
            moves_counter++;
        }
        else
            return false;
        return true;
    }

    //checking if the game is over. using by GameActivity
    public boolean is_game_over()
    {
        int num = 1;
        for (int i = 0; i < board_size; i++)
        {
            for (int j = 0; j < board_size; j++)
            {
                if(num != 16 && board_list[i][j] != Integer.toString(num))
                    return false;
                num++;
            }
        }
        return true;
    }
}
