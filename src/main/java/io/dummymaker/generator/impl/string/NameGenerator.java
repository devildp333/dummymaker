package io.dummymaker.generator.impl.string;

import io.dummymaker.bundle.IBundle;
import io.dummymaker.bundle.impl.FemaleNamePresetBundle;
import io.dummymaker.bundle.impl.MaleNamePresetBundle;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Generates names male and female as a string
 *
 * @author GoodforGod
 * @since 26.05.2017
 */
public class NameGenerator extends BigIdGenerator {

    private final IBundle<String> maleBundle = new MaleNamePresetBundle();
    private final IBundle<String> femaleBundle = new FemaleNamePresetBundle();

    @Override
    public String generate() {
        int gender = current().nextInt(100);

        return (gender > 50)
                ? maleBundle.getRandom()
                : femaleBundle.getRandom();
    }
}