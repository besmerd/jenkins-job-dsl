package ch.besmerd.jenkins.dls

import javaposse.jobdsl.dsl.Builder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

abstract class JobBuilder {

    protected Job job

    JobBuilder(DslFactory dslFactory, String jobName) {
        this.job = dslFactory.job(jobName)
    }

    Job with(Closure additionalConfig) {
        // Standards for all jobs
        // TODO

        // Additional configs
        runClosure(additionalConfig)
        job
    }

    // Build your own closures to expand Job DSL
    private void runClosure(Closure runClosure) {
        // Create clone of closure for threading access
        Closure runClone = runClosure.clone()

        // Set delegate of closure to this builder and
        // only use this builder as the closure delegate
        runClone.delegate = this
        runClone.resolveStrategy = Closure.DELEGATE_ONLY

        // Run closure code
        runClone()
    }

    private void methodMissing(String name, argument) {
        job.invokeMethod(name, (Object[]) argument)
    }

    enum Cron {
        FIVE_MINUTES('H/5 * * * *'),
        FIFTEEN_MINUTES('H/15 * * * *'),
        HOURLY('H * * * *'),
        HOURLY_OFFICE_HOURS(),
        MIDNIGHT(),
        DAILY('H H * * *'),
        WEEKDAYS('H H(0-2) * * *'),
        WEEKENDS(),
        WEEKLY('H H * * H'),
        MONTHLY('H H H * *')
    }

    void cron(Cron c) {
        job.triggers {
            cron(c.getTab())
        }
    }

    void git(String _url, String _branch, String credentialId) {
        job.scm {
            branch(_branch)
            git {
                remote {
                    credentials(credentialId)
                    url(_url)
                }
            }
        }
    }

    void svn(String _url, String credentialId) {
        job.scm {
            svn {
                locaiton(_url) {
                    credentials(credentialId)
                }
            }

        }
    }
