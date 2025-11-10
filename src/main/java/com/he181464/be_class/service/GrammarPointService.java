package com.he181464.be_class.service;

import com.he181464.be_class.dto.GrammarPointDto;

import java.util.List;

public interface GrammarPointService {
    List<GrammarPointDto> getByLessonId(int classroomId);

    GrammarPointDto create(GrammarPointDto grammarPointDto);

    GrammarPointDto updateGrammar(GrammarPointDto grammarPointDto);

    void deleteGrammar(Integer id);

    GrammarPointDto getById(Integer id);
}
