# HaengGu
## Contents
- [Instroduction](#Introduction)
- [Requirements](#Requriements)
- [Installation](#Installation)
- [Deployment](#Deployment)
- [Style Guide](#Style-Guide)
- [Contributing](#Contributing)
- [Maintainers](#Maintainers)

## Introduction
This project's purpose is to create rest api server for HaengGu mobile app.

## Requriements
- [JDK 11](http://jdk.java.net/archive/) or later
- Gradle 4+
- You can also import the code straight into your IDE:
  - [Spring Tool Suite (STS)](https://spring.io/tools)
  - [IntelliJ IDEA](https://www.jetbrains.com/)

## Installation
- Install as you woud normally install a Java JDK 11
- If you use IntelliJ, you must install lombok plugin
  - Click *File > Settings > Plugin* or *Ctrl+Alt+S > Plugin*
  - Search **lombok** and Install

## Deployment
### Configuration
- Configurate Docker environment file.
  - Copy `.env.example` file and set the file name `.env`
  - Enter the mariaDB information
      ```
      POSTGRES_DB={database name}
      POSTGRES_USER={username}
      POSTGRES_PASSWORD={userpassword}
      ```
  - Example
      ```
      POSTGRES_DB=testdatabase
      POSTGRES_USER=testUsername
      POSTGRES_PASSWORD=testPassword
      ```
- Edit application.yml file
  - Correct the part below
      ```
      spring:
          datasource:
              url: jdbc:postgresql://localhost:5432/{database_name}
              username: {user_name}
              password: {user_password}
          ...
      ```
  - Example
      ```
      spring:
          datasource:
              url: jdbc:postgresql://localhost:5432/testdatabase
              username: testUsername
              password: testPassword
          ...
      ```

### Run Docker container for postgreSQL and Spring
```
docker-compose up --build -d
```

### Use Swagger API Docs
After building the project, access `http://localhost:8080/swagger-ui/index.html`

## Style Guide
- I referred to [Google Style Guidelines](https://github.com/JunHoPark93/google-java-styleguide)
- Source file structure
  - A source file consists of, **in order**:
    - License or copyright information, if present
    - Package statement
    - Import statements
    - Exactly one top-level class
- Class Name
  - Use PascalCase
  - Example
    - `public class HelloWorld {}`
- Method Name
  - Use lowerCamelCase
  - Begin with a verb/preposition
  - Example
    - `public void getUserByName(){}`
    - `public void toString(){}`
- Variable Name
  - Use lowerCamelCase
  - Example
    - `private String myName`

## Contributing
1. Create issues about the work.
2. Create a branch on the issue.
3. Commit, push to the created branch.
4. When the work is completed, request a pull request to main branch after rebaseing the main branch.
5. Review the code and merge it.

### Branching
```
ISSUE_NUMBER-description
```
- e.g. Issue 2 related to user authentication.
    ```
    2-user-authentication
    ```

### Commit Message
Referred to [Beom Dev Log](https://beomseok95.tistory.com/328) and [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)

```
<type>[optional scope]: <description>
[optional body]
[optional footer(s)]
```
- Type
  - build, docs, feat, fix, perf, reactor, test
- Example
    ```
    feat: allow provided config object to extend other configs
    ```

## Maintainers
Current maintainers:
- Jiyoon Bak - https://github.com/jiy00nn