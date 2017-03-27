package io.dworkin.product.impl;

import akka.japi.Pair;
import com.github.pgasync.ConnectionPool;
import com.github.pgasync.Row;
import io.dworkin.product.api.Product;
import io.dworkin.product.api.PropertyWithCount;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * Created by yakov on 19.03.2017.
 */
public class ProductRepository {

    private final ConnectionPool connectionPool;

    public ProductRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public CompletionStage<Optional<Product>> getByCode(String code) {
        CompletableFuture<Optional<Product>> future = new CompletableFuture<>();

        String query = "select * from product where code = $1";

        connectionPool.query(query, singletonList(code), result -> {
            if (result.size() > 0)
                future.complete(Optional.of(
                        new Product(result.row(0).getString("code"), result.row(0).getString("displayName"),
                                result.row(0).getBigDecimal("price").doubleValue(), result.row(0).getString("description"),
                                result.row(0).getString("imageUrl"))));
            else
                future.complete(Optional.empty());
        }, future::completeExceptionally);

        return future;
    }

    public CompletionStage<PSequence<Product>> listByCategoryNameAndPropertyValues(String category, PSequence<PSequence<String>> propertyValues,
                                                                                   Integer first, Integer max, String orderProperty, Boolean isAsc) {

        final CompletableFuture<PSequence<Product>> future = new CompletableFuture<>();

        final StringBuilder queryBuilder = new StringBuilder("select * from product as prod where category_id=(select id from category where name='").append(category).append("')");

        buildPropertyValuesSubqueries(propertyValues, queryBuilder);
        queryBuilder.append(" order by ").append(orderProperty);
        if (!isAsc)
            queryBuilder.append(" desc");
        queryBuilder.append(" limit ").append(max).append(" offset ").append(first);

        connectionPool.query(queryBuilder.toString(),
                queryRes -> future.complete(TreePVector.from(StreamSupport.stream(queryRes.spliterator(), false).map(row ->
                        new Product(row.getString("code"), row.getString("displayName"),
                                row.getBigDecimal("price").doubleValue(), row.getString("description"),
                                row.getString("imageUrl"))).collect(toList()))), future::completeExceptionally);

        return future;
    }

    public CompletionStage<PSequence<PropertyWithCount>> countPropertyValuesByCategoryIdAndFilter(String category, String property, PSequence<Pair<String, PSequence<String>>> propertyValues) {

        final CompletableFuture<PSequence<PropertyWithCount>> future = new CompletableFuture<>();

        final StringBuilder queryBuilder = new StringBuilder("select prop.name as prop_name, prop.displayname as prop_displayname,")
                .append(" propval.name as propval_name, propval.displayname as propval_displayname, count(*) from product as prod")
                .append(" inner join product_property_value as ppv on product_id=prod.id")
                .append(" inner join property_value as propval on propval.id=ppv.propertyvalues_id")
                .append(" inner join property as prop on prop.id=propval.property_id")
                .append(" where prod.category_id=(select id from category where name='").append(category).append("')");

        if (property != null) {
            queryBuilder.append(" and prop.name = '").append(property).append("'");

            final PSequence<PSequence<String>> propertyValuesFiltered = TreePVector.from(propertyValues.stream()
                    .filter(pair -> !pair.first().equals(property)).map(Pair::second).collect(toList()));

            buildPropertyValuesSubqueries(propertyValuesFiltered, queryBuilder);
        } else
            buildPropertyValuesSubqueries(TreePVector.from(propertyValues.stream().map(Pair::second).collect(toList())), queryBuilder);

        final PSequence<String> flatPropertyValues = TreePVector.from(propertyValues.stream().flatMap(pair -> pair.second().stream()).collect(toList()));
        if (!flatPropertyValues.isEmpty()) {
            queryBuilder.append(" and propval.name not in (");
            for (int i = 0; i < flatPropertyValues.size(); i++) {
                queryBuilder.append("'").append(flatPropertyValues.get(i)).append("'");
                if (i != flatPropertyValues.size() - 1)
                    queryBuilder.append(",");
            }
            queryBuilder.append(")");
        }

        queryBuilder.append(" group by prop.name, prop.displayname, propval.name, propval.displayname, ppv.propertyvalues_id order by prop.displayname, propval.displayname");

        connectionPool.query(queryBuilder.toString(),
                queryRes -> {
                    final List<PropertyWithCount> result = new ArrayList<>();
                    PropertyWithCount resultItem = null;
                    for (Row row : queryRes) {
                        if (resultItem == null || !resultItem.name.equals(row.getString("prop_name"))) {
                            resultItem = new PropertyWithCount(row.getString("prop_name"), row.getString("prop_displayname"), TreePVector.empty());
                            result.add(resultItem);
                        }

                        resultItem.propertyValues.plus(new PropertyWithCount.PropertyValueWithCount(row.getString("propval_name"),
                                row.getString("propval_displayname"), row.getLong("count")));
                    }

                    future.complete(TreePVector.from(result));
                }, future::completeExceptionally);

        return future;
    }

    public CompletionStage<Boolean> create(Product product, String category, PSequence<String> propertyValues) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();

        final String query = "insert into product(id, code, displayname, description, imageurl, price, category_id) values (nextval('product_id_seq',$1,$2,$3,$4,$5,(select id from category where name=$6))";

        connectionPool.query(query, asList(product.code, product.displayName, product.description, product.imageUrl, product.price, category),
                result -> connectionPool.query(updatePropertyValuesQuery(product.code, propertyValues),
                        res2 -> future.complete(true), future::completeExceptionally), future::completeExceptionally);

        return future;
    }

    public CompletionStage<Boolean> update(Product product, String category, PSequence<String> propertyValues) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();

        final String query = "update product set displayname=$2, description=$3, imageurl=$4, price=$5, category_id=(select id from category where name=$6) where code=$1";

        connectionPool.query(query, asList(product.code, product.displayName, product.description, product.imageUrl, product.price, category),
                result -> connectionPool.query(updatePropertyValuesQuery(product.code, propertyValues),
                        res2 -> future.complete(true), future::completeExceptionally), future::completeExceptionally);

        return future;
    }

    private void buildPropertyValuesSubqueries(PSequence<PSequence<String>> propertyValues, StringBuilder queryBuilder) {
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

    private String updatePropertyValuesQuery(String code, PSequence<String> propertyValues) {
        final StringBuilder stringBuffer = new StringBuilder("delete from product_property_value where product_id = (select id from product where code = '");
        stringBuffer
                .append(code).append("');");

        if (!propertyValues.isEmpty()) {
            stringBuffer.append("INSERT INTO product_property_value(product_id, propertyvalues_id) VALUES");
            final Iterator<String> iterator = propertyValues.iterator();
            while (iterator.hasNext()) {
                final String propertyValueName = iterator.next();
                stringBuffer.append(" ((select id from product where code='").append(code)
                        .append("'), (select id from property_value where name='").append(propertyValueName).append("'))");
                if (iterator.hasNext())
                    stringBuffer.append(", ");
                else
                    stringBuffer.append(";");
            }
        }

        return stringBuffer.toString();
    }
}
