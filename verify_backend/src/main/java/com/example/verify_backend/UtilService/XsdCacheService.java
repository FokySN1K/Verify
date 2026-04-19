package com.example.verify_backend.UtilService;

import com.example.verify_backend.Entity.Xsd;
import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.XsdRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class XsdCacheService {

    private final ConcurrentHashMap<Long, Xsd> processingXsdCacheMap = new ConcurrentHashMap<>();

    private final XsdRepository xsdRepository;

    public Xsd getXsdDataByXsdId(@NotNull Long xsdId) {

        return processingXsdCacheMap.getOrDefault(xsdId, xsdRepository.getXsdByXsdId(xsdId)
                .orElseThrow(() -> new BusinessLogicException("Переданного xsd_id не существует")));
    }

    // Обновляем кэш 1 раз в 6 часов
    @Scheduled(fixedDelay = 6 * 60 * 60 * 1000, initialDelay = 6_000)
    private void renewProcessingXsdCacheMap() {
        List<Xsd> xsdList = xsdRepository.getProcessedXsdList();

        ConcurrentHashMap<Long, Xsd> newXsdCacheMap = new ConcurrentHashMap<>();

        for (Xsd xsd : xsdList) {
            newXsdCacheMap.put(xsd.getId(), xsd);
        }

        // Сначала положим всё новое, затем удалим отсутствующие элементы
        processingXsdCacheMap.putAll(newXsdCacheMap);
        processingXsdCacheMap.entrySet().removeIf(entry -> !newXsdCacheMap.containsKey(entry.getKey()));

    }


}
