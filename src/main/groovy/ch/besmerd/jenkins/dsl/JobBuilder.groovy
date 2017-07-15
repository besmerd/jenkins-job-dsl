package ch.besmerd.jenkins.dsl

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.helpers.scm.SvnCheckoutStrategy

public class JobBuilder {

    protected Job job

    JobBuilder(DslFactory dslFactory, String jobName) {
        this.job = dslFactory.job(jobName)
    }

    Job with(Closure additionalConfig) {
        // Standards for all jobs
        // TODO

        // Set additional configs
        runClosure(additionalConfig)
        job
    }

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

    private void methodMissing(String name, args) {
        job.invokeMethod(name, (Object[]) args)
    }

    enum Cron {

        FIVE_MINUTES('H/5 * * * *', 'Once in five minutes'),
        FIFTEEN_MINUTES('H/15 * * * *', 'Once in fifteen minutes'),
        HOURLY('H * * * *', 'Once an hour'),
        DAILY('H H * * *', 'Once a day'),
        WEEKLY('H H * * H', 'Once a week'),
        MONTHLY('H H H * *', 'Once a month')

        final String tab, description

        Cron(String tab, String description) {
            this.tab = tab
            this.description = description
        }

        String getSpec() {
            return "$tab  # $description"
        }

    }

    void cron(Cron c) {
        job.triggers {
            cron(c.spec)
        }
    }

    void git(String gitUrl, String credentialsId, String gitBranch = '**') {
        job.scm {
            git {
                remote {
                    credentials(credentialsId)
                    url(gitUrl)
                }
                branch(gitBranch)
            }
        }
    }

    void svn(String svnUrl, String credentialsId, SvnCheckoutStrategy strategy = SvnCheckoutStrategy.UPDATE_WITH_REVERT) {
        job.scm {
            svn {
                location(svnUrl) {
                    credentials(credentialsId)
                }
                checkoutStrategy(strategy)
            }

        }
    }
}
