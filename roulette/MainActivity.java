package com.example.roulette;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView tvResult; // лейбл с результатом
    private ImageView roulette;
    private Random random;
    private int old_degree = 0; //начальное положение рулетки (в градусах)
    private int degree = 0; // положение до которого вращем
    private static final float FACTOR = 4.86f; /* 360 (градусов окружности) / 37 (ячеек на рулетке) / 2 (половина ячейки)
                                                360 / 37 / 2 = 4.86c
                                                используем половину ячейки, т.к. начальное положение рулетки - в середине ячейки 0  */
    private String[] numbers = {"32 RED","15 BLACK","19 RED","4 BLACK",
            "21 RED","2 BLACK","25 RED","17 BLACK", "34 RED",
            "6 BLACK","27 RED","13 BLACK","36 RED","11 BLACK","30 RED",
            "8 BLACK","23 RED","10 BLACK","5 RED","24 BLACK","16 RED","33 BLACK",
            "1 RED","20 BLACK","14 RED","31 BLACK","9 RED","22 BLACK","18 RED",
            "29 BLACK","7 RED","28 BLACK","12 RED","35 BLACK","3 RED","26 BLACK","0"}; //значения ячеек

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    public void onClickStart(View view) { //обрабатываем событие "нажатие на кнопку"

        old_degree = degree % 360; // отбрасываем лишние градусы полных оборотов
        degree = random.nextInt(3600) + 720; // от 2 до 12 полных вращений

        //задаём вращение картинки относительно её собственного центра (relative_to_self, x:0.5, y:0.5)
        RotateAnimation rotate = new RotateAnimation(old_degree, degree, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                                                         RotateAnimation.RELATIVE_TO_SELF, 0.5f );

        rotate.setDuration(3600); // длительность вращения
        rotate.setFillAfter(true);
        rotate.setInterpolator(new DecelerateInterpolator());
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tvResult.setText(""); //убираем текст с результами очков когда запускается анимация прокрутки
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvResult.setText(getResult(360 - (degree % 360))); // когда анимация вращения закончилась - выводим результат с помощью getResult
                // отбрасываем полные обороты (degree % 360) и инвертируем градусы (отнимаем от 360) - т.к. анимацию вращаем вправо по часовой (и для удобства отсчитываем клетки по часовой) а относительно стрелки поля прокручиваются влево (против часовой)
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        roulette.startAnimation(rotate);
    }
    //инициализация объектов
    private  void init() {
        tvResult = findViewById(R.id.tvResult);
        roulette = findViewById(R.id.roulette);
        random = new Random();
    }
    private String getResult(int degree) { // получаем значение которое поместим на лейбл с выпавшим числом
        String text = "";

        int factor_x = 1; //левая граница ячейки
        int factor_y = 3; //правая граница ячейки


        for(int i = 0;i<37;i++) { //ищем выпавшую ячейку в массиве ячеек
            if (degree >= (FACTOR * factor_x) && degree < (FACTOR * factor_y)) {
                text = numbers[i];
            }
            factor_x += 2; // сдвигаем границы ячейки на следующую чейку (на 2 половины одной ячейки)
            factor_y += 2;
        }
        //обрабатываем получение ячейки "0"
        // если первая или последняя (по порядку) половина ячейки, то у нас "0"
        if(degree >= (FACTOR * 73) && degree < 360 || degree >= 0 && degree < FACTOR)
            text = numbers[numbers.length - 1];

        return text;
    }

}