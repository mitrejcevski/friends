package nl.jovmit.friends.timeline

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.post.InMemoryPostCatalog
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.user.Following
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.infrastructure.builder.UserBuilder.Companion.aUser
import nl.jovmit.friends.timeline.state.TimelineState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LoadPostsTest {

  private val tim = aUser().withId("timId").build()
  private val anna = aUser().withId("annaId").build()
  private val lucy = aUser().withId("lucyId").build()
  private val sara = aUser().withId("saraId").build()

  private val timPosts = listOf(
    Post("postId", tim.id, "post text", 1L)
  )
  private val lucyPosts = listOf(
    Post("post2", lucy.id, "post 2", 2L),
    Post("post1", lucy.id, "post 1", 1L)
  )
  private val saraPosts = listOf(
    Post("post4", sara.id, "post 4", 4L),
    Post("post3", sara.id, "post 3", 3L)
  )

  private val availablePosts = timPosts + lucyPosts + saraPosts

  @Test
  fun noPostsAvailable() {
    val viewModel = TimelineViewModel(
      InMemoryUserCatalog(),
      InMemoryPostCatalog(availablePosts)
    )

    viewModel.timelineFor("tomId")

    assertEquals(TimelineState.Posts(emptyList()), viewModel.timelineState.value)
  }

  @Test
  fun postsAvailable() {
    val viewModel = TimelineViewModel(
      InMemoryUserCatalog(),
      InMemoryPostCatalog(availablePosts)
    )

    viewModel.timelineFor(tim.id)

    assertEquals(TimelineState.Posts(timPosts), viewModel.timelineState.value)
  }

  @Test
  fun postsFormFriends() {
    val viewModel = TimelineViewModel(
      InMemoryUserCatalog(
        followings = listOf(
          Following(anna.id, lucy.id)
        )
      ),
      InMemoryPostCatalog(availablePosts)
    )

    viewModel.timelineFor(anna.id)

    assertEquals(TimelineState.Posts(lucyPosts), viewModel.timelineState.value)
  }

  @Test
  fun postsFromFriendsAlongOwn() {
    val viewModel = TimelineViewModel(
      InMemoryUserCatalog(
        followings = listOf(
          Following(sara.id, lucy.id)
        )
      ),
      InMemoryPostCatalog(availablePosts)
    )

    viewModel.timelineFor(sara.id)

    assertEquals(TimelineState.Posts(lucyPosts + saraPosts), viewModel.timelineState.value)
  }
}