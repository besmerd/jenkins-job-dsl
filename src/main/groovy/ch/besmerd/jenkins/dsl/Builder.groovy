package ch.besmerd.jenkins.dsl

import javaposse.jobdsl.dsl.Job

abstract class Builder {

    protected Job job

    protected void runClosure(Closure runClosure) {
        // Create clone of closure for threading access
        Closure runClone = runClosure.clone()

        // Set delegate of closure to this builder and
        // only use this builder as the closure delegate
        runClone.delegate = this
        runClone.resolveStrategy = Closure.DELEGATE_ONLY

        // Run closure code
        runClone()
    }

    protected void methodMissing(String name, args) {
        job.invokeMethod(name, (Object[]) args)
    }

}
