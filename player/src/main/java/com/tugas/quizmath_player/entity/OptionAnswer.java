/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.entity;

/**
 *
 * @author syafiq
 */
public class OptionAnswer {
    public int id;
    public String answer;
    public int score;
    public boolean correct;
    public String image_answer;

    public OptionAnswer(int id, String answer,int score,boolean correct, String image_answer) {
        this.id = id;
        this.answer = answer;
        this.score = score;
        this.correct = correct;
        this.image_answer = image_answer;
    }

   
}

