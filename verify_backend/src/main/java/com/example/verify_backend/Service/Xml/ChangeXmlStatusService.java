package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.UtilService.Notification.EmailSender;
import com.example.verify_backend.dto.ChangeXmlStatusRequest;
import com.example.verify_backend.dto.Result;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChangeXmlStatusService {

    private static final String MAIL_HEADER_STRING = "Смена статуса xml документа";

    private final XmlRepository xmlRepository;
    private final EmailSender emailSender;

    public Result changeXmlStatusService(ChangeXmlStatusRequest request) {

        log.info("[changeXmlStatusService] Operation started");
        XmlLight xmlLight = xmlRepository.getLastXmlLightInfo(request.getXmlName(), request.getXsdId(), request.getContractId())
                .orElseThrow(() -> new BusinessLogicException("Не найдена информация по xml документу"));

        String textXml = String.format("У xml '%s' принадлежащий '%s' этап='%s' сменился статус с '%s' на '%s'. Зайдите в кабинет, чтобы узнать подробную информацию",
                xmlLight.getName(), xmlLight.getXsdLight().getName(), xmlLight.getXsdLight().getStage(), xmlLight.getStatus().name(), request.getXmlStatus().name());

        if (request.getClientId().equals(xmlLight.getContractor().getClientId())) {
            validateContractorTransition(xmlLight, request.getXmlStatus());

            xmlRepository.changeXmlStatus(xmlLight, request.getXmlStatus(), null);
            sendNotificationSafely(textXml, xmlLight.getCustomer().getEmail());
        } else if (request.getClientId().equals(xmlLight.getCustomer().getClientId())) {
            String reason = validateCustomerTransitionAndGetReason(request, xmlLight);

            xmlRepository.changeXmlStatus(xmlLight, request.getXmlStatus(), reason);
            sendNotificationSafely(textXml, xmlLight.getContractor().getEmail());
        } else {
            throw new BusinessLogicException("Xml документ не принадлежит клиенту");
        }

        log.info("[changeXmlStatusService] Operation finished");
        return new Result();
    }

    private void validateContractorTransition(XmlLight xmlLight, XmlStatus newStatus) {
        boolean isNewToProcessing = XmlStatus.NEW == xmlLight.getStatus() && XmlStatus.PROCESSING == newStatus;
        boolean isProcessingToChecking = XmlStatus.PROCESSING == xmlLight.getStatus() && XmlStatus.CHECKING == newStatus;

        if (!isNewToProcessing && !isProcessingToChecking) {
            throw new BusinessLogicException("Подрядчик может менять статус только с 'NEW' -> 'PROCESSING' или с 'PROCESSING' -> 'CHECKING'");
        }
    }

    private String validateCustomerTransitionAndGetReason(ChangeXmlStatusRequest request, XmlLight xmlLight) {
        if (XmlStatus.CHECKING != xmlLight.getStatus()
                || (XmlStatus.REFUSED != request.getXmlStatus() && XmlStatus.DONE != request.getXmlStatus())) {
            throw new BusinessLogicException("Заказчик может менять статус только с 'CHECKING' -> 'DONE' или с 'CHECKING' -> 'REFUSED'");
        }

        if (XmlStatus.REFUSED == request.getXmlStatus()) {
            if (StringUtils.isBlank(request.getReason())) {
                throw new BusinessLogicException("При смене статуса на 'REFUSED' необходимо описывать причину");
            }
            return request.getReason();
        }

        return null;
    }

    private void sendNotificationSafely(String text, String email) {
        try {
            emailSender.sendNotification(MAIL_HEADER_STRING, text, email);
        } catch (Exception e) {
            log.warn("Не удалось отправить уведомление на почту {}", email, e);
        }
    }
}
