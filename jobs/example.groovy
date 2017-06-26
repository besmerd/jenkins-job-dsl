import ch.besmerd.jenkins.dsl.JobBuilder

new JobBuilder(this, 'test').with{
    description('bla')
}
