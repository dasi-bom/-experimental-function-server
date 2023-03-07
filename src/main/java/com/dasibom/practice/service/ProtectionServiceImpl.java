package com.dasibom.practice.service;

import static com.dasibom.practice.exception.ErrorCode.PET_NOT_FOUND;
import static com.dasibom.practice.exception.ErrorCode.USER_NOT_FOUND;

import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.User;
import com.dasibom.practice.dto.ProtectionEndReqDto;
import com.dasibom.practice.exception.CustomException;
import com.dasibom.practice.repository.PetRepository;
import com.dasibom.practice.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProtectionServiceImpl implements ProtectionService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional
    public void saveEndStatus(ProtectionEndReqDto protectionEndReqDto) {
        // TODO: 하드 코딩 변경
        User user = userRepository.findByUsername("test")
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 임보 종료일 update
        Pet pet = findPet(protectionEndReqDto.getPet(), user);
        pet.updateProtectionEndedAt();

        // 다시보기 컨텐츠 생성



    }

    private Pet findPet(Pet reqPet , User user) {
        Pet pet = null;
        if (reqPet != null) {
            pet = petRepository.findByPetNameAndOwner(reqPet.getPetName(), user)
                    .orElseThrow(() -> new CustomException(PET_NOT_FOUND));
        }
        return pet;
    }
}
