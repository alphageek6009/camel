package com.apachecamel.apachecamel;


import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;

import java.util.concurrent.Future;

import static org.apache.camel.builder.ProcessorBuilder.setHeader;

@RestController
public class ApplicaitonResource extends RouteBuilder {

    @Value("${server.port}")
    String serverPort;

    // register the Camel servlet at the root of our context path
   @Value("${remzi.api.path}")
    String contextPath;

    @Autowired
    private StudentService studentService;


    @Override
    public void configure() throws Exception {

        CamelContext context = new DefaultCamelContext();

        // http://localhost:8080/camel/api-doc
        restConfiguration().contextPath(contextPath) //
                .port(serverPort)
                .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Test REST API")
                .apiProperty("api.version", "v1")
                .apiProperty("cors", "true") // cross-site
                .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        rest("/api")
                .id("api-service") // id = identification of the route inside the CamelContext
                .produces(MediaType.APPLICATION_JSON)
                .get("/getStudents").route().setBody(()-> studentService.getStudents());

        rest("/api")
                .post("/addStudent").produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON).type(Student.class).enableCORS(true).route() .process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                studentService.addStudent(exchange.getIn().getBody(Student.class));
            }
        });

        from("direct:invokeRestApi2").description("Test REST Service").id("api-route2").
        to("http://localhost:8082/api/order?orderId=c65b2f5a-e004-493d-b897-f57f67f583d4&bridgeEndpoint=true");

        from("direct:splitter")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        CamelContext context = exchange.getContext();
                        ProducerTemplate producerTemplate = context.createProducerTemplate();

                        // Asynchronous call to internal route
                        String body = exchange.getIn().getBody(String.class);
                        Future<Order> api2 = producerTemplate.asyncRequestBody("direct:invokeRestApi2","", Order.class);
                        System.out.println(api2.isDone());

                        while (api2.isDone()) {
                            System.out.println(api2);
                            // Do rest of the work
                            exchange.getOut().setBody(api2);

                        }

                    }
                });


        rest("/api")
                .id("api-service") // id = identification of the route inside the CamelContext
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .get("/orders").
        to("direct:splitter");



     }
}
