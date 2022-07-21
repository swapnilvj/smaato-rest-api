# smaato-rest-api

● The service has one GET endpoint - /api/smaato/accept which is able to accept an integer id as a mandatory query parameter and an optional string HTTP endpoint query parameter. 
It should return String “ok” if there were no errors processing the request and “failed” in case of any errors.
    
    ● While designing this part I took into consideration the url, the query params, whether they are mandatory/optional
    ● Tried to use best coding practices like well structured packages and claases, Meaningful and self-explainatory Class-names, method-name.
    ● Consolidated all the constants and used static imports for them
    ● All the common helper methods are extracted out to helper class to keep the service class clean and business logic centric.
    ● A simple application specific exception class is introduced keeping in mind a customized exception handling

● Every minute, the application should write the count of unique requests your application received in that minute to a log file - please use a standard logger . Uniqueness of request is based on the id parameter provided
                
    ● To acheive this goal - I thought the best choice would be to introduce a filter which will intercept each and every incoming request and hence the best place to keep track of request count
    ● There are other possible approaches like using actuator - but I thought for this simple requirement the actuator would be an overkill
    ● To keep track of unique requests in a particular minute - I thought of using a ConcurrentHashMap which will store a map of id vs its uniquCount against the corresponding timestamp.
    ● This way we are able to lookup the map and find out all the unique request-ids and their respective count for that particular minute.
    ● The filter injects timestamp as a request attribute in the intercepted request so that it can be passed on to business logic to fetch unique request count 
    ● To Log the unique request count every minute - the simplest and quickest choice was to use Scheduler along with appropriate cron expression to make sure the logger staement is executed every minute 
    ● For logging I used standard Log4j logger   

● When the endpoint is provided, the service should fire an HTTP GET request to the provided endpoint with count of unique requests in the current minute as a query parameter. Also log the HTTP status code of the response.
        
    ● This flow depends upon whether query param named endpoint is being provided in the request - so a simple condition at controller level will decide if the logic to fire HTTP GET request needs to be invoked.
    ● Used standard UriComponentsBuilder to buld the http get url which is combination of query param endpoint and the unique request count of the respective id in that particular minute. 
    ● RestTemplate created with message-converters for all media-types with a helper method and used to fire the http get request and necessary exception handing is also taken into consideration.
    ● The response http status code is logged as expected.  
    ● This Service logic is covered with simple yet effective Mockito Unit test to verify the expected behaviour. 
     
● REST service which is able to process 10K requests per second.

    ● This one was a tricky part to test on my personal laptop with limited cpu, ram and memory
    ● My thoughts on this part is we will have to bring in a Load Balancer like Zuul along with something for Service Registry like Eureka/Feign to spin up and host multiple instances of the application.  
    ● Due to time constraint and other commitments I could not actually build this infrastructure and configuration on my machine 
        But My approach towards achieveing this goal would be :
        ● we can think about spinning up 20 odd instances of application running independently and registering themselves with Eureka Service Registry.
        ● we can configure server.tomcat.threads.max property to 500 so that all 20 instances will be able to handle 500*20 = 10K requests per second
        ● Zuul load balancer will be responsible to manage the incoming traffic and perform load-balancing based Round-Robin considering very minimal processing required for each request.    
        
● Sample Output from log file:
    
   ● log the HTTP status code of the response
                    
        2022-07-22T00:14:54,442 INFO  [http-nio-8080-exec-5] c.s.s.i.RestServiceImpl: Endpoint Response Status: 200 OK
            


   ● write the count of unique requests your application received in that minute to a log file
            
            2022-07-22T00:15:01,007 INFO  [scheduling-1] c.s.s.i.RequestMetricServiceImpl: Count Of Unique Requests in current minute are :
            2022-07-22T00:15:01,008 INFO  [scheduling-1] c.s.s.i.RequestMetricServiceImpl: 	id: 34 	Count: 4
            2022-07-22T00:15:01,008 INFO  [scheduling-1] c.s.s.i.RequestMetricServiceImpl: 	id: 36 	Count: 2