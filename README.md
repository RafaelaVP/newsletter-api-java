# Backend Challenge - Newsletter

This project is a backend system for registration and sending of newsletter emails. It allows the registration of clients and news via Web API. Every day at 08:00, the system checks the records and sends emails with the registered news that have not yet been processed to the registered emails. If a birth date was registered for a client, the system sends a "happy birthday" if the current date is the same as the birth date.

## Technologies Used

- **Backend**: Quarkus (Java)
- **Mailer**: MailHog (for development and testing)
- **Quarkus mailer**

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

 ```shell script tests
./mvnw test
```

```shell script clean dependencies
./mvnw clean install
```
 Start the database and MailHog services with Docker Compose using the command `docker-compose up`


## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Testing the System

To test the system, you can use any HTTP client to make requests to the API. The available endpoints are:

- `POST /clients`: To register a new client.
- `POST /news`: To register a new news.
- `GET /clients`: .
- `GET /news`: List clients.

To test the sending of emails, you can use the MailHog web interface, which is available at `http://localhost:8025`.

#### Email Format:
- **Subject**: News of the Day!
- **Body**:
  - Personalized greeting: "Good morning \<client's name\>!"
  - Birthday message, if applicable: "Happy Birthday!"
  - List of news for the day with:
    - Clickable title (if a link is available).
    - Description.

## Notes

This project uses MailHog to simulate the sending of emails. MailHog is an email testing tool that captures and displays emails sent by the application. This allows us to see exactly what is being sent without actually sending emails to real addresses.


## Contributing

Fork the repository.
Create a new branch: git checkout -b feature/feature-name.
Make your modifications and commit: git commit -m 'Add new feature'.
Push your changes to the remote repository: git push origin feature/feature-name.
Create a new Pull Request on Bitbucket.

## License

Nest is [MIT licensed](LICENSE).

## Contact

If you have any questions or suggestions, please contact:

Rafaela Valerio - rafavalpint@gmail.com
Project on Github: https://github.com/RafaelaVP/newsletter-api-java