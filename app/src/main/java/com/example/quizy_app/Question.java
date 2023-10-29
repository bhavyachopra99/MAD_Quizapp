package com.example.quizy_app;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private List<Integer> answerNrs; // Store multiple correct answers

    public Question() {
        answerNrs = new ArrayList<>();
    }

    public Question(String question, String option1, String option2, String option3, List<Integer> answerNrs) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answerNrs = answerNrs;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public List<Integer> getAnswerNrs() {
        return answerNrs;
    }

    public void setAnswerNrs(List<Integer> answerNrs) {
        this.answerNrs = answerNrs;
    }
}
