package org.knowledge4retail.api.device.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.filter.DeviceImageFilter;
import org.knowledge4retail.api.device.model.DeviceImage;
import org.knowledge4retail.api.device.repository.DeviceImageRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

import static org.knowledge4retail.api.shared.util.SearchUtil.copyNonNullProperties;
import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceImageMutation implements GraphQLMutationResolver {

    private final DeviceImageRepository deviceImageRepository;

    public DeviceImage updateDeviceImage (DeviceImage updateDeviceImage, DeviceImageFilter filter) throws IllegalAccessException{

        log.info("update oder post a single deviceImage using graphQL");
        if (filter != null) {

            Specification<DeviceImage> spec = getSpecification(filter, null);
            try {

                DeviceImage originalDeviceImage = deviceImageRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateDeviceImage.setId(originalDeviceImage.getId());
                copyNonNullProperties(updateDeviceImage, originalDeviceImage);
                return deviceImageRepository.save(originalDeviceImage);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return deviceImageRepository.save(updateDeviceImage);
    }

    public List<DeviceImage> postDeviceImages (List<DeviceImage> deviceImages){

        log.info("create a list of deviceImages using graphQL");
        return deviceImageRepository.saveAll(deviceImages);
    }
}
