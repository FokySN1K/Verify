package com.example.verify_backend.Service.Xml;

import ch.qos.logback.core.util.StringUtil;
import com.example.verify_backend.Entity.Xml;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.GetContractsRequest;
import com.example.verify_backend.dto.GetContractsResponse;
import com.example.verify_backend.dto.GetXmlDataRequest;
import com.example.verify_backend.dto.GetXmlDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetXmlDataService {

    private final XmlRepository xmlRepository;

    public GetXmlDataResponse getXmlData(GetXmlDataRequest request) {

        log.info("[getXmlData] Operation started");
        Xml xml = xmlRepository.getXmlByXmlId(request.getXmlId())
                .orElseThrow(() -> new BusinessLogicException("Не найден xml документ"));

        if (!Strings.CS.equalsAny(request.getClientId(), xml.getContractor().getClientId(), xml.getCustomer().getClientId())) {
            throw new BusinessLogicException("Xml документ не принадлежит клиенту");
        }

        log.info("[getXmlData] Operation finished");
        return new GetXmlDataResponse().setXmlData(xml.getXmlData());
    }

}
