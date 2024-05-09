package com.dku.council.domain.admin.service;

import com.dku.council.domain.admin.dto.StampPageDto;
import com.dku.council.domain.danfesta.exception.StampNotFoundException;
import com.dku.council.domain.danfesta.model.dto.response.ResponseStampForAdminDto;
import com.dku.council.domain.danfesta.model.entity.Stamp;
import com.dku.council.domain.danfesta.repository.StampRepository;
import com.dku.council.domain.danfesta.repository.spec.StampSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StampPageService {

    private final StampRepository stampRepository;

    public Page<StampPageDto> list(String studentId, Pageable pageable) {
        Specification<Stamp> spec = StampSpec.withStudentId(studentId);
        return stampRepository.findAll(spec, pageable).map(StampPageDto::new);
    }

    public Stamp findOne(String studentId) {
        return stampRepository.findByStudentId(studentId).orElseThrow(StampNotFoundException::new);
    }

    public void stamp(String studentId, int boothNumber) {
        String booth = "mission" + boothNumber;
        System.out.println("booth : " + booth);
        System.out.println("studentId : " + studentId);
        Stamp stamp = stampRepository.findByStudentId(studentId).orElseThrow(StampNotFoundException::new);
        stamp.stampToMission(booth);
    }
}
