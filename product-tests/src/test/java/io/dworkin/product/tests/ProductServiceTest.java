package io.dworkin.product.tests;

import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import io.dworkin.product.api.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.startServer;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
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
    public void listFilteredWithoutProperties() throws Exception {
        final ListFilteredRequest request = new ListFilteredRequest("cpu", emptyList(), null, null, null, null);
        List<Product> products = productService.listFiltered().invoke(request).toCompletableFuture().get(5, SECONDS);
        assertEquals(100, products.size());
    }

    @Test
    public void listFilteredWithProperties() throws Exception {
        final ListFilteredRequest request = new ListFilteredRequest("cpu", asList(
                new PropertyItem("manufacturer", singletonList("intel")),
                new PropertyItem("socket", asList("socket-1150", "socket-2011"))),
                null, null, null, null);
        List<Product> products = productService.listFiltered().invoke(request).toCompletableFuture().get(5, SECONDS);
        assertEquals(80, products.size());
    }

    @Test
    public void countWithoutProperties() throws Exception {
        CountPropertyValuesRequest request = new CountPropertyValuesRequest("cpu", emptyList());
        CountPropertyValuesResponse propertyValueResponse = productService.countPropertyValues().invoke(request).toCompletableFuture().get(5, SECONDS);
        assertEquals(2, propertyValueResponse.properties.size());
    }

    @Test
    public void countWithProperties() throws Exception {
        CountPropertyValuesRequest request = new CountPropertyValuesRequest("cpu", singletonList(
                new PropertyItem("manufacturer", singletonList("intel"))));
        CountPropertyValuesResponse propertyValueResponse = productService.countPropertyValues().invoke(request).toCompletableFuture().get(5, SECONDS);
        assertEquals(1, propertyValueResponse.properties.size());
    }
}
