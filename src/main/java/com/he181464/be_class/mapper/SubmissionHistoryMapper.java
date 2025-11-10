package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.SubmissionHistoryDto;
import com.he181464.be_class.entity.SubmissionHistory;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)

public interface SubmissionHistoryMapper {
    SubmissionHistory toSubmissionHistoryEntity(SubmissionHistoryDto submissionHistory);

    SubmissionHistoryDto toSubmissionHistoryDto(SubmissionHistory submissionHistory);
}
