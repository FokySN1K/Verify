package com.example.verify_backend.Controller;

import com.example.verify_backend.Entity.XsdEntity;
import com.example.verify_backend.Repository.XsdRepository;
import com.example.verify_backend.dto.GetItemsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController(XsdController.BASE_PREFIX)
public class XsdController {

    public static final String BASE_PREFIX = "/xsd";

    public static final String GET_XSD_INFO = BASE_PREFIX + "/get_xsd_info";

    private final XsdRepository xsdRepository;

    @PostMapping(GET_XSD_INFO)
    public GetItemsResponse<XsdEntity> getXsdInfo() {
        return new GetItemsResponse<>(xsdRepository.getXsdInfo());
    }

}
