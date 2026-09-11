package hk.org.ha.iams.termsearch.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Stand-in for hk.org.ha.iams.common.util.CommonDictionaryUtils.
 * Splits on whitespace; does not stem. Matches "ca lung" → ["ca", "lung"].
 */
public final class CommonDictionaryUtils {

    private CommonDictionaryUtils() {
    }

    public static List<String> tokenizeWords(List<String> wordList, boolean unusedStem) {
        List<String> tokens = new ArrayList<>();
        if (wordList == null) {
            return tokens;
        }
        for (String word : wordList) {
            if (TermStrings.isBlank(word)) {
                continue;
            }
            for (String token : word.trim().toLowerCase(Locale.ROOT).split("\\s+")) {
                if (!token.isEmpty()) {
                    tokens.add(token);
                }
            }
        }
        return tokens;
    }
}
