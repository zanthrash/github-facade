# GitHub Facade

## Scope

Create a RESTful facade around the GitHub API for the purposes of featching the top 5 repos for and organization based on the number of pull requests the repo has.



## Getting Started

### 1. Clone the repo

```
git clone https://github.com/zanthrash/github-facade.git
```



### 2. Build and Run the project with Gradle

**NOTE: The needs to run against Java 1.7**

#### Unauthenticated
```
$ ./gradlew runBuild 
```

GitHub has places a 60 calls per hour rate limit on all non authenticate API calls.  
Running unauthenticated will work but you will quickly bump your head on the limit

#### Authenticated

Autenticated API user can make 5000 calls per hour.

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


3. Once the app is running navigate to:

    [http://localhost:8080/org/netflix/repo](http://localhost:8080/org/netflix/repos)
    
    This call will get a list of the top 5 Netflix repos sorted in decending order by the number of pull reqests a repo has
    
    Buy default the query will get the top 5 repos. This can be adjust with the **top** param
    
    Get top 2: [http://localhost:8080/org/netflix/repo?top=2](http://localhost:8080/org/netflix/repos?top=2)
    
    Get top 10: [http://localhost:8080/org/netflix/repo?top=10](http://localhost:8080/org/netflix/repos?top=10)
    
    


## Technologies Used

- Spring Boot (http://projects.spring.io/spring-boot)
- RxJava / RxGroovy (https://github.com/ReactiveX/RxJava)
- Spock (https://github.com/spockframework/spock)

