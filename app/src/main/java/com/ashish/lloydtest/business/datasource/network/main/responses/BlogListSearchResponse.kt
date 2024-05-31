package com.ashish.lloydtest.business.datasource.network.main.responses

import com.ashish.lloydtest.business.datasource.network.main.BlogPostDto
import com.ashish.lloydtest.business.datasource.network.main.toBlogPost
import com.ashish.lloydtest.business.domain.models.BlogPost
import com.google.gson.annotations.SerializedName

class BlogListSearchResponse(

    @SerializedName("results")
    var results: List<BlogPostDto>,

    @SerializedName("detail")
    var detail: String
)

fun BlogListSearchResponse.toList(): List<BlogPost>{
    val list: MutableList<BlogPost> = mutableListOf()
    for(dto in results){
        list.add(
            dto.toBlogPost()
        )
    }
    return list
}






