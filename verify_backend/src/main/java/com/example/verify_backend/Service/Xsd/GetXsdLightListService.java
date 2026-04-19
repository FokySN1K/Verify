package com.example.verify_backend.Service.Xsd;

import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.Repository.XsdRepository;
import com.example.verify_backend.dto.GetXsdLightListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetXsdLightListService {

    private final XsdRepository xsdRepository;

    public GetXsdLightListResponse getXsdLightList() {

        List<XsdLight> xsdLightList = xsdRepository.getXsdLightList();

        return new GetXsdLightListResponse().setXsdLightList(xsdLightList);
    }

}
