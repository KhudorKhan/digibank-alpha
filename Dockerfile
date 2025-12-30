# ---------- BUILD STAGE ----------
    FROM maven:3.9.6-eclipse-temurin-17 AS build
    WORKDIR /app
    
    # копируем pom.xml и зависимости
    COPY pom.xml .
    RUN mvn dependency:go-offline
    
    # копируем исходники и собираем jar
    COPY src ./src
    RUN mvn clean package -DskipTests
    
    # ---------- RUN STAGE ----------
    FROM eclipse-temurin:17-jre
    WORKDIR /app
    
    # копируем собранный jar
    COPY --from=build /app/target/*.jar app.jar
    
    # Render использует PORT из env
    EXPOSE 8080
    ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
    