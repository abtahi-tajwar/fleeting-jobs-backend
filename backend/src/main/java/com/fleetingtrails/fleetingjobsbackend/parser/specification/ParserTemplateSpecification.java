package com.fleetingtrails.fleetingjobsbackend.parser.specification;

import com.fleetingtrails.fleetingjobsbackend.parser.dto.ParserTemplateFilterDto;
import com.fleetingtrails.fleetingjobsbackend.parser.entity.ParserTemplateEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class ParserTemplateSpecification {

    private ParserTemplateSpecification () { }

    public static Specification<ParserTemplateEntity> withFilters (ParserTemplateFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.conjunction();
            }

            if (filter.getCompanyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), filter.getCompanyId()));
            }

            if (filter.getCompanyName() != null && !filter.getCompanyName().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("company").get("name")),
                                "%" + filter.getCompanyName().toLowerCase() + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
