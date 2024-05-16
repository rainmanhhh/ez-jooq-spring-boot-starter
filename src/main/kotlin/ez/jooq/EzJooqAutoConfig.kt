package ez.jooq

import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(JooqAutoConfiguration::class)
@AutoConfigureAfter(JooqAutoConfiguration::class)
@Configuration
class EzJooqAutoConfig {
  @ConditionalOnBean(JooqConf::class)
  @ConditionalOnMissingBean(Jooq::class)
  @Bean
  fun jooq(jooqConf: JooqConf): Jooq {
    return Jooq(jooqConf)
  }
}
