# GitHub Facade

## Getting Started

### 1. Clone the repo

```
git clone https://github.com/zanthrash/github-facade.git
```

### 2. Build and Run the project with Gradle

#### Unauthenticated
```
$ ./gradlew runBuild 
```

GitHub has places a 60 calls per hour rate limit on all non authenticate API calls.  
Running unauthenticated will work but you will quickly bump your head on the limit

#### Authenticated

1. The first thing you need to do is get a User API key from git hub by following these steps: 
    1. log into your GitHub account
    1. Go to Settings -> Application (https://github.com/settings/applications)
    1. Under the 'Personal access token' section click the 'Generate new token' button
    1. Follow the directions provide
    1. When your token is generated copy it somewhere safe.

2. Once you have your token you can execute this command

```
$ ./gradlew runBuild -PapiKey=your_token_here
 
```

Or if you do not like to have your API token in your command history you can add it to the

```
github.token
```

property in the application.yml file in the **build/libs**  directory

and then you can just run 

```
$ ./gradlew 

```



