package io.generator;

import io.generator.populate.GenPopulateFactory;
import io.generator.populate.IPopulateFactory;
import io.generator.produce.GenProduceFactory;
import io.generator.produce.IProduceFactory;

import java.util.List;

/**
 * Default Comment
 *
 * @author @GoodforGod
 * @since 31.05.2017
 */
public class GenPrimeFactory<T> implements IPrimeFactory<T>{

    private IProduceFactory<T> produceFactory;
    private IPopulateFactory<T> populateFactory;

    private GenPrimeFactory() {}

    public GenPrimeFactory(Class<T> primeClass) {
        produceFactory = new GenProduceFactory<>(primeClass);
        populateFactory = new GenPopulateFactory<>();
    }

    @Override
    public T produce() {
        return produceFactory.produce();
    }


    @Override
    public List<T> produce(int amount) {
        return produceFactory.produce(amount);
    }

    @Override
    public T populate(T t) {
        return populateFactory.populate(t);
    }

    @Override
    public List<T> populate(List<T> t) {
        return populateFactory.populate(t);
    }
}
