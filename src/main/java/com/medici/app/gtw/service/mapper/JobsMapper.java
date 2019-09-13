package com.medici.app.gtw.service.mapper;

import com.medici.app.gtw.domain.*;
import com.medici.app.gtw.service.dto.JobsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Jobs and its DTO JobsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobsMapper extends EntityMapper<JobsDTO, Jobs> {



    default Jobs fromId(Long id) {
        if (id == null) {
            return null;
        }
        Jobs jobs = new Jobs();
        jobs.setId(id);
        return jobs;
    }
}
