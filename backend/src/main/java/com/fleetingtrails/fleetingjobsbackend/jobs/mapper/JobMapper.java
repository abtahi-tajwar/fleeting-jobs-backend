package com.fleetingtrails.fleetingjobsbackend.jobs.mapper;

import com.fleetingtrails.fleetingjobsbackend.company.entity.CompanyEntity;
import com.fleetingtrails.fleetingjobsbackend.jobs.dto.JobListItemDto;
import com.fleetingtrails.fleetingjobsbackend.jobs.entity.JobEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {
    JobListItemDto toJobListItemDto(JobEntity entity);
    JobEntity toJobEntity(JobListItemDto item);
}
