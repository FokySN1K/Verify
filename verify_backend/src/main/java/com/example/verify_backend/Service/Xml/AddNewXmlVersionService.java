package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddNewXmlVersionService {

    private final XmlRepository xmlRepository;

    public Result addNewXmlVersion() {

        XmlLight xmlLight = xmlRepository.getLastXmlLightInfo()


    }

}
