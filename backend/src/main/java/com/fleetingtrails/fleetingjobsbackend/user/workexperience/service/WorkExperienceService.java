package com.fleetingtrails.fleetingjobsbackend.user.workexperience.service;

import com.fleetingtrails.fleetingjobsbackend.common.exception.ResourceNotFoundException;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import com.fleetingtrails.fleetingjobsbackend.user.workexperience.dto.WorkExperienceCreateDto;
import com.fleetingtrails.fleetingjobsbackend.user.workexperience.dto.WorkExperienceResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.workexperience.dto.WorkExperienceUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.user.workexperience.entity.WorkExperienceEntity;
import com.fleetingtrails.fleetingjobsbackend.user.workexperience.mapper.WorkExperienceMapper;
import com.fleetingtrails.fleetingjobsbackend.user.workexperience.repository.WorkExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final UserRepository userRepository;
    private final WorkExperienceMapper workExperienceMapper;

    public List<WorkExperienceResponseDto> getWorkExperiencesByUserId(Long userId) {
        return workExperienceRepository.findByUserId(userId).stream()
                .map(workExperienceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkExperienceResponseDto createWorkExperience(Long userId, WorkExperienceCreateDto createDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        WorkExperienceEntity experience = workExperienceMapper.toEntity(createDto);
        experience.setUser(user);

        WorkExperienceEntity savedExperience = workExperienceRepository.save(experience);
        return workExperienceMapper.toResponseDto(savedExperience);
    }

    @Transactional
    public WorkExperienceResponseDto updateWorkExperience(Long experienceId, WorkExperienceUpdateDto updateDto) {
        WorkExperienceEntity experience = workExperienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Work experience not found with id: " + experienceId));

        workExperienceMapper.updateEntity(experience, updateDto);
        WorkExperienceEntity updatedExperience = workExperienceRepository.save(experience);
        return workExperienceMapper.toResponseDto(updatedExperience);
    }

    @Transactional
    public void deleteWorkExperience(Long experienceId) {
        if (!workExperienceRepository.existsById(experienceId)) {
            throw new ResourceNotFoundException("Work experience not found with id: " + experienceId);
        }
        workExperienceRepository.deleteById(experienceId);
    }
}
