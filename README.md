[![Kotlin 1.0](https://img.shields.io/badge/Kotlin-1.0.5-blue.svg)](http://kotlinlang.org)
[![](https://jitpack.io/v/beyondeye/graphkool.svg)](https://jitpack.io/#beyondeye/graphkool)
[![Build Status](https://travis-ci.org/beyondeye/graphkool.svg?branch=master)](https://travis-ci.org/beyondeye/graphkool)
[![Slack channel](https://img.shields.io/badge/Chat-Slack-green.svg)](https://kotlinlang.slack.com/messages/graphkool/)
# graphkool: a port of GraphQl server for Kotlin
Some notable features:

 
##### Table of Contents 
- [Gradle dependencies](#gradledeps)
- [Introduction to GraphKool](#graphkool_intro)
- [Credits from other open source libraries](#opensource)
- [Licence](#licence)

<a name="gradledeps"></a>
## dependencies for gradle
```groovy
// First, add JitPack to your repositories
repositories {
    //...
    maven { url "https://jitpack.io" }
}

// main graphkool package
compile 'com.github.beyondeye.graphkool:graphkool-core:0.1.0'
```

<a name="graphkool_intro"></a>
# An introduction to GraphKool
A collection of Kotlin extension functions and utilities around [GraphQL-java](https://github.com/graphql-java/graphql-java ) for reducing its
verbosity:
The original GraphQL hello world example written with GraphQL-java:

```java
public class HelloWorld {

    public static void main(String[] args) {    
        GraphQLObjectType queryType = newObject()
                        .name("helloWorldQuery")
                        .field(newFieldDefinition()
                                .type(GraphQLString)
                                .name("hello")
                                .staticValue("world"))
                        .build();
        
        GraphQLSchema schema = GraphQLSchema.newSchema()
                        .query(queryType)
                        .build();
        Map<String, Object> result = (Map<String, Object>) new GraphQL(schema).execute("{hello}").getData();

        System.out.println(result);
        // Prints: {hello=world}
    }
}
```
with GraphKool can be written:
```kotlin
fun main() {
            val queryType = newObject("helloWorldQuery")
                    .field("hello"..GraphQLString staticValue "world")

            val schema = newGraphQLSchema(query = queryType)
            val result = newGraphQL(schema).execute("{hello}").data as Map<String, Any>
            println(result)
}
```
The original GraphQL [StarWarsSchema](./graphkool-core/src/test/kotlin/com/beyondeye/graphkool/StarWarsSchemaJava.java)  written with GraphQL-java:
```Java
public class StarWarsSchemaJava {


    public static GraphQLEnumType episodeEnum = newEnum()
            .name("Episode")
            .description("One of the films in the Star Wars Trilogy")
            .value("NEWHOPE", 4, "Released in 1977.")
            .value("EMPIRE", 5, "Released in 1980.")
            .value("JEDI", 6, "Released in 1983.")
            .build();


    public static GraphQLInterfaceType characterInterface = newInterface()
            .name("Character")
            .description("A character in the Star Wars Trilogy")
            .field(newFieldDefinition()
                    .name("id")
                    .description("The id of the character.")
                    .type(new GraphQLNonNull(GraphQLString)))
            .field(newFieldDefinition()
                    .name("name")
                    .description("The name of the character.")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("friends")
                    .description("The friends of the character, or an empty list if they have none.")
                    .type(new GraphQLList(new GraphQLTypeReference("Character"))))
            .field(newFieldDefinition()
                    .name("appearsIn")
                    .description("Which movies they appear in.")
                    .type(new GraphQLList(episodeEnum)))
            .typeResolver(StarWarsData.getCharacterTypeResolver())
            .build();

    public static GraphQLObjectType humanType = newObject()
            .name("Human")
            .description("A humanoid creature in the Star Wars universe.")
            .withInterface(characterInterface)
            .field(newFieldDefinition()
                    .name("id")
                    .description("The id of the human.")
                    .type(new GraphQLNonNull(GraphQLString)))
            .field(newFieldDefinition()
                    .name("name")
                    .description("The name of the human.")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("friends")
                    .description("The friends of the human, or an empty list if they have none.")
                    .type(new GraphQLList(characterInterface))
                    .dataFetcher(StarWarsData.getFriendsDataFetcher()))
            .field(newFieldDefinition()
                    .name("appearsIn")
                    .description("Which movies they appear in.")
                    .type(new GraphQLList(episodeEnum)))
            .field(newFieldDefinition()
                    .name("homePlanet")
                    .description("The home planet of the human, or null if unknown.")
                    .type(GraphQLString))
            .build();

    public static GraphQLObjectType droidType = newObject()
            .name("Droid")
            .description("A mechanical creature in the Star Wars universe.")
            .withInterface(characterInterface)
            .field(newFieldDefinition()
                    .name("id")
                    .description("The id of the droid.")
                    .type(new GraphQLNonNull(GraphQLString)))
            .field(newFieldDefinition()
                    .name("name")
                    .description("The name of the droid.")
                    .type(GraphQLString))
            .field(newFieldDefinition()
                    .name("friends")
                    .description("The friends of the droid, or an empty list if they have none.")
                    .type(new GraphQLList(characterInterface))
                    .dataFetcher(StarWarsData.getFriendsDataFetcher()))
            .field(newFieldDefinition()
                    .name("appearsIn")
                    .description("Which movies they appear in.")
                    .type(new GraphQLList(episodeEnum)))
            .field(newFieldDefinition()
                    .name("primaryFunction")
                    .description("The primary function of the droid.")
                    .type(GraphQLString))
            .build();


    public static GraphQLObjectType queryType = newObject()
            .name("QueryType")
            .field(newFieldDefinition()
                    .name("hero")
                    .type(characterInterface)
                    .argument(newArgument()
                            .name("episode")
                            .description("If omitted, returns the hero of the whole saga. If provided, returns the hero of that particular episode.")
                            .type(episodeEnum))
                    .dataFetcher(new StaticDataFetcher(StarWarsData.getArtoo())))
            .field(newFieldDefinition()
                    .name("human")
                    .type(humanType)
                    .argument(newArgument()
                            .name("id")
                            .description("id of the human")
                            .type(new GraphQLNonNull(GraphQLString)))
                    .dataFetcher(StarWarsData.getHumanDataFetcher()))
            .field(newFieldDefinition()
                    .name("droid")
                    .type(droidType)
                    .argument(newArgument()
                            .name("id")
                            .description("id of the droid")
                            .type(new GraphQLNonNull(GraphQLString)))
                    .dataFetcher(StarWarsData.getDroidDataFetcher()))
            .build();


    public static GraphQLSchema starWarsSchema = GraphQLSchema.newSchema()
            .query(queryType)
            .build();
}
```
And the GraphQL [StarWarsSchema](./graphkool-core/src/test/kotlin/com/beyondeye/graphkool/StarWarsSchema.kt)  rewritten with GraphKool:
```kotlin
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
                    argument (+"episode"..episodeEnum description "If omitted, returns the hero of the whole saga. If provided, returns the hero of that particular episode.")
                    dataFetcher (StaticDataFetcher(StarWarsData.artoo)))
            .field("human"..humanType
                    argument (+"id"..!GraphQLString description "id of the human" )
                    dataFetcher (StarWarsData.humanDataFetcher))
            .field("droid"..droidType
                    argument (+"id"..!GraphQLString description "id of the droid")
                    dataFetcher (StarWarsData.droidDataFetcher))


    var starWarsSchema = newGraphQLSchema(query = queryType).build()
}
```
Notice that the syntax for the name and type definition for `field` and `argument` is slightly different:

```kotlin
.field("name"..GraphQLString)
```
and
```kotlin
.argument(+"id"..!GraphQLString)
```
Notice the `+` added for argument definition syntax?
Also notice that for defining a field/argument as non-nullable with prefix the type name with `!`

<a name="opensource"></a>
## Open source library included/modified or that inspired GraphKool
- https://github.com/graphql/graphql-js/: is the original server implementation in javascript
- http://facebook.github.io/graphql/ : is the official specification
- https://github.com/graphql-java/graphql-java an implementation of GraphQl server in Java
- https://github.com/sangria-graphql/sangria an implementation of GraphQL in Scala
- https://github.com/engagingspaces/vertx-dataloader a port to java of the original https://github.com/facebook/dataloader

<a name="licence"></a>
## License
~~~
The MIT License (MIT)
Copyright (c) 2016 Dario Elyasy

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
~~~
