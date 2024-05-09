package com.dku.council.domain.danfesta.repository.spec;

import com.dku.council.domain.danfesta.model.FestivalDate;
import com.dku.council.domain.danfesta.model.entity.LineUp;
import org.springframework.data.jpa.domain.Specification;

public class LineUpSpec {

    public static Specification<LineUp> withFestivalDate(FestivalDate festivalDate) {
        if (festivalDate == null) {
            return Specification.where(null);
        }

        return (root, query, builder) ->
                builder.equal(root.get("festivalDate"), festivalDate);
    }
}
