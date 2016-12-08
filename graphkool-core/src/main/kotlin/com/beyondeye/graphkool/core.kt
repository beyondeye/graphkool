package com.beyondeye.graphkool

import graphql.GraphQL
import graphql.execution.ExecutionStrategy
import graphql.schema.*

/**
 * Created by daely on 12/8/2016.
 */
fun newGraphQL(schemaBuilder: GraphQLSchema.Builder, executionStrategy: ExecutionStrategy?=null) = GraphQL(schemaBuilder.build(),executionStrategy)
fun newGraphQL(schema: GraphQLSchema, executionStrategy: ExecutionStrategy?=null) = GraphQL(schema,executionStrategy)
fun newObject(name:String)= GraphQLObjectType.newObject().name(name)
fun newEnum(name:String)= GraphQLEnumType.newEnum().name(name)
infix fun String.ofType(type_: GraphQLOutputType): GraphQLFieldDefinition.Builder {
    return GraphQLFieldDefinition.newFieldDefinition().type(type_).name(this)
}
operator fun String.rangeTo(type_: GraphQLOutputType) = this.ofType(type_)
infix fun GraphQLFieldDefinition.Builder.staticValue(value:Any) = this.staticValue(value)
infix fun GraphQLFieldDefinition.Builder.description(d:String) = this.description(d)
fun newGraphQLSchema( query: GraphQLObjectType.Builder)= GraphQLSchema.newSchema().query(query)