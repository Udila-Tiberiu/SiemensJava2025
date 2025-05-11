package com.siemens.internship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ItemServiceAsyncTest {

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
    public void testProcessItemsAsync() throws Exception {
        List<Long> ids = Arrays.asList(1L, 2L);
        Mockito.when(itemRepository.findAllIds()).thenReturn(ids);

        Item item1 = new Item(1L, "Item1", "This is a test item", "New", "test@example.com");
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        Mockito.when(itemRepository.findById(2L)).thenReturn(Optional.empty());

        Mockito.when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompletableFuture<List<Item>> future = itemService.processItemsAsync();
        List<Item> processedItems = future.get();

        assertNotNull(processedItems);
        assertEquals(1, processedItems.size()); // Only one was processed

        Item processed = processedItems.get(0);
        assertNotNull(processed);
        assertEquals("Processed", processed.getStatus());
    }

}
