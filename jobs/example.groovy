import ch.besmerd.jenkins.dsl.JobBuilder
import ch.besmerd.jenkins.dsl.helpers.Cron

new JobBuilder(this, 'test').with{
    description('bla')
    cron(Cron.HOURLY)
}
