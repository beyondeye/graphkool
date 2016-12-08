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
A collection of extension functions and utility around [GraphQL-java](https://github.com/graphql-java/graphql-java ) for reducing its
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
                    .field("hello"..GraphQLString withStaticValue "world")

            val schema = newGraphQLSchema(query = queryType)
            val result = newGraphQL(schema).execute("{hello}").data as Map<String, Any>
            println(result)
}
```
**Kool!!**

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
