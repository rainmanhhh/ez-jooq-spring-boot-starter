package ez.jooq

import org.jooq.conf.Settings
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@AutoConfigureAfter(JooqAutoConfiguration::class)
@ConditionalOnBean(JooqConf::class)
@ConfigurationProperties("ez.jooq")
@Configuration
class EzJooqAutoConfig {
  @NestedConfigurationProperty
  var settings = Settings()

  @ConditionalOnMissingBean(Jooq::class)
  @Bean
  fun jooq(jooqConf: JooqConf): Jooq {
    return Jooq(jooqConf)
  }

  @Bean
  fun jooqSettings() = DefaultConfigurationCustomizer {
    it.setSettings(settings)
  }
}
