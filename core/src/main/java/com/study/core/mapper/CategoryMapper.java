package com.study.core.mapper;

import com.study.core.dto.Category.CategoryDTO;
import com.study.core.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TopicMapper.class})
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);
}
