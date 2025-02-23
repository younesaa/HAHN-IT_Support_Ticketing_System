package com.hahn.supportticketsystem.utils;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.hahn.supportticketsystem.dto.CommentResponseDTO;
import com.hahn.supportticketsystem.model.Comment;


// need to add ListMAppers as well

@Mapper
public interface CommentResponseMapper {
    CommentResponseMapper INSTANCE = Mappers.getMapper(CommentResponseMapper.class);

    CommentResponseDTO toDTO(Comment comment);
    Comment toEntity(CommentResponseDTO commentResponseDTO);
}