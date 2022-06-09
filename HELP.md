# Project Explanations

The following was discovered as part of building this project:
* Core
* Token

in this project I tried to resolve my assignments, it is still under progress to get better and better. the final result is to create an API gateway that uses Token authorization to provide you some 3rd party web APIs to use.


# Dependencies

### Reference Documentation

Below dependencies are used to create this project.

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/#build-image)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#boot-features-security)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#using-boot-devtools)
* [Spring Native Reference Guide](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#boot-features-mongodb)
* [OAuth2 Client](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#boot-features-security-oauth2-client)
* [OAuth2 Resource Server](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#boot-features-security-oauth2-server)

# Practice Part

The following practices is resolved and tested in this project:

* [Spring-Data-JPA]()
: this part implemented with Mongo DB. in Token Module, tokenImpl and tokenService classes use a repository. repository implements MongoRepository to quey and save the token informations to mongo DB. datasource is refered in properties file.
````
@Data
@Document(collection = "tokens")
@NoArgsConstructor
public class TokenImpl implements Token, Serializable {

    @Id
    private String key;
    private long keyCreationTime;
    private String extendedInformation;

    private String digest;


    public TokenImpl(String key, long creationTime, String extendedInformation, String digest) {
        this.key = key;
        this.keyCreationTime = creationTime;
        this.extendedInformation = extendedInformation;
        this.digest = digest;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public long getKeyCreationTime() {
        return this.keyCreationTime;
    }

    @Override
    public String getExtendedInformation() {
        return extendedInformation;
    }

//commit

    @Override
    public String toString() {
        return "TokenImpl{" +
                "keyTime=" + keyCreationTime +
                ", entryKey='" + extendedInformation + '\'' +
                ", Token=" + digest +
                '}';
    }
}

````
* [Pagination Query]()
: in repository a new custom method defined with @Query to find data based on pagination and it returns a list of tokens based on intered pageable info.
````

public interface TokenRepository extends MongoRepository<TokenImpl,String> {

    List<TokenImpl> findByKey(String key);

    List<TokenImpl> findByDigest(String digest);

    @Query("FROM TOKENS")
    List<TokenImpl> findAllPages(Pageable pagination);
}

````
* [Swagger UI]()
: There are 3 REST service provided in Token module. you can run the project and use below address to check swagger UI to find service documentations.
````
http://localhost:9091/swagger-ui/index.htm
http://localhost:9091/v1/api
````
* [Propertie usage]()
: The properties such as Data base address or application profile are defined in properties file.
````

spring.config.activate.on-profile = dev
spring.output.ansi.enabled=always
spring.security.user.name = ali
spring.security.user.password = 12345
spring.data.mongodb.uri=mongodb://localhost:27017/apigateway
spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect
spring.data.mongodb.database=apigateway
server.port=9091
spring.data.mongodb.repositories.enabled=true
springdoc.api-docs.path=/v1/api
springdoc.swagger-ui.path=/documentation.html

````
* [MapStruct]()
: a map struct plugin implemented in Token module for TokenImp and TokenImpDto to use mapstruct for doing a conversion. generated source is :
````
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-03T15:48:11+0430",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.12 (Oracle Corporation)"
)
@Component
public class TokenDtoConvertorImpl implements TokenDtoConvertor {

    @Override
    public TokenImpDto convertTokenImplToDto(TokenImpl token) {
        if ( token == null ) {
            return null;
        }

        TokenImpDto tokenImpDto = new TokenImpDto();

        tokenImpDto.setDigestive( token.getDigest() );
        tokenImpDto.setKey( token.getKey() );
        tokenImpDto.setKeyCreationTime( token.getKeyCreationTime() );
        tokenImpDto.setExtendedInformation( token.getExtendedInformation() );

        return tokenImpDto;
    }

    @Override
    public List<TokenImpDto> modelsToDtos(List<TokenImpl> tokenList) {
        if ( tokenList == null ) {
            return null;
        }

        List<TokenImpDto> list = new ArrayList<TokenImpDto>( tokenList.size() );
        for ( TokenImpl tokenImpl : tokenList ) {
            list.add( convertTokenImplToDto( tokenImpl ) );
        }

        return list;
    }

    @Override
    public TokenImpl dtoToModel(TokenImpDto tokenImpDto) {
        if ( tokenImpDto == null ) {
            return null;
        }

        TokenImpl tokenImpl = new TokenImpl();

        tokenImpl.setDigest( tokenImpDto.getDigestive() );
        tokenImpl.setKey( tokenImpDto.getKey() );
        tokenImpl.setKeyCreationTime( tokenImpDto.getKeyCreationTime() );
        tokenImpl.setExtendedInformation( tokenImpDto.getExtendedInformation() );

        return tokenImpl;
    }
}
````
* [REST APIs](): A controller defined in both core and Token modules. these controllers are under development to provide token authentication before invoking APIs.



## Container Creation

This project has been configured to let you generate either a lightweight container or a native executable. but I tried to create docker image with Maven, I faced to below error:


To create the image, run the following goal:

```
$ ./mvnw spring-boot:build-image
```
I faced to below error:
````
Builder lifecycle 'creator' failed with status code 51
````

Then, you can run the app like any other container:

```
$ docker run --rm token:0.0.1-SNAPSHOT
```

