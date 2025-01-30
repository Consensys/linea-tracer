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

### For one report

To produce a report, you can either

- Run unit or reference tests tasks locally with

```
GOMEMLIMIT=26GiB ./gradlew :arithmetization:test
GOMEMLIMIT=26GiB ./gradlew -Dblockchain=Ethereum referenceBlockchainTests
```

These tasks are finalized by Jacoco test reports

- Or get xml report from the CI pipeline in unit or reference tests actions

Paste the xml report in the following paths :

- for unit tests
  `/arithmetization/build/reports/jacoco/test/jacocoTestReport.xml`
- or for reference tests
  `/referenceBlockchainTests/build/reports/jacoco/jacocoReferenceBlockchainTestsReport/jacocoReferenceBlockchainTestsReport.xml`

### Concatenate two Jacoco reports

Get jacoco exec files from the CI pipeline in unit and reference tests actions

Paste the exec files in the following paths :

- for unit tests
  `/arithmetization/build/jacoco/test.exec`
- for reference tests
  `/referenceBlockchainTests/build/jacoco/referenceBlockchainTests.exec`

Run the jacocoReferenceBlockchainTestsReport task with the right `executionData` uncommented/commented

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

Configure `sonar.coverage.jacoco.xmlReportPaths` to match the path of the xml report you want to view. If you have concatenated two reports, default to the latter path (see comment in task).

Then run `sonar` gradle task in verification group

Go to `localhost:80/projects` in your browser and check the results

## Exit Sonarqube server

```
docker compose down
```
