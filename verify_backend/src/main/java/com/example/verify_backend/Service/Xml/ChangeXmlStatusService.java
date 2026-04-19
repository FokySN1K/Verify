package com.example.verify_backend.Service.Xml;

import ch.qos.logback.core.util.StringUtil;
import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.UtilService.Notification.EmailSender;
import com.example.verify_backend.dto.ChangeXmlStatusRequest;
import com.example.verify_backend.dto.Result;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeXmlStatusService {

    private final XmlRepository xmlRepository;

    private final EmailSender emailSender;

    private final String MAIL_HEADER_STRING = "Смена статуса xml документа";

    public Result changeXmlStatusService(ChangeXmlStatusRequest request) {

        XmlLight xmlLight = xmlRepository.getLastXmlLightInfo(request.getXmlName(), request.getXsdId(), request.getContractId())
                .orElseThrow(() -> new BusinessLogicException("Не найдена информация по xml документу"));

        String textXml = String.format("У xml '%s' принадлежащий '%s' этап='%s' сменился статус с '%s' на '%s'. Зайдите в кабинет, чтобы узнать подробную информацию",
                xmlLight.getName(), xmlLight.getXsdLight().getName(), xmlLight.getXsdLight().getStage(), xmlLight.getStatus().name(), request.getXmlStatus().name());


        System.out.println(xmlLight);
        if (request.getClientId().equals(xmlLight.getContractor().getClientId())) {
            // Если клиент подрядчик - можем менять статус с PROCESSING -> CHECKING
            if (XmlStatus.PROCESSING.name().equals(xmlLight.getStatus().name())
                    && XmlStatus.CHECKING.name().equals(request.getXmlStatus().name())) {
                xmlRepository.changeXmlStatus(xmlLight, request.getXmlStatus());
                try {
                    emailSender.sendNotification(MAIL_HEADER_STRING, textXml, xmlLight.getCustomer().getEmail());
                } catch (Exception e) {
                    System.out.println("asdf");
                }

            } else {
                throw new BusinessLogicException("Подрядчик может менять статус только с 'PROCESSING' -> 'CHECKING'");
            }
        } else if (request.getClientId().equals(xmlLight.getCustomer().getClientId())) {
            // Если клиент подрядчик - можем менять статус с CHECKING -> REFUSED, CHECKING -> DONE
            if (XmlStatus.CHECKING.name().equals(xmlLight.getStatus().name())
                    && (XmlStatus.REFUSED.name().equals(request.getXmlStatus().name())
                          || XmlStatus.DONE.name().equals(request.getXmlStatus().name())) ) {

                if (XmlStatus.REFUSED.name().equals(request.getXmlStatus().name())) {
                    if (StringUtils.isBlank(request.getReason())) {
                        throw new BusinessLogicException("При смене статуса на 'REFUSED' необходимо описывать причину");
                    } else {
                        xmlLight.setReason(request.getReason());
                    }
                }

                xmlRepository.changeXmlStatus(xmlLight, request.getXmlStatus());

                try {
                    emailSender.sendNotification(MAIL_HEADER_STRING, textXml, xmlLight.getContractor().getEmail());
                } catch (Exception e) {
                    System.out.println("asdf");
                }

            } else {
                throw new BusinessLogicException("Заказчик может менять статус только с 'CHECKING' -> 'DONE', 'CHECKING' -> 'REFUSED'");
            }

        } else {
            throw new RuntimeException("Xml документ не принадлежит клиенту");
        }

        return new Result();
    }

}
