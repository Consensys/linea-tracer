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

## Launch local Sonarqube server

Run `docker compose up`

Wait for following log

```
sonarqube-1   | 2025.01.23 16:40:21 INFO  app[][o.s.a.SchedulerImpl] SonarQube is operational
```

Change password to not get prompted

```
sonarqube-1   | 2025.01.23 16:40:21 INFO  app[][o.s.a.SchedulerImpl] SonarQube is operational
```

Go to `localhost:80` in your browser and login with the credentials `admin/Adminadmin123*`

## Launch Sonar task with gradle

Run unit and/or reference tests tasks locally with

```
GOMEMLIMIT=26GiB ./gradlew :arithmetization:test
GOMEMLIMIT=26GiB ./gradlew -Dblockchain=Ethereum referenceBlockchainTests
```

Then run `sonar` gradle task in verification group

Go to `localhost:80/projects` in your browser and check the results

## Exit Sonarqube server

```
docker compose down
```
