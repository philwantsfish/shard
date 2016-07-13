package fish.philwants

import fish.philwants.modules._

object ModuleFactory {
  val modules: Seq[AbstractModule] = Seq (
    FacebookModule,
    LinkedinModule,
    RedditModule,
    TwitterModule,
    InstagramModule,
    GitHubModule,
    BitBucketModule,
    KijijiModule
  )
}
