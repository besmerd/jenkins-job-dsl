package ch.besmerd.jenkins.dsl

import javaposse.jobdsl.dsl.Job
import spock.lang.Specification

class JobBuilderTest extends Specification {

    JobParentStub jobParent = new JobParentStub()

    def 'simple job'() {
        setup:
        Job job = new JobBuilder(jobParent, 'myJob').with{}

        expect:
        job.name == 'myJob'
    }

}