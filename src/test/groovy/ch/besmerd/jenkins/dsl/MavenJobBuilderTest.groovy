package ch.besmerd.jenkins.dsl

import javaposse.jobdsl.dsl.jobs.MavenJob
import spock.lang.Specification

class MavenJobBuilderTest extends Specification {

    JobParentStub jobParent = new JobParentStub()

    def 'simple maven job'() {

        def recipients = 'test@example.com'
        def name = 'myJob'

        setup:
        MavenJob job = new MavenJobBuilder(jobParent, name).with {
            mail(recipients)
        }

        expect:
        job.name == name

        with(job.node) {
            reporters.'hudson.maven.reporters.MavenMailer'
            reporters.size() == 1
            reporters[0].'hudson.maven.reporters.MavenMailer'.recipients.text() == recipients
            reporters[0].'hudson.maven.reporters.MavenMailer'.dontNotifyEveryUnstableBuild[0].value() == false
            reporters[0].'hudson.maven.reporters.MavenMailer'.sendToIndividuals[0].value() == false
            reporters[0].'hudson.maven.reporters.MavenMailer'.perModuleEmail[0].value() == true
        }
    }

}