/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.entity;

/**
 *
 * @author syafiq
 */
public class Answer {
    public int id;
    public String answer;
    public String label;
    public int score;
    public boolean correct;
    
    // Constructor lengkap
    public Answer(int id, String answer, String label, int score, boolean correct) {
        this.id = id;
        this.answer = answer;
        this.score = score;
        this.label = label;
        this.correct = correct;
    }

    public Answer(String answer, String label, int score, boolean correct) {
        this.id = 0;
        this.answer = answer;
        this.score = score;
        this.label = label;
        this.correct = correct;
    }
}
