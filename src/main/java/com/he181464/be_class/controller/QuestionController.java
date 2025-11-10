package com.he181464.be_class.controller;

import com.he181464.be_class.OpenAIService.OpenAIService;
import com.he181464.be_class.dto.*;
import com.he181464.be_class.service.AccountService;
import com.he181464.be_class.service.QuestionService;
import com.he181464.be_class.service.SubmissionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/Question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

//    private final OpenAIService openAIService;

    private final AccountService accountService;

    private final SubmissionHistoryService submissionHistoryService;

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<?>getByLessonId(@PathVariable Long lessonId){
        return ResponseEntity.ok(questionService.getListQuestionByLesson(lessonId));
    }


    @PostMapping("/create")
    public ResponseEntity<?>createListQuestion(@RequestBody QuestionCreateDto questionCreateDto){
        return ResponseEntity.ok(questionService.createListQuestion(questionCreateDto));
    }

    @DeleteMapping("/deleteByQuestionType/{id}")
    public ResponseEntity<?>deleteQuestionByQuestionType(@PathVariable Integer id){
        questionService.deleteQuestionByQuestionType(id);
        return ResponseEntity.ok("Deleted");
    }

    @DeleteMapping("delete/reading/{id}")
    public ResponseEntity<?>deleteReading(@PathVariable Integer id){
        questionService.deleteReadingPassage(id);
        return ResponseEntity.ok("Deleted Reading passage success");
    }

    @DeleteMapping("delete/listening/{id}")
    public ResponseEntity<?>deleteListening(@PathVariable Integer id){
        questionService.deleteListeningPassage(id);
        return ResponseEntity.ok("Deleted Listening passage success");
    }

    @PutMapping("/update")
    public ResponseEntity<?>updateQuetion(@RequestBody QuestionCreateDto questionCreateDto){
        return ResponseEntity.ok(questionService.updateQuestion(questionCreateDto));
    }

    @GetMapping("/questionType/{id}")
    public ResponseEntity<?>getByQuestionType(@PathVariable Integer id){
        return ResponseEntity.ok(questionService.getQuestionByQuestionTypeId(id));
    }

    @PostMapping("/check-answer-vocab/{lessonId}" )
    public ResponseEntity<?>checkAnswerVocab(@RequestBody List<AnswerCheckDto> answers, @PathVariable Long lessonId){

        List<AnswerCheckedDto> answerCheckDtos = questionService.checkAnswerVocab(answers, lessonId);

        Integer failCount = answerCheckDtos.size();
        String type = "vocabulary";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Long accountId = accountService.getAccountIdByEmail(email);
        SubmissionHistoryDto submissionHistoryDto = new SubmissionHistoryDto();
        submissionHistoryDto.setStudentId(accountId);
        submissionHistoryDto.setLessonId(lessonId);
        submissionHistoryDto.setNumberWrong(failCount);
        submissionHistoryDto.setType(type);
        submissionHistoryService.createSubmissionHistory(submissionHistoryDto);
        return ResponseEntity.ok(answerCheckDtos);
    }

    @PostMapping("/check-answer-listening/{lessonId}" )
    public ResponseEntity<?>checkAnswerListening(@RequestBody List<AnswerCheckDto> answers, @PathVariable Long lessonId){
        List<AnswerCheckedDto> answerCheckDtos = questionService.checkAnswerListen(answers, lessonId);

        Integer failCount = answerCheckDtos.size();
        String type = "listening";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Long accountId = accountService.getAccountIdByEmail(email);
        SubmissionHistoryDto submissionHistoryDto = new SubmissionHistoryDto();
        submissionHistoryDto.setStudentId(accountId);
        submissionHistoryDto.setLessonId(lessonId);
        submissionHistoryDto.setNumberWrong(failCount);
        submissionHistoryDto.setType(type);
        submissionHistoryService.createSubmissionHistory(submissionHistoryDto);
        return ResponseEntity.ok(answerCheckDtos);
    }

    @PostMapping("/check-answer-reading/{lessonId}" )
    public ResponseEntity<?>checkAnswerReading(@RequestBody List<AnswerCheckDto> answers, @PathVariable Long lessonId){
        List<AnswerCheckedDto> answerCheckDtos = questionService.checkAnswerReading(answers, lessonId);

        Integer failCount = answerCheckDtos.size();
        String type = "reading";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Long accountId = accountService.getAccountIdByEmail(email);
        SubmissionHistoryDto submissionHistoryDto = new SubmissionHistoryDto();
        submissionHistoryDto.setStudentId(accountId);
        submissionHistoryDto.setLessonId(lessonId);
        submissionHistoryDto.setNumberWrong(failCount);
        submissionHistoryDto.setType(type);
        submissionHistoryService.createSubmissionHistory(submissionHistoryDto);
        return ResponseEntity.ok(answerCheckDtos);
    }

    @PostMapping("/check-answer-writing/{lessonId}" )
    public ResponseEntity<?>checkAnswerWriting(@RequestBody List<AiWrittingDto> answers, @PathVariable Long lessonId){
        for(AiWrittingDto answer : answers){
            String type = "writing";
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            Long accountId = accountService.getAccountIdByEmail(email);
            SubmissionHistoryDto submissionHistoryDto = new SubmissionHistoryDto();
            submissionHistoryDto.setStudentId(accountId);
            submissionHistoryDto.setLessonId(lessonId);
            submissionHistoryDto.setType(type);
            submissionHistoryDto.setAnswerWriting(answer.getQuestion());
            submissionHistoryService.createSubmissionHistory(submissionHistoryDto);
        }
        return ResponseEntity.ok("Submitted writing tasks successfully");
    }

    @GetMapping("/listening/{lessonId}")
    public ResponseEntity<?>getListeningPassageByLessonId(@PathVariable Long lessonId){
        return ResponseEntity.ok(questionService.getListeningPassageByLessonId(lessonId));
    }

    @GetMapping("/reading/{lessonId}")
    public ResponseEntity<?>getReadingPassageByLessonId(@PathVariable Long lessonId) {
        return ResponseEntity.ok(questionService.getReadingPassageByLessonId(lessonId));
    }



    @GetMapping("/history/{id}")
    public ResponseEntity<?>getSubmissionHistoryByLesson(@PathVariable Long id){
        return ResponseEntity.ok(questionService.getSubmissionHistoryByLesson(id));
    }

    @GetMapping("/result/fail/{id}")
    private ResponseEntity<?>getQuestionAnswerFailBySubmission(@PathVariable Long id){
        return ResponseEntity.ok(questionService.getQuestionAnswerFailBySubmissionHistory(id));
    }

//    @PostMapping("/AI-check-writing")
//    public ResponseEntity<?>AIcheckWriting(@RequestBody AiWrittingDto question){
//        String response = openAIService.ask(question.getQuestion());
//        return ResponseEntity.ok(response);
//    }
}
