package nl.jovmit.friends.postcomposer

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.post.InMemoryPostCatalog
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.post.PostRepository
import nl.jovmit.friends.domain.user.InMemoryUserDataStore
import nl.jovmit.friends.infrastructure.ControllableClock
import nl.jovmit.friends.infrastructure.ControllableIdGenerator
import nl.jovmit.friends.postcomposer.state.CreateNewPostScreenState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingCreatePostStatesTest {

  private val loggedInUserId = "userId"
  private val postId = "postId"
  private val timestamp = 1L
  private val text = "Post Text"
  private val post = Post(postId, loggedInUserId, text, timestamp)

  private val idGenerator = ControllableIdGenerator(postId)
  private val clock = ControllableClock(timestamp)
  private val postCatalog = InMemoryPostCatalog(idGenerator = idGenerator, clock = clock)
  private val userData = InMemoryUserDataStore(loggedInUserId)
  private val postRepository = PostRepository(userData, postCatalog)
  private val dispatchers = TestDispatchers()
  private val viewModel = CreatePostViewModel(postRepository, dispatchers)

  @Test
  fun uiStatesAreDeliveredInParticularOrder() {
    val deliveredStates = mutableListOf<CreateNewPostScreenState>()
    viewModel.postScreenState.observeForever { deliveredStates.add(it) }

    viewModel.createPost(text)

    assertEquals(
      listOf(
        CreateNewPostScreenState(isLoading = true),
        CreateNewPostScreenState(createdPostId = post.id)
      ),
      deliveredStates
    )
  }
}