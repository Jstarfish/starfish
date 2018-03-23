package priv.starfish.common.base;

import java.util.Map;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * 
 * @author koqiui
 * 
 */
public class UriMatcher {
	public static class MatchResult {
		private String uri;
		private String pattern;
		private Map<String, String> vars;

		public void setUri(String uri) {
			this.uri = uri;
		}

		public String getUri() {
			return uri;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		public String getPattern() {
			return pattern;
		}

		public void setVars(Map<String, String> vars) {
			this.vars = vars;
		}

		public Map<String, String> getVars() {
			return vars;
		}

	}

	//
	private static PathMatcher theSoleMatcher = new AntPathMatcher();

	//
	public static MatchResult doAntMatch(String uriPattern, String uri) {
		if (theSoleMatcher.match(uriPattern, uri)) {
			MatchResult matchResult = new MatchResult();
			matchResult.setUri(uri);
			matchResult.setPattern(uriPattern);
			if (uriPattern.indexOf("{") != -1) {
				matchResult.setVars(theSoleMatcher.extractUriTemplateVariables(uriPattern, uri));
			}
			return matchResult;
		}
		return null;
	}

	private static final String MATCH_ALL = "/**";

	private static boolean subPathMatches(String subpath, String path) {
		int length = subpath.length();
		return path.startsWith(subpath) && (path.length() == length || path.charAt(length) == '/');
	}

	public static boolean matches(String pattern, String uri) {
		if (pattern.equals(MATCH_ALL) || pattern.equals("**")) {
			return true;
		} else {
			// If the pattern ends with {@code /**} and has no other wildcards, then optimize to a sub-path match
			if (pattern.endsWith(MATCH_ALL) && pattern.indexOf('?') == -1
					&& pattern.indexOf("*") == pattern.length() - 2) {
				return subPathMatches(pattern.substring(0, pattern.length() - 3), uri);
			} else {
				return theSoleMatcher.match(pattern, uri);
			}
		}
	}
}
