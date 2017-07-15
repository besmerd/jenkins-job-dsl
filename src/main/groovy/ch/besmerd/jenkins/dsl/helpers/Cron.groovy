package ch.besmerd.jenkins.dsl.helpers

enum Cron {

    FIVE_MINUTES('H/5 * * * *', 'Once in five minutes'),
    FIFTEEN_MINUTES('H/15 * * * *', 'Once in fifteen minutes'),
    THIRTY_MINUTES('H/30 * * * *', 'Once every thirty minutes'),
    HOURLY('H * * * *', 'Once an hour'),
    WEEKDAY_TWO_HOURLY('H 8-18/2 * * 1-5', 'Once every two hours weekdays'),
    MIDNIGHT('H H(0-2) * * *', 'Once after midnight'),
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
