package com.beyondeye.graphkool

import io.kotlintest.specs.FunSpec
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLSchema
import graphql.GraphQL
import graphql.execution.ExecutionStrategy
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLOutputType


fun newGraphQL(schemaBuilder:GraphQLSchema.Builder,executionStrategy: ExecutionStrategy?=null) = GraphQL(schemaBuilder.build(),executionStrategy)
fun newGraphQL(schema:GraphQLSchema,executionStrategy: ExecutionStrategy?=null) = GraphQL(schema,executionStrategy)
fun newObject(name:String)= newObject().name(name)
infix fun String.ofType(type_:GraphQLOutputType):GraphQLFieldDefinition.Builder {
    return newFieldDefinition().type(type_).name(this)
}
operator fun String.rangeTo(type_:GraphQLOutputType) = this.ofType(type_)
infix fun GraphQLFieldDefinition.Builder.withStaticValue(value:Any) = this.staticValue(value)
fun newGraphQLSchema( query: GraphQLObjectType.Builder)=GraphQLSchema.newSchema().query(query)

/**
 * Created by daely on 12/8/2016.
 */
class MyTests : FunSpec() {
    init {
        test("the GraphQL hello world test, with builder extension functions") {
            val queryType = newObject("helloWorldQuery")
                    .field("hello"..GraphQLString withStaticValue "world")

            val schema = newGraphQLSchema(query = queryType)
            val result = newGraphQL(schema).execute("{hello}").data as Map<String, Any>

            result.toString() shouldBe "{hello=world}" //a map with key="hello" and value="world"
        }
    }
}