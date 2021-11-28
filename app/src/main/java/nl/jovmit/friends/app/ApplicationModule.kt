package nl.jovmit.friends.app

import nl.jovmit.friends.domain.post.InMemoryPostCatalog
import nl.jovmit.friends.domain.post.PostCatalog
import nl.jovmit.friends.domain.post.PostRepository
import nl.jovmit.friends.domain.timeline.TimelineRepository
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.domain.user.InMemoryUserDataStore
import nl.jovmit.friends.domain.user.UserCatalog
import nl.jovmit.friends.domain.user.UserRepository
import nl.jovmit.friends.domain.validation.RegexCredentialsValidator
import nl.jovmit.friends.postcomposer.CreatePostViewModel
import nl.jovmit.friends.signup.SignUpViewModel
import nl.jovmit.friends.timeline.TimelineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
  single<CoroutineDispatchers> { DefaultDispatchers() }
  single<UserCatalog> { InMemoryUserCatalog() }
  single<PostCatalog> { InMemoryPostCatalog() }
  single { InMemoryUserDataStore("") }
  factory { RegexCredentialsValidator() }
  factory { UserRepository(userCatalog = get()) }
  factory { TimelineRepository(userCatalog = get(), postCatalog = get()) }
  factory { PostRepository(userDataStore = get(), postCatalog = get()) }

  viewModel {
    SignUpViewModel(
      credentialsValidator = get(),
      userRepository = get(),
      dispatchers = get()
    )
  }

  viewModel {
    TimelineViewModel(timelineRepository = get(), dispatchers = get())
  }

  viewModel { CreatePostViewModel(postRepository = get(), dispatchers = get()) }
}