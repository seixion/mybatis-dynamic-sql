/**
 *    Copyright 2016-2017 the original author or authors.
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
package org.mybatis.dynamic.sql.delete.render;

import java.util.Collections;
import java.util.Objects;

import org.mybatis.dynamic.sql.delete.DeleteModel;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.where.WhereModel;
import org.mybatis.dynamic.sql.where.render.WhereRenderer;
import org.mybatis.dynamic.sql.where.render.WhereSupport;

public class DeleteRenderer {
    private DeleteModel deleteModel;
    
    private DeleteRenderer(DeleteModel deleteModel) {
        this.deleteModel = Objects.requireNonNull(deleteModel);
    }
    
    public DeleteSupport render(RenderingStrategy renderingStrategy) {
        DeleteSupport.Builder builder = new DeleteSupport.Builder()
                .withTableName(deleteModel.table().name());
        
        deleteModel.whereModel().ifPresent(wm -> applyWhere(builder, wm, renderingStrategy));
        
        return builder.build();
    }
    
    private void applyWhere(DeleteSupport.Builder builder, WhereModel whereModel, RenderingStrategy renderingStrategy) {
        WhereSupport whereSupport = new WhereRenderer.Builder(whereModel, renderingStrategy, Collections.emptyMap())
                .build()
                .render();
        
        builder.withWhereClause(whereSupport.getWhereClause());
        builder.withParameters(whereSupport.getParameters());
    }
    
    public static DeleteRenderer of(DeleteModel deleteModel) {
        return new DeleteRenderer(deleteModel);
    }
}
