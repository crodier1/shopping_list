package com.example.shopping_list;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.AlteredCharSequence;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.View;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import java.util.HashSet;
import java.util.Set;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> shoppingList = null;
    ArrayAdapter<String> adapter = null;
    ListView lv = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //handle of current obj
        shoppingList = getArrayVal(getApplicationContext());

        // hard coded items in shopping list //
//        shoppingList = new ArrayList<>();
//        Collections.addAll(shoppingList, "Eggs", "Milk", "Cereal");
//        shoppingList.addAll(Arrays.asList("Bread", "Cheese", "Butter"));
//        shoppingList.add("Veggies");
//        shoppingList.add("Fruit");

        Collections.sort(shoppingList);
        //this in adapter refers to the current activity
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,shoppingList);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView parent, View view, final int position, long id){

                //when you click on an item you are getting the text inside
                String selectItem = ((TextView) view).getText().toString();
                if(selectItem.trim().equals(shoppingList.get(position).trim())){
                    removeElement(selectItem,position);
                } else {
                    //if we cannot return the string then we will throw up a temp message box. Toast is a message box
                    Toast.makeText(getApplicationContext(), "Error Removing Element", Toast.LENGTH_LONG).show();
                }
            }
        });

        // floating action button //
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //settings
//        if (id == R.id.action_settings) {
//            return true;
//        }

        //it sort is clicked on
//        if (id == R.id.action_sort) {
//            //sort shoppingList
//            Collections.sort(shoppingList);
//            lv.setAdapter(adapter);
//            return true;
//        }

        //add item to list
        if(id == R.id.action_add){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Item");
            final EditText input = new EditText(this);
            builder.setView(input);

            builder.setView(input);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    shoppingList.add(preferredCase(input.getText().toString().trim()));
                    //this will store the array list in db
                    storeArrayVal(shoppingList, getApplicationContext());
                    Collections.sort(shoppingList);
                    storeArrayVal(shoppingList, getApplicationContext());
                    lv.setAdapter(adapter);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }

        if(id == R.id.action_clear){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Clear Entire List");

            //if click yes on clear list then clear whole list
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    shoppingList.clear();
                    lv.setAdapter(adapter);
                }
            });

            //if click no then on clear then close dialog box
            builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //capitalizes first character of each grocery shopping item
    public static String preferredCase(String original)
    {
        if(original.isEmpty())return original;

        return original.substring(0,1).toUpperCase() + original.substring(1).toLowerCase();
    }

    //stores data in db
    public static void storeArrayVal(ArrayList inArrayList, Context context)
    {
        Set WhatToWrite = new HashSet(inArrayList);
        SharedPreferences WordSearchputPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchputPrefs.edit();
        prefEditor.putStringSet("myArray", WhatToWrite);
        prefEditor.commit();
    }

    //gets data in db
    public static ArrayList getArrayVal(Context dan)
    {
        SharedPreferences WordSearchGetPrefs = dan.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        Set tempSet = new HashSet();
        tempSet = WordSearchGetPrefs.getStringSet("myArray",tempSet);
        return new ArrayList<>(tempSet);
    }

    //removes unwanted elements
    public void removeElement(final String selectedItem, final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove " + selectedItem + "?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

                shoppingList.remove(position);
                Collections.sort(shoppingList);
                storeArrayVal(shoppingList, getApplicationContext());
                lv.setAdapter(adapter);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        builder.show();
    }


}
