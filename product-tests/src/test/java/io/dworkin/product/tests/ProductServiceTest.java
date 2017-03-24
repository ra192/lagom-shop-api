package io.dworkin.product.tests;

import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import io.dworkin.product.api.ListFilteredRequest;
import io.dworkin.product.api.Product;
import io.dworkin.product.api.ProductService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.startServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

/**
 * Created by yakov on 24.03.2017.
 */
public class ProductServiceTest {
    private static ServiceTest.TestServer server;
    private static ProductService productService;

    @BeforeClass
    public static void setUp() {
        server = startServer(defaultSetup().withCluster(false));
        productService = server.client(ProductService.class);
    }

    @AfterClass
    public static void tearDown() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Test
    public void listFiltered() throws Exception {
        final ListFilteredRequest request = new ListFilteredRequest("cpu", new ArrayList<>(), null, null, null, null);
        List<Product> products = productService.listFiltered().invoke(request).toCompletableFuture().get(5, SECONDS);
        assertEquals(100,products.size());
    }
}
