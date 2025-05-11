package com.siemens.internship;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testFindAllIds() {
        Item testItem = new Item(1L, "Item1", "This is a test item", "New", "test@example.com");
        itemRepository.save(testItem);
        List<Long> ids = itemRepository.findAllIds();
        assertNotNull(ids);
        assertFalse(ids.isEmpty());
    }

}

