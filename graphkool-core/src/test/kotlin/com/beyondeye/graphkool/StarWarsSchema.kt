package com.beyondeye.graphkool


import graphql.schema.*

import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLArgument.newArgument
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType.newObject

object StarWarsSchema {


    var episodeEnum = newEnum("Episode")
            .description("One of the films in the Star Wars Trilogy")
            .value("NEWHOPE", 4, "Released in 1977.")
            .value("EMPIRE", 5, "Released in 1980.")
            .value("JEDI", 6, "Released in 1983.")
            .build()


    var characterInterface:GraphQLInterfaceType = newInterface("Character")
            .description("A character in the Star Wars Trilogy")
            .field("id"..!GraphQLString description "The id of the character." )
            .field("name"..GraphQLString  description "The name of the character." )
            .field("friends"..listOfRefs("Character") description "The friends of the character, or an empty list if they have none." )
            .field("appearsIn"..listOfObjs(episodeEnum) description "Which movies they appear in." )
            .typeResolver(StarWarsData.characterTypeResolver)
            .build()


    var humanType = newObject("Human")
            .description("A humanoid creature in the Star Wars universe.")
            .withInterface(characterInterface)
            .field("id"..!GraphQLString description "The id of the human." )
            .field("name"..GraphQLString description "The name of the human.")
            .field("friends"..GraphQLList(characterInterface) description "The friends of the human, or an empty list if they have none."
                    dataFetcher(StarWarsData.friendsDataFetcher))
            .field("appearsIn"..GraphQLList(episodeEnum) description "Which movies they appear in.")
            .field("homePlanet"..GraphQLString description "The home planet of the human, or null if unknown.")
            .build()


    var droidType = newObject("Droid")
            .description("A mechanical creature in the Star Wars universe.")
            .withInterface(characterInterface)
            .field("id"..!GraphQLString description "The id of the droid.")
            .field("name"..GraphQLString description "The name of the droid.")
            .field("friends"..GraphQLList(characterInterface) description "The friends of the droid, or an empty list if they have none."
                    dataFetcher(StarWarsData.friendsDataFetcher))
            .field("appearsIn"..GraphQLList(episodeEnum) description "Which movies they appear in.")
            .field("primaryFunction"..GraphQLString description "The primary function of the droid.")
            .build()


    var queryType = newObject("QueryType")
            .field("hero"..characterInterface
                    argument(newArgument()
                            .name("episode")
                            .description("If omitted, returns the hero of the whole saga. If provided, returns the hero of that particular episode.")
                            .type(episodeEnum))
                    dataFetcher(StaticDataFetcher(StarWarsData.artoo)))
            .field("human"..humanType
                    argument(newArgument()
                            .name("id")
                            .description("id of the human")
                            .type(GraphQLNonNull(GraphQLString)))
                    dataFetcher(StarWarsData.humanDataFetcher))
            .field("droid"..droidType
                    argument(newArgument()
                            .name("id")
                            .description("id of the droid")
                            .type(!GraphQLString))
                    dataFetcher(StarWarsData.droidDataFetcher))


    var starWarsSchema = newGraphQLSchema(query = queryType).build()
}
