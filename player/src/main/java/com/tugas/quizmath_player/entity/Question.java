/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.entity;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author syafiq
 */
public class Question {
    public int id;
    public String question_text;
    public String answer_type;
    public String level;
    public String topic;
    public String image_path;
    public List<OptionAnswer> answers = new ArrayList<>();

    
    
    public Question(int id, String question_text, String answer_type,
                    String level, String topic,  String image_path) {
        this.id = id;
        this.question_text = question_text;
        this.answer_type = answer_type;
        this.level = level;
        this.topic = topic;
        this.image_path = image_path;
    }
    
    public void addAnswer(OptionAnswer answer) {
        this.answers.add(answer);
    }
}
