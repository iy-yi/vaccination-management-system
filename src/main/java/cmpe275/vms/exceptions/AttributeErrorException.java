package cmpe275.vms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AttributeErrorException extends RuntimeException{
    public AttributeErrorException(String message) {
        super(message);
    }
}
