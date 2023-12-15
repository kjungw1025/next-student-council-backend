package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithDankookService<E extends WithDankook> {

    protected final UserRepository userRepository;

    
}
