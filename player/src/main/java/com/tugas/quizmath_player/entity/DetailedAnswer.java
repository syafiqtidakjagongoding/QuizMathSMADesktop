package com.tugas.quizmath_player.entity;

public class DetailedAnswer {
    private int questionNumber;
    private String questionText;
    private String studentAnswer;
    private boolean isCorrect;
    private String correctAnswer;
    private int questionId;

    public DetailedAnswer() {
    }

    public DetailedAnswer(int questionNumber, String questionText, String studentAnswer, 
                         boolean isCorrect, String correctAnswer, int questionId) {
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.studentAnswer = studentAnswer;
        this.isCorrect = isCorrect;
        this.correctAnswer = correctAnswer;
        this.questionId = questionId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}
