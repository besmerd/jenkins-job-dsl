package ch.besmerd.jenkins.dsl

import ch.besmerd.jenkins.dsl.JobBuilder.Cron
import javaposse.jobdsl.dsl.Job
import spock.lang.Specification

class JobBuilderTest extends Specification {

    JobParentStub jobParent = new JobParentStub()

    def 'simple job'() {
        setup:
        Job job = new JobBuilder(jobParent, 'myJob').with {}

        expect:
        job.name == 'myJob'
    }

    def 'simple cron job'() {
        setup:
        Job job = new JobBuilder(jobParent, 'myJob').with {
            cron(Cron.DAILY)
        }

        expect:
        with(job.node) {
            triggers.'hudson.triggers.TimerTrigger'
            triggers.size() == 1
            triggers[0].'hudson.triggers.TimerTrigger'.spec.text() == Cron.DAILY.spec
        }

    }

    def 'simple git job'() {
        String url = 'https://example.com/repo'
        String credentialsId = 'credentials'

        setup:
        Job job = new JobBuilder(jobParent, 'myJob').with {
            git(url, credentialsId)
        }

        expect:
        with(job.node) {
            scm.@class == ['hudson.plugins.git.GitSCM']
            scm.userRemoteConfigs.'hudson.plugins.git.UserRemoteConfig'.url.text() == url
            scm.userRemoteConfigs.'hudson.plugins.git.UserRemoteConfig'.credentialsId.text() == credentialsId
            scm.branches.size() == 1
            scm.branches.'hudson.plugins.git.BranchSpec'.name.text() == '**'
        }

    }

    def 'simple svn job'() {
        String url = 'https://example.com/repo'
        String credentialsId = 'credentials'

        setup:
        Job job = new JobBuilder(jobParent, 'myJob').with {
            svn(url, credentialsId)
        }

        expect:
        with(job.node) {
            scm.@class == ['hudson.scm.SubversionSCM']
            scm.locations.size() == 1
            scm.locations[0].'hudson.scm.SubversionSCM_-ModuleLocation'.remote[0].text() == url
            scm.locations[0].'hudson.scm.SubversionSCM_-ModuleLocation'.credentialsId.text() == credentialsId
            scm.workspaceUpdater.@class == ['hudson.scm.subversion.UpdateWithRevertUpdater']
        }

    }

}
