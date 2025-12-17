package com.mycom.myapp.domain.admin.dto;
import com.mycom.myapp.domain.enums.Difficulty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryRequest {
    private String title;
}