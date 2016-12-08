package com.beyondeye.graphkool

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLObjectType
import graphql.schema.TypeResolver

class StarWarsData {
    companion object {
        @JvmStatic val luke = mapOf(
                "id" to "1000",
                "name" to "Luke Skywalker",
                "friends" to listOf("1002", "1003", "2000", "2001"),
                "appearsIn" to listOf(4, 5, 6),
                "homePlanet" to "Tatooine"
        )
        @JvmStatic val vader = mapOf(
                "id" to "1001",
                "name" to "Darth Vader",
                "friends" to listOf("1004"),
                "appearsIn" to listOf(4, 5, 6),
                "homePlanet" to "Tatooine"
        )
        @JvmStatic val han = mapOf(
                "id" to "1002",
                "name" to "Han Solo",
                "friends" to listOf("1000", "1003", "2001"),
                "appearsIn" to listOf(4, 5, 6)
        )
        @JvmStatic val leia = mapOf(
                "id" to "1003",
                "name" to "Leia Organa",
                "friends" to listOf("1000", "1002", "2000", "2001"),
                "appearsIn" to listOf(4, 5, 6),
                "homePlanet" to "Alderaan"
        )
        @JvmStatic val tarkin = mapOf(
                "id" to "1004",
                "name" to "Wilhuff Tarkin",
                "friends" to listOf("1001"),
                "appearsIn" to listOf(4)
        )
        @JvmStatic val humanData = mapOf(
                "1000" to luke,
                "1001" to vader,
                "1002" to han,
                "1003" to leia,
                "1004" to tarkin
        )

        @JvmStatic val threepio = mapOf(
                "id" to "2000",
                "name" to "C-3PO",
                "friends" to listOf("1000", "1002", "1003", "2001"),
                "appearsIn" to listOf(4, 5, 6),
                "primaryFunction" to "Protocol"
        )

        @JvmStatic val artoo = mapOf(
                "id" to "2001",
                "name" to "R2-D2",
                "friends" to listOf("1000", "1002", "1003"),
                "appearsIn" to listOf(4, 5, 6),
                "primaryFunction" to "Astromech"
        )
        @JvmStatic val droidData = mapOf(
                "2000" to threepio,
                "2001" to artoo
        )

        @JvmStatic fun getCharacter(id: String): Map<String, Any>? {
            if (humanData[id] != null) return humanData[id]
            if (droidData[id] != null) return droidData[id]
            return null
        }

        @JvmStatic val humanDataFetcher = object : DataFetcher {
            override fun get(environment: DataFetchingEnvironment): Any? {
                val id = environment.arguments["id"]
                return humanData[id]
            }
        }

        @JvmStatic val droidDataFetcher = object : DataFetcher {
            override fun get(environment: DataFetchingEnvironment): Any? {
                val id = environment.arguments["id"]
                return droidData[id]
            }
        }

        fun Any.getKey(k: String): Any? = (this as? Map<String, Any>)?.get(k)

        @JvmStatic val friendsDataFetcher = object : DataFetcher {
            override fun get(environment: DataFetchingEnvironment): Any? {
                return (environment.source.getKey("friends") as? List<String>)?.map { id -> getCharacter(id) }
            }
        }
        @JvmStatic val heroDataFetcher = object : DataFetcher {
            override fun get(environment: DataFetchingEnvironment): Any? {
                if (environment.containsArgument("episode")
                        && 5 == environment.getArgument<Int>("episode")) return luke
                return artoo
            }
        }


        @JvmStatic  val characterTypeResolver = object : TypeResolver {
            override fun getType(`object`: Any?): GraphQLObjectType? {
                if (`object` == null) return null
                val id = `object`.getKey("id")
                if (humanData[id] != null)
                    return StarWarsSchema.humanType
                if (droidData[id] != null)
                    return StarWarsSchema.droidType
                return null
            }
        }

    }
}
