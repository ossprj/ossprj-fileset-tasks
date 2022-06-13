package com.ossprj.commons.text.function;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceTokens implements BiFunction<String, Map<String, Object>, String> {

    public static final Pattern pattern = Pattern.compile("\\{(.+?)\\}");

    @Override
    public String apply(final String template, final Map<String, Object> tokenValues) {

        final Matcher matcher = pattern.matcher(template);
        final StringBuffer output = new StringBuffer();

        while (matcher.find()) {
            final Object replacement = tokenValues.get(matcher.group(1));
            if (replacement != null) {
                matcher.appendReplacement(output, replacement.toString());
            } else {
                matcher.appendReplacement(output, "");
            }
        }
        matcher.appendTail(output);
        return output.toString();
    }
}
