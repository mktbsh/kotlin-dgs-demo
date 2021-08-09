package com.example

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class ShowDataFetcher {
    private val shows = listOf(
        Show("Stranger Things", 2016),
        Show("Ozark", 2017),
        Show("The Crown", 2016),
        Show("Dead to Me", 2019),
        Show("Orange Days", 2013),
    )

    @DgsQuery
    fun shows(@InputArgument titleFilter: String?): List<Show> {
        return if (titleFilter != null) {
            shows.filter { it.title.contains(titleFilter) }
        } else {
            shows
        }
    }

    data class Show(val title: String, val releaseYear: Int)
}