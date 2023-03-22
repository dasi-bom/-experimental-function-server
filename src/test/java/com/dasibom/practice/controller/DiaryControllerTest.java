package com.dasibom.practice.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import com.dasibom.practice.dto.DiaryDto;
import com.dasibom.practice.service.DiaryService;
import com.dasibom.practice.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(DiaryController.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
class DiaryControllerTest {

    @MockBean
    private DiaryService diaryService;
    @MockBean
    private S3Service s3Service;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(springSecurity())    // springSecurity설정이 되어있지 않으면 생략
                .apply(documentationConfiguration(restDocumentation))
                .apply(sharedHttpSession())
                .build();
    }

    private DiaryDto.DetailResponse createDiaryResponse() {
        return DiaryDto.DetailResponse.builder()
                .title("titleTest")
                .content("contentTest")
                .petName("gomgom")
                .imgUrls(
                        List.of("https://user-images.githubusercontent.com/59405576/226525170-7f2465ab-6ed5-47f6-9d88-38c39481f14a.jpeg"))
                .stampTypes(List.of("WALK", "TREAT"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("싱글 다이어리를 조회한다.")
    void getDetailedDiary() throws Exception {
        Long id = 1L;
        DiaryDto.DetailResponse diary = createDiaryResponse();
        given(diaryService.getDetailedDiary(id)).willReturn(diary);
//        given(diaryService.getDetailedDiary(any())).willReturn(diary);

        mockMvc.perform(get("/diary/detail/{diaryId}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("getDetailedDiary",
                        pathParameters(
                                parameterWithName("diaryId").description("조회할 일기의 ID")
                        ),
                        responseFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("일기 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("일기 내용"),
                                fieldWithPath("petName").type(JsonFieldType.STRING).description("일기 대상(동물)"),
                                fieldWithPath("imgUrls").type(JsonFieldType.ARRAY).description("이미지(path) 리스트"),
                                fieldWithPath("stampTypes").type(JsonFieldType.ARRAY).description("스탬프 리스트"),
                                fieldWithPath("createdAt").type(JsonFieldType.VARIES).description("일기 작성일")
                        )));
    }


}