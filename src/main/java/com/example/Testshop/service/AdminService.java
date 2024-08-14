package com.example.Testshop.service;

import com.example.Testshop.repository.GoodsRepository;
import com.example.Testshop.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final GoodsRepository goodsRepository;
    private final ModelRepository modelRepository;

}
