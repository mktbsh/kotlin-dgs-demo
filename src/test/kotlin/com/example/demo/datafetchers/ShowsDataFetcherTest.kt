package com.example.demo.datafetchers

import com.example.demo.client.ShowsGraphQLQuery
import com.example.demo.client.ShowsProjectionRoot
import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(classes = [DgsAutoConfiguration::class, ShowDataFetcher::class])
class ShowsDataFetcherTest {

    @Autowired
    lateinit var dgsQueryExecutor: DgsQueryExecutor

    @MockBean
    lateinit var showsService: ShowsService

    @BeforeEach
    fun before() {
        Mockito.`when`(showsService.shows()).thenAnswer {
            listOf(ShowDataFetcher.Show("mock title", 2021))
        }
    }

    @Test
    fun shows() {
        val titles: List<String> = dgsQueryExecutor.executeAndExtractJsonPath("""
            {
                shows {
                    title
                    releaseYear
                }
            }
        """.trimIndent(), "data.shows[*].title")

        assertThat(titles).contains("mock title")
    }

    @Test
    fun showsWithQueryApi() {
        val graphQLQueryRequest = GraphQLQueryRequest(
            ShowsGraphQLQuery.Builder()
                .titleFilter("mo")
                .build(),
            ShowsProjectionRoot().title())

        val titles = dgsQueryExecutor.executeAndExtractJsonPath<List<String>>(graphQLQueryRequest.serialize(), "data.shows[*].title")
        assertThat(titles).containsExactly("mock title")
    }

}