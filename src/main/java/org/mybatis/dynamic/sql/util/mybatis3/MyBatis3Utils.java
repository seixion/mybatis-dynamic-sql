/**
 *    Copyright 2016-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.dynamic.sql.util.mybatis3;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlTable;
import org.mybatis.dynamic.sql.delete.DeleteDSL;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.InsertDSL;
import org.mybatis.dynamic.sql.insert.MultiRowInsertDSL;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.CompletableQuery;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;

/**
 * Utility functions for building MyBatis3 mappers.
 * 
 * @author Jeff Butler
 *
 */
public class MyBatis3Utils {
    private MyBatis3Utils() {}

    public static long count(ToLongFunction<SelectStatementProvider> mapper,
            SqlTable table, SelectDSLCompleter completer) {
        return count(mapper, SelectDSL.select(SqlBuilder.count()).from(table), completer);
    }

    public static long count(ToLongFunction<SelectStatementProvider> mapper,
            QueryExpressionDSL<SelectModel> start, SelectDSLCompleter completer) {
        return mapper.applyAsLong(completer.apply(start).build().render(RenderingStrategies.MYBATIS3));
    }

    public static int deleteFrom(ToIntFunction<DeleteStatementProvider> mapper,
            SqlTable table, DeleteDSLCompleter completer) {
        return mapper.applyAsInt(
                completer.apply(DeleteDSL.deleteFrom(table))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }
    
    public static <R> int insert(ToIntFunction<InsertStatementProvider<R>> mapper, R record, 
            SqlTable table, UnaryOperator<InsertDSL<R>> completer) {
        return mapper.applyAsInt(completer.apply(
                InsertDSL.insert(record).into(table)).build().render(RenderingStrategies.MYBATIS3));
    }
    
    public static <R> int insertMultiple(ToIntFunction<MultiRowInsertStatementProvider<R>> mapper,
            Collection<R> records, SqlTable table, UnaryOperator<MultiRowInsertDSL<R>> completer) {
        return mapper.applyAsInt(completer.apply(
                MultiRowInsertDSL.insert(records).into(table)).build().render(RenderingStrategies.MYBATIS3));
    }
    
    public static <R> List<R> selectDistinct(Function<SelectStatementProvider, List<R>> mapper,
            BasicColumn[] selectList, SqlTable table, SelectDSLCompleter completer) {
        return selectDistinct(mapper, SelectDSL.selectDistinct(selectList).from(table), completer);
    }

    public static <R> List<R> selectDistinct(Function<SelectStatementProvider, List<R>> mapper,
            CompletableQuery<SelectModel> start, SelectDSLCompleter completer) {
        return mapper.apply(completer.apply(start).build().render(RenderingStrategies.MYBATIS3));
    }

    public static <R> List<R> selectList(Function<SelectStatementProvider, List<R>> mapper,
            BasicColumn[] selectList, SqlTable table, SelectDSLCompleter completer) {
        return selectList(mapper, SelectDSL.select(selectList).from(table), completer);
    }

    public static <R> List<R> selectList(Function<SelectStatementProvider, List<R>> mapper,
            CompletableQuery<SelectModel> start, SelectDSLCompleter completer) {
        return mapper.apply(completer.apply(start).build().render(RenderingStrategies.MYBATIS3));
    }

    public static <R> R selectOne(Function<SelectStatementProvider, R> mapper,
            BasicColumn[] selectList, SqlTable table, SelectDSLCompleter completer) {
        return selectOne(mapper, SelectDSL.select(selectList).from(table), completer);
    }

    public static <R> R selectOne(Function<SelectStatementProvider, R> mapper,
            CompletableQuery<SelectModel> start,
            SelectDSLCompleter completer) {
        return mapper.apply(completer.apply(start).build().render(RenderingStrategies.MYBATIS3));
    }

    public static int update(ToIntFunction<UpdateStatementProvider> mapper,
            SqlTable table, UpdateDSLCompleter completer) {
        return mapper.applyAsInt(
                completer.apply(UpdateDSL.update(table))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }
}