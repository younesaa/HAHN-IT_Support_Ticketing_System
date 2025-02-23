package com.hahn.supportticketsystem.utils;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.hahn.supportticketsystem.dto.CommentCreationDTO;
import com.hahn.supportticketsystem.model.Comment;

@Mapper
public interface CommentCreationMapper {
    CommentCreationMapper INSTANCE = Mappers.getMapper(CommentCreationMapper.class);

    CommentCreationDTO toDTO(Comment comment);
    Comment toEntity(CommentCreationDTO commentCreationDTO);
}