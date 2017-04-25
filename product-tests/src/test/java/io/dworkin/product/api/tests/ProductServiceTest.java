package io.dworkin.product.api.tests;

import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import io.dworkin.product.api.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.startServer;
import static java.util.Arrays.asList;
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
        final ListFilteredRequest request = new ListFilteredRequest("cpu", TreePVector.empty(), null, null, null, null, null);
        ListFilteredResponse response = productService.listFiltered().invoke(request).toCompletableFuture().get(5, SECONDS);
        assertEquals(100, response.products.size());
    }

    @Test
    public void listFilteredWithProperties() throws Exception {
        final ListFilteredRequest request = new ListFilteredRequest("cpu", TreePVector.from(asList(
                new PropertyRequest("manufacturer", TreePVector.singleton("intel")),
                new PropertyRequest("socket", TreePVector.from(asList("socket-1150", "socket-2011"))))),
                null, null, null, null, null);
        ListFilteredResponse response = productService.listFiltered().invoke(request).toCompletableFuture().get(5, SECONDS);
        assertEquals(80, response.products.size());
    }

    @Test
    public void countWithoutProperties() throws Exception {
        CountPropertyValuesRequest request = new CountPropertyValuesRequest("cpu", TreePVector.empty(), null);
        PSequence<PropertyWithCount> propertyValueResponse = productService.countPropertyValues().invoke(request).toCompletableFuture().get(5, SECONDS);
        assertEquals(2, propertyValueResponse.size());
    }

    @Test
    public void countWithProperties() throws Exception {
        CountPropertyValuesRequest request = new CountPropertyValuesRequest("cpu", TreePVector.singleton(
                new PropertyRequest("manufacturer", TreePVector.singleton("intel"))), null);
        PSequence<PropertyWithCount> propertyValueResponse = productService.countPropertyValues().invoke(request).toCompletableFuture().get(5, SECONDS);
        assertEquals(1, propertyValueResponse.size());
    }
}
