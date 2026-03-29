package com.example.verify_backend.dto.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ChooseContractorRequest {

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("contractor_id")
    private String contractorId;
}
