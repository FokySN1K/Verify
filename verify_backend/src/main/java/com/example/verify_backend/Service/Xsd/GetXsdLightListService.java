package com.example.verify_backend.Service.Xsd;

import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.Repository.XsdRepository;
import com.example.verify_backend.dto.GetXsdLightListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetXsdLightListService {

    private final XsdRepository xsdRepository;

    public GetXsdLightListResponse getXsdLightList() {

        log.info("[getXsdLightList] Operation started");
        List<XsdLight> xsdLightList = xsdRepository.getXsdLightList();

        log.info("[getXsdLightList] Operation finished");
        return new GetXsdLightListResponse().setXsdLightList(xsdLightList);
    }

}
