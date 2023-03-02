package com.dasibom.practice.domain.dto;

import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.Stamp;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiarySaveReqDto {

    @NotNull(message = "대상 동물은 필수 선택 값입니다.")
    private Pet pet;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;

    private List<Stamp> stamps = new ArrayList<>();
}
