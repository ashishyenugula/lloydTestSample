package com.ashish.lloydtest.datasource.network.blog

import com.ashish.lloydtest.business.domain.models.AuthToken
import com.ashish.lloydtest.business.domain.models.BlogPost
import com.ashish.lloydtest.business.domain.util.DateUtils


object ConfirmBlogExistsOnServerResponses {

    // account info
    val email = "astest12@gmail.com"
    val password = "123456"
    val pk = 1
    val username = "ashish_test"
    val token = "de803edc9ebefa3dee77faea8f34fff3e6b217b5"

    val authToken = AuthToken(
        accountPk = pk,
        token = token,
    )

    // blog info
    val blogPk = 453
    val title = "How to test a blog post!"
    val body = "I'm publishing a blog about how to test a blog! Wow!"
    val image = "https://ashishpoststest.com/image.png"
    val slug = "how-to-test-a-blog-post"
    val dateUpdated = "2024-05-31T16:26:23.121544Z"

    val blogPost = BlogPost(
        pk = blogPk,
        title = title,
        body = body,
        image = image,
        slug = slug,
        dateUpdated = DateUtils.convertServerStringDateToLong(dateUpdated),
        username = username
    )

    val success_blogExists = "{ \"pk\": $pk, \"title\": \"$title\", \"slug\": \"$slug\", \"body\": \"$body\", \"image\": \"$image\", \"date_updated\": \"$dateUpdated\", \"username\": \"$username\" }"

}

















