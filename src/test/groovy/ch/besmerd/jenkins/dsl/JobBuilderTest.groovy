package ch.besmerd.jenkins.dsl

import ch.besmerd.jenkins.dsl.helpers.Cron
import javaposse.jobdsl.dsl.Job
import spock.lang.Specification

class JobBuilderTest extends Specification {

    static final String SCM_URL = 'https://example.com/repository'
    static final String SCM_CREDENTIALS_ID = 'credentials'

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
        setup:
        Job job = new JobBuilder(jobParent, 'myJob').with {
            git(SCM_URL, SCM_CREDENTIALS_ID)
        }

        expect:
        with(job.node) {
            scm.@class == ['hudson.plugins.git.GitSCM']
            scm.userRemoteConfigs.'hudson.plugins.git.UserRemoteConfig'.url.text() == SCM_URL
            scm.userRemoteConfigs.'hudson.plugins.git.UserRemoteConfig'.credentialsId.text() == SCM_CREDENTIALS_ID
            scm.branches.size() == 1
            scm.branches.'hudson.plugins.git.BranchSpec'.name.text() == '**'
        }

    }

    def 'simple svn job'() {
        setup:
        Job job = new JobBuilder(jobParent, 'myJob').with {
            svn(SCM_URL, SCM_CREDENTIALS_ID)
        }

        expect:
        with(job.node) {
            scm.@class == ['hudson.scm.SubversionSCM']
            scm.locations.size() == 1
            scm.locations[0].'hudson.scm.SubversionSCM_-ModuleLocation'.remote[0].text() == SCM_URL
            scm.locations[0].'hudson.scm.SubversionSCM_-ModuleLocation'.credentialsId.text() == SCM_CREDENTIALS_ID
            scm.workspaceUpdater.@class == ['hudson.scm.subversion.UpdateWithRevertUpdater']
        }

    }

}
