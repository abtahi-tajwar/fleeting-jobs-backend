package com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.service;

import com.fleetingtrails.fleetingjobsbackend.common.exception.ResourceNotFoundException;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.entity.CertificationEntity;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.mapper.CertificationMapper;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final CertificationMapper certificationMapper;

    public List<CertificationResponseDto> getCertificationsByUserId(Long userId) {
        return certificationRepository.findByUserId(userId).stream()
                .map(certificationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CertificationResponseDto createCertification(Long userId, CertificationCreateDto createDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        CertificationEntity certification = certificationMapper.toEntity(createDto);
        certification.setUser(user);

        CertificationEntity savedCertification = certificationRepository.save(certification);
        return certificationMapper.toResponseDto(savedCertification);
    }

    @Transactional
    public CertificationResponseDto updateCertification(Long certificationId, CertificationUpdateDto updateDto) {
        CertificationEntity certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found with id: " + certificationId));

        certificationMapper.updateEntity(certification, updateDto);
        CertificationEntity updatedCertification = certificationRepository.save(certification);
        return certificationMapper.toResponseDto(updatedCertification);
    }

    @Transactional
    public void deleteCertification(Long certificationId) {
        if (!certificationRepository.existsById(certificationId)) {
            throw new ResourceNotFoundException("Certification not found with id: " + certificationId);
        }
        certificationRepository.deleteById(certificationId);
    }
}
