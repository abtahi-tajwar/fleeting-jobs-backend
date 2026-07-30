package com.fleetingtrails.fleetingjobsbackend.user.education.service;

import com.fleetingtrails.fleetingjobsbackend.common.exception.ResourceNotFoundException;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import com.fleetingtrails.fleetingjobsbackend.user.education.dto.EducationCreateDto;
import com.fleetingtrails.fleetingjobsbackend.user.education.dto.EducationResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.education.dto.EducationUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.user.education.entity.EducationEntity;
import com.fleetingtrails.fleetingjobsbackend.user.education.mapper.EducationMapper;
import com.fleetingtrails.fleetingjobsbackend.user.education.repository.EducationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationService {

    private final EducationRepository educationRepository;
    private final UserRepository userRepository;
    private final EducationMapper educationMapper;

    public List<EducationResponseDto> getEducationsByUserId(Long userId) {
        return educationRepository.findByUserId(userId).stream()
                .map(educationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EducationResponseDto createEducation(Long userId, EducationCreateDto createDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        EducationEntity education = educationMapper.toEntity(createDto);
        education.setUser(user);

        EducationEntity savedEducation = educationRepository.save(education);
        return educationMapper.toResponseDto(savedEducation);
    }

    @Transactional
    public EducationResponseDto updateEducation(Long educationId, EducationUpdateDto updateDto) {
        EducationEntity education = educationRepository.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + educationId));

        educationMapper.updateEntity(education, updateDto);
        EducationEntity updatedEducation = educationRepository.save(education);
        return educationMapper.toResponseDto(updatedEducation);
    }

    @Transactional
    public void deleteEducation(Long educationId) {
        if (!educationRepository.existsById(educationId)) {
            throw new ResourceNotFoundException("Education not found with id: " + educationId);
        }
        educationRepository.deleteById(educationId);
    }
}
