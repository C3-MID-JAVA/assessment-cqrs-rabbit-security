package ec.com.sofka.mapper;

import ec.com.sofka.data.ErrorLogEntity;
import ec.com.sofka.model.ErrorMessage;
import org.springframework.stereotype.Component;

@Component
public class ErrorLogEntityMapper {

    public static ErrorLogEntity mapToEntity(ErrorMessage errorLog) {
        return new ErrorLogEntity(
                errorLog.getError(),
                errorLog.getProcess(),
                errorLog.getDate()
        );
    }

    public static ErrorMessage mapToModel(ErrorLogEntity errorLogEntity) {
        return new ErrorMessage(
                errorLogEntity.getError(),
                errorLogEntity.getProccess()
        );
    }

}