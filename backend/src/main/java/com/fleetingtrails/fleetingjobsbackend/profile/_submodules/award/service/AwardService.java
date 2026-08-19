package com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.service;

import com.fleetingtrails.fleetingjobsbackend.common.exception.ResourceNotFoundException;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.dto.AwardCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.dto.AwardResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.dto.AwardUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.entity.AwardEntity;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.mapper.AwardMapper;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.repository.AwardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final AwardRepository awardRepository;
    private final UserRepository userRepository;
    private final AwardMapper awardMapper;

    public List<AwardResponseDto> getAwardsByUserId(Long userId) {
        return awardRepository.findByUserId(userId).stream()
                .map(awardMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AwardResponseDto createAward(Long userId, AwardCreateDto createDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        AwardEntity award = awardMapper.toEntity(createDto);
        award.setUser(user);

        AwardEntity savedAward = awardRepository.save(award);
        return awardMapper.toResponseDto(savedAward);
    }

    @Transactional
    public AwardResponseDto updateAward(Long awardId, AwardUpdateDto updateDto) {
        AwardEntity award = awardRepository.findById(awardId)
                .orElseThrow(() -> new ResourceNotFoundException("Award not found with id: " + awardId));

        awardMapper.updateEntity(award, updateDto);
        AwardEntity updatedAward = awardRepository.save(award);
        return awardMapper.toResponseDto(updatedAward);
    }

    @Transactional
    public void deleteAward(Long awardId) {
        if (!awardRepository.existsById(awardId)) {
            throw new ResourceNotFoundException("Award not found with id: " + awardId);
        }
        awardRepository.deleteById(awardId);
    }
}