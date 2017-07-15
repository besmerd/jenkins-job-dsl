package ch.besmerd.jenkins.dsl

import javaposse.jobdsl.dsl.JobParent
import javaposse.jobdsl.dsl.MemoryJobManagement

class JobParentStub extends JobParent {

    JobParentStub() {
        // all tests will only live in memory
        this.setJm(new MemoryJobManagement())
    }

    /**
     * Don't actually do anything
     */
    @Override
    Object run() {
        return null
    }
}