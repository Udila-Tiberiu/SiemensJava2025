package com.siemens.internship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item testItem;

    @BeforeEach
    public void setUp() {
        testItem = new Item(1L, "Item1", "This is a test item", "New", "test@example.com");
    }

    @Test
    public void testFindAll() {
        List<Item> items = Collections.singletonList(testItem);
        Mockito.when(itemRepository.findAll()).thenReturn(items);

        List<Item> result = itemService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testItem.getName(), result.get(0).getName());
    }

    @Test
    public void testFindById() {
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        Optional<Item> result = itemService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(testItem.getName(), result.get().getName());
    }

    @Test
    public void testSave() {
        Mockito.when(itemRepository.save(testItem)).thenReturn(testItem);

        Item result = itemService.save(testItem);

        assertNotNull(result);
        assertEquals(testItem.getName(), result.getName());
    }

    @Test
    public void testDeleteById() {
        itemService.deleteById(1L);

        Mockito.verify(itemRepository, Mockito.times(1)).deleteById(1L);
    }
}
