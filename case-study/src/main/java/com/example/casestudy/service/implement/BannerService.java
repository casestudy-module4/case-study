package com.example.casestudy.service.implement;

import com.example.casestudy.model.Banner;
import com.example.casestudy.repository.BannerRepository;
import com.example.casestudy.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService  implements IBannerService {
    @Autowired
    private BannerRepository bannerRepository;

    @Override
    public List<Banner> findAll() {
        return bannerRepository.findAll();
    }
}
