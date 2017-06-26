job('seed') {
    scm {
        github('besmerd/jenkins-job-dsl')
    }
    steps {
        dsl {
            external('jobs/*.groovy')
        }
    }
}
