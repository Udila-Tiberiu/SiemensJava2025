package com.siemens.internship;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Item testItem;

    @BeforeEach
    public void setUp() {
        itemRepository.deleteAll();
        testItem = new Item(null, "Item1", "This is a test item", "NEW", "test@example.com");
        testItem = itemRepository.save(testItem);
    }

    @Test
    public void testGetAllItems() throws Exception {
        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(testItem.getName()));
    }

    @Test
    public void testCreateItem() throws Exception {
        Item newItem = new Item(null, "Item2", "Another item", "NEW", "new@example.com");

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Item2"));
    }

    @Test
    public void testGetItemById() throws Exception {
        mockMvc.perform(get("/api/items/{id}", testItem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testItem.getName()));
    }

    @Test
    public void testGetItemByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/items/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateItem() throws Exception {
        testItem.setName("Updated Item");

        mockMvc.perform(put("/api/items/{id}", testItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Item"));
    }

    @Test
    public void testUpdateItemNotFound() throws Exception {
        Item updatedItem = new Item(null, "Updated Item", "Desc", "UPDATED", "test@example.com");

        mockMvc.perform(put("/api/items/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteItem() throws Exception {
        mockMvc.perform(delete("/api/items/{id}", testItem.getId()))
                .andExpect(status().isNoContent());

        assertThat(itemRepository.findById(testItem.getId())).isEmpty();
    }

}