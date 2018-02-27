package lucene;

import java.io.Reader;
import java.lang.reflect.Array;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * This is a custom lucene Analyzer class. It serves as a StandardAnalyzer as it
 * uses the standard tokenizer and standard filtering. The constructor
 * initializes the given stopwords.
 * 
 * @author Steve Galgouranas
 */
public class MyAnalyzer extends Analyzer {

	private int maxTokenLength = 255;

	public MyAnalyzer() {
		// super(stopwords);
	}

	public void setMaxTokenLength(int length) {
		maxTokenLength = length;
	}

	public int getMaxTokenLength() {
		return maxTokenLength;
	}

	/**
	 * Adds extra stopwords to the given set. Standard Analyzer's stopwords: a,
	 * an, and, are, as, at, be, but, by, for, if, in, into, is, it, no, not,
	 * of, on, or, such, that, the, their, then, there, these, they, this, to,
	 * was, will, with
	 * 
	 * @param myStopSet
	 *            -> the given set.
	 * @return -> the set after adding extra stopwords.
	 */
	// add extra stopwords
	public CharArraySet addStopwords() {
		CharArraySet myStopSet = new CharArraySet(maxTokenLength, false);
		myStopSet.add("about");
		myStopSet.add("above");
		myStopSet.add("after");
		myStopSet.add("because");
		myStopSet.add("can");
		myStopSet.add("do");
		myStopSet.add("from");
		myStopSet.add("get");
		myStopSet.add("how");
		myStopSet.add("most");
		myStopSet.add("more");
		myStopSet.add("so");
		myStopSet.add("would");
		myStopSet.add("we");
		myStopSet.add("when");
		myStopSet.add("while");
		myStopSet.add("which");
		myStopSet.add("you");
		return myStopSet;
	}

	@Override
	protected TokenStreamComponents createComponents(final String fieldName) {
		final Tokenizer src;
		StandardTokenizer t = new StandardTokenizer(); // the standard tokenizer
														// of lucene
		t.setMaxTokenLength(maxTokenLength);
		src = t;

		TokenStream tok = new StandardFilter(src); // the standard filter of
													// lucene
		tok = new LowerCaseFilter(tok); // lower-case filter
		tok = new StopFilter(tok, addStopwords()); // stopword filter
		return new TokenStreamComponents(src, tok) {
			@Override
			protected void setReader(final Reader reader) {
				int m = MyAnalyzer.this.maxTokenLength;
				((StandardTokenizer) src).setMaxTokenLength(m);
				super.setReader(reader);
			}
		};
	}

}
