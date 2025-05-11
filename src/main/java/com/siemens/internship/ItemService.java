package com.siemens.internship;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    // Constructor to inject ItemRepository
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    //List of items
    private List<Item> processedItems = new ArrayList<>();
    private int processedCount = 0;


    //Function that returns all the items from the repository
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    //Function that returns the 'Optional.of(item)' with the specific id from the repository
    //or 'Optional.empty()' if there is no item in the repository with the specific id
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    //Function that adds an item into the repository if it is valid and returns it
    public Item save(Item item) {
        return itemRepository.save(item); // No need for manual validation
    }

    //Function that deletes an item for the repository with the specific id
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }



    @Async
    public CompletableFuture<List<Item>> processItemsAsync() {
        //Thread-safe list to store processed items across multiple threads
        CopyOnWriteArrayList<Item> processedItems = new CopyOnWriteArrayList<>();

        //List to collect individual futures for each async item
        List<CompletableFuture<Item>> futures = new ArrayList<>();

        //Fetch all item IDs
        List<Long> itemIds = itemRepository.findAllIds();

        //Create and submit an asynchronous task for each item ID
        for (Long id : itemIds) {
            CompletableFuture<Item> future = CompletableFuture.supplyAsync(() -> {
                try {
                    //Processing delay
                    Thread.sleep(100);

                    //Retrieve the item by ID, or skip if not found
                    Optional<Item> optionalItem = itemRepository.findById(id);
                    if (optionalItem.isEmpty()) {
                        return null;
                    }

                    //Process the item by updating its status
                    Item item = optionalItem.get();
                    item.setStatus("Processed");

                    //Save the updated item and add it to the shared list
                    Item saved = itemRepository.save(item);
                    processedItems.add(saved);

                    return saved;
                } catch (Exception e) {
                    throw new CompletionException("Error processing item with ID " + id, e);
                }
            }, executor);
            //Track each future
            futures.add(future);
        }

        //Combine all async tasks into one that completes when all individual tasks finish,
        //and return the list of successfully processed items after all tasks are done
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> new ArrayList<>(processedItems));
    }

}

