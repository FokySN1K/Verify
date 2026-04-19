package com.example.verify_backend.UtilService;

import com.example.verify_backend.Entity.Xsd;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.XsdRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class XsdCacheService {

    private final ConcurrentHashMap<Long, Xsd> processingXsdCacheMap = new ConcurrentHashMap<>();

    private final XsdRepository xsdRepository;

    public Xsd getXsdDataByXsdId(@NotNull Long xsdId) {
        log.info("[getXsdDataByXsdId] Operation started");
        Xsd cachedXsd = processingXsdCacheMap.get(xsdId);
        if (cachedXsd != null) {
            log.info("[getXsdDataByXsdId] Operation finished");
            return cachedXsd;
        }

        return xsdRepository.getXsdByXsdId(xsdId)
                .orElseThrow(() -> new BusinessLogicException("Переданного xsd_id не существует"));
    }

    @PostConstruct
    public void initCache() {
        renewProcessingXsdCacheMap();
    }

    // Обновляем кэш 1 раз в 6 часов
    @Scheduled(fixedDelay = 6 * 60 * 60 * 1000)
    public void renewProcessingXsdCacheMap() {
        List<Xsd> xsdList = xsdRepository.getProcessedXsdList();

        ConcurrentHashMap<Long, Xsd> newXsdCacheMap = new ConcurrentHashMap<>();

        for (Xsd xsd : xsdList) {
            newXsdCacheMap.put(xsd.getId(), xsd);
        }

        processingXsdCacheMap.clear();
        processingXsdCacheMap.putAll(newXsdCacheMap);
    }

}
