package org.knowledge4retail.api.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.converter.StoreConverter;
import org.knowledge4retail.api.store.dto.StoreDto;
import org.knowledge4retail.api.store.model.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class StoreConverterTest {

    private static final String TEST_ITEM_NO = "TEST";

    private StoreDto storeDto;
    private Store store;

    @BeforeEach
    public void setUp() {

        storeDto = StoreDto.builder().storeName("My Store").storeNumber("MFS001").build();
        store = Store.builder().storeName("Store").storeNumber("MFS002").build();
    }

    @Test
    public void testConvertDtoToModel()
    {
        Store newStore = StoreConverter.INSTANCE.dtoToStore(storeDto);
        Assertions.assertEquals(storeDto.getStoreName(), newStore.getStoreName());
        Assertions.assertEquals(storeDto.getStoreNumber(), newStore.getStoreNumber());
    }

    @Test
    public  void  testConvertModelToDto()
    {
        StoreDto newDto = StoreConverter.INSTANCE.storeToDto(store);
        Assertions.assertEquals(store.getStoreName(), newDto.getStoreName());
        Assertions.assertEquals(store.getStoreNumber(), newDto.getStoreNumber());
    }

    @Test
    public void testConvertStoreListCountMatches() {

        int storesToCreate = 5;

        List<Store> stores = new ArrayList<>();

        IntStream.range(0, storesToCreate).forEach(x -> stores.add(store));

        List<StoreDto> storeDtos = StoreConverter.INSTANCE.storesToDtos(stores);

        Assertions.assertNotNull(storeDtos);
        Assertions.assertEquals(storesToCreate, storeDtos.size());
        Assertions.assertEquals(store.getStoreName(), storeDtos.get(0).getStoreName());
    }

}
