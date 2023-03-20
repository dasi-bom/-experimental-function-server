package com.dasibom.practice.controller;

import com.dasibom.practice.domain.Response;
import com.dasibom.practice.dto.ProtectionEndReqDto;
import com.dasibom.practice.service.ProtectionService;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/protection")
@Slf4j
public class ProtectionController {

    private final ProtectionService protectionService;

    @ApiOperation(value = "임보 종료하기", notes = "스탬프 별 다시보기 컨텐츠 생성")
    @PostMapping("/end")
    public Response saveEndStatus(@RequestBody @Valid ProtectionEndReqDto protectEndReqDto) {
        protectionService.saveEndStatus(protectEndReqDto);
        return new Response("OK", "임시보호 종료에 성공했습니다");
    }
}
