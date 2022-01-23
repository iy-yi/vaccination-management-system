package cmpe275.vms.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class IdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) throws HibernateException {
        Random rd = new Random();
        long temp;
        temp = rd.nextInt(999999899) + 100;
        System.out.println("generated value");
        System.out.println(temp);
        return String.valueOf(temp);
    }
}
