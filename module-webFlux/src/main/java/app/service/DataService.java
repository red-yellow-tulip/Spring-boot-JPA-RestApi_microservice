package app.service;

import app.mapper.Mapper;
import app.utils.Request;
import app.utils.Response;
import app.utils.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;

@Service
public class DataService {

    private Mapper mapper = null;

    @Autowired
    public void setStrategyFactory(Mapper mapper) {
        this.mapper = mapper;
    }

    public Response execute(Request request) {

        return Response.builder()
                .date(request.getDate().plusYears(1))
                .strEng(request.getStrEng().toUpperCase())
                .strRus(request.getStrRus().toUpperCase())
                .val(request.getVal().multiply(BigDecimal.valueOf(2)))
                .build();
    }

    public Response execute(String id) {
        return Response.builder()
                .date(LocalDate.now().plusYears(Integer.parseInt(id)))
                .strEng(id)
                .strRus(id)
                .val(BigDecimal.valueOf(Long.parseLong(id)))
                .build();
    }

    public Flux<ResponseDTO> getAll(int count) {
        return Flux
                .range(1,count)
                //.delayElements(Duration.ofMillis(250))
                .map(x -> mapper.toResponseDTO(execute(String.valueOf(x))))
                ;
    }
}
