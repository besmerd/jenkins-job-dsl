package ch.besmerd.jenkins.dsl

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.jobs.MavenJob

class MavenJobBuilder extends JobBuilder {

    static final MAVEN_VERSION = 'maven-3.3.9'
    static final MAVEN_GLOBAL_SETTINGS = 'maven-global-settings'
    static final MAVEN_USER_SETTINGS = 'maven-user-settings'

    MavenJobBuilder(DslFactory dslFactory, String jobName) {
        super(dslFactory.mavenJob(jobName))
    }

    MavenJob with(Closure additionalConfig) {
        // Standards for all maven jobs
        job.mavenInstallation(MAVEN_VERSION)
        job.providedGlobalSettings(MAVEN_GLOBAL_SETTINGS)
        job.providedSettings(MAVEN_USER_SETTINGS)

        // Additional configuration
        super.with(additionalConfig)
        job as MavenJob
    }

}
