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
    private final SubmissionHistoryRepository submissionHistoryRepository;
    private final SubmissionHistoryMapper submissionHistoryMapper;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final QuestionAnswerMapper questionAnswerMapper;
    private final ClassRoomStudentRepository classRoomStudentRepository;
    private final AccountRepository accountRepository;
    private final ExamRepository examRepository;
    @Transactional
    @Override
    public QuestionCreateDto createListQuestion(QuestionCreateDto questionCreateDto) {
        Lesson lesson = null;
        Exam exam = null;
        if (questionCreateDto.getQuestionTypeDto().getExamId() == null) {
            lesson = lessonRepository.findById(questionCreateDto.getQuestionTypeDto().getLessonId()).orElseThrow(() -> new IllegalArgumentException("Khong tim thay lesson voi id la"));
        } else {
            exam = examRepository.findById(questionCreateDto.getQuestionTypeDto().getExamId()).orElseThrow(() -> new IllegalArgumentException("Khong tim thay exam Id"));
        }
        ReadingDto readingDto = questionCreateDto.getReadingDto();
        ReadingPassage savedReading = null;
        if (readingDto != null) {
            ReadingPassage readingEntity = readingMapper.toReadingPassageEntity(readingDto);
            if (lesson != null)
            readingEntity.setLesson(lesson);
            else {
                readingEntity.setExam(exam);
            }
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
        if (exam == null) {
            questionType.setLesson(lesson);
        } else {
            questionType.setExam(exam);
        }
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
                if (listeningPassage.getPassage_type().equalsIgnoreCase("long")) {

                    isLong = true;
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
                        || isLong) {
                    List<QuestionOption> questionOptions = questionOptionByQuestion.get(question.getId());
                    List<QuestionOptionDto> questionOptionDtos = questionOptions.stream().map(questionOption -> {
                        QuestionOptionDto questionOptionDto = questionOptionMapper.toQuestionOptionDto(questionOption);
                        return questionOptionDto;
                    }).toList();
                    questionDto.setOptions(questionOptionDtos);
                }
                if (!isLong && questionType.getType().equalsIgnoreCase("listening")) {
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
                orElseThrow(() -> new IllegalArgumentException("Khong tim thay questio type id"));

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
        Boolean isLesson = false;
        if (questionCreateDto.getQuestionTypeDto() != null
                && questionCreateDto.getQuestionTypeDto().getLessonId() != null) {
            isLesson = true;
        }        QuestionTypeDto questionTypeDto = questionCreateDto.getQuestionTypeDto();
        QuestionType savedQuestionType = questionTypeMapper.toQuestionTypeEntity(questionTypeDto);
        if (isLesson) {
        savedQuestionType.setLesson(lessonRepository.findById(questionCreateDto.getQuestionTypeDto().getLessonId()).orElseThrow(
                () -> new IllegalArgumentException("Khong tim thay id")
        ));
        } else {
            savedQuestionType.setExam(examRepository.findById(questionCreateDto.
                    getQuestionTypeDto().getExamId()).orElseThrow(
                    () -> new IllegalArgumentException("Khong tim thay id")));
        }
        savedQuestionType.setId(questionTypeDto.getQuestionTypeId());
        ReadingPassage readingPassage = null;
        ListeningPassage listeningPassage = null;
        if (savedQuestionType.getType().equalsIgnoreCase("reading")) {
            readingPassage = readingMapper.toReadingPassageEntity(
                    questionCreateDto.getReadingDto());
            if (isLesson) {
                readingPassage.setLesson(savedQuestionType.getLesson());
            } else {
                readingPassage.setExam(savedQuestionType.getExam());
            }
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
        Boolean isLesson = false;
        if (questionCreateDto.getQuestionTypeDto() != null
                && questionCreateDto.getQuestionTypeDto().getLessonId() != null) {
            isLesson = true;
        }        List<Questions> questionsList = questionRepository.findByQuestionType_Id(id);
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
        if(isLesson) {
            questionTypeDto.setLessonId(questionType.getLesson().getId());
        }else{
            questionTypeDto.setExamId(questionType.getExam().getId());
        }
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

    @Override
    public List<AnswerCheckedDto> checkAnswer(List<AnswerCheckDto> answers) {
        List<AnswerCheckedDto> resultFail = new ArrayList<>();
        for (AnswerCheckDto answerCheckDto : answers) {
            Questions question = questionRepository.findById(answerCheckDto.getQuestionId()).orElseThrow(
                    () -> new IllegalArgumentException("Khong tim thay cau hoi voi id la" + answerCheckDto.getQuestionId())
            );
            if (question.getQuestionType().getType().equalsIgnoreCase("fill")) {
                if (!question.getCorrectAnswer().trim().equalsIgnoreCase(answerCheckDto.getTextAnswer().trim())) {
                    AnswerCheckedDto answerCheckedDto = new AnswerCheckedDto();
                    answerCheckedDto.setQuestionId(question.getId());
                    answerCheckedDto.setQuestionText(question.getQuestionText());
                    answerCheckedDto.setTextTrueAnswer(question.getCorrectAnswer());
                    resultFail.add(answerCheckedDto);
                }
            } else if (question.getQuestionType().getType().equalsIgnoreCase("mc")) {
                List<QuestionOption> correctOptions = question.getQuestionOptions().stream()
                        .filter(QuestionOption::getCorrectAnswer)
                        .toList();
                int c = 0;
                for (Integer checkOption : answerCheckDto.getSelected()) {
                    for (QuestionOption option : correctOptions) {
                        if (option.getId().equals(checkOption)) {
                            c++;
                        }
                    }
                }
                if (c == correctOptions.size() && c == answerCheckDto.getSelected().size()) {
                    continue;
                } else {
                    AnswerCheckedDto answerCheckedDto = new AnswerCheckedDto();
                    answerCheckedDto.setQuestionId(question.getId());
                    answerCheckedDto.setQuestionText(question.getQuestionText());
                    String optionTrueAnswer = "";
                    for (QuestionOption option : correctOptions) {
                        optionTrueAnswer += option.getOptionText() + "; ";
                    }
                    answerCheckedDto.setTextTrueAnswer(optionTrueAnswer);
                    resultFail.add(answerCheckedDto);
                }
            }

        }
        return resultFail;
    }

    @Override
    public List<SubmissionHistoryDto> getSubmissionHistoryByLesson(Long id) {
        List<SubmissionHistory> submissionHistories = submissionHistoryRepository.findByLessonId(id);
        List<SubmissionHistoryDto> submissionHistoryDtos= submissionHistories.stream().map(
                submissionHistoryMapper::toSubmissionHistoryDto
        ).toList();
        return submissionHistoryDtos;
    }

    @Override
    public List<QuestionAnswerDto> getQuestionAnswerFailBySubmissionHistory(Long id) {
        List<QuestionAnswers> questionAnswers = questionAnswerRepository.getIncorrectOrUnscoredAnswers(id);
        List<QuestionAnswerDto> questionAnswerDtos = questionAnswers.stream().map(
                item -> {
                    QuestionAnswerDto questionAnswerDto = questionAnswerMapper.toQuestionAnswerDto(item);
                    questionAnswerDto.setSubmissionHistoryId(item.getSubmissionHistory().getId());
                    return questionAnswerDto;
                }
        ).toList();
        return questionAnswerDtos;
    }
    public List<AnswerCheckedDto> checkAnswerVocab(List<AnswerCheckDto> answers, Long lessonId) {
        List<QuestionType> questionTypesFill = questionTypeRepository.findByLessonIdAndType(lessonId, "fill");
        List<QuestionType> questionTypesMc = questionTypeRepository.findByLessonIdAndType(lessonId, "mc");
        List<Questions> allQuestionTypes = new ArrayList<>();
        List<AnswerCheckedDto> resultFail = new ArrayList<>();
        for (QuestionType qt : questionTypesFill) {
            allQuestionTypes.addAll(qt.getQuestions());
        }
        for (QuestionType qt : questionTypesMc) {
            allQuestionTypes.addAll(qt.getQuestions());
        }
        for (Questions qt : allQuestionTypes) {
            int checkEx = 0;
            for (AnswerCheckDto answerCheckDto : answers) {
                Questions question = questionRepository.findById(answerCheckDto.getQuestionId()).orElseThrow(
                        () -> new IllegalArgumentException("Khong tim thay cau hoi voi id la" + answerCheckDto.getQuestionId())
                );
                if (question.getId() == qt.getId()) {
                    ++checkEx;
                    if (qt.getQuestionType().getType().equalsIgnoreCase("fill")) {
                        if (!question.getCorrectAnswer().trim().equalsIgnoreCase(answerCheckDto.getTextAnswer().trim())) {
                            AnswerCheckedDto answerCheckedDto = new AnswerCheckedDto();
                            answerCheckedDto.setQuestionId(question.getId());
                            answerCheckedDto.setQuestionText(question.getQuestionText());
                            answerCheckedDto.setTextTrueAnswer(question.getCorrectAnswer());
                            resultFail.add(answerCheckedDto);
                        }
                    } else if (qt.getQuestionType().getType().equalsIgnoreCase("mc")) {
                        List<QuestionOption> correctOptions = question.getQuestionOptions().stream()
                                .filter(QuestionOption::getCorrectAnswer)
                                .toList();
                        int c = 0;
                        for (Integer checkOption : answerCheckDto.getSelected()) {
                            for (QuestionOption option : correctOptions) {
                                if (option.getId().equals(checkOption)) {
                                    c++;
                                }
                            }
                        }
                        if (c == correctOptions.size() && c == answerCheckDto.getSelected().size()) {
                            continue;
                        } else {
                            AnswerCheckedDto answerCheckedDto = new AnswerCheckedDto();
                            answerCheckedDto.setQuestionId(question.getId());
                            answerCheckedDto.setQuestionText(question.getQuestionText());
                            String optionTrueAnswer = "";
                            for (QuestionOption option : correctOptions) {
                                optionTrueAnswer += option.getOptionText() + "; ";
                            }
                            answerCheckedDto.setTextTrueAnswer(optionTrueAnswer);
                            resultFail.add(answerCheckedDto);
                        }

                    }
                }

            }
            if (checkEx == 0) {
                AnswerCheckedDto answerCheckedDto = new AnswerCheckedDto();
                answerCheckedDto.setQuestionId(qt.getId());
                answerCheckedDto.setQuestionText(qt.getQuestionText());
                if (qt.getQuestionType().getType().equalsIgnoreCase("fill")) {
                    answerCheckedDto.setTextTrueAnswer(qt.getCorrectAnswer());
                } else if (qt.getQuestionType().getType().equalsIgnoreCase("mc")) {
                    String optionTrueAnswer = "";
                    List<QuestionOption> correctOptions = qt.getQuestionOptions().stream()
                            .filter(QuestionOption::getCorrectAnswer)
                            .toList();
                    for (QuestionOption option : correctOptions) {
                        optionTrueAnswer += option.getOptionText() + "; ";
                    }
                    answerCheckedDto.setTextTrueAnswer(optionTrueAnswer);
                }
                resultFail.add(answerCheckedDto);
            }
        }
        return resultFail;
    }

    @Override
    public List<AnswerCheckedDto> checkAnswerListen(List<AnswerCheckDto> answers, Long lessonId) {
        List<QuestionType> questionTypesFill = questionTypeRepository.findByLessonIdAndType(lessonId, "listening");
        List<Questions> allQuestionTypes = new ArrayList<>();
        List<AnswerCheckedDto> resultFail = new ArrayList<>();
        for (QuestionType qt : questionTypesFill) {
            allQuestionTypes.addAll(qt.getQuestions());
        }
        for (Questions qt : allQuestionTypes) {
            int checkEx = 0;
            for (AnswerCheckDto answerCheckDto : answers) {
                Questions question = questionRepository.findById(answerCheckDto.getQuestionId()).orElseThrow(
                        () -> new IllegalArgumentException("Khong tim thay cau hoi voi id la" + answerCheckDto.getQuestionId())
                );
                if (question.getId() == qt.getId()) {
                    ++checkEx;
                    if (qt.getListeningPassage().getPassage_type().equalsIgnoreCase("long")) {
                        List<QuestionOption> correctOptions = question.getQuestionOptions().stream()
                                .filter(QuestionOption::getCorrectAnswer)
                                .toList();
                        int c = 0;
                        for (Integer checkOption : answerCheckDto.getSelected()) {
                            for (QuestionOption option : correctOptions) {
                                if (option.getId().equals(checkOption)) {
                                    c++;
                                }
                            }
                        }
                        if (c == correctOptions.size() && c == answerCheckDto.getSelected().size()) {
                            continue;
                        } else {
                            AnswerCheckedDto answerCheckedDto = new AnswerCheckedDto();
                            answerCheckedDto.setQuestionId(question.getId());
                            answerCheckedDto.setQuestionText(question.getQuestionText());
                            String optionTrueAnswer = "";
                            for (QuestionOption option : correctOptions) {
                                optionTrueAnswer += option.getOptionText() + "; ";
                            }
                            answerCheckedDto.setTextTrueAnswer(optionTrueAnswer);
                            resultFail.add(answerCheckedDto);
                        }
                    } else if (qt.getListeningPassage().getPassage_type().equalsIgnoreCase("short")) {
                        if (!question.getCorrectAnswer().trim().equalsIgnoreCase(answerCheckDto.getTextAnswer().trim())) {
                            AnswerCheckedDto answerCheckedDto = new AnswerCheckedDto();
                            answerCheckedDto.setQuestionId(question.getId());
                            answerCheckedDto.setQuestionText(question.getQuestionText());
                            answerCheckedDto.setTextTrueAnswer(question.getCorrectAnswer());
                            resultFail.add(answerCheckedDto);
                        }
                    }

                }

            }
            if (checkEx == 0) {
                AnswerCheckedDto answerCheckedDto = new AnswerCheckedDto();
                answerCheckedDto.setQuestionId(qt.getId());
                answerCheckedDto.setQuestionText(qt.getQuestionText());
                if (qt.getListeningPassage().getPassage_type().equalsIgnoreCase("short")) {
                    answerCheckedDto.setTextTrueAnswer(qt.getCorrectAnswer());
                } else if (qt.getListeningPassage().getPassage_type().equalsIgnoreCase("long")) {
                    String optionTrueAnswer = "";
                    List<QuestionOption> correctOptions = qt.getQuestionOptions().stream()
                            .filter(QuestionOption::getCorrectAnswer)
                            .toList();
                    for (QuestionOption option : correctOptions) {
                        optionTrueAnswer += option.getOptionText() + "; ";
                    }
                    answerCheckedDto.setTextTrueAnswer(optionTrueAnswer);
                }
                resultFail.add(answerCheckedDto);
            }

        }
        return resultFail;
    }

    @Override
    public List<AnswerCheckedDto> checkAnswerReading(List<AnswerCheckDto> answers, Long lessonId) {
        List<QuestionType> questionTypesFill = questionTypeRepository.findByLessonIdAndType(lessonId, "reading");
        List<Questions> allQuestionTypes = new ArrayList<>();
        List<AnswerCheckedDto> resultFail = new ArrayList<>();
        for (QuestionType qt : questionTypesFill) {
            allQuestionTypes.addAll(qt.getQuestions());
        }
        for (Questions qt : allQuestionTypes) {
            int checkEx = 0;
            for (AnswerCheckDto answerCheckDto : answers) {
                Questions question = questionRepository.findById(answerCheckDto.getQuestionId()).orElseThrow(
                        () -> new IllegalArgumentException("Khong tim thay cau hoi voi id la" + answerCheckDto.getQuestionId())
                );
                if (question.getId() == qt.getId()) {
                    ++checkEx;
                    if (qt.getReadingPassage() != null) {
                        List<QuestionOption> correctOptions = question.getQuestionOptions().stream()
                                .filter(QuestionOption::getCorrectAnswer)
                                .toList();
                        int c = 0;
                        for (Integer checkOption : answerCheckDto.getSelected()) {
                            for (QuestionOption option : correctOptions) {
                                if (option.getId().equals(checkOption)) {
                                    c++;
                                }
                            }
                        }
                        if (c == correctOptions.size() && c == answerCheckDto.getSelected().size()) {
                            continue;
                        } else {
                            AnswerCheckedDto answerCheckedDto = new AnswerCheckedDto();
                            answerCheckedDto.setQuestionId(question.getId());
                            answerCheckedDto.setQuestionText(question.getQuestionText());
                            String optionTrueAnswer = "";
                            for (QuestionOption option : correctOptions) {
                                optionTrueAnswer += option.getOptionText() + "; ";
                            }
                            answerCheckedDto.setTextTrueAnswer(optionTrueAnswer);
                            resultFail.add(answerCheckedDto);
                        }
                    }
                }

            }
            if (checkEx == 0) {
                AnswerCheckedDto answerCheckedDto = new AnswerCheckedDto();
                answerCheckedDto.setQuestionId(qt.getId());
                answerCheckedDto.setQuestionText(qt.getQuestionText());
                for( QuestionOption option : qt.getQuestionOptions().stream()
                        .filter(QuestionOption::getCorrectAnswer)
                        .toList()) {
                    String optionTrueAnswer = "";
                    optionTrueAnswer += option.getOptionText() + "; ";
                    answerCheckedDto.setTextTrueAnswer(optionTrueAnswer);
                }
                resultFail.add(answerCheckedDto);
            }

        }
        return resultFail;
    }

    @Override
    public List<ListeningPassageDto> getListeningPassageByLessonId(Long lessonId) {
        List<ListeningPassage> listeningPassages = listeningPassageRepository.findByLessonId(lessonId);
        List<ListeningPassageDto> listeningPassageDtos = listeningPassages.stream().map(p -> {
                    ListeningPassageDto listeningPassageDto = listeningPassageMapper.toListeningPassageDto(p);
                    List<Questions> questions = p.getQuestions();
                    List<QuestionDto> questionDtos = questions.stream().map(question -> {
                        QuestionDto questionDto = questionMapper.toQuestionDto(question);
                        questionDto.setQuestionTypeId(question.getQuestionType().getId());
                        List<QuestionOption> questionOptions = question.getQuestionOptions();
                        if (questionOptions != null) {
                            List<QuestionOptionDto> questionOptionDtos = questionOptions.stream().map(questionOption -> {
                                QuestionOptionDto questionOptionDto = questionOptionMapper.toQuestionOptionDto(questionOption);
                                return questionOptionDto;
                            }).toList();
                            questionDto.setOptions(questionOptionDtos);
                        }
                        return questionDto;
                    }).toList();
                    listeningPassageDto.setQuestions(questionDtos);
                    return listeningPassageDto;
                }
        ).toList();
        return listeningPassageDtos;
    }

    @Override
    public List<ReadingDto> getReadingPassageByLessonId(Long lessonId) {
        List<ReadingPassage> readingPassages = readingRepository.findByLessonId(lessonId);
        List<ReadingDto> readingDtos = readingPassages.stream().map(p -> {
                    ReadingDto readingDto = readingMapper.toReadingDto(p);
                    List<Questions> questions = p.getQuestions();
                    List<QuestionDto> questionDtos = questions.stream().map(question -> {
                        QuestionDto questionDto = questionMapper.toQuestionDto(question);
                        questionDto.setQuestionTypeId(question.getQuestionType().getId());
                        List<QuestionOption> questionOptions = question.getQuestionOptions();
                        if (questionOptions != null) {
                            List<QuestionOptionDto> questionOptionDtos = questionOptions.stream().map(questionOption -> {
                                QuestionOptionDto questionOptionDto = questionOptionMapper.toQuestionOptionDto(questionOption);
                                return questionOptionDto;
                            }).toList();
                            questionDto.setOptions(questionOptionDtos);
                        }
                        return questionDto;
                    }).toList();
                    readingDto.setQuestions(questionDtos);
                    return readingDto;
                }
        ).toList();
        return readingDtos;
    }

    @Override
    public List<HomeWorkStatusDto> getStatusOfSubmission(Long lessonId) {
        List<SubmissionHistory> submissions = submissionHistoryRepository.findLatestSubmissionPerStudentPerType(lessonId);
        Map<Long, List<SubmissionHistory>> submissionsByStudent = submissions.stream().collect(Collectors.groupingBy(submissionHistory -> submissionHistory.getAccount().getId()));


        List<String> typeHomework = questionTypeRepository.findDistinctTypesByLessonId(lessonId);
        if (typeHomework.contains("mc") || typeHomework.contains("fill")) {
            typeHomework.remove("mc");
            typeHomework.remove("fill");
            typeHomework.add("vocab");
        }
        Long classRoomId = lessonRepository.findById(lessonId).orElseThrow(() -> new IllegalArgumentException("Khong the tim thay claasroom")).getClassRoomId();
        Map<Long, String> listIdOfStudent = classRoomStudentRepository
                .findByClassRoomId(classRoomId).stream().map(item -> {
                    Long id = item.getStudentId();
                    String email = accountRepository.findById(id).orElseThrow(() ->
                            new IllegalArgumentException("Khong tim thay student Id: " + id)).getEmail();

                    return Map.entry(id, email);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        LocalDateTime deadline = lessonRepository.findById(lessonId).orElseThrow(() -> new IllegalArgumentException("Khong tim thay lesson ID")).getHomeworkDeadline();
        List<HomeWorkStatusDto> homeWorkStatusDtos = new ArrayList<>();
        for (Map.Entry<Long, List<SubmissionHistory>> entry : submissionsByStudent.entrySet()) {
            Long studentId = entry.getKey();
            List<SubmissionHistory> submissionHistories = entry.getValue();
            String studentName = listIdOfStudent.get(studentId);
            List<String> types = new ArrayList<>(typeHomework);
            for (SubmissionHistory submissionHistory : submissionHistories) {
                HomeWorkStatusDto homeWorkStatusDto = new HomeWorkStatusDto();
                if (submissionHistory.getSubmittedAt().isBefore(deadline)) {
                    homeWorkStatusDto.setStatus("Done");

                } else {
                    homeWorkStatusDto.setStatus("Late");
                }
                homeWorkStatusDto.setSubmittedAt(submissionHistory.getSubmittedAt());
                homeWorkStatusDto.setNumberWrong(submissionHistory.getNumberWrong());
                homeWorkStatusDto.setStudentName(studentName);
                homeWorkStatusDto.setAnswerText(submissionHistory.getAnswerWriting());
                homeWorkStatusDto.setType(submissionHistory.getType());
                types.remove(submissionHistory.getType());
                homeWorkStatusDtos.add(homeWorkStatusDto);
            }
            if (types != null) {
                for (String type : types) {
                    HomeWorkStatusDto homeWorkStatusDto = new HomeWorkStatusDto();
                    homeWorkStatusDto.setStudentName(studentName);
                    homeWorkStatusDto.setType(type);
                    homeWorkStatusDto.setStatus("Missing");
                    homeWorkStatusDtos.add(homeWorkStatusDto);
                }

            }
            listIdOfStudent.remove(studentId);

        }
        if (listIdOfStudent != null) {
            for (Map.Entry<Long, String> entry : listIdOfStudent.entrySet()) {
                String studentName = entry.getValue();
                for (String type : typeHomework) {
                    HomeWorkStatusDto homeWorkStatusDto = new HomeWorkStatusDto();
                    homeWorkStatusDto.setStudentName(studentName);
                    homeWorkStatusDto.setType(type);
                    homeWorkStatusDto.setStatus("Missing");
                    homeWorkStatusDtos.add(homeWorkStatusDto);
                }
            }

        }
        return homeWorkStatusDtos;

    }

    @Override
    public List<QuestionCreateDto> getListQuestionByExam(Integer id) {
        List<QuestionType> questionTypes = questionTypeRepository.findByExamId(id);
        List<Questions> questionsList = questionRepository.findByQuestionType_ExamId(id);
        List<QuestionOption> questionOptionList = questionRepository.findQuestionOptionByExamId(id);
        Map<Integer, List<Questions>> questionByTypeId = questionsList.stream().collect(Collectors.groupingBy(questions -> questions.getQuestionType().getId()));
        Map<Integer, List<QuestionOption>> questionOptionByQuestion = questionOptionList.stream().collect(Collectors.groupingBy(questionOption -> questionOption.getQuestions().getId()));
        List<QuestionCreateDto> questionCreateDtos = new ArrayList<>();
        for (QuestionType questionType : questionTypes) {
            QuestionCreateDto questionCreateDto = new QuestionCreateDto();
            QuestionTypeDto questionTypeDto = questionTypeMapper.toQuestionTypeDto(questionType);
            questionTypeDto.setExamId(id);
            questionTypeDto.setQuestionTypeId(questionType.getId());
            List<Questions> questions = questionByTypeId.get(questionTypeDto.getQuestionTypeId());

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
                ) {
                    List<QuestionOption> questionOptions = questionOptionByQuestion.get(question.getId());
                    List<QuestionOptionDto> questionOptionDtos = questionOptions.stream().map(questionOption -> {
                        QuestionOptionDto questionOptionDto = questionOptionMapper.toQuestionOptionDto(questionOption);
                        return questionOptionDto;
                    }).toList();
                    questionDto.setOptions(questionOptionDtos);
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
}



