package com.dasibom.practice.dto;

import com.dasibom.practice.domain.Pet;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProtectionDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class EndRequest {
        @NotNull(message = "대상 동물은 필수 선택 값입니다.")
        private Pet pet;
    }
}
