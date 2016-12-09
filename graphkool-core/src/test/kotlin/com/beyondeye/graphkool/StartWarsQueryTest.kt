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

    }
}

/*

    def 'Allows us to query for the friends of friends of R2-D2'() {
        given:

        def query = """
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
        def expected = [
                hero: [name   : 'R2-D2',
                       friends: [
                               [
                                       name     :
                                               'Luke Skywalker',
                                       appearsIn: ['NEWHOPE', 'EMPIRE', 'JEDI'],
                                       friends  : [
                                               [
                                                       name: 'Han Solo',
                                               ],
                                               [
                                                       name: 'Leia Organa',
                                               ],
                                               [
                                                       name: 'C-3PO',
                                               ],
                                               [
                                                       name: 'R2-D2',
                                               ],
                                       ]
                               ],
                               [
                                       name     : 'Han Solo',
                                       appearsIn: ['NEWHOPE', 'EMPIRE', 'JEDI'],
                                       friends  : [
                                               [
                                                       name: 'Luke Skywalker',
                                               ],
                                               [
                                                       name: 'Leia Organa',
                                               ],
                                               [
                                                       name: 'R2-D2',
                                               ],
                                       ]
                               ],
                               [
                                       name     : 'Leia Organa',
                                       appearsIn: ['NEWHOPE', 'EMPIRE', 'JEDI'],
                                       friends  : [
                                               [
                                                       name: 'Luke Skywalker',
                                               ],
                                               [
                                                       name: 'Han Solo',
                                               ],
                                               [
                                                       name: 'C-3PO',
                                               ],
                                               [
                                                       name: 'R2-D2',
                                               ],
                                       ]
                               ],
                       ]
                ]
        ]

        when:
        def result = new GraphQL(StarWarsSchema.starWarsSchema).execute(query).data

        then:
        result == expected


    }

    def 'Allows us to query for Luke Skywalker directly, using his ID'() {
        given:
        def query = """
        query FetchLukeQuery {
            human(id: "1000") {
                name
            }
        }
        """
        def expected = [
                human: [
                        name: 'Luke Skywalker'
                ]
        ]

        when:
        def result = new GraphQL(StarWarsSchema.starWarsSchema).execute(query).data

        then:
        result == expected
    }

    def 'Allows us to create a generic query, then use it to fetch Luke Skywalker using his ID'() {
        given:
        def query = """
        query FetchSomeIDQuery(\$someId: String!) {
            human(id: \$someId) {
                name
            }
        }
        """
        def params = [
                someId: '1000'
        ]
        def expected = [
                human: [
                        name: 'Luke Skywalker'
                ]
        ]
        when:
        def result = new GraphQL(StarWarsSchema.starWarsSchema).execute(query, null, params).data

        then:
        result == expected
    }

    def 'Allows us to create a generic query, then pass an invalid ID to get null back'() {
        given:
        def query = """
        query humanQuery(\$id: String!) {
            human(id: \$id) {
                name
            }
        }
        """
        def params = [
                id: 'not a valid id'
        ]
        def expected = [
                human: null
        ]
        when:
        def result = new GraphQL(StarWarsSchema.starWarsSchema).execute(query, null, params).data

        then:
        result == expected
    }


    def 'Allows us to query for Luke, changing his key with an alias'() {
        given:
        def query = """
            query FetchLukeAliased {
                luke: human(id: "1000") {
                    name
                }
            }
        """
        def expected = [
                luke: [
                        name: 'Luke Skywalker'
                ],
        ]

        when:
        def result = new GraphQL(StarWarsSchema.starWarsSchema).execute(query).data

        then:
        result == expected
    }

    def 'Allows us to query for both Luke and Leia, using two root fields and an alias'() {
        given:

        def query = """
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
        def expected = [
                luke:
                        [
                                name: 'Luke Skywalker'
                        ],
                leia:
                        [
                                name: 'Leia Organa'
                        ]
        ]

        when:
        def result = new GraphQL(StarWarsSchema.starWarsSchema).execute(query).data

        then:
        result == expected

    }

    def 'Allows us to query using duplicated content'() {
        given:
        def query = """
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
        def expected = [
                luke: [name      : 'Luke Skywalker',
                       homePlanet:
                               'Tatooine'
                ],
                leia: [name      : 'Leia Organa',
                       homePlanet:
                               'Alderaan'
                ]
        ];

        when:
        def result = new GraphQL(StarWarsSchema.starWarsSchema).execute(query).data

        then:
        result == expected

    }

    def 'Allows us to use a fragment to avoid duplicating content'() {
        given:
        def query = """
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
        def expected = [
                luke: [
                        name      : 'Luke Skywalker',
                        homePlanet: 'Tatooine'
                ],
                leia: [
                        name      : 'Leia Organa',
                        homePlanet: 'Alderaan'
                ]
        ];
        when:
        def result = new GraphQL(StarWarsSchema.starWarsSchema).execute(query).data

        then:
        result == expected
    }

    def 'Allows us to verify that R2-D2 is a droid'() {
        given:
        def query = """
        query CheckTypeOfR2 {
            hero {
                __typename
                name
            }
        }
        """
        def expected = [
                hero: [
                        __typename: 'Droid',
                        name      : 'R2-D2'
                ],
        ]
        when:
        def result = new GraphQL(StarWarsSchema.starWarsSchema).execute(query).data

        then:
        result == expected
    }
}

 */