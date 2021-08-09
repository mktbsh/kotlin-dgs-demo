package com.example.demo.datafetchers

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ShowsService {
    fun shows(): List<ShowDataFetcher.Show>
}

@Service
class BasicShowsService: ShowsService {
    override fun shows(): List<ShowDataFetcher.Show> {
        return listOf(
            ShowDataFetcher.Show("Stranger Things", 2016),
            ShowDataFetcher.Show("Ozark", 2017),
            ShowDataFetcher.Show("The Crown", 2016),
            ShowDataFetcher.Show("Dead to Me", 2019),
            ShowDataFetcher.Show("Orange Days", 2013),
        )
    }
}

@DgsComponent
class ShowDataFetcher {

    @Autowired
    lateinit var showsService: ShowsService

    @DgsData(parentType = "Query", field = "shows")
    fun shows(@InputArgument("titleFilter") titleFilter: String?): List<Show> {
        return if (titleFilter != null) {
            showsService.shows().filter { it.title.contains(titleFilter) }
        } else {
            showsService.shows()
        }
    }

    data class Show(val title: String, val releaseYear: Int)
}