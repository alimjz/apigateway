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
* [Spring Boot Security](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/#build-image)

* [Bucket4J](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#boot-features-security)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#using-boot-devtools)
* [Spring Native Reference Guide](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#boot-features-mongodb)
* [Swagger Open API](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#boot-features-mongodb)
* [Actuator](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#boot-features-mongodb)
* [Jackson](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#boot-features-mongodb)


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
* [REST APIs](): A controller defined in both core and Token modules. these controllers are providing some end points. also core module will invoke token module to verify the token information by API.
````

@RestController("/api/v1/movies")
@Slf4j
public class ApiController {

    ImdbServices imdbServices;

    @Value("k_yn6mhy7i")
    private String API_KEY = "k_yn6mhy7i";
    @Value("http://localhost:9091/api/v1/auth/")
    private String tokenUrl = "http://localhost:9091/api/v1/auth/";


    RestTemplate restTemplate;

    @Autowired
    public void setImdbServices(ImdbServices imdbServices) {
        this.imdbServices = imdbServices;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @GetMapping("/toprank")
    public MovieItems topRankMovies(){
        return imdbServices.topRank250Movies();
    }
    @Operation(summary = "Query top 250 list of IMDB",description = "the token should be valid. otherwise, it return error.",operationId = "topRankByRestTemplate()")
    @PostMapping("/top250")
    public MovieItems topRankByRestTemplate(@RequestHeader("digest") String digest,
                                        @RequestBody RequestMovieTopRank requestMovieTopRank) throws AuthenticationException{
            authorize(digest,requestMovieTopRank);
            String url = "https://imdb-api.com/en/API/Top250Movies/" + API_KEY;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> stringHttpEntity = new HttpEntity<String>(httpHeaders);
            return restTemplate.exchange(url, HttpMethod.GET,stringHttpEntity,MovieItems.class).getBody();

    }

    public void authorize(String digest, RequestMovieTopRank requestMovieTopRank) {
        if (requestMovieTopRank == null || digest.isEmpty())
            throw new AuthenticationException(RequestMovieTopRank.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<RequestTokenAuth> requestTokenAuthHttpEntity = new HttpEntity<RequestTokenAuth>(requestMovieTopRank.getRequestTokenAuth(), httpHeaders);

            ResponseTokenAuth authResponse = restTemplate.exchange(tokenUrl + digest, HttpMethod.POST,
                            requestTokenAuthHttpEntity, ResponseTokenAuth.class)
                    .getBody();
            assert authResponse != null;
            if (authResponse.getResponseCode() != 200) {
                throw new AuthenticationException(RequestMovieTopRank.class);
            }
    }
}

````
* one of the REST API request and success response are as below. first core module invoke token Service to verify token, and then invoke a 3rd party to provide required info.
* Request:
````
{
  "requestTokenAuth": {
    "digest": "5TbXrtN4CxISy7SR9FlXYg==",
    "key": "MTY1NDE2OTA2NzQ2MjoxMTg2Mzg5Mjk0OjEyNTU2Ojk2NDBkNTc4YzZhZmY2YTI1YTJlNTYyYjc1YmQ2NzI4MzVlMWZlNzFiYjQwMGVmMzkxNDUxYzdlMzUwYmU4MDEyZTRjYWE4MWYwOTU0YmNkMjhkMzEzMTJlYWIxNWUxYjc1ODNkZmQ3N2E5ZjkxOTQzOWU1M2ZiMDAzMDY4Y2U2"
  },
  "requestName": "toprank"
}
````
* Response:
````
{
   "items":    [
            {
         "id": "tt0111161",
         "rank": "1",
         "title": "The Shawshank Redemption",
         "fullTitle": "The Shawshank Redemption (1994)",
         "year": "1994",
         "image": "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX128_CR0,3,128,176_AL_.jpg",
         "crew": "Frank Darabont (dir.), Tim Robbins, Morgan Freeman",
         "imDbRating": "9.2",
         "imDbRatingCount": "2595777"
      },
            {
         "id": "tt0068646",
         "rank": "2",
         "title": "The Godfather",
         "fullTitle": "The Godfather (1972)",
         "year": "1972",
         "image": "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_UX128_CR0,1,128,176_AL_.jpg",
         "crew": "Francis Ford Coppola (dir.), Marlon Brando, Al Pacino",
         "imDbRating": "9.2",
         "imDbRatingCount": "1791030"
      },
````

* [Exception Handling](): I did a customization on exception handling. the case is that if the authentication info is wrong, then a customized exception will trigger.

My exceptions Class:
````
public class AuthenticationException extends RuntimeException{
    public AuthenticationException(Class clazz){
        super(AuthenticationException.generateMessage(clazz.getSimpleName()));
    }
    private static String generateMessage(String entity) {
        return StringUtils.capitalize(entity) +
                " Authentication Failed" ;
    }
    private String generateMessageAuth(String entity, boolean authResult) {
        return StringUtils.capitalize(entity) + "Invalid Authentication happened.";
    }
}

public class LimitExceedException extends RuntimeException{
    public LimitExceedException(Class clazz){
        super(LimitExceedException.generateMessage(clazz.getFields()));
    }
    private static String generateMessage(Field[] field) {
        return Arrays.toString(field) + "Maximum Limit Exceeded." ;
    }
}
````

Also below Method implemented in RESTException Handler :
````
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleInvalidToken(AuthenticationException ex) {
        ApiError requestError = new ApiError(NOT_FOUND);
        requestError.setMessage("The Token is Expired or Invalid.");
        requestError.setDebugMessage("Please check the Entered Digest or Key.");
        return buildResponseEntity(requestError);
    }
    
    @ExceptionHandler(LimitExceedException.class)
    protected ResponseEntity<Object> handleLimitExceed(LimitExceedException ex) {
        RequestError requestError = new RequestError(BANDWIDTH_LIMIT_EXCEEDED);
        requestError.setMessage("The Maximum Limit is Exceeded. Try a Minute Later.");
        requestError.setDebugMessage("Please check the Headers to find refill wait time.");
        return buildResponseEntity(requestError);
    }
````
so, in case of invalid token, below Error will return:
````
{"requesterror": {
   "status": "NOT_FOUND",
   "timestamp": "10-06-2022 02:24:00",
   "message": "The Token is Expired or Invalid.",
   "debugMessage": null,
   "subErrors": null
}}
````
and In case of Limit Exceedation :
````
{"requesterror": {
   "status": "BANDWIDTH_LIMIT_EXCEEDED",
   "timestamp": "10-06-2022 09:04:49",
   "message": "The Maximum Limit is Exceeded. Try a Minute Later.",
   "debugMessage": "Please check the Headers to find refill wait time.",
   "subErrors": null
}}
````

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



