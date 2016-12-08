package com.beyondeye.graphkool

import io.kotlintest.specs.FunSpec
import graphql.Scalars.GraphQLString

/**
 * Created by daely on 12/8/2016.
 */
class HelloWorldTest : FunSpec() {
    init {
        test("the GraphQL hello world test, with builder extension functions") {
            val queryType = newObject("helloWorldQuery")
                    .field("hello"..GraphQLString staticValue "world")

            val schema = newGraphQLSchema(query = queryType)
            val result = newGraphQL(schema).execute("{hello}").data as Map<String, Any>

            result.toString() shouldBe "{hello=world}" //a map with key="hello" and value="world"
        }
    }
}