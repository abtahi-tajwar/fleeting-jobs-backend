package com.fleetingtrails.fleetingjobsbackend.user.skill.service;

import com.fleetingtrails.fleetingjobsbackend.common.exception.ResourceNotFoundException;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import com.fleetingtrails.fleetingjobsbackend.user.skill.dto.SkillCreateDto;
import com.fleetingtrails.fleetingjobsbackend.user.skill.dto.SkillResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.skill.dto.SkillUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.user.skill.entity.SkillEntity;
import com.fleetingtrails.fleetingjobsbackend.user.skill.mapper.SkillMapper;
import com.fleetingtrails.fleetingjobsbackend.user.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final SkillMapper skillMapper;

    public List<SkillResponseDto> getSkillsByUserId(Long userId) {
        return skillRepository.findByUserId(userId).stream()
                .map(skillMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SkillResponseDto createSkill(Long userId, SkillCreateDto createDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        SkillEntity skillEntity = skillMapper.toEntity(createDto);
        skillEntity.setUser(user);

        SkillEntity savedSkill = skillRepository.save(skillEntity);
        return skillMapper.toResponseDto(savedSkill);
    }

    @Transactional
    public SkillResponseDto updateSkill(Long skillId, SkillUpdateDto updateDto) {
        SkillEntity skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillId));

        skillMapper.updateEntity(skill, updateDto);
        SkillEntity updatedSkill = skillRepository.save(skill);
        return skillMapper.toResponseDto(updatedSkill);
    }

    @Transactional
    public void deleteSkill(Long skillId) {
        if (!skillRepository.existsById(skillId)) {
            throw new ResourceNotFoundException("Skill not found with id: " + skillId);
        }
        skillRepository.deleteById(skillId);
    }
}
