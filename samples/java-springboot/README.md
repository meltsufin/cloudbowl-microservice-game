Cloud Bowl Sample - Java Spring Boot
------------------------------------

To make changes, edit the `src/main/java/hello/Application.java` file.

Run Locally:
```
./mvnw spring-boot:run
```

[![Run on Google Cloud](https://deploy.cloud.run/button.svg)](https://deploy.cloud.run)


1. Build and push the image to Container Registry.
    ```
    export PROJECT_ID=your-project-id
    export SAMPLE=java-spring 
    mvn compile -Dimage=gcr.io/${PROJECT_ID}/$SAMPLE jib:build
    ```

1. Deploy to Cloud Run.
    ```
    gcloud run deploy $SAMPLE --image gcr.io/$PROJECT_ID/$SAMPLE --platform managed --region us-central1 --memory=512Mi --allow-unauthenticated
    ```
