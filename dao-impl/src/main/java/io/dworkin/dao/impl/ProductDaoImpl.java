package io.dworkin.dao.impl;

import io.dworkin.dao.ProductDao;
import io.dworkin.db.MyConnectionPool;
import io.dworkin.model.ProductEntity;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created by yakov on 19.03.2017.
 */
public class ProductDaoImpl implements ProductDao {

    private final MyConnectionPool connectionPool;

    public ProductDaoImpl(MyConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    @Override
    public CompletableFuture<Optional<ProductEntity>> getByCode(String code) {
        CompletableFuture<Optional<ProductEntity>> future = new CompletableFuture<>();

        String query = "select * from product where code = $1";

        connectionPool.getDb().query(query, Arrays.asList(code), result -> {
            if (result.size() > 0)
                future.complete(Optional.of(
                        new ProductEntity(result.row(0).getString("code"), result.row(0).getString("displayName"),
                                result.row(0).getBigDecimal("price").doubleValue(), result.row(0).getString("description"),
                                result.row(0).getString("imageUrl"))));
            else
                future.complete(Optional.empty());
        }, future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<List<ProductEntity>> listByCategoryNameAndPropertyValues(String category, List<List<String>> propertyValues,
                                                                                      Integer first, Integer max, String orderProperty, Boolean isAsc) {

        final CompletableFuture<List<ProductEntity>> future = new CompletableFuture<>();

        final StringBuilder queryBuilder = new StringBuilder("select * from product as prod where category_id=(select id from category where name='").append(category).append("')");

        buildPropertyValuesSubqueries(propertyValues, queryBuilder);
        queryBuilder.append(" order by ").append(orderProperty);
        if (!isAsc)
            queryBuilder.append(" desc");
        queryBuilder.append(" limit ").append(max).append(" offset ").append(first);

        connectionPool.getDb().query(queryBuilder.toString(),
                queryRes -> {
                    List<ProductEntity> products = new ArrayList<>();
                    queryRes.forEach(row -> products.add(new ProductEntity(row.getString("code"), row.getString("displayName"),
                            row.getBigDecimal("price").doubleValue(), row.getString("description"), row.getString("imageUrl"))));
                    future.complete(products);
                }, future::completeExceptionally);

        return future;
    }

    private static void buildPropertyValuesSubqueries(List<List<String>> propertyValues, StringBuilder queryBuilder) {
        propertyValues.forEach(names -> {
            queryBuilder.append(" and exists (select * from product_property_value where prod.id=product_id and propertyvalues_id in (");
            for (int i = 0; i < names.size(); i++) {
                queryBuilder.append("(select id from property_value where name='").append(names.get(i)).append("')");
                if (i != names.size() - 1)
                    queryBuilder.append(",");
            }
            queryBuilder.append("))");
        });
    }
}
