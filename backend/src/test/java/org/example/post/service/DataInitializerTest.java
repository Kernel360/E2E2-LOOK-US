package org.example.post.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DataInitializerTest {

	@Mock
	private CategoryService categoryService;

	@InjectMocks
	private DataInitializer dataInitializer;

	@Test
	void testRun() throws Exception {
		// Initialize mocks
		MockitoAnnotations.openMocks(this);

		// Execute the run method
		dataInitializer.run();

		// Verify that the initializeCategories method was called
		verify(categoryService).initializeCategories();
	}
}
