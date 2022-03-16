package nl.jovmit.friends.timeline

import androidx.lifecycle.SavedStateHandle
import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.post.InMemoryPostCatalog
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.timeline.TimelineRepository
import nl.jovmit.friends.domain.user.Following
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.infrastructure.builder.UserBuilder.Companion.aUser
import nl.jovmit.friends.timeline.state.TimelineScreenState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LoadTimelineTest {

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

  private val availablePosts = (timPosts + lucyPosts + saraPosts).toMutableList()

  @Test
  fun noPostsAvailable() {
    val userCatalog = InMemoryUserCatalog()
    val postCatalog = InMemoryPostCatalog(availablePosts)
    val viewModel = TimelineViewModel(
      TimelineRepository(userCatalog, postCatalog),
      SavedStateHandle(),
      TestDispatchers()
    )

    viewModel.timelineFor("tomId")

    assertEquals(TimelineScreenState(posts = emptyList()), viewModel.screenState.value)
  }

  @Test
  fun postsAvailable() {
    val userCatalog = InMemoryUserCatalog()
    val postCatalog = InMemoryPostCatalog(availablePosts)
    val viewModel = TimelineViewModel(
      TimelineRepository(userCatalog, postCatalog),
      SavedStateHandle(),
      TestDispatchers()
    )

    viewModel.timelineFor(tim.id)

    assertEquals(TimelineScreenState(posts = timPosts), viewModel.screenState.value)
  }

  @Test
  fun postsFormFriends() {
    val userCatalog = InMemoryUserCatalog(followings = mutableListOf(Following(anna.id, lucy.id)))
    val postCatalog = InMemoryPostCatalog(availablePosts)
    val viewModel = TimelineViewModel(
      TimelineRepository(userCatalog, postCatalog),
      SavedStateHandle(),
      TestDispatchers()
    )

    viewModel.timelineFor(anna.id)

    assertEquals(TimelineScreenState(posts = lucyPosts), viewModel.screenState.value)
  }

  @Test
  fun postsFromFriendsAlongOwn() {
    val userCatalog = InMemoryUserCatalog(followings = mutableListOf(Following(sara.id, lucy.id)))
    val postCatalog = InMemoryPostCatalog(availablePosts)
    val viewModel = TimelineViewModel(
      TimelineRepository(userCatalog, postCatalog),
      SavedStateHandle(),
      TestDispatchers()
    )

    viewModel.timelineFor(sara.id)

    assertEquals(TimelineScreenState(posts = lucyPosts + saraPosts), viewModel.screenState.value)
  }
}