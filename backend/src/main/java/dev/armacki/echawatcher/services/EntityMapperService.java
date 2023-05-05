package dev.armacki.echawatcher.services;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EntityMapperService {

    private final Logger logger = LoggerFactory.getLogger(EntityMapperService.class);
    private final DozerBeanMapper dozerMapper = new DozerBeanMapper();
    private final Map<String, String> validClassMapping = Map.of(
            "Document", "DocumentDTO",
            "Substance", "SubstanceDTO",
            "Hazard", "HazardDTO",
            "SubstanceHazard", "SubstanceHazardDTO"
    );

    private final String DTO_PACKAGE_PATH = "dev.armacki.echawatcher.dtos";

    public Object mapObject(Object sourceObj, String destinationClassName) {
        if (sourceObj == null)
            throw new IllegalArgumentException("Source object is null");
        if (destinationClassName == null || destinationClassName.isBlank())
            throw new IllegalArgumentException("Destination class name is null");

        String sourceClassName = sourceObj.getClass().getSimpleName();
        if (!validClassMapping.containsKey(sourceClassName))
            return null;

        if (!validClassMapping.get(sourceClassName).equals(destinationClassName))
            return null;

        Object destinationObj;
        try {
            destinationObj = dozerMapper.map(sourceObj, Class.forName(DTO_PACKAGE_PATH + "." + destinationClassName));
        } catch (ClassNotFoundException ex) {
            logger.warn(String.format("Class name %s not found", destinationClassName), ex);
            return null;
        }

        return destinationObj;

    }
}
