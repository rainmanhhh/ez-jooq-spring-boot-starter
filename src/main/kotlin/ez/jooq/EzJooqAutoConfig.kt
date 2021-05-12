package ez.jooq

import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(JooqAutoConfiguration::class)
@AutoConfigureAfter(JooqAutoConfiguration::class)
@Configuration
open class EzJooqAutoConfig {
  @ConditionalOnMissingBean(Jooq::class)
  @Bean
  open fun jooq(jooqConf: JooqConf): Jooq {
    return Jooq(jooqConf)
  }
}
