package app.controller;

import app.controller.dto.RequestDTO;
import app.controller.dto.ResponseDTO;
import app.controller.mapper.Mapper;
import app.controller.service.DataService;
import app.controller.utils.Request;
import app.controller.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/data")
public class DataController {

    private static final Logger log = LoggerFactory.getLogger(DataController.class);

    private Mapper mapper = null;

    private DataService dataService;

    @Autowired
    public void setStrategyFactory(DataService dataService) {
        this.dataService = dataService;
    }

    @Autowired
    public void setStrategyFactory(Mapper mapper) {
        this.mapper = mapper;
    }


    @RequestMapping(value = "/all/{count}", headers="Accept=*/*", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public Flux<ResponseDTO> getAllData( @PathVariable int count) {

        log.info("method GET: /data/all" );

        Flux<ResponseDTO>  res = dataService.getAll(count);

        return  res;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public Mono<ResponseDTO> getData(@PathVariable  String id) {

        log.info("method GET: /data/{id}? id = " + id );

        if (id == null || id.isEmpty())
            return Mono.error(new RuntimeException("id is bad"));

        Response response = dataService.execute(id);

        ResponseDTO responseDTO = mapper.toResponseDTO(response);

        return Mono.just(responseDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public Mono<ResponseDTO> postData(@RequestBody RequestDTO requestTO, @PathVariable String id) {

        log.info("method POST: /data/{id}? id = " + id );

        if (id == null || id.isEmpty())
            return Mono.error(new RuntimeException("id is bad"));

        if (requestTO == null )
            return Mono.error(new RuntimeException("RequestDTO is bad"));

        Request request = mapper.toRequest(requestTO);

        Response response = dataService.execute(request);

        ResponseDTO responseDTO = mapper.toResponseDTO(response);

        return Mono.just(responseDTO);
    }

}
