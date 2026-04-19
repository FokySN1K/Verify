package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.GetContractsResponse;
import com.example.verify_backend.dto.GetXmlLightInfoListRequest;
import com.example.verify_backend.dto.GetXmlLightInfoListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetXmlLightInfoListService {

    private final XmlRepository xmlRepository;

    public GetXmlLightInfoListResponse getXmlLightInfoList(GetXmlLightInfoListRequest request) {

        log.info("[getXmlLightInfoList] Operation started");
        List<XmlLight> xmlLightList = xmlRepository.getXmlLightInfoList(request.getClientId())
                .stream()
                .sorted(Comparator.comparing(XmlLight::getName).thenComparing(XmlLight::getVersion))
                .toList();

        log.info("[getXmlLightInfoList] Operation finished");
        return new GetXmlLightInfoListResponse().setXmlLightList(xmlLightList);

    }

}
