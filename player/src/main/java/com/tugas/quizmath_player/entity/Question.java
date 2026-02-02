/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author syafiq
 */
@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String question_text;

    @Column(name = "answer_type", nullable = false)
    private String answer_type;

    @Column(name = "level", nullable = false)
    private String level;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Transient // Mapped via logic or separate entity query, or we can map it relationally
    private String image_path;

    // If we want to map it completely:
    // @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    // private QuestionImage questionImage;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OptionAnswer> answers = new ArrayList<>();

    public Question() {
    }

    public Question(int id, String question_text, String answer_type,
            String level, String topic, String image_path) {
        this.id = id;
        this.question_text = question_text;
        this.answer_type = answer_type;
        this.level = level;
        this.topic = topic;
        this.image_path = image_path;
    }

    public void addAnswer(OptionAnswer answer) {
        answer.setQuestion(this);
        this.answers.add(answer);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return question_text;
    }

    public void setQuestionText(String question_text) {
        this.question_text = question_text;
    }

    public String getAnswerType() {
        return answer_type;
    }

    public void setAnswerType(String answer_type) {
        this.answer_type = answer_type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getImagePath() {
        return image_path;
    }

    public void setImagePath(String image_path) {
        this.image_path = image_path;
    }

    public List<OptionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<OptionAnswer> answers) {
        this.answers = answers;
        for (OptionAnswer oa : answers)
            oa.setQuestion(this);
    }

    // Compatibility setters for field access replacement
    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }
}
