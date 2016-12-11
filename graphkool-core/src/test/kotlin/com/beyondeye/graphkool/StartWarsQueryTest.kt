package com.beyondeye.graphkool

import graphql.GraphQL
import io.kotlintest.specs.BehaviorSpec

/**
 * Created by daely on 12/9/2016.
 */
class StartWarsQueryTest : BehaviorSpec() {
    init {
        given("the query 1") {
            val query = """
                    query HeroNameQuery {
                    hero   {
                             name
                            }
                    }
                    """
            //val expected = mapOf("hero" to mapOf("name" to "R2-D2"))
            `when`("run the query") {
                val  result = GraphQL(StarWarsSchema.starWarsSchema).execute(query).data
                then("Correctly identifies R2-D2 as the hero of the Star Wars Saga") {
                    result.toString() shouldBe "{hero={name=R2-D2}}"
                }
            }
        }
        given("the query 2") {
            val query = """
                     query HeroNameAndFriendsQuery {
                         hero {
                             id
                             name
                             friends {
                                 name
                             }
                         }
                     }
                    """
/*            val expected = mapOf(
                    "hero" to mapOf(
                            "id" to "2001",
                            "name" to "R2-D2",
                            "friends" to arrayOf(
                                    mapOf("name" to "Luke Skywalker"),
                                    mapOf("name" to "Han Solo"),
                                    mapOf("name" to "Leia Organa")
                            )
                    )
            )
            */
            `when`("run the query") {
                val  result = GraphQL(StarWarsSchema.starWarsSchema).execute(query).data
                then("Allows us to query for the ID and friends of R2-D2") {
                    result.toString()  shouldBe "{hero={id=2001, name=R2-D2, friends=[{name=Luke Skywalker}, {name=Han Solo}, {name=Leia Organa}]}}"
                }
            }
        }
        given("the query 3") {
            val query = """
                       query NestedQuery {
                           hero {
                               name
                               friends {
                                   name
                                   appearsIn
                                   friends {
                                       name
                                   }
                               }
                           }
                       }
                    """
            val expected = mapOf(
                    "hero" to mapOf("name" to "R2-D2",
                            "friends" to listOf(
                                    mapOf(
                                            "name" to
                                                    "Luke Skywalker",
                                            "appearsIn" to listOf("NEWHOPE", "EMPIRE", "JEDI"),
                                            "friends" to listOf(
                                                    mapOf("name" to "Han Solo"),
                                                    mapOf("name" to "Leia Organa"),
                                                    mapOf("name" to "C-3PO"),
                                                    mapOf("name" to "R2-D2")
                                            )
                                    ),
                                    mapOf(
                                            "name" to "Han Solo",
                                            "appearsIn" to listOf("NEWHOPE", "EMPIRE", "JEDI"),
                                            "friends" to listOf(
                                                    mapOf("name" to "Luke Skywalker"),
                                                    mapOf("name" to "Leia Organa"),
                                                    mapOf("name" to "R2-D2")
                                            )
                                    ),
                                    mapOf(
                                            "name" to "Leia Organa",
                                            "appearsIn" to listOf("NEWHOPE", "EMPIRE", "JEDI"),
                                            "friends" to listOf(
                                                    mapOf("name" to "Luke Skywalker"),
                                                    mapOf("name" to "Han Solo"),
                                                    mapOf("name" to "C-3PO"),
                                                    mapOf("name" to "R2-D2")
                                            )
                                    )
                            )
                    )
            )

            //val expected = mapOf("hero" to mapOf("name" to "R2-D2"))
            `when`("run the query") {
                val  result = GraphQL(StarWarsSchema.starWarsSchema).execute(query).data
                then("Allows us to query for the friends of friends of R2-D2") {
                    result.toString() shouldBe expected.toString() //"{hero={name=R2-D2, friends=[{name=Luke Skywalker, appearsIn=[NEWHOPE, EMPIRE, JEDI], friends=[{name=Han Solo}, {name=Leia Organa}, {name=C-3PO}, {name=R2-D2}]}, {name=Han Solo, appearsIn=[NEWHOPE, EMPIRE, JEDI], friends=[{name=Luke Skywalker}, {name=Leia Organa}, {name=R2-D2}]}, {name=Leia Organa, appearsIn=[NEWHOPE, EMPIRE, JEDI], friends=[{name=Luke Skywalker}, {name=Han Solo}, {name=C-3PO}, {name=R2-D2}]}]}}"
                }
            }
        }

        given("the query 4") {
            val query = """
                       query FetchLukeQuery {
                           human(id: "1000") {
                               name
                           }
                       }
                    """
            val expected= mapOf("human" to mapOf("name" to "Luke Skywalker"))
            //val expected = mapOf("hero" to mapOf("name" to "R2-D2"))
            `when`("run the query") {
                val  result = GraphQL(StarWarsSchema.starWarsSchema).execute(query).data
                then("Allows us to query for Luke Skywalker directly, using his ID") {
                    result.toString() shouldBe expected.toString()
                }
            }
        }
        given("the query 5") {
            val query = """
                        query FetchSomeIDQuery(${'$'}someId: String!) {
                            human(id: ${'$'}someId) {
                                name
                            }
                        }
                    """
            val expected = mapOf("human" to mapOf(
                    "name" to "Luke Skywalker"
            ))
            val params = mapOf(
                    "someId" to "1000"
            )
            `when`("run the query") {
                val  result = GraphQL(StarWarsSchema.starWarsSchema).execute(query,null as Any?, params).data
                then("Allows us to create a generic query, then use it to fetch Luke Skywalker using his ID") {
                    result.toString() shouldBe expected.toString()
                }
            }
        }
        given("the query 6") {
            val query = """
                         query humanQuery(${'$'}id: String!) {
                             human(id: ${'$'}id) {
                                 name
                             }
                         }
                    """
            val expected = mapOf("human" to null)
            val params= mapOf("id" to "not a valid id")
            //val expected = mapOf("hero" to mapOf("name" to "R2-D2"))
            `when`("run the query") {
                val  result = GraphQL(StarWarsSchema.starWarsSchema).execute(query,null as Any?, params).data
                then("Allows us to create a generic query, then pass an invalid ID to get null back") {
                    result.toString() shouldBe expected.toString()
                }
            }
        }
        given("the query 7") {
            val query = """
                              query FetchLukeAliased {
                                  luke: human(id: "1000") {
                                      name
                                  }
                              }
                    """
            val expected = mapOf("luke" to mapOf("name" to "Luke Skywalker"))
            `when`("run the query") {
                val  result = GraphQL(StarWarsSchema.starWarsSchema).execute(query).data
                then("Allows us to query for Luke, changing his key with an alias") {
                    result.toString() shouldBe expected.toString()
                }
            }
        }
        given("the query 8") {
            val query = """
                        query FetchLukeAndLeiaAliased {
                            luke:
                            human(id: "1000") {
                                name
                            }
                            leia:
                            human(id: "1003") {
                                name
                            }
                        }
                    """
            val expected = mapOf(
                    "luke" to mapOf("name" to "Luke Skywalker"),
                    "leia" to mapOf("name" to "Leia Organa"))
            `when`("run the query") {
                val  result = GraphQL(StarWarsSchema.starWarsSchema).execute(query).data
                then("Allows us to query for both Luke and Leia, using two root fields and an alias") {
                    result.toString() shouldBe expected.toString()
                }
            }
        }
        given("the query 9") {
            val query = """
                          query DuplicateFields {
                              luke: human(id: "1000") {
                                  name
                                  homePlanet
                              }
                              leia: human(id: "1003") {
                                  name
                                  homePlanet
                              }
                          }
                    """
            val expected = mapOf(
                    "luke" to mapOf("name" to "Luke Skywalker",
                            "homePlanet" to "Tatooine"),
                    "leia" to mapOf("name" to "Leia Organa",
                            "homePlanet" to "Alderaan"))
            `when`("run the query") {
                val  result = GraphQL(StarWarsSchema.starWarsSchema).execute(query).data
                then("Allows us to query using duplicated content") {
                    result.toString() shouldBe expected.toString()
                }
            }
        }
        given("the query 10") {
            val query = """
                          query UseFragment {
                             luke: human(id: "1000") {
                                 ...HumanFragment
                             }
                             leia: human(id: "1003") {
                                 ...HumanFragment
                             }
                         }
                         fragment HumanFragment on Human {
                             name
                             homePlanet
                         }
                    """
            val expected = mapOf(
                    "luke" to mapOf("name" to "Luke Skywalker",
                            "homePlanet" to "Tatooine"),
                    "leia" to mapOf("name" to "Leia Organa",
                            "homePlanet" to "Alderaan"))
            `when`("run the query") {
                val  result = GraphQL(StarWarsSchema.starWarsSchema).execute(query).data
                then("Allows us to use a fragment to avoid duplicating content") {
                    result.toString() shouldBe expected.toString()
                }
            }
        }
        given("the query 11") {
            val query = """
                          query CheckTypeOfR2 {
                             hero {
                                 __typename
                                 name
                             }
                         }
                    """
            val expected = mapOf("hero" to mapOf("__typename" to "Droid",
                    "name" to "R2-D2"))
            `when`("run the query") {
                val  result = GraphQL(StarWarsSchema.starWarsSchema).execute(query).data
                then("Allows us to verify that R2-D2 is a droid") {
                    result.toString() shouldBe expected.toString()
                }
            }
        }
    }
}
