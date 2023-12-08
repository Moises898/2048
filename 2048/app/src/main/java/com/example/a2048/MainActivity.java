package com.example.a2048;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    View contenedorTableroGrafico;
    TextView TextScore,HighScore;
    Button btnplay;
    GestureDetector gestos;
    int[][] tablero;
    int highScore;
    LinearLayout main;
    Metodos helper = new Metodos();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("score", MODE_PRIVATE);
        contenedorTableroGrafico = findViewById(R.id.tablero);
        btnplay = findViewById(R.id.btnPlay);
        TextScore = findViewById(R.id.tvPuntuacion);
        HighScore = findViewById(R.id.tvHighScore);
        gestos = new GestureDetector(this,new Slide());
        highScore = sharedPreferences.getInt("score", 0);
        String txtHighscore = "High Score:" + "\n" + highScore;
        HighScore.setText(txtHighscore);
        main = contenedorTableroGrafico.findViewById(R.id.grid);
    }

    TextView[][] tableroTextView = new TextView[8][8];
    // Start the game and apply the apropiate methods
    public void start(View view){
        for (int i=0; i < main.getChildCount(); i++) {
            LinearLayout rows = (LinearLayout) main.getChildAt(i);
            for (int j = 0; j < rows.getChildCount(); j++) {
                View casilla = rows.getChildAt(j);
                if (casilla instanceof TextView) {
                    tableroTextView[i][j] = (TextView) casilla;
                }
            }
        }
        tablero = helper.newGame(tableroTextView);
        btnplay.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestos.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    class Slide extends GestureDetector.SimpleOnGestureListener{
        // Gesture Detector
        @Override
        public boolean onFling(MotionEvent e1,MotionEvent e2, float velocityX, float velocityY) {
            float ancho = Math.abs(e2.getX()-e1.getX());
            float alto = Math.abs(e2.getY()-e1.getY());

            // Slide in X
            if (ancho > alto){
                // Right
                if (e2.getX() > e1.getX()){
                    tablero = helper.movimientos(1,tablero,tableroTextView);
                }
                // Left
                else{
                    tablero = helper.movimientos(3,tablero,tableroTextView);
                }
            }
            // Slide in Y
            else{
                // Down
                if (e2.getY() > e1.getY()){
                    tablero = helper.movimientos(0,tablero,tableroTextView);
                }
                // Up
                else{
                    tablero = helper.movimientos(2,tablero,tableroTextView);
                }
            }
            String score = "Score: "+"\n"+ helper.score;
            TextScore.setText(score);
            if (helper.score > highScore){
                highScore = helper.score;
                sharedPreferences.edit().putInt("score", highScore).apply();
                String hscore = "High Score: "+"\n"+ highScore;
                HighScore.setText(hscore);
            }
            if (!helper.tableroLLeno(tablero) && !helper.validMoveExists(tablero )){
                contenedorTableroGrafico.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Fin del juego", Toast.LENGTH_SHORT).show();
            } else if (!helper.validMoveExists(tablero)) {
                contenedorTableroGrafico.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Fin del juego", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

}