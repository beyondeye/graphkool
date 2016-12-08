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
fun newInterface(name:String)=GraphQLInterfaceType.newInterface().name(name)
infix fun String.ofType(type_: GraphQLOutputType): GraphQLFieldDefinition.Builder {
    return GraphQLFieldDefinition.newFieldDefinition().type(type_).name(this)
}
operator fun String.rangeTo(type_: GraphQLOutputType) = this.ofType(type_)

fun GraphQLType.nonNull() = GraphQLNonNull(this)
operator fun GraphQLType.not() = GraphQLNonNull(this)

infix fun GraphQLFieldDefinition.Builder.staticValue(value:Any) = this.staticValue(value)
infix fun GraphQLFieldDefinition.Builder.description(d:String) = this.description(d)
infix fun GraphQLFieldDefinition.Builder.dataFetcher(fetcher:DataFetcher) = this.dataFetcher(fetcher)

infix fun GraphQLFieldDefinition.Builder.argument(builder: GraphQLArgument.Builder) = this.argument(builder)

fun newGraphQLSchema( query: GraphQLObjectType.Builder)= GraphQLSchema.newSchema().query(query)

fun listOfRefs(typeName:String)=GraphQLList(GraphQLTypeReference(typeName))
fun listOfObjs(wrappedType: GraphQLType) = GraphQLList(wrappedType)