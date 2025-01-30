# Launch test coverage with local sonarqube server

## Development Setup

### Install docker

```
brew install docker
```

### Install docker compose plugin

```
brew install docker-compose
```

Add to your ~/.docker/config.json

```
 "cliPluginsExtraDirs": [
     "/opt/homebrew/lib/docker/cli-plugins"
 ]
```

## Produce Jacoco test reports

Sonarqube server needs a third party test coverage report to display a coverage score. This test coverage report is produced by Jacoco plugin.
To produce once, you can either

- Run unit and/or reference tests tasks locally with

```
GOMEMLIMIT=26GiB ./gradlew :arithmetization:test
GOMEMLIMIT=26GiB ./gradlew -Dblockchain=Ethereum referenceBlockchainTests
```

These tasks are finalized by Jacoco test reports

- Or get xml reports from the CI pipeline in unit and/or reference tests actions

Paste the xml report in the following paths :

- for unit tests
  `/arithmetization/build/reports/jacoco/test/jacocoTestReport.xml`
- for reference tests
  `/referenceBlockchainTests/build/reports/jacoco/jacocoReferenceBlockchainTestsReport/jacocoReferenceBlockchainTestsReport.xml`

## Launch local Sonarqube server

Run `docker compose up`

Wait for following log

```
sonarqube-1   | 2025.01.23 16:40:21 INFO  app[][o.s.a.SchedulerImpl] SonarQube is operational
```

Change default password to not get prompted

```
curl -u admin:admin -X POST "http://localhost:80/api/users/change_password?login=admin&previousPassword=admin&password=Adminadmin123*"
```

Go to `localhost:80` in your browser and login with the credentials `admin/Adminadmin123*`

## Launch Sonar task with gradle

Configure `sonar.coverage.jacoco.xmlReportPaths` to match the path of the xml report you want to view.

Then run `sonar` gradle task in verification group

Go to `localhost:80/projects` in your browser and check the results

Note : Sonar task cannot aggregate 2 xml reports - so reports can be viewed one by one

## Exit Sonarqube server

```
docker compose down
```
