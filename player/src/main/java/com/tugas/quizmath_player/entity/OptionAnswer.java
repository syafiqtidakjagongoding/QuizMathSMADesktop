/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.entity;

import jakarta.persistence.*;

/**
 *
 * @author syafiq
 */
@Entity
@Table(name = "options_answer")
public class OptionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "correct", nullable = false)
    private boolean correct;

    @Column(name = "image_answer")
    private String image_answer;

    @Column(name = "label")
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public OptionAnswer() {
    }

    public OptionAnswer(int id, String answer, int score, boolean correct, String image_answer) {
        this.id = id;
        this.answer = answer;
        this.score = score;
        this.correct = correct;
        this.image_answer = image_answer;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getImageAnswer() {
        return image_answer;
    }

    public void setImageAnswer(String image_answer) {
        this.image_answer = image_answer;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
