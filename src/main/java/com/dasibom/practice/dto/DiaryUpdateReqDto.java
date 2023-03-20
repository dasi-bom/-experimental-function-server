package com.dasibom.practice.dto;

import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.Stamp;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiaryUpdateReqDto {
    private Pet pet = null;
    private String title;
    private String content;
    private List<Stamp> stamps = null;
}
