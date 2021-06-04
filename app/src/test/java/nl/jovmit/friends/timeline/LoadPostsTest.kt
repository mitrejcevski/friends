package nl.jovmit.friends.timeline

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.post.InMemoryPostCatalog
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.infrastructure.builder.UserBuilder.Companion.aUser
import nl.jovmit.friends.timeline.state.TimelineState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LoadPostsTest {

  @Test
  fun noPostsAvailable() {
    val viewModel = TimelineViewModel(InMemoryUserCatalog(), InMemoryPostCatalog(
      listOf(
        Post("postId", "timId", "post text", 1L),
        Post("post2", "lucyId", "post 2", 2L),
        Post("post1", "lucyId", "post 1", 1L),
        Post("post4", "saraId", "post 4", 4L),
        Post("post3", "saraId", "post 3", 3L)
      )
    )
    )

    viewModel.timelineFor("tomId")

    assertEquals(TimelineState.Posts(emptyList()), viewModel.timelineState.value)
  }

  @Test
  fun postsAvailable() {
    val tim = aUser().withId("timId").build()
    val timPosts = listOf(Post("postId", tim.id, "post text", 1L))
    val viewModel = TimelineViewModel(InMemoryUserCatalog(), InMemoryPostCatalog(
      listOf(
        Post("postId", "timId", "post text", 1L),
        Post("post2", "lucyId", "post 2", 2L),
        Post("post1", "lucyId", "post 1", 1L),
        Post("post4", "saraId", "post 4", 4L),
        Post("post3", "saraId", "post 3", 3L)
      )
    )
    )

    viewModel.timelineFor(tim.id)

    assertEquals(TimelineState.Posts(timPosts), viewModel.timelineState.value)
  }

  @Test
  fun postsFormFriends() {
    val anna = aUser().withId("annaId").build()
    val lucy = aUser().withId("lucyId").build()
    val lucyPosts = listOf(
      Post("post2", lucy.id, "post 2", 2L),
      Post("post1", lucy.id, "post 1", 1L)
    )
    val viewModel = TimelineViewModel(InMemoryUserCatalog(), InMemoryPostCatalog(
      listOf(
        Post("postId", "timId", "post text", 1L),
        Post("post2", "lucyId", "post 2", 2L),
        Post("post1", "lucyId", "post 1", 1L),
        Post("post4", "saraId", "post 4", 4L),
        Post("post3", "saraId", "post 3", 3L)
      )
    )
    )

    viewModel.timelineFor(anna.id)

    assertEquals(TimelineState.Posts(lucyPosts), viewModel.timelineState.value)
  }

  @Test
  fun postsFromFriendsAlongOwn() {
    val lucy = aUser().withId("lucyId").build()
    val lucyPosts = listOf(
      Post("post2", lucy.id, "post 2", 2L),
      Post("post1", lucy.id, "post 1", 1L)
    )
    val sara = aUser().withId("saraId").build()
    val saraPosts = listOf(
      Post("post4", sara.id, "post 4", 4L),
      Post("post3", sara.id, "post 3", 3L)
    )
    val viewModel = TimelineViewModel(InMemoryUserCatalog(), InMemoryPostCatalog(
      listOf(
        Post("postId", "timId", "post text", 1L),
        Post("post2", "lucyId", "post 2", 2L),
        Post("post1", "lucyId", "post 1", 1L),
        Post("post4", "saraId", "post 4", 4L),
        Post("post3", "saraId", "post 3", 3L)
      )
    )
    )

    viewModel.timelineFor(sara.id)

    assertEquals(TimelineState.Posts(lucyPosts + saraPosts), viewModel.timelineState.value)
  }


}