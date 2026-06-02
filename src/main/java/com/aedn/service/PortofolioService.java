package com.aedn.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.aedn.entity.Portofolio;
import com.aedn.repository.PortofolioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortofolioService {
    
    private final PortofolioRepository portofolioRepository;

    // public List<Portofolio> getAll() {}


}
