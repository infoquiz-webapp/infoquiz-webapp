package com.davidefella.infoquiz.utility.datafactory;

import com.davidefella.infoquiz.model.persistence.*;
import com.davidefella.infoquiz.service.*;
import com.davidefella.infoquiz.utility.DecimalRounder;
import com.davidefella.infoquiz.utility.StartupDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/* Classe che produce dati di test e li salva nel database.
 * Utile per test e demo
 *
 * */

@Component
@Deprecated
public class DummyDataFactory {

    private static final Logger logger = LoggerFactory.getLogger(StartupDataLoader.class);

    private final AnswerService answerService;
    private final EvaluationService evaluationService;
    private final EvaluationStudentService evaluationStudentService;
    private final StudentService studentService;
    private final QuestionService questionItemService;
    private final Environment environment;

    @Autowired
    public DummyDataFactory(AnswerService answerService, EvaluationService evaluationService,
                            EvaluationStudentService evaluationStudentService, StudentService studentService,
                            QuestionService questionItemService, Environment environment) {
        this.answerService = answerService;
        this.evaluationService = evaluationService;
        this.evaluationStudentService = evaluationStudentService;
        this.studentService = studentService;
        this.questionItemService = questionItemService;
        this.environment = environment;
    }

    public void loadAllDummyData() {
        loadUserData();
        loadEvaluationData();
        loadEvaluationStudentData();
        loadQuestionData();
        loadAnswerData();

    }

    private void loadEvaluationData() {
        List<Evaluation> evaluations = Arrays.asList(
                new Evaluation("E1", LocalDate.now(), "Evaluation 1", "Descrizione Evaluation 1", true),
                new Evaluation("E2", LocalDate.now().minusMonths(1), "Evaluation 2", "Descrizione Evaluation 2", true),
                new Evaluation("E3", LocalDate.now().minusMonths(2), "Evaluation 3", "Descrizione Evaluation 3", true)
        );

        for (Evaluation evaluation : evaluations) {
            evaluationService.save(evaluation);
        }

        logger.info("Loaded evaluations");
    }

    private void loadQuestionData() {
        List<Evaluation> evaluations = evaluationService.findAll();

        for (Evaluation evaluation : evaluations) {
            List<Question> questions = Arrays.asList(
                    new Question(evaluation.getCode().concat("Q1"), evaluation, "Question 1 ?"),
                    new Question(evaluation.getCode().concat("Q2"), evaluation, "Question 2 ?"),
                    new Question(evaluation.getCode().concat("Q3"), evaluation, "Question 3 ?"),
                    new Question(evaluation.getCode().concat("Q4"), evaluation, "Question 4 ?"),
                    new Question(evaluation.getCode().concat("Q5"), evaluation, "Question 5 ?")
            );

            for (Question question : questions) {
                questionItemService.save(question);
            }
        }

        logger.info("Loaded questions");

    }

    private void loadAnswerData() {
        List<Question> questions = questionItemService.findAll();

        for (Question question : questions) {
            List<Answer> answers = Arrays.asList(
                    new Answer(question.getCode().concat("A1"), question, "Correct answer for " + question.getQuestionText(), true),
                    new Answer(question.getCode().concat("A2"), question, "answer 2 <code> Some Code </code>?\n ", false),
                    new Answer(question.getCode().concat("A3"), question, "answer 3 ", false),
                    new Answer(question.getCode().concat("A4"), question, "answer 4 ", false)
            );

            for (Answer answer : answers) {
                answerService.save(answer);
            }
        }

        logger.info("Loaded answers");

    }

    private void loadUserData() {
        List<Student> students = Arrays.asList(
                new Student("Nome 1", "Nome 1"),
                new Student("Nome 2", "Nome 2")
        );

        for (Student student : students) {
            studentService.save(student);
        }

        logger.info("Loaded students");
    }

    private void loadEvaluationStudentData() {
        List<Student> students = studentService.findAll();
        List<Evaluation> evaluations = evaluationService.findAll();

        for (Student student : students) {
            for (Evaluation evaluation : evaluations) {
                EvaluationStudent evaluationStudent = new EvaluationStudent();
                evaluationStudent.setStudent(student);
                evaluationStudent.setEvaluation(evaluation);
                evaluationStudent.setScore(DecimalRounder.roundToTwoDecimals(Math.random() * 10)); // Random score between 0 and 100
                evaluationStudentService.save(evaluationStudent);
            }
        }

        logger.info("Loaded students sessions");

    }


}
