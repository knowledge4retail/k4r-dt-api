package org.knowledge4retail.api.store.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.BarcodeFilter;
import org.knowledge4retail.api.store.model.Barcode;
import org.knowledge4retail.api.store.repository.BarcodeRepository;
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
public class BarcodeMutation implements GraphQLMutationResolver {

    private final BarcodeRepository barcodeRepository;

    public Barcode updateBarcode (Barcode updateBarcode, BarcodeFilter filter) throws IllegalAccessException{

        log.info("update oder post a single barcode using graphQL");
        if (filter != null) {

            Specification<Barcode> spec = getSpecification(filter, null);
            try {

                Barcode originalBarcode = barcodeRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateBarcode.setId(originalBarcode.getId());
                copyNonNullProperties(updateBarcode, originalBarcode);
                return barcodeRepository.save(originalBarcode);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return barcodeRepository.save(updateBarcode);
    }

    public List<Barcode> postBarcodes (List<Barcode> barcodes){

        log.info("create a list of barcodes using graphQL");
        return barcodeRepository.saveAll(barcodes);
    }
}
