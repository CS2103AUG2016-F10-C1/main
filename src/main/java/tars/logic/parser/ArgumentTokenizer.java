package tars.logic.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import tars.commons.util.StringUtil;

// @@author A0139924W
/**
 * Tokenizes arguments string of the form: {@code preamble <prefix>value <prefix>value ...}<br>
 * e.g. {@code some preamble text /dt today at 3pm /t tag1 /t tag2 /ls} where prefixes are
 * {@code /dt /t}.<br>
 * 1. An argument's value can be an empty string e.g. the value of {@code /ls} in the above
 * example.<br>
 * 2. Leading and trailing whitespaces of an argument value will be discarded.<br>
 * 3. A prefix need to have leading and trailing spaces e.g. the {@code /d today at 3pm /t tag1} in
 * the above example<br>
 * 4. An argument may be repeated and all its values will be accumulated e.g. the value of
 * {@code /t} in the above example.<br>
 */
public class ArgumentTokenizer {
    private static final int EMPTY_SIZE = 0;
    private static final int INVALID_POS = -1;
    private static final int START_INDEX_POS = -1;

    private final Prefix[] prefixes;
    private HashMap<Prefix, String> prefixValueMap;
    private TreeMap<Integer, Prefix> prefixPosMap;
    private String args;

    public ArgumentTokenizer(Prefix... prefixes) {
        this.prefixes = prefixes;
        init();
    }

    public void tokenize(String args) {
        resetExtractorState();
        this.args = args;
        this.prefixPosMap = getPrefixPositon();
        extractArguments();
    }

    private void init() {
        this.args = StringUtil.EMPTY_STRING;
        this.prefixValueMap = new HashMap<Prefix, String>();
        this.prefixPosMap = new TreeMap<Integer, Prefix>();
    }

    private void resetExtractorState() {
        this.prefixValueMap.clear();
    }

    /**
     * Gets all prefix positions from arguments string
     */
    private TreeMap<Integer, Prefix> getPrefixPositon() {
        prefixPosMap = new TreeMap<Integer, Prefix>();

        for (int i = StringUtil.START_INDEX; i < prefixes.length; i++) {
            int curIndexPos = START_INDEX_POS;

            do {
                curIndexPos = args.indexOf(
                        StringUtil.STRING_WHITESPACE + prefixes[i].value,
                        curIndexPos + StringUtil.LAST_INDEX);

                if (curIndexPos >= StringUtil.START_INDEX) {
                    prefixPosMap.put(curIndexPos, prefixes[i]);
                }
            } while (curIndexPos >= StringUtil.START_INDEX);
        }

        return prefixPosMap;
    }

    /**
     * Extracts the option's prefix and arg from arguments string.
     */
    private HashMap<Prefix, String> extractArguments() {
        prefixValueMap = new HashMap<Prefix, String>();

        int endPos = args.length();

        for (Map.Entry<Integer, Prefix> entry : prefixPosMap.descendingMap()
                .entrySet()) {
            Prefix prefix = entry.getValue();
            Integer pos = entry.getKey();

            if (pos == INVALID_POS) {
                continue;
            }

            String arg = args.substring(pos, endPos).trim();
            endPos = pos;

            if (prefixValueMap.containsKey(prefix)) {
                prefixValueMap.put(prefix, prefixValueMap.get(prefix)
                        .concat(StringUtil.STRING_WHITESPACE).concat(arg));
            } else {
                prefixValueMap.put(prefix, arg);
            }

        }

        return prefixValueMap;
    }

    public Optional<String> getValue(Prefix prefix) {
        if (!prefixValueMap.containsKey(prefix)) {
            return Optional.empty();
        }

        return Optional
                .of(getMultipleValues(prefix).get().iterator().next().trim());
    }

    public Optional<Set<String>> getMultipleValues(Prefix prefix) {
        if (!prefixValueMap.containsKey(prefix)) {
            return Optional.empty();
        }
        return Optional
                .of(getMultipleFromArgs(prefixValueMap.get(prefix), prefix));
    }

    public Optional<String> getMultipleRawValues(Prefix prefix) {
        if (!prefixValueMap.containsKey(prefix)) {
            return Optional.empty();
        }

        return Optional.of(prefixValueMap.get(prefix).replaceAll(
                prefix.value + StringUtil.STRING_WHITESPACE,
                StringUtil.EMPTY_STRING));
    }

    public int numPrefixFound() {
        return prefixPosMap.size();
    }

    public Optional<String> getPreamble() {
        if (args.trim().length() == StringUtil.EMPTY_STRING_LENGTH) {
            return Optional.empty();
        }

        if (prefixPosMap.size() == EMPTY_SIZE) {
            return Optional.of(args.trim());
        } else if (prefixPosMap.firstKey() == StringUtil.START_INDEX) {
            return Optional.empty();
        }

        return Optional.of(
                args.substring(StringUtil.START_INDEX, prefixPosMap.firstKey())
                        .trim());
    }

    private Set<String> getMultipleFromArgs(String multipleArguments,
            Prefix prefix) {
        if (multipleArguments.isEmpty()) {
            return Collections.emptySet();
        }

        multipleArguments = multipleArguments.trim();

        // replace first delimiter prefix, then split
        List<String> multipleArgList = Arrays.asList(multipleArguments
                .replaceFirst(prefix.value + StringUtil.STRING_WHITESPACE,
                        StringUtil.EMPTY_STRING)
                .split(StringUtil.STRING_WHITESPACE + prefix.value
                        + StringUtil.STRING_WHITESPACE));

        for (int i = StringUtil.START_INDEX; i < multipleArgList.size(); i++) {
            multipleArgList.set(i, multipleArgList.get(i).trim());
        }

        return new HashSet<>(multipleArgList);
    }

}
