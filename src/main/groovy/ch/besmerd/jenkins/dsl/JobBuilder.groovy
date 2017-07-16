package ch.besmerd.jenkins.dsl

import ch.besmerd.jenkins.dsl.Builder
import ch.besmerd.jenkins.dsl.helpers.Cron
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.helpers.scm.SvnCheckoutStrategy

public class JobBuilder extends Builder {

    static final String JDK_VERSION = '1.8.0_latest'

    protected Job job

    JobBuilder(DslFactory dslFactory, String jobName) {
        this.job = dslFactory.job(jobName)
    }

    Job with(Closure additionalConfig) {
        // Standards for all jobs
        job.logRotator {
            daysToKeep(2)
            numToKeep(10)
            artifactDaysToKeep(-1)
            artifactNumToKeep(1)
        }
        job.jdk(JDK_VERSION)

        // Set additional configs
        runClosure(additionalConfig)
        job
    }

    void cron(Cron c) {
        job.triggers {
            cron(c.spec)
        }
    }

    void cronScm(Cron c) {
        job.triggers {
            scm(c.spec)
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
