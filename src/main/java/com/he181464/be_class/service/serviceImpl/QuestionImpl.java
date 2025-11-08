package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.*;
import com.he181464.be_class.entity.*;
import com.he181464.be_class.mapper.*;
import com.he181464.be_class.repository.*;
import com.he181464.be_class.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final QuestionTypeRepository questionTypeRepository;
    private final LessonRepository lessonRepository;
    private final QuestionTypeMapper questionTypeMapper;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionOptionMapper questionOptionMapper;
    private final ReadingRepository readingRepository;
    private final ReadingMapper readingMapper;
    private final ListeningPassageRepository listeningPassageRepository;
    private final ListeningPassageMapper listeningPassageMapper;

    @Transactional
    @Override
    public QuestionCreateDto createListQuestion(QuestionCreateDto questionCreateDto) {

        Lesson lesson = lessonRepository.findById(questionCreateDto.getQuestionTypeDto().getLessonId()).orElseThrow(() -> new IllegalArgumentException("Khong tim thay lesson voi id la"));
        ReadingDto readingDto = questionCreateDto.getReadingDto();
        ReadingPassage savedReading = null;
        if (readingDto != null) {
            ReadingPassage readingEntity = readingMapper.toReadingPassageEntity(readingDto);
            readingEntity.setLesson(lesson);
            readingEntity.setCreatedAt(LocalDateTime.now());
            savedReading = readingRepository.save(readingEntity);
        }

        ListeningPassageDto listeningPassageDto = questionCreateDto.getListeningPassageDto();
        ListeningPassage savedListening = null;

        if (listeningPassageDto != null) {
            ListeningPassage listeningEntity = listeningPassageMapper.toListeningPassageEntity(listeningPassageDto);
            listeningEntity.setLesson(lesson);
            listeningEntity.setCreatedAt(LocalDateTime.now());
            savedListening = listeningPassageRepository.save(listeningEntity);
        }

        QuestionType questionType = questionTypeMapper.toQuestionTypeEntity(questionCreateDto.getQuestionTypeDto());
        questionType.setLesson(lesson);
        questionType.setType(questionCreateDto.getQuestionTypeDto().getType());
        List<QuestionDto> questionDtos = questionCreateDto.getQuestionTypeDto().getQuestions();
        List<Questions> questions = new ArrayList<>();
        for (QuestionDto questionDto : questionDtos) {

            Questions question = questionMapper.toQuestionEntity(questionDto);
            question.setQuestionType(questionType);
            question.setCreatedAt(LocalDateTime.now());
            if (savedReading != null) {
                question.setReadingPassage(savedReading);
            }
            if (savedListening != null) {
                question.setListeningPassage(savedListening);
            }

            if (questionDto.getOptions() != null) {
                List<QuestionOption> questionOptions = questionDto.getOptions().stream().map(
                        questionOptionMapper::toQuestionOptionEntity).toList();
                question.setQuestionOptions(questionOptions);
                for (QuestionOption option : question.getQuestionOptions()) {
                    option.setQuestions(question);
                }
            }
            questions.add(question);
        }
        questionType.setQuestions(questions);
        QuestionType savedType = questionTypeRepository.save(questionType);
        questionCreateDto.getQuestionTypeDto().setQuestionTypeId(savedType.getId());
        return questionCreateDto;
    }

    @Override
    public List<QuestionCreateDto> getListQuestionByLesson(Long id) {
        List<QuestionType> questionTypes = questionTypeRepository.findByLessonId(id);
        List<Questions> questionsList = questionRepository.findByQuestionType_LessonId(id);
        List<QuestionOption> questionOptionList = questionRepository.findQuestionOptionByLessonId(id);
        Map<Integer, List<Questions>> questionByTypeId = questionsList.stream().collect(Collectors.groupingBy(questions -> questions.getQuestionType().getId()));
        Map<Integer, List<QuestionOption>> questionOptionByQuestion = questionOptionList.stream().collect(Collectors.groupingBy(questionOption -> questionOption.getQuestions().getId()));
        List<QuestionCreateDto> questionCreateDtos = new ArrayList<>();
        for (QuestionType questionType : questionTypes) {
            QuestionCreateDto questionCreateDto = new QuestionCreateDto();
            QuestionTypeDto questionTypeDto = questionTypeMapper.toQuestionTypeDto(questionType);
            questionTypeDto.setLessonId(id);
            questionTypeDto.setQuestionTypeId(questionType.getId());
            List<Questions> questions = questionByTypeId.get(questionTypeDto.getQuestionTypeId());
            ListeningPassage listeningPassage;
            Boolean isLong;
            if (questionType.getType().equalsIgnoreCase("listening")) {
                listeningPassage = questions.get(0).getListeningPassage();
                questionCreateDto.setListeningPassageDto(listeningPassageMapper.
                        toListeningPassageDto(listeningPassage));
                if(listeningPassage.getPassage_type().equalsIgnoreCase("long")){

                    isLong=true;
                } else {
                    isLong = false;
                }
            } else {
                isLong = false;
            }
            ReadingPassage readingPassage;
            if (questionType.getType().equalsIgnoreCase("reading")) {
                readingPassage = questions.get(0).getReadingPassage();
                ReadingDto readingDto = readingMapper.toReadingDto(readingPassage);
                questionCreateDto.setReadingDto(readingDto);
            } else {
                readingPassage = null;
            }
            List<QuestionDto> questionDtos = questions.stream().map(question -> {
                QuestionDto questionDto = questionMapper.toQuestionDto(question);
                questionDto.setQuestionTypeId(questionType.getId());
                if ((readingPassage != null) || questionType.getType().equalsIgnoreCase("mc")
                        ||isLong) {
                    List<QuestionOption> questionOptions = questionOptionByQuestion.get(question.getId());
                    List<QuestionOptionDto> questionOptionDtos = questionOptions.stream().map(questionOption -> {
                        QuestionOptionDto questionOptionDto = questionOptionMapper.toQuestionOptionDto(questionOption);
                        return questionOptionDto;
                    }).toList();
                    questionDto.setOptions(questionOptionDtos);
                }
                if(!isLong&&questionType.getType().equalsIgnoreCase("listening")){
                    ListeningPassageDto listeningPassageDto = listeningPassageMapper.toListeningPassageDto(question.getListeningPassage());
                }
                if (readingPassage != null) {
                    questionDto.setReadingPassageId(readingPassage.getId());
                }
                return questionDto;
            }).toList();
            questionTypeDto.setQuestions(questionDtos);
            questionCreateDto.setQuestionTypeDto(questionTypeDto);

            questionCreateDtos.add(questionCreateDto);
        }
        return questionCreateDtos;
    }

    @Override
    public void deleteQuestionByQuestionType(Integer id) {
        QuestionType questionType = questionTypeRepository.findById(id).
                orElseThrow(()->new IllegalArgumentException("Khong tim thay questio type id"));

        questionTypeRepository.deleteById(id);

    }

    @Override
    public void deleteReadingPassage(Integer id) {
        readingRepository.deleteById(id);
    }

    @Override
    public void deleteListeningPassage(Integer id) {
        listeningPassageRepository.deleteById(id);
    }

@Transactional
    @Override
    public QuestionCreateDto updateQuestion(QuestionCreateDto questionCreateDto) {
        QuestionTypeDto questionTypeDto = questionCreateDto.getQuestionTypeDto();
        QuestionType savedQuestionType = questionTypeMapper.toQuestionTypeEntity(questionTypeDto);
        savedQuestionType.setLesson(lessonRepository.findById(questionCreateDto.getQuestionTypeDto().getLessonId()).orElseThrow(
                () -> new IllegalArgumentException("Khong tim thay id")
        ));
        savedQuestionType.setId(questionTypeDto.getQuestionTypeId());
        ReadingPassage readingPassage =null;
        ListeningPassage listeningPassage=null;
        if (savedQuestionType.getType().equalsIgnoreCase("reading")) {
            readingPassage = readingMapper.toReadingPassageEntity(
                    questionCreateDto.getReadingDto());
            readingPassage.setLesson(savedQuestionType.getLesson());
            readingPassage = readingRepository.save(readingPassage);
        }
        if (savedQuestionType.getType().equalsIgnoreCase("listening")) {
            listeningPassage = listeningPassageMapper.toListeningPassageEntity(
                    questionCreateDto.getListeningPassageDto());
            listeningPassage.setLesson(savedQuestionType.getLesson());
            listeningPassage = listeningPassageRepository.save(listeningPassage);
        }

        ReadingPassage finalReadingPassage = readingPassage;
        ListeningPassage finalListeningPassage = listeningPassage;
        List<Questions> questions = questionTypeDto.getQuestions().stream()
                .map(item -> {
                    Questions question = questionMapper.toQuestionEntity(item);
                    question.setQuestionType(savedQuestionType);
                    if (questionTypeDto.getType().equalsIgnoreCase("reading")) {
                        question.setReadingPassage(finalReadingPassage);
                    } else if (finalListeningPassage != null) {
                        question.setListeningPassage(finalListeningPassage);
                    }
                    if (questionTypeDto.getType().equalsIgnoreCase("mc")
                            || questionTypeDto.getType().equalsIgnoreCase("reading")
                            || (finalListeningPassage.getPassage_type().equalsIgnoreCase("long"))) {

                        List<QuestionOption> questionOptions = item.getOptions().stream()
                                .map(questionOptionDto -> {
                                    QuestionOption questionOption = questionOptionMapper.toQuestionOptionEntity(questionOptionDto);
                                    questionOption.setQuestions(question);
                                    return questionOption;
                                }).toList();

                        question.setQuestionOptions(questionOptions);
                    }

                    return question;
                })
                .toList();

        savedQuestionType.setQuestions(questions);

        questionTypeRepository.save(savedQuestionType);

        return questionCreateDto;
    }

    @Override
    public QuestionCreateDto getQuestionByQuestionTypeId(Integer id) {
        QuestionType questionType = questionTypeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Khong tim thay question type id")
        );
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();

        List<Questions> questionsList = questionRepository.findByQuestionType_Id(id);
        ListeningPassage listeningPassage;
        if (questionType.getType().equalsIgnoreCase("listening")) {
            listeningPassage = questionsList.get(0).getListeningPassage();
            ListeningPassageDto listeningPassageDto = listeningPassageMapper.toListeningPassageDto(
                    listeningPassage);
            questionCreateDto.setListeningPassageDto(listeningPassageDto);
        } else {
            listeningPassage = null;
        }
        ReadingPassage readingPassage;
        if (questionType.getType().equalsIgnoreCase("reading")) {
            readingPassage = questionsList.get(0).getReadingPassage();
            ReadingDto readingDto = readingMapper.toReadingDto(readingPassage);
            questionCreateDto.setReadingDto(readingDto);
        } else {
            readingPassage = null;
        }
        List<QuestionOption> questionOptionList = questionRepository.findQuestionOptionByQuestionTypeId(id);
        Map<Integer, List<QuestionOption>> questionOptionByQuestion = questionOptionList.stream().collect(Collectors.groupingBy(questionOption -> questionOption.getQuestions().getId()));
        QuestionTypeDto questionTypeDto = questionTypeMapper.toQuestionTypeDto(questionType);
        questionTypeDto.setLessonId(questionType.getLesson().getId());
        questionTypeDto.setQuestionTypeId(questionType.getId());

        List<QuestionDto> questionDtos = questionsList.stream().map(question -> {
            QuestionDto questionDto = questionMapper.toQuestionDto(question);
            if (readingPassage != null) {
                questionDto.setReadingPassageId(readingPassage.getId());
            }
            questionDto.setQuestionTypeId(questionType.getId());
            if (questionType.getType().equalsIgnoreCase("reading")
                    || questionType.getType().equalsIgnoreCase("mc")
                    || (listeningPassage != null && listeningPassage.getPassage_type().equalsIgnoreCase("long"))) {
                List<QuestionOption> questionOptions = questionOptionByQuestion.get(question.getId());
                List<QuestionOptionDto> questionOptionDtos = questionOptions.stream().map(questionOption -> {
                    QuestionOptionDto questionOptionDto = questionOptionMapper.toQuestionOptionDto(questionOption);
                    return questionOptionDto;
                }).toList();
                questionDto.setOptions(questionOptionDtos);
            }
            return questionDto;
        }).toList();
        questionTypeDto.setQuestions(questionDtos);
        questionCreateDto.setQuestionTypeDto(questionTypeDto);


        return questionCreateDto;
    }


}
