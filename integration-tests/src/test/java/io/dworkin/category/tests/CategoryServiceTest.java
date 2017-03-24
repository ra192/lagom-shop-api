package io.dworkin.category.tests;

import io.dworkin.product.api.Category;
import io.dworkin.product.api.CategoryService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

/**
 * Created by yakov on 24.03.2017.
 */
public class CategoryServiceTest {

    private static TestServer server;
    private static CategoryService categoryService;

    @BeforeClass
    public static void setUp() {
        server = startServer(defaultSetup().withCluster(false));
        categoryService = server.client(CategoryService.class);
    }

    @AfterClass
    public static void tearDown() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Test
    public void listRoots() throws Exception {
        final List<Category> categories = categoryService.listRoots().invoke().toCompletableFuture().get(5, SECONDS);

        assertEquals("pc_parts",categories.get(0).name);
    }

    @Test
    public void listByParent() throws Exception {
        final List<Category> categories = categoryService.listByParent("pc_parts").invoke().toCompletableFuture().get(5, SECONDS);

        assertEquals("cpu",categories.get(0).name);
    }
}
