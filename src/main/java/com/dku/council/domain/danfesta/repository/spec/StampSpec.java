package com.dku.council.domain.danfesta.repository.spec;

import com.dku.council.domain.danfesta.model.entity.Stamp;
import org.springframework.data.jpa.domain.Specification;

public class StampSpec {

    public static Specification<Stamp> withStudentId(String studentId) {
        if (studentId == null) {
            return Specification.where(null);
        }

        String pattern = "%" + studentId + "%";
        return (root, query, builder) ->
                builder.like(root.get("user").get("studentId"), pattern);
    }
}
