package nl.jovmit.friends.postcomposer

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.post.InMemoryPostCatalog
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.post.PostRepository
import nl.jovmit.friends.domain.user.InMemoryUserData
import nl.jovmit.friends.infrastructure.ControllableClock
import nl.jovmit.friends.infrastructure.ControllableIdGenerator
import nl.jovmit.friends.postcomposer.state.CreatePostState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingCreatePostStatesTest {

  private val idGenerator = ControllableIdGenerator("postId")
  private val clock = ControllableClock(1L)
  private val postCatalog = InMemoryPostCatalog(idGenerator = idGenerator, clock = clock)
  private val userData = InMemoryUserData("userId")
  private val postRepository = PostRepository(userData, postCatalog)
  private val dispatchers = TestDispatchers()
  private val viewModel = CreatePostViewModel(postRepository, dispatchers)

  @Test
  fun uiStatesAreDeliveredInParticularOrder() {
    val deliveredStates = mutableListOf<CreatePostState>()
    viewModel.postState.observeForever { deliveredStates.add(it) }
    val post = Post("postId", "userId", "Post Text", 1L)

    viewModel.createPost("Post Text")

    assertEquals(
      listOf(CreatePostState.Loading, CreatePostState.Created(post)),
      deliveredStates
    )
  }
}