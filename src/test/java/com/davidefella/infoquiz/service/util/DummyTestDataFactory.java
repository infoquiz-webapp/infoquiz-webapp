package com.davidefella.infoquiz.service.util;

import com.davidefella.infoquiz.model.persistence.*;
import com.davidefella.infoquiz.service.*;
import com.davidefella.infoquiz.service.business.EvaluationHandler;
import com.davidefella.infoquiz.utility.StartupDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* Classe che produce dati di test e li salva nel database.
 * Utile per test e demo */
@Component
public class DummyTestDataFactory {

    private static final Logger logger = LoggerFactory.getLogger(StartupDataLoader.class);

    private final AnswerService answerService;
    private final EvaluationService evaluationService;
    private final EvaluationStudentService evaluationStudentService;
    private final StudentService studentService;
    private final QuestionService questionItemService;
    private final EvaluationHandler evaluationHandler;

    @Autowired
    public DummyTestDataFactory(AnswerService answerService, EvaluationService evaluationService,
                                EvaluationStudentService evaluationStudentService, StudentService studentService,
                                QuestionService questionItemService, EvaluationHandler evaluationHandler) {
        this.answerService = answerService;
        this.evaluationService = evaluationService;
        this.evaluationStudentService = evaluationStudentService;
        this.studentService = studentService;
        this.questionItemService = questionItemService;
        this.evaluationHandler = evaluationHandler;
    }

    public void loadAllDummyData() {
        loadUserData();
        loadEvaluationData();
        //loadEvaluationPlayerData();
        loadQuestionData();
        loadAnswerData();

    }

    private void loadEvaluationData() {
        List<Evaluation> evaluations = List.of(new Evaluation("T_E1", LocalDate.now(), "Evaluation 1", "Evaluation per la classe di Test", true),
                                               new Evaluation("T_E2", LocalDate.now(), "Evaluation 2", "Evaluation vuota", true));

        evaluationService.saveAll(evaluations);

    }

    private void loadQuestionData() {
        Evaluation e1 = evaluationService.findByCode("T_E1").get();

        List<Question> questions = Arrays.asList(
                new Question("T_Q1", e1, "Question 1 ?"),
                new Question("T_Q2", e1, "Question 2 ?"),
                new Question("T_Q3", e1, "Question 3 ?")
        );

        questionItemService.saveAll(questions);

    }

    private void loadAnswerData() {
        Question q1 = questionItemService.findByCode("T_Q1").get();
        List<Answer> answersQ1 = Arrays.asList(
                Answer.builder().code("T_A1").question(q1).answerText("Correct answer").isCorrect(true).build(),
                Answer.builder().code("T_A2").question(q1).answerText("Wrong answer").isCorrect(false).build(),
                Answer.builder().code("T_A3").question(q1).answerText("Wrong answer").isCorrect(false).build()
        );
        answerService.saveAll(answersQ1);

        Question q2 = questionItemService.findByCode("T_Q2").get();
        List<Answer> answersQ2 = Arrays.asList(
                Answer.builder().code("T_A4").question(q2).answerText("Wrong answer").isCorrect(false).build(),
                Answer.builder().code("T_A5").question(q2).answerText("Correct answer").isCorrect(true).build(),
                Answer.builder().code("T_A6").question(q2).answerText("Wrong answer").isCorrect(false).build()
        );
        answerService.saveAll(answersQ2);


        Question q3 = questionItemService.findByCode("T_Q2").get();
        List<Answer> answersQ3 = Arrays.asList(
                Answer.builder().code("T_A7").question(q3).answerText("Wrong answer").isCorrect(false).build(),
                Answer.builder().code("T_A8").question(q3).answerText("Wrong answer").isCorrect(false).build(),
                Answer.builder().code("T_A9").question(q3).answerText("Correct answer").isCorrect(true).build()
        );
        answerService.saveAll(answersQ3);

        logger.info("Loaded answers");

    }

    private void loadUserData() {
        List<Student> students = new ArrayList<>(List.of(new Student("T_Cognome 1", "T_Nome 1")));

        studentService.saveAll(students);

        logger.info("Loaded Students");
    }
}
