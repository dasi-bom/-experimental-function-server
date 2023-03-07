package com.dasibom.practice.dto;

import com.dasibom.practice.domain.Pet;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProtectionEndReqDto {

    @NotNull(message = "대상 동물은 필수 선택 값입니다.")
    private Pet pet;
}
