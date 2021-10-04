package learn.thyme.thyteams.user;

import org.springframework.format.Formatter;
import javax.annotation.Nonnull;
import java.util.Locale;

public class PhoneNumberFormatter implements Formatter<PhoneNumber> {
    @Nonnull
    @Override
    public PhoneNumber parse(@Nonnull String text, @Nonnull Locale locale) {
        return new PhoneNumber(text);
    }

    @Nonnull
    @Override
    public String print(@Nonnull PhoneNumber object, @Nonnull Locale locale) {
        return object.asString();
    }
}
