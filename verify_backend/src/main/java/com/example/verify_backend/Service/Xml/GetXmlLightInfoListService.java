package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.GetContractsResponse;
import com.example.verify_backend.dto.GetXmlLightInfoListRequest;
import com.example.verify_backend.dto.GetXmlLightInfoListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetXmlLightInfoListService {

    private final XmlRepository xmlRepository;

    public GetXmlLightInfoListResponse getXmlLightInfoList(GetXmlLightInfoListRequest request) {

        List<XmlLight> xmlLightList = xmlRepository.getXmlLightInfoList(request.getClientId())
                .stream()
                .sorted(Comparator.comparing(XmlLight::getName).thenComparing(XmlLight::getVersion))
                .toList();

        return new GetXmlLightInfoListResponse().setXmlLightList(xmlLightList);

    }

}
